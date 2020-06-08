package com.example.epharm;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.epharm.adapters.AdapterPatient;
import com.example.epharm.database.Constants;
import com.example.epharm.database.DbHelper;
import com.flutterwave.raveandroid.rave_presentation.RavePayManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    //views
    private FloatingActionButton addUserBtn;
    private RecyclerView addedPatientsRv;

    ActionBar actionBar;
    private Boolean exit = false;

    DbHelper dbHelper;
    //sort options
    String orderByNewest = Constants.PA_ADDED_TIMESTAMP + " DESC";


    //for refreshing items, refresh with last choosen sort option
    String currentOrderByStatus = orderByNewest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        actionBar.setTitle("All Users");

        //init views
        initViews();
        initObjects();
        loadItems(orderByNewest);

        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start add new item activity
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    //initialize views
    private void initViews(){
        addedPatientsRv = findViewById(R.id.addedPatientsRv);
        addUserBtn = findViewById(R.id.addUserBtn);
    }
    private void initObjects(){
        dbHelper = new DbHelper(this);
    }

    private void loadItems(String orderBy){
        currentOrderByStatus = orderBy;
        AdapterPatient adapterPatient = new AdapterPatient(MainActivity.this,
                dbHelper.getAllPatients(orderBy));
        addedPatientsRv.setAdapter(adapterPatient);
    }

    @Override
    public void onBackPressed() {
        //The Handler here handles accidental back presses,

        if (exit) {
            finish(); // finish activity
        } else {
            // it simply shows a Toast, and if there is another back press within 3 seconds, it closes the application.
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

    @Override
    public void onResume(){
        super.onResume();
        loadItems(currentOrderByStatus); // refresh Item list
    }


}
