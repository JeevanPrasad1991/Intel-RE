/*
package intelre.cpm.com.intelre.upload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.constant.AlertandMessages;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.retrofit.PostApiForUpload;
import intelre.cpm.com.intelre.retrofit.StringConverterFactory;
import retrofit.Retrofit;

*/
/**
 * Created by jeevanp on 5/29/2018.
 *//*


public class UploadImageWithRetrofitOne {
    RequestBody body1;
    PostApiForUpload api;
    retrofit.Call<String> call;
    private Retrofit adapter;
    int status = 0;
    int count = 0;
    public static int uploadedFiles = 0;
    public static int totalFiles = 0;
    boolean isvalid = false, statusUpdated = true;
    Context context;
    String visitDate, userID, uploadStatus;
    int storeId = 0;
    INTEL_RE_DB db;
    ProgressDialog pd;
    ArrayList<JourneyPlan> storeList, storeList_deviation;

    public UploadImageWithRetrofitOne(Context context) {
        this.context = context;
        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setTitle("Please wait");
        pd.setMessage("Uploading images");
        pd.show();
    }

    public UploadImageWithRetrofitOne() {
    }

    public UploadImageWithRetrofitOne(Context context, String msg) {
        this.context = context;
        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setTitle("Please wait");
        pd.setMessage(msg);
        pd.show();
    }

    public void upload_image(final Context context) {
        try {
            status = 0;
            String filename = null, foldername = null;
            int totalfiles = 0;
            File f = new File(CommonString.FILE_PATH);
            File file[] = f.listFiles();
            count = file.length;
            if (file.length > 0) {
                filename = "";
                totalfiles = f.listFiles().length;
                pd.setMessage("Uploading images" + "(" + uploadedFiles + "/" + totalFiles + ")");
                for (int i = 0; i < file.length; i++) {
                    if (new File(CommonString.FILE_PATH + file[i].getName()).exists()) {
                        if (file[i].getName().contains("_StoreImg-")) {
                            foldername = "Coverage";
                        }
                        if (file[i].getName().contains("_STOREIMG_") || file[i].getName().contains("_NONWORKING_") || file[i].getName().contains("_STOREC_OUTIMG_")) {
                            foldername = "CoverageImages";
                        } else if (file[i].getName().contains("_AUDITIMG_")) {
                            foldername = "AuditImages";
                        } else if (file[i].getName().contains("_AUDITIMG_")) {
                            foldername = "AuditImages";
                        } else if (file[i].getName().contains("_SOFTMERCHIMG_")) {
                            foldername = "SoftPosmImages";
                        } else if (file[i].getName().contains("_SEMIPMERCHIMG_ONE_") || file[i].getName().contains("_SEMIPMERCHIMG_TWO_") || file[i].getName().contains("_SEMIPMERCHIMG_THREE_")) {
                            foldername = "PermanantPosmImages";
                        } else if (file[i].getName().contains("_IPOSING_")) {
                            foldername = "IPOSImages";
                        } else if (file[i].getName().contains("_RXTING_")) {
                            foldername = "RXTImages";
                        } else if (file[i].getName().contains("_MARKETINFOING_")) {
                            foldername = "MarketInfoImages";
                        } else if (file[i].getName().contains("_TRAININGIMG_")) {
                            foldername = "TrainingImages";
                        } else if (file[i].getName().contains("_GeoTag_")) {
                            foldername = "GeoTagImages";
                        }
                        filename = file[i].getName();
                    }
                    break;
                }


                File originalFile = new File(CommonString.FILE_PATH + filename);
                final File finalFile = saveBitmapToFileSmaller(originalFile);
                String date;
                if (filename.contains("-")) {
                    date = getParsedDate(filename);
                } else {
                    date = visitDate;
                }

                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
                okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
                okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);

                com.squareup.okhttp.RequestBody photo = com.squareup.okhttp.RequestBody.
                        create(com.squareup.okhttp.MediaType.parse("application/octet-stream"), finalFile);
                    body1 = new MultipartBuilder()
                            .type(MultipartBuilder.FORM)
                            .addFormDataPart("file", finalFile.getName(), photo)
                            .addFormDataPart("FolderName", foldername)
                            .addFormDataPart("Path", date)
                            .build();

                    adapter = new retrofit.Retrofit.Builder()
                            .baseUrl(CommonString.URLGORIMAG)
                            .client(okHttpClient)
                            .addConverterFactory(new StringConverterFactory())
                            .build();
                    api = adapter.create(PostApiForUpload.class);
                    call = api.getUploadImageRetrofitOne(body1);

                call.enqueue(new retrofit.Callback<String>() {
                    @Override
                    public void onResponse(retrofit.Response<String> response) {
                        if (response.isSuccess() && response.body().contains("Success")) {
                            finalFile.delete();
                            status = 1;
                            uploadedFiles++;
                        } else {
                            status = 0;
                            //uploadedFiles = 0;
                        }
                        if (status == 0) {
                            pd.dismiss();
                            if (!((Activity) context).isFinishing()) {
                                AlertandMessages.showAlert((Activity) context, "Image not uploaded." + "\n" + uploadedFiles + " images uploaded out of " + totalFiles, true);
                            }
                        } else {
                            upload_image(context);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (t instanceof IOException || t instanceof SocketTimeoutException || t instanceof SocketException) {
                            status = -1;
                            //uploadedFiles = 0;
                            pd.dismiss();
                            // AlertandMessages.showAlert((Activity) context, "Network Error in upload", false);
                            if (!((Activity) context).isFinishing()) {
                                AlertandMessages.showAlert((Activity) context, "Network Error in upload." + "\n" + uploadedFiles + " images uploaded out of " + totalFiles, true);
                            } else {

                            }
                        }
                    }
                });

            } else {
                if (totalFiles == uploadedFiles) {
                    //region Coverage upload status Data
                    //new UploadImageWithRetrofit.StatusUpload().execute();
                    //endregion
                }

            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INVALID_JSON, true);
        } catch (Exception e) {
            e.printStackTrace();
            if (totalFiles == uploadedFiles && !statusUpdated) {
                AlertandMessages.showAlert((Activity) context, "All images uploaded but status not updated", true);
            } else {
                AlertandMessages.showAlert((Activity) context, CommonString.KEY_FAILURE, true);
            }
        }

    }
    public File saveBitmapToFileSmaller(File file) {
        File file2 = file;
        try {
            int inWidth = 0;
            int inHeight = 0;

            InputStream in = new FileInputStream(file2);
            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            in = null;

            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;

            // decode full image pre-resized
            in = new FileInputStream(file2);
            options = new BitmapFactory.Options();
            // calc rought re-size (this is no exact resize)
            options.inSampleSize = Math.max(inWidth / 800, inHeight / 500);
            // decode full image
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

            // calc exact destination size
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, 800, 500);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);
            // resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);
            // save image
            FileOutputStream out = new FileOutputStream(file2);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

        } catch (Exception e) {
            Log.e("Image", e.toString(), e);
            return file;
        }
        return file2;
    }

    String getParsedDate(String filename) {
        String testfilename = filename;
        testfilename = testfilename.substring(testfilename.indexOf("-") + 1);
        testfilename = testfilename.substring(0, testfilename.indexOf("-"));
        return testfilename;
    }
}
*/
