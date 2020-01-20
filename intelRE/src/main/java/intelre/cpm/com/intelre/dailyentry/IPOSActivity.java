package intelre.cpm.com.intelre.dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.CommonFunctions;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gsonGetterSetter.SkuMaster;

public class IPOSActivity extends AppCompatActivity implements View.OnClickListener {
    String store_cd, visit_date, username, Error_Message, _pathforcheck, _path, img1 = "";
    EditText ipos_noof_display, ipos_noof_machineon, ipos_edt, costomized_ipos,gaming_ipos;
    private SharedPreferences.Editor editor = null;
    SkuMaster ipos_Object = new SkuMaster();
    private SharedPreferences preferences;
    FloatingActionButton fab;
    CardView card_viewIPOS;
    boolean updateflag=false;
    ImageView ipos_img;
    Context context;
    INTEL_RE_DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipos);
        context = this;
        ipos_uid_data();
        addontextchangeonEdittext();
        validateinserted_data();
    }

    private void ipos_uid_data() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ipos_noof_display = (EditText) findViewById(R.id.ipos_noof_display);
        ipos_noof_machineon = (EditText) findViewById(R.id.ipos_noof_machineon);
        ipos_edt = (EditText) findViewById(R.id.ipos_edt);
        ipos_img = (ImageView) findViewById(R.id.ipos_img);
        card_viewIPOS = (CardView) findViewById(R.id.card_viewIPOS);
        costomized_ipos = (EditText) findViewById(R.id.costomized_ipos);
        gaming_ipos = (EditText) findViewById(R.id.gaming_ipos);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();

        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        if (!CommonFunctions.isTablet(context)){
            toolbar.setTitleTextAppearance(context,R.style.changestext_sizefor_mobile);
        }
        setTitle(getString(R.string.ipos) + " - " + visit_date);
        db = new INTEL_RE_DB(context);
        db.open();
        ipos_img.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    private boolean validate() {
        boolean status = false;
        try {
            if (ipos_noof_display.getText().toString().isEmpty()) {
                Error_Message = "Please enter Numbers of Display.";
            } else if (ipos_noof_machineon.getText().toString().isEmpty()) {
                Error_Message = "Please enter Number of Machine On.";
            } else if (Integer.parseInt(ipos_noof_machineon.getText().toString()) > Integer.parseInt(ipos_noof_display.getText().toString())) {
                Error_Message = "Number of Machine On must be less then OR equal to Numbers of Display.";
                ipos_noof_machineon.setText("");
            } else if (ipos_edt.getText().toString().isEmpty()) {
                Error_Message = "Please enter IPOS.";
            } else if (Integer.parseInt(ipos_edt.getText().toString()) > Integer.parseInt(ipos_noof_machineon.getText().toString())) {
                Error_Message = "IPOS must be less then OR equal to Number of Machine On.";
                ipos_edt.setText("");
            } else if (costomized_ipos.getText().toString().isEmpty()) {
                Error_Message = "Please enter Customized IPOS.";
            } else if (!ipos_edt.getText().toString().isEmpty() && !costomized_ipos.getText().toString().isEmpty() && Integer.parseInt(costomized_ipos.getText().toString()) > Integer.parseInt(ipos_edt.getText().toString())) {
                Error_Message = "Customized IPOS must be less then OR equal to IPOS.";
                costomized_ipos.setText("");
            }
            else if (gaming_ipos.getText().toString().isEmpty()) {
                Error_Message = "Please enter Gaming IPOS.";
            } else if (!ipos_edt.getText().toString().isEmpty() && !gaming_ipos.getText().toString().isEmpty() && Integer.parseInt(gaming_ipos.getText().toString()) > Integer.parseInt(ipos_edt.getText().toString())) {
                Error_Message = "Gaming IPOS must be less then OR equal to IPOS.";
                gaming_ipos.setText("");
            }
            else if (!ipos_edt.getText().toString().isEmpty() && !ipos_edt.getText().toString().equals("0") && img1.equals("")) {
                Error_Message = "Please click IPOS image.";
            } else {

                ////addded data in ipos object................
                ipos_Object.setNoof_display(ipos_noof_display.getText().toString());
                ipos_Object.setMachine_on(ipos_noof_machineon.getText().toString());
                ipos_Object.setIpos(ipos_edt.getText().toString());
                ipos_Object.setCustomised_ipos(costomized_ipos.getText().toString());
                ipos_Object.setGaming_ipos(gaming_ipos.getText().toString());
                ipos_Object.setIpos_img(img1);
                status = true;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }

        return status;
    }


    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                IPOSActivity.this.finish();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                    IPOSActivity.this.finish();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

        }

        return super.onOptionsItemSelected(item);
    }

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
                        if (new File(CommonString.FILE_PATH + _pathforcheck).exists()) {
                            String metadata = CommonFunctions.setMetadataAtImages(preferences.getString(CommonString.KEY_STORE_NAME, null), store_cd, "Ipos Image", username);
                            CommonFunctions.addMetadataAndTimeStampToImage(context, _path, metadata, visit_date);
                            ipos_img.setImageResource(R.mipmap.camera_green);
                            img1 = _pathforcheck;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ipos_img:
                _pathforcheck = store_cd + "_IPOSING_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                CommonFunctions.startAnncaCameraActivity(context, _path, null, false);
                break;
            case R.id.fab:
                if (validate()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(getString(R.string.parinaam)).setMessage(getString(R.string.alert_save));
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.open();
                            db.insertIPOSData(store_cd, visit_date, ipos_Object);
                            Snackbar.make(fab, "Data has been saved", Snackbar.LENGTH_SHORT).show();
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                            IPOSActivity.this.finish();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    });
                    builder.show();
                } else {
                    Snackbar.make(fab, Error_Message, Snackbar.LENGTH_LONG).setAction("", null).show();
                }
                break;
        }
    }

    private void addontextchangeonEdittext() {
        ipos_noof_display.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String value = editable.toString();
                if (value.equals("0")) {
                    if (updateflag){
                        img1="";
                    }
                    ipos_noof_machineon.setText("0");
                    costomized_ipos.setText("0");
                    ipos_edt.setText("0");
                    gaming_ipos.setText("0");

                    ipos_edt.setEnabled(false);
                    ipos_img.setEnabled(false);
                    costomized_ipos.setEnabled(false);
                    ipos_noof_machineon.setEnabled(false);
                    gaming_ipos.setEnabled(false);
                    ipos_img.setImageResource(R.mipmap.camera_grey);
                } else {
                    if (updateflag){
                        img1="";
                    }
                    ipos_noof_machineon.setText("");
                    ipos_edt.setText("");
                    costomized_ipos.setText("");
                    gaming_ipos.setText("");
                    costomized_ipos.setEnabled(true);
                    ipos_noof_machineon.setEnabled(true);
                    ipos_edt.setEnabled(true);
                    gaming_ipos.setEnabled(true);
                    ipos_img.setImageResource(R.mipmap.camera_orange);
                    ipos_img.setEnabled(true);
                }
            }
        });

        ipos_noof_machineon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("0")) {
                    if (updateflag){
                        img1="";
                    }
                    costomized_ipos.setText("0");
                    ipos_edt.setText("0");
                    gaming_ipos.setText("0");
                    ipos_edt.setEnabled(false);
                    ipos_img.setEnabled(false);
                    costomized_ipos.setEnabled(false);
                    gaming_ipos.setEnabled(false);
                    ipos_img.setImageResource(R.mipmap.camera_grey);
                } else {
                    if (updateflag){
                        img1="";
                    }
                    ipos_edt.setText("");
                    costomized_ipos.setText("");
                    gaming_ipos.setText("");
                    costomized_ipos.setEnabled(true);
                    ipos_edt.setEnabled(true);
                    gaming_ipos.setEnabled(true);
                    ipos_img.setImageResource(R.mipmap.camera_orange);
                    ipos_img.setEnabled(true);
                }

            }
        });

        ipos_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("0")) {
                    if (updateflag){
                        img1="";
                    }
                    costomized_ipos.setText("0");
                    gaming_ipos.setText("0");
                    ipos_img.setEnabled(false);
                    gaming_ipos.setEnabled(false);
                    costomized_ipos.setEnabled(false);
                    ipos_img.setImageResource(R.mipmap.camera_grey);
                } else {
                    if (updateflag){
                        img1="";
                    }
                    costomized_ipos.setText("");
                    gaming_ipos.setText("");
                    costomized_ipos.setEnabled(true);
                    ipos_img.setImageResource(R.mipmap.camera_orange);
                    ipos_img.setEnabled(true);
                    gaming_ipos.setEnabled(true);
                }
            }
        });
    }

    private void validateinserted_data() {
        db.open();
        ipos_Object = db.getipos_inserteddata_object(store_cd);
        try {
            if (ipos_Object.getKey_id() != null) {
                updateflag=true;
                ipos_noof_display.setText(ipos_Object.getNoof_display());
                ipos_noof_machineon.setText(ipos_Object.getMachine_on());
                ipos_edt.setText(ipos_Object.getIpos());
                costomized_ipos.setText(ipos_Object.getCustomised_ipos());
                gaming_ipos.setText(ipos_Object.getGaming_ipos());
                if (!ipos_Object.getIpos().equals("0")) {
                    img1 = ipos_Object.getIpos_img();
                    ipos_img.setImageResource(R.mipmap.camera_green);
                } else {
                    ipos_img.setImageResource(R.mipmap.camera_grey);
                }
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.edit_txt));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }
}
