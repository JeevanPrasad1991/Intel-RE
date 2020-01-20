package intelre.cpm.com.intelre.constant;

import android.os.Environment;

/**
 * Created by jeevanp on 14-12-2017.
 */

public class CommonString {
    public static final String KEY_CHECKOUT_IMAGE = "CHECKOUT_IMAGE";
    //preference
    public static final String KEY_USERNAME = "USERNAME";
    public static final String KEY_PASSWORD = "PASSWORD";
    public static final String KEY_STATUS = "STATUS";
    public static final int CAPTURE_MEDIA = 131;
    public static final String KEY_QUESTION_CD = "question_cd";
    public static final String KEY_ANSWER_CD = "answer_cd";

    public static final String KEY_DATE = "DATE";
    public static final String KEY_YYYYMMDD_DATE = "yyyymmddDate";
    public static final String KEY_STOREVISITED_STATUS = "STOREVISITED_STATUS";
    public static String URL = "http://intelre.parinaam.in/webservice/intelwebservice.svc/";
    public static String URLGORIMAG = "http://intelre.parinaam.in/webservice/Imageupload.asmx/";

    public static final String KEY_PATH = "PATH";
    public static final String KEY_VERSION = "APP_VERSION";
    public static final String KEY_SUCCESS = "Success";
    public static final String KEY_FAILURE = "Failure";
    public static final String MESSAGE_INTERNET_NOT_AVALABLE = "No Internet Connection.Please Check Your Network Connection";
    public static final String MESSAGE_EXCEPTION = "Problem Occured : Report The Problem To Parinaam ";
    public static final String MESSAGE_ERROR_IN_EXECUTING = " Error in executing :";
    public static final String MESSAGE_SOCKETEXCEPTION = "Network Communication Failure. Please Check Your Network Connection";
    public static final String MESSAGE_NO_RESPONSE_SERVER = "Server Not Responding.Please try again.";
    public static final String MESSAGE_XmlPull = "Problem Occured xml pull: Report The Problem To Parinaam";
    public static final String MESSAGE_INVALID_JSON = "Problem Occured while parsing Json : invalid json data";
    public static final String MESSAGE_NUMBER_FORMATE_EXEP = "Invailid Mid";

    public static final String KEY_P = "P";
    public static final String KEY_D = "D";
    public static final String KEY_U = "U";
    public static final String KEY_C = "C";
    public static final String KEY_Y = "Y";
    public static final String KEY_N = "N";
    public static final String STORE_STATUS_LEAVE = "L";
    public static final String KEY_CHECK_IN = "I";
    ///all service key

    public static final String KEY_LOGIN_DETAILS = "LoginDetaillatest";
    public static final String KEY_DOWNLOAD_INDEX = "download_Index";
    public static final int TAG_FROM_PREVIOUS = 0;
    public static final int TAG_FROM_CURRENT = 1;
    public static final int DOWNLOAD_ALL_SERVICE = 2;
    public static final int LOGIN_SERVICE = 1;
    public static final int COVERAGE_DETAIL = 3;
    public static final int UPLOADJCPDetail = 4;
    public static final int UPLOADJsonDetail = 5;
    public static final int COVERAGEStatusDetail = 6;
    public static final int CHECKOUTDetail = 7;
    public static final int DELETE_COVERAGE = 8;
    public static final int COVERAGE_NONWORKING = 9;
    //File Path
    public static final String BACKUP_FILE_PATH = Environment.getExternalStorageDirectory() + "/INTEL_RE_backup/";
    ////for insert data key
    public static final String KEY_STORE_CD = "STORE_CD";
    public static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/.IntelRE_Images/";
    public static final String ONBACK_ALERT_MESSAGE = "Unsaved data will be lost - Do you want to continue?";
    public static final String KEY_USER_TYPE = "RIGHTNAME";
    public static final String KEY_IS_QUIZ_DONE = "is_quiz_done";

