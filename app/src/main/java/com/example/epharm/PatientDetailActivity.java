package com.example.epharm;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.epharm.R;
import com.example.epharm.database.Constants;
import com.example.epharm.database.DbHelper;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Calendar;
import java.util.Locale;

public class PatientDetailActivity extends AppCompatActivity {
    //views
    private CircularImageView patientIv;
    private TextView userNameTv,patientEmailTv, patientPhoneTv, dateAddedTv, dateUpdatedTv;
    Button messageBtn;
    //Action bar
    private ActionBar actionBar;

    //db helper
    private DbHelper dbHelper;

    private int patientId;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);

        //setting up actionbar with title and back button
        actionBar = getSupportActionBar();
        actionBar.setTitle("Patient Details");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get item id froom adapter through intent
        Intent intent = getIntent();
        patientId = intent.getIntExtra("PATIENT_ID", 0);

        //init db helper class
        dbHelper = new DbHelper(this);

        //init views
        patientIv= findViewById(R.id.patientIv);
        userNameTv = findViewById(R.id.userNameTv);
        patientPhoneTv = findViewById(R.id.patientPhoneTv);
        patientEmailTv = findViewById(R.id.patientEmailTv);
        dateAddedTv = findViewById(R.id.dateAddedTv);
        dateUpdatedTv = findViewById(R.id.dateUpdatedTv);
        messageBtn = findViewById(R.id.messageBtn);

        showItemDetails();

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        sendMessage();
            }
        });
    }

    private void sendMessage(){
        Intent intent1 = new Intent(this, MailToPatient.class);
        intent1.putExtra("PATIENT_EMAIL", email);
        startActivity(intent1);
    }

    private void showItemDetails() {
        //get item details

        String timeUpdated;
        //query to select item based on item id
        String selectQuery = "SELECT * FROM " + Constants.PATIENT_TABLE + " WHERE " + Constants.PA_ID +" =\"" + patientId+"\"";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //keep checking in the whole db for that record
        if(cursor.moveToNext()){
            do{
                //get data
                String id = ""+cursor.getInt(cursor.getColumnIndex(Constants.PA_ID));
                String firstName = ""+cursor.getString(cursor.getColumnIndex(Constants.PA_FIRST_NAME));
                String lastName = ""+cursor.getString(cursor.getColumnIndex(Constants.PA_LAST_NAME));
                email = ""+cursor.getString(cursor.getColumnIndex(Constants.PA_EMAIL));
                String phoneNo = ""+cursor.getString(cursor.getColumnIndex(Constants.PA_PHONE_NUMBER));
                String image = ""+cursor.getString(cursor.getColumnIndex(Constants.PA_PATIENT_IMAGE));
                String timestampAdded = ""+cursor.getString(cursor.getColumnIndex(Constants.PA_ADDED_TIMESTAMP));
                String timestampUpdated = ""+cursor.getString(cursor.getColumnIndex(Constants.PA_UPDATED_TIMESTAMP));

                //convert timestamp to dd/mm/yyyy hh:mm aa e.g. 22/0/2020 08:22 AM
                Calendar calendar1 = Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(timestampAdded));
                String timeAdded = ""+DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar1);

                if(timestampUpdated.equals("null")){
                    timeUpdated = "Account not updated";
                }
                else{
                    Calendar calendar2 = Calendar.getInstance(Locale.getDefault());
                    calendar2.setTimeInMillis(Long.parseLong(timestampUpdated));
                    timeUpdated = ""+DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar2);
                }

                if(phoneNo.equals("null")){
                    phoneNo = "Not updated yet";
                }

                //set data
                String fullName = firstName + " " + lastName;

                if (firstName.equals("null") || lastName.equals("null")){
                    fullName = "Name not updated yet";
                }
                userNameTv.setText(fullName);
                patientEmailTv.setText(email);
                patientPhoneTv.setText(phoneNo);
                dateAddedTv.setText(timeAdded);
                dateUpdatedTv.setText(timeUpdated);

                //if item has no image
                if (image.equals("null")){
                    //no image in record set default
                    patientIv.setImageResource(R.drawable.ic_user_icon);
                }
                else{
                    //have image in record
                    patientIv.setImageURI(Uri.parse(image));
                }

            }while (cursor.moveToNext());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//goto previous activity
        return super.onSupportNavigateUp();
    }
}
