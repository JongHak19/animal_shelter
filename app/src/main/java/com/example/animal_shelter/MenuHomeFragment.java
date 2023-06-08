package com.example.animal_shelter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Adoption> myadoptions;
    private String userID;
    public MenuHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuHomeFragment newInstance(String param1, String param2) {
        MenuHomeFragment fragment = new MenuHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_menu_home, container, false);
        ListView lv = rootview.findViewById(R.id.reviewlistView);
        ReviewCtr reviewCtr = new ReviewCtr();

        // Bundle에서 데이터 가져오기
        if (getArguments() != null) {
            userID = getArguments().getString("userID");
            // 이제 userID를 이용할 수 있습니다.
        }

        ///////////로그인한 고객의 입양 정보 조회///////////////////////
        lv.setAdapter(reviewCtr);

        //////////////////플로팅 버튼 클릭////////////////////////
        FloatingActionButton fb = rootview.findViewById(R.id.addReviewFBtn);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myadoptions = new ArrayList<>();
                AdoptionCtr adoptionCtr = new AdoptionCtr(myadoptions, 2);
                adoptionCtr.setUserID(userID);
                adoptionCtr.addAllAdoptionsToList(getContext());
                myadoptions = adoptionCtr.getAdoptions();

                Iterator<Adoption> iterator = myadoptions.iterator();
                while (iterator.hasNext()) {
                    Adoption adoption = iterator.next();
                    if (!"입양완료".equals(adoption.getState())) {
                        iterator.remove();
                    }
                }
                if (myadoptions.isEmpty()) {
                    Toast.makeText(getContext(), "입양 신청 내역이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Integer> adoptionIds = new ArrayList<>();
                    ArrayList<String> dogNames = new ArrayList<>();
                    ArrayList<String> createDates = new ArrayList<>();

                    for (Adoption adoption : myadoptions) {
                        adoptionIds.add(adoption.getAdoptionId());
                        dogNames.add(adoption.getDogName());

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String date = formatter.format(adoption.getCreateDate());
                        createDates.add(date); // Date를 String으로 변환하여 리스트에 추가
                    }

                    Intent intent = new Intent(getActivity(), reviewActivity.class);
                    intent.putIntegerArrayListExtra("adoptionIDs", adoptionIds);
                    intent.putStringArrayListExtra("dogNames", dogNames);
                    intent.putStringArrayListExtra("createDates", createDates);
                    startActivity(intent);
                }
            }
        });
        ///////////////////플로팅 버튼 클릭 끝///////////////////////////////

        ////////////컨트롤러에서 db 불러와서 메소드 실행하여 리스트에 저장////////////////////
        reviewCtr.setContext(getContext()); //부모 Context를 컨트롤러에 전달
        reviewCtr.addAllReviewsToList(getContext());
        ////////////컨트롤러에서 db 불러와서 메소드 실행하여 리스트에 저장 끝////////////////////
        return rootview;
    }

}