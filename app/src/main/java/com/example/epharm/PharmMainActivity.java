package com.example.epharm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epharm.adapters.AdapterPatientPharm;
import com.example.epharm.adapters.AdapterPharmPatient;
import com.example.epharm.database.Constants;
import com.example.epharm.database.DbHelper;
import com.example.epharm.models.PatientModel;
import com.example.epharm.models.PharmacistModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PharmMainActivity extends AppCompatActivity {

    //views
    //private Button addUserBtn;
    private RecyclerView addedPatientsRv;
    private TextView noItemYet;

    ActionBar actionBar;
    private Boolean exit = false;

    //patient Arraylist
    private ArrayList<PharmacistModel> pharmacist;


    DbHelper dbHelper;
    //sort options
    String orderByNewest = Constants.PHARM_ADDED_TIMESTAMP + " DESC";
    String pharmEmail;


    //for refreshing items, refresh with last choosen sort option
    String currentOrderByStatus = orderByNewest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharm_main);

        actionBar = getSupportActionBar();
        actionBar.setTitle("All Patients");

        //get logged in user email froom adapter through intent
        Intent intent = getIntent();
        pharmEmail = intent.getStringExtra("EMAIL");

        //init views
        initViews();
        initObjects();
        loadItems(orderByNewest);

    }

    private void loadItems(String orderByNewest) {
        currentOrderByStatus = orderByNewest;
        AdapterPharmPatient adapterPharm = new AdapterPharmPatient(PharmMainActivity.this,
                dbHelper.getAllPatientSubscribed(orderByNewest, pharmEmail));
        addedPatientsRv.setAdapter(adapterPharm);

        if(dbHelper.getAllPatientSubscribed(orderByNewest, pharmEmail).size()<=0){
            addedPatientsRv.setVisibility(View.GONE);
            noItemYet.setVisibility(View.VISIBLE);
        }
        else{
            addedPatientsRv.setVisibility(View.VISIBLE);
            noItemYet.setVisibility(View.GONE);
        }
    }

    private void initObjects() {
        dbHelper = new DbHelper(this);
    }

    private void initViews() {
        addedPatientsRv = findViewById(R.id.addedPatientsRv);
        noItemYet = findViewById(R.id.noItemYet);
        //addUserBtn = findViewById(R.id.addUserBtn);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadItems(currentOrderByStatus); // refresh Item list
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.menu_main_pharm, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle menu items
        int id = item.getItemId();
        if (id == R.id.update_profile) {
            //show sort options(show in dialog)
            updateProfile();
        }
        if(id == R.id.signOut){
            String[] options = {"No", "Yes"};
            //dialog
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //add items to dialog
            builder.setTitle("Are you sure you want to Logout?")
                    .setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //handle item clicks
                            if (which==0){
                                onResume();
                            }
                            else if(which==1){
                                logOut();
                            }
                        }
                    });
            //show dialog
            builder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut(){
        Intent intent = new Intent(this, RoleSelectionActivity.class);
        startActivity(intent);
    }


    private void updateProfile() {
        if (dbHelper.getSinglePharmacist(pharmEmail).size() > 0) {
            pharmacist = dbHelper.getSinglePharmacist(pharmEmail);
            System.out.println("Ade");
            int id = pharmacist.get(0).getId();
            String firstName = pharmacist.get(0).getFirstName();
            String lastName = pharmacist.get(0).getLastName();
            String email = pharmacist.get(0).getEmail();
            String phoneNo = pharmacist.get(0).getPhoneNumber();
            String profileImg = pharmacist.get(0).getProfileImage();
            String yearOfExperience = pharmacist.get(0).getYearOfExperience();
            String consultationFee = pharmacist.get(0).getConsultationFee();

            Intent intent = new Intent(this, UpdatePharmProfile.class);
            intent.putExtra("FIRST_NAME", firstName);
            intent.putExtra("EMAIL", email);
            intent.putExtra("LAST_NAME", lastName);
            intent.putExtra("PHONE_NUMBER", phoneNo);
            intent.putExtra("PROFILE_IMAGE", profileImg);
            intent.putExtra("YEAR_OF_EXPERIENCE", yearOfExperience);
            intent.putExtra("CONSULTATION_FEE", consultationFee);

            startActivity(intent);
        }

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
}
