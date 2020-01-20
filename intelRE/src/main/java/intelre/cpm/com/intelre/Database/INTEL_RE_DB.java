package intelre.cpm.com.intelre.Database;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import intelre.cpm.com.intelre.constant.CommonString;

import intelre.cpm.com.intelre.gettersetter.GeotaggingBeans;

import intelre.cpm.com.intelre.delegates.CoverageBean;
import intelre.cpm.com.intelre.gettersetter.StoreProfileGetterSetter;
import intelre.cpm.com.intelre.gettersetter.TrainingGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.AuditQuestion;
import intelre.cpm.com.intelre.gsonGetterSetter.AuditQuestionGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.BrandMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.BrandMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.BraveIndexScore;
import intelre.cpm.com.intelre.gsonGetterSetter.CategoryMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.InfoTypeMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.InfoTypeMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.JCPGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.gsonGetterSetter.MappingPermanentPosm;
import intelre.cpm.com.intelre.gsonGetterSetter.AllPosmTableGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.MappingSemiPermanentPosm;
import intelre.cpm.com.intelre.gsonGetterSetter.MappingSoftPosm;
import intelre.cpm.com.intelre.gsonGetterSetter.MappingSoftPosmGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.NonWorkingReason;
import intelre.cpm.com.intelre.gsonGetterSetter.NonWorkingReasonGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmType;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmTypeQuestion;
import intelre.cpm.com.intelre.gsonGetterSetter.RspDetailGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.SkuMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.SkuMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.StoreCategoryMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.TrainingTopicGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.TrainingTypeGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.WindowChecklist;
import intelre.cpm.com.intelre.gsonGetterSetter.WindowMaster;

/**
 * /**
 * Created by jeevanp on 15-12-2017.
 */

