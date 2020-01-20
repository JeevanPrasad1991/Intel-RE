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

public class RXTMarketingTActivity extends AppCompatActivity implements View.OnClickListener {
    String store_cd, visit_date, user_type, username, Error_Message, _pathforcheck, _path, img1 = "";
    EditText rxtnoof_display, rxt_machine_onEDT, rxt_EDT, engegment_rxt, customized_rxt;
    private SharedPreferences.Editor editor = null;
    SkuMaster rxt_Object = new SkuMaster();
    private SharedPreferences preferences;
    boolean updateflag=false;
    FloatingActionButton fab;
    ImageView rxt_img;
    Context context;
    INTEL_RE_DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxtmarketing_t);
        rxt_ui_interface();
        addontextchangeonEdittext();
        validateinserted_data();
    }

    private void rxt_ui_interface() {
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rxtnoof_display = (EditText) findViewById(R.id.rxt_noof_display);
        rxt_machine_onEDT = (EditText) findViewById(R.id.rxt_machine_onEDT);
        engegment_rxt = (EditText) findViewById(R.id.engegment_rxt);
        rxt_EDT = (EditText) findViewById(R.id.rxt_EDT);
        customized_rxt = (EditText) findViewById(R.id.customized_rxt);
        rxt_img = (ImageView) findViewById(R.id.rxt_img);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        if (!CommonFunctions.isTablet(context)){
            toolbar.setTitleTextAppearance(context,R.style.changestext_sizefor_mobile);
        }
        setTitle(getString(R.string.rxt) + " - " + visit_date);
        db = new INTEL_RE_DB(context);
        db.open();
        rxt_img.setOnClickListener(this);
        fab.setOnClickListener(this);
    }


    private boolean validate() {
        boolean status = false;
        try {
            if (rxtnoof_display.getText().toString().isEmpty()) {
                Error_Message = "Please enter Numbers of Display.";
            } else if (rxt_machine_onEDT.getText().toString().isEmpty()) {
                Error_Message = "Please enter Numbers of Machine On.";
            } else if (Integer.parseInt(rxt_machine_onEDT.getText().toString()) > Integer.parseInt(rxtnoof_display.getText().toString())) {
                Error_Message = "Number of Machine on must be less then OR equal to Numbers of Display.";
                rxt_machine_onEDT.setText("");
            } else if (engegment_rxt.getText().toString().isEmpty()) {
                Error_Message = "Please enter Numbers of Engagement.";
            } else if (rxt_EDT.getText().toString().isEmpty()) {
                Error_Message = "Please enter RXT Screens.";
            } else if (Integer.parseInt(rxt_EDT.getText().toString()) > Integer.parseInt(rxt_machine_onEDT.getText().toString())) {
                Error_Message = "RXT Screens  must be less than OR equal to Number of Machine On .";
                rxt_EDT.setText("");
            } else if (customized_rxt.getText().toString().isEmpty()) {
                Error_Message = "Please enter Customized RXT Screens.";
            } else if (Integer.parseInt(customized_rxt.getText().toString()) > Integer.parseInt(rxt_EDT.getText().toString())) {
                Error_Message = "Customized RXT Screens must be less than OR equal to RXT screens.";
                customized_rxt.setText("");
            } else if (!rxt_EDT.getText().toString().equals("0") && img1.equals("")) {
                Error_Message = "Please click RXT Screens image.";
            } else {
                ////addded data in ipos object................
                if (Integer.parseInt(rxt_EDT.getText().toString()) == 0) {
                    engegment_rxt.setText("0");
                    rxt_Object.setEngegment("0");
                }

                rxt_Object.setNoof_display(rxtnoof_display.getText().toString());
                rxt_Object.setMachine_on(rxt_machine_onEDT.getText().toString());
                rxt_Object.setEngegment(engegment_rxt.getText().toString());
                rxt_Object.setRxt(rxt_EDT.getText().toString());
                rxt_Object.setCustomised_ipos(customized_rxt.getText().toString());
                rxt_Object.setRxt_img(img1);
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
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                RXTMarketingTActivity.this.finish();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                            RXTMarketingTActivity.this.finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
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
                            String metadata = CommonFunctions.setMetadataAtImages(preferences.getString(CommonString.KEY_STORE_NAME, null), store_cd, "Rxt Image", username);
                            CommonFunctions.addMetadataAndTimeStampToImage(context, _path, metadata, visit_date);
                            rxt_img.setImageResource(R.mipmap.camera_green);
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
            case R.id.rxt_img:
                _pathforcheck = store_cd + "_RXTING_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                CommonFunctions.startAnncaCameraActivity(context, _path, null, false);
                break;
            case R.id.fab:
                if (validate()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(getString(R.string.parinaam)).setMessage(getString(R.string.alert_save)).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.open();
                            db.insertRXTData(store_cd, visit_date, rxt_Object);
                            Snackbar.make(fab, "Data has been saved", Snackbar.LENGTH_SHORT).show();
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                            RXTMarketingTActivity.this.finish();
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
                    Snackbar.make(fab, Error_Message, Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void addontextchangeonEdittext() {
        rxtnoof_display.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("0")) {
                    if (updateflag){
                        img1="";
                    }
                    rxt_machine_onEDT.setText("0");
                    rxt_EDT.setText("0");
                    engegment_rxt.setText("0");
                    customized_rxt.setText("0");

                    rxt_img.setEnabled(false);
                    rxt_EDT.setEnabled(false);
                    engegment_rxt.setEnabled(false);
                    customized_rxt.setEnabled(false);
                    rxt_machine_onEDT.setEnabled(false);
                    rxt_img.setImageResource(R.mipmap.camera_grey);
                } else {
                    if (updateflag){
                        img1="";
                    }
                    rxt_machine_onEDT.setText("");
                    rxt_EDT.setText("");
                    engegment_rxt.setText("");
                    customized_rxt.setText("");
                    rxt_machine_onEDT.setEnabled(true);
                    engegment_rxt.setEnabled(true);
                    rxt_EDT.setEnabled(true);
                    rxt_img.setEnabled(true);
                    customized_rxt.setEnabled(true);
                    rxt_img.setImageResource(R.mipmap.camera_orange);

                }
            }
        });

        rxt_machine_onEDT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("0")) {
                    if (updateflag){
                        img1="";
                    }
                    rxt_EDT.setText("0");
                    engegment_rxt.setText("0");
                    customized_rxt.setText("0");
                    rxt_img.setEnabled(false);
                    rxt_EDT.setEnabled(false);
                    engegment_rxt.setEnabled(false);
                    customized_rxt.setEnabled(false);
                    rxt_img.setImageResource(R.mipmap.camera_grey);
                } else {
                    if (updateflag){
                        img1="";
                    }
                    rxt_EDT.setText("");
                    engegment_rxt.setText("");
                    customized_rxt.setText("");
                    engegment_rxt.setEnabled(true);
                    rxt_EDT.setEnabled(true);
                    rxt_img.setEnabled(true);
                    customized_rxt.setEnabled(true);
                    rxt_img.setImageResource(R.mipmap.camera_orange);

                }
            }
        });


        rxt_EDT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("0")) {
                    if (updateflag){
                        img1="";
                    }
                    customized_rxt.setText("0");
                    rxt_img.setEnabled(false);
                    customized_rxt.setEnabled(false);
                    rxt_img.setImageResource(R.mipmap.camera_grey);
                } else {
                    if (updateflag){
                        img1="";
                    }
                    customized_rxt.setText("");
                    rxt_img.setEnabled(true);
                    customized_rxt.setEnabled(true);
                    rxt_img.setImageResource(R.mipmap.camera_orange);
                }
            }
        });
    }

    private void validateinserted_data() {
        db.open();
        rxt_Object = db.getrxt_insertedData(store_cd);
        try {
            if (rxt_Object.getKey_id() != null) {
                updateflag=true;
                rxtnoof_display.setText(rxt_Object.getNoof_display());
                rxt_machine_onEDT.setText(rxt_Object.getMachine_on());
                engegment_rxt.setText(rxt_Object.getEngegment());
                rxt_EDT.setText(rxt_Object.getRxt());
                customized_rxt.setText(rxt_Object.getCustomised_ipos());
                if (!rxt_Object.getRxt().equals("0")) {
                    img1 = rxt_Object.getRxt_img();
                    rxt_img.setImageResource(R.mipmap.camera_green);
                } else {
                    rxt_img.setImageResource(R.mipmap.camera_grey);
                }

                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.edit_txt));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }


}
