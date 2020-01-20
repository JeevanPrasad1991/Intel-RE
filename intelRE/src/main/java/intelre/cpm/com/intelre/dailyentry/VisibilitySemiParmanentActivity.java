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
import android.graphics.Typeface;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.CommonFunctions;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmTypeQuestion;

public class VisibilitySemiParmanentActivity extends AppCompatActivity {
    String _pathforcheck, _pathforcheck_child, _path, img1 = "", img_child = "", store_cd, visit_date, user_type, username, Error_Message;
    boolean isDialogOpen = true, installation_flag = false, checkflag = true;
    HashMap<PosmTypeQuestion, List<PosmTypeQuestion>> listDataChild;
    ArrayList<Integer> checkHeaderArray = new ArrayList<>();
    String[] dentiststring = {"-Select-", "Yes", "No"};
    private SharedPreferences.Editor editor = null;
    private ArrayAdapter<String> reason_adapter;
    private SharedPreferences preferences;
    ExpandableListView lvExp_semi_p_merch;
    List<PosmTypeQuestion> listDataHeader;
    List<PosmTypeQuestion> questionList;
    ExpandableListAdapter listAdapter;
    static int child_position = -1;
    static int grp_position = -1;
    FloatingActionButton fab;
    Context context;
    INTEL_RE_DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visibility_samipermanent);
        context = this;
        visibilitysemiparmanentuidisign();
        prepareListData();
        listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);
        // setting brandMasterArrayList adapter
        lvExp_semi_p_merch.setAdapter(listAdapter);

        lvExp_semi_p_merch.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastItem = firstVisibleItem + visibleItemCount;
                if (firstVisibleItem == 0) {
                    fab.setVisibility(View.VISIBLE);
                } else if (lastItem == totalItemCount) {
                    fab.setVisibility(View.INVISIBLE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                lvExp_semi_p_merch.invalidateViews();
                lvExp_semi_p_merch.clearFocus();
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
            }
        });

        // Listview Group click listener
        lvExp_semi_p_merch.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                listAdapter.notifyDataSetChanged();
                lvExp_semi_p_merch.invalidateViews();
                lvExp_semi_p_merch.clearFocus();
                return false;
            }
        });

        // Listview Group expanded listener
        lvExp_semi_p_merch.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }

            }
        });

        // Listview Group collasped listener
        lvExp_semi_p_merch.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
            }
        });

        // Listview on child click listener
        lvExp_semi_p_merch.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvExp_semi_p_merch.clearFocus();
                if (validateData(listDataChild, listDataHeader)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(R.string.parinaam).setMessage(R.string.alertsaveData).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.open();
                            db.insertVisibilitySemiParmanetMerchData(store_cd, visit_date, listDataChild, listDataHeader);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            VisibilitySemiParmanentActivity.this.finish();
                        }
                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                } else {
                    listAdapter.notifyDataSetChanged();
                    if (Error_Message != null) {
                        Snackbar.make(lvExp_semi_p_merch, Error_Message, Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void visibilitysemiparmanentuidisign() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lvExp_semi_p_merch = findViewById(R.id.lvExp_semi_p_merch);
        fab = findViewById(R.id.fab);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        if (!CommonFunctions.isTablet(context)){
            toolbar.setTitleTextAppearance(context,R.style.changestext_sizefor_mobile);
        }
        getSupportActionBar().setTitle("SP Merch - " + visit_date);
        db = new INTEL_RE_DB(context);
        db.open();
    }

    private void prepareListData() {
        db.open();
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        listDataHeader = db.getsemip_insertedHeaderdata(store_cd, visit_date);
        if (listDataHeader.size() == 0) {
            listDataHeader = db.getsemiparmanentheaderData(Integer.parseInt(store_cd));
        }
        if (listDataHeader.size() > 0) {
            for (int i = 0; i < listDataHeader.size(); i++) {
                questionList = db.getVisibilitySemiPermanetMerchInsertedData(store_cd, listDataHeader.get(i).getPosmTypeId());
                if (questionList.size() > 0) {
                    fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.edit_txt));
                } else {
                    questionList = db.getVisibilitySemiPermanetMerchChildData(listDataHeader.get(i).getPosmTypeId());
                }
                listDataChild.put(listDataHeader.get(i), questionList); // Header, Child data
            }
        }
    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<PosmTypeQuestion> _listDataHeader;
        private HashMap<PosmTypeQuestion, List<PosmTypeQuestion>> _listDataChild;

        public ExpandableListAdapter(Context context, List<PosmTypeQuestion> listDataHeader, HashMap<PosmTypeQuestion, List<PosmTypeQuestion>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            final PosmTypeQuestion childText = (PosmTypeQuestion) getChild(groupPosition, childPosition);
            final PosmTypeQuestion headerTitle = (PosmTypeQuestion) getGroup(groupPosition);
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_parent_visibility_semip, null);
                holder = new ViewHolder();
                holder.cardView = convertView.findViewById(R.id.card_view);
                holder.semip_spin = (Spinner) convertView.findViewById(R.id.semip_spin);
                holder.semi_permanent_img = (ImageView) convertView.findViewById(R.id.semi_permanent_img);
                holder.semip_deploymentdate = (TextView) convertView.findViewById(R.id.semip_deploymentdate);
                holder.rl_deployment_date = (LinearLayout) convertView.findViewById(R.id.rl_deployment_date);
                holder.rl_installation = (LinearLayout) convertView.findViewById(R.id.rl_installation);
                holder.semip_installation_date = (TextView) convertView.findViewById(R.id.semip_installation_date);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //non changeable value
            TextView txtListChild = convertView.findViewById(R.id.lblListItem_v_soft);
            txtListChild.setText(childText.getQuestion().trim());

            //for reason spinner
            final ArrayList<PosmTypeQuestion> reason_list = db.getvisibilitysemip_answer(childText.getQuestionId());
            PosmTypeQuestion non = new PosmTypeQuestion();
            non.setAnswer("- Select Reason -");
            non.setAnswerId(0);
            reason_list.add(0, non);

            holder.semip_spin.setAdapter(new ReasonSpinnerAdapter(_context, R.layout.spinner_text_view, reason_list));

            final ViewHolder finalHolder = holder;
            holder.semip_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    if (pos != 0) {
                        PosmTypeQuestion ans = reason_list.get(pos);
                        if (childText.getQuestionId().toString().equals("14")) {
                            if (ans.getQuestionDisable().toString().equals("15")) {
                                finalHolder.rl_deployment_date.setVisibility(View.VISIBLE);
                                finalHolder.semip_deploymentdate.setText(visit_date);
                                childText.setDeployment_flag(ans.getQuestionDisable().toString());
                                childText.setDeployment_date(visit_date);
                            } else {
                                finalHolder.rl_deployment_date.setVisibility(View.GONE);
                                finalHolder.semip_deploymentdate.setText("");
                                childText.setDeployment_flag("0");
                                childText.setDeployment_date("");
                            }
                        } else {
                            finalHolder.rl_deployment_date.setVisibility(View.GONE);
                            finalHolder.semip_deploymentdate.setText("");
                            childText.setDeployment_flag("0");
                            childText.setDeployment_date("");
                        }

                        ///for installation set data.......
                        if (childText.getQuestionId().toString().equals("2")) {
                            if (ans.getAnswerId().toString().equals("3")) {
                                installation_flag = true;
                            } else {
                                installation_flag = false;
                            }
                        } else if (childText.getQuestionId().toString().equals("3")) {
                            if (ans.getAnswerId().toString().equals("5")) {
                                if (installation_flag) {
                                    finalHolder.rl_installation.setVisibility(View.VISIBLE);
                                    finalHolder.semip_installation_date.setText(visit_date);
                                    childText.setInstallation_date(visit_date);
                                } else {
                                    finalHolder.rl_installation.setVisibility(View.VISIBLE);
                                    finalHolder.semip_installation_date.setText(visit_date);
                                    childText.setInstallation_date(visit_date);
                                }
                                childText.setDeployment_flag(ans.getQuestionDisable().toString());

                            } else {
                                if (installation_flag) {
                                    finalHolder.rl_installation.setVisibility(View.VISIBLE);
                                    finalHolder.semip_installation_date.setText(visit_date);
                                    childText.setInstallation_date(visit_date);
                                    childText.setDeployment_flag("4");
                                } else {
                                    finalHolder.rl_installation.setVisibility(View.GONE);
                                    finalHolder.semip_installation_date.setText("");
                                    childText.setInstallation_date("");
                                    childText.setDeployment_flag("0");
                                }
                            }
                        }


                        if (ans.getImageAllow()) {
                            childText.setSemip_currectimageAllow("1");
                            finalHolder.semi_permanent_img.setVisibility(View.VISIBLE);
                        } else {
                            finalHolder.semi_permanent_img.setVisibility(View.GONE);
                            childText.setSemip_currectimageAllow("0");
                        }
                        childText.setSemip_right_ans_Id(ans.getAnswerId().toString());
                        childText.setSemip_right_ans(ans.getAnswer());
                    } else {
                        childText.setSemip_right_ans_Id("0");
                        childText.setSemip_right_ans("");
                        childText.setDeployment_flag("0");
                        childText.setDeployment_date("");
                        childText.setInstallation_date("");
                        childText.setSemip_currectimageAllow("0");
                        childText.setSemipmerch_img("");
                        finalHolder.rl_deployment_date.setVisibility(View.GONE);

                        ///for installation set data.......
                        if (childText.getQuestionId().toString().equals("2")) {
                            installation_flag = false;
                        } else if (childText.getQuestionId().toString().equals("3")) {

                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            for (int i = 0; i < reason_list.size(); i++) {
                if (reason_list.get(i).getAnswerId().toString().equals(childText.getSemip_right_ans_Id())) {
                    holder.semip_spin.setSelection(i);
                    break;
                }
            }


            holder.semi_permanent_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lvExp_semi_p_merch.clearFocus();
                    grp_position = groupPosition;
                    child_position = childPosition;
                    _pathforcheck_child = store_cd + "_" + childText.getQuestionId().toString() + "_SEMIPMERCHIMG_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + _pathforcheck_child;
                    CommonFunctions.startAnncaCameraActivity(_context, _path, null, false);
                }
            });


            if (!img_child.equals("")) {
                if (childPosition == child_position) {
                    if (groupPosition == grp_position) {
                        childText.setSemipmerch_img(img_child);
                        img_child = "";
                    }
                }
            }


            if (childText.getSemip_currectimageAllow().equals("1")) {
                holder.semi_permanent_img.setVisibility(View.VISIBLE);
                holder.semi_permanent_img.setId(childPosition);
            } else {
                holder.semi_permanent_img.setVisibility(View.GONE);
                holder.semi_permanent_img.setId(childPosition);
            }

            if (childText.getDeployment_flag().equals("15")) {
                holder.rl_deployment_date.setVisibility(View.VISIBLE);
                holder.semip_deploymentdate.setText(visit_date);
                childText.setDeployment_date(visit_date);
                holder.rl_deployment_date.setId(childPosition);
                holder.semip_deploymentdate.setId(childPosition);
            } else {
                holder.rl_deployment_date.setVisibility(View.GONE);
                holder.semip_deploymentdate.setText("");
                holder.semip_deploymentdate.setText("");
                holder.rl_deployment_date.setId(childPosition);
                holder.semip_deploymentdate.setId(childPosition);
            }


            if (childText.getDeployment_flag().equals("4")) {
                holder.rl_installation.setVisibility(View.VISIBLE);
                holder.semip_installation_date.setText(visit_date);
                childText.setInstallation_date(visit_date);
                holder.rl_installation.setId(childPosition);
                holder.semip_installation_date.setId(childPosition);
            } else {
                holder.rl_installation.setVisibility(View.GONE);
                holder.semip_installation_date.setText("");
                childText.setInstallation_date("");
                holder.rl_installation.setId(childPosition);
                holder.semip_installation_date.setId(childPosition);
            }


            if (!childText.getSemipmerch_img().equals("")) {
                holder.semi_permanent_img.setImageResource(R.mipmap.camera_green);
                holder.semi_permanent_img.setId(childPosition);
            } else {
                holder.semi_permanent_img.setImageResource(R.mipmap.camera_orange);
                holder.semi_permanent_img.setId(childPosition);
            }


            if (checkflag == false) {
                boolean tempflag = false;
                if (childText.getSemip_right_ans_Id().equals("0")) {
                    tempflag = true;
                } else if (!childText.getSemip_currectimageAllow().equals("0") && childText.getSemipmerch_img().equals("")) {
                    tempflag = true;
                }
                if (tempflag) {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                }
            } else {
                holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
            }

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
            final PosmTypeQuestion headerTitle = (PosmTypeQuestion) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_storesoftmerch, null);
            }


            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader_soft);
            final Spinner subcategorySpin = (Spinner) convertView.findViewById(R.id.subcategorySpin);
            final CardView card_view = (CardView) convertView.findViewById(R.id.card_view);
            final ImageView semiheader_img = (ImageView) convertView.findViewById(R.id.semiheader_img);
            lblListHeader.setText(headerTitle.getPosmType().trim());

            reason_adapter = new ArrayAdapter<>(_context, R.layout.spinner_custom_item, dentiststring);
            subcategorySpin.setAdapter(reason_adapter);
            reason_adapter.setDropDownViewResource(R.layout.spinner_custom_item);
            subcategorySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    //Do the stuff you only want triggered by real user interaction.
                    if (pos != 0) {
                        headerTitle.setHeader_availebility(String.valueOf(parent.getSelectedItem().toString()));
                        if (parent.getSelectedItem().toString().equals("Yes")) {
                            if (!headerTitle.getPosmTypeId().toString().equals("12")) {
                                semiheader_img.setVisibility(View.VISIBLE);
                            } else {
                                headerTitle.setHeader_image("");
                                semiheader_img.setVisibility(View.GONE);
                            }
                        } else {
                            if (lvExp_semi_p_merch.isGroupExpanded(groupPosition)) {
                                lvExp_semi_p_merch.collapseGroup(groupPosition);
                            }
                            try {
                                for (int k = 0; k < _listDataChild.get(headerTitle).size(); k++) {
                                    _listDataChild.get(headerTitle).get(k).setSemip_right_ans_Id("0");
                                    _listDataChild.get(headerTitle).get(k).setSemip_right_ans("");
                                    _listDataChild.get(headerTitle).get(k).setSemipmerch_img("");
                                    _listDataChild.get(headerTitle).get(k).setSemip_currectimageAllow("0");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Crashlytics.logException(e);
                            }
                            headerTitle.setHeader_image("");
                            semiheader_img.setVisibility(View.GONE);
                            semiheader_img.setImageResource(R.mipmap.camera_orange);
                        }
                    } else {
                        headerTitle.setHeader_image("");
                        headerTitle.setHeader_availebility("0");
                        semiheader_img.setVisibility(View.GONE);
                        if (lvExp_semi_p_merch.isGroupExpanded(groupPosition)) {
                            lvExp_semi_p_merch.collapseGroup(groupPosition);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            semiheader_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grp_position = groupPosition;
                    _pathforcheck = store_cd + "_" + headerTitle.getPosmTypeId().toString() + "_SEMIH_IMG_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + _pathforcheck;
                    CommonFunctions.startAnncaCameraActivity(_context, _path, null, false);
                }
            });

            if (!img1.equals("")) {
                if (grp_position == groupPosition) {
                    headerTitle.setHeader_image(img1);
                    img1 = "";
                }
            }

            if (!headerTitle.getHeader_image().equals("")) {
                semiheader_img.setImageResource(R.mipmap.camera_green);
            } else {
                semiheader_img.setImageResource(R.mipmap.camera_orange);
            }


            if (headerTitle.getHeader_availebility().equalsIgnoreCase("Yes")) {
                subcategorySpin.setSelection(1);
            } else if (headerTitle.getHeader_availebility().equalsIgnoreCase("No")) {
                subcategorySpin.setSelection(2);
            }
            if (!headerTitle.getPosmTypeId().toString().equals("12") && headerTitle.getHeader_availebility().equalsIgnoreCase("Yes")) {
                semiheader_img.setVisibility(View.VISIBLE);
            } else {
                semiheader_img.setVisibility(View.GONE);
            }


            card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (subcategorySpin.getSelectedItemId() == 1) {
                        if (!headerTitle.getPosmTypeId().toString().equals("12")) {
                            if (!headerTitle.getHeader_image().equals("")) {
                                if (lvExp_semi_p_merch.isGroupExpanded(groupPosition)) {
                                    lvExp_semi_p_merch.collapseGroup(groupPosition);
                                } else {
                                    lvExp_semi_p_merch.expandGroup(groupPosition);
                                }
                            } else {
                                if (isDialogOpen) {
                                    isDialogOpen = !isDialogOpen;
                                    AlertDialog.Builder alert = new AlertDialog.Builder(_context).setTitle(getString(R.string.parinaam)).setMessage("Please click image of " + headerTitle.getPosmType()).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            isDialogOpen = !isDialogOpen;
                                            dialog.dismiss();
                                        }
                                    });
                                    alert.show();
                                }

                            }
                        } else {
                            if (lvExp_semi_p_merch.isGroupExpanded(groupPosition)) {
                                lvExp_semi_p_merch.collapseGroup(groupPosition);
                            } else {
                                lvExp_semi_p_merch.expandGroup(groupPosition);
                            }
                        }
                    } else if (subcategorySpin.getSelectedItemId() == 2) {

                    } else {
                        if (isDialogOpen) {
                            isDialogOpen = !isDialogOpen;
                            AlertDialog.Builder alert = new AlertDialog.Builder(_context).setTitle(getString(R.string.parinaam)).setMessage("Please select availability of " + headerTitle.getPosmType()).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isDialogOpen = !isDialogOpen;
                                    dialog.dismiss();
                                }
                            });
                            alert.show();
                        }
                    }
                }
            });


            if (!checkflag) {
                if (checkHeaderArray.contains(groupPosition)) {
                    card_view.setCardBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    card_view.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            } else {
                card_view.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public class ViewHolder {
        ImageView semi_permanent_img;
        LinearLayout rl_deployment_date, rl_installation;
        TextView semip_deploymentdate, semip_installation_date;
        Spinner semip_spin;
        CardView cardView;
    }


    boolean validateData
            (HashMap<PosmTypeQuestion, List<PosmTypeQuestion>> listDataChild2, List<PosmTypeQuestion> listDataHeader2) {
        checkflag = true;
        checkHeaderArray.clear();
        for (int i = 0; i < listDataHeader2.size(); i++) {
            for (int j = 0; j < listDataChild2.get(listDataHeader2.get(i)).size(); j++) {
                ///for header data
                String headerAvailebility = listDataHeader2.get(i).getHeader_availebility();
                String header_img = listDataHeader2.get(i).getHeader_image();
                ///for child data
                String semip_right_ans = listDataChild2.get(listDataHeader2.get(i)).get(j).getSemip_right_ans_Id();
                String semip_currectimageAllow = listDataChild2.get(listDataHeader2.get(i)).get(j).getSemip_currectimageAllow();
                String semipmerchImg = listDataChild2.get(listDataHeader2.get(i)).get(j).getSemipmerch_img();
                if (headerAvailebility.equals("0")) {
                    checkflag = false;
                    break;
                } else if (headerAvailebility.equalsIgnoreCase("Yes")) {
                    if (listDataHeader2.get(i).getPosmTypeId().toString().equals("12")) {
                        if (semip_right_ans.equals("0")) {
                            Error_Message = "Please select all spinner dropdown.";
                            checkflag = false;
                            break;
                        } else if (!semip_right_ans.equals("0") && !semip_currectimageAllow.equals("0") && semipmerchImg.equals("")) {
                            Error_Message = "Please click image.";
                            checkflag = false;
                            break;
                        } else {
                            checkflag = true;
                        }
                    } else {
                        if (header_img.equals("")) {
                            checkflag = false;
                            break;
                        } else if (!header_img.equals("") && semip_right_ans.equals("0")) {
                            Error_Message = "Please select all spinner dropdown.";
                            checkflag = false;
                            break;
                        } else if (!semip_right_ans.equals("0") && !semip_currectimageAllow.equals("0") && semipmerchImg.equals("")) {
                            Error_Message = "Please click image.";
                            checkflag = false;
                            break;
                        } else {
                            checkflag = true;
                        }
                    }


                } else {
                    checkflag = true;
                }
            }
            if (checkflag == false) {
                if (!checkHeaderArray.contains(i)) {
                    checkHeaderArray.add(i);
                }
                break;
            }
        }
        listAdapter.notifyDataSetChanged();

        return checkflag;
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
                        if (new File(CommonString.FILE_PATH + _pathforcheck).exists()) {
                            String metadata = CommonFunctions.setMetadataAtImages(preferences.getString(CommonString.KEY_STORE_NAME, null), store_cd, "Semi Header Image", username);
                            CommonFunctions.addMetadataAndTimeStampToImage(context, _path, metadata, visit_date);
                            img1 = _pathforcheck;
                            lvExp_semi_p_merch.invalidateViews();
                            _pathforcheck = "";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }

                } else if (_pathforcheck_child != null && !_pathforcheck_child.equals("")) {
                    try {
                        if (new File(CommonString.FILE_PATH + _pathforcheck_child).exists()) {
                            String metadata = CommonFunctions.setMetadataAtImages(preferences.getString(CommonString.KEY_STORE_NAME, null), store_cd, "SemiMerch Image", username);
                            CommonFunctions.addMetadataAndTimeStampToImage(context, _path, metadata, visit_date);
                            img_child = _pathforcheck_child;
                            lvExp_semi_p_merch.invalidateViews();
                            _pathforcheck_child = "";
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


    public class ReasonSpinnerAdapter extends ArrayAdapter<PosmTypeQuestion> {
        List<PosmTypeQuestion> list;
        Context context;
        int resourceId;

        public ReasonSpinnerAdapter(Context context, int resourceId, ArrayList<PosmTypeQuestion> list) {
            super(context, resourceId, list);
            this.context = context;
            this.list = list;
            this.resourceId = resourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);

            PosmTypeQuestion cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(list.get(position).getAnswer());

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);

            PosmTypeQuestion cm = list.get(position);

            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(cm.getAnswer());

            return view;
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage(CommonString.ONBACK_ALERT_MESSAGE).setCancelable(false).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                VisibilitySemiParmanentActivity.this.finish();
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage(CommonString.ONBACK_ALERT_MESSAGE).setCancelable(false).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                    VisibilitySemiParmanentActivity.this.finish();
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

}
