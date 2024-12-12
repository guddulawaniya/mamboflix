package com.mamboflix.utils;


import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.mamboflix.base.Constantss;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Utility {
    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(true);
        m_Dialog.show();
        return m_Dialog;
    }

    public static int calculateNoOfColumns(Context context, float dimension) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / dimension);
    }

    static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date GetUTCdatetimeAsDate() {
        //note: doesn't check for null
        return StringDateToDate(GetUTCdatetimeAsString());
    }

    public static String GetUTCdatetimeAsString() {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

    public static Date StringDateToDate(String StrDate) {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);

        try {
            dateToReturn = (Date) dateFormat.parse(StrDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateToReturn;
    }


    public static void LoadImage(final Context context, final String url, final ImageView imgView) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Bitmap bitmap =  Glide.with(context).load(url).
                //.(imgView);
            }
        };
        new Thread(runnable).start();
    }


    public static boolean isReadAndWriteStoragePermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //  Log.v(TAG,"Permission is granted1");
                return true;
            }
        } else {
            return true;
        }
        return false;
    }


    public static boolean IsLocationPermissionGranted(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static String getFileDataType(String extesion) {
        String fileDataType = "";
        switch (extesion.toLowerCase()) {
            case Constantss.FileType.PNG:
            case Constantss.FileType.JPG:
            case Constantss.FileType.BMP:
            case Constantss.FileType.JPEG:
                fileDataType = "image/*";
                break;
            case Constantss.FileType.PDF:
                fileDataType = "application/pdf";
                break;
            case Constantss.FileType.DOC:
                fileDataType = "application/msword";

            case Constantss.FileType.DOCX:
                fileDataType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                //"application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                //  "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" // .xls & .xlsx
                break;
            case Constantss.FileType.TXT:
                fileDataType = "text/plain";
                break;
            case Constantss.FileType.MP3:
                fileDataType = "audio/*";
                break;
            case Constantss.FileType.MP4:
                fileDataType = "video/*";
                break;
            default:
                fileDataType = "application/*";
                break;
        }
        return fileDataType;
    }

    public static boolean IsFileExists(String url) {
        URI uri = null;
        try {

            int lastIndex = url.lastIndexOf('/');

            String fileName = url.substring(lastIndex + 1);
            String fileEncodedName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

            String basefilePath = url.substring(0, lastIndex + 1);
            url = basefilePath.concat(fileEncodedName);

            uri = new URI(url);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (uri == null) {
            return false;
        }

        String path = uri.getPath();
        final String fileName = path.substring(path.lastIndexOf('/') + 1);
        File storage = Environment.getExternalStorageDirectory();
        final File file = new File(storage, Constantss.Directory.Base + File.separator + fileName);

        return file.exists();
    }

    public static Uri GetUriFromPath(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String path = uri.getPath();
        final String fileName = path.substring(path.lastIndexOf('/') + 1);
        File storage = Environment.getExternalStorageDirectory();
        final File file = new File(storage, fileName);
        if (file.exists()) {
            Uri fileUri = Uri.fromFile(file);
            return fileUri;
        }
        return null;

    }

    public static String getYoutubeThumbnail(String youtubeUrl) {
//        Uri uri = Uri.parse(youtubeUrl);
//        if(uri == null)
//            return null;
//        String videoID = uri.getQueryParameter("v");

        String videoID = "";

        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youtubeUrl); //url is youtube url for which you want to extract the id.
        if (matcher.find()) {
            videoID = matcher.group();
        }
        if (videoID.equals("")) {
            return null;
        }
        return "http://img.youtube.com/vi/" + videoID + "/mqdefault.jpg";


    }

    public static String Join(String delimiter, Collection<? extends String> words) {
        if (delimiter == null) {
            delimiter = "";
        }
        StringBuilder wordList = new StringBuilder();
        int size = delimiter.length();
        for (String word : words) {
            wordList.append(word + delimiter);
        }
        return new String(wordList.deleteCharAt(wordList.length() - size));
    }

    public static String GetChatTime(long milliseconds) {

        if (milliseconds == 0)
            return "";

        try {
            String dateString;

            Date date = new Date(milliseconds);

            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            //  set the calendar to start of today
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            Date today = c.getTime();
            if (date.before(today)) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                dateString = formatter.format(date);
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
                dateString = formatter.format(date);
            }

            return dateString;
        } catch (Exception ex) {
            return "";
        }
    }

    public static void onRequestPermissionresult(int[] grantResults, final Context context, String contextType, String title) {

        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setMessage(title)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (!Utility.isReadAndWriteStoragePermissionGranted(context)) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                                }
                            }
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog dialog = builder.create();
            dialog.show();

        } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + Constantss.Directory.Base);
            if (!directory.exists()) directory.mkdirs();
            //Log.e("path","<<File path>>"+directory);
        }

    }

    public static String GetFileName(Uri uri) {
        String fileName;

        File file = new File(uri.getPath());
        fileName = file.getName();
        return fileName;
    }

    public static String GetFileName(Uri uri, Context context) {
        String fileName = "";

        if (uri != null) {

            if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
                File file = new File(uri.getPath());
                fileName = file.getName();
            }
            // Content Scheme.
            else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                Cursor returnCursor =
                        context.getContentResolver().query(uri, null, null, null, null);
                if (returnCursor != null && returnCursor.moveToFirst()) {
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

                    fileName = returnCursor.getString(nameIndex);
                    returnCursor.close();
                }
            }
            return fileName;
        }
        return "";
    }

    public static String[] GetFileLength(Uri uri, Context context) {

        float fileSize = 0;
        long fileSizeInByte = 0;
        String[] size = new String[2];

        if (uri != null) {

            if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
                File file = new File(uri.getPath());
                fileSize = file.length();
            }
            // Content Scheme.
            else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                Cursor returnCursor =
                        context.getContentResolver().query(uri, null, null, null, null);
                if (returnCursor != null && returnCursor.moveToFirst()) {
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    fileSizeInByte = returnCursor.getLong(sizeIndex);

                    fileSize = Utility.GetFileLengthInKB(fileSizeInByte);

                    returnCursor.close();
                }
            }

            if (fileSize > 1024) {
                fileSize = Utility.GetFileLengthInMB(fileSizeInByte);
                size[0] = String.valueOf(fileSize);
                size[1] = "MB";
            } else {
                size[0] = String.valueOf(fileSize);
                size[1] = "KB";
            }
            return size;
        }
        return size;
    }

    public static float GetFileLengthInKB(long sizeInByte) {

        float sizeInKB;

        sizeInKB = ((float) Math.round((sizeInByte / (1024)) * 10) / 10);
        return sizeInKB;
    }

    public static float GetFileLengthInMB(long sizeInByte) {

        float sizeInMB;

        sizeInMB = ((float) Math.round((sizeInByte / (1024 * 1024)) * 10) / 10);
        return sizeInMB;
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    public static boolean isYoutubeUrlValid(String url) {

        if (url == null) {
            return false;
        }
        if (URLUtil.isValidUrl(url)) {
            // Check host of url if youtube exists
            Uri uri = Uri.parse(url);
            if ("www.youtube.com".equals(uri.getHost())) {
                return true;
            } else if ("youtu.be".equals(uri.getHost())) {
                return true;
            }
            // Other way You can check into url also like
            //if (url.startsWith("https://www.youtube.com/")) {
            //return true;
            //}
        }
        // In other any case
        return false;
    }

    public static boolean isValidUrl(String url) {

        if (url == null) {
            return false;
        }

        if (URLUtil.isValidUrl(url)) {
            return true;
        }

        return false;
    }


    public static void ShowUpdateAppWindow(final Context context) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("Latest Update Available!");
        builder.setMessage("Your app is outdated. Please update your app to get latest features.");

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OpenAppInPlayStore(context);
            }
        });

        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public static void OpenAppInPlayStore(Context context) {
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        Intent intent;
        Uri uri;

        try {
            uri = Uri.parse("market://details?id=" + appPackageName);
        } catch (android.content.ActivityNotFoundException anfe) {
            uri = Uri.parse(Constantss.PLAYSTORE_URL + appPackageName);
        }

        intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String getRealPathFromURI(Uri contentUri, Context mContext) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(mContext, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getRealPathFromURIApi19(final Uri uri, final Context context) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            System.out.println("getPath() uri: " + uri.toString());
            System.out.println("getPath() uri authority: " + uri.getAuthority());
            System.out.println("getPath() uri path: " + uri.getPath());

            // ExternalStorageProvider
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                System.out.println("getPath() docId: " + docId + ", split: " + split.length + ", type: " + type);

                // This is for checking Main Memory
                if ("primary".equalsIgnoreCase(type)) {
                    if (split.length > 1) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1] + "/";
                    } else {
                        return Environment.getExternalStorageDirectory() + "/";
                    }
                    // This is for checking SD Card
                } else {
                    return "storage" + "/" + docId.replace(":", "/");
                }

            }
        }
        return null;
    }

    public static int[] GetResolutionOfImage(Context context, Uri uri) throws FileNotFoundException {
        int[] resolutionArr = new int[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(uri),
                null,
                options);
      /*  int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;*/
        resolutionArr[0] = options.outHeight;
        resolutionArr[1] = options.outWidth;
        return resolutionArr;
    }

    public static void ClearCachefromGlide(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
    }

    public static File CreateFileName(int studentId, Context context) {
        String fileName = "img_" + studentId + ".jpg";
        return new File(context.getFilesDir(), fileName);
    }

    public static File GetFileFromPath(Uri croppedImageUri, Context context) {
        String path;
        File file;
        /* path = RealPathUtil.getRealPath(context, croppedImageUri);*/
        path = RealPathFromUri.GetRealPath(context, croppedImageUri);

        if (path == null || path.equals("")) {
            Toast.makeText(context, "Something went wrong.File can not be uploaded.", Toast.LENGTH_SHORT).show();
            return null;
        }
        file = new File(path);
        return file;
    }

    public static Uri GetImageUriFromBitmap(Context context, Bitmap inImage) {
        String path = "";
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);

        } catch (Exception ex) {
            Log.e("ImageUri From Bitmap", ex.getMessage());
        }

        return Uri.parse(path);
    }

    public static Uri GetUriFromIntent(Intent data, Context context) {
        Uri croppedImageUri = null;

        try {
            croppedImageUri = null;

            Bundle bundle = data.getExtras();
            if (bundle != null) {
                if (bundle.getParcelable("data") != null) {

                    Bitmap bitmapPic = bundle.getParcelable("data");
                    if (bitmapPic != null) {
                        croppedImageUri = GetImageUriFromBitmap(context, bitmapPic);
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Uri from intent", ex.getMessage());
        }

        return croppedImageUri;
    }

    /*public static void SetToolbar(Context context, String title, String subTitle) {
        androidx.appcompat.app.ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setSubtitle(subTitle);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(context.getResources().getDrawable(R.drawable.ic_back_arrow_sm));
        }
    }

    public static void SaveInSqlite(Context context, InputModel model) {
        *//*DbOperations dbOperations = new DbOperations(context);
        dbOperations.AddItem(model);*//*
    }

    public static void ShareAppLink(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object

        try {
            intent.putExtra(Intent.EXTRA_TEXT, Constantss.PLAYSTORE_URL + appPackageName);
            context.startActivity(Intent.createChooser(intent, "Share app via"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int CountDays(Context context, long edateInMilisec, long sdateInMilisec) {
        long diff = edateInMilisec - sdateInMilisec;
        return (int) (diff / (24 * 60 * 60 * 1000));
    }

   *//* public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }*//*

    public static String GetDeviceScreenResolution(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x; //device width
        int height = size.y; //device height

        return "" + width + " x " + height; //example "480 * 800"
    }

    public static String CheckBluetoothConnection() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return "Not supported";
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                return "Disabled";
            } else {
                return "Enabled";
            }
        }
    }

    public static Double[] GetLocation(Context context) {
        LocationManager locationManager;
        LocationListener locationListener;
        Location location;
        final Double[] latlong = new Double[2];

        // Acquire a reference to the system LocationTrack Manager
        locationManager = (LocationManager) (context).getSystemService(Context.LOCATION_SERVICE);


// Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                if (location != null) {
                    latlong[0] = location.getLatitude();
                    latlong[1] = location.getLongitude();
                }
                //makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };


        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, Constantss.PermissionRequestCode.LOCATION);
            // return;
        }

        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latlong[0] = location.getLatitude();
                latlong[1] = location.getLongitude();
            }
        }

        //locationManager.removeUpdates(locationListener);
        return latlong;
    }

    public static Double[] GetCurrentLocation(Context context) {
        final Double[] latlong = new Double[2];
        LocationTrack locationTrack = new LocationTrack(context);

        if (locationTrack.canGetLocation()) {
            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();

            latlong[0] = latitude;
            latlong[1] = longitude;
            // Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        } else {

            locationTrack.showSettingsAlert();
        }
        return latlong;
    }*/

    public static String isWifiNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return Constantss.ConnectivityType.WIFI;
        } else return isMobileNetworkAvailable(context);
    }

    public static String isMobileNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return Constantss.ConnectivityType.MOBILE_DATA;
        }
        return "";
    }

    /*public static void ShowTapTargetView(Context context, View view, String msg1, String msg2, float alpha, int targetCircleColor) {
        if (alpha != 0) {
            TapTargetView.showFor((Activity) context, TapTarget.forView(view, msg1, msg2)
                            .tintTarget(false)
                            .outerCircleColor(R.color.orange)
                            .outerCircleAlpha(alpha)
                    // .icon(context.getResources().getDrawable(R.drawable.arrow_tilt))
                    *//*.targetRadius(110)*//*
            );

        } else {
            TapTargetView.showFor((Activity) context, TapTarget.forView(view, msg1, msg2)
                            .tintTarget(false)
                            .outerCircleColor(R.color.orange)
                            .targetCircleColor(targetCircleColor)
                    // .icon(context.getResources().getDrawable(R.drawable.arrow_tilt))
                    // .targetRadius(80)
            );
        }

    }*/

    public static void GetLocationPermissn(Context context) {
        if (!Utility.IsLocationPermissionGranted(context)) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constantss.PermissionRequestCode.LOCATION);
        }
    }

    public static ContextWrapper changeLang(Context context, String lang_code) {
        Locale sysLocale;

        Resources rs = context.getResources();
        Configuration config = rs.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = config.getLocales().get(0);
        } else {
            sysLocale = config.locale;
        }
        if (!lang_code.equals("") && !sysLocale.getLanguage().equals(lang_code)) {
            Locale locale = new Locale(lang_code);
            Locale.setDefault(locale);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale);
            } else {
                config.locale = locale;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                context = context.createConfigurationContext(config);
            } else {
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            }
        }

        return new ContextWrapper(context);
    }

    public static void SetAlarm(int operationType, Context context, PendingIntent pi, long trigerAtMillis, long intervalMillis) {
        try {
            if (!((Activity) context).isFinishing()) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {

                    switch (operationType) {
                        case Constantss.Alarm.START:
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, trigerAtMillis, intervalMillis, pi);
                            break;

                        case Constantss.Alarm.CANCEL:
                            alarmManager.cancel(pi);
                            break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void ShareApplication(Context context) {
        ApplicationInfo app = context.getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
        intent.setType("*/*");

        // Append file and send Intent
        File originalApk = new File(filePath);

        try {
            //Make new directory in new location
            File tempFile = new File(context.getExternalCacheDir() + "/ExtractedApk");
            //If directory doesn't exists create new
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            //Get application's name and convert to lowercase
            tempFile = new File(tempFile.getPath() + "/" + context.getString(app.labelRes).replace(" ", "").toLowerCase() + ".apk");
            //If file doesn't exists create new
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            //Copy file to new location
            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            //Open share dialog
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            context.startActivity(Intent.createChooser(intent, "Share app via"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int GetRandomMaterialColor(String typeColor, Context mContext) {
        int returnColor = Color.GRAY;
        int arrayId = mContext.getResources().getIdentifier(typeColor, "array", mContext.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = mContext.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    public static String GetLocationNameFromLatLong(String latitude, String longitude, Context context) throws IOException {
        Geocoder myLocation = new Geocoder(context, Locale.getDefault());
        List<Address> myList = myLocation.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
        Address address = (Address) myList.get(0);
        String addressStr = "";
        addressStr += address.getAddressLine(0);
        return addressStr;
    }

    public static void CloseKeyboard(Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    /*public static void ShowChart(AnyChartView chartView, String chartTitle, String xAxisTitle, String yAxisTitle, List<DataEntry> dataEntry) {
        *//* final AnyChartView chartView = bottomSheet.findViewById(R.id.chartView);*//*
        *//*chartView.setProgressBar(bottomSheet.findViewById(R.id.pb));*//*
        //chartView.clear();
        //   chartView.clear();

        Cartesian cartesian = AnyChart.column();
        //cartesian.removeAllSeries();

        Column column = cartesian.column(dataEntry);


        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title(chartTitle);

        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

       *//* cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);*//*

        cartesian.xAxis(0).title(xAxisTitle);
        cartesian.yAxis(0).title(yAxisTitle);

        // format labels
      *//*  cartesian.xAxis(0).labels().width(45);
        cartesian.xAxis(0).labels().height(50);
        cartesian.xAxis(0).labels().textOverflow(com.anychart.graphics.vector.text.TextOverflow.ELLIPSIS);*//*
        LabelsFactory xaxis = cartesian.xAxis(0).labels();
        xaxis.rotation(90);
        cartesian.xScroller(true);

        chartView.refreshDrawableState();
        chartView.invalidate();

        chartView.setChart(cartesian);

       *//* List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("1990", 10, 40 ));
        seriesData.add(new CustomDataEntry("1991", 40, 5 ));
        seriesData.add(new CustomDataEntry("1992", 80, 50 ));
        seriesData.add(new CustomDataEntry("1993", 80, 20));
        seriesData.add(new CustomDataEntry("1994", 40, 80));
        seriesData.add(new CustomDataEntry("1995", 15, 50));

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");
        //Mapping series3Data = set.mapAs("{ x: 'x', value: 'value3' }");

        Column series1 = cartesian.column(series1Data);
        series1.name("Registration")
                .color("#00ff00");

        Column series2 = cartesian.column(series2Data);
        series2.name("Admission")
                .color("#0000ff");

        cartesian.xAxis(0).orientation(Orientation.TOP)
                .stroke(null)
                .ticks(false);
        cartesian.xGrid(0).enabled(true);

        cartesian.legend(true);
        cartesian.legend()
                .position(Orientation.RIGHT)
                .itemsLayout(LegendLayout.VERTICAL);

        chartView.setChart(cartesian);*//*
    }*/

   /* private static class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
            //setValue("value3", value3);
        }

    }*/

    public static String GetFormattedDateMDY(Date dateObj) {
        SimpleDateFormat df_mdy = new SimpleDateFormat("MM/dd/yyyy");
        return df_mdy.format(dateObj);
    }

    public static String GetFormattedDateDMY(String dateStr) throws ParseException {
        Date dateObj= new SimpleDateFormat("MM/dd/yyyy").parse(dateStr);

        SimpleDateFormat df_mdy = new SimpleDateFormat("dd MMM, yyyy");
        return df_mdy.format(dateObj);
    }


    public static String GetFormattedTimeHMS(Date dateObj) {
        SimpleDateFormat df_hms = new SimpleDateFormat("HH:mm:ss");
        return df_hms.format(dateObj);
    }

    public static String GetFormattedDateDM(String dateStr) throws ParseException {
        Date dateObj= new SimpleDateFormat("MM/dd/yyyy").parse(dateStr);

        SimpleDateFormat df_mdy = new SimpleDateFormat("dd MMM");
        return df_mdy.format(dateObj);
    }
}


