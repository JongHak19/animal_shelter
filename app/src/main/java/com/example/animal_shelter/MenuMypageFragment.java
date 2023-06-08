package com.example.animal_shelter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuMypageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuMypageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView yourAdoptionView;
    private RecyclerView myAdoptionView;
    private AdoptionCtr youradoptionCtr;
    private AdoptionCtr myadoptionCtr;
    private List<Adoption> youradoptions;
    private List<Adoption> myadoptions;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String userID;
    private String userName;
    private String userEmail;
    private String userAddress;
    public MenuMypageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuMypageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuMypageFragment newInstance(String param1, String param2) {
        MenuMypageFragment fragment = new MenuMypageFragment();
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
        View view = inflater.inflate(R.layout.fragment_menu_mypage, container, false);
        // Inflate the layout for this fragment

        // Bundle에서 데이터 가져오기
        if (getArguments() != null) {
            userID = getArguments().getString("userID");
            userName = getArguments().getString("userName");
            userAddress = getArguments().getString("userAddress");
            userEmail = getArguments().getString("userEmail");
            // 이제 userID를 이용할 수 있습니다.
        }
        //////////////////////////////회원 정보 출력///////////////////////////////////////
        TextView textView7 = view.findViewById(R.id.textView7);
        TextView textView8 = view.findViewById(R.id.textView8);
        TextView textView9 = view.findViewById(R.id.textView9);

        textView7.setText(userName);
        textView8.setText(userEmail);
        textView9.setText(userAddress);
    /////////////////////////나에게온 입양신청///////////////////////////////
        yourAdoptionView = view.findViewById(R.id.youradoptionList);
        youradoptions = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        yourAdoptionView.setLayoutManager(layoutManager1);
        youradoptionCtr = new AdoptionCtr(youradoptions,1);
        yourAdoptionView.setAdapter(youradoptionCtr);
        youradoptionCtr.setContext(getContext());
        youradoptionCtr.setUserID(userID);
        youradoptionCtr.addAllAdoptionsToList(getContext());
    /////////////////////////나에게온 입양신청  끝 ///////////////////////////////

    /////////////////////////내가 한 입양신청///////////////////////////////
        myAdoptionView = view.findViewById(R.id.myadoptionList);
        myadoptions = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        myAdoptionView.setLayoutManager(layoutManager2);
        myadoptionCtr = new AdoptionCtr(myadoptions,2);
        myAdoptionView.setAdapter(myadoptionCtr);
        myadoptionCtr.setContext(getContext());
        myadoptionCtr.setUserID(userID);
        myadoptionCtr.addAllAdoptionsToList(getContext());

    /////////////////////////내가 한 입양신청 끝 ///////////////////////////////
        return view;
    }
}