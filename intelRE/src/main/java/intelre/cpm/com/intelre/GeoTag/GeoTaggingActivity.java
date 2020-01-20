package intelre.cpm.com.intelre.GeoTag;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONObject;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.IntelLoginActivty;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.AlertandMessages;
import intelre.cpm.com.intelre.constant.CommonFunctions;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.dailyentry.StoreimageActivity;
import intelre.cpm.com.intelre.delegates.CoverageBean;
import intelre.cpm.com.intelre.gettersetter.GeotaggingBeans;
import intelre.cpm.com.intelre.retrofit.DownloadAllDatawithRetro;

public class GeoTaggingActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    String result, errormsg = "";
    private ProgressBar pb;
    private GoogleMap mMap;
    double latitude = 0.0;
    double longitude = 0.0;
    protected String _path, _pathforcheck, img_str = "", status;
    private Location mLastLocation;
    private LocationManager locmanager = null;
    FloatingActionButton fab, fabcarmabtn;
    SharedPreferences preferences;
    String username, str, storename, visitData, storeid;
    INTEL_RE_DB db;
    LocationManager locationManager;
    Marker currLocationMarker;
    Geocoder geocoder;
    boolean enabled;
    boolean uploadflag = false;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static int UPDATE_INTERVAL = 500; // 5 sec
    private static int FATEST_INTERVAL = 100; // 1 sec
    private static int DISPLACEMENT = 5; // 10 meters
    private static final String TAG = GeoTaggingActivity.class.getSimpleName();
    ArrayList<GeotaggingBeans> geotaglist = new ArrayList<>();
    ProgressDialog loading;
    Context context;
    Activity activity;
    DownloadAllDatawithRetro upload;
    String app_ver = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_tagging);
        declaration();
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }
        locmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        enabled = locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            // Setting Dialog Title
            alertDialog.setTitle(getResources().getString(R.string.gps));
            // Setting Dialog Message
            alertDialog.setMessage(getResources().getString(R.string.gpsebale));
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton(getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton(getResources().getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            dialog.cancel();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();

        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNetIsAvailable()) {
                    if (!img_str.equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(getString(R.string.parinaam)).setMessage("Are you sure you want to save and upload Geo Tag data ?");
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                status = "N";
                                if (db.InsertSTOREgeotag(storeid, latitude, longitude, img_str, status) > 0) {
                                    img_str = "";
                                    new GeoTagUpload(context).execute();
                                } else {
                                    Snackbar.make(fab, "Error in saving Geotag.", Snackbar.LENGTH_SHORT).show();
                                }
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
                        Snackbar.make(fab, "Please Take Image", Snackbar.LENGTH_SHORT).show();

                    }
                } else {
                    Snackbar.make(fab, "No internet connection !", Snackbar.LENGTH_SHORT).show();

                }
            }
        });
        fabcarmabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _pathforcheck = storeid + "_GeoTag_" + visitData.replace("/", "") + "_" + getCurrentTime().replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                CommonFunctions.startAnncaCameraActivity(context, _path, null, false);

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.notsuppoted), Toast.LENGTH_LONG).show();
                finish();
            }

            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }


    protected void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
        }
    }

    private boolean checkNetIsAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            String result = null;
            LatLng latLng;
            try {
                List<Address> addressList = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                if (addressList != null && addressList.size() > 0) {
                    result = addressList.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                Log.e(TAG, "Unable connect to Geocoder", e);
            }
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(result);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;
            case -1:
                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    try {
                        if (new File(CommonString.FILE_PATH + _pathforcheck).exists()) {
                            String metadata = CommonFunctions.setMetadataAtImages("", storeid, "Store Image", username);
                            CommonFunctions.addMetadataAndTimeStampToImage(this, _path, metadata, visitData);
                            fabcarmabtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.camera_green));
                            fabcarmabtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFE0B2")));
                            img_str = _pathforcheck;
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    public void onLocationChanged(Location location) {

    }


    void declaration() {
        activity = this;
        context = this;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        storename = preferences.getString(CommonString.KEY_STORE_NAME, null);
        visitData = preferences.getString(CommonString.KEY_DATE, "");
        fab = findViewById(R.id.fab);
        fabcarmabtn = findViewById(R.id.camrabtn);
        db = new INTEL_RE_DB(context);
        db.open();
        str = CommonString.FILE_PATH;
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        geocoder = new Geocoder(context);
        upload = new DownloadAllDatawithRetro(context);
        try {
            app_ver = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Crashlytics.logException(e);
        }

        storeid = getIntent().getStringExtra(CommonString.KEY_STORE_ID);
    }


    public class GeoTagUpload extends AsyncTask<Void, Void, String> {
        private Context context;

        GeoTagUpload(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(context, "Processing", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                // uploading Geotag
                uploadflag = false;
                geotaglist = db.getinsertGeotaggingData(storeid, "N");
                if (geotaglist.size() > 0) {
                    JSONArray topUpArray = new JSONArray();
                    for (int j = 0; j < geotaglist.size(); j++) {
                        JSONObject obj = new JSONObject();
                        obj.put(CommonString.KEY_STORE_ID, geotaglist.get(j).getStoreid());
                        obj.put(CommonString.KEY_VISIT_DATE, visitData);
                        obj.put(CommonString.KEY_LATITUDE, geotaglist.get(j).getLatitude());
                        obj.put(CommonString.KEY_LONGITUDE, geotaglist.get(j).getLongitude());
                        obj.put("FRONT_IMAGE", geotaglist.get(j).getImage());
                        topUpArray.put(obj);
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("MID", "0");
                    jsonObject.put("Keys", "GeoTag");
                    jsonObject.put("JsonData", topUpArray.toString());
                    jsonObject.put("UserId", username);

                    String jsonString2 = jsonObject.toString();
                    result = upload.downloadDataUniversal(jsonString2, CommonString.UPLOADJsonDetail);
                    if (result.equalsIgnoreCase(CommonString.MESSAGE_NO_RESPONSE_SERVER)) {
                        uploadflag = false;
                        throw new SocketTimeoutException();
                    } else if (result.equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                        uploadflag = false;
                        throw new IOException();
                    } else if (result.equalsIgnoreCase(CommonString.MESSAGE_INVALID_JSON)) {
                        uploadflag = false;
                        throw new JsonSyntaxException("Primary_Grid_Image");
                    } else if (result.equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                        uploadflag = false;
                        throw new Exception();
                    } else {
                        uploadflag = true;
                    }
                }
            } catch (SocketException ex) {
                uploadflag = false;
                ex.printStackTrace();
                errormsg = CommonString.MESSAGE_INTERNET_NOT_AVALABLE;
            } catch (IOException ex) {
                uploadflag = false;
                ex.printStackTrace();
                errormsg = CommonString.MESSAGE_INTERNET_NOT_AVALABLE;
            } catch (JsonSyntaxException ex) {
                uploadflag = false;
                ex.printStackTrace();
                errormsg = CommonString.MESSAGE_INVALID_JSON;
            } catch (NumberFormatException e) {
                uploadflag = false;
                errormsg = CommonString.MESSAGE_NUMBER_FORMATE_EXEP;
            } catch (Exception ex) {
                uploadflag = false;
                errormsg = CommonString.MESSAGE_EXCEPTION;
            }

            if (uploadflag) {
                return CommonString.KEY_SUCCESS;
            } else {
                return errormsg;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();
            if (result.equals(CommonString.KEY_SUCCESS)) {
                status = "Y";
                db.updateStatus(storeid, status);
                if (db.updateInsertedGeoTagStatus(storeid, status) > 0) {
                    img_str = "";
                    AlertandMessages.showToastMsg(context, "Geotag Saved Successfully.");
                    startActivity(new Intent(context, StoreimageActivity.class));
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                    GeoTaggingActivity.this.finish();
                } else {
                    AlertandMessages.showAlert((Activity) context, "Error in updating Geotag status.", false);
                }
            } else {
                AlertandMessages.showAlert((Activity) context, getResources().getString(R.string.failure) + " : " + errormsg, true);
                GeoTaggingActivity.this.finish();
            }
        }
    }
}
