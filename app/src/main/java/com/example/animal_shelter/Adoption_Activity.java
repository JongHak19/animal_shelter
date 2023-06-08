package com.example.animal_shelter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Adoption_Activity extends AppCompatActivity {
    private EditText AdoptPost_UserNameText;
    private EditText AdoptPost_UserPhoneText;
    private EditText AdoptPost_UserAdressText;
    private EditText AdoptPost_UserReasonEdit;
    private Button AdoptPost_OkBtn; // 입양 신청서 확인버튼
    private Button AdoptPost_CancelBtn; // 입양 신청서 취소버튼
    private Button approveBtn;
    private Button rejectBtn;
    private Button okBtn;
    private AdoptionCtr adoptionCtr;
    private String userID;
    private int admissionID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoption);
        adoptionCtr = new AdoptionCtr();
        adoptionCtr.setContext(this);

        /////////////////////////////////커스텀 타이틀바 ////////////////////////////////
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getSupportActionBar().setCustomView(R.layout.toolbar_layout);
        //////////////////////////////////커스텀 타이틀바 끝////////////////////////

        ///////////로그인한 유저 ID/////////////////
        customer app = (customer) getApplication();
        userID = app.getId();

        ////////////////////////입소 게시글 ID 인텐트//////////////////////
        Intent intent = getIntent();
        admissionID = intent.getIntExtra("key", 0);

        /////////////////////////////나의 입양 신청, 나에게 온 입양 신청 에 따른 뷰 처리///////////////////////
        Intent intent1 = getIntent();
        String value = intent1.getStringExtra("value");
        String reason = intent1.getStringExtra("reason");
        String name = intent1.getStringExtra("name");
        String phone = intent1.getStringExtra("phone");
        String address = intent1.getStringExtra("address");
        int adoptionID = intent1.getIntExtra("adoptionID",0);

        AdoptPost_UserNameText = findViewById(R.id.AdoptPost_UserNameEdit); // 입양 신청서 이름 텍스트
        AdoptPost_UserPhoneText = findViewById(R.id.AdoptPost_UserPhoneEdit); // 입양 신청서 연락처 텍스트
        AdoptPost_UserAdressText = findViewById(R.id.AdoptPost_UserAdressEdit); // 입양 신청서 주소 텍스트
        AdoptPost_UserReasonEdit = findViewById(R.id.AdoptPost_UserReasonEdit); // 입양 신청서 입양 이유 텍스트

        if(name == null || phone == null || address == null){
            name = app.getName();
            address = app.getAddress();
            phone = app.getPhone();
        }
        AdoptPost_UserNameText.setText(name);
        AdoptPost_UserAdressText.setText(address);
        AdoptPost_UserPhoneText.setText(phone);

        AdoptPost_UserNameText.setEnabled(false);
        AdoptPost_UserPhoneText.setEnabled(false);
        AdoptPost_UserAdressText.setEnabled(false);

        AdoptPost_OkBtn = findViewById(R.id.AdoptPost_OkBtn); // 확인버튼
        AdoptPost_CancelBtn = findViewById(R.id.AdoptPost_CancelBtn); // 취소버튼
        approveBtn = findViewById(R.id.approveBtn);  //승인버튼
        rejectBtn = findViewById(R.id.rejectBtn);    //거절버튼
        okBtn = findViewById(R.id.okBtn);

        if(value !=null) {
            if (value.equals("MyAdoption")) {
                AdoptPost_UserReasonEdit.setText(reason);
                System.out.println(reason);
                AdoptPost_UserReasonEdit.setEnabled(false);
                AdoptPost_OkBtn.setVisibility(View.GONE);
                AdoptPost_CancelBtn.setVisibility(View.GONE);

                okBtn.setVisibility(View.VISIBLE);
            }
            if (value.equals("YourAdoption")) {
                AdoptPost_UserReasonEdit.setText(reason);

                AdoptPost_UserReasonEdit.setEnabled(false);
                AdoptPost_OkBtn.setVisibility(View.GONE);
                approveBtn.setVisibility(View.VISIBLE);

                AdoptPost_CancelBtn.setVisibility(View.GONE);
                rejectBtn.setVisibility(View.VISIBLE);
            }
        }
        /////////////////등록 버튼/////////////////
        AdoptPost_OkBtn.setOnClickListener(view -> {
            insertDataIntoDatabase();
        });
        ///////////////////조회 확인 버튼////////////////
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ////////////////////취소 버튼///////////////////////
        AdoptPost_CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ///////////////////승인 버튼/////////////////////////
        approveBtn.setOnClickListener(view -> {
            adoptionCtr.updateAdoptionState(adoptionID, "입양완료");
        });
        ///////////////////거절 버튼//////////////////////////
        rejectBtn.setOnClickListener(view -> {
            adoptionCtr.updateAdoptionState(adoptionID, "입양거절");
        });
    }

    private void insertDataIntoDatabase() {
        String userName = AdoptPost_UserNameText.getText().toString(); // 이름
        String phone = AdoptPost_UserPhoneText.getText().toString(); // 전화번호
        String adress = AdoptPost_UserAdressText.getText().toString(); // 주소
        String reason = AdoptPost_UserReasonEdit.getText().toString();    // 입양 이유


        if (userName.isEmpty() || phone.isEmpty() || adress.isEmpty() || reason.isEmpty()) {
            // 필수 정보가 비어있을 경우 에러 메시지 출력
            Toast.makeText(this, "모든 정보를 입력하세요.", Toast.LENGTH_SHORT).show();
        } else {
            adoptionCtr.setContext(this);
            adoptionCtr.adoption_insertData(userID, reason, admissionID);
            // 저장 완료 메시지 출력
            Toast.makeText(this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("page", "MyPage");
            startActivity(intent);
        }
    }
}