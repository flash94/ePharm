package com.example.epharm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epharm.database.DbHelper;
import com.example.epharm.models.PatientModel;
import com.example.epharm.models.PharmacistModel;
import com.example.epharm.validation.InputValidation;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emailEt, passwordEt, confirmPasswordEt;

    private Button signUpBtn;
    private RadioButton phamRadioBtn, patientRadioBtn;
    private TextInputLayout emailNameVal, passwordVal, confirmPasswordVal, radioVal;

    private int priviledgeId = 0;
    private String role = "none";

    //input validator
    private InputValidation inputValidation;
    private DbHelper dbHelper;

    //Models
    private PatientModel patientModel;
    private PharmacistModel pharmacistModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialize views
        initViews();
        //initialize click listeners
        initListeners();
        initObject();
    }

    private void initViews(){
        emailEt = findViewById(R.id.email);
        passwordEt = findViewById(R.id.password);
        confirmPasswordEt = findViewById(R.id.confirmPassword);
        signUpBtn = findViewById(R.id.registerButton);
        phamRadioBtn = findViewById(R.id.phamRadioButton);
        patientRadioBtn = findViewById(R.id.patientRadioButton);
        emailNameVal = findViewById(R.id.emailNameVal);
        passwordVal = findViewById(R.id.passwordVal);
        confirmPasswordVal = findViewById(R.id.confirmPasswordVal);
        radioVal = findViewById(R.id.RadioVal);
    }

    private void initListeners(){
        signUpBtn.setOnClickListener(this);
    }

    private void initObject(){
        inputValidation = new InputValidation(this);
        dbHelper = new DbHelper(this);
        patientModel = new PatientModel();
        pharmacistModel = new PharmacistModel();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerButton:
                if(priviledgeId==0){
                    Toast.makeText(this,"Please fill in all the fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(priviledgeId == 1){
                        //register as a phamasist here
                        registerPharmacist();

                    }
                    if(priviledgeId == 9){
                        //register patient here
                        registerPatient();
                    }
                }
                break;
            case R.id.signIn:{
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }
    }

    public void checkButton(View view) {
        if (phamRadioBtn.isChecked()){
            priviledgeId = 1;
            role = "Pharmacist";
        }
        if(patientRadioBtn.isChecked()){
            priviledgeId = 9;
            role = "Patient";
        }
    }

    private void registerPatient(){
        if(!inputValidation.isInputEditTextEmail(emailEt, emailNameVal, "Invalid Email")){
            return;
        }
        if(!inputValidation.isInputEditTextMatches(passwordEt, passwordVal, "Password length to short")){
            return;
        }
        if(!inputValidation.isPasswordTextMatches(passwordEt, confirmPasswordEt, confirmPasswordVal, "Password does not match")){
            return;
        }
        if(!inputValidation.isInputRadioButtonSelected(phamRadioBtn, patientRadioBtn, radioVal, "please select user type")){
            return;
        }
        String timestamp = ""+System.currentTimeMillis();
        if(!dbHelper.checkPatientExist(emailEt.getText().toString().trim())){

            patientModel.setEmail(emailEt.getText().toString().trim());
            patientModel.setPassword(passwordEt.getText().toString().trim());
            patientModel.setAddedTimeStamp(timestamp);

            dbHelper.insertPatient(patientModel);
            Toast.makeText(this, "New " + role + " Registered", Toast.LENGTH_SHORT).show();
            emptyInputEditText();

        }
        else{
            Toast.makeText(this, "Registration failed! User already exists", Toast.LENGTH_LONG).show();
        }
    }

    private void registerPharmacist(){
        if(!inputValidation.isInputEditTextEmail(emailEt, emailNameVal, "Invalid Email")){
            return;
        }
        if(!inputValidation.isInputEditTextMatches(passwordEt, passwordVal, "Password length to short")){
            return;
        }
        if(!inputValidation.isPasswordTextMatches(passwordEt, confirmPasswordEt, confirmPasswordVal, "Password does not match")){
            return;
        }
        if(!inputValidation.isInputRadioButtonSelected(phamRadioBtn, patientRadioBtn, radioVal, "please select user type")){
            return;
        }
        String timestamp = ""+System.currentTimeMillis();
        if(!dbHelper.checkPharmacistExist(emailEt.getText().toString().trim())){

            pharmacistModel.setEmail(emailEt.getText().toString().trim());
            pharmacistModel.setPassword(passwordEt.getText().toString().trim());
            pharmacistModel.setAddedTimeStamp(timestamp);

            dbHelper.insertPharmacist(pharmacistModel);
            Toast.makeText(this, "New" + role + "Registered", Toast.LENGTH_SHORT).show();
            emptyInputEditText();

        }
        else{
            Toast.makeText(this, "Registration failed! User already exists", Toast.LENGTH_LONG).show();
        }
    }


    private void emptyInputEditText() {
        emailEt.setText(null);
        passwordEt.setText(null);
        confirmPasswordEt.setText(null);
    }

}
