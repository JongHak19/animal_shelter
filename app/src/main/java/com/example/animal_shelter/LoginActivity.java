package com.example.animal_shelter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText editID;
    private EditText editPassword;
    private Button loginbutton;
    private UserCtr userCtr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /////////////////////////////////커스텀 타이틀바 ////////////////////////////////
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getSupportActionBar().setCustomView(R.layout.toolbar_layout);
        //////////////////////////////////커스텀 타이틀바 끝////////////////////////
        editID = findViewById(R.id.editID);
        editPassword = findViewById(R.id.editPassword);
        loginbutton = findViewById(R.id.loginbutton);
        userCtr = new UserCtr();
        userCtr.setContext(this);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String id = editID.getText().toString();
                String password = editPassword.getText().toString();

                if (id.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (userCtr.validateLogin(id, password)) {
                    // 로그인 성공
                    userCtr.userLogin(id, password);
                    customer user = userCtr.getUser();
                    customer userApp = (customer) getApplication();

                    /////////////////에플리케이션 전역 단위로 유저 정보 저장(앱 실행중 데이터 유지)//////////////////////
                    userApp.setId(id);
                    userApp.setPassword(password);
                    userApp.setEmail(user.getEmail());
                    userApp.setName(user.getName());
                    userApp.setPhone(user.getPhone());
                    userApp.setAddress(user.getAddress());
                    /////////////////에플리케이션 전역 단위로 유저 정보 저장(앱 실행중 데이터 유지) 끝//////////////////////

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 로그인 실패
                    Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}