package com.mamboflix.utils;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.mamboflix.base.Constantss;
import com.mamboflix.utils.videoDownloadUtils.DownloadListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

import static com.mamboflix.base.Constantss.FILE_AUTHORITY;


public class FileDownloader {

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private ProgressDialog progressDialog;

    static int downloadedSize = 0, totalsize;
    static float per = 0;
    private static final int MEGABYTE = 1024 * 1024;

    private File _download(String fileUrl, String directory) {

        File file = null;
        try {

            URL url = new URL(fileUrl.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
            //   urlConnection.setDoOutput(true);
            // connect
            urlConnection.connect();
            // set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            file = new File(SDCardRoot, directory);
            FileOutputStream fileOutput = new FileOutputStream(file);
            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();
            // this is the total size of the file which we are
            // downloading
            int totalsize = urlConnection.getContentLength();


            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;

            long total = 0;
            boolean downloadComplete = false;

            while ((bufferLength = inputStream.read(buffer))>0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                per = ((float) downloadedSize / totalsize) * 100;
             /*   setText("Total PDF File size  : "
                        + (totalsize / 1024)
                        + " KB\n\nDownloading PDF " + (int) per
                        + "% complete");*/
                total += bufferLength;

                int progress = (int) ((double) (total * 100) / (double) totalsize);
                //updateNotification(progress);

                downloadComplete = true;
             Log.e("Total PDF File size  :","" +totalsize / 1024
                     + " KB\n\nDownloading PDF " + (int) per
                     + "% complete");
            }
            // close the output stream when complete //
            fileOutput.close();
            //setText("Download Complete. Open PDF Application installed in the device.");
        } catch (final MalformedURLException e) {
            /*setTextError("Some error occured. Press back and try again.",
                    Color.RED);*/
        } catch (final IOException e) {
            String msg = e.getMessage();
         /*   setTextError("Some error occured. Press back and try again.",
                    Color.RED);*/
        } catch (final Exception e) {
          /*  setTextError(
                    "Failed to download image. Please check your internet connection.",
                    Color.RED);*/
        }
        return file;

    }

    public void DownloadAndOpen(final Activity mContext, String download_file_url, final String fileExtension, DownloadListener downloadListener) {
        URI uri = null;

        try {

            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + Constantss.Directory.Base);
            if (!directory.exists()){
                directory.mkdirs();
                Log.e("directory type", "<<if>>"+directory);
            }else {
                Log.e("directory type", "<<else>>"+directory);
            }

            int fileIndex = download_file_url.lastIndexOf('.');
            //String extensionName = download_file_url.substring(fileIndex + 1);

            String extensionName  = fileExtension;

            Log.e("file type", "<<Extension>>"+extensionName);

            int lastIndex = download_file_url.lastIndexOf('/');

            String fileName = download_file_url.substring(lastIndex + 1);
            String fileEncodedName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

            String basefilePath = download_file_url.substring(0, lastIndex + 1);
            download_file_url = basefilePath.concat(fileEncodedName);
            uri = new URI(download_file_url);

            File storage = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            final File file = new File(storage, Constantss.Directory.Base + File.separator + fileName);
            String fileDataType = Utility.getFileDataType(extensionName);
            //String fileDataType = Utility.getFileDataType(fileExtension);
            if (!file.exists()) {

                Log.e("file type", "<<Download>>"+file);
                new DownloadFileTask(downloadListener,mContext).execute(download_file_url, fileDataType);
            } else {
                _open(mContext, file, fileDataType);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (uri == null) {
            return;
        }

    }

    private void _open(Activity mContext, File file, String fileDataType) {

        Log.e("file type", "<<Open file>>"+fileDataType+"file>>"+file);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(mContext, FILE_AUTHORITY, file);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(contentUri, fileDataType);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                mContext.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(mContext, "Please click again to view the file", Toast.LENGTH_LONG).show();
            }

        } else {
            Uri path1 = Uri.fromFile(file);
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path1, fileDataType);
                mContext.startActivity(intent);
            } catch (ActivityNotFoundException e) {
            }
        }
    }


    private class DownloadFileTask extends AsyncTask<String, Void, File> {
        public ProgressDialog dialog;
        String dataType;
        DownloadListener downloadListner;
        Activity mContext;

        public DownloadFileTask(DownloadListener downloadListner, Activity mContext) {
            this.downloadListner = downloadListner;
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
//            downloadListner.onDownloadStart();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Downloading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected File doInBackground(String... params) {
            File file = null;
            try {
                String fileUrl = params[0];
                dataType = params[1];
                URI uri = new URI(fileUrl);
                String path = uri.getPath();
                String fileName = path.substring(path.lastIndexOf('/') + 1);

                DownNotification(mContext);
                file = _download(fileUrl, Constantss.Directory.Base + File.separator + fileName);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return file;
        }

        @Override
        protected void onPostExecute(File file) {
        //    downloadListner.onDownloadComplete();
            progressDialog.dismiss();
        }
    }



    public void DownNotification(Context context){
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("id", "an", NotificationManager.IMPORTANCE_LOW);

            notificationChannel.setDescription("no sound");
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        notificationBuilder = new NotificationCompat.Builder(context, "id")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle("Download")
                .setContentText("Downloading Image")
                .setDefaults(0)
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());
    }


    private void updateNotification(int currentProgress) {


        notificationBuilder.setProgress(100, currentProgress, false);
        notificationBuilder.setContentText("Downloaded: " + currentProgress + "%");
        notificationManager.notify(0, notificationBuilder.build());
    }

}



