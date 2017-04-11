/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shivshankar;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.Config;
import com.shivshankar.utills.commonVariables;

import java.net.URL;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static int NOTIFICATION_ID = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        String message = "", title = "", strImageURL = "";
        if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            message = "" + remoteMessage.getData().get(Config.MESSAGE_KEY);
            title = "" + remoteMessage.getData().get(Config.TITLE_KEY);
            strImageURL = "" + remoteMessage.getData().get(Config.IMAGE_KEY);
        } else if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            message = "" + remoteMessage.getNotification().getBody();
            title = "" + remoteMessage.getNotification().getTitle();
            strImageURL = "" + remoteMessage.getNotification().getIcon();
        }

        if (strImageURL != null && !strImageURL.equals("") && !strImageURL.equals("null")) {
            Handler handler = new Handler(Looper.getMainLooper());
            final String finalTitle = title;
            final String finalMessage = message;
            final String finalStrImageURL = strImageURL;
            handler.post(new Runnable() {
                public void run() {
                    new ServerAPICallImageBitmap(finalTitle, finalMessage, finalStrImageURL, "").execute();
                }
            });
        } else if (title.equalsIgnoreCase("Logout")) {
            try {
                SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                editor.putString(commonVariables.KEY_LOGIN_ID, "0");
                editor.putBoolean(commonVariables.KEY_IS_LOG_IN, false);
                editor.putString(commonVariables.KEY_SELLER_PROFILE, "");
                editor.putString(commonVariables.KEY_BUYER_PROFILE, "");
                editor.putString(commonVariables.KEY_BRAND, "");
                editor.putBoolean(commonVariables.KEY_IS_BRAND, false);
                editor.putBoolean(commonVariables.KEY_IS_SELLER, false);
                editor.putBoolean(commonVariables.KEY_IS_SKIPPED_LOGIN_BUYER, false);
                editor.commit();
                android.os.Process.killProcess(android.os.Process.myPid());
                List<ApplicationInfo> packages;
                PackageManager pm;
                pm = getPackageManager();
                packages = pm.getInstalledApplications(0);

                ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                String myPackage = getApplicationContext().getPackageName();
                for (ApplicationInfo packageInfo : packages) {
                    if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) continue;
                    if (packageInfo.packageName.equals(myPackage)) continue;
                    mActivityManager.killBackgroundProcesses(packageInfo.packageName);
                }
                mActivityManager.restartPackage(myPackage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            sendNotification(title, message);
        Log.i(TAG, "Received: " + remoteMessage.toString());
    }

    public class ServerAPICallImageBitmap extends AsyncTask<String, String, Bitmap> {

        String msg = "", image_URL = null, title = "";

        public ServerAPICallImageBitmap(String strTitle, String message, String url, String strLoadHTML) {
            msg = message;
            image_URL = url;
            title = strTitle;

        }

        @Override
        protected Bitmap doInBackground(String... params) {

            URL url_value;
            Bitmap mIcon1 = null;
            if (image_URL == null)
                return null;
            else
                try {
                    url_value = new URL(image_URL);
                    mIcon1 = BitmapFactory.decodeStream(url_value.openConnection().getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            return mIcon1;
        }

        @Override
        protected void onPostExecute(final Bitmap result) {
            super.onPostExecute(result);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyFirebaseMessagingService.this);
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_noti_fcm);
            Intent intent;
            if (AppPreferences.getPrefs().getBoolean(commonVariables.KEY_IS_SELLER, false)) {
                stackBuilder.addParentStack(MainActivitySeller.class);
                intent = new Intent(MyFirebaseMessagingService.this, MainActivitySeller.class);
            } else {
                stackBuilder.addParentStack(MainActivityBuyer.class);
                intent = new Intent(MyFirebaseMessagingService.this, MainActivityBuyer.class);
            }
            stackBuilder.addNextIntent(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            int requestID = (int) System.currentTimeMillis();
            PendingIntent contentIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this);
            builder.setSmallIcon(R.drawable.ic_noti_fcm);
            builder.setLargeIcon(logo);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
            builder.setContentTitle(title);
            builder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            builder.setContentText(msg);
            builder.setVibrate(new long[]{100, 100, 0, 0});
            builder.setPriority(NotificationCompat.PRIORITY_MAX);
            builder.setWhen(0);
            builder.setContentIntent(contentIntent);

            if (result != null) {
                NotificationCompat.BigPictureStyle bigPicutureStyle = new NotificationCompat.BigPictureStyle(builder);
                bigPicutureStyle.bigLargeIcon(logo);
                bigPicutureStyle.bigPicture(result);
                bigPicutureStyle.setBigContentTitle(title);
                bigPicutureStyle.setSummaryText(msg);
                builder.setStyle(bigPicutureStyle);
            }

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID++, builder.build());

            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNotification(String strTitle, String strMsg) {
        Log.d("TAG", "Preparing to send notification...: " + strTitle + "  Msg: " + strMsg);
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent;
        if (AppPreferences.getPrefs().getBoolean(commonVariables.KEY_IS_SELLER, false))
            intent = new Intent(this, MainActivitySeller.class);
        else
            intent = new Intent(this, MainActivityBuyer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int requestID = (int) System.currentTimeMillis();
        PendingIntent contentIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(strTitle)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(strMsg))
                .setContentText(strMsg)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setWhen(0)
                .setVibrate(new long[]{100, 100, 0, 0});

        mBuilder.setSmallIcon(R.drawable.ic_noti_fcm);
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mBuilder.setAutoCancel(true);
        if (!strMsg.contains("Your OTP is"))
            mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());
        Log.d("TAG", "Notification sent successfully.");
    }

}
