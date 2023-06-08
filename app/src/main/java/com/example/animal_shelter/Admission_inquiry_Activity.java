package com.example.animal_shelter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;

public class Admission_inquiry_Activity extends AppCompatActivity {
    AdmissionCtr admissionCtr;
    Admission admission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission_inquiry);

        /////////////////////////////////커스텀 타이틀바 ////////////////////////////////
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getSupportActionBar().setCustomView(R.layout.toolbar_layout);
        //////////////////////////////////커스텀 타이틀바 끝////////////////////////

        //////로그인한 유저ID////////////
        customer app = (customer) getApplication();
        String userID = app.getId();

        // 뷰 초기화
        ImageView dogImage = findViewById(R.id.imageView2);
        TextView breed = findViewById(R.id.textView6);
        TextView gender = findViewById(R.id.textView7);
        TextView admissionDate = findViewById(R.id.textView8);
        TextView resqueArea = findViewById(R.id.textView9);
        TextView name = findViewById(R.id.textView19);
        TextView age = findViewById(R.id.textView20);
        TextView weight = findViewById(R.id.textView21);
        TextView health = findViewById(R.id.textView22);
        TextView fur = findViewById(R.id.textView23);
        TextView habit = findViewById(R.id.textView24);
        TextView personality = findViewById(R.id.textView25);
        TextView caution = findViewById(R.id.textView26);

        admissionCtr = new AdmissionCtr();
        admissionCtr.setContext(this);
        admissionCtr.setUserID(userID);

        // 이전 액티비티로부터 전달된 값 받기
        Intent intent = getIntent();
        int admissionId = intent.getIntExtra("key", 0); // 전달된 값(key)을 "key"로 참조하고, 기본값으로 0을 설정합니다.

        admission = admissionCtr.getAdmissionById(admissionId);

        /////////////////화면 데이터 변경///////////////////////////
        dogImage.setImageDrawable(admission.getImage());
        breed.setText(admission.getBreed());
        gender.setText(admission.getGender());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(admission.getCreateDate());
        admissionDate.setText(formattedDate);
        resqueArea.setText(admission.getRescueArea());
        name.setText(admission.getName());
        age.setText(admission.getAge() + "살");
        weight.setText(admission.getWeight()+"kg");
        health.setText(admission.getHealth());
        fur.setText(admission.getFur());
        habit.setText(admission.getHabits());
        personality.setText(admission.getPersonality());
        caution.setText(admission.getCaution());

        //////////////////플로팅 버튼 클릭////////////////////////
        FloatingActionButton adoptionBtn = this.findViewById(R.id.addAdoptionFBtn);
        adoptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Adoption_Activity.class);
                intent.putExtra("key", admissionId);
                startActivity(intent);
            }
        });
        //////////////////플로팅 버튼 클릭 끝////////////////////////
    }
}


// DBHelper 또는 데이터베이스 관리 클래스 인스턴스 생성

// 데이터베이스에서 사용자 이름 검색

// 텍스트뷰에 사용자 이름 설정
