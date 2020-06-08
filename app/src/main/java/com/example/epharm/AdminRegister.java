package com.example.epharm;

import androidx.appcompat.app.ActionBar;
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
import com.example.epharm.models.AdminModel;
import com.example.epharm.validation.InputValidation;
import com.google.android.material.textfield.TextInputLayout;

public class AdminRegister extends AppCompatActivity implements View.OnClickListener {
    EditText emailEt, passwordEt, confirmPasswordEt;

    private Button signUpBtn;
    private TextView signInTxt;
    private TextInputLayout emailNameVal, passwordVal, confirmPasswordVal;

    private String Role = "Admin";

    private AdminModel adminModel;
    private InputValidation inputValidation;
    private DbHelper dbHelper;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);
        //back button
        actionBar= getSupportActionBar();
        actionBar.setTitle("Admin Sign Up");
        //back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //initViews
        initViews();
        initObjects();
        initListeners();
    }

    private void initViews(){
        emailEt = findViewById(R.id.email);
        passwordEt = findViewById(R.id.password);
        confirmPasswordEt = findViewById(R.id.confirmPassword);
        signUpBtn = findViewById(R.id.registerButton);
        signInTxt = findViewById(R.id.signIn);
        emailNameVal = findViewById(R.id.emailNameVal);
        passwordVal = findViewById(R.id.passwordVal);
        confirmPasswordVal = findViewById(R.id.confirmPasswordVal);
    }

    //initialize objects
    private void initObjects(){
        adminModel = new AdminModel();
        inputValidation = new InputValidation(this);
        dbHelper = new DbHelper(this);
    }

    //initialize listeners
    private void initListeners(){
        signInTxt.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerButton:
                registerAdmin();
                break;
            case R.id.signIn:
                Intent intent = new Intent(this, RoleSelectionActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go back by clicking back button on actionbar
        return super.onSupportNavigateUp();
    }

    private void registerAdmin() {
        if(!inputValidation.isInputEditTextEmail(emailEt, emailNameVal, "Invalid Email")){
            return;
        }
        if(!inputValidation.isInputEditTextMatches(passwordEt, passwordVal, "Password length to short")){
            return;
        }
        if(!inputValidation.isPasswordTextMatches(passwordEt, confirmPasswordEt, confirmPasswordVal, "Password does not match")){
            return;
        }

        String timestamp = ""+System.currentTimeMillis();
        if(!dbHelper.checkAdminExists()){

            adminModel.setEmail(emailEt.getText().toString().trim());
            adminModel.setPassword(passwordEt.getText().toString().trim());
            adminModel.setAddedTimeStamp(timestamp);

            dbHelper.insertAdmin(adminModel);
            Toast.makeText(this, "Admin Registered", Toast.LENGTH_SHORT).show();
            emptyInputEditText();

        }
        else{
            Toast.makeText(this, "Registration failed! Only one Admin is allowed", Toast.LENGTH_LONG).show();
        }
    }


    private void emptyInputEditText() {
        emailEt.setText(null);
        passwordEt.setText(null);
        confirmPasswordEt.setText(null);
    }
}