    //jeevan
    public static final String DATA_DELETE_ALERT_MESSAGE = "Saved data will be lost - Do you want to continue?";
    public static final String KEY_STORE_NAME = "STORE_NAME";
    public static final String KEY_STORE_ID = "STORE_ID";
    public static final String KEY_VISIT_DATE = "VISIT_DATE";
    public static final String KEY_LATITUDE = "LATITUDE";
    public static final String KEY_LONGITUDE = "LONGITUDE";
    public static final String KEY_REASON_ID = "REASON_ID";
    public static final String KEY_REASON = "REASON";
    public static final String KEY_IMAGE = "STORE_IMAGE";
    public static final String KEY_COVERAGE_REMARK = "REMARK";
    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_ID = "ID";
    //key for user profile
    public static final String KEY_STORE_PROFILE_STORE_NAME = "PROFILE_STORE_NAME";
    public static final String KEY_STORE_PROFILE_STORE_ADDRESS1 = "PROFILE_STORE_ADDRESS_1";
    public static final String KEY_STORE_PROFILE_CITY = "PROFILE_STORE_CITY";
    public static final String KEY_STORE_PROFILE_OWNER_NAME = "STORE_PROFILE_OWNER";
    public static final String KEY_STORE_PROFILE_CONTACT_NO = "STORE_PROFILE_CONTACT";
    public static final String KEY_STORE_PROFILE_DOB = "DOB";
    public static final String KEY_STORE_PROFILE_DOA = "DOA";
    public static final String KEY_STORE_PROFILE_VISIBILITY_LOCATION1 = "VISIBILITY_LOCATION1";
    public static final String KEY_STORE_PROFILE_VISIBILITY_LOCATION2 = "VISIBILITY_LOCATION2";
    public static final String KEY_STORE_PROFILE_VISIBILITY_LOCATION3 = "VISIBILITY_LOCATION3";
    public static final String KEY_STORE_PROFILE_DIMENTION1 = "DIMENTION1";
    public static final String KEY_STORE_PROFILE_DIMENTION2 = "DIMENTION2";
    public static final String KEY_STORE_PROFILE_DIMENTION3 = "DIMENTION3";


    public static final String TABLE_COVERAGE_DATA = "COVERAGE_DATA";
    public static final String CREATE_TABLE_COVERAGE_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_COVERAGE_DATA + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_STORE_ID
            + " INTEGER,USER_ID VARCHAR, "
            + KEY_VISIT_DATE + " VARCHAR,"
            + KEY_LATITUDE + " VARCHAR," + KEY_LONGITUDE + " VARCHAR,"
            + KEY_IMAGE + " VARCHAR,"
            + KEY_CHECKOUT_IMAGE + " VARCHAR,"
            + KEY_REASON_ID + " INTEGER," + KEY_COVERAGE_REMARK
            + " VARCHAR," + KEY_REASON + " VARCHAR)";

    public static final String TABLE_STORE_PROFILE_DATA = "STORE_PROFILE_DATA";

    public static final String CREATE_TABLE_STORE_PROFILE_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_STORE_PROFILE_DATA + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_STORE_ID
            + " INTEGER,USER_ID VARCHAR, "
            + KEY_VISIT_DATE + " VARCHAR,"
            + KEY_STORE_PROFILE_STORE_NAME + " VARCHAR," + KEY_STORE_PROFILE_STORE_ADDRESS1 + " VARCHAR,"
            + KEY_STORE_PROFILE_CITY + " VARCHAR," + KEY_STORE_PROFILE_OWNER_NAME + " VARCHAR,"
            + KEY_STORE_PROFILE_CONTACT_NO + " VARCHAR," + KEY_STORE_PROFILE_DOB + " VARCHAR,"
            + KEY_STORE_PROFILE_DOA + " VARCHAR,"
            + KEY_STORE_PROFILE_VISIBILITY_LOCATION1 + " VARCHAR,"


            + KEY_STORE_PROFILE_VISIBILITY_LOCATION2 + " VARCHAR," + KEY_STORE_PROFILE_VISIBILITY_LOCATION3 + " VARCHAR,"

            + KEY_STORE_PROFILE_DIMENTION1 + " VARCHAR,"
            + KEY_STORE_PROFILE_DIMENTION2
            + " VARCHAR," + KEY_STORE_PROFILE_DIMENTION3 + " VARCHAR)";


