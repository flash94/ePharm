package com.example.epharm.database;

public class Constants {
    //db name
    public static final String DB_NAME = "EPHARM_DB";
    //db version
    public static final int DB_VERSION = 1;
    //table name
    public static final String ADMIN_TABLE = "ADMIN_TABLE";
    public static final String PATIENT_TABLE = "PATIENT_TABLE";
    public static final String PHARMACIST_TABLE = "PHARMACIST_TABLE";


    //login role id
    public static String storedPatientEmail = "epharm@gmail.com";
    public static String subscribedPharmacistEmail =  "null";



    //columns/fields of Admin TABLE
    public static final  String A_ID = "ID";
    public static final  String A_EMAIL = "EMAIL";
    public static final  String A_PASSWORD = "PASSWORD";
    public static final  String A_ADDED_TIMESTAMP = "ADDED_TIME_STAMP";


    //COLUMNS/FIELDS OF PATIENT TABLE
    public static final  String PA_ID = "ID";
    public static final  String PA_EMAIL = "EMAIL";
    public static final String PA_PASSWORD = "PASSWORD";
    public static final String PA_FIRST_NAME = "FIRST_NAME";
    public static final String PA_LAST_NAME = "LAST_NAME";
    public static final  String PA_PHONE_NUMBER = "PHONE_NUMBER";
    public static final String PA_PATIENT_IMAGE = "PATIENT_IMAGE";
    public static final String PA_PHARMACIST_EMAIL = "PHARMACIST_EMAIL";
    public static final  String PA_ADDED_TIMESTAMP = "ADDED_TIME_STAMP";
    public static final String PA_UPDATED_TIMESTAMP = "UPDATED_TIME_STAMP";

    //columns/fields of PHARMACIST TABLE
    public static final  String PHARM_ID = "ID";
    public static final  String PHARM_EMAIL = "EMAIL";
    public static final String PHARM_PASSWORD = "PASSWORD";
    public static final String PHARM_FIRST_NAME = "FIRST_NAME";
    public static final String PHARM_LAST_NAME = "LAST_NAME";
    public static final  String PHARM_PHONE_NUMBER = "PHONE_NUMBER";
    public static final String PHARM_PATIENT_IMAGE = "PATIENT_IMAGE";
    public static final String PHARM_YEAR_OF_EXP = "YEAR_OF_EXP";
    public static final  String PHARM_CONSULT_FEE = "CONSULT_FEE";
    public static final String PHARM_UPDATED_TIMESTAMP = "UPDATED_TIMESTAMP";
    public static final  String PHARM_ADDED_TIMESTAMP = "ADDED_TIME_STAMP";


    //Create ADMIN table query
    public static final  String CREATE_ADMIN_TABLE = "CREATE TABLE " + ADMIN_TABLE + "("
            + A_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + A_EMAIL + " TEXT,"
            + A_PASSWORD + " TEXT,"
            + A_ADDED_TIMESTAMP + " TEXT"
            + ")";

    //Create PATIENT table query
    public static final  String CREATE_PATIENT_TABLE = "CREATE TABLE " + PATIENT_TABLE + "("
            + PA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PA_EMAIL + " TEXT,"
            + PA_FIRST_NAME + " TEXT,"
            + PA_LAST_NAME + " TEXT,"
            + PA_PASSWORD + " TEXT,"
            + PA_PHONE_NUMBER + " TEXT,"
            + PA_PATIENT_IMAGE + " TEXT,"
            + PA_PHARMACIST_EMAIL + " TEXT,"
            + PA_ADDED_TIMESTAMP + " TEXT,"
            + PA_UPDATED_TIMESTAMP + " TEXT"
            + ")";

    //Create PHARMACIST table query
    public static final  String CREATE_PHARMACIST_TABLE = "CREATE TABLE " + PHARMACIST_TABLE + "("
            + PHARM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PHARM_EMAIL + " TEXT,"
            + PHARM_FIRST_NAME + " TEXT,"
            + PHARM_LAST_NAME + " TEXT,"
            + PHARM_PASSWORD + " TEXT,"
            + PHARM_PHONE_NUMBER + " TEXT,"
            + PHARM_PATIENT_IMAGE + " TEXT,"
            + PHARM_ADDED_TIMESTAMP + " TEXT,"
            + PHARM_YEAR_OF_EXP + " TEXT,"
            + PHARM_CONSULT_FEE + " TEXT,"
            + PHARM_UPDATED_TIMESTAMP + " TEXT"
            + ")";

}
