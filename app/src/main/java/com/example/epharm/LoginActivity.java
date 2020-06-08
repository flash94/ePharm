package com.example.epharm;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epharm.database.DbHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emailEt, passwordEt;
    Button login;
    TextView signUpTxt;
    private int roleId;

    ActionBar actionBar;

    private String email;

    //dbHelper
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        actionBar= getSupportActionBar();

        actionBar.setTitle("Sign In");
        //back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);
        //get user roleId
        getRoleId();
        //initialize views
        initViews();
        initListeners();
        initObject();
    }

    private void initViews(){
        emailEt = findViewById(R.id.username);
        passwordEt = findViewById(R.id.password);
        login = findViewById(R.id.loginButton);
        signUpTxt = findViewById(R.id.signUp);
    }

    private void initListeners(){
        login.setOnClickListener(this);
        signUpTxt.setOnClickListener(this);
    }

    private void initObject(){
        dbHelper = new DbHelper(this);
    }

    private void getRoleId(){
        Intent intent = getIntent();
        roleId = intent.getIntExtra("roleId", 0);
        System.out.println("y");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                loginUser();
                break;
            case R.id.signUp:
                Intent intent = new Intent(this, AdminRegister.class);
                startActivity(intent);
                finish();
                break;
        }

    }

    private void loginUser() {
        if(roleId == 1){
            //check admin table for user to login
            checkAdmin();
        }
        if(roleId == 2){
            //check phamacists table for login
            checkPharmacist();
        }
        if(roleId ==3){
            //check patient table to login
            checkPatient();

        }
    }

    private void checkAdmin(){
        if(dbHelper.checkAdmin(emailEt.getText().toString(), passwordEt.getText().toString())){
            Intent intent = new Intent(this, MainAdminActivity.class);
            intent.putExtra("RoleID", roleId);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Login Failed! Invalid admin Login credentials", Toast.LENGTH_LONG).show();
        }
    }

    private void checkPatient(){
        email = emailEt.getText().toString();
        if(dbHelper.checkPatient(emailEt.getText().toString(),passwordEt.getText().toString())){
            Intent intent = new Intent(this, PatientMainActivity.class);
            intent.putExtra("RoleID", roleId);
            intent.putExtra("EMAIL", email);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Login Failed! Invalid patient login credentials", Toast.LENGTH_LONG).show();
        }
    }

    private void checkPharmacist(){
        email = emailEt.getText().toString();
        if(dbHelper.checkPharmacist(emailEt.getText().toString(),passwordEt.getText().toString())){
            Intent intent = new Intent(this, PharmMainActivity.class);
            intent.putExtra("RoleID", roleId);
            intent.putExtra("EMAIL", email);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Login Failed! Invalid pharmacist Login credentials", Toast.LENGTH_LONG).show();
        }
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed(); // go back by clicking back button on actionbar
//        return super.onSupportNavigateUp();
//    }
}
