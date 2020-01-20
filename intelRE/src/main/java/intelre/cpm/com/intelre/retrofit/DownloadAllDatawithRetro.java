package intelre.cpm.com.intelre.retrofit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import intelre.cpm.com.intelre.constant.AlertandMessages;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gettersetter.ReferenceVariablesForDownloadActivity;
import intelre.cpm.com.intelre.gsonGetterSetter.AuditQuestionGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.BrandMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.CategoryMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.InfoTypeMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.JCPGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.AllPosmTableGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.MappingSoftPosmGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.NonWorkingReasonGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.RspDetailGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.SkuMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.TableStructure;
import intelre.cpm.com.intelre.gsonGetterSetter.TableStructureGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.TrainingTopicGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.TrainingTypeGetterSetter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by upendrak on 15-12-2017.
 */

public class DownloadAllDatawithRetro extends ReferenceVariablesForDownloadActivity {
    boolean isvalid;
    private Retrofit adapter;
    Context context;
    public int listSize = 0;
    int status = 0;
    INTEL_RE_DB db;
    ProgressDialog pd;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String _UserId, date, app_ver;
    int from;

    public DownloadAllDatawithRetro(Context context) {
        this.context = context;
    }

    public DownloadAllDatawithRetro(Context context, INTEL_RE_DB db, ProgressDialog pd, int from) {

        this.context = context;
        this.db = db;
        this.pd = pd;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        this.from = from;
        db.open();
    }


