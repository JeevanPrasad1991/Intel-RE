package intelre.cpm.com.intelre.dailyentry;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.AlertandMessages;
import intelre.cpm.com.intelre.constant.CommonFunctions;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.delegates.CoverageBean;
import intelre.cpm.com.intelre.retrofit.PostApi;
import intelre.cpm.com.intelre.upload.UploadDataActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckoutActivty extends AppCompatActivity implements View.OnClickListener {
    ImageView img_cam, img_clicked;
    FloatingActionButton btn_save;
    String _pathforcheck, _path, str;
    String store_cd, visit_date, username;
    private SharedPreferences preferences;
    private INTEL_RE_DB database;
    ByteArrayOutputStream bytearrayoutputstream;
    ArrayList<CoverageBean> specificDATa = new ArrayList<>();
    AlertDialog alert;
    String img_str;
    ProgressDialog loading;
    String app_ver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_activty);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        img_cam = findViewById(R.id.img_selfie_check);
        img_clicked = findViewById(R.id.img_cam_selfie_checkout);
        bytearrayoutputstream = new ByteArrayOutputStream();
        btn_save = (FloatingActionButton) findViewById(R.id.btn_save_selfie_checkout);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        app_ver = preferences.getString(CommonString.KEY_VERSION, "");
        if (!CommonFunctions.isTablet(this)){
            toolbar.setTitleTextAppearance(this,R.style.changestext_sizefor_mobile);
        }
        setTitle("Checkout image - " + visit_date);
        str = CommonString.FILE_PATH;
        store_cd = getIntent().getStringExtra(CommonString.KEY_STORE_CD);
        database = new INTEL_RE_DB(this);
        database.open();
        img_cam.setOnClickListener(this);
        img_clicked.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        specificDATa = database.getSpecificCoverageData(visit_date, store_cd);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                    CheckoutActivty.this.finish();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                CheckoutActivty.this.finish();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_cam_selfie_checkout:
                _pathforcheck = store_cd + "_STOREC_OUTIMG_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                CommonFunctions.startAnncaCameraActivity(this, _path, null, false);
                break;

            case R.id.btn_save_selfie_checkout:

                if (img_str != null) {
                    if (checkNetIsAvailable()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivty.this).setTitle(getString(R.string.parinaam)).setMessage(getString(R.string.alert_save)).setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                try {
                                                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                                    if (specificDATa.size() > 0) {
                                                        JSONObject jsonObject = new JSONObject();
                                                        jsonObject.put("UserId", username);
                                                        jsonObject.put("StoreId", specificDATa.get(0).getStoreId());
                                                        jsonObject.put("Latitude", specificDATa.get(0).getLatitude());
                                                        jsonObject.put("Longitude", specificDATa.get(0).getLongitude());
                                                        jsonObject.put("Checkout_Date", specificDATa.get(0).getVisitDate());
                                                        uploadCoverageIntimeDATA(jsonObject.toString());
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int id) {
                                        dialog.cancel();
                                    }
                                });
                        alert = builder.create();
                        alert.show();
                    } else {
                        Snackbar.make(btn_save, "No Network Available", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(btn_save, "Please click the checkout image.", Snackbar.LENGTH_SHORT).show();

                }

                break;
        }

    }

    public boolean checkNetIsAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    protected void startCameraActivity() {
        try {
            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(file);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("MakeMachine", "resultCode: " + resultCode);
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;
            case -1:
                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    try {
                        if (new File(str + _pathforcheck).exists()) {
                            String metadata = CommonFunctions.setMetadataAtImages(preferences.getString(CommonString.KEY_STORE_NAME, null), store_cd, "Checkout Image", username);
                            Bitmap bmp = CommonFunctions.addMetadataAndTimeStampToImage(this, _path, metadata, visit_date);
                            img_cam.setImageBitmap(bmp);
                            img_clicked.setVisibility(View.GONE);
                            img_cam.setVisibility(View.VISIBLE);
                            //Set Clicked image to Imageview
                            img_str = _pathforcheck;
                            _pathforcheck = "";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
    }


    public void uploadCoverageIntimeDATA(String jsondata) {
        try {
            loading = ProgressDialog.show(CheckoutActivty.this, "Processing", "Please wait...",
                    false, false);
            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsondata.toString());
            Retrofit adapter = new Retrofit.Builder().baseUrl(CommonString.URL)
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
                                database.open();
                                database.updateCoverageCheckoutIMG(store_cd, visit_date, img_str);
                                database.updateJaurneyPlanSpecificStoreStatus(store_cd, visit_date, CommonString.KEY_C);
                                startActivity(new Intent(CheckoutActivty.this, UploadDataActivity.class));
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                CheckoutActivty.this.finish();
                                loading.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Crashlytics.logException(e);
                            loading.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    loading.dismiss();
                    if (t instanceof SocketTimeoutException || t instanceof IOException || t instanceof Exception) {
                        AlertandMessages.showAlertlogin(CheckoutActivty.this, t.getMessage().toString());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
            loading.dismiss();

        }

    }
}
