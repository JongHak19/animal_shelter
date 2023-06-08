package com.example.animal_shelter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Admission_Activity extends AppCompatActivity {
    private EditText breed; // 품종
    private EditText gender; // 성별
    private EditText resqueArea; // 구조
    private EditText name; // 이름
    private EditText age; // 나이
    private EditText weight; // 몸무게
    private EditText health; // 건강
    private EditText personality; // 성격
    private EditText habit; // 배변 습관
    private EditText fur; // 털빠짐
    private EditText caution; // 주의사항
    private ImageView dogImage;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private AdmissionCtr admissionCtr;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission);
        admissionCtr = new AdmissionCtr();

        /////////////////////////////////커스텀 타이틀바 ////////////////////////////////
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getSupportActionBar().setCustomView(R.layout.toolbar_layout);
        //////////////////////////////////커스텀 타이틀바 끝////////////////////////

        ///////////로그인한 유저 ID/////////////////
        customer app = (customer) getApplication();
        userID = app.getId();

        // Button과 ImageView를 초기화하고 클릭 이벤트를 설정
        Button btnSelectImage = findViewById(R.id.image_button);
        dogImage = findViewById(R.id.imageView_in);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            dogImage.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        btnSelectImage.setOnClickListener(v -> openGallery());

        //등록 버튼 클릭시 액티비티 전환 ; 추후에 확인 메시지 출력 후 메인 화면으로 전환하기
        Button Registration_btn = findViewById(R.id.Registration_Button);
        Registration_btn.setOnClickListener(view -> {
            insertDataIntoDatabase();
        });
        ////////////////취소 버튼/////////////////////
        Button CencleBtn = findViewById(R.id.Cancel_Button);
        CencleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 데이터 삽입 예시

        breed = findViewById(R.id.editText2); // 품종
        gender = findViewById(R.id.editText3); // 성별
        resqueArea = findViewById(R.id.editText4); // 구조 지역
        name = findViewById(R.id.editText5); // 이름
        age = findViewById(R.id.editText6); // 나이
        weight = findViewById(R.id.editText7); // 몸무게
        health = findViewById(R.id.editText8); // 건강
        personality = findViewById(R.id.editText9); // 성격
        habit = findViewById(R.id.editText10); // 배변 습관
        fur = findViewById(R.id.editText11); // 털빠짐
        caution = findViewById(R.id.editText12); // 주의사항
        dogImage = findViewById(R.id.imageView_in); // 이미지
    }
    private void insertDataIntoDatabase() {
        String dogName = name.getText().toString(); // 이름
        String breed = this.breed.getText().toString(); // 품종
        String rescueArea = resqueArea.getText().toString(); // 구조 지역
        String age = this.age.getText().toString();    // 나이
        String weight = this.weight.getText().toString(); // 몸무게
        String health = this.health.getText().toString(); // 건강
        String personality = this.personality.getText().toString(); // 성격
        String habit = this.habit.getText().toString(); // 배변 습관
        String fur = this.fur.getText().toString(); // 털빠짐
        String caution = this.caution.getText().toString(); // 주의사항
        Drawable dogImage = this.dogImage.getDrawable(); // 사진DD
        String gender = this.gender.getText().toString(); // 성별

        if (admissionCtr.validateInput(dogName, age, breed, rescueArea, weight, health, personality, habit, fur, caution, dogImage, gender)) {
            // 필수 정보가 비어있을 경우 에러 메시지 출력
            Toast.makeText(this, "모든 정보를 입력하세요.", Toast.LENGTH_SHORT).show();
        } else {
            admissionCtr.setContext(this);
            admissionCtr.admission_insertData(userID, dogName, breed, rescueArea, age, weight, health,
                    personality, habit, fur, caution, dogImage, gender);
            // 저장 완료 메시지 출력
            Toast.makeText(this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("page", "Adoption");
            startActivity(intent);
        }
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);   // Intent.ACTION_PICK 액션을 사용하여 갤러리를 호출
        imagePickerLauncher.launch(intent);
    }
}