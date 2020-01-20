package intelre.cpm.com.intelre.dailyentry;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.CommonFunctions;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gettersetter.StoreProfileGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.reports.BraveindexScoreActivity;

public class StoreProfileActivity extends AppCompatActivity implements View.OnClickListener {
    EditText storeProfile_address_1, storeProfile_ownerN,
            storeProfile_contctN, storeProfile_visibtLo, storeProfile_visibtLo2, storeProfile_visibtLo3,
            storeProfile_dimention, storeProfile_dimention2, storeProfile_dimention3;
    TextView storeProfile_dob, storeProfile_doa, storeProfile_City, storeProfile_userN;
    FloatingActionButton btn_save, btn_next;
    ImageView img_dob, img_doa;
    SharedPreferences preferences;
    String visit_date, userId, user_type, store_cd;
    ArrayList<JourneyPlan> specifDATA = new ArrayList<>();
    StoreProfileGetterSetter storePGT;
    int mYear, mMonth, mDay;
    DatePickerDialog dpd;
    Calendar c;
    INTEL_RE_DB db;
    boolean update_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_profile);
        db = new INTEL_RE_DB(this);
        db.open();
        declaration();
        validate();
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StoreProfileActivity.this, BraveindexScoreActivity.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_next.setVisibility(View.GONE);
                uienble();
                btn_save.setImageDrawable(ContextCompat.getDrawable(StoreProfileActivity.this, R.drawable.save_icon));
                if (update_flag) {
                    if (checkCondition()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StoreProfileActivity.this);
                        builder.setTitle("Parinaam").setMessage(R.string.alertsaveData);
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.open();
                                storePGT = new StoreProfileGetterSetter();
                                storePGT.setProfileStoreName(storeProfile_userN.getText().toString());
                                storePGT.setProfileAddress1(storeProfile_address_1.getText().toString().trim().replaceAll("[(!@#$%^&*?)\"]", ""));
                                storePGT.setProfileCity(storeProfile_City.getText().toString().trim());
                                storePGT.setProfileOwner(storeProfile_ownerN.getText().toString().trim().replaceAll("[(!@#$%^&*?)\"]", ""));
                                storePGT.setProfileContact(storeProfile_contctN.getText().toString());
                                storePGT.setProfileDOB(storeProfile_dob.getText().toString());
                                storePGT.setProfileDOA(storeProfile_doa.getText().toString());
                                storePGT.setProfileVisibilityLocation1(storeProfile_visibtLo.getText().toString().trim().replaceAll("[(!@#$%^&*?)\"]", ""));
                                storePGT.setProfileDimension1(storeProfile_dimention.getText().toString());
                                storePGT.setProfileVisibilityLocation2(storeProfile_visibtLo2.getText().toString().replaceAll("[(!@#$%^&*?)\"]", ""));
                                storePGT.setProfileDimension2(storeProfile_dimention2.getText().toString().trim());
                                storePGT.setProfileVisibilityLocation3(storeProfile_visibtLo3.getText().toString().trim().replaceAll("[(!@#$%^&*?)\"]", ""));
                                storePGT.setProfileDimension3(storeProfile_dimention3.getText().toString().trim());

                                db.insertStoreProfileData(userId, store_cd, visit_date, storePGT);
                                btn_next.setVisibility(View.VISIBLE);
                                btn_save.setImageDrawable(ContextCompat.getDrawable(StoreProfileActivity.this, R.drawable.edit_txt));
                                uidisableEnable(true);
                                dialogInterface.dismiss();
                                update_flag = false;
                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();
                    }


                }
                update_flag = true;


            }
        });
    }

    private void validate() {
        update_flag = false;
        db.open();
        storePGT = db.getStoreProfileData(store_cd, visit_date);
        if (storePGT != null && storePGT.getProfileStoreName() != null) {
            uidisableEnable(true);
            btn_save.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.edit_txt));
            storeProfile_userN.setText(storePGT.getProfileStoreName());
            storeProfile_address_1.setText(storePGT.getProfileAddress1());
            storeProfile_ownerN.setText(storePGT.getProfileOwner());
            storeProfile_contctN.setText(storePGT.getProfileContact());
            storeProfile_City.setText(storePGT.getProfileCity());
            storeProfile_doa.setText(storePGT.getProfileDOA());
            storeProfile_dob.setText(storePGT.getProfileDOB());

            storeProfile_visibtLo.setText(storePGT.getProfileVisibilityLocation1());
            storeProfile_dimention.setText(storePGT.getProfileDimension1());
            storeProfile_visibtLo2.setText(storePGT.getProfileVisibilityLocation2());
            storeProfile_visibtLo3.setText(storePGT.getProfileVisibilityLocation3());
            storeProfile_dimention2.setText(storePGT.getProfileDimension2());
            storeProfile_dimention3.setText(storePGT.getProfileDimension3());
        } else {
            uidisableEnable(true);
            specifDATA = db.getSpecificStoreData(store_cd);
            storeProfile_userN.setText(specifDATA.get(0).getStoreName());
            storeProfile_address_1.setText(specifDATA.get(0).getAddress1());
            storeProfile_ownerN.setText(specifDATA.get(0).getContactPerson());
            storeProfile_contctN.setText(specifDATA.get(0).getContactNo());
            storeProfile_City.setText(specifDATA.get(0).getCity());
            storeProfile_doa.setText("");
            storeProfile_dob.setText("");

            storeProfile_visibtLo.setText(specifDATA.get(0).getVisibilityLocation1());
            storeProfile_dimention.setText(specifDATA.get(0).getDimension1());
            storeProfile_visibtLo2.setText(specifDATA.get(0).getVisibilityLocation2());
            storeProfile_visibtLo3.setText(specifDATA.get(0).getVisibilityLocation3());
            storeProfile_dimention2.setText(specifDATA.get(0).getDimension2());
            storeProfile_dimention3.setText(specifDATA.get(0).getDimension3());
        }
    }

    private void declaration() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_save = findViewById(R.id.btn_save);
        btn_next = findViewById(R.id.btn_next);
        storeProfile_userN = findViewById(R.id.storeProfile_userN);
        storeProfile_address_1 = findViewById(R.id.storeProfile_address_1);
        storeProfile_ownerN = findViewById(R.id.storeProfile_ownerN);
        storeProfile_contctN = findViewById(R.id.storeProfile_contctN);
        storeProfile_visibtLo = findViewById(R.id.storeProfile_visibtLo);
        storeProfile_dimention = findViewById(R.id.storeProfile_dimention);
        storeProfile_City = findViewById(R.id.storeProfile_City);
        storeProfile_dob = findViewById(R.id.storeProfile_dob);
        storeProfile_doa = findViewById(R.id.storeProfile_doa);
        storeProfile_visibtLo2 = findViewById(R.id.storeProfile_visibtLo2);
        storeProfile_visibtLo3 = findViewById(R.id.storeProfile_visibtLo3);
        storeProfile_dimention2 = findViewById(R.id.storeProfile_dimention2);
        storeProfile_dimention3 = findViewById(R.id.storeProfile_dimention3);
        img_dob = findViewById(R.id.img_dob);
        img_doa = findViewById(R.id.img_doa);
        img_dob.setOnClickListener(this);
        img_doa.setOnClickListener(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        if (!CommonFunctions.isTablet(this)){
            toolbar.setTitleTextAppearance(this,R.style.changestext_sizefor_mobile);
        }

        setTitle("Store Profile - " + visit_date);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.img_dob:
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                // Launch Date Picker Dialog
                dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dob = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                        storeProfile_dob.setText(dob);
                    }

                }, mYear, mMonth, mDay);
                dpd.show();

                break;
            case R.id.img_doa:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                // Launch Date Picker Dialog
                dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dob = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                        storeProfile_doa.setText(dob);
                    }

                }, mYear, mMonth, mDay);
                // TODO Hide Future Date Here
                //  dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                // TODO Hide Past Date Here
                dpd.show();
                break;
        }
    }

    private boolean checkCondition() {
        boolean status = true;
        if (specifDATA.size() > 0) {
            /*if (!specifDATA.get(0).getAddress1().isEmpty() || !storeProfile_address_1.getText().toString().isEmpty()) {
                status = false;
                meassage(CommonString.stpaddress1);
            } else if (!storeProfile_ownerN.getText().toString().isEmpty()) {
                meassage(CommonString.stpownname);
                status = false;
            } else if (!storeProfile_contctN.getText().toString().isEmpty()) {
                meassage(CommonString.stpcontactno);
                status = false;
            } else*/
            if (!storeProfile_contctN.getText().toString().isEmpty() &&
                    storeProfile_contctN.getText().toString().length() < 10) {
                meassage(CommonString.stpcontactnolenght);
                status = false;
            } /*else if (!storeProfile_dob.getText().toString().isEmpty()) {
                meassage(CommonString.stpdob);
                status = false;
            } else if (!storeProfile_doa.getText().toString().isEmpty()) {
                meassage(CommonString.stpdoa);
                status = false;
            } else if (!storeProfile_visibtLo.getText().toString().isEmpty()) {
                meassage(CommonString.stpvisibility1);
                status = false;
            } else if (!storeProfile_dimention.getText().toString().isEmpty()) {
                meassage(CommonString.stpdimension1);
                status = false;
            } else if (!storeProfile_visibtLo2.getText().toString().isEmpty()) {
                meassage(CommonString.stpvisibility2);
                status = false;
            } else if (!storeProfile_dimention2.getText().toString().isEmpty()) {
                meassage(CommonString.stpdimension2);
                status = false;
            } else if (!storeProfile_visibtLo3.getText().toString().isEmpty()) {
                meassage(CommonString.stpvisibility3);
                status = false;
            } else if (!storeProfile_dimention3.getText().toString().isEmpty()) {
                meassage(CommonString.stpdimension3);
                status = false;
            }*/

        }
        return status;
    }

    private void meassage(String msg) {
        Snackbar.make(btn_save, msg, Snackbar.LENGTH_LONG).show();
    }

    private void uidisableEnable(boolean status) {
        if (status) {
            storeProfile_address_1.setEnabled(false);
            storeProfile_ownerN.setEnabled(false);
            storeProfile_contctN.setEnabled(false);
            storeProfile_visibtLo.setEnabled(false);
            storeProfile_visibtLo2.setEnabled(false);
            storeProfile_visibtLo3.setEnabled(false);
            storeProfile_dimention.setEnabled(false);
            storeProfile_dimention2.setEnabled(false);
            storeProfile_dimention3.setEnabled(false);
            storeProfile_dob.setEnabled(false);
            storeProfile_doa.setEnabled(false);
            img_dob.setEnabled(false);
            img_doa.setEnabled(false);
        } else {
            storeProfile_address_1.setEnabled(true);
            storeProfile_ownerN.setEnabled(true);
            storeProfile_contctN.setEnabled(true);
            storeProfile_visibtLo.setEnabled(true);
            storeProfile_visibtLo2.setEnabled(true);
            storeProfile_visibtLo3.setEnabled(true);
            storeProfile_dimention.setEnabled(true);
            storeProfile_dimention2.setEnabled(true);
            storeProfile_dimention3.setEnabled(true);
            storeProfile_dob.setEnabled(true);
            storeProfile_doa.setEnabled(true);
            img_dob.setEnabled(true);
            img_doa.setEnabled(true);
        }

    }

    private boolean uienble() {
        storeProfile_address_1.setEnabled(true);
        storeProfile_ownerN.setEnabled(true);
        storeProfile_contctN.setEnabled(true);
        storeProfile_visibtLo.setEnabled(true);
        storeProfile_visibtLo2.setEnabled(true);
        storeProfile_visibtLo3.setEnabled(true);
        storeProfile_dimention.setEnabled(true);
        storeProfile_dimention2.setEnabled(true);
        storeProfile_dimention3.setEnabled(true);
        storeProfile_dob.setEnabled(true);
        storeProfile_doa.setEnabled(true);
        img_dob.setEnabled(true);
        img_doa.setEnabled(true);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!update_flag) {
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(StoreProfileActivity.this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // finish();
                            update_flag = false;
                            btn_next.setVisibility(View.VISIBLE);
                            btn_save.setImageDrawable(ContextCompat.getDrawable(StoreProfileActivity.this, R.drawable.edit_txt));
                            uidisableEnable(true);
                            dialog.dismiss();
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
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
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!update_flag) {
                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(StoreProfileActivity.this);
                builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                update_flag = false;
                                btn_next.setVisibility(View.VISIBLE);
                                btn_save.setImageDrawable(ContextCompat.getDrawable(StoreProfileActivity.this, R.drawable.edit_txt));
                                uidisableEnable(true);
                                dialog.dismiss();
                                // finish();
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
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
        }
        return super.onOptionsItemSelected(item);
    }

}
