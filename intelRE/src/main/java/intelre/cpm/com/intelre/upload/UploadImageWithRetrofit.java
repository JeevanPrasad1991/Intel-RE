/*
package intelre.cpm.com.intelre.upload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.constant.AlertandMessages;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.delegates.CoverageBean;
import intelre.cpm.com.intelre.gettersetter.GeotaggingBeans;
import intelre.cpm.com.intelre.gettersetter.ReferenceVariablesForDownloadActivity;
import intelre.cpm.com.intelre.gettersetter.StoreProfileGetterSetter;
import intelre.cpm.com.intelre.gettersetter.TrainingGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.AuditQuestion;
import intelre.cpm.com.intelre.gsonGetterSetter.InfoTypeMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.gsonGetterSetter.MappingPermanentPosm;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.SkuMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.StoreCategoryMaster;
import intelre.cpm.com.intelre.retrofit.PostApi;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

*/
/**
 * Created by jeevanp on 5/29/2018.
 *//*


public class UploadImageWithRetrofit extends ReferenceVariablesForDownloadActivity {
    ArrayList<JourneyPlan> specific_uploadStatus;
    StoreProfileGetterSetter storePGT;
    boolean isvalid;
    RequestBody body1;
    private Retrofit adapter;
    Context context;
    public static int uploadedFiles = 0;
    public int listSize = 0;
    int status = 0;
    INTEL_RE_DB db;
    ProgressDialog pd;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String[] jj;
    boolean statusUpdated = true;
    String userId, visit_date, app_version;
    int from;

    public UploadImageWithRetrofit(Context context) {
        this.context = context;
    }

    public UploadImageWithRetrofit(Context context, String progessTitle, String progressStr) {
        this.context = context;
        pd = new ProgressDialog(this.context);
        pd.setTitle(progessTitle);
        pd.setMessage(progressStr);
        pd.setCancelable(false);
        if (pd != null && (!pd.isShowing())) {
            pd.show();
        }
    }

    public UploadImageWithRetrofit(Context context, INTEL_RE_DB db, ProgressDialog pd, int from) {
        this.context = context;
        this.db = db;
        this.pd = pd;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        this.from = from;
        userId = preferences.getString(CommonString.KEY_USERNAME, "");
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        try {
            app_version = String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        db.open();
    }


    public void UploadImageFileJsonList(final Context context, final String coverageDate) {
        try {
            String filename = null, foldername = null;
            int totalfiles = 0;
            String jsonString;
            File f = new File(CommonString.FILE_PATH);
            File file[] = f.listFiles();
            JSONObject list = new JSONObject();
            filename = "";
            totalfiles = f.listFiles().length;
            if (totalfiles == 0) {
                list.put("[ 0 ]", "no files");
            } else {
                for (int i = 0; i < file.length; i++) {
                    list.put("[" + i + "]", file[i].getName());
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("MID", "0");
            jsonObject.put("Keys", "FileList");
            jsonObject.put("JsonData", list.toString());
            jsonObject.put("UserId", userId);
            jsonString = jsonObject.toString();
            status = 0;
            isvalid = false;

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();

            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
            adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
            PostApi api = adapter.create(PostApi.class);
            Call<JSONObject> observable = api.getUploadJsonDetailForFileList(jsonData);
            observable.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    uploadImage(coverageDate);
                    if (response.isSuccessful() && response.message().equalsIgnoreCase("OK")) {
                        isvalid = true;
                        status = 1;
                    } else {
                        isvalid = true;
                        status = 0;
                    }
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    isvalid = true;
                    uploadImage(coverageDate);
                    // Toast.makeText(context, finalFile.getName() + " not uploaded", Toast.LENGTH_SHORT).show();
                    if (t instanceof IOException || t instanceof SocketTimeoutException || t instanceof SocketException) {
                        status = -1;
                        // AlertandMessages.showAlert((Activity) context, "Error in FileList upload", false);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void uploadImage(String coverageDate) {

        File f = new File(CommonString.FILE_PATH);
        File file[] = f.listFiles();

        if (file.length > 0) {
            UploadImageWithRetrofitOne.uploadedFiles = 0;
            UploadImageWithRetrofitOne.totalFiles = file.length;
            UploadImageWithRetrofitOne uploadImg = new UploadImageWithRetrofitOne(visit_date, userId, context);
            uploadImg.upload_image(context);
        } else {
            UploadImageWithRetrofitOne.totalFiles = file.length;
            new StatusUpload(coverageDate).execute();
        }
    }


    class StatusUpload extends AsyncTask<String, String, String> {
        String coverageDate;

        StatusUpload(String coverageDate) {
            this.coverageDate = coverageDate;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                db = new INTEL_RE_DB(context);
                db.open();
                ArrayList<JourneyPlan> storeList = db.getStoreData(coverageDate);
                for (int i = 0; i < storeList.size(); i++) {
                    if (storeList.get(i).getUploadStatus().equalsIgnoreCase(CommonString.KEY_D)) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("StoreId", storeList.get(i).getStoreId());
                        jsonObject.put("VisitDate", coverageDate);
                        jsonObject.put("UserId", userId);
                        jsonObject.put("Status", CommonString.KEY_U);

                        UploadImageWithRetrofit upload = new UploadImageWithRetrofit(context);
                        String jsonString2 = jsonObject.toString();
                        String result = upload.downloadDataUniversal(jsonString2, CommonString.COVERAGEStatusDetail);

                        if (result.equalsIgnoreCase(CommonString.MESSAGE_NO_RESPONSE_SERVER)) {
                            statusUpdated = false;
                            throw new SocketTimeoutException();
                        } else if (result.equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                            statusUpdated = false;
                            throw new IOException();
                        } else if (result.equalsIgnoreCase(CommonString.MESSAGE_INVALID_JSON)) {
                            statusUpdated = false;
                            throw new JsonSyntaxException("Coverage Status Detail");
                        } else if (result.equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                            statusUpdated = false;
                            throw new Exception();
                        } else {
                            statusUpdated = true;
                            if (db.updateCheckoutStatus(String.valueOf(storeList.get(i).getStoreId()), CommonString.KEY_U, CommonString.TABLE_Journey_Plan) > 0) {
                                db.deleteTableWithStoreID(String.valueOf(storeList.get(i).getStoreId()));

                            } else {
                                //AlertandMessages.showAlert((Activity) context, "Store status not updated", false);
                            }
                        }
                    }
                }

            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_SOCKETEXCEPTION, true);
            } catch (IOException e) {
                e.printStackTrace();
                AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_SOCKETEXCEPTION, true);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INVALID_JSON, true);
            } catch (Exception e) {
                e.printStackTrace();

            }
            if (statusUpdated) {
                return CommonString.KEY_SUCCESS;
            } else {
                return CommonString.KEY_FAILURE;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            if (s.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                if (UploadImageWithRetrofitOne.totalFiles == uploadedFiles && statusUpdated) {
                    AlertandMessages.showAlert((Activity) context, "All images uploaded Successfully", true);
                } else if (UploadImageWithRetrofitOne.totalFiles == uploadedFiles && !statusUpdated) {
                    AlertandMessages.showAlert((Activity) context, "All images uploaded Successfully, but status not updated", true);
                } else {
                    AlertandMessages.showAlert((Activity) context, "Some images not uploaded", true);
                }
            }
        }
    }

    public void uploadDataUsingCoverageRecursive(ArrayList<CoverageBean> coverageList, int coverageIndex) {
        try {
            ArrayList<String> keyList = new ArrayList<>();
            keyList.clear();
            String store_id = coverageList.get(coverageIndex).getStoreId();
            pd.setMessage("Uploading store " + (coverageIndex + 1) + "/" + coverageList.size());
            db.open();
            specific_uploadStatus = db.getSpecificStoreData(store_id);
            String status = null;
            status = specific_uploadStatus.get(0).getUploadStatus();
            pd.setMessage("Uploading store " + (coverageIndex + 1) + "/" + coverageList.size());
            if (!status.equalsIgnoreCase(CommonString.KEY_D)) {
                keyList.add("CoverageDetail_latest");
                keyList.add("STORE_PROFILE_DATA");
                keyList.add("RSP_DETAILS_DATA");
                keyList.add("STORE_AUDIT_NEW_DATA");
                keyList.add("VISIBILITY_SOFT_MERCH_DATA");
                keyList.add("VISIBILITY_SEMIP_MERCH_DATA");
                keyList.add("SHOPER_MKT_IPOS");
                keyList.add("SHOPER_MKT_RXT");
                keyList.add("MARKET_INFO_NEW_DATA");
                keyList.add("GeoTag");
                keyList.add("TRAINING_DATA");
                keyList.add("TRAINING_ADD_RSP_DATA");
            }

            if (keyList.size() > 0) {

                UploadImageWithRetrofit upload = new UploadImageWithRetrofit(context, db, pd, from);
                upload.uploadDataWithoutWait(keyList, 0, coverageList, coverageIndex);
            } else {

                if (++coverageIndex != coverageList.size()) {
                    uploadDataUsingCoverageRecursive(coverageList, coverageIndex);
                } else {
                    String coverageDate = null;
                    if (coverageList.size() > 0) {
                        coverageDate = coverageList.get(0).getVisitDate();
                    } else {
                        coverageDate = visit_date;
                    }
                    UploadImageFileJsonList(context, coverageDate);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            pd.dismiss();
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
                    storePGT = db.getStoreProfileData(coverageList.get(coverageIndex).getStoreId().toString(),
                            coverageList.get(coverageIndex).getVisitDate());
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
                    ArrayList<StoreCategoryMaster> rsp_detailsList = db.getRspDetailinsertData
                            (coverageList.get(coverageIndex).getStoreId().toString());
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
                    String imageallowansw = "";
                    ArrayList<AuditQuestion> auditList = db.getStoreAuditData(coverageList.get(coverageIndex).getStoreId());
                    if (auditList.size() > 0) {
                        JSONArray promoArray = new JSONArray();
                        for (int j = 0; j < auditList.size(); j++) {
                            JSONObject obj = new JSONObject();
                            if (auditList.get(j).getImageAllowforanswer().equalsIgnoreCase("true")) {
                                imageallowansw = "1";
                            } else {
                                imageallowansw = "0";
                            }
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("QUESTION_CD", auditList.get(j).getQuestionId());
                            // obj.put("IMAGE_ALLOW", auditList.get(j).getImageAllow());
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
                    //endregion
                    break;
                case "VISIBILITY_SOFT_MERCH_DATA":
                    //region Competition data
                    db.open();
                    ArrayList<PosmMaster> softmerch_list = db.getVisibilitySoftMerchData(coverageList.get(coverageIndex).getStoreId());
                    if (softmerch_list.size() > 0) {
                        JSONArray compArray = new JSONArray();
                        for (int j = 0; j < softmerch_list.size(); j++) {
                            if (!softmerch_list.get(j).getDeployment_Value().equals("")) {
                                JSONObject obj = new JSONObject();
                                obj.put("MID", coverageList.get(coverageIndex).getMID());
                                obj.put("UserId", userId);
                                obj.put("VISIBILITY_POSM_CD", softmerch_list.get(j).getPosmId().toString());
                                obj.put("VISIBILITY_NEWDEPLOYMENT_VALUE", softmerch_list.get(j).getDeployment_Value());
                                obj.put("VISIBILITY_SOFTIMG", softmerch_list.get(j).getSoft_merchIMG());
                                obj.put("POSM_TYPE_CD", softmerch_list.get(j).getPosmTypeId());
                                compArray.put(obj);
                            }

                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "VISIBILITY_SOFT_MERCH_DATA");
                        jsonObject.put("JsonData", compArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;
                case "VISIBILITY_SEMIP_MERCH_DATA":
                    //region SS_POSM/Touchpoint
                    db.open();
                    List<MappingPermanentPosm> semipermanentMerchList = db.getVisibilitySemiPermanetMerchData(coverageList.get(coverageIndex).getStoreId());
                    if (semipermanentMerchList.size() > 0) {
                        JSONArray posmArray = new JSONArray();
                        for (int j = 0; j < semipermanentMerchList.size(); j++) {
                            if (!semipermanentMerchList.get(j).getNewDeploymnt_Value().equals("")) {
                                JSONObject obj = new JSONObject();
                                obj.put("MID", coverageList.get(coverageIndex).getMID());
                                obj.put("UserId", userId);
                                obj.put("SP_VISIBILITY_POSM_CD", semipermanentMerchList.get(j).getPosmId());
                                obj.put("SP_PREVIOUS_QUANTITY", semipermanentMerchList.get(j).getPrev_Qty());
                                obj.put("SP_PREVIOUS_EDT_VALUE", semipermanentMerchList.get(j).getPreV_dValue());
                                obj.put("SP_VISIBILITY_NEWDEPLOYMENT", semipermanentMerchList.get(j).getNewDeploymnt_Value());
                                obj.put("SP_IMG_1", semipermanentMerchList.get(j).getsPermanetIMG_1());
                                obj.put("SP_IMG_2", semipermanentMerchList.get(j).getsPermanetIMG_2());
                                obj.put("SP_IMG_3", semipermanentMerchList.get(j).getsPermanetIMG_3());
                                posmArray.put(obj);
                            }
                        }
                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "VISIBILITY_SEMIP_MERCH_DATA");
                        jsonObject.put("JsonData", posmArray.toString());
                        jsonObject.put("UserId", userId);
                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;

                    }
                    //endregion
                    break;
                case "SHOPER_MKT_IPOS":
                    //region Secondary_backwall_image
                    db.open();
                    ArrayList<SkuMaster> ipos_listData = db.getipos_inserteddata_object(coverageList.get(coverageIndex).getStoreId());
                    if (ipos_listData.size() > 0) {
                        JSONArray topUpArray = new JSONArray();
                        for (int j = 0; j < ipos_listData.size(); j++) {
                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("SKU_CD", ipos_listData.get(j).getSkuId().toString());
                            obj.put("NUMBER", ipos_listData.get(j).getNoof_display());
                            obj.put("MACHINE_ON", ipos_listData.get(j).getMachine_on());
                            obj.put("IPOS", ipos_listData.get(j).getIpos());
                            obj.put("IPOS_IMG", ipos_listData.get(j).getIpos_img());
                            topUpArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "SHOPER_MKT_IPOS");
                        jsonObject.put("JsonData", topUpArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;
                case "SHOPER_MKT_RXT":
                    //region primary_image_Grid
                    ArrayList<SkuMaster> secCompleteIPOSDATA = db.getrxt_insertedData(coverageList.get(coverageIndex).getStoreId());
                    if (secCompleteIPOSDATA.size() > 0) {
                        JSONArray topUpArray = new JSONArray();
                        for (int j = 0; j < secCompleteIPOSDATA.size(); j++) {
                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("RXT_SKU_CD", secCompleteIPOSDATA.get(j).getSkuId().toString());
                            obj.put("RXT_NUMBER", secCompleteIPOSDATA.get(j).getNoof_display());
                            obj.put("RXT_MACHINE_ON", secCompleteIPOSDATA.get(j).getMachine_on());
                            obj.put("RXT", secCompleteIPOSDATA.get(j).getRxt());
                            obj.put("RXT_IMG", secCompleteIPOSDATA.get(j).getRxt_img());
                            obj.put("ENGEGMENT", secCompleteIPOSDATA.get(j).getEngegment());
                            topUpArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "SHOPER_MKT_RXT");
                        jsonObject.put("JsonData", topUpArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;
                case "MARKET_INFO_NEW_DATA":
                    //region Primary_Stock
                    String existsvalue = "";
                    ArrayList<InfoTypeMaster> market_infoList = db.getinfotypeinsetedDATA(coverageList.get(coverageIndex).getStoreId());
                    if (market_infoList.size() > 0) {
                        JSONArray topUpArray = new JSONArray();
                        for (int j = 0; j < market_infoList.size(); j++) {
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
                    ArrayList<GeotaggingBeans> geotaglist = db.getinsertGeotaggingData(coverageList.get(coverageIndex).
                            getStoreId(), "N");
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
                    //endregion
                    break;
                case "TRAINING_DATA":
                    //region secondary display data
                    db.open();
                    ArrayList<TrainingGetterSetter> trainingList = db.getinsertedTrainingDataForRSPGRZero(
                            coverageList.get(coverageIndex).getStoreId().toString(),
                            coverageList.get(coverageIndex).getVisitDate());
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
                    ArrayList<TrainingGetterSetter> trainingRspUniqueList = db.getinsertedTrainingDataForRSPZero(
                            coverageList.get(coverageIndex).getStoreId().toString(),
                            coverageList.get(coverageIndex).getVisitDate());
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

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();
            final int[] finalJsonIndex = {keyIndex};
            final String finalKeyName = keyList.get(keyIndex);
            if (jsonString != null && !jsonString.equalsIgnoreCase("")) {
                pd.setMessage("Uploading (" + keyIndex + "/" + keyList.size() + ") \n" + keyList.get(keyIndex) + "\n Store uploading " +
                        (coverageIndex + 1) + "/" + coverageList.size());
                RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
                adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create()).build();
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
                                    pd.dismiss();
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
                                            db.updateJaurneyPlanSpecificStoreStatus(coverageList.get(coverageIndex).getStoreId(),
                                                    coverageList.get(coverageIndex).getVisitDate(),
                                                    CommonString.KEY_P);

                                        } catch (NumberFormatException ex) {
                                            pd.dismiss();
                                            AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + finalKeyName, true);
                                        }
                                    } else if (data_global[0].contains(CommonString.KEY_SUCCESS)) {
                                        if (finalKeyName.equalsIgnoreCase("GeoTag")) {
                                        }
                                    } else {
                                        pd.dismiss();
                                        AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + finalKeyName + " : " + data_global[0], true);
                                    }
                                    finalJsonIndex[0]++;
                                    if (finalJsonIndex[0] != keyList.size()) {
                                        uploadDataWithoutWait(keyList, finalJsonIndex[0], coverageList, coverageIndex);
                                    } else {
                                        pd.setMessage("updating status :" + coverageIndex);
                                        //uploading status D for current store from coverageList
                                        specific_uploadStatus.get(0).setUploadStatus(CommonString.KEY_D);
                                        updateStatus(coverageList, coverageIndex, CommonString.KEY_D);
                                    }
                                }

                            } catch (Exception e) {
                                pd.dismiss();
                                AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + finalKeyName, true);
                            }
                        } else {
                            pd.dismiss();
                            AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + finalKeyName, true);

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        pd.dismiss();
                        if (t == null) {
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                        } else {
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE + "(" + t.getMessage().toString() + ")", true);

                        }
                    }
                });

            } else {
                finalJsonIndex[0]++;
                if (finalJsonIndex[0] != keyList.size()) {
                    uploadDataWithoutWait(keyList, finalJsonIndex[0], coverageList, coverageIndex);
                } else {
                    pd.setMessage("updating status :" + coverageIndex);
                    //uploading status D for current store from coverageList
                    specific_uploadStatus.get(0).setUploadStatus(CommonString.KEY_D);
                    updateStatus(coverageList, coverageIndex, CommonString.KEY_D);

                }
            }
        } catch (Exception ex) {
            pd.dismiss();
            AlertandMessages.showAlert((Activity) context, ex.toString(), true);
        }
    }

    void updateStatus(final ArrayList<CoverageBean> coverageList, final int coverageIndex,
                      final String status) {
        if (coverageList.get(coverageIndex) != null) {
            try {
                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .build();

                final int[] tempcoverageIndex = {coverageIndex};
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
                pd.setMessage("Uploading store status " + (coverageIndex + 1) + "/" + coverageList.size());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody responseBody = response.body();
                        String data = null;
                        if (responseBody != null && response.isSuccessful()) {
                            try {
                                data = response.body().string();
                                if (data.equalsIgnoreCase("")) {
                                    pd.dismiss();
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
                                            pd.setMessage("updoading images");
                                            String coverageDate = null;
                                            if (coverageList.size() > 0) {
                                                coverageDate = coverageList.get(0).getVisitDate();
                                            } else {
                                                coverageDate = visit_date;
                                            }

                                            UploadImageFileJsonList(context, coverageDate);
                                        }
                                    } else {
                                        pd.dismiss();
                                        AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + coverageIndex, true);
                                    }

                                }
                            } catch (Exception e) {
                                pd.dismiss();
                                AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + coverageIndex, true);
                            }
                        } else {
                            pd.dismiss();
                            AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + coverageIndex, true);

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        pd.dismiss();
                        if (t == null) {
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                        } else {
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE + "(" + t.getMessage().toString() + ")", true);
                        }

                    }
                });

            } catch (JSONException ex) {
                pd.dismiss();
                AlertandMessages.showAlert((Activity) context, ex.toString(), true);

            }
        }

    }


}
*/
