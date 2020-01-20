package intelre.cpm.com.intelre.download;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;


import org.json.JSONObject;

import java.util.ArrayList;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.CommonString;

import intelre.cpm.com.intelre.retrofit.DownloadAllDatawithRetro;

public class DownloadActivity extends AppCompatActivity {
    INTEL_RE_DB db;
    String userId, date;
    private SharedPreferences preferences = null;
    Toolbar toolbar;
    Context context;
    int downloadindex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        db = new INTEL_RE_DB(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        date = preferences.getString(CommonString.KEY_DATE, "");
        downloadindex = preferences.getInt(CommonString.KEY_DOWNLOAD_INDEX, 0);
        getSupportActionBar().setTitle("Download - " + date);
        toolbar.setTitle("Download - " + date);
        UploadDataTask();
    }

    public void UploadDataTask() {
        try {
            ArrayList<String> keysList = new ArrayList<>();
            ArrayList<String> jsonList = new ArrayList<>();
            ArrayList<String> KeyNames = new ArrayList<>();
            KeyNames.clear();
            keysList.clear();
            keysList.add("Table_Structure");
            keysList.add("Journey_Plan");
            keysList.add("Non_Working_Reason");
            keysList.add("Posm_Master");
            keysList.add("Rsp_Detail");
            keysList.add("Audit_Question");
            keysList.add("Training_Type");
            keysList.add("Training_Topic");
            keysList.add("Mapping_SoftPosm");
            keysList.add("Category_Master");
            keysList.add("Brand_Master");
            keysList.add("Sku_Master");
            keysList.add("Info_Type_Master");
            keysList.add("Mapping_semi_permanent_posm");
            keysList.add("Posm_Type_Question");
            keysList.add("Posm_Type");
            keysList.add("Brave_Index_Score");

            if (keysList.size() > 0) {
                for (int i = 0; i < keysList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Downloadtype", keysList.get(i));
                    jsonObject.put("Username", userId);
                    jsonList.add(jsonObject.toString());
                    KeyNames.add(keysList.get(i));
                }

                if (jsonList.size() > 0) {
                    ProgressDialog pd = new ProgressDialog(context);
                    pd.setCancelable(false);
                    pd.setMessage("Downloading Data" + "(" + "/" + ")");
                    pd.show();
                    DownloadAllDatawithRetro downloadData = new DownloadAllDatawithRetro(context, db, pd, CommonString.TAG_FROM_CURRENT);
                    downloadData.listSize = jsonList.size();
                    downloadData.downloadDataUniversalWithoutWait(jsonList, KeyNames, downloadindex, CommonString.DOWNLOAD_ALL_SERVICE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
