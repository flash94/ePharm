package com.example.epharm;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MailToPharmacist extends AppCompatActivity {
    String pharmacistEmail;
    TextView mRecipientEt;
    EditText mSubjectEt, mMessageEt;
    Button mSendMessageBtn;

    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_to_pharmacist);

        actionBar= getSupportActionBar();

        actionBar.setTitle("Send Message ");
        //back button
//        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        pharmacistEmail = intent.getStringExtra("PHARMACIST_EMAIL");

        initView();

        mSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get inputs from all text fields
                String recipient = mRecipientEt.getText().toString().trim();
                String subject = mSubjectEt.getText().toString().trim();
                String message = mMessageEt.getText().toString().trim();

                //method call for message intent with this inputs as parameters
                sendMessage(recipient, subject, message);
            }
        });
    }

    private void sendMessage(String recipient, String subject, String message) {
        //ACTION_SEND action to launch email client installed on your android device
        Intent mEmailIntent = new Intent(Intent.ACTION_SEND);
        //To send an email you need to specify mailTo: as URI using setData() method
        //and data type will be to text/plain using setType() method
        mEmailIntent.setData(Uri.parse("mailto:"));
        mEmailIntent.setType("text/plain");

        //put receipient email in intent
        /* receipient is put as array because you may wanna send email to multiple emails
        so enter comma(,) seperated emails, it will be stored in array*/
        mEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {recipient});
        //put subject email
        mEmailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        //put message
        mEmailIntent.putExtra(Intent.EXTRA_TEXT, message);
        try {
            //no error, so start intent
            startActivity(Intent.createChooser(mEmailIntent, "Select default Email app"));

        } catch (Exception e) {
            //if anything goes wrong e.g no internet or no email client
            //get and show exception message
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initView(){
        //initialize all views here
        mRecipientEt = findViewById(R.id.emailEt);
        mSubjectEt = findViewById(R.id.subjectEt);
        mMessageEt = findViewById(R.id.messageEt);
        mSendMessageBtn = findViewById(R.id.sendMessageBtn);

        if(pharmacistEmail.equals("null")){
            mRecipientEt.setText("Please subscribe to a pharmacist");
        }
        else{
            mRecipientEt.setText(pharmacistEmail);
        }

    }
}