    //for store profile
    public static final String stpaddress1 = "Please fill store profile address 1";
    public static final String stpcontactno = "Please fill store profile contact number";
    public static final String stpcontactnolenght = "Please fill atleast 10 digit contact number";

    public static final String stpownname = "Please fill store profile owner name";
    public static final String stpdob = " Please fill store profile DOB";
    public static final String stpdoa = "Please fill store profile DOA";
    public static final String stpvisibility1 = "Please fill store profile visibility location 1";
    public static final String stpdimension1 = "Please fill store profile dimension 1";

    public static final String stpvisibility2 = "Please fill store profile visibility location 2";
    public static final String stpdimension2 = "Please fill store profile dimension 2";

    public static final String stpvisibility3 = "Please fill store profile visibility location 3";
    public static final String stpdimension3 = "Please fill store profile dimension 3";


    public static final String TABLE_INSERT_AUDIT_OPENINGHEADER_DATA = "AUDIT_OPENINGHEADER_DATA";

    public static final String CREATE_TABLE_AUDIT_OPENINGHEADER_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_INSERT_AUDIT_OPENINGHEADER_DATA + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_STORE_CD
            + " INTEGER, "
            + "QUESTION_CATEGORY_CD" + " INTEGER,"
            + " QUESTION_CATEGORY" + " VARCHAR)";


    public static final String TABLE_STORE_AUDIT_DATA = "STORE_AUDIT_DATA";

    public static final String CREATE_TABLE_STORE_AUDIT_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_STORE_AUDIT_DATA + " (" + "Common_Id"
            + " INTEGER  ," + KEY_STORE_CD
            + " INTEGER, "
            + "QUESTION_CATEGORY_CD" + " INTEGER, "

            + "QUESTION" + " VARCHAR, "
            + "QUESTION_CD" + " INTEGER, "
            + "CURRECT_ANSWER" + " VARCHAR, "
            + "ANSWER_CD" + " INTEGER, "
            + "IMAGE_ALLOW" + " VARCHAR, "
            + "IMAGEALLOW_ANS" + " VARCHAR, "
            + "AUDIT_IMG" + " VARCHAR, "
            + " QUESTION_CATEGORY" + " VARCHAR)";


    public static final String KEY_FOR_SPINNER_DROP_DOWN = "Please select dropDown answer";
    public static final String KEY_FOR_CAMERA_C = "Please click camera";


    //upendra19dec
    //  public static final String KEY_ID = "Id";
    public static final String COMMON_ID = "COMMON_ID";
    public static final String KEY_EMAILID = "Email";
    public static final String KEY_PHONENO = "Mobile";
    public static final String KEY_BRAND = "Brand_Id";
    public static final String KEY_IREP_REGISTERED = "IREP_Status";

    public static final String KEY_VISITDATE = "VISIT_DATE";
    public static final String KEY_UNIQUE_ID = "UNIQUE_ID";
    public static final String KEY_RSPID = "Rsp_Id";
    public static final String KEY_FLAG = "Flag";
    public static final String KEY_OBJECT = "OBJECT";
    public static final String KEY_RSPNAME = "Rsp_Name";
    public static final String KEY_MODE = "MODE";

    public static final String KEY_FROM_LIST = "FROM_LIST";
    public static final String KEY_FROM_ADD_STORE = "FROM_ADD_STORE";
    public static final String TABLE_INSERT_RSPDETAILS = "DR_RSPDETAILS";
    public static final String CREATE_TABLE_RSPDETAILS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_INSERT_RSPDETAILS
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + COMMON_ID + " INTEGER,"
            + KEY_STORE_ID + " INTEGER,"
            + KEY_FLAG + " VARCHAR,"
            + KEY_RSPID + " INTEGER,"
            + KEY_UNIQUE_ID + " VARCHAR,"

            + KEY_VISITDATE + " VARCHAR,"
            + KEY_RSPNAME + " VARCHAR,"
            + KEY_EMAILID + " VARCHAR,"
            + KEY_PHONENO + " INTEGER,"
            + KEY_BRAND + " VARCHAR,"
            + KEY_IREP_REGISTERED + " VARCHAR"
            + ")";

    public static final String KEY_FOR_DEPLOYMENT = "Please enter new deployment value";


