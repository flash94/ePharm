package com.example.epharm.fragments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.epharm.MainActivity;
import com.example.epharm.R;
import com.example.epharm.RegisterActivity;
import com.example.epharm.adapters.AdapterPatient;
import com.example.epharm.database.Constants;
import com.example.epharm.database.DbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PatientFragment extends Fragment {

    //views
    private FloatingActionButton addUserBtn;
    private RecyclerView addedPatientsRv;
    TextView noItemYet;

    View view;
    ActionBar actionBar;
    private Boolean exit = false;

    DbHelper dbHelper;
    //sort options
    String orderByNewest = Constants.PA_ADDED_TIMESTAMP + " DESC";


    //for refreshing items, refresh with last choosen sort option
    String currentOrderByStatus = orderByNewest;

    public PatientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_patient_fragment, container, false);

        addedPatientsRv = (RecyclerView) view.findViewById(R.id.addedPatientsRv);
        addUserBtn = view.findViewById(R.id.addUserBtn);
        noItemYet = view.findViewById(R.id.noItemYet);
        initObjects();
        loadItems(orderByNewest);

        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start add new item activity
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });

        return view;
    }

    private void initObjects(){
        dbHelper = new DbHelper(getActivity());
    }


    private void loadItems(String orderBy){
        currentOrderByStatus = orderBy;
        AdapterPatient adapterPatient = new AdapterPatient(getContext(),
                dbHelper.getAllPatients(orderBy));
        addedPatientsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        addedPatientsRv.setAdapter(adapterPatient);

        if(dbHelper.getAllPatients(orderBy).size()<=0){
            addedPatientsRv.setVisibility(View.GONE);
            noItemYet.setVisibility(View.VISIBLE);
        }
        else{
            addedPatientsRv.setVisibility(View.VISIBLE);
            noItemYet.setVisibility(View.GONE);
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        loadItems(currentOrderByStatus); // refresh Item list
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
