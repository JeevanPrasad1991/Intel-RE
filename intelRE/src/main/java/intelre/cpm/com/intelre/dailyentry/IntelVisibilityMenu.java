package intelre.cpm.com.intelre.dailyentry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.CommonFunctions;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.delegates.NavMenuItemGetterSetter;

public class IntelVisibilityMenu extends AppCompatActivity {
    INTEL_RE_DB db;
    Context context;
    ValueAdapter adapter;
    RecyclerView recyclerView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    String store_cd, visit_date, user_type, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intel_visibility);
        context = this;
        uivalidate();
        db = new INTEL_RE_DB(context);
        db.open();
    }

    private void uivalidate() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.drawer_layout_recycle_store_visibility);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        if (!CommonFunctions.isTablet(context)){
            toolbar.setTitleTextAppearance(context,R.style.changestext_sizefor_mobile);
        }
        setTitle("Visibility Menu - " + visit_date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
        adapter = new ValueAdapter(context, getdata());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        this.finish();
    }


    private class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<NavMenuItemGetterSetter> data = Collections.emptyList();

        public ValueAdapter(Context context, List<NavMenuItemGetterSetter> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.custom_row, parent, false);
            MyViewHolder holder = new ValueAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder viewHolder, final int position) {
            final NavMenuItemGetterSetter current = data.get(position);
            viewHolder.icon.setImageResource(current.getIconImg());
            viewHolder.icon_txtname.setText(current.getIconName());
            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (current.getIconImg() == R.drawable.soft_merch || current.getIconImg() == R.drawable.soft_merch_done) {
                        Intent in7 = new Intent(context, SoftMerchVisibilityActivity.class);
                        startActivity(in7);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    }
                    //semi permanent visibility
                    else if (current.getIconImg() == R.drawable.semi_permanent_merch || current.getIconImg() == R.drawable.semi_permanent_merch_done) {
                        Intent in7 = new Intent(context, VisibilitySemiParmanentActivity.class);
                        startActivity(in7);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView icon_txtname;

            public MyViewHolder(View itemView) {
                super(itemView);
                icon = (ImageView) itemView.findViewById(R.id.list_icon);
                icon_txtname = (TextView) itemView.findViewById(R.id.icon_txtname);
            }
        }

    }

    public List<NavMenuItemGetterSetter> getdata() {
        List<NavMenuItemGetterSetter> data = new ArrayList<>();
        int softMerch = 0, semiparmanet_merch = 0;

        if (db.getsoftm_posmChilddata(db.getSpecificStoreData(store_cd).get(0).getRegionId(), db.getSpecificStoreData(store_cd).get(0).getClassificationId(), db.getSpecificStoreData(store_cd).get(0).getStoreTypeId()).size() > 0) {
            if (db.isVisibilitySoftMerchFilled(store_cd)) {
                softMerch = R.drawable.soft_merch_done;
            } else {
                softMerch = R.drawable.soft_merch;
            }
        } else {
            softMerch = R.drawable.soft_merch_grey;
        }


        if (db.getsemiparmanentheaderData(Integer.parseInt(store_cd)).size() > 0) {
            if (db.isVisibilitySPMerchFilled(store_cd)) {
                semiparmanet_merch = R.drawable.semi_permanent_merch_done;
            } else {
                semiparmanet_merch = R.drawable.semi_permanent_merch;
            }
        } else {
            semiparmanet_merch = R.drawable.semi_permanent_merch_grey;
        }
        int img[] = {softMerch, semiparmanet_merch};
        String name[] = {"Soft Merch", "Semi Permanent Merch"};
        for (int i = 0; i < img.length; i++) {
            NavMenuItemGetterSetter recData = new NavMenuItemGetterSetter();
            recData.setIconImg(img[i]);
            recData.setIconName(name[i]);
            data.add(recData);
        }
        return data;
    }

}
