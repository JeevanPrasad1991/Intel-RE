package intelre.cpm.com.intelre.upload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.MultipartBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.AlertandMessages;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.delegates.CoverageBean;
import intelre.cpm.com.intelre.gettersetter.GeotaggingBeans;
import intelre.cpm.com.intelre.gettersetter.StoreProfileGetterSetter;
import intelre.cpm.com.intelre.gettersetter.TrainingGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.AuditQuestion;
import intelre.cpm.com.intelre.gsonGetterSetter.ImageNMResponseGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.InfoTypeMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.gsonGetterSetter.MappingPermanentPosm;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmTypeQuestion;
import intelre.cpm.com.intelre.gsonGetterSetter.SkuMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.StoreCategoryMaster;
import intelre.cpm.com.intelre.retrofit.PostApi;
import intelre.cpm.com.intelre.retrofit.PostApiForUpload;
import intelre.cpm.com.intelre.retrofit.StringConverterFactory;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PreviousDataUploadActivity extends AppCompatActivity {
    INTEL_RE_DB db;
    Toolbar toolbar;
    String mid = "0";
    com.squareup.okhttp.RequestBody body1;
    private Retrofit adapter;
    int status = 0, statusforimage = 0;
    Context context;
    private SharedPreferences preferences;
    String userId, visit_date, app_version;
    private ProgressDialog pb;
    ArrayList<CoverageBean> coverageList = new ArrayList<>();
    ArrayList<JourneyPlan> specific_uploadStatus;
    StoreProfileGetterSetter storePGT;
    ProgressDialog loading;
    public static int uploadedFiles = 0;
    public static int totalFiles = 0;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        app_version = preferences.getString(CommonString.KEY_VERSION, null);
        context = this;
        db = new INTEL_RE_DB(this);
        db.open();
        isDataValid();
        uploadedFiles = 0;
    }

    private boolean chekDataforCheckout(String store_cd, int region_id, int classification_id, int storeType_id) {
        boolean status, audit_store, visibility, mkt_tool, market_info;
        //store_audit data
        db.open();
        if (db.getStoreAuditHeaderData().size() > 0) {
            if (db.isStoreAuditFilled(store_cd)) {
                audit_store = true;
            } else {
                audit_store = false;
            }
        } else {
            audit_store = true;
        }

        //visibility filled data
        if (db.getsoftm_posmChilddata(region_id, classification_id, storeType_id).size() > 0 && db.getsemiparmanentheaderData(Integer.parseInt(store_cd)).size() > 0) {
            if (db.isVisibilitySoftMerchFilled(store_cd) && db.isVisibilitySPMerchFilled(store_cd)) {
                visibility = true;
            } else {
                visibility = false;
            }
        } else if (db.getsoftm_posmChilddata(region_id, classification_id, storeType_id).size() > 0) {
            if (db.isVisibilitySoftMerchFilled(store_cd)) {
                visibility = true;
            } else {
                visibility = false;
            }
        } else if (db.getsemiparmanentheaderData(Integer.parseInt(store_cd)).size() > 0) {
            if (db.isVisibilitySPMerchFilled(store_cd)) {
                visibility = true;
            } else {
                visibility = false;
            }
        } else {
            visibility = true;
        }

        //for shoper mkt tool

        if (db.isRXTFilled(store_cd) && db.isIPOSFilled(store_cd)) {
            mkt_tool = true;
        } else {
            mkt_tool = false;
        }


        //for market info
        if (db.getbranddataformarketinfo().size() > 0 && db.getInfoTypeData().size() > 0) {
            if (db.isMarketInfoFilled(store_cd)) {
                market_info = true;
            } else {
                market_info = false;
            }
        } else {
            market_info = true;
        }

        if (audit_store && visibility && mkt_tool && market_info) {
            status = true;
        } else {
            status = false;
        }


        return status;
    }


    void isDataValid() {
        boolean flag_invalid = false;
        String Store_cd = "", previous_date = "";
        JourneyPlan jcp = null;
        ArrayList<CoverageBean> coverage_list = db.getcoverageDataPrevious(visit_date);
        for (int i = 0; i < coverage_list.size(); i++) {
            jcp = db.getSpecificStoreDataPrevious(visit_date, coverage_list.get(i).getStoreId());
            if (jcp.getUploadStatus().equalsIgnoreCase(CommonString.KEY_CHECK_IN)) {
                Store_cd = jcp.getStoreId().toString();
                previous_date = jcp.getVisitDate();
                if (chekDataforCheckout(jcp.getStoreId().toString(), jcp.getRegionId(), jcp.getClassificationId(), jcp.getStoreTypeId())) {
                    flag_invalid = true;
                    break;
                } else {
                    db.open();
                    db.updateJaurneyPlanSpecificStoreStatus(Store_cd, jcp.getVisitDate(), CommonString.KEY_N);
                    db.deleteSpecificStoreData(Store_cd);
                    flag_invalid = false;
                    break;
                }
            }
        }

        if (flag_invalid) {
            db.open();
            ArrayList<CoverageBean> specificDATa = db.getSpecificCoverageData(previous_date, Store_cd);
            if (specificDATa.size() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("UserId", userId);
                    jsonObject.put("StoreId", specificDATa.get(0).getStoreId());
                    jsonObject.put("Latitude", specificDATa.get(0).getLatitude());
                    jsonObject.put("Longitude", specificDATa.get(0).getLongitude());
                    jsonObject.put("Checkout_Date", visit_date);
                    uploadCoverageIntimeDATA(jsonObject.toString(), specificDATa);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //start upload
                coverageList = db.getcoverageDataPrevious(visit_date);
                if (coverageList.size() > 0) {
                    pb = new ProgressDialog(context);
                    pb.setCancelable(false);
                    pb.setMessage("Uploading Data");
                    pb.show();
                    uploadDataUsingCoverageRecursive(coverageList, 0);
                } else {
                    db.deleteAllTables();
                    AlertandMessages.showAlert((Activity) context, "All data and images upload Successfully.", true);
                }
            }

        } else {
            db.deleteAllTables();
            AlertandMessages.showAlert((Activity) context, "All data and images upload Successfully.", true);
        }
    }

    public void uploadCoverageIntimeDATA(String jsondata, final ArrayList<CoverageBean> specific_CData) {
        try {
            loading = ProgressDialog.show(PreviousDataUploadActivity.this, "Processing", "Please wait...",
                    false, false);
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();
            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsondata.toString());
            Retrofit adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            PostApi api = adapter.create(PostApi.class);
            retrofit2.Call<ResponseBody> call = api.getCheckout(jsonData);
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    ResponseBody responseBody = response.body();
                    String data = null;
                    if (responseBody != null && response.isSuccessful()) {
                        try {
                            data = response.body().string();
                            if (!data.equals("0")) {
                                db.open();
                                db.updateCoverageCheckoutIMG(specific_CData.get(0).getStoreId(), specific_CData.get(0).getVisitDate(), "");
                                db.updateJaurneyPlanSpecificStoreStatus(specific_CData.get(0).getStoreId(),
                                        specific_CData.get(0).getVisitDate(), CommonString.KEY_C);
                                loading.dismiss();
                                coverageList = db.getcoverageDataPrevious(visit_date);
                                if (coverageList.size() > 0) {
                                    pb = new ProgressDialog(context);
                                    pb.setCancelable(false);
                                    pb.setMessage("Uploading Data");
                                    pb.show();
                                    uploadDataUsingCoverageRecursive(coverageList, 0);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            loading.dismiss();
                            AlertandMessages.showAlert(PreviousDataUploadActivity.this, "Check internet conection", true);
                        }
                    }

                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    loading.dismiss();
                    if (t instanceof SocketTimeoutException || t instanceof IOException || t instanceof Exception) {
                        AlertandMessages.showAlert(PreviousDataUploadActivity.this, "Check internet conection", true);
                    } else {
                        AlertandMessages.showAlert(PreviousDataUploadActivity.this, "Check internet conection", true);

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            loading.dismiss();
            AlertandMessages.showAlert(PreviousDataUploadActivity.this, "Check internet conection", true);
        }
    }


    //upload previous data
    public void uploadDataUsingCoverageRecursive(ArrayList<CoverageBean> coverageList, int coverageIndex) {
        try {
            ArrayList<String> keyList = new ArrayList<>();
            keyList.clear();
            String store_id = coverageList.get(coverageIndex).getStoreId();
            db.open();
            specific_uploadStatus = db.getSpecificStoreData(store_id);
            String status = null;
            status = specific_uploadStatus.get(0).getUploadStatus();
            pb.setMessage("Uploading store " + (coverageIndex + 1) + "/" + coverageList.size());
            if (!status.equalsIgnoreCase(CommonString.KEY_D)) {
                keyList.add("CoverageDetail_latest");
                keyList.add("STORE_PROFILE_DATA");
                keyList.add("RSP_DETAILS_DATA");
                keyList.add("STORE_AUDIT_NEW_DATA");
                //  keyList.add("VISIBILITY_SOFT_MERCH_DATA");
                keyList.add("POSM_SOFT_MERCH_DATA");
                // keyList.add("VISIBILITY_SEMIP_MERCH_DATA");
                keyList.add("POSM_SEMIP_MERCH_DATA");
//                keyList.add("SHOPER_MKT_IPOS");
                keyList.add("IPOS_NEW_DATA");
                // keyList.add("SHOPER_MKT_RXT");
                keyList.add("RXT_DATA");
                keyList.add("MARKET_INFO_NEW_DATA");
                keyList.add("GeoTag");
                keyList.add("TRAINING_DATA");
                keyList.add("TRAINING_ADD_RSP_DATA");
            }

            if (keyList.size() > 0) {
                uploadDataWithoutWait(keyList, 0, coverageList, coverageIndex);
            } else {
                if (++coverageIndex != coverageList.size()) {
                    uploadDataUsingCoverageRecursive(coverageList, coverageIndex);
                } else {

                    pb.setMessage("updoading images");
                    File dir = new File(CommonString.FILE_PATH);
                    if (getFileNames(dir.listFiles()).size() > 0) {
                        totalFiles = getFileNames(dir.listFiles()).size();
                        uploadImage(coverageList.get(0).getVisitDate());
                    } else {
                        pb.setMessage("Updating status");
                        updatestatusforu(coverageList, 0, coverageList.get(0).getVisitDate(), CommonString.KEY_U);
                    }

                }
            }
            //endregion

        } catch (Exception e) {
            e.printStackTrace();
            pb.dismiss();
            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_SOCKETEXCEPTION, true);
        }
    }

    public void uploadDataWithoutWait(final ArrayList<String> keyList,
                                      final int keyIndex, final ArrayList<CoverageBean> coverageList,
                                      final int coverageIndex) {
        try {
            status = 0;
            final String[] data_global = {""};
            String jsonString = "";
            int type = 0;
            JSONObject jsonObject;

            //region Creating json data
            switch (keyList.get(keyIndex)) {
                case "CoverageDetail_latest":
                    JourneyPlan journeyPlan;
                    //region Coverage Data
                    jsonObject = new JSONObject();
                    jsonObject.put("StoreId", coverageList.get(coverageIndex).getStoreId());
                    jsonObject.put("VisitDate", coverageList.get(coverageIndex).getVisitDate());
                    jsonObject.put("Latitude", coverageList.get(coverageIndex).getLatitude());
                    jsonObject.put("Longitude", coverageList.get(coverageIndex).getLongitude());
                    jsonObject.put("ReasonId", coverageList.get(coverageIndex).getReasonid());
                    jsonObject.put("SubReasonId", "0");
                    jsonObject.put("Remark", "");
                    jsonObject.put("ImageName", coverageList.get(coverageIndex).getImage());
                    jsonObject.put("AppVersion", app_version);
                    jsonObject.put("UploadStatus", CommonString.KEY_P);
                    jsonObject.put("Checkout_Image", coverageList.get(coverageIndex).getCkeckout_image());
                    jsonObject.put("UserId", userId);
                    jsonString = jsonObject.toString();
                    type = CommonString.COVERAGE_DETAIL;
                    //endregion
                    break;
                case "STORE_PROFILE_DATA":
                    db.open();
                    storePGT = db.getStoreProfileData(coverageList.get(coverageIndex).getStoreId(), coverageList.get(coverageIndex).getVisitDate());
                    if (storePGT.getProfileCity() != null && !storePGT.getProfileCity().equals("")) {
                        JSONArray storeDetail = new JSONArray();
                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("UserId", userId);
                        jsonObject.put("PROFILE_STORE_ID", coverageList.get(coverageIndex).getStoreId().toString());
                        jsonObject.put("PROFILE_STORE_NAME", storePGT.getProfileStoreName());
                        jsonObject.put("PROFILE_STORE_ADDRESS_1", storePGT.getProfileAddress1());
                        jsonObject.put("PROFILE_STORE_CITY", storePGT.getProfileCity());
                        jsonObject.put("STORE_PROFILE_OWNER", storePGT.getProfileOwner());
                        jsonObject.put("STORE_PROFILE_CONTACT", storePGT.getProfileContact());
                        jsonObject.put("DOB", storePGT.getProfileDOB());
                        jsonObject.put("DOA", storePGT.getProfileDOA());
                        jsonObject.put("VISIBILITY_LOCATION1", storePGT.getProfileVisibilityLocation1());
                        jsonObject.put("VISIBILITY_LOCATION2", storePGT.getProfileVisibilityLocation2());
                        jsonObject.put("VISIBILITY_LOCATION3", storePGT.getProfileVisibilityLocation3());
                        jsonObject.put("DIMENTION1", storePGT.getProfileDimension1());
                        jsonObject.put("DIMENTION2", storePGT.getProfileDimension2());
                        jsonObject.put("DIMENTION3", storePGT.getProfileDimension3());
                        storeDetail.put(jsonObject);
                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "STORE_PROFILE_DATA");
                        jsonObject.put("JsonData", storeDetail.toString());
                        jsonObject.put("UserId", userId);
                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;

                    }
                    //endregion
                    break;
                case "RSP_DETAILS_DATA":
                    //region primary bay data
                    db.open();
                    ArrayList<StoreCategoryMaster> rsp_detailsList = db.getRspDetailinsertData(coverageList.get(coverageIndex).getStoreId().toString());
                    if (rsp_detailsList.size() > 0) {
                        JSONArray rspArray = new JSONArray();
                        for (int j = 0; j < rsp_detailsList.size(); j++) {
                            boolean irep_status = rsp_detailsList.get(j).getIREPStatus();
                            String value = "";
                            if (irep_status) {
                                value = "1";
                            } else {
                                value = "0";
                            }
                            if (rsp_detailsList.get(j).getRsp_uniqueID() == null) {
                                rsp_detailsList.get(j).setRsp_uniqueID("");
                            }

                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("RSP_NAME", rsp_detailsList.get(j).getRspName());
                            obj.put("RSP_ID", rsp_detailsList.get(j).getRspId().toString());
                            obj.put("EMAIL", rsp_detailsList.get(j).getEmail());
                            obj.put("MOBILE", rsp_detailsList.get(j).getMobile());
                            obj.put("FLAG", rsp_detailsList.get(j).getFlag());
                            obj.put("STORE_CD", rsp_detailsList.get(j).getStoreId().toString());
                            obj.put("IREP_STATUS", value);
                            obj.put("BRAND_ID", rsp_detailsList.get(j).getBrandId().toString());
                            obj.put("RSP_UNIQUE_ID", rsp_detailsList.get(j).getRsp_uniqueID());
                            rspArray.put(obj);
                        }
                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "RSP_DETAILS_DATA");
                        jsonObject.put("JsonData", rspArray.toString());
                        jsonObject.put("UserId", userId);
                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;
                case "STORE_AUDIT_NEW_DATA":
                    //region Promotion data
                    db.open();

                    ArrayList<AuditQuestion> auditList = db.getStoreAuditData(coverageList.get(coverageIndex).getStoreId());
                    if (auditList.size() > 0) {
                        JSONArray promoArray = new JSONArray();
                        for (int j = 0; j < auditList.size(); j++) {
                            String imageallowansw = "";
                            JSONObject obj = new JSONObject();
                            if (auditList.get(j).getImageAllowforanswer().equalsIgnoreCase("true")) {
                                imageallowansw = "1";
                            } else {
                                imageallowansw = "0";
                            }
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("QUESTION_CD", auditList.get(j).getQuestionId());
                            obj.put("ANSWER_CD", auditList.get(j).getCurrectanswerCd());
                            obj.put("AUDIT_IMG", auditList.get(j).getAudit_cam());
                            obj.put("IMAGEALLOW_ANSWERWISE", imageallowansw);
                            promoArray.put(obj);
                        }
                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "STORE_AUDIT_NEW_DATA");
                        jsonObject.put("JsonData", promoArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }

                    break;
                case "POSM_SOFT_MERCH_DATA":
                    //region Competition data
                    db.open();
                    ArrayList<PosmTypeQuestion> softmerchHeaderList = db.getSoftmerchvisibilityHeaderData(coverageList.get(coverageIndex).getStoreId(), coverageList.get(coverageIndex).getVisitDate());
                    if (softmerchHeaderList.size() > 0) {
                        JSONArray soft_child_array = new JSONArray();
                        JSONArray softheaderArray = new JSONArray();
                        for (int k = 0; k < softmerchHeaderList.size(); k++) {
                            db.open();
                            ArrayList<PosmTypeQuestion> softmerchChildListbyCommonId = db.getsoftmerchChild(coverageList.get(coverageIndex).getStoreId(), coverageList.get(coverageIndex).getVisitDate(), softmerchHeaderList.get(k).getKey_Id());
                            if (softmerchChildListbyCommonId.size() > 0) {
                                if (softmerchHeaderList.get(k).getHeader_availebility().equalsIgnoreCase("Yes")) {
                                    for (int j = 0; j < softmerchChildListbyCommonId.size(); j++) {
                                        JSONObject obj = new JSONObject();
                                        obj.put("MID", coverageList.get(coverageIndex).getMID());
                                        obj.put("UserId", userId);
                                        obj.put("POSM_TYPE_Id", softmerchChildListbyCommonId.get(j).getPosmTypeId().toString());
                                        if (softmerchChildListbyCommonId.get(j).getSemip_right_ans_Id().equals("1")) {
                                            obj.put("STATUS", "1");
                                        } else {
                                            obj.put("STATUS", "0");
                                        }
                                        obj.put("SOFT_QTY", softmerchChildListbyCommonId.get(j).getPromoType_qty());
                                        obj.put("KEY_Id", softmerchHeaderList.get(k).getKey_Id());
                                        soft_child_array.put(obj);
                                    }
                                }
                            }

                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("AVAILABILITY", softmerchHeaderList.get(k).getHeader_availebility());
                            obj.put("HEADER_IMG", softmerchHeaderList.get(k).getHeader_image());
                            obj.put("KEY_Id", softmerchHeaderList.get(k).getKey_Id());
                            if (softmerchHeaderList.get(k).getHeader_availebility().equalsIgnoreCase("Yes")) {
                                obj.put("SOFT_CHILD", soft_child_array);
                            } else {
                                obj.put("SOFT_CHILD", "");
                            }
                            softheaderArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "POSM_SOFT_MERCH_DATA");
                        jsonObject.put("JsonData", softheaderArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;
                case "POSM_SEMIP_MERCH_DATA":
                    db.open();
                    ArrayList<PosmTypeQuestion> semip_visibilityHeaderList = db.getsemip_headerList(coverageList.get(coverageIndex).getStoreId(), coverageList.get(coverageIndex).getVisitDate());
                    if (semip_visibilityHeaderList.size() > 0) {
                        JSONArray soft_child_array = null;
                        JSONArray softheaderArray = new JSONArray();
                        for (int k = 0; k < semip_visibilityHeaderList.size(); k++) {
                            db.open();
                            ArrayList<PosmTypeQuestion> semipmerchChildList = db.getsemipermmerchChild(coverageList.get(coverageIndex).getStoreId(), coverageList.get(coverageIndex).getVisitDate(), semip_visibilityHeaderList.get(k).getKey_Id());
                            if (semipmerchChildList.size() > 0) {
                                if (semip_visibilityHeaderList.get(k).getHeader_availebility().equalsIgnoreCase("Yes")) {
                                    soft_child_array = new JSONArray();
                                    for (int j = 0; j < semipmerchChildList.size(); j++) {
                                        if (semipmerchChildList.get(j).getDeployment_flag().equals("15")) {
                                            PosmTypeQuestion object = new PosmTypeQuestion();
                                            object.setQuestionId(Integer.valueOf(semipmerchChildList.get(j).getDeployment_flag()));
                                            object.setSemip_right_ans(semipmerchChildList.get(j).getDeployment_date());
                                            object.setSemipmerch_img("");
                                            semipmerchChildList.add(j + 1, object);
                                        } else if (semipmerchChildList.get(j).getDeployment_flag().equals("4")) {
                                            PosmTypeQuestion object = new PosmTypeQuestion();
                                            object.setQuestionId(Integer.valueOf(semipmerchChildList.get(j).getDeployment_flag()));
                                            object.setSemip_right_ans(semipmerchChildList.get(j).getInstallation_date());
                                            object.setSemipmerch_img("");
                                            semipmerchChildList.add(j + 1, object);
                                        }

                                        JSONObject obj = new JSONObject();
                                        obj.put("MID", coverageList.get(coverageIndex).getMID());
                                        obj.put("UserId", userId);
                                        obj.put("POSM_TYPE_Id", semip_visibilityHeaderList.get(k).getPosmTypeId().toString());
                                        obj.put("QUESTION_Id", semipmerchChildList.get(j).getQuestionId().toString());
                                        obj.put("ANSWER", semipmerchChildList.get(j).getSemip_right_ans());
                                        obj.put("SEMI_IMG", semipmerchChildList.get(j).getSemipmerch_img());
                                        obj.put("KEY_Id", semip_visibilityHeaderList.get(k).getKey_Id());
                                        soft_child_array.put(obj);
                                    }
                                }
                            }

                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("POSM_TYPE_Id", semip_visibilityHeaderList.get(k).getPosmTypeId().toString());
                            obj.put("AVAILABILITY", semip_visibilityHeaderList.get(k).getHeader_availebility());
                            obj.put("HEADER_IMG", semip_visibilityHeaderList.get(k).getHeader_image());
                            obj.put("KEY_Id", semip_visibilityHeaderList.get(k).getKey_Id());
                            if (semip_visibilityHeaderList.get(k).getHeader_availebility().equalsIgnoreCase("Yes")) {
                                obj.put("SOFT_CHILD", soft_child_array);
                            } else {
                                obj.put("SOFT_CHILD", "");
                            }
                            softheaderArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "POSM_SEMIP_MERCH_DATA");
                        jsonObject.put("JsonData", softheaderArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    break;
                case "IPOS_NEW_DATA":
                    //region Secondary_backwall_image
                    db.open();
                    SkuMaster ipos_listData = db.getipos_inserteddata_object(coverageList.get(coverageIndex).getStoreId());
                    if (ipos_listData.getIpos() != null) {
                        JSONArray topUpArray = new JSONArray();

                        JSONObject obj = new JSONObject();
                        obj.put("MID", coverageList.get(coverageIndex).getMID());
                        obj.put("UserId", userId);
                        obj.put("NOOF_DISPLAY", ipos_listData.getNoof_display());
                        obj.put("NOOF_MACHINE_ON", ipos_listData.getMachine_on());
                        obj.put("IPOS", ipos_listData.getIpos());
                        obj.put("CUSTOMIZED_IPOS", ipos_listData.getCustomised_ipos());
                        obj.put("GAMING_IPOS", ipos_listData.getGaming_ipos());
                        obj.put("IPOS_IMG", ipos_listData.getIpos_img());
                        topUpArray.put(obj);

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "IPOS_NEW_DATA");
                        jsonObject.put("JsonData", topUpArray.toString());
                        jsonObject.put("UserId", userId);
                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    break;
                case "RXT_DATA":
                    //region primary_image_Grid
                    SkuMaster secCompleteIPOSDATA = db.getrxt_insertedData(coverageList.get(coverageIndex).getStoreId());
                    if (secCompleteIPOSDATA.getNoof_display() != null) {
                        JSONArray topUpArray = new JSONArray();
                        JSONObject obj = new JSONObject();
                        obj.put("MID", coverageList.get(coverageIndex).getMID());
                        obj.put("UserId", userId);
                        obj.put("NOOF_DISPLAY", secCompleteIPOSDATA.getNoof_display());
                        obj.put("NOOF_MACHINE_ON", secCompleteIPOSDATA.getMachine_on());
                        obj.put("ENGEGMENT", secCompleteIPOSDATA.getEngegment());
                        obj.put("RXT", secCompleteIPOSDATA.getRxt());
                        obj.put("CUSTOMIZED_RXT", secCompleteIPOSDATA.getCustomised_ipos());
                        obj.put("RXT_IMG", secCompleteIPOSDATA.getRxt_img());
                        topUpArray.put(obj);

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "RXT_DATA");
                        jsonObject.put("JsonData", topUpArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }

                    break;
                case "MARKET_INFO_NEW_DATA":
                    //region Primary_Stock

                    ArrayList<InfoTypeMaster> market_infoList = db.getinfotypeinsetedDATA(coverageList.get(coverageIndex).getStoreId());
                    if (market_infoList.size() > 0) {
                        JSONArray topUpArray = new JSONArray();
                        for (int j = 0; j < market_infoList.size(); j++) {
                            String existsvalue = "";
                            JSONObject obj = new JSONObject();
                            if (market_infoList.get(j).isExistsFlag()) {
                                existsvalue = "1";
                            } else {
                                existsvalue = "0";
                            }
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("BRAND_CD", market_infoList.get(j).getBrand_cd());
                            obj.put("TYPE_CD", market_infoList.get(j).getType_cd());
                            obj.put("INFO_TYPE_CD", market_infoList.get(j).getInfoTypeId());
                            obj.put("REMARK", market_infoList.get(j).getRemark());
                            obj.put("MARKET_INFO_IMG", market_infoList.get(j).getMarketinfo_img());
                            obj.put("MARKET_INFO_EXISTS", existsvalue);
                            topUpArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "MARKET_INFO_NEW_DATA");
                        jsonObject.put("JsonData", topUpArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;


                case "GeoTag":
                    //region GeoTag
                    ArrayList<GeotaggingBeans> geotaglist = db.getinsertGeotaggingData(coverageList.get(coverageIndex).getStoreId(), "N");
                    if (geotaglist.size() > 0) {
                        JSONArray topUpArray = new JSONArray();
                        for (int j = 0; j < geotaglist.size(); j++) {
                            JSONObject obj = new JSONObject();
                            obj.put(CommonString.KEY_STORE_ID, geotaglist.get(j).getStoreid());
                            obj.put(CommonString.KEY_VISIT_DATE, visit_date);
                            obj.put(CommonString.KEY_LATITUDE, geotaglist.get(j).getLatitude());
                            obj.put(CommonString.KEY_LONGITUDE, geotaglist.get(j).getLongitude());
                            obj.put("FRONT_IMAGE", geotaglist.get(j).getImage());
                            topUpArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "GeoTag");
                        jsonObject.put("JsonData", topUpArray.toString());
                        jsonObject.put("UserId", userId);
                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }

                    break;
                case "TRAINING_DATA":
                    //region secondary display data
                    db.open();
                    ArrayList<TrainingGetterSetter> trainingList = db.getinsertedTrainingDataForRSPGRZero(coverageList.get(coverageIndex).getStoreId().toString(), coverageList.get(coverageIndex).getVisitDate());
                    if (trainingList.size() > 0) {
                        JSONArray secArray = new JSONArray();
                        for (int j = 0; j < trainingList.size(); j++) {
                            String rsp_id = trainingList.get(j).getRspname_cd();
                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("RSP_CD", trainingList.get(j).getRspname_cd());
                            obj.put("TRAINING_TYPE_CD", trainingList.get(j).getTrainingtype_cd().toString());
                            obj.put("TOPIC_CD", trainingList.get(j).getTopic_cd().toString());
                            obj.put("TRAINING_PIC", trainingList.get(j).getPhoto());
                            secArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "TRAINING_DATA");
                        jsonObject.put("JsonData", secArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;

                case "TRAINING_ADD_RSP_DATA":
                    //region secondary display data
                    db.open();
                    ArrayList<TrainingGetterSetter> trainingRspUniqueList = db.getinsertedTrainingDataForRSPZero(coverageList.get(coverageIndex).getStoreId(), coverageList.get(coverageIndex).getVisitDate());
                    if (trainingRspUniqueList.size() > 0) {
                        JSONArray secArray = new JSONArray();
                        for (int j = 0; j < trainingRspUniqueList.size(); j++) {
                            String rsp_id = trainingRspUniqueList.get(j).getRspname_cd();
                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("RSP_CD", trainingRspUniqueList.get(j).getUnoque_RSPID());
                            obj.put("TRAINING_TYPE_CD", trainingRspUniqueList.get(j).getTrainingtype_cd().toString());
                            obj.put("TOPIC_CD", trainingRspUniqueList.get(j).getTopic_cd().toString());
                            obj.put("TRAINING_PIC", trainingRspUniqueList.get(j).getPhoto());
                            secArray.put(obj);

                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "TRAINING_ADD_RSP_DATA");
                        jsonObject.put("JsonData", secArray.toString());
                        jsonObject.put("UserId", userId);
                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;
            }
            //endregion

            final int[] finalJsonIndex = {keyIndex};
            final String finalKeyName = keyList.get(keyIndex);
            if (jsonString != null && !jsonString.equals("")) {
                pb.setMessage("Uploading (" + keyIndex + "/" + keyList.size() + ") \n" + keyList.get(keyIndex) + "\n Store uploading " + (coverageIndex + 1) + "/" + coverageList.size());

                final OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).connectTimeout(20, TimeUnit.SECONDS).build();
                RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
                adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
                PostApi api = adapter.create(PostApi.class);
                Call<ResponseBody> call = null;
                if (type == CommonString.COVERAGE_DETAIL) {
                    call = api.getCoverageDetail(jsonData);
                } else if (type == CommonString.UPLOADJsonDetail) {
                    call = api.getUploadJsonDetail(jsonData);
                }
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody responseBody = response.body();
                        String data = null;
                        if (responseBody != null && response.isSuccessful()) {
                            try {
                                data = response.body().string();
                                if (data.equalsIgnoreCase("")) {
                                    pb.dismiss();
                                    data_global[0] = "";
                                    AlertandMessages.showAlert((Activity) context, "Invalid Data :" +
                                            " problem occured at " + keyList.get(keyIndex), true);
                                } else {
                                    data = data.substring(1, data.length() - 1).replace("\\", "");
                                    data_global[0] = data;
                                    if (finalKeyName.equalsIgnoreCase("CoverageDetail_latest")) {
                                        try {
                                            coverageList.get(coverageIndex).setMID(Integer.parseInt(data_global[0]));
                                            specific_uploadStatus.get(0).setUploadStatus(CommonString.KEY_P);
                                            db.updateJaurneyPlanSpecificStoreStatus(coverageList.get(coverageIndex).getStoreId(), coverageList.get(coverageIndex).getVisitDate(), CommonString.KEY_P);

                                        } catch (NumberFormatException ex) {
                                            pb.dismiss();
                                            AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + finalKeyName, true);
                                        }
                                    } else if (data_global[0].contains(CommonString.KEY_SUCCESS)) {
                                        if (finalKeyName.equalsIgnoreCase("GeoTag")) {
                                            // db.updateInsertedGeoTagStatus(coverageList.get(coverageIndex).getStoreId(), CommonString.KEY_Y);
                                            // db.updateStatus(coverageList.get(coverageIndex).getStoreId(), CommonString.KEY_Y);
                                            // db.updateGeoTagStatusForPjpDevaition(coverageList.get(coverageIndex).getStoreId(), CommonString.KEY_Y);
                                        }
                                    } else {
                                        pb.dismiss();
                                        AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + finalKeyName + " : " + data_global[0], true);
                                    }
                                    finalJsonIndex[0]++;
                                    if (finalJsonIndex[0] != keyList.size()) {
                                        uploadDataWithoutWait(keyList, finalJsonIndex[0], coverageList, coverageIndex);
                                    } else {
                                        pb.setMessage("updating status :" + coverageIndex);
                                        //uploading status D for current store from coverageList
                                        specific_uploadStatus.get(0).setUploadStatus(CommonString.KEY_D);
                                        updateStatus(coverageList, coverageIndex, CommonString.KEY_D);
                                    }
                                }

                            } catch (Exception e) {
                                pb.dismiss();
                                AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + finalKeyName, true);
                            }
                        } else {
                            pb.dismiss();
                            AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + finalKeyName, true);

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        pb.dismiss();
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                    }
                });

            } else {
                finalJsonIndex[0]++;
                if (finalJsonIndex[0] != keyList.size()) {
                    uploadDataWithoutWait(keyList, finalJsonIndex[0], coverageList, coverageIndex);
                } else {
                    pb.setMessage("updating status :" + coverageIndex);
                    //uploading status D for current store from coverageList
                    specific_uploadStatus.get(0).setUploadStatus(CommonString.KEY_D);
                    updateStatus(coverageList, coverageIndex, CommonString.KEY_D);

                }
            }
        } catch (Exception ex) {
            pb.dismiss();
            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE + " (" + ex.toString() + " )", true);
        }
    }

    void updateStatus(final ArrayList<CoverageBean> coverageList, final int coverageIndex,
                      final String status) {
        if (coverageList.get(coverageIndex) != null) {
            try {
                final int[] tempcoverageIndex = {coverageIndex};

                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .build();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("StoreId", coverageList.get(coverageIndex).getStoreId());
                jsonObject.put("VisitDate", coverageList.get(coverageIndex).getVisitDate());
                jsonObject.put("UserId", userId);
                jsonObject.put("Status", status);
                RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                PostApi api = adapter.create(PostApi.class);
                Call<ResponseBody> call = api.getCoverageStatusDetail(jsonData);
                pb.setMessage("Uploading store status " + (coverageIndex + 1) + "/" + coverageList.size());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody responseBody = response.body();
                        String data = null;
                        if (responseBody != null && response.isSuccessful()) {
                            try {
                                data = response.body().string();
                                if (data.equalsIgnoreCase("")) {
                                    pb.dismiss();
                                    AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + coverageIndex, true);
                                } else {
                                    data = data.substring(1, data.length() - 1).replace("\\", "");
                                    if (data.contains("1")) {
                                        db.open();
                                        db.updateJaurneyPlanSpecificStoreStatus(coverageList.get(coverageIndex).getStoreId(),
                                                coverageList.get(coverageIndex).getVisitDate(), status);
                                        specific_uploadStatus.get(0).setUploadStatus(status);
                                        tempcoverageIndex[0]++;
                                        if (tempcoverageIndex[0] != coverageList.size()) {
                                            uploadDataUsingCoverageRecursive(coverageList, tempcoverageIndex[0]);
                                        } else {
                                            //  uploadimages();
                                            pb.setMessage("uploading images");
                                            File dir = new File(CommonString.FILE_PATH);
                                            if (getFileNames(dir.listFiles()).size() > 0) {
                                                totalFiles = getFileNames(dir.listFiles()).size();
                                                uploadImage(coverageList.get(coverageIndex).getVisitDate());
                                            } else {
                                                db.open();
                                                db.updateJaurneyPlanSpecificStoreStatus(coverageList.get(coverageIndex).getStoreId(), coverageList.get(coverageIndex).getVisitDate(), status);
                                                updateStatus(coverageList, coverageIndex, status);
                                            }
                                        }
                                    } else {
                                        pb.dismiss();
                                        AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + coverageIndex, true);
                                    }

                                }
                            } catch (Exception e) {
                                pb.dismiss();
                                AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + coverageIndex, true);
                            }
                        } else {
                            pb.dismiss();
                            AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + coverageIndex, true);

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        pb.dismiss();
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);

                    }
                });

            } catch (JSONException ex) {
                pb.dismiss();
                AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INVALID_JSON + " (" + ex.toString() + " )", true);

            }
        }

    }

    public ArrayList<String> getFileNames(File[] file) {
        ArrayList<String> arrayFiles = new ArrayList<String>();
        if (file.length > 0) {
            for (int i = 0; i < file.length; i++)
                arrayFiles.add(file[i].getName());
        }
        return arrayFiles;
    }


    private void updatestatusforu(final ArrayList<CoverageBean> coverageList, int index, final String visit_date, final String status) {
        try {
            db.open();
            final int[] indexlocal = {index};
            final boolean[] status_u = {false};
            final ArrayList<JourneyPlan> store_data = db.getSpecificStoreData(coverageList.get(index).getStoreId().toString());
            if (store_data.size() > 0) {
                if (store_data.get(indexlocal[0]).getUploadStatus().equalsIgnoreCase(CommonString.KEY_D)) {
                    index++;
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .build();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("StoreId", store_data.get(indexlocal[0]).getStoreId());
                    jsonObject.put("VisitDate", visit_date);
                    jsonObject.put("UserId", userId);
                    jsonObject.put("Status", status);
                    RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                    adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    PostApi api = adapter.create(PostApi.class);
                    Call<ResponseBody> call = api.getCoverageStatusDetail(jsonData);
                    pb.setMessage("Uploading store status " + (index) + "/" + coverageList.size());
                    final int finalIndex = index;
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            ResponseBody responseBody = response.body();
                            String data = null;
                            if (responseBody != null && response.isSuccessful()) {
                                try {
                                    data = response.body().string();
                                    if (data.equalsIgnoreCase("")) {
                                        pb.dismiss();
                                        status_u[0] = false;
                                        AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + finalIndex, true);
                                    } else {
                                        data = data.substring(1, data.length() - 1).replace("\\", "");
                                        if (data.contains("1")) {
                                            status_u[0] = true;
                                            db.open();
                                            db.updateJaurneyPlanSpecificStoreStatus(store_data.get(indexlocal[0]).getStoreId().toString(),
                                                    store_data.get(indexlocal[0]).getVisitDate(), status);
                                            db.deleteSpecificStoreData(store_data.get(indexlocal[0]).getStoreId().toString());
                                            indexlocal[0]++;
                                            if (indexlocal[0] != coverageList.size()) {
                                                updatestatusforu(coverageList, indexlocal[0], coverageList.get(0).getVisitDate(), CommonString.KEY_U);
                                            } else {
                                                if (status_u[0] == true) {
                                                    pb.dismiss();
                                                    AlertandMessages.showAlert((Activity) context, "All data and images upload Successfully.", true);
                                                }
                                            }
                                        } else {
                                            status_u[0] = false;
                                            pb.dismiss();
                                            AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + finalIndex, true);
                                        }

                                    }
                                } catch (Exception e) {
                                    status_u[0] = false;
                                    pb.dismiss();
                                    AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + finalIndex, true);
                                }
                            } else {
                                status_u[0] = false;
                                pb.dismiss();
                                AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + finalIndex, true);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            status_u[0] = false;
                            pb.dismiss();
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);


                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            pb.dismiss();
            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INVALID_JSON + " (" + e.toString() + " )", true);

        }

    }

    public static File saveBitmapToFile(File file) {
        File file2 = file;
        try {
            int inWidth = 0;
            int inHeight = 0;
            InputStream in = new FileInputStream(file2);
            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            in = null;
            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;
            // decode full image pre-resized
            in = new FileInputStream(file2);
            options = new BitmapFactory.Options();
            // calc rought re-size (this is no exact resize)
            options.inSampleSize = Math.max(inWidth / 800, inHeight / 500);
            // decode full image
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);
            if (roughBitmap==null){
                return null;
            }
            // calc exact destination size
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, 800, 500);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);
            // resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int)
                    (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);
            // save image
            try {
                FileOutputStream out = new FileOutputStream(file2);
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            } catch (Exception e) {
                Log.e("Image", e.toString(), e);
            }
        } catch (IOException e) {
            Log.e("Image", e.toString(), e);
            return file2;
        }
        return file;
    }

    String getParsedDate(String filename) {
        String testfilename = filename;
        testfilename = testfilename.substring(testfilename.indexOf("-") + 1);
        testfilename = testfilename.substring(0, testfilename.indexOf("-"));
        return testfilename;
    }


    void uploadImage(String coverageDate) {
        pb.setMessage("updoading images");
        File f = new File(CommonString.FILE_PATH);
        File file[] = f.listFiles();
        if (file.length > 0) {
            UploadImageRecursive(context, coverageDate);
        } else {
            pb.setMessage("Updating status");
            updatestatusforu(coverageList, 0, coverageDate, CommonString.KEY_U);
        }
    }

    public void UploadImageRecursive(final Context context, final String covergeDate) {
        try {
            statusforimage = 0;
            int totalfiles = 0;
            String filename = null, foldername = null;
            File f = new File(CommonString.FILE_PATH);
            File file[] = f.listFiles();
            count = file.length;
            if (file.length > 0) {
                filename = "";
                totalfiles = f.listFiles().length;
                pb.setMessage("Uploading images" + "(" + uploadedFiles + "/" + totalFiles + ")");
                for (int i = 0; i < file.length; i++) {
                    if (new File(CommonString.FILE_PATH + file[i].getName()).exists()) {

                        if (file[i].getName().contains("_STOREIMG_") || file[i].getName().contains("_NONWORKING_") || file[i].getName().contains("_STOREC_OUTIMG_")) {
                            foldername = "CoverageImages";
                        } else if (file[i].getName().contains("_AUDITIMG_")) {
                            foldername = "AuditImages";
                        } else if (file[i].getName().contains("_SOFTMERCHIMG_") || file[i].getName().contains("_SOFTM_IMG_")) {
                            foldername = "SoftPosmImages";
                        } else if (file[i].getName().contains("_SEMIH_IMG_") || file[i].getName().contains("_SEMIPMERCHIMG_") || file[i].getName().contains("_SEMIPMERCHIMG_ONE_") || file[i].getName().contains("_SEMIPMERCHIMG_TWO_") || file[i].getName().contains("_SEMIPMERCHIMG_THREE_")) {
                            foldername = "PermanantPosmImages";
                        } else if (file[i].getName().contains("_IPOSING_")) {
                            foldername = "IPOSImages";
                        } else if (file[i].getName().contains("_RXTING_")) {
                            foldername = "RXTImages";
                        } else if (file[i].getName().contains("_MARKETINFOING_")) {
                            foldername = "MarketInfoImages";
                        } else if (file[i].getName().contains("_TRAININGIMG_")) {
                            foldername = "TrainingImages";
                        } else if (file[i].getName().contains("_GeoTag_")) {
                            foldername = "GeoTagImages";
                        } else {
                            foldername = "BulkUpload";
                        }
                        filename = file[i].getName();
                    }
                    break;
                }


                File originalFile = new File(CommonString.FILE_PATH + filename);
                File finalFile = saveBitmapToFile(originalFile);
                if (finalFile == null) {
                    finalFile = originalFile;
                }
                String date;
                if (filename.contains("-")) {
                    date = getParsedDate(filename);
                } else {
                    date = visit_date.replace("/", "");
                }


                com.squareup.okhttp.OkHttpClient okHttpClient = new com.squareup.okhttp.OkHttpClient();
                okHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
                okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
                okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);

                com.squareup.okhttp.RequestBody photo = com.squareup.okhttp.RequestBody.create(com.squareup.okhttp.MediaType.parse("application/octet-stream"), finalFile);
                body1 = new MultipartBuilder()
                        .type(MultipartBuilder.FORM)
                        .addFormDataPart("file", finalFile.getName(), photo)
                        .addFormDataPart("FolderName", foldername)
                        .addFormDataPart("Path", date)
                        .build();

                retrofit.Retrofit adapter = new retrofit.Retrofit.Builder()
                        .baseUrl(CommonString.URLGORIMAG)
                        .client(okHttpClient)
                        .addConverterFactory(new StringConverterFactory())
                        .build();
                PostApiForUpload api = adapter.create(PostApiForUpload.class);
                retrofit.Call<String> call = api.getUploadImageRetrofitOne(body1);

                final File finalFile1 = finalFile;
                call.enqueue(new retrofit.Callback<String>() {
                    @Override
                    public void onResponse(retrofit.Response<String> response) {
                        if (response.code() == 200 && response.message().equals("OK") && response.isSuccess() && response.body().contains("Success")) {
                            finalFile1.delete();
                            statusforimage = 1;
                            uploadedFiles++;
                        } else {
                            statusforimage = 0;
                        }
                        if (statusforimage == 0) {
                            pb.dismiss();
                            if (!((Activity) context).isFinishing()) {
                                AlertandMessages.showAlert((Activity) context, "Image not uploaded." + "\n" + uploadedFiles + " images uploaded out of " + totalFiles, true);
                            }
                        } else {
                            UploadImageRecursive(context, covergeDate);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (t instanceof IOException || t instanceof SocketTimeoutException || t instanceof SocketException) {
                            statusforimage = -1;
                            pb.dismiss();
                            if (!((Activity) context).isFinishing()) {
                                AlertandMessages.showAlert((Activity) context, "Network Error in upload." + "\n" + uploadedFiles + " images uploaded out of " + totalFiles, true);
                            }
                        }
                    }
                });

            } else {
                if (totalFiles == uploadedFiles) {
                    pb.setMessage("Updating Status");
                    specific_uploadStatus.get(0).setUploadStatus(CommonString.KEY_U);
                    updatestatusforu(coverageList, 0, visit_date, CommonString.KEY_U);
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INVALID_JSON, true);
        } catch (Exception e) {
            e.printStackTrace();
            if (totalFiles == uploadedFiles) {
                AlertandMessages.showAlert((Activity) context, "All images uploaded but status not updated", true);
            } else {
                AlertandMessages.showAlert((Activity) context, CommonString.KEY_FAILURE + " (" + e.toString() + " )", true);
            }
        }

    }
}
