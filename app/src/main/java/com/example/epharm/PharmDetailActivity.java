package com.example.epharm;

import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.epharm.R;
import com.example.epharm.database.Constants;
import com.example.epharm.database.DbHelper;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.flutterwave.raveandroid.rave_presentation.RavePayManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class PharmDetailActivity extends AppCompatActivity {
    //views
    private CircularImageView pharmIv;
    private TextView userNameTv,pharmEmailTv, pharmPhoneTv, pharmFeeTv, pharmExpTv, dateAddedTv, dateUpdatedTv;
    Button subscribeBtn;
    //Action bar
    private ActionBar actionBar;

    //db helper
    private DbHelper dbHelper;

    private int pharmacistId;

    String email;
    String fee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharm_detail);

        //setting up actionbar with title and back button
        actionBar = getSupportActionBar();
        actionBar.setTitle("Pharmacist Details");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get item id froom adapter through intent
        Intent intent = getIntent();
        pharmacistId = intent.getIntExtra("PHARMACIST_ID", 0);

        //init db helper class
        dbHelper = new DbHelper(this);

        //init views
         pharmIv= findViewById(R.id.pharmIv);
        userNameTv = findViewById(R.id.userNameTv);
        pharmPhoneTv = findViewById(R.id.pharmPhoneTv);
        pharmEmailTv = findViewById(R.id.pharmEmailTv);
        pharmFeeTv = findViewById(R.id.pharmFeeTv);
        pharmExpTv = findViewById(R.id.pharmExpTv);
        dateAddedTv = findViewById(R.id.dateAddedTv);
        dateUpdatedTv = findViewById(R.id.dateUpdatedTv);
        subscribeBtn = findViewById(R.id.subscribeBtn);

        showItemDetails();

        subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribe();
            }
        });
    }

    private void subscribe(){
        String narration = "Consultant Fee";
        String txRef;
        String country = "NG";
        String currency = "NGN";
        txRef = email +" "+  UUID.randomUUID().toString() + "Ref";
        final String publicKey = "FLWPUBK-2dd619c197a799a0b310a1c5fb23c803-X"; //Get your public key from your account
        final String encryptionKey = "8258a2e4620f08a65ff11238"; //Get your encryption key from your account


        //save update image
        if(fee.equals("Not updated yet")){
            Toast.makeText(this,"Pharmacist Profile Not Updated yet, Subscribe to another pharmacist", Toast.LENGTH_LONG).show();
        }
        else{
            if(!dbHelper.checkSubscription()){
                Toast.makeText(this,"You are already subscribed to a pharmacist", Toast.LENGTH_LONG).show();
            }
            else{
                new RaveUiManager(this).setAmount(Double.parseDouble(fee))
                        .setCountry(country)
                        .setCurrency(currency)
                        .setEmail(email)
                        .setNarration(narration)
                        .setPublicKey(publicKey)
                        .setEncryptionKey(encryptionKey)
                        .setTxRef(txRef)
                        .acceptAccountPayments(true)
                        .acceptCardPayments(true)
                        .acceptMpesaPayments(false)
                        .acceptGHMobileMoneyPayments(false)
                        .onStagingEnv(true)
                        .shouldDisplayFee(true)
                        .allowSaveCardFeature(true)
                        .initialize();
            }
        }
    }
    private void showItemDetails() {
        //get item details

        String timeUpdated;
        //query to select item based on item id
        String selectQuery = "SELECT * FROM " + Constants.PHARMACIST_TABLE + " WHERE " + Constants.PHARM_ID +" =\"" + pharmacistId+"\"";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //keep checking in the whole db for that record
        if(cursor.moveToNext()){
            do{
                //get data
                String id = ""+cursor.getInt(cursor.getColumnIndex(Constants.PHARM_ID));
                String firstName = ""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_FIRST_NAME));
                String lastName = ""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_LAST_NAME));
                email = ""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_EMAIL));
                String phoneNo = ""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_PHONE_NUMBER));
                fee = ""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_CONSULT_FEE));
                String expe = ""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_YEAR_OF_EXP));
                String image = ""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_PATIENT_IMAGE));
                String timestampAdded = ""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_ADDED_TIMESTAMP));
                String timestampUpdated = ""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_UPDATED_TIMESTAMP));

                //convert timestamp to dd/mm/yyyy hh:mm aa e.g. 22/0/2020 08:22 AM
                Calendar calendar1 = Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(timestampAdded));
                String timeAdded = ""+DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar1);

                if(timestampUpdated.equals("null")){
                    timeUpdated = "Not updated yet";
                }
                else{
                    Calendar calendar2 = Calendar.getInstance(Locale.getDefault());
                    calendar2.setTimeInMillis(Long.parseLong(timestampUpdated));
                    timeUpdated = ""+DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar2);
                }

                if(phoneNo.equals("null")){
                    phoneNo = "Not updated yet";
                }

                if(fee.equals("null")){
                    fee = "Not updated yet";
                }
                if(expe.equals("null")){
                    expe = "Not updated yet";
                }

                //set data
                String fullName = firstName + " " + lastName;

                if (firstName.equals("null") || lastName.equals("null")){
                    fullName = "Name not updated yet";
                }
                userNameTv.setText(fullName);
                pharmEmailTv.setText(email);
                pharmPhoneTv.setText(phoneNo);
                pharmFeeTv.setText(fee);
                pharmExpTv.setText(expe);
                dateAddedTv.setText(timeAdded);
                dateUpdatedTv.setText(timeUpdated);

                //if item has no image
                if (image.equals("null")){
                    //no image in record set default
                    pharmIv.setImageResource(R.drawable.ic_user_icon);
                }
                else{
                    //have image in record
                    pharmIv.setImageURI(Uri.parse(image));
                }

            }while (cursor.moveToNext());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "PAYMENT SUCCESSFUL ", Toast.LENGTH_SHORT).show();
                if(dbHelper.pharmacistSubscription(""+email))
                {

                    Toast.makeText(this,"Subscribed Successfully", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this," Error try again", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "PAYMENT UNSUCCESSFUL ", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//goto previous activity
        return super.onSupportNavigateUp();
    }
}