    public void downloadDataUniversalWithoutWait(final ArrayList<String> jsonStringList, final ArrayList<String> KeyNames, int downloadindex, int type) {
        status = 0;
        isvalid = false;
        final String[] data_global = {""};
        String jsonString = "", KeyName = "";
        int jsonIndex = 0;

        if (jsonStringList.size() > 0) {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();
            jsonString = jsonStringList.get(downloadindex);
            KeyName = KeyNames.get(downloadindex);
            jsonIndex = downloadindex;

            pd.setMessage("Downloading (" + downloadindex + "/" + listSize + ") \n" + KeyName + "");
            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
            adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
            PostApi api = adapter.create(PostApi.class);
            Call<String> call = api.getDownloadAll(jsonData);
            final int[] finalJsonIndex = {jsonIndex};
            final String finalKeyName = KeyName;

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String responseBody = response.body();
                    String data = null;
                    if (responseBody != null && response.isSuccessful()) {
                        try {
                            data = response.body();
                            if (data.equalsIgnoreCase("")) {
                                data_global[0] = "";
                            } else {
                                data_global[0] = data;
                                if (finalKeyName.equalsIgnoreCase("Table_Structure")) {
                                    editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, finalJsonIndex[0]);
                                    editor.apply();
                                    tableStructureObj = new Gson().fromJson(data, TableStructureGetterSetter.class);
                                    String isAllTableCreated = createTable(tableStructureObj);
                                    if (isAllTableCreated != CommonString.KEY_SUCCESS) {
                                        pd.dismiss();
                                        AlertandMessages.showAlert((Activity) context, isAllTableCreated + " not created", true);
                                    }
                                } else {
                                    editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, finalJsonIndex[0]);
                                    editor.apply();
                                    switch (finalKeyName) {
                                        case "Journey_Plan":
                                            if (!data.contains("No Data")) {
                                                jcpObject = new Gson().fromJson(data, JCPGetterSetter.class);
                                                if (jcpObject != null && !db.insertJCPData(jcpObject)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "JCP data not saved");
                                                }
                                            } else {
                                                throw new java.lang.Exception();
                                            }
                                            break;
                                        case "Non_Working_Reason":
                                            if (!data.contains("No Data")) {
                                                nonWorkingObj = new Gson().fromJson(data, NonWorkingReasonGetterSetter.class);
                                                if (nonWorkingObj != null && !db.insertNonWorkingData(nonWorkingObj)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Non Working Reason not saved");
                                                }
                                            } else {
                                                throw new java.lang.Exception();
                                            }
                                            break;
                                        case "Posm_Master":
                                            if (!data.contains("No Data")) {
                                                posmMObject = new Gson().fromJson(data, PosmMasterGetterSetter.class);
                                                if (posmMObject != null && !db.insertPosmMaster(posmMObject)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Posm Master not saved");
                                                }
                                            } else {
                                                throw new java.lang.Exception();
                                            }
                                            break;
                                        case "Rsp_Detail":
                                            if (!data.contains("No Data")) {
                                                rspDetailObject = new Gson().fromJson(data, RspDetailGetterSetter.class);
                                                if (rspDetailObject != null && !db.insertRspDetailnData(rspDetailObject)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Rsp Detail not saved");
                                                }
                                                break;
                                            }
                                            break;
                                        case "Audit_Question":
                                            auditQuestionObject = new Gson().fromJson(data, AuditQuestionGetterSetter.class);
                                            if (auditQuestionObject != null && !db.insertAuditQuestionData(auditQuestionObject)) {
                                                pd.dismiss();
                                                AlertandMessages.showSnackbarMsg(context, "Audit Question not saved");
                                            }
                                            break;

                                        //TODAY
                                        case "Training_Type":
                                            if (!data.contains("No Data")) {
                                                trainingTypeObject = new Gson().fromJson(data, TrainingTypeGetterSetter.class);
                                                if (trainingTypeObject != null && !db.insertTrainingTypeData(trainingTypeObject)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Training_Type not saved");
                                                }
                                            }
                                            break;
                                        case "Training_Topic":
                                            if (!data.contains("No Data")) {
                                                trainingTopiceObject = new Gson().fromJson(data, TrainingTopicGetterSetter.class);
                                                if (trainingTopiceObject != null && !db.insertTrainingTopicData(trainingTopiceObject)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Training Topic not saved");
                                                }
                                            }
                                            break;
                                        case "Mapping_SoftPosm":
                                            if (!data.contains("No Data")) {
                                                softm_posmObject = new Gson().fromJson(data, AllPosmTableGetterSetter.class);
                                                if (softm_posmObject != null && !db.insertMappingSoftPosmData(softm_posmObject)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Mapping Soft Posm not saved");
                                                }
                                            }
                                            break;


                                        case "Mapping_semi_permanent_posm":
                                            if (!data.contains("No Data")) {
                                                mappingSemiparmanentObject = new Gson().fromJson(data, AllPosmTableGetterSetter.class);
                                                if (mappingSemiparmanentObject != null && !db.insertmappingsemiparmanentsoft(mappingSemiparmanentObject)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Mapping semi Permanent Posm not saved");
                                                }
                                            }
                                            break;


                                        case "Posm_Type_Question":
                                            if (!data.contains("No Data")) {
                                                posmTypeQuestionObject = new Gson().fromJson(data, AllPosmTableGetterSetter.class);
                                                if (posmTypeQuestionObject != null && !db.insertposmTypeQuestion(posmTypeQuestionObject)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Posm Type Question data not saved");
                                                }
                                            }
                                            break;


                                        case "Posm_Type":
                                            if (!data.contains("No Data")) {
                                                posm_typeObject = new Gson().fromJson(data, AllPosmTableGetterSetter.class);
                                                if (posm_typeObject != null && !db.insertposmTypedata(posm_typeObject)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Posm Type data not saved");
                                                }
                                            }
                                            break;


                                        case "Category_Master":
                                            if (!data.contains("No Data")) {
                                                categoryMasterObject = new Gson().fromJson(data, CategoryMasterGetterSetter.class);
                                                if (categoryMasterObject != null && !db.insertCategoryMasterData(categoryMasterObject)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Mapping Permanent Posm not saved");
                                                }
                                            } else {
                                                throw new java.lang.Exception();
                                            }
                                            break;
                                        case "Brand_Master":
                                            if (!data.contains("No Data")) {
                                                brandMasterObject = new Gson().fromJson(data, BrandMasterGetterSetter.class);
                                                if (brandMasterObject != null && !db.insertBrandMasterData(brandMasterObject)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Mapping Permanent Posm not saved");
                                                }
                                            } else {
                                                throw new java.lang.Exception();
                                            }

                                            break;
                                        case "Sku_Master":
                                            if (!data.contains("No Data")) {
                                                skuMasterObject = new Gson().fromJson(data, SkuMasterGetterSetter.class);
                                                if (skuMasterObject != null && !db.insertSkuMasterData(skuMasterObject)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Mapping Permanent Posm not saved");
                                                }
                                            } else {
                                                throw new java.lang.Exception();
                                            }
                                            break;


                                        case "Info_Type_Master":
                                            if (!data.contains("No Data")) {
                                                infotypeObject = new Gson().fromJson(data, InfoTypeMasterGetterSetter.class);
                                                if (infotypeObject != null && !db.insertInfoTypeData(infotypeObject)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Mapping Permanent Posm not saved");
                                                }
                                            } else {
                                                throw new java.lang.Exception();
                                            }

                                            break;

                                        case "Brave_Index_Score":
                                            if (!data.contains("No Data")) {
                                                brave_indexscoreObject = new Gson().fromJson(data, AllPosmTableGetterSetter.class);
                                                if (brave_indexscoreObject != null && !db.insertbraveindex_scoredata(brave_indexscoreObject)) {
                                                    pd.dismiss();
                                                    AlertandMessages.showSnackbarMsg(context, "Brave Index Score data not saved");
                                                }
                                            }
                                            break;

                                    }
                                }
                            }

                            finalJsonIndex[0]++;
                            if (finalJsonIndex[0] != KeyNames.size()) {
                                editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, finalJsonIndex[0]);
                                editor.apply();
                                downloadDataUniversalWithoutWait(jsonStringList, KeyNames, finalJsonIndex[0],
                                        CommonString.DOWNLOAD_ALL_SERVICE);
                            } else {
                                editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, 0);
                                editor.apply();
                                pd.setMessage("Downloading Images");
                                new DownloadImageTask(pd).execute();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, finalJsonIndex[0]);
                            editor.apply();
                            pd.dismiss();
                            AlertandMessages.showAlert((Activity) context, finalKeyName + " Data not found ", true);
                        }
                    } else {
                        editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, finalJsonIndex[0]);
                        editor.apply();
                        pd.dismiss();
                        AlertandMessages.showAlert((Activity) context, "Error in downloading Data at " + finalKeyName, true);

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    isvalid = true;
                    pd.dismiss();
                    if (t instanceof SocketTimeoutException || t instanceof IOException || t instanceof SocketException) {
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE + "(" + t.toString() + ")", true);
                    } else {
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                    }
                }
            });
        } else {
            editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, 0);
            editor.apply();
            pd.setMessage("Downloading Images");
            new DownloadImageTask(pd).execute();
        }
    }

    String createTable(TableStructureGetterSetter tableGetSet) {
        List<TableStructure> tableList = tableGetSet.getTableStructure();
        for (int i = 0; i < tableList.size(); i++) {
            String table = tableList.get(i).getSqlText();
            if (db.createtable(table) == 0) {
                return table;
            }
        }

        return CommonString.KEY_SUCCESS;
    }

    public String downloadDataUniversal(final String jsonString, int type) {
        try {
            status = 0;
            isvalid = false;
            final String[] data_global = {""};
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();
            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
            adapter = new Retrofit.Builder()
                    .baseUrl(CommonString.URL).client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            PostApi api = adapter.create(PostApi.class);
            Call<JsonObject> call = api.getGeotag(jsonData);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    String responseBody = response.body().get("UploadJsonDetailResult").toString();
                    String data = null;
                    if (responseBody != null && response.isSuccessful()) {
                        try {
                            data = response.body().get("UploadJsonDetailResult").toString();

                            if (data.equalsIgnoreCase("")) {
                                data_global[0] = "";
                                isvalid = true;
                                status = 1;
                            } else {
                                data = data.substring(1, data.length() - 1).replace("\\", "");
                                data_global[0] = data;
                                isvalid = true;
                                status = 1;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            isvalid = true;
                            status = -2;
                        }
                    } else {
                        isvalid = true;
                        status = -1;
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    isvalid = true;
                    if (t instanceof SocketTimeoutException) {
                        status = 3;
                    } else if (t instanceof IOException) {
                        status = 3;
                    } else {
                        status = 3;
                    }

                }
            });

            while (isvalid == false) {
                synchronized (this) {
                    this.wait(25);
                }
            }
            if (isvalid) {
                synchronized (this) {
                    this.notify();
                }
            }
            if (status == 1) {
                return data_global[0];
            } else if (status == 2) {
                return CommonString.MESSAGE_NO_RESPONSE_SERVER;
            } else if (status == 3) {
                return CommonString.MESSAGE_SOCKETEXCEPTION;
            } else if (status == -2) {
                return CommonString.MESSAGE_INVALID_JSON;
            } else {
                return CommonString.KEY_FAILURE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommonString.KEY_FAILURE;
        }
    }

    class DownloadImageTask extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        DownloadImageTask(ProgressDialog pd) {
            this.pd = pd;
        }

        @Override
        protected String doInBackground(String... strings) {
            return CommonString.KEY_SUCCESS;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            if (s != null && s.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                pd.dismiss();
                AlertandMessages.showAlert((Activity) context, "All data downloaded Successfully", true);
            } else {
                pd.dismiss();
                AlertandMessages.showAlert((Activity) context, "Error in downloading", true);
            }

        }

    }

    protected void onDestroy_dialog() {
        if (pd != null)
            if (pd.isShowing()) {
                pd.dismiss();
            }
        pd = null;
    }


}
