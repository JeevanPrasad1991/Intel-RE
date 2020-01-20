package intelre.cpm.com.intelre.dailyentry;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
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
import intelre.cpm.com.intelre.gsonGetterSetter.PosmMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmTypeQuestion;

public class SoftMerchVisibilityActivity extends AppCompatActivity implements View.OnClickListener {
    String store_cd, visit_date, user_type, username, Error_Message, _pathforcheck, _path, img1 = "";
    ArrayList<Integer> checkHeaderArray = new ArrayList<>();
    HashMap<PosmTypeQuestion, List<PosmTypeQuestion>> listDataChild;
    String[] dentiststring = {"-Select-", "Yes", "No"};
    private SharedPreferences.Editor editor = null;
    boolean checkflag = true, isDialogOpen = true;
    private ArrayAdapter<String> reason_adapter;
    private SharedPreferences preferences;
    ExpandableListView lvExp_softmerch;
    ExpandableListAdapter listAdapter;
    static int child_position = -1;
    List<PosmTypeQuestion> listDataHeader;
    List<PosmTypeQuestion> questionList;
    static int grp_position = -1;
    FloatingActionButton fab;
    PosmTypeQuestion object;
    INTEL_RE_DB db;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_merch_visibility);
        context = this;
        softmerchvisibilityudesign();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvExp_softmerch.clearFocus();
                if (validateData(listDataChild, listDataHeader)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(getString(R.string.parinaam)).setMessage(R.string.alertsaveData).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.open();
                            db.insertVisibilitySoftMerchData(store_cd, visit_date, listDataChild, listDataHeader);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            SoftMerchVisibilityActivity.this.finish();
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
                        Snackbar.make(lvExp_softmerch, Error_Message, Snackbar.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    private void softmerchvisibilityudesign() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lvExp_softmerch = findViewById(R.id.lvExp_softmerch);
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

        setTitle("Soft Merch - " + visit_date);
        db = new INTEL_RE_DB(context);
        db.open();
        prepareListData();
        listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);
        // setting brandMasterArrayList adapter
        lvExp_softmerch.setAdapter(listAdapter);

        lvExp_softmerch.setOnScrollListener(new AbsListView.OnScrollListener() {
            @SuppressLint("RestrictedApi")
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
                lvExp_softmerch.invalidateViews();
                lvExp_softmerch.clearFocus();
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
            }
        });


        // Listview Group click listener
        lvExp_softmerch.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        lvExp_softmerch.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
            }
        });

        // Listview Group collasped listener
        lvExp_softmerch.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
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
        lvExp_softmerch.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });

    }

    private void prepareListData() {
        db.open();
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        listDataHeader = db.getinsertedSoftMerchVisibility(store_cd, visit_date);
        if (listDataHeader.size() == 0) {
            object = new PosmTypeQuestion();
            object.setSoft_merch_name("Is soft Merch");
            object.setHeader_availebility("0");
            object.setHeader_image("");
            listDataHeader.add(object);
        }

        if (listDataHeader.size() > 0) {
            questionList = db.getVisibilitySoftMerchInsertedData(store_cd, visit_date);
            if (questionList.size() > 0) {
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.edit_txt));
            } else {
                questionList = db.getsoftm_posmChilddata(db.getSpecificStoreData(store_cd).get(0).getRegionId(), db.getSpecificStoreData(store_cd).get(0).getClassificationId(), db.getSpecificStoreData(store_cd).get(0).getStoreTypeId());
            }
            listDataChild.put(listDataHeader.get(0), questionList); // Header, Child data
        }
    }

    @Override
    public void onClick(View v) {

    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<PosmTypeQuestion> _listDataHeader;
        private HashMap<PosmTypeQuestion, List<PosmTypeQuestion>> _listDataChild;

        public ExpandableListAdapter(Context context, List<PosmTypeQuestion> listDataHeader,
                                     HashMap<PosmTypeQuestion, List<PosmTypeQuestion>> listChildData) {
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
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_parent_visibility_soft, null);
                holder = new ViewHolder();
                holder.cardView = convertView.findViewById(R.id.card_view);
                holder.softm_spin = (Spinner) convertView.findViewById(R.id.softm_spin);
                holder.rl_deployment = (LinearLayout) convertView.findViewById(R.id.rl_deployment);
                holder.nDeploymentSoft_edt = convertView.findViewById(R.id.nDeploymentSoft_edt);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TextView txtListChild = convertView.findViewById(R.id.lblListItem_v_soft);
            txtListChild.setText(childText.getPosmType().trim());

            reason_adapter = new ArrayAdapter<>(_context, R.layout.spinner_custom_item, dentiststring);
            holder.softm_spin.setAdapter(reason_adapter);
            reason_adapter.setDropDownViewResource(R.layout.spinner_custom_item);
            final ViewHolder finalHolder = holder;
            holder.softm_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    //Do the stuff you only want triggered by real user interaction.
                    if (pos != 0) {
                        childText.setSemip_right_ans_Id(String.valueOf(parent.getSelectedItemId()));
                        if (parent.getSelectedItem().toString().equals("Yes")) {
                            finalHolder.rl_deployment.setVisibility(View.VISIBLE);
                        } else {
                            finalHolder.rl_deployment.setVisibility(View.GONE);
                            finalHolder.nDeploymentSoft_edt.setText("");
                            childText.setPromoType_qty("");

                        }
                    } else {
                        childText.setSemip_right_ans_Id("0");
                        childText.setPromoType_qty("");
                        childText.setSemip_right_ans("0");
                        finalHolder.nDeploymentSoft_edt.setText("");
                        finalHolder.rl_deployment.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            if (childText.getSemip_right_ans_Id().equals("1")) {
                holder.softm_spin.setSelection(1);
                holder.softm_spin.setId(childPosition);
                finalHolder.rl_deployment.setVisibility(View.VISIBLE);
            } else if (childText.getSemip_right_ans_Id().equals("2")) {
                holder.softm_spin.setSelection(2);
                holder.softm_spin.setId(childPosition);
                finalHolder.rl_deployment.setVisibility(View.GONE);
            }

            holder.nDeploymentSoft_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        final EditText Caption = (EditText) v;
                        String value = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                        if (value.equals("")) {
                            childText.setPromoType_qty("");
                        } else {
                            childText.setPromoType_qty(value);
                        }
                    }
                }

            });

            holder.nDeploymentSoft_edt.setText(childText.getPromoType_qty());
            holder.nDeploymentSoft_edt.setId(childPosition);

            if (!checkflag) {
                boolean tempflag = false;
                if (childText.getSemip_right_ans_Id().equals("0")) {
                    tempflag = true;
                } else if (childText.getSemip_right_ans_Id().equals("1") && childText.getPromoType_qty().equals("")) {
                    holder.nDeploymentSoft_edt.setHintTextColor(Color.WHITE);
                    holder.nDeploymentSoft_edt.setHint("EMPTY");
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
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            final PosmTypeQuestion headerTitle = (PosmTypeQuestion) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_storesoftmerch, null);
            }

            TextView lblListHeader_soft = (TextView) convertView.findViewById(R.id.lblListHeader_soft);
            lblListHeader_soft.setText(headerTitle.getSoft_merch_name().trim());

            final Spinner subcategorySpin = (Spinner) convertView.findViewById(R.id.subcategorySpin);
            final CardView card_view = (CardView) convertView.findViewById(R.id.card_view);
            final ImageView semiheader_img = (ImageView) convertView.findViewById(R.id.semiheader_img);

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
                            semiheader_img.setVisibility(View.VISIBLE);
                        } else {
                            headerTitle.setHeader_image("");
                            semiheader_img.setVisibility(View.GONE);
                            semiheader_img.setImageResource(R.mipmap.camera_orange);
                            try {
                                for (int k = 0; k < _listDataChild.get(headerTitle).size(); k++) {
                                    _listDataChild.get(headerTitle).get(k).setSemip_right_ans_Id("0");
                                    _listDataChild.get(headerTitle).get(k).setSemip_right_ans("");
                                    _listDataChild.get(headerTitle).get(k).setPromoType_qty("");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Crashlytics.logException(e);
                            }
                            if (lvExp_softmerch.isGroupExpanded(groupPosition)) {
                                lvExp_softmerch.collapseGroup(groupPosition);
                            }
                        }
                    } else {
                        headerTitle.setHeader_image("");
                        headerTitle.setHeader_availebility("0");
                        semiheader_img.setVisibility(View.GONE);
                        if (lvExp_softmerch.isGroupExpanded(groupPosition)) {
                            lvExp_softmerch.collapseGroup(groupPosition);
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
                    _pathforcheck = store_cd + "_SOFTM_IMG_" + visit_date.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + _pathforcheck;
                    CommonFunctions.startAnncaCameraActivity(context, _path, null, false);
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
                semiheader_img.setVisibility(View.VISIBLE);
                subcategorySpin.setSelection(1);
            } else if (headerTitle.getHeader_availebility().equalsIgnoreCase("No")) {
                subcategorySpin.setSelection(2);
                semiheader_img.setVisibility(View.GONE);
            }


            card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (subcategorySpin.getSelectedItemId() == 1) {
                        if (!headerTitle.getHeader_image().equals("")) {
                            if (lvExp_softmerch.isGroupExpanded(groupPosition)) {
                                lvExp_softmerch.collapseGroup(groupPosition);
                            } else {
                                lvExp_softmerch.expandGroup(groupPosition);
                            }
                        } else {
                            if (isDialogOpen) {
                                isDialogOpen = !isDialogOpen;
                                AlertDialog.Builder alert = new AlertDialog.Builder(_context).setTitle(getString(R.string.parinaam)).setMessage("Please click image of Soft Merch Visibility.").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        isDialogOpen = !isDialogOpen;
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();
                            }

                        }
                    } else if (subcategorySpin.getSelectedItemId() == 2) {

                    } else {
                        if (isDialogOpen) {
                            isDialogOpen = !isDialogOpen;
                            AlertDialog.Builder alert = new AlertDialog.Builder(_context).setTitle(getString(R.string.parinaam)).setMessage("Please select availability of Soft Merch Visibility.").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
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
        CardView cardView;
        Spinner softm_spin;
        LinearLayout rl_deployment;
        EditText nDeploymentSoft_edt;


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
                String soft_m_qty = listDataChild2.get(listDataHeader2.get(i)).get(j).getPromoType_qty();
                if (headerAvailebility.equals("0")) {
                    checkflag = false;
                    break;
                } else if (headerAvailebility.equalsIgnoreCase("Yes")) {
                    if (header_img.equals("")) {
                        checkflag = false;
                        break;
                    } else if (!header_img.equals("") && semip_right_ans.equals("0")) {
                        Error_Message = "Please select all spinner dropdown.";
                        checkflag = false;
                        break;
                    } else if (semip_right_ans.equals("1") && soft_m_qty.equals("")) {
                        Error_Message = "Please enter deployment quantity.";
                        checkflag = false;
                        break;
                    } else {
                        checkflag = true;
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
                            String metadata = CommonFunctions.setMetadataAtImages(preferences.getString(CommonString.KEY_STORE_NAME, null), store_cd, "Softmerch Image", username);
                            CommonFunctions.addMetadataAndTimeStampToImage(context, _path, metadata, visit_date);
                            img1 = _pathforcheck;
                            lvExp_softmerch.invalidateViews();
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
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SoftMerchVisibilityActivity.this);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SoftMerchVisibilityActivity.this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
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
        return super.onOptionsItemSelected(item);
    }

}
