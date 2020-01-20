package intelre.cpm.com.intelre.dailyentry;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.AlertandMessages;
import intelre.cpm.com.intelre.constant.CommonFunctions;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlanSearch;
import intelre.cpm.com.intelre.retrofit.PostApi;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JourneyPlanSearchActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView date;
    private Button search;
    private ImageView img_dob;
    int mYear, mMonth, mDay;
    DatePickerDialog dpd;
    Calendar c;
    INTEL_RE_DB db;
    private Toolbar toolbar;
    private SharedPreferences preferences = null;
    String userId, visit_date;
    Context context;
    private RecyclerView drawer_layout_recycle_store;
    String curent_date = "";
    ProgressDialog loading;
    private Retrofit adapter;
    boolean uploadflag = false;
    ValueAdapter adapter1;
    ArrayList<JourneyPlanSearch> storelist = new ArrayList<JourneyPlanSearch>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_plan_search);
        declaration();
    }


    private void declaration() {
        date = (TextView) findViewById(R.id.dob);
        search = (Button) findViewById(R.id.search);
        img_dob = (ImageView) findViewById(R.id.img_dob);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer_layout_recycle_store = (RecyclerView) findViewById(R.id.drawer_layout_recycle_store);
        context = this;
        db = new INTEL_RE_DB(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, "");
        if (!CommonFunctions.isTablet(context)){
            toolbar.setTitleTextAppearance(context,R.style.changestext_sizefor_mobile);
        }
        getSupportActionBar().setTitle("Lookup Route Plan");
        search.setOnClickListener(this);
        img_dob.setOnClickListener(this);
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
                        String month= String.valueOf((monthOfYear + 1));
                        String day= String.valueOf(dayOfMonth);

                        if (month.toString().length()== 1) {
                            month = "0" + month;
                        }
                        if (day.toString().length()==1){
                            day="0"+day;
                        }

                        curent_date = month + "/" + day + "/" + year;
                        date.setText(curent_date);
                    }

                }, mYear, mMonth, mDay);
                dpd.show();
                break;

            case R.id.search:
                if (validation()) {
                    //  UploadDataTask();
                    uploadSearchList();
                }
                break;

        }

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

    public boolean validation() {

        boolean value = true;
        if (curent_date.isEmpty()) {
            value = false;
            showMessage("Please Select Date");
        } else {

            value = true;
        }
        return value;
    }

    public void showMessage(String message) {

        Snackbar.make(search, message, Snackbar.LENGTH_SHORT).show();

    }

    private void uploadSearchList() {
        try {
            loading = ProgressDialog.show(JourneyPlanSearchActivity.this, "Processing", "Please wait...", false, false);
            uploadflag = false;

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();
            JSONObject obj = new JSONObject();
            obj = new JSONObject();
            obj.put("Downloadtype", "Journey_Plan_Search");
            obj.put("Username", userId + ":" + curent_date);
            String jsonString = obj.toString();

            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
            adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient).
                    addConverterFactory(GsonConverterFactory.create()).build();
            PostApi api = adapter.create(PostApi.class);

            Call<String> call = api.getDownloadAll(jsonData);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String responseBody = response.body().toString();
                    String data = null;
                   if (responseBody != null && response.isSuccessful()) {
                        try {
                            if (!responseBody.contains("No Data")) {
                                JSONObject jsnobject = new JSONObject(responseBody);
                                JSONArray jsonArray = jsnobject.getJSONArray("Journey_Plan_Search");
                                storelist.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JourneyPlanSearch jps = new JourneyPlanSearch();
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    jps.setStoreId(Integer.parseInt(jsonObject2.getString("Store_Id")));
                                    jps.setVisitDate((jsonObject2.getString("Visit_Date")));
                                    jps.setStoreName((jsonObject2.getString("Store_Name")));
                                    jps.setAddress1((jsonObject2.getString("Address1")));
                                    jps.setAddress2((jsonObject2.getString("Address2")));
                                    jps.setLandmark((jsonObject2.getString("Landmark")));
                                    jps.setPincode((jsonObject2.getString("Pincode")));
                                    jps.setCity((jsonObject2.getString("City")));
                                    jps.setStoreType((jsonObject2.getString("Store_Type")));
                                    jps.setClassification((jsonObject2.getString("Classification")));
                                    storelist.add(jps);
                                }

                                adapter1 = new ValueAdapter(getApplicationContext(), storelist);
                                drawer_layout_recycle_store.setAdapter(adapter1);
                                drawer_layout_recycle_store.setLayoutManager(new LinearLayoutManager(context));
                                loading.dismiss();
                            } else {
                                throw new java.lang.Exception();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            loading.dismiss();
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_NO_JCP, true);
                        }
                    } else {
                        loading.dismiss();
                        AlertandMessages.showAlertlogin(JourneyPlanSearchActivity.this, "Server Not Responding. Please Try Again");

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if (t instanceof SocketTimeoutException || t instanceof IOException || t instanceof SocketException) {
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE
                                +"("+t.getMessage().toString()+")", true);
                        loading.dismiss();
                    } else {
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                        loading.dismiss();
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            AlertandMessages.showAlertlogin(JourneyPlanSearchActivity.this,
                    e.getLocalizedMessage().toString());
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt, txt_storeAddress;
        ImageView icon;
        RelativeLayout relativelayout;
        ImageView imageview;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.geolistviewxml_storename);
            relativelayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout);
            imageview = (ImageView) itemView.findViewById(R.id.imageView1);
            txt_storeAddress = (TextView) itemView.findViewById(R.id.txt_storeAddress);
        }
    }

    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {

        private LayoutInflater inflator;
        List<JourneyPlanSearch> data = Collections.emptyList();

        public ValueAdapter(Context context, List<JourneyPlanSearch> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.storesearchlist, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
            final JourneyPlanSearch current = data.get(position);
            viewHolder.txt.setText(current.getStoreName());
            viewHolder.txt_storeAddress.setText(current.getAddress1());

           /* if (current.getGeoTag().equalsIgnoreCase("Y")) {
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.imageview.setBackgroundResource(R.mipmap.geotag_grey);
            } else if (current.getGeoTag().equalsIgnoreCase("N")) {
                viewHolder.imageview.setVisibility(View.INVISIBLE);
                viewHolder.imageview.setBackgroundResource(R.mipmap.entry_grey);
            }  else {
                viewHolder.imageview.setVisibility(View.INVISIBLE);
            }*/

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt, txt_storeAddress;
            ImageView icon;
            RelativeLayout relativelayout;
            ImageView imageview;

            public MyViewHolder(View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.geolistviewxml_storename);
                relativelayout = (RelativeLayout) itemView.findViewById(R.id.relativelayout);
                imageview = (ImageView) itemView.findViewById(R.id.imageView1);
                txt_storeAddress = (TextView) itemView.findViewById(R.id.txt_storeAddress);
            }
        }
    }


}
