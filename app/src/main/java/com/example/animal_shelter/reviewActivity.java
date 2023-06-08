package com.example.animal_shelter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class reviewActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView imgView;
    static final String TAG = "reviewActivity";
    ActivityResultLauncher<Intent> imagePickerLauncher;

    private ArrayList<Integer> adoptionIDs;
    private ArrayList<String> dogNames;
    private ArrayList<String> createDates;
    private EditText title;
    private EditText content;
    private ImageView dogImage;
    private Button image_button;
    private Button registbutton;
    private ReviewCtr reviewCtr;
    private String userID;
    private Spinner spinner;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ///////////// 뷰 초기화//////////////////////
        title = findViewById(R.id.editTextText);
        content = findViewById(R.id.editTextTextMultiLine);
        dogImage = findViewById(R.id.imageView01);
        image_button = findViewById(R.id.image_button);
        registbutton = findViewById(R.id.registbutton);

        ///////////로그인한 유저 ID/////////////////
        customer app = (customer) getApplication();
        userID = app.getId();

        /////////컨트롤러 생성///////////////////////
        reviewCtr = new ReviewCtr();
        /////////////////////////////////커스텀 타이틀바 ////////////////////////////////
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getSupportActionBar().setCustomView(R.layout.toolbar_layout);
        //////////////////////////////////커스텀 타이틀바 끝////////////////////////

        // Intent에서 adoptionIDs 값을 받아옴
        Intent intent = getIntent();
        adoptionIDs = intent.getIntegerArrayListExtra("adoptionIDs");
        dogNames = intent.getStringArrayListExtra("dogNames");
        createDates = intent.getStringArrayListExtra("createDates");
        spinner = findViewById(R.id.spinner);

        // 어댑터를 생성하여 스피너에 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 0, new ArrayList<String>()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.spinner_item_layout, parent, false);
                }

                TextView textView1 = convertView.findViewById(R.id.spinnertextView1);
                TextView textView2 = convertView.findViewById(R.id.spinnertextView2);
                TextView textView3 = convertView.findViewById(R.id.spinnertextView3);

                textView1.setVisibility(View.GONE);

                String itemText = getItem(position);
                String[] parts = itemText.split(" - ");
                textView1.setText(parts[0]);
                textView2.setText(parts[1]);
                textView3.setText(parts[2]);

                return convertView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.spinner_item_layout, parent, false);
                }

                TextView textView1 = convertView.findViewById(R.id.spinnertextView1);
                TextView textView2 = convertView.findViewById(R.id.spinnertextView2);
                TextView textView3 = convertView.findViewById(R.id.spinnertextView3);

                textView1.setVisibility(View.GONE);

                String itemText = getItem(position);
                String[] parts = itemText.split(" - ");
                textView1.setText(parts[0]);
                textView2.setText(parts[1]);
                textView3.setText(parts[2]);

                return convertView;
            }
        };

        // 어댑터에 데이터를 추가합니다
        for (int i = 0; i < adoptionIDs.size(); i++) {
            String itemText = adoptionIDs.get(i) + " - " + dogNames.get(i) + " - " + createDates.get(i);
            adapter.add(itemText);
        }

        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        spinner.setAdapter(adapter);

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

        ////////////////////////////등록 버튼////////////////////////////////
        registbutton.setOnClickListener(view -> {
            insertDataIntoDatabase();
        });
        image_button.setOnClickListener(v-> {
            openGallery();
        });
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    //이미지 선택작업을 후의 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                ImageView imageView = findViewById(R.id.imageView01);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ////////////////////////////등록을 위한 메소드///////////////////////////////////
    private void insertDataIntoDatabase() {
        int adoptionID = (int) spinner.getItemIdAtPosition(0);
        String title = this.title.getText().toString(); // 이름
        String content = this.content.getText().toString();    // 설명
        Drawable dogImage = this.dogImage.getDrawable(); // 사진

        if (title.isEmpty() || content.isEmpty() || (dogImage == null)) {
            // 필수 정보가 비어있을 경우 에러 메시지 출력
            Toast.makeText(this, "모든 정보를 입력하세요.", Toast.LENGTH_SHORT).show();
        } else {
            // 주어진 메서드를 사용해서 데이터를 삽입합니다.
            reviewCtr.setContext(this);
            reviewCtr.review_insertData(userID, title, adoptionID, content, dogImage);
            // 여기에 데이터 삽입 로직을 구현해야 합니다.

            // 저장 완료 메시지 출력
            Toast.makeText(this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}