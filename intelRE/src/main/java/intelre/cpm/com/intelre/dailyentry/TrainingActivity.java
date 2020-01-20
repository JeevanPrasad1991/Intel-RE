package intelre.cpm.com.intelre.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.AlertandMessages;
import intelre.cpm.com.intelre.constant.CommonFunctions;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gettersetter.TrainingGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.StoreCategoryMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.WindowChecklist;
import intelre.cpm.com.intelre.gsonGetterSetter.WindowMaster;
import intelre.cpm.com.intelre.multiselectionspin.MultiSpinnerSearch;
import intelre.cpm.com.intelre.multiselectionspin.SpinnerListener;

public class TrainingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    String _pathforcheck, _path, str, image1 = "";
    String visit_date, userId, user_type, store_cd;
    Spinner sp_trainingtype, sp_topic;
    MultiSpinnerSearch sp_rsp;
    ImageView img_photoMar;
    RecyclerView marketinteligence_list;
    private SharedPreferences preferences;
    Toolbar toolbar;
    INTEL_RE_DB db;
    private Context context;
    String date;
    ArrayList<StoreCategoryMaster> rsplist = new ArrayList<>();
    ArrayList<WindowMaster> trainingtypelist = new ArrayList<WindowMaster>();
    ArrayList<WindowChecklist> trainingtopiclist = new ArrayList<>();
    private ArrayAdapter<CharSequence> training_adapter;
    private ArrayAdapter<CharSequence> trainingtopic_adapter;
    String training_name, training_nameid, training_topic, training_topicid;
    ArrayList<TrainingGetterSetter> inserteslistData = new ArrayList<>();
    ArrayList<StoreCategoryMaster> insertedRspDetailList = new ArrayList<>();
    MyAdapter adapter;
    boolean sampleaddflag = false;
    FloatingActionButton fab, btn_add;
    ArrayList<StoreCategoryMaster> selectedRSPList = new ArrayList<>();
    private static final String TAG = TrainingActivity.class.getSimpleName();
    ArrayList<TrainingGetterSetter> insertedRSPTempList = new ArrayList<>();
    RspDataAdapter rspDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        context = this;
        db = new INTEL_RE_DB(context);
        db.open();
        declaration();
        setDataToListView();
    }

    void declaration() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp_rsp = findViewById(R.id.sp_rsp);
        sp_trainingtype = (Spinner) findViewById(R.id.sp_trainingtype);
        sp_topic = (Spinner) findViewById(R.id.sp_topic);
        img_photoMar = (ImageView) findViewById(R.id.img_photoMar);
        btn_add = findViewById(R.id.btn_add);
        marketinteligence_list = (RecyclerView) findViewById(R.id.marketinteligence_list);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        str = CommonString.FILE_PATH;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        date = preferences.getString(CommonString.KEY_DATE, null);
        if (!CommonFunctions.isTablet(context)){
            toolbar.setTitleTextAppearance(context,R.style.changestext_sizefor_mobile);
        }

        setTitle(getString(R.string.title_activity_training) + " - " + date);
        db.open();
        insertedRspDetailList = db.getRspDetailinsertData(store_cd);
        rsplist = db.getRspDetailData(store_cd);
        try {
            if (insertedRspDetailList.size() > 0) {
                for (int i = 0; i < insertedRspDetailList.size(); i++) {
                    if (insertedRspDetailList.get(i).getFlag().equalsIgnoreCase(CommonString.KEY_ADD)) {
                        rsplist.add(insertedRspDetailList.get(i));
                    } else if (insertedRspDetailList.get(i).getFlag().equalsIgnoreCase(CommonString.KEY_EDIT)) {
                        int pos = getrspPosition(insertedRspDetailList.get(i).getRspId());
                        if (pos != -1)
                            rsplist.set(pos, insertedRspDetailList.get(i));
                    } else if (insertedRspDetailList.get(i).getFlag().equalsIgnoreCase(CommonString.KEY_DELETE)) {
                        int pos = getrspPosition(insertedRspDetailList.get(i).getRspId());
                        if (pos != -1)
                            rsplist.remove(pos);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }

        trainingtypelist = db.getTrainingTypeData();
        // sp_rsp.setOnItemSelectedListener(this);
        sp_trainingtype.setOnItemSelectedListener(this);
        sp_topic.setOnItemSelectedListener(this);
        img_photoMar.setOnClickListener(this);
        fab.setOnClickListener(this);
        btn_add.setOnClickListener(this);

        /// RSP INTERFACE DATA REFLACTING....................
        rspInterfaceData();

        training_adapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_custom_item);
        training_adapter.add("-Select TR Type-");
        for (int i = 0; i < trainingtypelist.size(); i++) {
            training_adapter.add(trainingtypelist.get(i).getTrainingType());

        }
        sp_trainingtype.setAdapter(training_adapter);
        training_adapter.setDropDownViewResource(R.layout.spinner_custom_item);


        trainingtopic_adapter = new ArrayAdapter<CharSequence>(context, R.layout.spinner_custom_item);
        trainingtopic_adapter.add("-Select Topic-");
        sp_topic.setAdapter(trainingtopic_adapter);
        trainingtopic_adapter.setDropDownViewResource(R.layout.spinner_custom_item);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_trainingtype:
                trainingtopiclist.clear();
                trainingtopic_adapter.clear();
                trainingtopic_adapter.add("-Select Topic-");
                if (position != 0) {
                    training_name = trainingtypelist.get(position - 1).getTrainingType();
                    training_nameid = trainingtypelist.get(position - 1).getTrainingTypeId().toString();
                    trainingtopiclist = db.getTrainingTopicData(training_nameid);
                    for (int i = 0; i < trainingtopiclist.size(); i++) {
                        trainingtopic_adapter.add(trainingtopiclist.get(i).getTopic());

                    }
                    sp_topic.setAdapter(trainingtopic_adapter);
                    trainingtopic_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    break;
                } else {
                    training_nameid = "0";
                    training_name = "";
                }
            case R.id.sp_topic:
                if (position != 0) {
                    training_topic = trainingtopiclist.get(position - 1).getTopic();
                    training_topicid = trainingtopiclist.get(position - 1).getTopicId().toString();
                    break;
                } else {
                    training_topicid = "0";
                    training_topic = "";
                }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private int getrspPosition(int rsp_id) {
        int pos = -1;
        try {
            if (rsplist.size() > 0) {
                for (int i = 0; i < rsplist.size(); i++) {
                    if (rsplist.get(i).getRspId() == rsp_id) {
                        pos = i;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return pos;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_photoMar:
                _pathforcheck = store_cd + "_" + training_nameid + "_TRAININGIMG_" + visit_date.replace("/", "") + getCurrentTime().replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                CommonFunctions.startAnncaCameraActivity(context, _path, null, false);
                break;


            case R.id.btn_add:
                //validationDuplication
                if (validation()) {
                    if (validationDuplication()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(getString(R.string.parinaam)).setMessage("Are you sure you want to add ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                db.open();
                                                if (selectedRSPList.size() > 0) {
                                                    TrainingGetterSetter trainingGetterSetter = null;
                                                    for (int i = 0; i < selectedRSPList.size(); i++) {
                                                        trainingGetterSetter = new TrainingGetterSetter();
                                                        trainingGetterSetter.setRspname_cd(selectedRSPList.get(i).getRspId().toString());
                                                        trainingGetterSetter.setRspname(selectedRSPList.get(i).getRspName());
                                                        trainingGetterSetter.setTrainingtype_cd(training_nameid);
                                                        trainingGetterSetter.setTrainingtype(training_name);
                                                        trainingGetterSetter.setTopic_cd(training_topicid);
                                                        trainingGetterSetter.setTopic(training_topic);
                                                        trainingGetterSetter.setPhoto(image1);
                                                        trainingGetterSetter.setUnoque_RSPID(selectedRSPList.get(i).getRsp_uniqueID());
                                                        inserteslistData.add(trainingGetterSetter);
                                                    }
                                                    //remove duplicate value frpm list
                                                    Set<TrainingGetterSetter> set = new HashSet<>();
                                                    set.addAll(inserteslistData);
                                                    ArrayList<TrainingGetterSetter> removeDuplicateJcp = new ArrayList<>();
                                                    removeDuplicateJcp.clear();
                                                    removeDuplicateJcp.addAll(set);
                                                    insertedRSPTempList.clear();
                                                    insertedRSPTempList.addAll(removeDuplicateJcp);

                                                    //  adapter = new MyAdapter(TrainingActivity.this, inserteslistData);
                                                    adapter = new MyAdapter(context, insertedRSPTempList);
                                                    marketinteligence_list.setAdapter(adapter);
                                                    marketinteligence_list.setLayoutManager(new LinearLayoutManager(context));
                                                    adapter.notifyDataSetChanged();
                                                    sp_rsp.setSelection(0);
                                                    sp_trainingtype.setSelection(0);
                                                    sp_topic.setSelection(0);

                                                    image1 = "";
                                                    sampleaddflag = true;
                                                    img_photoMar.setImageResource(R.mipmap.camera_orange);
                                                    Snackbar.make(btn_add, "Data has been added", Snackbar.LENGTH_SHORT).show();
                                                }

                                                unckeckedRspList();
                                                rspInterfaceData();
                                                selectedRSPList.clear();

                                            }
                                        }).setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        Snackbar.make(btn_add, "Already added .", Snackbar.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.fab:
                if (inserteslistData.size() > 0) {
                    if (sampleaddflag) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context).setTitle(getString(R.string.parinaam)).setMessage(getString(R.string.alert_save))
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                db.open();
                                                db.insertTrainingData(store_cd, user_type, visit_date, inserteslistData);
                                                TrainingActivity.this.finish();
                                                sampleaddflag = false;
                                                Snackbar.make(btn_add, "Data has been saved", Snackbar.LENGTH_SHORT).show();
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
                    } else {
                        Snackbar.make(btn_add, "Please add data.", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(btn_add, "Please add data.", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
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
                        if (new File(str + _pathforcheck).exists()) {
                            String metadata = CommonFunctions.setMetadataAtImages(preferences.getString(CommonString.KEY_STORE_NAME, null), store_cd, "Training Image", userId);
                            CommonFunctions.addMetadataAndTimeStampToImage(context, _path, metadata, visit_date);
                            img_photoMar.setImageResource(R.mipmap.camera_green);
                            image1 = _pathforcheck;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }
                    _pathforcheck = "";
                }
                break;
        }

    }

    public void showMessage(String message) {
        Snackbar.make(btn_add, message, Snackbar.LENGTH_SHORT).show();
    }

    public boolean validation() {
        boolean value = true;
        try {
            if (sp_trainingtype.getSelectedItem().toString().equalsIgnoreCase("-Select TR Type-")) {
                value = false;
                showMessage("Please Select Training Type Dropdown");
            }
            if (value && sp_topic.getSelectedItem().toString().equalsIgnoreCase("-Select Topic-")) {
                value = false;
                showMessage("Please Select Training Topic Dropdown");
            }

            if (value && selectedRSPList.size() == 0) {
                value = false;
                showMessage("Please Select RSP");
            }
            if (value && image1.equals("")) {
                value = false;
                showMessage("Please Capture Photo");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }

        return value;
    }

    public void setDataToListView() {
        try {
            inserteslistData = db.getinsertedTrainingData(store_cd, visit_date);
            //remove duplicate value frpm list
            Set<TrainingGetterSetter> set = new HashSet<>();
            set.addAll(inserteslistData);
            ArrayList<TrainingGetterSetter> removeDuplicateJcp = new ArrayList<>();
            removeDuplicateJcp.clear();
            removeDuplicateJcp.addAll(set);
            insertedRSPTempList.clear();
            insertedRSPTempList.addAll(removeDuplicateJcp);

            /////////////////////////////////////////////
            if (insertedRSPTempList.size() > 0) {
                Collections.reverse(insertedRSPTempList);
                adapter = new MyAdapter(context, insertedRSPTempList);
                marketinteligence_list.setAdapter(adapter);
                marketinteligence_list.setLayoutManager(new LinearLayoutManager(context));
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching", e.toString());
            Crashlytics.logException(e);
        }
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        Context context;
        ArrayList<TrainingGetterSetter> insertedlist_Data;

        MyAdapter(Context context, ArrayList<TrainingGetterSetter> insertedlist_Data) {
            inflator = LayoutInflater.from(context);
            this.context = context;
            this.insertedlist_Data = insertedlist_Data;

        }

        @Override
        public int getItemCount() {
            return insertedlist_Data.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflator.inflate(R.layout.secondary_training_adapter, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (insertedlist_Data.get(position).getKey_id() == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(getString(R.string.parinaam)).setMessage("Are you sure you want to Delete ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                if (inserteslistData.size() > 0) {
                                                    for (int i = 0; i < inserteslistData.size(); i++) {
                                                        if (inserteslistData.get(i).getTopic_cd().equals(insertedlist_Data.get(position).getTopic_cd())) {
                                                            inserteslistData.remove(i--);
                                                        }
                                                    }
                                                }
                                                insertedlist_Data.remove(position);

                                                if (insertedlist_Data.size() > 0) {
                                                    MyAdapter adapter = new MyAdapter(context, insertedlist_Data);
                                                    marketinteligence_list.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                }
                                                notifyDataSetChanged();
                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(getString(R.string.parinaam)).setMessage("Are you sure you want to Delete ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                if (inserteslistData.size() > 0) {
                                                    for (int i = 0; i < inserteslistData.size(); i++) {
                                                        if (inserteslistData.get(i).getTopic_cd().equals(insertedlist_Data.get(position).getTopic_cd())) {
                                                            inserteslistData.remove(i--);
                                                        }
                                                    }
                                                }
                                                db.remove_trainingByTopic_cd(insertedlist_Data.get(position).getTopic_cd());
                                                insertedlist_Data.remove(position);
                                                if (insertedlist_Data.size() > 0) {
                                                    MyAdapter adapter = new MyAdapter(TrainingActivity.this, insertedlist_Data);
                                                    marketinteligence_list.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                }
                                                notifyDataSetChanged();
                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                }
            });

            holder.txt_rsplist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setTitle("RSP List");
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_rsplist);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();
                    wlp.gravity = Gravity.CENTER;
                    wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                    window.setAttributes(wlp);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ArrayList<TrainingGetterSetter> rsptempList = new ArrayList<>();
                    if (inserteslistData.size() > 0) {
                        rsptempList.clear();
                        for (int i = 0; i < inserteslistData.size(); i++) {
                            if (inserteslistData.get(i).getTopic_cd().equals(insertedlist_Data.get(position).getTopic_cd())) {
                                rsptempList.add(inserteslistData.get(i));
                            }
                        }
                    }
                    RecyclerView recyclerRSP = dialog.findViewById(R.id.dialog_rsplist);
                    rspDataAdapter = new RspDataAdapter(context, rsptempList);
                    recyclerRSP.setAdapter(rspDataAdapter);
                    recyclerRSP.setLayoutManager(new LinearLayoutManager((Activity) context));
                    Button ok = dialog.findViewById(R.id.dialog_ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });

            holder.txt_comp.setText(insertedlist_Data.get(position).getTrainingtype());
            holder.txt_cate.setText(insertedlist_Data.get(position).getTopic());

            holder.txt_comp.setId(position);
            holder.txt_cate.setId(position);
            holder.remove.setId(position);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_comp, txt_cate;
            ImageView remove, txt_rsplist;

            public MyViewHolder(View convertView) {
                super(convertView);
                txt_comp = convertView.findViewById(R.id.txt_comp);
                txt_cate = convertView.findViewById(R.id.txt_cate);
                remove = (ImageView) convertView.findViewById(R.id.imgDelRow);
                txt_rsplist = convertView.findViewById(R.id.txt_rsplist);
            }
        }
    }

    public boolean validationDuplication() {
        boolean value = true;
        try {
            if (inserteslistData.size() > 0) {
                for (int i = 0; i < inserteslistData.size(); i++) {
                    if (inserteslistData.get(i).getTopic_cd().equals(training_topicid)) {
                        value = false;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }

        // &&inserteslistData.get(i).getTopic_cd().equals(training_topicid)
        return value;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // NavUtils.navigateUpFromSameTask(this);
            new AlertandMessages().backpressedAlert((Activity) context);
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertandMessages().backpressedAlert((Activity) context);
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    ////for multiselect spin.................................

    private void unckeckedRspList() {
        if (rsplist.size() > 0) {
            for (int i = 0; i < rsplist.size(); i++) {
                rsplist.get(i).setSelected(false);
            }
        }
    }

    private void rspInterfaceData() {
        sp_rsp.setItems(rsplist, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(ArrayList<StoreCategoryMaster> items) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i(TAG, i + " : " + items.get(i).getRspName() + " : " + items.get(i).isSelected());
                        selectedRSPList.add(items.get(i));
                    }
                }
            }
        });
    }


    private class RspDataAdapter extends RecyclerView.Adapter<RspDataAdapter.RspHolder> {
        Context context;
        ArrayList<TrainingGetterSetter> rspList;
        LayoutInflater inflater;

        RspDataAdapter(Context context, ArrayList<TrainingGetterSetter> rspList) {
            inflater = LayoutInflater.from(context);
            this.context = context;
            this.rspList = rspList;
        }

        @Override
        public RspHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.secondary_adapter_rsplist, parent, false);
            RspHolder holder = new RspHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RspHolder holder, int position) {
            holder.rsp_txt.setText(rspList.get(position).getRspname());
            holder.rsp_txt.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            holder.rsp_txt.setTextColor(Color.BLACK);
        }

        @Override
        public int getItemCount() {
            return rspList.size();
        }

        class RspHolder extends RecyclerView.ViewHolder {
            TextView rsp_txt;

            public RspHolder(View itemView) {
                super(itemView);
                rsp_txt = itemView.findViewById(R.id.rsp_txt);
            }
        }
    }
}
