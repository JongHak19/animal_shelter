    package com.example.animal_shelter;

    import android.content.Intent;
    import android.os.Bundle;

    import androidx.fragment.app.Fragment;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ListView;
    import android.widget.Spinner;

    import com.google.android.material.floatingactionbutton.FloatingActionButton;

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link MenuAdoptionFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public class MenuAdoptionFragment extends Fragment {

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;
        private Spinner spinner;
        private String userID;
        public MenuAdoptionFragment() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MenuAdoptionFragment.
         */
        // TODO: Rename and change types and number of parameters
        public static MenuAdoptionFragment newInstance(String param1, String param2) {
            MenuAdoptionFragment fragment = new MenuAdoptionFragment();
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
            ///////////////콤보박스/////////////////
            spinner = (Spinner)getActivity().findViewById(R.id.searchSpinner);
            ///////////////콤보박스 끝/////////////////

            // Bundle에서 데이터 가져오기
            if (getArguments() != null) {
                userID = getArguments().getString("userID");
                // 이제 userID를 이용할 수 있습니다.
            }

            View rootview = inflater.inflate(R.layout.fragment_menu_adoption, container, false);

            ListView lv = rootview.findViewById(R.id.admissionlistView);
            AdmissionCtr admCtr = new AdmissionCtr();
            admCtr.setUserID(userID);
            lv.setAdapter(admCtr);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // 클릭된 항목의 데이터를 가져옴
                    Admission clickedItem = (Admission) admCtr.getItem(position);

                    // 이동할 액티비티를 지정하고 필요한 데이터를 전달
                    Intent intent = new Intent(getActivity(), Admission_inquiry_Activity.class);  // TargetActivity는 이동할 액티비티를 지정합니다.
                    intent.putExtra("key", clickedItem.getAdmissionId()); // clickedItem는 전달할 데이터입니다. "key"는 이 데이터를 참조하는 키입니다.
                    // 액티비티 시작
                    startActivity(intent);
                }
            });
            //////////////////플로팅 버튼 클릭////////////////////////
            FloatingActionButton fb = rootview.findViewById(R.id.addAdmissionFBtn);
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Admission_Activity.class);
                    startActivity(intent);
                }
            });
            //////////////////플로팅 버튼 클릭 끝////////////////////////

            ////////////컨트롤러에서 db 불러와서 메소드 실행하여 리스트에 저장////////////////////
            admCtr.setContext(getContext()); //부모 Context를 컨트롤러에 전달
            admCtr.addAllAdmissionsToList(getContext());
            ////////////컨트롤러에서 db 불러와서 메소드 실행하여 리스트에 저장 끝////////////////////

            //addAdmission 할때 이 파일은 fragment이므로 getContext() 부분에 this 사용하면 안됨
            return rootview;
        }
    }