    public static final String TABLE_INSERT_SOFT_MERCH_HEADER_DATA = "SOFTM_HEADER_TABLE";
    public static final String CREATE_TABLE_SOFT_MERCH_HEADER_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_INSERT_SOFT_MERCH_HEADER_DATA + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_STORE_CD + " INTEGER, "
            + KEY_VISIT_DATE + " VARCHAR, "
            + "SOFTM_NAME" + " VARCHAR, "
            + "AVAIBILITY" + " VARCHAR, "
            + "SOFTM_IMG" + " VARCHAR)";

    public static final String TABLE_VISIBILITYSOFT_MERCH_DATA = "SOFTM_TABLE";
    public static final String CREATE_TABLE_VISIBILITYSOFT_MERCH_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_VISIBILITYSOFT_MERCH_DATA + " (" + "Common_Id" + " INTEGER, "
            + KEY_STORE_CD + " INTEGER, "
            + KEY_VISIT_DATE + " VARCHAR, "
            + "SOFTM_NAME" + " VARCHAR, "
            + "AVAIBILITY" + " VARCHAR, "
            + "SOFTM_IMG" + " VARCHAR, "
            + "POSM_TYPE_Id" + " INTEGER, "
            + "POSM_TYPE" + " VARCHAR, "
            + "ANSWER_Id" + " INTEGER, "
            + "SOFT_QTY" + " INTEGER)";


    //for semi permanent
    public static final String TABLE_INSERT_VISIBILITY_SEMIPERMAN_HEADER_DATA = "SEMIP_HEADER_TABLE";
    public static final String CREATE_TABLE_VISIBILITY_SEMIPERMAN_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_INSERT_VISIBILITY_SEMIPERMAN_HEADER_DATA + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_STORE_CD + " INTEGER, "
            + KEY_VISIT_DATE + " VARCHAR, "
            + "POSM_TYPE_CD" + " INTEGER, "
            + "POSM_TYPE" + " VARCHAR, "
            + "AVAIBILITY" + " VARCHAR,"
            + " POSM_TYPE_IMG" + " VARCHAR)";


    public static final String TABLE_VISIBILITYSEMI_PERMANENT_MERCH_DATA = "SEMIP_MERCH_TABLE";

    public static final String CREATE_TABLE_VISIBILITYSEMI_PERMANENT_MERCH_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_VISIBILITYSEMI_PERMANENT_MERCH_DATA + " (" + "Common_Id" + " INTEGER, "
            + KEY_STORE_CD + " INTEGER,"
            + KEY_VISIT_DATE + " VARCHAR, "
            + "POSM_TYPE_CD" + " INTEGER, "
            + "POSM_TYPE" + " VARCHAR, "
            + "AVAIBILITY" + " VARCHAR, "
            + "POSM_TYPE_IMG" + " VARCHAR, "
            + "QUESTION_Id" + " INTEGER, "
            + "QUESTION" + " VARCHAR, "
            + "ANSWER_Id" + " INTEGER, "
            + "ANSWER" + " VARCHAR, "
            + "IMAGE_ALLOW" + " INTEGER, "
            + "DEPLOYMENT_ALLOW" + " INTEGER, "
            + "DEPLOYMENT_DATE" + " VARCHAR, "
            + "INSTALLATION_DATE" + " VARCHAR, "
            + "SEMI_IMG" + " VARCHAR)";

    public static final String KEY_FOR_CAMERA_C_ALL = "Please click all camera";

    public static final String TABLE_MARKETINFO_DATA = "MARKETINFO_DATA";


    public static final String CREATE_TABLE_MARKETINFO_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_MARKETINFO_DATA + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_STORE_CD
            + " INTEGER,"
            + " VISIT_DATE" + " VARCHAR,"
            + " BRAND" + " VARCHAR,"
            + " BRAND_CD" + " INTEGER,"

            + " TYPE" + " VARCHAR,"
            + " TYPE_CD" + " INTEGER,"

            + " INFO_TYPE" + " VARCHAR,"
            + " INFO_TYPE_CD" + " INTEGER,"

            + " REMARK" + " VARCHAR,"

            + " CHECKBOX" + " VARCHAR,"

