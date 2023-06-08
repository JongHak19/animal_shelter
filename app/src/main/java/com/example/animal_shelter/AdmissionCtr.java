//////////////이 파일을 컨트롤러로 바꿀 예정//////////////////////////////////

package com.example.animal_shelter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdmissionCtr extends BaseAdapter {
    ArrayList<Admission> admissions = new ArrayList<>();
    private DBhelper dBhelper;
    private Context context;
    private String userID;
    @Override
    public int getCount() {
        return admissions.size();
    }

    @Override
    public Object getItem(int i) {
        return admissions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context c=viewGroup.getContext();
        if(view==null){
            LayoutInflater li = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=li.inflate(R.layout.admissonlistview, viewGroup, false); //root에 붙이지 않는다(보내기 위한 수단).
            // 입소 게시글 리스트로 넘어감
        }

        ImageView dogImage = view.findViewById(R.id.admission_dogImage);
        TextView dogBreed = view.findViewById(R.id.dogBreed);
        TextView dogGender = view.findViewById(R.id.dogGender);
        TextView admissionDate = view.findViewById(R.id.admissionDate);
        TextView resqueArea = view.findViewById(R.id.rescueArea);
        dogImage.setClipToOutline(true); //이미지 둥글게 하기 위해서 모서리 자르기 허용

        Admission admission = admissions.get(i);

        //////////////listview의 textview에 데이터 입력////////////////
        dogImage.setImageDrawable(admission.getImage());
        dogBreed.setText(admission.getBreed());
        dogGender.setText(admission.getGender());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(admission.getCreateDate());
        admissionDate.setText(formattedDate);

        resqueArea.setText(admission.getRescueArea());
        /////////////listview의 textview에 데이터 입력끝//////////////////
        return view;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    /////////////////////////////리스트에 게시글을 넣어서 화면에 표시/////////////////////////////////////////////////
    public void addAllAdmissionsToList(Context context){
        dBhelper = new DBhelper(context);
        Date date;
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        Cursor cursor = db.query("admission", null, null, null, null, null, "admissionDate DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // admission 테이블의 각 컬럼에서 데이터 가져와 admission 객체에 저장

                // 사진 불러와서 리스트에 저장
                @SuppressLint("Range") byte[] bytes = cursor.getBlob(cursor.getColumnIndex("image"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Drawable image = new BitmapDrawable(context.getResources(),bitmap);
                // 다른 정보들 불러와서 리스트에 저장
                @SuppressLint("Range") String breed = cursor.getString(cursor.getColumnIndex("breed"));
                @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex("gender"));
                @SuppressLint("Range") String rescueArea = cursor.getString(cursor.getColumnIndex("resqueArea"));
                @SuppressLint("Range") String createDate = cursor.getString(cursor.getColumnIndex("admissionDate"));
                @SuppressLint("Range") int admissionID = cursor.getInt(cursor.getColumnIndex("admissionID"));
                @SuppressLint("Range") String state = cursor.getString(cursor.getColumnIndex("state"));
                /////게시글 등록한 회원 id 받음////////////
                @SuppressLint("Range") String customerID = cursor.getString(cursor.getColumnIndex("customerID"));

                Admission admission = new Admission();
                admission.setImage(image);
                admission.setCustomerId(customerID);
                admission.setBreed(breed);
                admission.setGender(gender);
                admission.setState(state);
                admission.setAdmissionId(admissionID); //리스트뷰에는 포함x, 하지만 화면 전환할때 넘겨줄 값
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = format.parse(createDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                admission.setCreateDate(date);
                admission.setRescueArea(rescueArea);

                if(!userID.equals(admission.getCustomerId()) && admission.getState().equals("입양대기")){
                    admissions.add(admission);
                }
            } while (cursor.moveToNext());

            cursor.close();
        }
    }

    ////////////////////////////////db에 입소 게시글 등록////////////////////////////////////////
    public void admission_insertData(String userID,String dogName, String breed, String resqueArea,
                                     String age, String weight, String helth, String personality, String habit, String fur,
                                     String caution, Drawable dogimage, String gender) {

        dBhelper = new DBhelper(context);

        SQLiteDatabase db = dBhelper.getWritableDatabase();

        ContentValues admission_values = new ContentValues(); // 컨테이너,, admission_values 객체 생성
        admission_values.put("customerID", userID);
        admission_values.put("admissionDate", getCurrentTimestamp());
        admission_values.put("dogName", dogName);
        admission_values.put("breed", breed);
        admission_values.put("resqueArea", resqueArea);
        admission_values.put("age", age);
        admission_values.put("weight", weight);
        admission_values.put("helth", helth);
        admission_values.put("personality", personality);
        admission_values.put("habit", habit);
        admission_values.put("fur", fur);
        admission_values.put("caution", caution);

        //////////////////drawable 사진을 blob에 저장할 형태로 변경////////////////
        Bitmap bitmap = ((BitmapDrawable) dogimage).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        //////////////////drawable 사진을 blob에 저장할 형태로 변경 끝////////////////
        admission_values.put("image", byteArray);
        admission_values.put("gender", gender);
        admission_values.put("state", "입양대기");

        try {
            long newRowId = db.insertOrThrow("admission", null, admission_values);
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
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date now = new Date();
        return sdf.format(now);
    }

    @SuppressLint("Range")
    public Admission getAdmissionById(int admissionId) {
        Admission admission = null;
        dBhelper = new DBhelper(context);
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        String[] projection = {
                "customerID",
                "admissionDate",
                "dogName",
                "breed",
                "resqueArea",
                "age",
                "weight",
                "helth",
                "personality",
                "habit",
                "fur",
                "caution",
                "image",
                "state",
                "gender"
        };
        String selection = "admissionID = ?";
        String[] selectionArgs = { String.valueOf(admissionId) };
        Cursor cursor = db.query("admission", projection, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String customerID = cursor.getString(cursor.getColumnIndex("customerID"));
            String admissionDate = cursor.getString(cursor.getColumnIndex("admissionDate"));
            String dogName = cursor.getString(cursor.getColumnIndex("dogName"));
            String breed = cursor.getString(cursor.getColumnIndex("breed"));
            String resqueArea = cursor.getString(cursor.getColumnIndex("resqueArea"));
            int age = cursor.getInt(cursor.getColumnIndex("age"));
            int weight = cursor.getInt(cursor.getColumnIndex("weight"));
            String helth = cursor.getString(cursor.getColumnIndex("helth"));
            String personality = cursor.getString(cursor.getColumnIndex("personality"));
            String habit = cursor.getString(cursor.getColumnIndex("habit"));
            String fur = cursor.getString(cursor.getColumnIndex("fur"));
            String caution = cursor.getString(cursor.getColumnIndex("caution"));
            byte[] bytes = cursor.getBlob(cursor.getColumnIndex("image"));
            Drawable image = null;
            if (bytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image = new BitmapDrawable(context.getResources(), bitmap);
            }
            String state = cursor.getString(cursor.getColumnIndex("state"));
            String gender = cursor.getString(cursor.getColumnIndex("gender"));

            admission = new Admission();
            admission.setAdmissionId(admissionId);
            admission.setCustomerId(customerID);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = format.parse(admissionDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            admission.setCreateDate(date);
            admission.setName(dogName);
            admission.setBreed(breed);
            admission.setRescueArea(resqueArea);
            admission.setAge(age);
            admission.setWeight(weight);
            admission.setHealth(helth);
            admission.setPersonality(personality);
            admission.setHabits(habit);
            admission.setFur(fur);
            admission.setCaution(caution);
            admission.setImage(image);
            admission.setState(state);
            admission.setGender(gender);

            cursor.close();
        }
        db.close();
        return admission;
    }
    public boolean validateInput(String dogName, String age, String breed, String rescueArea,
                                 String weight, String health, String personality, String habit,
                                 String fur, String caution, Drawable dogImage, String gender) {
        if (dogName.isEmpty() || age.isEmpty() || breed.isEmpty() || rescueArea.isEmpty()
                || weight.isEmpty() || health.isEmpty() || personality.isEmpty()
                || habit.isEmpty() || fur.isEmpty() || caution.isEmpty()
                || (dogImage == null) || (gender == null)) {
            return true;
        }
        return false;
    }
}