@SuppressLint("LongLogTag")
public class INTEL_RE_DB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "IntelRE_Datab";
    public static final int DATABASE_VERSION = 6;
    private SQLiteDatabase db;
    Context context;

    public INTEL_RE_DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void open() {
        try {
            db = this.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            db.execSQL(CommonString.CREATE_TABLE_RSPDETAILS);
            //jeevan
            db.execSQL(CommonString.CREATE_TABLE_COVERAGE_DATA);
            db.execSQL(CommonString.CREATE_TABLE_STORE_PROFILE_DATA);
            db.execSQL(CommonString.CREATE_TABLE_AUDIT_OPENINGHEADER_DATA);
            db.execSQL(CommonString.CREATE_TABLE_STORE_AUDIT_DATA);
            db.execSQL(CommonString.CREATE_TABLE_SOFT_MERCH_HEADER_DATA);
            db.execSQL(CommonString.CREATE_TABLE_VISIBILITYSOFT_MERCH_DATA);
            db.execSQL(CommonString.CREATE_TABLE_VISIBILITY_SEMIPERMAN_DATA);
            db.execSQL(CommonString.CREATE_TABLE_VISIBILITYSEMI_PERMANENT_MERCH_DATA);
            db.execSQL(CommonString.CREATE_TABLE_MARKETINFO_DATA);
            db.execSQL(CommonString.CREATE_TABLE_IPOS_DATA);
            db.execSQL(CommonString.CREATE_TABLE_RXT_DATA);
            //upendra21
            db.execSQL(CommonString.CREATE_TABLE_TRAINING);
            db.execSQL(CommonString.CREATE_TABLE_STORE_GEOTAGGING);

        } catch (SQLException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }

    public void deleteSpecificStoreData(String storeid) {
        try {
            db.delete(CommonString.TABLE_COVERAGE_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
            db.delete(CommonString.TABLE_STORE_PROFILE_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
            db.delete(CommonString.TABLE_INSERT_AUDIT_OPENINGHEADER_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
            db.delete(CommonString.TABLE_STORE_AUDIT_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
            db.delete(CommonString.TABLE_INSERT_SOFT_MERCH_HEADER_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
            db.delete(CommonString.TABLE_VISIBILITYSOFT_MERCH_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
            db.delete(CommonString.TABLE_INSERT_VISIBILITY_SEMIPERMAN_HEADER_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
            db.delete(CommonString.TABLE_VISIBILITYSEMI_PERMANENT_MERCH_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
            db.delete(CommonString.TABLE_MARKETINFO_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
            db.delete(CommonString.TABLE_IPOS_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
            db.delete(CommonString.TABLE_RXT_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
            db.delete(CommonString.TABLE_INSERT_RSPDETAILS, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
            db.delete(CommonString.TABLE_INSERT_TRAINING_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }


    public void deleteAllTables() {
        try {
            db.delete(CommonString.TABLE_COVERAGE_DATA, null, null);
            db.delete(CommonString.TABLE_STORE_PROFILE_DATA, null, null);
            db.delete(CommonString.TABLE_INSERT_AUDIT_OPENINGHEADER_DATA, null, null);
            db.delete(CommonString.TABLE_STORE_AUDIT_DATA, null, null);
            db.delete(CommonString.TABLE_INSERT_SOFT_MERCH_HEADER_DATA, null, null);
            db.delete(CommonString.TABLE_VISIBILITYSOFT_MERCH_DATA, null, null);
            db.delete(CommonString.TABLE_INSERT_VISIBILITY_SEMIPERMAN_HEADER_DATA, null, null);
            db.delete(CommonString.TABLE_VISIBILITYSEMI_PERMANENT_MERCH_DATA, null, null);
            db.delete(CommonString.TABLE_MARKETINFO_DATA, null, null);
            db.delete(CommonString.TABLE_IPOS_DATA, null, null);
            db.delete(CommonString.CREATE_TABLE_RXT_DATA, null, null);
            db.delete(CommonString.TABLE_INSERT_RSPDETAILS, null, null);
            db.delete(CommonString.TABLE_INSERT_TRAINING_DATA, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }

    public void deletePreviousUploadedData(String visit_date) {
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT  * from COVERAGE_DATA where VISIT_DATE < '" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getCount();
                dbcursor.close();
                if (icount > 0) {
                    db.delete(CommonString.TABLE_COVERAGE_DATA, null, null);
                    db.delete(CommonString.TABLE_STORE_PROFILE_DATA, null, null);
                    db.delete(CommonString.TABLE_INSERT_AUDIT_OPENINGHEADER_DATA, null, null);
                    db.delete(CommonString.TABLE_STORE_AUDIT_DATA, null, null);
                    db.delete(CommonString.TABLE_INSERT_SOFT_MERCH_HEADER_DATA, null, null);
                    db.delete(CommonString.TABLE_VISIBILITYSOFT_MERCH_DATA, null, null);
                    db.delete(CommonString.TABLE_INSERT_VISIBILITY_SEMIPERMAN_HEADER_DATA, null, null);
                    db.delete(CommonString.TABLE_VISIBILITYSEMI_PERMANENT_MERCH_DATA, null, null);
                    db.delete(CommonString.TABLE_MARKETINFO_DATA, null, null);
                    db.delete(CommonString.TABLE_IPOS_DATA, null, null);
                    db.delete(CommonString.CREATE_TABLE_RXT_DATA, null, null);
                    db.delete(CommonString.TABLE_INSERT_RSPDETAILS, null, null);
                    db.delete(CommonString.TABLE_INSERT_TRAINING_DATA, null, null);
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public int createtable(String sqltext) {
        try {
            db.execSQL(sqltext);
            return 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }


    public boolean insertJCPData(JCPGetterSetter data) {
        db.delete("Journey_Plan", null, null);
        List<JourneyPlan> jcpList = data.getJourneyPlan();

        ContentValues values = new ContentValues();
        try {
            if (jcpList.size() == 0) {
                return false;
            }

            for (int i = 0; i < jcpList.size(); i++) {

                values.put("Store_Id", jcpList.get(i).getStoreId());
                values.put("Visit_Date", jcpList.get(i).getVisitDate());
                values.put("Store_Name", jcpList.get(i).getStoreName());
                values.put("Address1", jcpList.get(i).getAddress1());
                values.put("Address2", jcpList.get(i).getAddress2());
                values.put("Landmark", jcpList.get(i).getLandmark());
                values.put("Pincode", jcpList.get(i).getPincode());
                values.put("Contact_Person", jcpList.get(i).getContactPerson());
                values.put("Contact_No", jcpList.get(i).getContactNo());
                values.put("City", jcpList.get(i).getCity());
                values.put("Store_Type", jcpList.get(i).getStoreType());
                values.put("Store_Category", jcpList.get(i).getStoreCategory());
                values.put("Classification", jcpList.get(i).getClassification());
                values.put("Store_Type_Id", jcpList.get(i).getStoreTypeId());
                values.put("Classification_Id", jcpList.get(i).getClassificationId());
                values.put("Store_Category_Id", jcpList.get(i).getStoreCategoryId());
                values.put("Reason_Id", jcpList.get(i).getReasonId());
                values.put("Upload_Status", jcpList.get(i).getUploadStatus());
                values.put("Geo_Tag", jcpList.get(i).getGeoTag());
                values.put("Distributor_Id", jcpList.get(i).getDistributorId());
                values.put("City_Id", jcpList.get(i).getCityId());
                values.put("Visibility_Location1", jcpList.get(i).getVisibilityLocation1());
                values.put("Visibility_Location2", jcpList.get(i).getVisibilityLocation2());
                values.put("Visibility_Location3", jcpList.get(i).getVisibilityLocation3());
                values.put("Dimension1", jcpList.get(i).getDimension1());
                values.put("Dimension2", jcpList.get(i).getDimension2());
                values.put("Dimension3", jcpList.get(i).getDimension3());
                values.put("Region_id", jcpList.get(i).getRegionId());

                long id = db.insert("Journey_Plan", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Exception in Jcp", ex.toString());
            return false;
        }
    }

    public boolean insertNonWorkingData(NonWorkingReasonGetterSetter nonWorkingdata) {
        db.delete("Non_Working_Reason", null, null);
        ContentValues values = new ContentValues();
        List<NonWorkingReason> data = nonWorkingdata.getNonWorkingReason();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Reason_Id", data.get(i).getReasonId());
                values.put("Reason", data.get(i).getReason());
                values.put("Entry_Allow", data.get(i).getEntryAllow());
                values.put("Image_Allow", data.get(i).getImageAllow());
                values.put("GPS_Mandatory", data.get(i).getGPSMandatory());

                long id = db.insert("Non_Working_Reason", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insertPosmMaster(PosmMasterGetterSetter posmmaster) {
        db.delete("Posm_Master", null, null);
        List<PosmMaster> list = posmmaster.getPosmMaster();

        ContentValues values = new ContentValues();
        try {
            if (list.size() == 0) {
                return false;
            }
            for (int i = 0; i < list.size(); i++) {
                values.put("Posm_Id", list.get(i).getPosmId());
                values.put("Posm", list.get(i).getPosm());
                values.put("Posm_Type", list.get(i).getPosmType());
                values.put("Posm_Type_Id", list.get(i).getPosmTypeId());
                long id = db.insert("Posm_Master", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Exception ", " in Posm_Master " + ex.toString());
            return false;
        }
    }


    public boolean insertAuditQuestionData(AuditQuestionGetterSetter auditdata) {
        db.delete("Audit_Question", null, null);
        ContentValues values = new ContentValues();
        List<AuditQuestion> data = auditdata.getAuditQuestion();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Question_Id", data.get(i).getQuestionId());
                values.put("Question", data.get(i).getQuestion());
                values.put("Answer", data.get(i).getAnswer());
                values.put("Answer_Id", data.get(i).getAnswerId());
                values.put("Image_Allow", data.get(i).getImageAllow());
                values.put("Question_Category", data.get(i).getQuestionCategory());
                values.put("Question_Category_Id", data.get(i).getQuestionCategoryId());
                values.put("Question_Id", data.get(i).getQuestionId());
                values.put("ImageAllow", data.get(i).getImageAllowforanswer());
                long id = db.insert("Audit_Question", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insertRspDetailnData(RspDetailGetterSetter rspDetail) {
        db.delete("Rsp_Detail", null, null);
        ContentValues values = new ContentValues();
        List<StoreCategoryMaster> data = rspDetail.getStoreCategoryMaster();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Email", data.get(i).getEmail());
                values.put("Mobile", data.get(i).getMobile());
                values.put("Rsp_Id", data.get(i).getRspId());
                values.put("Rsp_Name", data.get(i).getRspName());
                values.put("Store_Id", data.get(i).getStoreId());
                values.put("Brand_Id", data.get(i).getBrandId());
                values.put("IREP_Status", data.get(i).getIREPStatus());

                long id = db.insert("Rsp_Detail", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    //upendra_18_12_2017 Training_Type
    public boolean insertTrainingTypeData(TrainingTypeGetterSetter trainingType) {
        db.delete("Training_Type", null, null);
        ContentValues values = new ContentValues();
        List<WindowMaster> data = trainingType.getWindowMaster();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Training_Type", data.get(i).getTrainingType());
                values.put("Training_Type_Id", data.get(i).getTrainingTypeId());


                long id = db.insert("Training_Type", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insertTrainingTopicData(TrainingTopicGetterSetter TrainingTopic) {
        db.delete("Training_Topic", null, null);
        ContentValues values = new ContentValues();
        List<WindowChecklist> data = TrainingTopic.getWindowChecklist();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Topic", data.get(i).getTopic());
                values.put("Topic_Id", data.get(i).getTopicId());
                values.put("Training_Type_Id", data.get(i).getTrainingTypeId());


                long id = db.insert("Training_Topic", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insertMappingSoftPosmData(AllPosmTableGetterSetter MappingSoft) {
        db.delete("Mapping_SoftPosm", null, null);
        ContentValues values = new ContentValues();
        List<MappingSoftPosm> data = MappingSoft.getMappingSoftPosm();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {
                values.put("Region_Id", data.get(i).getRegionId());
                values.put("Classification_Id", data.get(i).getClassificationId());
                values.put("Store_Type_Id", data.get(i).getStoreTypeId());
                values.put("Posm_Type_Id", data.get(i).getPosmTypeId());
                long id = db.insert("Mapping_SoftPosm", null, values);

                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insertmappingsemiparmanentsoft(AllPosmTableGetterSetter MappingPermanent) {
        db.delete("Mapping_semi_permanent_posm", null, null);
        ContentValues values = new ContentValues();
        List<MappingSemiPermanentPosm> data = MappingPermanent.getMappingSemiPermanentPosm();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {
                values.put("Posm_Type_Id", data.get(i).getPosmTypeId());
                values.put("Store_Id", data.get(i).getStoreId());
                long id = db.insert("Mapping_semi_permanent_posm", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insertposmTypeQuestion(AllPosmTableGetterSetter MappingPermanent) {
        db.delete("Posm_Type_Question", null, null);
        ContentValues values = new ContentValues();
        List<PosmTypeQuestion> data = MappingPermanent.getPosmTypeQuestion();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {
                values.put("Question_Id", data.get(i).getQuestionId());
                values.put("Question", data.get(i).getQuestion());
                values.put("Posm_Type_Id", data.get(i).getPosmTypeId());
                values.put("Answer_Id", data.get(i).getAnswerId());
                values.put("Answer", data.get(i).getAnswer());
                values.put("Image_Allow", data.get(i).getImageAllow());
                values.put("Question_Disable", data.get(i).getQuestionDisable());
                long id = db.insert("Posm_Type_Question", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insertposmTypedata(AllPosmTableGetterSetter MappingPermanent) {
        db.delete("Posm_Type", null, null);
        ContentValues values = new ContentValues();
        List<PosmType> data = MappingPermanent.getPosmType();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {
                values.put("Posm_Type_Id", data.get(i).getPosmTypeId());
                values.put("Posm_Type", data.get(i).getPosmType());
                values.put("Type", data.get(i).getType());
                long id = db.insert("Posm_Type", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insertbraveindex_scoredata(AllPosmTableGetterSetter braveindex) {
        db.delete("Brave_Index_Score", null, null);
        ContentValues values = new ContentValues();
        List<BraveIndexScore> data = braveindex.getBraveIndexScore();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {
                values.put("Store_Id", data.get(i).getStoreId());
                values.put("Time_Period", data.get(i).getTimePeriod());
                values.put("SM_Score", data.get(i).getSMScore());
                values.put("TNA_Score", data.get(i).getTNAScore());
                values.put("Visibility_Score", data.get(i).getVisibilityScore());
                values.put("Brave_Index", data.get(i).getBraveIndex());
                long id = db.insert("Brave_Index_Score", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }


    public boolean insertCategoryMasterData(CategoryMasterGetterSetter CategoryMaster) {
        db.delete("Category_Master", null, null);
        ContentValues values = new ContentValues();
        List<intelre.cpm.com.intelre.gsonGetterSetter.CategoryMaster> data = CategoryMaster.getCategoryMaster();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Category", data.get(i).getCategory());
                values.put("Category_Id", data.get(i).getCategoryId());
                values.put("Category_Sequence", data.get(i).getCategorySequence());


                long id = db.insert("Category_Master", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insertBrandMasterData(BrandMasterGetterSetter BrandMaster) {
        db.delete("Brand_Master", null, null);
        ContentValues values = new ContentValues();
        List<intelre.cpm.com.intelre.gsonGetterSetter.BrandMaster> data = BrandMaster.getBrandMaster();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Brand", data.get(i).getBrand());
                values.put("Brand_Id", data.get(i).getBrandId());
                values.put("Brand_Sequence", data.get(i).getBrandSequence());
                values.put("Category_Id", data.get(i).getCategoryId());


                long id = db.insert("Brand_Master", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insertSkuMasterData(SkuMasterGetterSetter BrandMaster) {
        db.delete("Sku_Master", null, null);
        ContentValues values = new ContentValues();
        List<SkuMaster> data = BrandMaster.getSkuMaster();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Brand_Id", data.get(i).getBrandId());
                values.put("Sku", data.get(i).getSku());
                values.put("Sku_Id", data.get(i).getSkuId());
                values.put("Sku_Sequence", data.get(i).getSkuSequence());

                long id = db.insert("Sku_Master", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }


    public boolean insertInfoTypeData(InfoTypeMasterGetterSetter infotype) {
        db.delete("Info_Type_Master", null, null);
        ContentValues values = new ContentValues();
        List<InfoTypeMaster> data = infotype.getInfoTypeMaster();
        try {
            if (data.size() == 0) {
                return false;
            }
            for (int i = 0; i < data.size(); i++) {
                values.put("Info_Type_Id", data.get(i).getInfoTypeId());
                values.put("Info_Type", data.get(i).getInfoType());
                long id = db.insert("Info_Type_Master", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }


    public ArrayList<JourneyPlan> getStoreData(String date) {
        ArrayList<JourneyPlan> list = new ArrayList<JourneyPlan>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT  * FROM Journey_Plan  " + "WHERE Visit_Date ='" + date + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    JourneyPlan sb = new JourneyPlan();
                    sb.setStoreId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Id"))));
                    sb.setVisitDate((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visit_Date"))));
                    sb.setStoreName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Name")));
                    sb.setAddress1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address1")));
                    sb.setAddress2((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address2"))));
                    sb.setLandmark(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Landmark")));
                    sb.setPincode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Pincode")));
                    sb.setContactPerson(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Contact_Person")));
                    sb.setContactNo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Contact_No")));
                    sb.setCity(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City")));
                    sb.setStoreType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type")));
                    sb.setStoreCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Category")));
                    sb.setClassification(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Classification")));
                    sb.setRegionId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Region_Id"))));
                    sb.setStoreTypeId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type_Id"))));
                    sb.setClassificationId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Classification_Id"))));
                    sb.setStoreCategoryId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Category_Id"))));
                    sb.setReasonId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));
                    sb.setDistributorId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Distributor_Id"))));
                    sb.setCityId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City_Id"))));
                    sb.setVisibilityLocation1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visibility_Location1")));
                    sb.setVisibilityLocation2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visibility_Location2")));
                    sb.setVisibilityLocation3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visibility_Location3")));
                    sb.setDimension1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Dimension1")));
                    sb.setDimension2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Dimension2")));
                    sb.setDimension3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Dimension3")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }


        return list;
    }

    //jeevan   nmjnmn,
    @SuppressLint("LongLogTag")
    public boolean isCoverageDataFilled(String visit_date) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM COVERAGE_DATA " + "where " + CommonString.KEY_VISIT_DATE + "<>'" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getCount();
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }

            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!", e.toString());
            Crashlytics.logException(e);
            return filled;
        }

        return filled;
    }

    //jeevan   nmjnmn,
    @SuppressLint("LongLogTag")
    public long InsertCoverageData(CoverageBean data) {
        db.delete(CommonString.TABLE_COVERAGE_DATA, "STORE_ID" + "='" + data.getStoreId() + "' AND VISIT_DATE='" + data.getVisitDate() + "'", null);
        ContentValues values = new ContentValues();
        long l = 0;
        try {
            values.put(CommonString.KEY_STORE_ID, data.getStoreId());
            values.put(CommonString.KEY_USER_ID, data.getUserId());
            values.put(CommonString.KEY_VISIT_DATE, data.getVisitDate());
            values.put(CommonString.KEY_LATITUDE, data.getLatitude());
            values.put(CommonString.KEY_LONGITUDE, data.getLongitude());
            values.put(CommonString.KEY_IMAGE, data.getImage());
            values.put(CommonString.KEY_COVERAGE_REMARK, data.getRemark());
            values.put(CommonString.KEY_REASON_ID, data.getReasonid());
            values.put(CommonString.KEY_REASON, data.getReason());
            values.put(CommonString.KEY_CHECKOUT_IMAGE, data.getCkeckout_image());
            l = db.insert(CommonString.TABLE_COVERAGE_DATA, null, values);
        } catch (Exception ex) {
            Crashlytics.logException(ex);
            Log.d("Database Exception while Insert Closes Data ", ex.toString());
        }
        return l;
    }

    //jeevan   nmjnmn,
    @SuppressLint("LongLogTag")
    public ArrayList<CoverageBean> getSpecificCoverageData(String visitdate, String store_cd) {
        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from " + CommonString.TABLE_COVERAGE_DATA + " where " + CommonString.KEY_VISIT_DATE + "='" + visitdate + "' AND " + CommonString.KEY_STORE_ID + "='" + store_cd + "'", null);


            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();
                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setUserId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID)));
                    sb.setVisitDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setLatitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)));
                    sb.setLongitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)));
                    sb.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE)));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON)));
                    sb.setReasonid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID)));
                    sb.setMID(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))));
                    sb.setCkeckout_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CHECKOUT_IMAGE)));
                    sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());

        }

        return list;

    }


    //jeevan   nmjnmn,
    public long updateJaurneyPlanSpecificStoreStatus(String storeid, String visit_date, String status) {
        long l = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("Upload_Status", status);
            l = db.update("Journey_Plan", values, " Store_Id ='" + storeid + "' AND Visit_Date ='" + visit_date + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return l;
    }

    //jeevan   nmjnmn,
    public ArrayList<JourneyPlan> getSpecificStoreData(String store_cd) {
        ArrayList<JourneyPlan> list = new ArrayList<JourneyPlan>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from Journey_Plan  " + "where Store_Id ='" + store_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    JourneyPlan sb = new JourneyPlan();
                    sb.setStoreId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Id"))));
                    sb.setVisitDate((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visit_Date"))));
                    sb.setStoreName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Name")));
                    sb.setAddress1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address1")));
                    sb.setAddress2((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address2"))));
                    sb.setLandmark(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Landmark")));
                    sb.setPincode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Pincode")));
                    sb.setContactPerson(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Contact_Person")));
                    sb.setContactNo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Contact_No")));
                    sb.setCity(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City")));
                    sb.setStoreType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type")));
                    sb.setStoreCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Category")));
                    sb.setClassification(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Classification")));
                    sb.setRegionId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Region_Id"))));
                    sb.setStoreTypeId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type_Id"))));
                    sb.setClassificationId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Classification_Id"))));
                    sb.setStoreCategoryId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Category_Id"))));
                    sb.setReasonId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));
                    sb.setDistributorId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Distributor_Id"))));
                    sb.setCityId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City_Id"))));
                    sb.setVisibilityLocation1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visibility_Location1")));
                    sb.setVisibilityLocation2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visibility_Location2")));
                    sb.setVisibilityLocation3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visibility_Location3")));
                    sb.setDimension1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Dimension1")));
                    sb.setDimension2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Dimension2")));
                    sb.setDimension3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Dimension3")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }


        return list;
    }


    //jeevan   nmjnmn,
    public long insertStoreProfileData(
            String user_id, String storeid, String visit_date, StoreProfileGetterSetter save_listDataHeader) {
        db.delete(CommonString.TABLE_STORE_PROFILE_DATA, " STORE_ID" + "='" + storeid + "' AND VISIT_DATE='" + visit_date + "'", null);
        ContentValues values = new ContentValues();
        long l = 0;
        try {
            db.beginTransaction();
            if (!save_listDataHeader.getProfileStoreName().isEmpty()) {
                values.put(CommonString.KEY_STORE_ID, storeid);
                values.put("USER_ID", user_id);
                values.put(CommonString.KEY_VISIT_DATE, visit_date);
                values.put(CommonString.KEY_STORE_PROFILE_STORE_NAME, save_listDataHeader.getProfileStoreName());
                values.put(CommonString.KEY_STORE_PROFILE_STORE_ADDRESS1, save_listDataHeader.getProfileAddress1());
                values.put(CommonString.KEY_STORE_PROFILE_CITY, save_listDataHeader.getProfileCity());
                values.put(CommonString.KEY_STORE_PROFILE_OWNER_NAME, save_listDataHeader.getProfileOwner());
                values.put(CommonString.KEY_STORE_PROFILE_CONTACT_NO, save_listDataHeader.getProfileContact());
                values.put(CommonString.KEY_STORE_PROFILE_DOB, save_listDataHeader.getProfileDOB());
                values.put(CommonString.KEY_STORE_PROFILE_DOA, save_listDataHeader.getProfileDOA());

                values.put(CommonString.KEY_STORE_PROFILE_VISIBILITY_LOCATION1, save_listDataHeader.getProfileVisibilityLocation1());
                values.put(CommonString.KEY_STORE_PROFILE_VISIBILITY_LOCATION2, save_listDataHeader.getProfileVisibilityLocation2());
                values.put(CommonString.KEY_STORE_PROFILE_VISIBILITY_LOCATION3, save_listDataHeader.getProfileVisibilityLocation3());
                values.put(CommonString.KEY_STORE_PROFILE_DIMENTION1, save_listDataHeader.getProfileDimension1());
                values.put(CommonString.KEY_STORE_PROFILE_DIMENTION2, save_listDataHeader.getProfileDimension2());
                values.put(CommonString.KEY_STORE_PROFILE_DIMENTION3, save_listDataHeader.getProfileDimension3());

                l = db.insert(CommonString.TABLE_STORE_PROFILE_DATA, null, values);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Header Data " + ex.toString());
        }
        return l;
    }

    //jeevan   nmjnmn,
    public StoreProfileGetterSetter getStoreProfileData(String store_cd, String visit_date) {
        StoreProfileGetterSetter sb = new StoreProfileGetterSetter();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM STORE_PROFILE_DATA WHERE STORE_ID ='" + store_cd + "' AND VISIT_DATE='" + visit_date + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    sb.setProfileStoreName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROFILE_STORE_NAME")));
                    sb.setProfileAddress1((dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROFILE_STORE_ADDRESS_1"))));
                    sb.setProfileCity(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROFILE_STORE_CITY")));
                    sb.setProfileOwner(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_PROFILE_OWNER")));
                    sb.setProfileContact((dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_PROFILE_CONTACT"))));
                    sb.setProfileDOB(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DOB")));
                    sb.setProfileDOA(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DOA")));
                    sb.setProfileVisibilityLocation1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIBILITY_LOCATION1")));
                    sb.setProfileVisibilityLocation2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIBILITY_LOCATION2")));
                    sb.setProfileVisibilityLocation3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIBILITY_LOCATION3")));
                    sb.setProfileDimension1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DIMENTION1")));
                    sb.setProfileDimension2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DIMENTION2")));
                    sb.setProfileDimension3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DIMENTION3")));
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return sb;
        }

        return sb;
    }

    //jeevan   nmjnmn,
    public ArrayList<StoreCategoryMaster> getRspDetailData(String storeId) {
        ArrayList<StoreCategoryMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * FROM Rsp_Detail  " + "WHERE Store_Id ='" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StoreCategoryMaster sb = new StoreCategoryMaster();
                    //changessss
                    sb.setRspId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Rsp_Id"))));
                    sb.setRspName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Rsp_Name")));
                    sb.setEmail(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Email")));
                    sb.setMobile(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Mobile")));
                    sb.setStoreId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Id"))));
                    sb.setBrandId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand_Id"))));
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("IREP_Status")).equals("1")) {
                        sb.setIREPStatus(true);
                    } else {
                        sb.setIREPStatus(false);
                    }
                    sb.setRsp_uniqueID("");
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
            return list;
        }
        return list;
    }


    public long InsertRspDetailData(StoreCategoryMaster data, String storeid, String visitdate) {
        db.delete(CommonString.TABLE_INSERT_RSPDETAILS, CommonString.KEY_ID + "='" + data.getKey_id() + "'", null);
        ContentValues values = new ContentValues();
        long id = 0;
        try {
            values.put(CommonString.KEY_STORE_ID, storeid);
            values.put(CommonString.KEY_VISITDATE, visitdate);
            //CHANGE ADDDD
            values.put(CommonString.KEY_UNIQUE_ID, data.getRsp_uniqueID());
            values.put(CommonString.KEY_RSPID, data.getRspId());
            values.put(CommonString.KEY_FLAG, data.getFlag());
            values.put(CommonString.KEY_RSPNAME, data.getRspName());
            values.put(CommonString.KEY_EMAILID, data.getEmail());
            values.put(CommonString.KEY_PHONENO, data.getMobile());
            values.put(CommonString.KEY_BRAND, data.getBrandId());
            values.put(CommonString.KEY_IREP_REGISTERED, data.getIREPStatus());
            id = db.insert(CommonString.TABLE_INSERT_RSPDETAILS, null, values);
            if (id > 0) {
                return id;
            } else {
                return 0;
            }
        } catch (Exception ex) {
            Log.d("Database Exception ", ex.toString());
            return 0;
        }

    }

    public void deleteRspDetail(StoreCategoryMaster data) {

        db.delete(CommonString.TABLE_INSERT_RSPDETAILS, CommonString.KEY_ID + "='" + data.getKey_id() + "'", null);
    }

    public ArrayList<AuditQuestion> getStoreAuditHeaderData() {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<AuditQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  DISTINCT Question_Category , Question_Category_Id from Audit_Question ", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AuditQuestion sb = new AuditQuestion();
                    sb.setQuestionCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Question_Category")));
                    sb.setQuestionCategoryId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Question_Category_Id"))));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<AuditQuestion> getStoreAuditChildData(int ques_category_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<AuditQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT Question,Question_Id FROM Audit_Question WHERE Question_Category_Id =" + ques_category_cd + " ", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AuditQuestion sb = new AuditQuestion();
                    sb.setQuestion(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Question")));
                    sb.setQuestionId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Question_Id"))));
                    sb.setImageAllowforanswer("");
                    sb.setCurrectanswer("");
                    sb.setCurrectanswerCd("0");
                    sb.setAudit_cam("");
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<AuditQuestion> getauditAnswerData(String question_id) {
        ArrayList<AuditQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("select DISTINCT Answer_Id,Answer,ImageAllow from Audit_Question where Question_Id ='" + question_id + "' ", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AuditQuestion df = new AuditQuestion();
                    df.setAnswerId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Answer_Id"))));
                    df.setAnswer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Answer")));
                    df.setImageAllowforanswer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ImageAllow")));
                    list.add(df);
                    dbcursor.moveToNext();
                }

                dbcursor.close();
                return list;
            }
        } catch (Exception e) {

            return list;
        }

        return list;

    }


    public void insertStoreAuditData(String storeid,
                                     HashMap<AuditQuestion,
                                             List<AuditQuestion>> data, List<AuditQuestion> save_listDataHeader) {
        db.delete(CommonString.TABLE_INSERT_AUDIT_OPENINGHEADER_DATA, " STORE_CD='" + storeid + "'", null);
        db.delete(CommonString.TABLE_STORE_AUDIT_DATA, " STORE_CD='" + storeid + "'", null);

        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put("STORE_CD", storeid);
                values.put("QUESTION_CATEGORY_CD", save_listDataHeader.get(i).getQuestionCategoryId());
                values.put("QUESTION_CATEGORY", save_listDataHeader.get(i).getQuestionCategory());
                long l = db.insert(CommonString.TABLE_INSERT_AUDIT_OPENINGHEADER_DATA, null, values);
                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    values1.put("Common_Id", (int) l);
                    values1.put("STORE_CD", storeid);
                    values1.put("QUESTION_CATEGORY_CD", save_listDataHeader.get(i).getQuestionCategoryId());
                    values1.put("QUESTION_CATEGORY", save_listDataHeader.get(i).getQuestionCategory());
                    values1.put("QUESTION", data.get(save_listDataHeader.get(i)).get(j).getQuestion());
                    values1.put("QUESTION_CD", data.get(save_listDataHeader.get(i)).get(j).getQuestionId());
                    values1.put("CURRECT_ANSWER", data.get(save_listDataHeader.get(i)).get(j).getCurrectanswer());
                    values1.put("ANSWER_CD", Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getCurrectanswerCd()));
                    values1.put("AUDIT_IMG", data.get(save_listDataHeader.get(i)).get(j).getAudit_cam());


                    values1.put("IMAGE_ALLOW", data.get(save_listDataHeader.get(i)).get(j).getImageAllow());
                    values1.put("IMAGEALLOW_ANS", data.get(save_listDataHeader.get(i)).get(j).getImageAllowforanswer());

                    db.insert(CommonString.TABLE_STORE_AUDIT_DATA, null, values1);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Posm Master Data " + ex.toString());
        }
    }

    public ArrayList<AuditQuestion> getStoreAuditInsertedData(String store_cd, int questCategory_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<AuditQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM STORE_AUDIT_DATA WHERE STORE_CD ='" + store_cd + "' AND QUESTION_CATEGORY_CD=" + questCategory_cd + "", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AuditQuestion sb = new AuditQuestion();
                    sb.setQuestion(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION")));
                    sb.setQuestionId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION_CD"))));
                    sb.setImageAllow(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_ALLOW")));
                    sb.setCurrectanswer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_CD")));
                    sb.setCurrectanswerCd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_CD")));
                    sb.setAudit_cam(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AUDIT_IMG")));
                    sb.setImageAllowforanswer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGEALLOW_ANS")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    //Upendra
    public ArrayList<BrandMaster> getBrandMasterData() {
        ArrayList<BrandMaster> list = new ArrayList<BrandMaster>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM Brand_Master", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    BrandMaster sb = new BrandMaster();

                    sb.setBrandId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand_Id"))));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand")));
                    sb.setCategoryId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Category_Id"))));
                    sb.setBrandSequence(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand_Sequence"))));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {

            return list;
        }

        return list;
    }

    public ArrayList<PosmTypeQuestion> getsoftm_posmChilddata(int region_cd, int classification_cd, int store_type_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<PosmTypeQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery(" select distinct pt.Posm_Type_Id,pt.Posm_Type from Posm_Type pt inner join Mapping_SoftPosm mp on mp.Posm_Type_Id=pt.Posm_Type_Id where mp.Region_Id=" + region_cd + " and mp.Classification_Id=" + classification_cd + " and mp.Store_Type_Id=" + store_type_cd + " ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PosmTypeQuestion sb = new PosmTypeQuestion();
                    sb.setPosmTypeId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Posm_Type_Id")));
                    sb.setPosmType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Posm_Type")));
                    sb.setSemip_right_ans("");
                    sb.setSemip_right_ans_Id("0");
                    sb.setPromoType_qty("");
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;

            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<PosmTypeQuestion> getinsertedSoftMerchVisibility(String store_Id, String visit_date) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<PosmTypeQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from SOFTM_HEADER_TABLE where STORE_CD='" + store_Id + "' and VISIT_DATE='" + visit_date + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PosmTypeQuestion sb = new PosmTypeQuestion();
                    sb.setSoft_merch_name(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SOFTM_NAME")));
                    sb.setHeader_availebility(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AVAIBILITY")));
                    sb.setHeader_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SOFTM_IMG")));
                    list.add(sb);
                    dbcursor.moveToNext();

                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public long insertVisibilitySoftMerchData(String storeid, String visit_date, HashMap<PosmTypeQuestion,
            List<PosmTypeQuestion>> data, List<PosmTypeQuestion> save_listDataHeader) {
        db.delete(CommonString.TABLE_INSERT_SOFT_MERCH_HEADER_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "' and VISIT_DATE='" + visit_date + "'", null);
        db.delete(CommonString.TABLE_VISIBILITYSOFT_MERCH_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "' and VISIT_DATE='" + visit_date + "'", null);

        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();
        long l2 = 0;
        try {
            db.beginTransaction();

            for (int i = 0; i < save_listDataHeader.size(); i++) {

                values.put(CommonString.KEY_STORE_CD, storeid);
                values.put(CommonString.KEY_VISIT_DATE, visit_date);
                values.put("SOFTM_NAME", save_listDataHeader.get(i).getSoft_merch_name());
                values.put("AVAIBILITY", save_listDataHeader.get(i).getHeader_availebility());
                if (save_listDataHeader.get(i).getHeader_availebility().equalsIgnoreCase("No")) {
                    values.put("SOFTM_IMG", "");
                } else {
                    values.put("SOFTM_IMG", save_listDataHeader.get(i).getHeader_image());
                }
                long l = db.insert(CommonString.TABLE_INSERT_SOFT_MERCH_HEADER_DATA, null, values);

                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    values1.put("Common_Id", (int) l);
                    values1.put(CommonString.KEY_STORE_CD, storeid);
                    values1.put(CommonString.KEY_VISIT_DATE, visit_date);
                    values1.put("SOFTM_NAME", save_listDataHeader.get(i).getSoft_merch_name());
                    values1.put("AVAIBILITY", save_listDataHeader.get(i).getHeader_availebility());
                    if (save_listDataHeader.get(i).getHeader_availebility().equalsIgnoreCase("No")) {
                        values1.put("SOFTM_IMG", "");
                        values1.put("POSM_TYPE_Id", data.get(save_listDataHeader.get(i)).get(j).getPosmTypeId());
                        values1.put("POSM_TYPE", data.get(save_listDataHeader.get(i)).get(j).getPosmType());
                        values1.put("ANSWER_Id", "0");
                        values1.put("SOFT_QTY", "");

                    } else {
                        values1.put("SOFTM_IMG", save_listDataHeader.get(i).getHeader_image());
                        values1.put("POSM_TYPE_Id", data.get(save_listDataHeader.get(i)).get(j).getPosmTypeId());
                        values1.put("POSM_TYPE", data.get(save_listDataHeader.get(i)).get(j).getPosmType());
                        values1.put("ANSWER_Id", data.get(save_listDataHeader.get(i)).get(j).getSemip_right_ans_Id());
                        values1.put("SOFT_QTY", data.get(save_listDataHeader.get(i)).get(j).getPromoType_qty());

                    }
                    l2 = db.insert(CommonString.TABLE_VISIBILITYSOFT_MERCH_DATA, null, values1);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Posm Master Data " + ex.toString());
        }
        return l2;
    }


    public ArrayList<PosmTypeQuestion> getVisibilitySoftMerchInsertedData(String store_cd, String visit_date) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<PosmTypeQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM SOFTM_TABLE WHERE STORE_CD ='" + store_cd + "' and VISIT_DATE='" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PosmTypeQuestion sb = new PosmTypeQuestion();
                    sb.setPosmTypeId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("POSM_TYPE_Id")));
                    sb.setPosmType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("POSM_TYPE")));
                    sb.setSemip_right_ans_Id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_Id")));
                    sb.setPromoType_qty(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SOFT_QTY")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<PosmTypeQuestion> getsemiparmanentheaderData(int store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<PosmTypeQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select distinct pt.Posm_Type_Id,pt.Posm_Type from Posm_Type pt inner join Mapping_semi_permanent_posm  mp on pt.Posm_Type_Id=mp.Posm_Type_Id " +
                    "where pt.Type='Permanant' and mp.Store_Id=" + store_cd + "", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PosmTypeQuestion sb = new PosmTypeQuestion();
                    sb.setPosmTypeId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Posm_Type_Id"))));
                    sb.setPosmType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Posm_Type")));
                    sb.setHeader_availebility("0");
                    sb.setHeader_image("");
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<PosmTypeQuestion> getVisibilitySemiPermanetMerchChildData(int posm_type_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<PosmTypeQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("Select distinct pq.Question_Id,pq.Question from Posm_Type_Question pq inner join Mapping_semi_permanent_posm mp on pq.Posm_Type_Id=mp.Posm_Type_Id " +
                    "where pq.Posm_Type_Id=" + posm_type_cd + " and pq.Answer_Id<>0", null);
            // and pq.Answer_Id<>0
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PosmTypeQuestion sb = new PosmTypeQuestion();
                    sb.setQuestionId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Question_Id")));
                    sb.setQuestion(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Question")));
                    sb.setSemip_currectimageAllow("0");
                    sb.setSemip_right_ans_Id("0");
                    sb.setSemip_right_ans("");
                    sb.setDeployment_date("");
                    sb.setDeployment_flag("0");
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public long insertVisibilitySemiParmanetMerchData(String storeid, String visit_date,
                                                      HashMap<PosmTypeQuestion,
                                                              List<PosmTypeQuestion>> data, List<PosmTypeQuestion> save_listDataHeader) {
        db.delete(CommonString.TABLE_INSERT_VISIBILITY_SEMIPERMAN_HEADER_DATA, " STORE_CD='" + storeid + "'", null);
        db.delete(CommonString.TABLE_VISIBILITYSEMI_PERMANENT_MERCH_DATA, " STORE_CD='" + storeid + "'", null);

        ContentValues values = new ContentValues();
        ContentValues value_child = new ContentValues();
        long l2 = 0;

        try {
            db.beginTransaction();
            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put(CommonString.KEY_STORE_CD, storeid);
                values.put(CommonString.KEY_VISIT_DATE, visit_date);
                values.put("POSM_TYPE_CD", save_listDataHeader.get(i).getPosmTypeId().toString());
                values.put("POSM_TYPE", save_listDataHeader.get(i).getPosmType());
                values.put("AVAIBILITY", save_listDataHeader.get(i).getHeader_availebility());
                if (save_listDataHeader.get(i).getHeader_availebility().equalsIgnoreCase("No")) {
                    values.put("POSM_TYPE_IMG", "");
                } else {
                    values.put("POSM_TYPE_IMG", save_listDataHeader.get(i).getHeader_image());
                }

                long l = db.insert(CommonString.TABLE_INSERT_VISIBILITY_SEMIPERMAN_HEADER_DATA, null, values);

                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    value_child.put("Common_Id", (int) l);
                    value_child.put(CommonString.KEY_STORE_CD, storeid);
                    value_child.put(CommonString.KEY_VISIT_DATE, visit_date);
                    value_child.put("POSM_TYPE_CD", save_listDataHeader.get(i).getPosmTypeId().toString());
                    value_child.put("POSM_TYPE", save_listDataHeader.get(i).getPosmType());
                    value_child.put("AVAIBILITY", save_listDataHeader.get(i).getHeader_availebility());

                    if (save_listDataHeader.get(i).getHeader_availebility().equalsIgnoreCase("No")) {
                        value_child.put("POSM_TYPE_IMG", "");
                        value_child.put("QUESTION_Id", data.get(save_listDataHeader.get(i)).get(j).getQuestionId().toString());
                        value_child.put("QUESTION", data.get(save_listDataHeader.get(i)).get(j).getQuestion());
                        value_child.put("ANSWER_Id", "0");
                        value_child.put("ANSWER", "");
                        value_child.put("IMAGE_ALLOW", "0");
                        value_child.put("DEPLOYMENT_ALLOW", "0");
                        value_child.put("DEPLOYMENT_DATE", "0");
                        value_child.put("INSTALLATION_DATE", "");
                        value_child.put("SEMI_IMG", "");
                    } else {
                        value_child.put("POSM_TYPE_IMG", save_listDataHeader.get(i).getHeader_image());
                        value_child.put("QUESTION_Id", data.get(save_listDataHeader.get(i)).get(j).getQuestionId().toString());
                        value_child.put("QUESTION", data.get(save_listDataHeader.get(i)).get(j).getQuestion());
                        value_child.put("ANSWER_Id", data.get(save_listDataHeader.get(i)).get(j).getSemip_right_ans_Id());
                        value_child.put("ANSWER", data.get(save_listDataHeader.get(i)).get(j).getSemip_right_ans());
                        value_child.put("IMAGE_ALLOW", data.get(save_listDataHeader.get(i)).get(j).getSemip_currectimageAllow());
                        value_child.put("DEPLOYMENT_ALLOW", data.get(save_listDataHeader.get(i)).get(j).getDeployment_flag());
                        value_child.put("DEPLOYMENT_DATE", data.get(save_listDataHeader.get(i)).get(j).getDeployment_date());
                        value_child.put("INSTALLATION_DATE", data.get(save_listDataHeader.get(i)).get(j).getInstallation_date());
                        value_child.put("SEMI_IMG", data.get(save_listDataHeader.get(i)).get(j).getSemipmerch_img());

                    }
                    l2 = db.insert(CommonString.TABLE_VISIBILITYSEMI_PERMANENT_MERCH_DATA, null, value_child);

                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Posm Master Data " + ex.toString());
        }
        return l2;
    }

    public ArrayList<PosmTypeQuestion> getVisibilitySemiPermanetMerchInsertedData(String store_cd, int posm_Type_Id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<PosmTypeQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from SEMIP_MERCH_TABLE where STORE_CD ='" + store_cd + "' and POSM_TYPE_CD =" + posm_Type_Id + " ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PosmTypeQuestion sb = new PosmTypeQuestion();
                    sb.setQuestionId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("QUESTION_Id")));
                    sb.setQuestion(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION")));
                    sb.setSemip_right_ans_Id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_Id")));
                    sb.setSemip_right_ans(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER")));
                    sb.setSemip_currectimageAllow(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGE_ALLOW")));
                    sb.setDeployment_flag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DEPLOYMENT_ALLOW")));
                    sb.setDeployment_date(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DEPLOYMENT_DATE")));
                    sb.setInstallation_date(dbcursor.getString(dbcursor.getColumnIndexOrThrow("INSTALLATION_DATE")));
                    sb.setSemipmerch_img(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SEMI_IMG")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<PosmTypeQuestion> getsemip_insertedHeaderdata(String store_cd, String visit_date) {
        ArrayList<PosmTypeQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from SEMIP_HEADER_TABLE where STORE_CD='" + store_cd + "' and VISIT_DATE='" + visit_date + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PosmTypeQuestion sb = new PosmTypeQuestion();
                    sb.setPosmTypeId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("POSM_TYPE_CD")));
                    sb.setPosmType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("POSM_TYPE")));
                    sb.setHeader_availebility(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AVAIBILITY")));
                    sb.setHeader_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow("POSM_TYPE_IMG")));
                    list.add(sb);
                    dbcursor.moveToNext();

                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<BrandMaster> getbranddataformarketinfo() {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<BrandMaster> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT br.Brand_Id, br.Brand, ca.Category,ca.Category_Id FROM Brand_Master br INNER JOIN Category_Master ca on br.Category_Id = ca.Category_id\n" +
                    "where br.Brand_Id <> 1", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    BrandMaster sb = new BrandMaster();
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand")));
                    sb.setBrandId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand_Id"))));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Category")));
                    sb.setCategoryId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Category_Id"))));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<InfoTypeMaster> getInfoTypeData() {
        ArrayList<InfoTypeMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * FROM Info_Type_Master", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    InfoTypeMaster sb = new InfoTypeMaster();
                    sb.setInfoTypeId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Info_Type_Id"))));
                    sb.setInfoType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Info_Type")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {

            return list;
        }


        return list;
    }

    public long deleteMarketInfoData(String storeid) {
        long l = 0;
        l = db.delete(CommonString.TABLE_MARKETINFO_DATA, "STORE_CD" + "='" + storeid + "'", null);
        return l;
    }

    @SuppressLint("LongLogTag")
    public long insertMarketinfoData(
            String storeid, String visit_date, ArrayList<InfoTypeMaster> secCompleteMarketDATA) {
        db.delete(CommonString.TABLE_MARKETINFO_DATA, "STORE_CD" + "='" + storeid + "'", null);
        ContentValues values = new ContentValues();
        long l = 0;
        try {
            for (int i = 0; i < secCompleteMarketDATA.size(); i++) {
                values.put("STORE_CD", storeid);
                values.put("VISIT_DATE", visit_date);
                values.put("BRAND", secCompleteMarketDATA.get(i).getBrand());
                values.put("BRAND_CD", secCompleteMarketDATA.get(i).getBrand_cd());
                values.put("TYPE", secCompleteMarketDATA.get(i).getType());
                values.put("TYPE_CD", secCompleteMarketDATA.get(i).getType_cd());
                values.put("INFO_TYPE", secCompleteMarketDATA.get(i).getInfoType());
                values.put("INFO_TYPE_CD", secCompleteMarketDATA.get(i).getInfoTypeId());

                values.put("REMARK", secCompleteMarketDATA.get(i).getRemark());
                values.put("MARKET_INFO_IMG", secCompleteMarketDATA.get(i).getMarketinfo_img());
                values.put("CHECKBOX", secCompleteMarketDATA.get(i).isExistsFlag());


                db.insert(CommonString.TABLE_MARKETINFO_DATA, null, values);

            }

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Facing Competition Data ", ex.toString());
        }

        return l;
    }

    public ArrayList<InfoTypeMaster> getinfotypeinsetedDATA(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<InfoTypeMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM MARKETINFO_DATA" + " WHERE STORE_CD ='" + store_cd + " '", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    InfoTypeMaster sb = new InfoTypeMaster();
                    sb.setBrand_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND")));
                    sb.setType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("TYPE")));
                    sb.setType_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("TYPE_CD")));
                    sb.setInfoType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("INFO_TYPE")));
                    sb.setInfoTypeId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("INFO_TYPE_CD"))));
                    sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow("REMARK")));
                    sb.setMarketinfo_img(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MARKET_INFO_IMG")));
                    sb.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID)));
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("CHECKBOX")).equals("1")) {
                        sb.setExistsFlag(true);
                    } else {
                        sb.setExistsFlag(false);
                    }
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public void remove(String user_id) {
        db.execSQL("DELETE FROM MARKETINFO_DATA WHERE " + CommonString.KEY_ID + " = '" + user_id + "'");
    }


    public ArrayList<SkuMaster> getSkuMasterData() {
        ArrayList<SkuMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * FROM Sku_Master", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SkuMaster sb = new SkuMaster();
                    sb.setSkuId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Sku_Id"))));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Sku")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {

            return list;
        }


        return list;
    }

    @SuppressLint("LongLogTag")
    public long insertIPOSData(String storeid, String visit_date, SkuMaster ipos_object) {
        db.delete(CommonString.TABLE_IPOS_DATA, "STORE_CD" + "='" + storeid + "'", null);
        ContentValues values = new ContentValues();
        long l = 0;

        try {

            values.put("STORE_CD", storeid);
            values.put("VISIT_DATE", visit_date);
            values.put("NOOF_DISPLAY", ipos_object.getNoof_display());
            values.put("NOOF_MACHINE_ON", ipos_object.getMachine_on());
            values.put("IPOS", ipos_object.getIpos());
            values.put("IPOS", ipos_object.getIpos());
            values.put("CUSTOMIZED_IPOS", ipos_object.getCustomised_ipos());
            values.put("GAMING_IPOS", ipos_object.getGaming_ipos());
            values.put("IPOS_IMG", ipos_object.getIpos_img());

            l = db.insert(CommonString.TABLE_IPOS_DATA, null, values);

        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.d("Database Exception while Insert Facing Competition Data ", e.toString());
        }

        return l;
    }


    public SkuMaster getipos_inserteddata_object(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        SkuMaster sb = new SkuMaster();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM IPOS_DATA" + " WHERE STORE_CD ='" + store_cd + " '", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    sb.setNoof_display(dbcursor.getString(dbcursor.getColumnIndexOrThrow("NOOF_DISPLAY")));
                    sb.setMachine_on(dbcursor.getString(dbcursor.getColumnIndexOrThrow("NOOF_MACHINE_ON")));
                    sb.setIpos(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IPOS")));
                    sb.setCustomised_ipos(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CUSTOMIZED_IPOS")));
                    sb.setIpos_img(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IPOS_IMG")));
                    sb.setGaming_ipos(dbcursor.getString(dbcursor.getColumnIndexOrThrow("GAMING_IPOS")));
                    sb.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID)));

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return sb;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return sb;
    }

    @SuppressLint("LongLogTag")
    public long insertRXTData(String storeid, String visit_date, SkuMaster rxt_Object) {
        db.delete(CommonString.TABLE_RXT_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        ContentValues values = new ContentValues();
        long l = 0;

        try {

            values.put("STORE_CD", storeid);
            values.put("VISIT_DATE", visit_date);

            values.put("NOOF_DISPLAY", rxt_Object.getNoof_display());
            values.put("NOOF_MACHINE_ON", rxt_Object.getMachine_on());
            values.put("ENGEGMENT", rxt_Object.getEngegment());
            values.put("RXT_SCREENS", rxt_Object.getRxt());
            values.put("CUSTOMIZED_RXT_SCREENS", rxt_Object.getCustomised_ipos());
            values.put("RXT_IMG", rxt_Object.getRxt_img());

            l = db.insert(CommonString.TABLE_RXT_DATA, null, values);

        } catch (Exception ex) {
            Log.d("Database Exception while Insert Facing Competition Data ", ex.toString());
        }

        return l;
    }


    public boolean isRXTFilled(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT NOOF_DISPLAY FROM RXT_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("NOOF_DISPLAY")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!", e.toString());
            return filled;
        }

        return filled;


    }


    public SkuMaster getrxt_insertedData(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        SkuMaster sb = new SkuMaster();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM RXT_DATA" + " WHERE STORE_CD ='" + store_cd + " '", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    sb.setNoof_display(dbcursor.getString(dbcursor.getColumnIndexOrThrow("NOOF_DISPLAY")));
                    sb.setMachine_on(dbcursor.getString(dbcursor.getColumnIndexOrThrow("NOOF_MACHINE_ON")));
                    sb.setEngegment(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ENGEGMENT")));
                    sb.setRxt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("RXT_SCREENS")));
                    sb.setCustomised_ipos(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CUSTOMIZED_RXT_SCREENS")));
                    sb.setRxt_img(dbcursor.getString(dbcursor.getColumnIndexOrThrow("RXT_IMG")));
                    sb.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID)));

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return sb;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return sb;
    }


    @SuppressLint("LongLogTag")
    // isIPOSFilled
    public boolean isIPOSFilled(String storeId) {
        boolean filled = false;

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT NOOF_DISPLAY FROM IPOS_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("NOOF_DISPLAY")).isEmpty()) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }

                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!", e.toString());
            return filled;
        }

        return filled;


    }

    @SuppressLint("LongLogTag")
    public boolean isMarketInfoFilled(String storeId) {
        boolean filled = false;

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT BRAND_CD FROM MARKETINFO_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }

                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!", e.toString());
            return filled;
        }

        return filled;


    }

    public boolean isVisibilitySoftMerchFilled(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT POSM_TYPE_Id FROM SOFTM_TABLE WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("POSM_TYPE_Id")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!", e.toString());
            return filled;
        }

        return filled;


    }

    @SuppressLint("LongLogTag")
    public boolean isVisibilitySPMerchFilled(String storeId) {
        boolean filled = false;

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT QUESTION_Id FROM SEMIP_MERCH_TABLE WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION_Id")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;
    }

    @SuppressLint("LongLogTag")
    public boolean isStoreAuditFilled(String storeId) {
        boolean filled = false;

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT QUESTION_CD FROM STORE_AUDIT_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION_CD")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;
    }


    public long updateCoverageCheckoutIMG(String storeid, String visit_date, String checkout_img) {
        long l = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(CommonString.KEY_CHECKOUT_IMAGE, checkout_img);
            l = db.update("COVERAGE_DATA", values, " STORE_ID ='" + storeid + "' AND VISIT_DATE ='" + visit_date + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    @SuppressLint("LongLogTag")
    public ArrayList<NonWorkingReason> getNonWorkingDataByFlag(boolean flag) {
        Log.d("FetchingAssetdata--------------->Start<------------",
                "------------------");
        ArrayList<NonWorkingReason> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM Non_Working_Reason", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (flag) {
                        NonWorkingReason sb = new NonWorkingReason();
                        String entry_allow_fortest = dbcursor.getString(dbcursor.getColumnIndexOrThrow("Entry_Allow"));
                        if (entry_allow_fortest.equals("1")) {
                            sb.setReasonId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                            sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason")));
                            String entry_allow = dbcursor.getString(dbcursor.getColumnIndexOrThrow("Entry_Allow"));
                            if (entry_allow.equals("1")) {
                                sb.setEntryAllow(true);
                            } else {
                                sb.setEntryAllow(false);
                            }
                            String image_allow = dbcursor.getString(dbcursor.getColumnIndexOrThrow("Image_Allow"));
                            if (image_allow.equals("1")) {
                                sb.setImageAllow(true);
                            } else {
                                sb.setImageAllow(false);
                            }
                            String gps_mendtry = dbcursor.getString(dbcursor.getColumnIndexOrThrow("GPS_Mandatory"));
                            if (gps_mendtry.equals("1")) {
                                sb.setGPSMandatory(true);
                            } else {
                                sb.setGPSMandatory(false);
                            }

                            list.add(sb);
                        }


                    } else {
                        NonWorkingReason sb = new NonWorkingReason();
                        sb.setReasonId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                        sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason")));
                        String entry_allow = dbcursor.getString(dbcursor.getColumnIndexOrThrow("Entry_Allow"));
                        if (entry_allow.equals("1")) {
                            sb.setEntryAllow(true);
                        } else {
                            sb.setEntryAllow(false);
                        }
                        String image_allow = dbcursor.getString(dbcursor.getColumnIndexOrThrow("Image_Allow"));
                        if (image_allow.equals("1")) {
                            sb.setImageAllow(true);
                        } else {
                            sb.setImageAllow(false);
                        }
                        String gps_mendtry = dbcursor.getString(dbcursor.getColumnIndexOrThrow("GPS_Mandatory"));
                        if (gps_mendtry.equals("1")) {
                            sb.setGPSMandatory(true);
                        } else {
                            sb.setGPSMandatory(false);
                        }

                        list.add(sb);
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Non working!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching non working data---------------------->Stop<-----------",
                "-------------------");
        return list;
    }

    public ArrayList<WindowMaster> getTrainingTypeData() {
        ArrayList<WindowMaster> list = new ArrayList<WindowMaster>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM Training_Type", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    WindowMaster sb = new WindowMaster();
                    sb.setTrainingTypeId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Training_Type_Id"))));
                    sb.setTrainingType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Training_Type")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {

            return list;
        }


        return list;
    }

    public ArrayList<WindowChecklist> getTrainingTopicData(String training_nameid) {

        ArrayList<WindowChecklist> list = new ArrayList<WindowChecklist>();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("Select * from Training_Topic " + "where Training_Type_Id='" + training_nameid + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    WindowChecklist sb = new WindowChecklist();

                    sb.setTrainingTypeId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Training_Type_Id"))));
                    sb.setTopicId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Topic_Id"))));
                    sb.setTopic(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Topic")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            return list;
        }


        return list;
    }

    public long insertTrainingData(String store_id, String user_name, String visit_date, ArrayList<TrainingGetterSetter> list) {
        db.delete(CommonString.TABLE_INSERT_TRAINING_DATA, CommonString.KEY_STORE_ID + "='" + store_id + "'AND VISIT_DATE='" + visit_date + "'", null);
        long l = 0;
        ContentValues values = new ContentValues();
        try {

            for (int i = 0; i < list.size(); i++) {
                values.put(CommonString.KEY_STORE_ID, store_id);
                values.put(CommonString.KEY_RSP_CD, list.get(i).getRspname_cd());
                values.put(CommonString.KEY_USERNAME, user_name);
                values.put(CommonString.KEY_TRAINING_TYPE_CD, list.get(i).getTrainingtype_cd());
                values.put(CommonString.KEY_TOPIC_CD, list.get(i).getTopic_cd());
                values.put(CommonString.KEY_PHOTO, list.get(i).getPhoto());
                values.put(CommonString.KEY_VISITDATE, visit_date);
                values.put(CommonString.KEY_RSP, list.get(i).getRspname());
                values.put(CommonString.KEY_TRAINING_TYPE, list.get(i).getTrainingtype());
                values.put(CommonString.KEY_TRAINING_TOPIC, list.get(i).getTopic());

                values.put(CommonString.KEY_UNIQUE_ID, list.get(i).getUnoque_RSPID());

                l = db.insert(CommonString.TABLE_INSERT_TRAINING_DATA, null, values);
            }
        } catch (Exception ex) {
            Log.d("Database Exception  ", ex.toString());
        }
        return l;
    }

    public ArrayList<TrainingGetterSetter> getinsertedTrainingData(String store_id, String visit_date) {
        Log.d("Fetching", "Storedata--------------->Start<------------");

        ArrayList<TrainingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from DR_TRAINING where STORE_ID='" + store_id + "'AND VISIT_DATE='" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    TrainingGetterSetter sb = new TrainingGetterSetter();

                    sb.setRspname_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_RSP_CD)));
                    sb.setTrainingtype_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_TRAINING_TYPE_CD)));
                    sb.setTopic_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_TOPIC_CD)));
                    sb.setPhoto(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_PHOTO)));
                    sb.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID)));
                    sb.setRspname(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_RSP)));
                    sb.setTrainingtype(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_TRAINING_TYPE)));
                    sb.setTopic(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_TRAINING_TOPIC)));
                    sb.setUnoque_RSPID(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_UNIQUE_ID)));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching opening stock!!!!!!!!!!! " + e.toString());
            return list;
        }
        Log.d("Fetching", " opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<TrainingGetterSetter> getinsertedTrainingDataForRSPGRZero(String store_id, String visit_date) {
        Log.d("Fetching", "Storedata--------------->Start<------------");

        ArrayList<TrainingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from DR_TRAINING where STORE_ID='" + store_id + "'AND VISIT_DATE='" + visit_date + "' AND RSP_CD>'0'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    TrainingGetterSetter sb = new TrainingGetterSetter();

                    sb.setRspname_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_RSP_CD)));
                    sb.setTrainingtype_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_TRAINING_TYPE_CD)));
                    sb.setTopic_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_TOPIC_CD)));
                    sb.setPhoto(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_PHOTO)));
                    sb.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID)));
                    sb.setRspname(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_RSP)));
                    sb.setTrainingtype(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_TRAINING_TYPE)));
                    sb.setTopic(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_TRAINING_TOPIC)));
                    sb.setUnoque_RSPID(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_UNIQUE_ID)));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching opening stock!!!!!!!!!!! " + e.toString());
            return list;
        }
        Log.d("Fetching", " opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<TrainingGetterSetter> getinsertedTrainingDataForRSPZero(String store_id, String visit_date) {
        Log.d("Fetching", "Storedata--------------->Start<------------");

        ArrayList<TrainingGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from DR_TRAINING where STORE_ID='" + store_id + "'AND VISIT_DATE='" + visit_date + "' AND RSP_CD='0'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    TrainingGetterSetter sb = new TrainingGetterSetter();

                    sb.setRspname_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_RSP_CD)));
                    sb.setTrainingtype_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_TRAINING_TYPE_CD)));
                    sb.setTopic_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_TOPIC_CD)));
                    sb.setPhoto(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_PHOTO)));
                    sb.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID)));
                    sb.setRspname(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_RSP)));
                    sb.setTrainingtype(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_TRAINING_TYPE)));
                    sb.setTopic(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_TRAINING_TOPIC)));
                    sb.setUnoque_RSPID(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_UNIQUE_ID)));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching opening stock!!!!!!!!!!! " + e.toString());
            return list;
        }
        Log.d("Fetching", " opening stock---------------------->Stop<-----------");
        return list;
    }


    public void remove_training(String user_id) {
        db.execSQL("DELETE FROM DR_TRAINING WHERE ID = '" + user_id + "'");
    }

    public void remove_trainingByTopic_cd(String topic_cd) {
        db.execSQL("DELETE FROM DR_TRAINING WHERE TOPIC_CD = '" + topic_cd + "'");
    }



    public ArrayList<StoreCategoryMaster> getRspDetailinsertData(String storeId) {

        ArrayList<StoreCategoryMaster> list = new ArrayList<StoreCategoryMaster>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * FROM DR_RSPDETAILS  " + "WHERE STORE_ID ='" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StoreCategoryMaster sb = new StoreCategoryMaster();
                    sb.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID)));
                    //change by jeeva
                    sb.setRspId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Rsp_Id"))));
                    sb.setRsp_uniqueID(dbcursor.getString(dbcursor.getColumnIndexOrThrow("UNIQUE_ID")));
                    sb.setRspName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Rsp_Name")));
                    sb.setEmail(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Email")));
                    sb.setMobile(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Mobile")));
                    sb.setStoreId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID))));
                    sb.setBrandId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand_Id"))));
                    String flag = dbcursor.getString(dbcursor.getColumnIndexOrThrow("IREP_Status"));
                    if (flag.equals("1")) {
                        sb.setIREPStatus(true);
                    } else {
                        sb.setIREPStatus(false);

                    }
                    sb.setFlag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Flag")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {

            Log.d("Exception when fetching Non working!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching non working data-------",
                "-------------------");
        return list;
    }


    public ArrayList<BraveIndexScore> getbraveindex_report(String storeId) {
        ArrayList<BraveIndexScore> list = new ArrayList();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select  * from Brave_Index_Score where Store_Id ='" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    BraveIndexScore sb = new BraveIndexScore();
                    //change by jeeva
                    sb.setTimePeriod((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Time_Period"))));
                    sb.setSMScore(dbcursor.getDouble(dbcursor.getColumnIndexOrThrow("SM_Score")));
                    sb.setTNAScore(dbcursor.getDouble(dbcursor.getColumnIndexOrThrow("TNA_Score")));
                    sb.setVisibilityScore(dbcursor.getDouble(dbcursor.getColumnIndexOrThrow("Visibility_Score")));
                    sb.setBraveIndex(dbcursor.getDouble(dbcursor.getColumnIndexOrThrow("Brave_Index")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {

            Log.d("Exception when fetching Non working!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching non working data-------",
                "-------------------");
        return list;
    }


    @SuppressLint("LongLogTag")
    public ArrayList<CoverageBean> getCoverageData(String visitdate) {
        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_COVERAGE_DATA +
                    " WHERE " + CommonString.KEY_VISIT_DATE + "='" + visitdate + "'", null);


            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();
                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setUserId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID)));
                    sb.setVisitDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setLatitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)));
                    sb.setLongitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)));
                    sb.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE)));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON)));
                    sb.setReasonid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID)));
                    sb.setMID(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))));
                    sb.setCkeckout_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CHECKOUT_IMAGE)));
                    sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());

        }

        return list;

    }

    public ArrayList<AuditQuestion> getStoreAuditData(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<AuditQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM STORE_AUDIT_DATA WHERE STORE_CD ='" + store_cd + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    AuditQuestion sb = new AuditQuestion();
                    sb.setQuestion(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION")));
                    sb.setQuestionId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION_CD"))));
                    sb.setCurrectanswerCd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_CD")));
                    sb.setAudit_cam(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AUDIT_IMG")));
                    sb.setImageAllowforanswer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IMAGEALLOW_ANS")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<PosmMaster> getVisibilitySoftMerchData(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<PosmMaster> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM VISIBILITYSOFT_MERCH_DATA WHERE STORE_CD ='" + store_cd + "' ", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PosmMaster sb = new PosmMaster();
                    sb.setPosmId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIBILITY_POSM_CD"))));
                    sb.setPosm(dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIBILITY_POSM")));
                    sb.setDeployment_Value(dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIBILITY_NEWDEPLOYMENT")));
                    sb.setSoft_merchIMG(dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIBILITY_SOFTIMG")));

                    sb.setPosmTypeId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("POSM_TYPE_CD"))));
                    sb.setPosmType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("POSM_TYPE")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<PosmTypeQuestion> getSoftmerchvisibilityHeaderData(String store_cd, String visit_date) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<PosmTypeQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM SOFTM_HEADER_TABLE WHERE STORE_CD ='" + store_cd + "' and VISIT_DATE='" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PosmTypeQuestion sb = new PosmTypeQuestion();
                    sb.setSoft_merch_name(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SOFTM_NAME")));
                    sb.setHeader_availebility(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AVAIBILITY")));
                    sb.setHeader_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SOFTM_IMG")));
                    sb.setKey_Id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID)));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<PosmTypeQuestion> getsoftmerchChild(String store_cd, String visit_date, String common_Id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<PosmTypeQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM SOFTM_TABLE WHERE STORE_CD ='" + store_cd + "' and VISIT_DATE='" + visit_date + "' and Common_Id='" + common_Id + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PosmTypeQuestion sb = new PosmTypeQuestion();
                    sb.setPosmTypeId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("POSM_TYPE_Id")));
                    sb.setSemip_right_ans_Id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_Id")));
                    sb.setPromoType_qty(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SOFT_QTY")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<PosmTypeQuestion> getsemip_headerList(String store_cd, String visit_date) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<PosmTypeQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM SEMIP_HEADER_TABLE WHERE STORE_CD ='" + store_cd + "' and VISIT_DATE='" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PosmTypeQuestion sb = new PosmTypeQuestion();
                    sb.setPosmTypeId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("POSM_TYPE_CD")));
                    sb.setHeader_availebility(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AVAIBILITY")));
                    sb.setHeader_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow("POSM_TYPE_IMG")));
                    sb.setKey_Id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID)));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<PosmTypeQuestion> getsemipermmerchChild(String store_cd, String visit_date, String common_Id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<PosmTypeQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM SEMIP_MERCH_TABLE WHERE STORE_CD ='" + store_cd + "' and VISIT_DATE='" + visit_date + "' and Common_Id='" + common_Id + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PosmTypeQuestion sb = new PosmTypeQuestion();
                    sb.setQuestionId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("QUESTION_Id")));
                    sb.setSemip_right_ans(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER")));
                    sb.setDeployment_flag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DEPLOYMENT_ALLOW")));
                    sb.setDeployment_date(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DEPLOYMENT_DATE")));
                    sb.setInstallation_date(dbcursor.getString(dbcursor.getColumnIndexOrThrow("INSTALLATION_DATE")));
                    sb.setSemipmerch_img(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SEMI_IMG")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<MappingPermanentPosm> getVisibilitySemiPermanetMerchData(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<MappingPermanentPosm> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM VISIBILITYSEMI_PERMANENT_MERCH_DATA" +
                    " WHERE STORE_CD ='" + store_cd + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    MappingPermanentPosm sb = new MappingPermanentPosm();
                    sb.setPosmId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SP_VISIBILITY_POSM_CD"))));
                    sb.setPosm(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SP_VISIBILITY_POSM")));

                    sb.setPrev_Qty(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SP_PREVIOUS"))));
                    sb.setPreV_dValue(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SP_PREVIOUS_EDT")));

                    sb.setNewDeploymnt_Value(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SP_VISIBILITY_NEWDEPLOYMENT")));
                    sb.setsPermanetIMG_1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SP_IMG_1")));

                    sb.setsPermanetIMG_2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SP_IMG_2")));
                    sb.setsPermanetIMG_3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SP_IMG_3")));

                    //  sb.setDeployment_Value(dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIBILITY_NEWDEPLOYMENT")));
                    // sb.setSoft_merchIMG(dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIBILITY_SOFTIMG")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<JourneyPlan> getSpecificStoreDatawithdate(String visit_date, String store_cd) {
        ArrayList<JourneyPlan> list = new ArrayList<JourneyPlan>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * from Journey_Plan  " +
                    "where Visit_Date ='" + visit_date + "' AND Store_Id='" + store_cd + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    JourneyPlan sb = new JourneyPlan();
                    sb.setStoreId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Id"))));
                    sb.setVisitDate((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visit_Date"))));
                    sb.setStoreName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Name")));
                    sb.setAddress1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address1")));
                    sb.setAddress2((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address2"))));
                    sb.setLandmark(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Landmark")));
                    sb.setPincode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Pincode")));
                    sb.setContactPerson(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Contact_Person")));
                    sb.setContactNo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Contact_No")));
                    sb.setCity(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City")));
                    sb.setStoreType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type")));
                    sb.setStoreCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Category")));
                    sb.setClassification(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Classification")));
                    sb.setRegionId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Region_Id"))));
                    sb.setStoreTypeId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type_Id"))));
                    sb.setClassificationId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Classification_Id"))));
                    sb.setStoreCategoryId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Category_Id"))));
                    sb.setReasonId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));
                    sb.setDistributorId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Distributor_Id"))));
                    sb.setCityId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City_Id"))));
                    sb.setVisibilityLocation1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visibility_Location1")));
                    sb.setVisibilityLocation2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visibility_Location2")));
                    sb.setVisibilityLocation3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visibility_Location3")));
                    sb.setDimension1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Dimension1")));
                    sb.setDimension2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Dimension2")));
                    sb.setDimension3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Dimension3")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }


        return list;
    }

    @SuppressLint("LongLogTag")
    public ArrayList<CoverageBean> getcoverageDataPrevious(String visitdate) {
        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from " +
                    CommonString.TABLE_COVERAGE_DATA + " where " + CommonString.KEY_VISIT_DATE + "<>'" + visitdate + "'", null);


            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();
                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setUserId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID)));
                    sb.setVisitDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setLatitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)));
                    sb.setLongitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)));
                    sb.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE)));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON)));
                    sb.setReasonid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID)));
                    sb.setMID(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))));
                    sb.setCkeckout_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CHECKOUT_IMAGE)));
                    sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());

        }

        return list;

    }

    public JourneyPlan getSpecificStoreDataPrevious(String date, String store_id) {
        JourneyPlan sb = new JourneyPlan();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT * from Journey_Plan  " +
                    "where Visit_Date <> '" + date + "' AND Store_Id='" + store_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    sb.setStoreId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Id"))));
                    sb.setVisitDate((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visit_Date"))));
                    sb.setStoreName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Name")));
                    sb.setAddress1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address1")));
                    sb.setAddress2((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address2"))));
                    sb.setLandmark(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Landmark")));
                    sb.setPincode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Pincode")));
                    sb.setContactPerson(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Contact_Person")));
                    sb.setContactNo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Contact_No")));
                    sb.setCity(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City")));
                    sb.setStoreType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type")));
                    sb.setStoreCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Category")));
                    sb.setClassification(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Classification")));
                    sb.setRegionId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Region_Id"))));
                    sb.setStoreTypeId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type_Id"))));
                    sb.setClassificationId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Classification_Id"))));
                    sb.setStoreCategoryId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Category_Id"))));
                    sb.setReasonId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));
                    sb.setDistributorId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Distributor_Id"))));
                    sb.setCityId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City_Id"))));
                    sb.setVisibilityLocation1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visibility_Location1")));
                    sb.setVisibilityLocation2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visibility_Location2")));
                    sb.setVisibilityLocation3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visibility_Location3")));
                    sb.setDimension1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Dimension1")));
                    sb.setDimension2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Dimension2")));
                    sb.setDimension3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Dimension3")));
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return sb;
        }

        return sb;
    }

//upendra 26 dec

    public void updateStatus(String id, String status) {
        ContentValues values = new ContentValues();
        try {
            values.put("GEO_TAG", status);
            db.update("Journey_Plan", values, CommonString.KEY_STORE_ID + "='" + id + "'", null);
        } catch (Exception ex) {
        }
    }

    public long InsertSTOREgeotag(String storeid, double lat, double longitude, String path, String status) {

        db.delete(CommonString.TABLE_STORE_GEOTAGGING, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        ContentValues values = new ContentValues();
        try {
            values.put("STORE_ID", storeid);
            values.put("LATITUDE", Double.toString(lat));
            values.put("LONGITUDE", Double.toString(longitude));
            values.put("FRONT_IMAGE", path);
            values.put("GEO_TAG", status);
            values.put("STATUS", status);

            return db.insert(CommonString.TABLE_STORE_GEOTAGGING, null, values);

        } catch (Exception ex) {
            Log.d("Database Exception ", ex.toString());
            return 0;
        }
    }

    public ArrayList<GeotaggingBeans> getinsertGeotaggingData(String storeid, String status) {
        ArrayList<GeotaggingBeans> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from " + CommonString.TABLE_STORE_GEOTAGGING + "" +
                    " where " + CommonString.KEY_STORE_ID + " ='" + storeid + "' and " + CommonString.KEY_STATUS + " = '" + status + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    GeotaggingBeans geoTag = new GeotaggingBeans();
                    geoTag.setStoreid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    geoTag.setLatitude(Double.parseDouble(
                            dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE))));
                    geoTag.setLongitude(Double.parseDouble(dbcursor.getString(dbcursor.getColumnIndexOrThrow
                            (CommonString.KEY_LONGITUDE))));
                    geoTag.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FRONT_IMAGE")));
                    list.add(geoTag);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception Brands",
                    e.toString());
            return list;
        }
        return list;

    }

    public long updateInsertedGeoTagStatus(String id, String status) {
        ContentValues values = new ContentValues();
        try {
            values.put("GEO_TAG", status);
            values.put("STATUS", status);
            return db.update(CommonString.TABLE_STORE_GEOTAGGING, values, CommonString.KEY_STORE_ID + "='" + id + "'", null);
        } catch (Exception ex) {
            return 0;
        }
    }

    public ArrayList<PosmTypeQuestion> getvisibilitysemip_answer(int question_Id) {
        Log.d("FetchingAssetdata--------------->Start<------------",
                "------------------");
        ArrayList<PosmTypeQuestion> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("select distinct Answer_Id ,Answer,Image_Allow,Question_Disable from Posm_Type_Question where Question_Id=" + question_Id + " and Answer_Id<>0", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PosmTypeQuestion sb = new PosmTypeQuestion();
                    sb.setAnswerId(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Answer_Id")));
                    sb.setAnswer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Answer")));
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("Image_Allow")).equals("1")) {
                        sb.setImageAllow(true);
                    } else {
                        sb.setImageAllow(false);
                    }
                    sb.setQuestionDisable(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("Question_Disable")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.d("Exception when fetching Non working!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching non working data---------------------->Stop<-----------",
                "-------------------");
        return list;
    }

}