            + " MARKET_INFO_IMG" + " VARCHAR" + ")";

    public static final String TABLE_IPOS_DATA = "IPOS_DATA";

    public static final String CREATE_TABLE_IPOS_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_IPOS_DATA + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_STORE_CD
            + " INTEGER,"
            + " VISIT_DATE" + " VARCHAR,"
            + " NOOF_DISPLAY" + " INTEGER,"
            + " NOOF_MACHINE_ON" + " INTEGER,"
            + " IPOS" + " INTEGER,"
            + " CUSTOMIZED_IPOS" + " INTEGER,"
            + " GAMING_IPOS" + " INTEGER,"
            + " IPOS_IMG" + " VARCHAR" + ")";

    public static final String TABLE_RXT_DATA = "RXT_DATA";
    public static final String CREATE_TABLE_RXT_DATA = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_RXT_DATA + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_STORE_CD + " INTEGER,"
            + " VISIT_DATE" + " VARCHAR,"
            + " NOOF_DISPLAY" + " INTEGER,"
            + " NOOF_MACHINE_ON" + " INTEGER,"
            + " ENGEGMENT" + " INTEGER,"
            + " RXT_SCREENS" + " INTEGER,"
            + " CUSTOMIZED_RXT_SCREENS" + " INTEGER,"
            + " RXT_IMG" + " VARCHAR" + ")";

    public static final String KEY_ADD = "A";
    public static final String KEY_EDIT = "E";
    public static final String KEY_DELETE = "D";

    //UPENDRA_21DEC
    public static final String TABLE_INSERT_TRAINING_DATA = "DR_TRAINING";
    public static final String KEY_RSP_CD = "RSP_CD";
    public static final String KEY_TRAINING_TYPE_CD = "TRAINING_TYPE_CD";
    public static final String KEY_TOPIC_CD = "TOPIC_CD";
    public static final String KEY_PHOTO = "PHOTO";
    public static final String KEY_RSP = "RSP";
    public static final String KEY_TRAINING_TYPE = "TRAINING_TYPE";
    public static final String KEY_TRAINING_TOPIC = "TRAINING_TOPIC";

    public static final String CREATE_TABLE_TRAINING = "CREATE TABLE IF NOT EXISTS "
            + TABLE_INSERT_TRAINING_DATA
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_STORE_ID + " INTEGER,"
            + KEY_RSP_CD + " INTEGER,"
            + KEY_TRAINING_TYPE_CD + " INTEGER,"
            + KEY_TOPIC_CD + " INTEGER,"
            + KEY_RSP + " VARCHAR,"
            + KEY_TRAINING_TYPE + " VARCHAR,"
            + KEY_TRAINING_TOPIC + " VARCHAR,"
            + KEY_PHOTO + " VARCHAR,"

            + KEY_USERNAME + " VARCHAR,"
            + KEY_UNIQUE_ID + " VARCHAR,"
            + KEY_VISITDATE + " VARCHAR"
            + ")";


    public static final String TAG_FROM_NONWORKING = "from_NonWorking";
    public static final String TAG_OBJECT = "OBJECT";
    public static final String TABLE_STORE_GEOTAGGING = "STORE_GEOTAGGING";
    public static final String CREATE_TABLE_STORE_GEOTAGGING = "CREATE TABLE IF NOT EXISTS "
            + TABLE_STORE_GEOTAGGING
            + " ("
            + "KEY_ID"
            + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + "STORE_ID"
            + " INTEGER,"

            + "LATITUDE"
            + " VARCHAR,"

            + "LONGITUDE"
            + " VARCHAR,"

            + "GEO_TAG"
            + " VARCHAR,"

            + "STATUS"
            + " VARCHAR,"

            + "FRONT_IMAGE" + " VARCHAR)";

    public static final String MESSAGE_NO_JCP = "NO JCP FOR THIS DATE";

    public static final String MESSAGE_CHANGED = "Invalid UserId Or Password / Password Has Been Changed.";
    public static final String MESSAGE_LOGIN_NO_DATA = "Data mapping error.";
    public static final String KEY_NOTICE_BOARD_LINK = "NOTICE_BOARD_LINK";


}
