package intelre.cpm.com.intelre.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.AlertandMessages;
import intelre.cpm.com.intelre.constant.CommonFunctions;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gsonGetterSetter.BrandMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.gsonGetterSetter.StoreCategoryMaster;

public class RspDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String visit_date, userId, user_type;
    String store_cd;
    private Context context;
    private SharedPreferences preferences;
    EditText edit_espname, edit_emailid, edit_phoneno;
    Spinner sp_brand, sp_registered;
    ArrayList<BrandMaster> brandMasterArrayList = new ArrayList<BrandMaster>();
    INTEL_RE_DB db;
    private ArrayAdapter<CharSequence> brand_adapter;
    String brand_name, brand_id;
    String[] irep_registered = {"Select", "YES", "NO"};
    StoreCategoryMaster storeCategoryMaster;
    String  spinner_irepregisterd;
    String str_rspname, str_emailid, str_phoneno;
    private String date;
    private ArrayList<JourneyPlan> storelist = new ArrayList<>();
    FloatingActionButton fab;
    String flag;
    TextView store_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsp_detail);
        context = this;
        declaration();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context).setMessage(getString(R.string.alert_save)).setTitle(getString(R.string.parinaam)).setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            rspDetailsSaveData();

                                        }
                                    })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert1 = builder1.create();
                    alert1.show();


                }

            }
        });
    }

    void declaration() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edit_espname = (EditText) findViewById(R.id.edit_espname);
        edit_emailid = (EditText) findViewById(R.id.edit_emailid);
        edit_phoneno = (EditText) findViewById(R.id.edit_phoneno);
        sp_brand = (Spinner) findViewById(R.id.sp_brand);
        sp_registered = (Spinner) findViewById(R.id.sp_registered);
        store_name = (TextView) findViewById(R.id.store_name);
        flag = getIntent().getStringExtra(CommonString.KEY_FLAG);
        storeCategoryMaster = (StoreCategoryMaster) getIntent().getSerializableExtra(CommonString.KEY_OBJECT);

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        date = preferences.getString(CommonString.KEY_DATE, null);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        db = new INTEL_RE_DB(context);
        db.open();
        brandMasterArrayList = db.getBrandMasterData();

        store_name.setText(db.getSpecificStoreData(store_cd).get(0).getStoreName());
        if (!CommonFunctions.isTablet(context)){
            toolbar.setTitleTextAppearance(context,R.style.changestext_sizefor_mobile);
        }
        setTitle(getString(R.string.title_activity_rsp_detail) + " - " + date);

        ArrayAdapter aa3 = new ArrayAdapter(context, android.R.layout.simple_spinner_item, irep_registered);
        aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_registered.setAdapter(aa3);


        brand_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        brand_adapter.add("Select Brand");
        for (int i = 0; i < brandMasterArrayList.size(); i++) {
            brand_adapter.add(brandMasterArrayList.get(i).getBrand());
        }
        sp_brand.setAdapter(brand_adapter);
        brand_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_brand.setOnItemSelectedListener(this);
        sp_registered.setOnItemSelectedListener(this);
        if (flag.equalsIgnoreCase(CommonString.KEY_EDIT)) {
            edit_espname.setEnabled(false);
            setdata();
        } else if (flag.equalsIgnoreCase(CommonString.KEY_ADD)) {
            if (storeCategoryMaster != null) {
                setdata();
            } else {
                storeCategoryMaster = new StoreCategoryMaster();
            }

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_brand:
                if (position != 0) {
                    brand_name = brandMasterArrayList.get(position - 1).getBrand();
                    brand_id = brandMasterArrayList.get(position - 1).getBrandId().toString();
                    break;
                } else {
                    brand_id = "0";
                    brand_name = "";
                }
            case R.id.sp_registered:
                if (position != 0) {
                    spinner_irepregisterd = String.valueOf(parent.getSelectedItemPosition());
                    if (parent.getSelectedItemPosition() == 1) {
                        storeCategoryMaster.setIREPStatus(true);
                    } else {
                        storeCategoryMaster.setIREPStatus(false);
                    }
                } else {
                    storeCategoryMaster.setIREPStatus(false);
                }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void rspDetailsSaveData() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system
        if (flag.equalsIgnoreCase(CommonString.KEY_ADD)) {
            storeCategoryMaster.setRspId(Integer.valueOf("0"));
            storeCategoryMaster.setRsp_uniqueID(userId + "-" + dateTime);
        }
        storeCategoryMaster.setFlag(flag);
        storeCategoryMaster.setRspName(str_rspname);
        storeCategoryMaster.setEmail(str_emailid);
        storeCategoryMaster.setMobile(str_phoneno);
        storeCategoryMaster.setBrandId(Integer.valueOf(brand_id));
        long id;

        id = db.InsertRspDetailData(storeCategoryMaster, store_cd, date);
        if (id > 0) {
            Snackbar.make(fab, "Data saved successfully", Snackbar.LENGTH_SHORT).show();
            finish();
        } else {
            Snackbar.make(fab, "Data not saved", Snackbar.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        db.open();
        storelist = db.getStoreData(date);
    }

    public boolean validation() {
        str_rspname = edit_espname.getText().toString().replaceAll("[(!@#$%^&*?)\"]", " ");
        str_emailid = edit_emailid.getText().toString().replaceAll("[(!$%^&*?)\"]", " ");
        str_phoneno = edit_phoneno.getText().toString().replaceAll("[(!@#$%^&*?)\"]", " ");
        boolean value = true;
        try {
            if (edit_espname.getText().toString().isEmpty()) {
                value = false;
                showMessage("Please Enter RSP Name");
            } else if (!AlertandMessages.isValid(str_emailid)) {
                value = false;
                showMessage("Please Enter valid Email ID");
            } else if (edit_phoneno.getText().toString().length() != 10) {
                value = false;
                showMessage("Please fill 10 digit store profile contact number");
            } else if (sp_brand.getSelectedItemPosition() == 0) {
                value = false;
                showMessage("Please Select Brand");
            } else if (sp_registered.getSelectedItemPosition() == 0) {
                value = false;
                showMessage("Please Select IRP Registered");
            } else {
                value = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return value;

    }

    public void showMessage(String message) {
        Snackbar.make(fab, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // NavUtils.navigateUpFromSameTask(this);
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        }

        return super.onOptionsItemSelected(item);
    }

    private void setdata() {
        edit_espname.setText(storeCategoryMaster.getRspName());
        edit_emailid.setText(storeCategoryMaster.getEmail());
        edit_phoneno.setText(storeCategoryMaster.getMobile());
        for (int i = 0; i < brandMasterArrayList.size(); i++) {
            if (brandMasterArrayList.get(i).getBrandId().equals(storeCategoryMaster.getBrandId())) {
                sp_brand.setSelection(i + 1);
                break;
            }
        }

        if (storeCategoryMaster != null) {
            if (storeCategoryMaster.getIREPStatus()) {
                sp_registered.setSelection(1);
            } else {
                sp_registered.setSelection(2);
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertandMessages().backpressedAlert((Activity) context);
    }
}
