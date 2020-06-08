package com.example.epharm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.epharm.models.AdminModel;
import com.example.epharm.models.PatientModel;
import com.example.epharm.models.PharmacistModel;

import java.util.ArrayList;

import static com.example.epharm.database.Constants.ADMIN_TABLE;
import static com.example.epharm.database.Constants.A_EMAIL;
import static com.example.epharm.database.Constants.A_PASSWORD;
import static com.example.epharm.database.Constants.PATIENT_TABLE;
import static com.example.epharm.database.Constants.PA_EMAIL;
import static com.example.epharm.database.Constants.PA_ID;
import static com.example.epharm.database.Constants.PA_PASSWORD;
import static com.example.epharm.database.Constants.PA_PHARMACIST_EMAIL;
import static com.example.epharm.database.Constants.PHARMACIST_TABLE;
import static com.example.epharm.database.Constants.PHARM_EMAIL;
import static com.example.epharm.database.Constants.PHARM_ID;
import static com.example.epharm.database.Constants.PHARM_PASSWORD;
import static com.example.epharm.database.Constants.storedPatientEmail;

//dbhelper class contains all crud operations
public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table on that db
        db.execSQL(Constants.CREATE_ADMIN_TABLE);
        db.execSQL(Constants.CREATE_PATIENT_TABLE);
        db.execSQL(Constants.CREATE_PHARMACIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //upgrade database (if there is any structure change, change db version)
        //drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.ADMIN_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.PATIENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.PHARMACIST_TABLE);
        //create table again
        onCreate(db);
    }

    //insert record to user Login table
    public long insertAdmin(AdminModel adminModel){
        //get writeable database because we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // id will be inserted automatically as we set AUTOINCREMENT in query

        //insert data
        values.put(Constants.A_EMAIL, adminModel.getEmail());
        values.put(Constants.A_PASSWORD, adminModel.getPassword());
        values.put(Constants.A_ADDED_TIMESTAMP, adminModel.getAddedTimeStamp());


        //insert row, it will return record id of saved record
        long id = db.insert(Constants.ADMIN_TABLE, null, values);
        //close db connection
        db.close();
        //return id of inserted item
        return id;
    }

    public boolean checkAdminExists() {
        String selectQuery = "SELECT * FROM " + Constants.ADMIN_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int Cursorcount = cursor.getCount();
        db.close();
        if(Cursorcount>0){
            return true;
        }
        return false;

    }

    public boolean checkPatientExist(String email) {
        String[] column = {PA_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = PA_EMAIL + " = ?";
        String[] selectionArgs= {email};

        Cursor cursor = db.query(PATIENT_TABLE,column,selection,selectionArgs,null,null,null);
        int Cursorcount = cursor.getCount();
        db.close();
        if(Cursorcount>0){
            return true;
        }
        return false;

    }

    public long insertPatient(PatientModel patientModel) {
        //get writeable database because we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // id will be inserted automatically as we set AUTOINCREMENT in query

        //insert data
        values.put(Constants.PA_EMAIL, patientModel.getEmail());
        values.put(Constants.PA_PASSWORD, patientModel.getPassword());
        values.put(Constants.PA_ADDED_TIMESTAMP, patientModel.getAddedTimeStamp());


        //insert row, it will return record id of saved record
        long id = db.insert(PATIENT_TABLE, null, values);
        //close db connection
        db.close();
        //return id of inserted item
        return id;
    }

    public boolean checkPharmacistExist(String email) {
        String[] column = {PHARM_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = PHARM_EMAIL + " = ?";
        String[] selectionArgs= {email};

        Cursor cursor = db.query(PHARMACIST_TABLE,column,selection,selectionArgs,null,null,null);
        int Cursorcount = cursor.getCount();
        db.close();
        if(Cursorcount>0){
            return true;
        }
        return false;
    }

    public long insertPharmacist(PharmacistModel pharmacistModel) {
        //get writeable database because we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // id will be inserted automatically as we set AUTOINCREMENT in query

        //insert data
        values.put(Constants.PHARM_EMAIL, pharmacistModel.getEmail());
        values.put(Constants.PHARM_PASSWORD, pharmacistModel.getPassword());
        values.put(Constants.PHARM_ADDED_TIMESTAMP, pharmacistModel.getAddedTimeStamp());


        //insert row, it will return record id of saved record
        long id = db.insert(PHARMACIST_TABLE, null, values);
        //close db connection
        db.close();
        //return id of inserted item
        return id;
    }

    public boolean checkAdmin(String email, String password){
        //String newRoleId = Integer.toString(roleId);
        String[] column = {Constants.A_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = A_EMAIL + " = ?" + " AND " + A_PASSWORD + " = ?";
        String[] selectionArgs= {email,password};

        Cursor cursor = db.query(ADMIN_TABLE,column,selection,selectionArgs,null,null,null);
        int Cursorcount = cursor.getCount();
        db.close();
        if(Cursorcount>0){
            return true;
        }
        return false;
    }

    public boolean checkPatient(String userName, String password){
        //String newRoleId = Integer.toString(roleId);
        String[] column = {Constants.PA_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = PA_EMAIL + " = ?" + " AND " + PA_PASSWORD + " = ?";
        String[] selectionArgs= {userName,password};

        Cursor cursor = db.query(PATIENT_TABLE,column,selection,selectionArgs,null,null,null);
        int Cursorcount = cursor.getCount();
        db.close();
        if(Cursorcount>0){
            return true;
        }
        return false;
    }

    public boolean checkPharmacist(String userName, String password){
        //String newRoleId = Integer.toString(roleId);
        String[] column = {Constants.PHARM_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = PHARM_EMAIL + " = ?" + " AND " + PHARM_PASSWORD + " = ?";
        String[] selectionArgs= {userName,password};

        Cursor cursor = db.query(PHARMACIST_TABLE,column,selection,selectionArgs,null,null,null);
        int Cursorcount = cursor.getCount();
        db.close();
        if(Cursorcount>0){
            return true;
        }
        return false;
    }

    //get all data from sqlite database
    public ArrayList<PatientModel> getAllPatients(String orderBy){
        //orderby query will allow to sort data e.g newest/oldest first, name ascending/descending
        //it will return list of items since we have used return type ArrayList<ModelItems>

        ArrayList<PatientModel> patientsList = new ArrayList<>();
        //query to select records
        String selectQuery = "SELECT * FROM " + PATIENT_TABLE + " ORDER BY " + orderBy;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all records and add to list
        if(cursor.moveToFirst()){
            do{
                PatientModel patientModel = new PatientModel();
                        patientModel.setId(cursor.getInt(cursor.getColumnIndex(Constants.PA_ID)));
                        patientModel.setEmail(cursor.getString(cursor.getColumnIndex(PA_EMAIL)));
                        patientModel.setPassword(cursor.getString(cursor.getColumnIndex(PA_PASSWORD)));
                        patientModel.setFirstName(cursor.getString(cursor.getColumnIndex(Constants.PA_FIRST_NAME)));
                        patientModel.setLastName(cursor.getString(cursor.getColumnIndex(Constants.PA_LAST_NAME)));
                        patientModel.setPhoneNumber(cursor.getString(cursor.getColumnIndex(Constants.PA_PHONE_NUMBER)));
                        patientModel.setPharmacistEmail(cursor.getString(cursor.getColumnIndex(Constants.PA_PHARMACIST_EMAIL)));
                        patientModel.setProfileImage(cursor.getString(cursor.getColumnIndex(Constants.PA_PATIENT_IMAGE)));
                        patientModel.setUpdatedTimestamp(cursor.getString(cursor.getColumnIndex(Constants.PA_UPDATED_TIMESTAMP)));
                        patientModel.setAddedTimeStamp(cursor.getString(cursor.getColumnIndex(Constants.PA_ADDED_TIMESTAMP)));
                //add record to list
                patientsList.add(patientModel);
            }while (cursor.moveToNext());
        }
        //close db connection
        db.close();
        //return the list
        return patientsList;
    }

    public void deleteData(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PATIENT_TABLE, PA_ID + " = ?", new String[] {String.valueOf(id)});
        db.close();
    }

    public void deletePharm(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PHARMACIST_TABLE, PA_ID + " = ?", new String[] {String.valueOf(id)});
        db.close();
    }

    public ArrayList<PharmacistModel> getAllPharmacists(String orderBy) {
        ArrayList<PharmacistModel> pharmacistList = new ArrayList<>();
        //query to select records
        String selectQuery = "SELECT * FROM " + PHARMACIST_TABLE + " ORDER BY " + orderBy;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all records and add to list
        if(cursor.moveToFirst()){
            do{
                PharmacistModel pharmacistModel = new PharmacistModel();
                pharmacistModel.setId(cursor.getInt(cursor.getColumnIndex(Constants.PHARM_ID)));
                pharmacistModel.setEmail(cursor.getString(cursor.getColumnIndex(PHARM_EMAIL)));
                pharmacistModel.setPassword(cursor.getString(cursor.getColumnIndex(PHARM_PASSWORD)));
                pharmacistModel.setFirstName(cursor.getString(cursor.getColumnIndex(Constants.PHARM_FIRST_NAME)));
                pharmacistModel.setLastName(cursor.getString(cursor.getColumnIndex(Constants.PHARM_LAST_NAME)));
                pharmacistModel.setPhoneNumber(cursor.getString(cursor.getColumnIndex(Constants.PHARM_PHONE_NUMBER)));
                pharmacistModel.setConsultationFee(cursor.getString(cursor.getColumnIndex(Constants.PHARM_CONSULT_FEE)));
                pharmacistModel.setYearOfExperience(cursor.getString(cursor.getColumnIndex(Constants.PHARM_YEAR_OF_EXP)));
                pharmacistModel.setProfileImage(cursor.getString(cursor.getColumnIndex(Constants.PHARM_PATIENT_IMAGE)));
                pharmacistModel.setUpdatedTimestamp(cursor.getString(cursor.getColumnIndex(Constants.PHARM_UPDATED_TIMESTAMP)));
                pharmacistModel.setAddedTimeStamp(cursor.getString(cursor.getColumnIndex(Constants.PHARM_ADDED_TIMESTAMP)));
                //add record to list
                pharmacistList.add(pharmacistModel);
            }while (cursor.moveToNext());
        }
        //close db connection
        db.close();
        //return the list
        return pharmacistList;
    }

    public void updatePatient(String patientEmail) {
    }

    public ArrayList<PatientModel> getSinglePatient(String patientEmail){

        ArrayList<PatientModel>patient = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        //query to select records
        String selectQuery = "SELECT * FROM " + PATIENT_TABLE + " WHERE " + PA_EMAIL + " =" + "\""+patientEmail+"\"";

        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all records and add to list
        if(cursor.moveToFirst()){
            do{
                PatientModel patientModel = new PatientModel();
                patientModel.setId(cursor.getInt(cursor.getColumnIndex(PA_ID)));
                patientModel.setProfileImage(""+cursor.getString(cursor.getColumnIndex(Constants.PA_PATIENT_IMAGE)));
                patientModel.setFirstName(""+cursor.getString(cursor.getColumnIndex(Constants.PA_FIRST_NAME)));
                patientModel.setLastName(""+cursor.getString(cursor.getColumnIndex(Constants.PA_LAST_NAME)));
                patientModel.setEmail(""+cursor.getString(cursor.getColumnIndex(PA_EMAIL)));
                patientModel.setPharmacistEmail(""+cursor.getString(cursor.getColumnIndex(Constants.PA_PHARMACIST_EMAIL)));
                patientModel.setPhoneNumber(""+cursor.getString(cursor.getColumnIndex(Constants.PA_PHONE_NUMBER)));
                patientModel.setUpdatedTimestamp(""+cursor.getString(cursor.getColumnIndex(Constants.PA_UPDATED_TIMESTAMP)));
                patientModel.setAddedTimeStamp(""+cursor.getString(cursor.getColumnIndex(Constants.PA_ADDED_TIMESTAMP)));

                //add record to list
                patient.add(patientModel);
            }while (cursor.moveToNext());
        }
        //close db connection
        db.close();

        //return the list
        return patient;
    }

    //update existing record to items database
    public void updatePatientRecord(String Email, String firstName, String lastName, String phoneNumber, String profileImage){
    //get writeable database because we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // id will be inserted automatically as we set AUTOINCREMENT in query

        //insert data
        String timeStamp = ""+System.currentTimeMillis();
        values.put(Constants.PA_FIRST_NAME, firstName);
        values.put(Constants.PA_LAST_NAME, lastName);
        values.put(Constants.PA_PHONE_NUMBER, phoneNumber);
        values.put(Constants.PA_PATIENT_IMAGE, profileImage);
        values.put(Constants.PA_UPDATED_TIMESTAMP, timeStamp);

        //insert row, it will return record id of saved record
        db.update(PATIENT_TABLE, values, PA_EMAIL+" = ?", new String[] {Email});
        //close db connection
        db.close();
    }

    public ArrayList<PharmacistModel> getSinglePharmacist(String pharmEmail) {
        ArrayList<PharmacistModel>pharmacist = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        //query to select records
        String selectQuery = "SELECT * FROM " + PHARMACIST_TABLE + " WHERE " + PHARM_EMAIL + " =" + "\""+pharmEmail+"\"";

        Cursor cursor = db.rawQuery(selectQuery, null);

//looping through all records and add to list
        if(cursor.moveToFirst()){
            do{
                PharmacistModel pharmacistModel = new PharmacistModel();
                pharmacistModel.setId(cursor.getInt(cursor.getColumnIndex(PHARM_ID)));
                pharmacistModel.setProfileImage(""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_PATIENT_IMAGE)));
                pharmacistModel.setFirstName(""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_FIRST_NAME)));
                pharmacistModel.setLastName(""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_LAST_NAME)));
                pharmacistModel.setEmail(""+cursor.getString(cursor.getColumnIndex(PHARM_EMAIL)));
                pharmacistModel.setYearOfExperience(""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_YEAR_OF_EXP)));
                pharmacistModel.setConsultationFee(""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_CONSULT_FEE)));
                pharmacistModel.setPhoneNumber(""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_PHONE_NUMBER)));
                pharmacistModel.setUpdatedTimestamp(""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_UPDATED_TIMESTAMP)));
                pharmacistModel.setAddedTimeStamp(""+cursor.getString(cursor.getColumnIndex(Constants.PHARM_ADDED_TIMESTAMP)));

                //add record to list
                pharmacist.add(pharmacistModel);
            }while (cursor.moveToNext());
        }
        //close db connection
        db.close();

        //return the list
        return pharmacist;
    }

    public void updatePharmacistRecord(String email, String firstName, String lastName,
                                       String phoneNumber, String consultationFee, String yearOfExp, String profileImage) {
        //get writeable database because we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // id will be inserted automatically as we set AUTOINCREMENT in query

        //insert data
        String timeStamp = ""+System.currentTimeMillis();
        values.put(Constants.PHARM_FIRST_NAME, firstName);
        values.put(Constants.PHARM_LAST_NAME, lastName);
        values.put(Constants.PHARM_PHONE_NUMBER, phoneNumber);
        values.put(Constants.PHARM_CONSULT_FEE, consultationFee);
        values.put(Constants.PHARM_YEAR_OF_EXP, yearOfExp);
        values.put(Constants.PHARM_PATIENT_IMAGE, profileImage);
        values.put(Constants.PHARM_UPDATED_TIMESTAMP, timeStamp);

        //insert row, it will return record id of saved record
        db.update(PHARMACIST_TABLE, values, PHARM_EMAIL+" = ?", new String[] {email});
        //close db connection
        db.close();
    }

    public boolean pharmacistSubscription(String email) {
        //get writeable database because we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // id will be inserted automatically as we set AUTOINCREMENT in query

        //insert data
        String timeStamp = ""+System.currentTimeMillis();
        values.put(Constants.PA_PHARMACIST_EMAIL, email);
        values.put(Constants.PA_UPDATED_TIMESTAMP, timeStamp);

        //insert row, it will return record id of saved record
        db.update(PATIENT_TABLE, values, PA_EMAIL+" = ?", new String[] {storedPatientEmail});
        //close db connection
        db.close();

        if(Constants.PA_PHARMACIST_EMAIL.equals("null")){
            return false;
        }
        return true;
    }

    public boolean checkSubscription(){
        //String newRoleId = Integer.toString(roleId);
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + PATIENT_TABLE + " WHERE " + PA_EMAIL + " =" + "\""+storedPatientEmail+"\""
                + " AND " + PA_PHARMACIST_EMAIL + " is " + "null " + "or " + PA_PHARMACIST_EMAIL + " = " + "\"''\"" ;

        Cursor cursor = db.rawQuery(selectQuery, null);
//        String empty = "null";
//        String[] column = {Constants.PA_ID};
//
//        String selection = PA_EMAIL + " = ?" + " AND " + PA_PHARMACIST_EMAIL + " = ?";
//        String[] selectionArgs= {storedPatientEmail, empty};
//
//        Cursor cursor = db.query(PATIENT_TABLE,column,selection,selectionArgs,null,null,null);
        int Cursorcount = cursor.getCount();
        db.close();
        if(Cursorcount>0){
            return true;
        }
        return false;
    }

    public ArrayList<PatientModel> getAllPatientSubscribed(String orderByNewest, String pharmEmail) {
        ArrayList<PatientModel>patient = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        //query to select records
        String selectQuery = "SELECT * FROM " + PATIENT_TABLE + " WHERE " + PA_PHARMACIST_EMAIL + " =" + "\""+pharmEmail+"\"";

        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all records and add to list
        if(cursor.moveToFirst()){
            do{
                PatientModel patientModel = new PatientModel();
                patientModel.setId(cursor.getInt(cursor.getColumnIndex(PA_ID)));
                patientModel.setProfileImage(""+cursor.getString(cursor.getColumnIndex(Constants.PA_PATIENT_IMAGE)));
                patientModel.setFirstName(""+cursor.getString(cursor.getColumnIndex(Constants.PA_FIRST_NAME)));
                patientModel.setLastName(""+cursor.getString(cursor.getColumnIndex(Constants.PA_LAST_NAME)));
                patientModel.setEmail(""+cursor.getString(cursor.getColumnIndex(PA_EMAIL)));
                patientModel.setPharmacistEmail(""+cursor.getString(cursor.getColumnIndex(Constants.PA_PHARMACIST_EMAIL)));
                patientModel.setPhoneNumber(""+cursor.getString(cursor.getColumnIndex(Constants.PA_PHONE_NUMBER)));
                patientModel.setUpdatedTimestamp(""+cursor.getString(cursor.getColumnIndex(Constants.PA_UPDATED_TIMESTAMP)));
                patientModel.setAddedTimeStamp(""+cursor.getString(cursor.getColumnIndex(Constants.PA_ADDED_TIMESTAMP)));

                //add record to list
                patient.add(patientModel);
            }while (cursor.moveToNext());
        }
        //close db connection
        db.close();

        //return the list
        return patient;
    }
}
