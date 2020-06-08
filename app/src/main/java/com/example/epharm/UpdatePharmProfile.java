package com.example.epharm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.epharm.database.DbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class UpdatePharmProfile extends AppCompatActivity {

    EditText firstNameEt, lastNameEt, phoneNoEt, pharmFeeEt, expEt;
    TextView emailEt;
    CircularImageView imageIv;
    private FloatingActionButton saveBtn;


    //permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;

    //array of permissions
    private String[] cameraPermissions; //camera and storage
    private String[] storagePermissions; //only storage
    //variables (Will contain only data to save to database)
    private Uri imageUri;

    private String firstName, lastName, phoneNumber, email, profileImage, consultationFee, yearOfExperience;

    //db helper
    private DbHelper dbHelper;

    //actionbar
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pharm_profile);

        //init actionbar
        actionBar = getSupportActionBar();
        //actionbar title
        actionBar.setTitle("Update Profile");
        //back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        firstName = intent.getStringExtra("FIRST_NAME");
        email = intent.getStringExtra("EMAIL");
        lastName = intent.getStringExtra("LAST_NAME");
        phoneNumber = intent.getStringExtra("PHONE_NUMBER");
        profileImage = intent.getStringExtra("PROFILE_IMAGE");
        yearOfExperience = intent.getStringExtra("YEAR_OF_EXPERIENCE");
        consultationFee = intent.getStringExtra("CONSULTATION_FEE");

        initViews();
    }

    private void initViews() {

        firstNameEt = findViewById(R.id.firstNameEt);
        lastNameEt = findViewById(R.id.lastNameEt);
        phoneNoEt = findViewById(R.id.phoneNoEt);
        emailEt = findViewById(R.id.emailEt);
        imageIv = findViewById(R.id.userImage);
        pharmFeeEt = findViewById(R.id.pharmFeeEt);
        expEt = findViewById(R.id.expEt);
        saveBtn = findViewById(R.id.saveBtn);

        if(firstName.equals("null")){
            firstNameEt.setHint("First Name");
            //firstName = "First Name";
        }
        else{
            firstNameEt.setText(firstName);
        }
        if(lastName.equals("null")){
            lastNameEt.setHint("Last Name");
            //lastName = " Last Name";
        }
        else{
            lastNameEt.setText(lastName);
        }
        if(phoneNumber.equals("null")){
            phoneNoEt.setHint("Phone Number");
            //phoneNumber = "Phone Number";
        }
        else{
            phoneNoEt.setText(phoneNumber);
        }
        if(consultationFee.equals("null")){
            pharmFeeEt.setHint("Consultation Fee");
            //phoneNumber = "Phone Number";
        }
        else{
            pharmFeeEt.setText(consultationFee);
        }
        if(yearOfExperience.equals("null")){
            expEt.setHint("Year of Experience");
            //phoneNumber = "Phone Number";
        }
        else{
            expEt.setText(yearOfExperience);
        }
        emailEt.setText(email);
        imageUri = Uri.parse(profileImage);

        //if no image was selected while adding data, imageUri value will be "null"
        if(imageUri.toString().equals("null")){
            //no image, set default
            imageIv.setImageResource(R.drawable.ic_add_photo);
        }
        else{
            //have image, set image to db image
            imageIv.setImageURI(imageUri);
        }


        //init dbhelper
        dbHelper = new DbHelper(this);

        //init permission arrays
        cameraPermissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //click image to show image picker dialog
        imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show image pick dialog
                imagePickDialog();

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save update image
                firstName = firstNameEt.getText().toString();
                lastName = lastNameEt.getText().toString();
                phoneNumber = phoneNoEt.getText().toString();
                consultationFee = pharmFeeEt.getText().toString();
                yearOfExperience = expEt.getText().toString();

                dbHelper.updatePharmacistRecord(""+email,
                        ""+firstName,
                        ""+lastName,
                        ""+phoneNumber,
                        ""+consultationFee,
                        ""+yearOfExperience,
                        ""+imageUri
                );
                Toast.makeText(getApplicationContext(),"Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void imagePickDialog() {
        //options to display in dialog
        String[] options = {"Camera", "Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //title
        builder.setTitle("Pick Image From");
        //set items/options
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle clicks
                if(which == 0){
                    //camera clicked
                    if(!checkCameraPersmissions()){
                        requestCameraPermission();
                    }
                    else {
                        //permission already granted
                        pickFromCamera();
                    }
                }
                else if (which == 1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else{
                        //permission already granted
                        pickFromGallery();
                    }
                }
            }
        });
        //create/show dialog
        builder.create().show();
    }

    private void pickFromGallery() {
        //intent to pick image from gallery, the image will be returned in onActivityResult method
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*"); //we want only images
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        //intent to pick image from gallery, the image will be returned in onActivityResult method

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");
        //put image uri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //intent to open camera for image
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission(){
        //check if storage permission is enabled or not

        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermission(){
        //request storage permission
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPersmissions(){
        //check if camera permissions is enabled or not
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission(){
        //request camera permission
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void copyFileOrDirectory(String srcDir, String desDir){
        //create folder in specific directory
        try {
            File src = new File(srcDir);
            File des = new File(desDir, src.getName());
            if(src.isDirectory()){
                String[] files = src.list();
                int filesLength = files.length;
                for (String file : files){
                    String src1 = new File(src, file).getPath();
                    String dst1 = des.getPath();

                    copyFileOrDirectory(src1, dst1);
                }
            }
            else {
                copyFile(src, des);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void copyFile(File srcDir, File desDir) throws IOException {
        if(!desDir.getParentFile().exists()){
            desDir.mkdirs(); //create if not exists
        }
        if(!desDir.exists()){
            desDir.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(srcDir).getChannel();
            destination = new FileOutputStream(desDir).getChannel();
            destination.transferFrom(source, 0, source.size());

            imageUri = Uri.parse(desDir.getPath()); //uri of saved image
            Log.d("ImagePath", "copyFile: "+imageUri);
        }
        catch (Exception e){
            //if there is an error saving the image
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            //close resources
            if(source!=null){
                source.close();
            }
            if (destination!=null){
                destination.close();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go back by clicking back button on actionbar
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //request of permission allowed/denied

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    //if allowed returns true otherwise false
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted) {
                        //both permission is allowed
                        pickFromCamera();
                    }
                    else{
                        Toast.makeText(this, "Camera & Storage permissions are required...", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    //if allowed returns true otherwise false
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        //storage permission is allowed
                        pickFromGallery();
                    }
                    else{
                        Toast.makeText(this, "Storage permissions are required...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //image picked from camera or gallery will be recieved here

        if(resultCode == RESULT_OK){
            //image is picked

            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //picked from gallery
                //crop image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //picked from camera
                //crop image
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                //cropped imaage received
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK){
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    //set image
                    imageIv.setImageURI(resultUri);

                    copyFileOrDirectory(""+imageUri.getPath(), ""+getDir("SQLiteItemImages",MODE_PRIVATE));
                }
                else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    //error
                    Exception error = result.getError();
                    Toast.makeText(this, " "+error, Toast.LENGTH_SHORT).show();

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
