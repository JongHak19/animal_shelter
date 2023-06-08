package com.example.animal_shelter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MenuAdoptionFragment fragmentAdoption = new MenuAdoptionFragment();
    private MenuHomeFragment fragmentHome = new MenuHomeFragment();
    private MenuMypageFragment fragmentMypage = new MenuMypageFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/////////////////////////////////커스텀 타이틀바 ////////////////////////////////
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getSupportActionBar().setCustomView(R.layout.toolbar_layout);
//////////////////////////////////커스텀 타이틀바 끝////////////////////////
        setContentView(R.layout.activity_main);

        String page = getIntent().getStringExtra("page");
        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);

        if(page !=null) {
            if (page.equals("Adoption")) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.menu_frame_layout, fragmentAdoption).commitAllowingStateLoss();
                bottomNavigationView.setSelectedItemId(R.id.menu_adoption);
            }
            else if (page.equals("MyPage")) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.menu_frame_layout, fragmentMypage).commitAllowingStateLoss();
                bottomNavigationView.setSelectedItemId(R.id.menu_mypage);
            }
        }else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.menu_frame_layout, fragmentHome).commitAllowingStateLoss();
            bottomNavigationView.setSelectedItemId(R.id.menu_home);
        }

        bottomNavigationView.setOnItemSelectedListener(new ItemSelectedListener());

        customer app = (customer) getApplication();
        String userID = app.getId();
        String userName = app.getName();
        String userEmail = app.getEmail();
        String userAddress = app.getAddress();

        // Bundle 생성
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        bundle.putString("userName",userName);
        bundle.putString("userEmail",userEmail);
        bundle.putString("userAddress",userAddress);

        // Fragment에 Bundle 설정
        fragmentAdoption.setArguments(bundle);
        fragmentHome.setArguments(bundle);
        fragmentMypage.setArguments(bundle);
    }

    class ItemSelectedListener implements NavigationBarView.OnItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.menu_adoption:
                    transaction.replace(R.id.menu_frame_layout, fragmentAdoption).commitAllowingStateLoss();
                    break;
                case R.id.menu_home:
                    transaction.replace(R.id.menu_frame_layout, fragmentHome).commitAllowingStateLoss();
                    break;
                case R.id.menu_mypage:
                    transaction.replace(R.id.menu_frame_layout, fragmentMypage).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}