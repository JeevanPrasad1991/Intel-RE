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

public class ShoperMToolMenu extends AppCompatActivity {
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
        setContentView(R.layout.activity_shoper_mtool_menu);
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
        recyclerView = findViewById(R.id.drawer_layout_recycle_shoper);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        if (!CommonFunctions.isTablet(context)){
            toolbar.setTitleTextAppearance(context,R.style.changestext_sizefor_mobile);
        }
        setTitle("Shoper MT Menu - " + visit_date);
    }


    @Override
    protected void onResume() {
        super.onResume();
        db.open();
        adapter = new ValueAdapter(context, getdata());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
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
            ValueAdapter.MyViewHolder holder = new ValueAdapter.MyViewHolder(view);
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
                    if (current.getIconImg() == R.drawable.ipos || current.getIconImg() == R.drawable.ipos_done) {
                        Intent in7 = new Intent(context, IPOSActivity.class);
                        startActivity(in7);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    }

                    //semi permanent visibility
                    else if (current.getIconImg() == R.drawable.rxt || current.getIconImg() == R.drawable.rxt_done) {
                        Intent in7 = new Intent(context, RXTMarketingTActivity.class);
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
        int ipos = 0, rxt;

        if (db.isIPOSFilled(store_cd)) {
            ipos = R.drawable.ipos_done;
        } else {
            ipos = R.drawable.ipos;
        }

        if (db.isRXTFilled(store_cd)) {
            rxt = R.drawable.rxt_done;
        } else {
            rxt = R.drawable.rxt;
        }

        int img[] = {ipos, rxt};
        String name[] = {"IPOS", "RXT"};
        for (int i = 0; i < img.length; i++) {
            NavMenuItemGetterSetter recData = new NavMenuItemGetterSetter();
            recData.setIconImg(img[i]);
            recData.setIconName(name[i]);
            data.add(recData);
        }
        return data;
    }
}
