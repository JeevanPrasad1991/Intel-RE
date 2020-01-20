package intelre.cpm.com.intelre.reports;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.AlertandMessages;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.dailyentry.StoreEntryActivity;
import intelre.cpm.com.intelre.dailyentry.StoreProfileActivity;
import intelre.cpm.com.intelre.gettersetter.TrainingGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.BraveIndexScore;
import intelre.cpm.com.intelre.gsonGetterSetter.StoreCategoryMaster;

public class BraveindexScoreActivity extends AppCompatActivity {
    ArrayList<BraveIndexScore> braveList = new ArrayList<>();
    String visit_date, userId, store_cd;
    INTEL_RE_DB db;
    private Context context;
    RecyclerView braveindex_recycl;
    FloatingActionButton btn_next;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braveindex_score);

        context = this;
        db = new INTEL_RE_DB(context);
        db.open();
        declaration();

    }

    void declaration() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        visit_date = preferences.getString(CommonString.KEY_DATE, "");
        userId = preferences.getString(CommonString.KEY_USERNAME, "");
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, "");
        visit_date = preferences.getString(CommonString.KEY_DATE, "");

        braveindex_recycl = (RecyclerView) findViewById(R.id.braveindex_recycl);
        btn_next = (FloatingActionButton) findViewById(R.id.btn_next);

        setTitle("Brave Score ( " + preferences.getString(CommonString.KEY_STORE_NAME, "") + " ) - " + visit_date);
        db.open();
        braveList = db.getbraveindex_report(store_cd);
        if (braveList.size() > 0) {
            braveindex_recycl.setAdapter(new MyAdapter(context, braveList));
            braveindex_recycl.setLayoutManager(new LinearLayoutManager(context));
        } else {
            startActivity(new Intent(context, StoreEntryActivity.class));
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            BraveindexScoreActivity.this.finish();
        }


        braveindex_recycl.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    btn_next.hide();
                else if (dy < 0)
                    btn_next.show();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, StoreEntryActivity.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                BraveindexScoreActivity.this.finish();
            }
        });
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        Context context;
        ArrayList<BraveIndexScore> bravelist;

        MyAdapter(Context context, ArrayList<BraveIndexScore> insertedlist_Data) {
            inflator = LayoutInflater.from(context);
            this.context = context;
            this.bravelist = insertedlist_Data;

        }

        @Override
        public int getItemCount() {
            return bravelist.size();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflator.inflate(R.layout.secondary_brave_index, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyAdapter.MyViewHolder holder, final int position) {
            final BraveIndexScore current = bravelist.get(position);

            holder.txt_qtrs.setText(current.getTimePeriod().trim());
            holder.txt_qtrs.setId(position);

            holder.txt_shopermkt.setText("" + current.getSMScore().toString().trim());
            holder.txt_shopermkt.setId(position);

            holder.txt_training.setText("" + current.getTNAScore().toString().trim());
            holder.txt_training.setId(position);

            holder.txt_visual_merchanding.setText("" + current.getVisibilityScore().toString().trim());
            holder.txt_visual_merchanding.setId(position);


            if (current.getBraveIndex() < 5.0) {
                holder.txt_brave_score.setBackgroundColor(getResources().getColor(R.color.red));
                holder.txt_brave_score.setTextColor(getResources().getColor(R.color.white));
                holder.txt_brave_score.setText("" + current.getBraveIndex().toString().trim());
                holder.txt_brave_score.setId(position);
            } else if (current.getBraveIndex() > 5.0 && current.getBraveIndex() <= 8.0) {
                holder.txt_brave_score.setBackgroundColor(getResources().getColor(R.color.amber));
                holder.txt_brave_score.setTextColor(getResources().getColor(R.color.black));
                holder.txt_brave_score.setText("" + current.getBraveIndex().toString().trim());
                holder.txt_brave_score.setId(position);
            } else if (current.getBraveIndex() > 8.0) {
                holder.txt_brave_score.setBackgroundColor(getResources().getColor(R.color.green));
                holder.txt_brave_score.setTextColor(getResources().getColor(R.color.white));
                holder.txt_brave_score.setText("" + current.getBraveIndex().toString().trim());
                holder.txt_brave_score.setId(position);
            } else {
                holder.txt_brave_score.setText("" + current.getBraveIndex().toString().trim());
                holder.txt_brave_score.setId(position);
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_qtrs, txt_shopermkt, txt_training, txt_visual_merchanding, txt_brave_score;

            public MyViewHolder(View convertView) {
                super(convertView);
                txt_qtrs = (TextView) convertView.findViewById(R.id.txt_qtrs);
                txt_shopermkt = (TextView) convertView.findViewById(R.id.txt_shopermkt);
                txt_training = (TextView) convertView.findViewById(R.id.txt_training);
                txt_visual_merchanding = (TextView) convertView.findViewById(R.id.txt_visual_merchanding);
                txt_brave_score = (TextView) convertView.findViewById(R.id.txt_brave_score);
            }
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
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            BraveindexScoreActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        BraveindexScoreActivity.this.finish();
    }

}
