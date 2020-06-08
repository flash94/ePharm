package com.example.epharm;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class RoleSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    RadioButton adminRadioBtn, phamRadioBtn, patientRadiBtn;
    Button continueBtn;
    RadioGroup radioGroup;
    private TextView signUptxt;
    private int roleId = 0;

    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Welcome");
        //initialize all views
        initViews();
        initListeners();


    }

    private void initViews(){
        adminRadioBtn = findViewById(R.id.adminRadioButton);
        phamRadioBtn = findViewById(R.id.phamRadioButton);
        patientRadiBtn = findViewById(R.id.patientRadioButton);
        continueBtn = findViewById(R.id.continueButton);
        signUptxt = findViewById(R.id.signUp);
        radioGroup = findViewById(R.id.userCheckBox);
    }

    private void initListeners(){
        continueBtn.setOnClickListener(this);
        signUptxt.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.continueButton:
                if(roleId==0){
                    Toast.makeText(this,"Please Select a Role to continue",Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(this, LoginActivity.class);
                    //send the roleId to the main login page
                    intent.putExtra("roleId",roleId);
                    startActivity(intent);
                }
                break;
            case R.id.signUp:
                Intent intentSignUp = new Intent(this, AdminRegister.class);
                startActivity(intentSignUp);
                break;
        }
    }

    public void checkButton(View view) {
        // get which role is checked by user
        if(adminRadioBtn.isChecked()){
            roleId = 1;
        }
        if(phamRadioBtn.isChecked()){
            roleId = 2;
        }
        if(patientRadiBtn.isChecked()){
            roleId = 3;
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
