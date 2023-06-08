package com.example.animal_shelter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdoptionCtr extends RecyclerView.Adapter<AdoptionCtr.ViewHolder>  {

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected ImageView adoption_dogImage;
        protected TextView adoption_dogName;
        protected TextView adoptionDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            adoption_dogImage = itemView.findViewById(R.id.adoption_dogImage);
            adoption_dogName = itemView.findViewById(R.id.adoption_dogName);
            adoptionDate = itemView.findViewById(R.id.adoptionDate);

            adoption_dogImage.setClipToOutline(true);

            itemView.setOnClickListener(this);
        }
        ////클릭 이벤트 처리/////////////
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                Adoption adoption = adoptions.get(position);
                Intent intent = new Intent(context, Adoption_Activity.class);
                intent.putExtra("reason",adoption.getReason());
                intent.putExtra("adoptionID",adoption.getAdoptionId());

                // 새로운 DBhelper를 생성합니다.
                dBhelper = new DBhelper(context);
                SQLiteDatabase db = dBhelper.getReadableDatabase();

                // customerId를 이용하여 customer의 정보를 가져옵니다.
                String[] projection = {"name", "address", "phone"};
                String selection = "ID=?";
                String[] selectionArgs = {adoption.getCustomerId()};
                Cursor cursor = db.query("customer", projection, selection, selectionArgs, null, null, null);

                if (cursor.moveToFirst()) {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                    @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));
                    @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("phone"));

                    // 이제 name, address, phone을 인텐트로 전달할 수 있습니다.
                    intent.putExtra("name", name);
                    intent.putExtra("address", address);
                    intent.putExtra("phone", phone);
                }

                cursor.close();
                db.close();
                if(adoption.getCustomerId().equals(userID)){
                    intent.putExtra("value", "MyAdoption");
                }
                else{
                    intent.putExtra("value","YourAdoption");
                }
                context.startActivity(intent);
            }
        }
    }

    private List<Adoption> adoptions;
    private DBhelper dBhelper;
    private String userID;
    private Context context;
    public List<Adoption> getAdoptions() {
        return adoptions;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private int recycleviewId;  //중요함!! 나에게온 입양신청(1) 리스트, 내가한 입양신청 리스트(2) 구별해주는 ID 값
    public AdoptionCtr(List<Adoption> adoptions, int recycleviewId){
        this.adoptions = adoptions;
        this.recycleviewId = recycleviewId;
    }
    public AdoptionCtr(){};

    @NonNull
    @Override
    public AdoptionCtr.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adoptionlistview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Adoption adoption = adoptions.get(position);
        holder.adoption_dogImage.setImageDrawable(adoption.getAdmissionDog());
        holder.adoption_dogName.setText(adoption.getDogName());

        if(recycleviewId == 1) {
            ////////////////날짜 데이터 문자열로/////////////////////////
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(adoption.getCreateDate());
            ////////////////날짜 데이터 문자열로 끝/////////////////////////

            holder.adoptionDate.setText(formattedDate);
        }
        else {
            holder.adoptionDate.setText(adoption.getState());
        }
    }

    @Override
    public int getItemCount() {
        return adoptions.size();
    }
    public void addAllAdoptionsToList(Context context){
        dBhelper = new DBhelper(context);
        Date date;
        SQLiteDatabase db = dBhelper.getReadableDatabase();

        //////////db를 join하여 읽어옴 (외래키를 사용하여 다른 테이플 참조를 위해)///////////
        String query = "SELECT adoption.*, admission.image, admission.dogName, admission.customerID AS admission_customerID, adoption.customerID AS adoption_customerID " +
                "FROM adoption " +
                "LEFT JOIN admission ON adoption.admissionID = admission.admissionID " +
                "ORDER BY adoption.adoptionDate DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // adoption 테이블의 각 컬럼에서 데이터 가져와 adoption 객체에 저장

                // 사진 불러와서 리스트에 저장
                @SuppressLint("Range") byte[] bytes = cursor.getBlob(cursor.getColumnIndex("image"));
                Bitmap bitmap = null;
                if(bytes != null){
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                }
                Drawable image = new BitmapDrawable(context.getResources(),bitmap);

                // 다른 정보들 불러와서 리스트에 저장
                @SuppressLint("Range") int adoptionID = cursor.getInt(cursor.getColumnIndex("adoptionID"));
                @SuppressLint("Range") String state = cursor.getString(cursor.getColumnIndex("state"));
                @SuppressLint("Range") String dogName = cursor.getString(cursor.getColumnIndex("dogName"));
                @SuppressLint("Range") String adoption_customerID = cursor.getString(cursor.getColumnIndex("adoption_customerID"));
                @SuppressLint("Range") String admission_customerID = cursor.getString(cursor.getColumnIndex("admission_customerID"));
                @SuppressLint("Range") String createDate = cursor.getString(cursor.getColumnIndex("adoptionDate"));
                @SuppressLint("Range") String reason = cursor.getString(cursor.getColumnIndex("reason"));

                Adoption adoption = new Adoption();
                adoption.setAdoptionId(adoptionID);
                adoption.setState(state);
                adoption.setCustomerId(adoption_customerID);
                adoption.setDogName(dogName);
                adoption.setReason(reason);
                adoption.setAdmissionDog(image);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = format.parse(createDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                adoption.setCreateDate(date);
                System.out.println(adoption_customerID);

                ////////////////내가 한 입양 신청 /////////////////////////
                if(recycleviewId == 2 && adoption_customerID.equals(userID)){
                    System.out.println("내가한 입양신청");
                    adoptions.add(adoption);
                }
                //////////////////////////////////////////////////////////
                //////////////////나에게온 입양 신청///////////////////////
                else if (recycleviewId == 1 && admission_customerID != null && admission_customerID.equals(userID) && adoption.getState().equals("입양대기")) {
                    System.out.println(admission_customerID);
                    adoptions.add(adoption);

                }
                //////////////////////////////////////////////////////////
            } while (cursor.moveToNext());
            cursor.close();
        }
        //////////db를 join하여 읽어옴 (외래키를 사용하여 다른 테이플 참조를 위해) 끝///////////
    }
    //////////////////로그인한 유저 id fragment에서 받아옴///////////////////////////////
    public void setUserID(String userID) {
        this.userID = userID;
    }
    ///////////////////////////////////입양 신청 등록////////////////////////////////////////
    public void adoption_insertData(String userID, String reason, int admissionID) {
        dBhelper = new DBhelper(context);
        SQLiteDatabase db = dBhelper.getWritableDatabase();
        ContentValues adoption_values = new ContentValues();
        adoption_values.put("customerID",userID);
        adoption_values.put("admissionID",admissionID);
        adoption_values.put("reason",reason);
        adoption_values.put("adoptionDate", getCurrentTimestamp());
        adoption_values.put("state","입양대기");

        try {
            long newRowId = db.insertOrThrow("adoption", null, adoption_values);
            if (newRowId == -1) {
                // 데이터 입력 실패
                showToast("데이터 입력에 실패했습니다.");
            } else {
                // 데이터 입력 성공
                showToast("데이터가 성공적으로 입력되었습니다.");
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            // 오류 처리 로직 추가
            showToast("데이터 입력 중 오류가 발생했습니다.");
        } finally {
            db.close();
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    /////////////////////////현재 날짜 만들기//////////////////////////
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date now = new Date();
        return sdf.format(now);
    }


    ///////////////////////////////승인 메소드////////////////////////////
    public void updateAdoptionState(int adoptionID, String state) {
        dBhelper = new DBhelper(context);
        SQLiteDatabase db = dBhelper.getWritableDatabase();

        ContentValues adoption_values = new ContentValues();
        adoption_values.put("state", state);

        String selection = "adoptionID=?";
        String[] selectionArgs = { String.valueOf(adoptionID) };
        System.out.println(adoptionID);
        int count = db.update(
                "adoption",
                adoption_values,
                selection,
                selectionArgs);
        if(state.equals("입양완료")) {
            String admissionIdQuery = "SELECT admissionID FROM adoption WHERE adoptionID=?";
            Cursor cursor = db.rawQuery(admissionIdQuery, selectionArgs);
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int admissionID = cursor.getInt(cursor.getColumnIndex("admissionID"));

                ContentValues admission_values = new ContentValues();
                admission_values.put("state", "입양완료");

                String selection_admission = "admissionID=?";
                String[] selectionArgs_admission = {String.valueOf(admissionID)};
                int count_admission = db.update(
                        "admission",
                        admission_values,
                        selection_admission,
                        selectionArgs_admission);

                // 입양이 완료된 후, 해당 admissionID를 가진 다른 adoption들의 state를 "입양거절"로 변경합니다.
                ContentValues otherAdoption_values = new ContentValues();
                otherAdoption_values.put("state", "입양거절");

                String selection_otherAdoption = "admissionID=? AND adoptionID!=?";
                String[] selectionArgs_otherAdoption = {String.valueOf(admissionID), String.valueOf(adoptionID)};
                db.update(
                        "adoption",
                        otherAdoption_values,
                        selection_otherAdoption,
                        selectionArgs_otherAdoption);

                if (count == 0) {
                    showToast("데이터 업데이트에 실패했습니다.");
                } else {
                    showToast("데이터가 성공적으로 업데이트되었습니다.");
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("page", "MyPage");
                    context.startActivity(intent);
                }
            }
        }
        if (count == 0) {
            showToast("데이터 업데이트에 실패했습니다.");
        } else {
            showToast("데이터가 성공적으로 업데이트되었습니다.");
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("page", "MyPage");
            context.startActivity(intent);
        }
        db.close();
    }

}
