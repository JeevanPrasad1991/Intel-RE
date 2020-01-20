/*
package intelre.cpm.com.intelre.upload;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import java.util.ArrayList;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.delegates.CoverageBean;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;

*/
/**
 * Created by jeevanp on 5/29/2018.
 *//*


public class UploadDataViaGTActivity extends AppCompatActivity {
    INTEL_RE_DB db;
    ArrayList<CoverageBean> coverageList;
    String date, userId, app_version;
    String Path, app_ver;
    private ProgressDialog pb;
    private SharedPreferences preferences;
    Object result = "";
    Toolbar toolbar;
    JourneyPlan jcpGetset;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        context = this;
        if (getIntent().getSerializableExtra(CommonString.TAG_OBJECT) != null) {
            jcpGetset = (JourneyPlan) getIntent().getSerializableExtra(CommonString.TAG_OBJECT);
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            app_ver = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        date = preferences.getString(CommonString.KEY_DATE, null);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        app_version = preferences.getString(CommonString.KEY_VERSION, null);
        toolbar.setTitle("Upload - " + date);
        Path = CommonString.FILE_PATH;
        db = new INTEL_RE_DB(this);
        db.open();
        coverageList = db.getCoverageData(date);
        pb = new ProgressDialog(context);
        pb.setCancelable(false);
        pb.setMessage("Uploading Data");
        pb.show();
        UploadImageWithRetrofit upload = new UploadImageWithRetrofit(context, db, pb, CommonString.TAG_FROM_CURRENT);
        upload.uploadDataUsingCoverageRecursive(coverageList, 0);
    }

    public void showAlert(String str) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Parinaam");
        builder.setMessage(str).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
*/
