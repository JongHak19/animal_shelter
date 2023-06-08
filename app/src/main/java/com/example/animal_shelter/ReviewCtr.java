////////////////////////이게 리뷰 컨트롤러 될거임 아마도?///////////////////////////
///////////지금은 content를 받아오지만 나중에는 content의 첫줄만 가져오도록/////////////////////////
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

public class ReviewCtr extends BaseAdapter {
    private DBhelper dBhelper;
    Context context;
    ArrayList<Review> reviews = new ArrayList<>();
    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int i) {
        return reviews.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context c = viewGroup.getContext();
        if(view==null){
            LayoutInflater li = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=li.inflate(R.layout.reviewlistview, viewGroup, false); //root에 붙이지 않는다(보내기 위한 수단).
            // 입소 게시글 리스트로 넘어감
        }

        TextView title = view.findViewById(R.id.reviewtitle);
        TextView summary = view.findViewById(R.id.reviewsummary);
        ImageView dogImage1 = view.findViewById(R.id.dogImage1);

        ////////////이미지 둥글게 하기위해 모서리 자르기 허용////////////////////////////
        dogImage1.setClipToOutline(true);
        ////////////이미지 둥글게 하기위해 모서리 자르기 허용 끝////////////////////////////

        Review review = reviews.get(i);

        //////////////listview의 textview에 데이터 입력////////////////
        title.setText(review.getTitle());
        summary.setText(review.getContent());
        dogImage1.setImageDrawable(review.getDogImage1());

        /////////////listview의 textview에 데이터 입력끝//////////////////
        return view;
    }
    public void setContext(Context context) {
        this.context = context;
    }
    public void addAllReviewsToList(Context context){
        dBhelper = new DBhelper(context);
        Date date;
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        Cursor cursor = db.query("review", null, null, null, null, null, "reviewDate DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // admission 테이블의 각 컬럼에서 데이터 가져와 admission 객체에 저장

                // 사진 불러와서 리스트에 저장
                @SuppressLint("Range") byte[] bytes = cursor.getBlob(cursor.getColumnIndex("image"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Drawable image = new BitmapDrawable(context.getResources(),bitmap);
                // 다른 정보들 불러와서 리스트에 저장
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") int reviewID = cursor.getInt(cursor.getColumnIndex("reviewID"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                // 밑에 코드는 후기 내용의 첫줄만 꺼내오기 위함
                content = content.split("\n")[0];
                @SuppressLint("Range") String createDate = cursor.getString(cursor.getColumnIndex("reviewDate"));

                Review review = new Review();
                review.setReviewId(reviewID);
                review.setDogImage1(image);
                review.setContent(content);
                review.setTitle(title);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = format.parse(createDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                review.setCreateDate(date);

                reviews.add(review);
            } while (cursor.moveToNext());

            cursor.close();
        }
    }

    //////////////////////////////////리뷰 게시글 등록////////////////////////////////////
    public void review_insertData (String userID, String title, int adoptionID, String content, Drawable dogImage1){
        dBhelper = new DBhelper(context);

        SQLiteDatabase db = dBhelper.getWritableDatabase();
        ContentValues review_values = new ContentValues();
        review_values.put("customerID",userID);
        review_values.put("adoptionID",adoptionID);
        review_values.put("content",content);
        review_values.put("reviewDate", getCurrentTimestamp());
        review_values.put("title",title);

        Bitmap bitmap = ((BitmapDrawable) dogImage1).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        review_values.put("image",byteArray);

        try {
            long newRowId = db.insertOrThrow("review", null, review_values);
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
}
