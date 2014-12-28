package com.example.wojtekswiderski.woahpaper;

/**
 * Created by wojtekswiderski on 14-12-13.
 */
/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 import com.google.android.gms.gcm.GoogleCloudMessaging;

 import android.app.IntentService;
 import android.app.NotificationManager;
 import android.app.WallpaperManager;
 import android.content.Context;
 import android.content.Intent;
 import android.graphics.Bitmap;
 import android.graphics.BitmapFactory;
 import android.graphics.Canvas;
 import android.graphics.Matrix;
 import android.os.Bundle;
 import android.support.v4.app.NotificationCompat;
 import android.util.DisplayMetrics;
 import android.util.Log;
 import org.json.JSONException;
 import org.json.JSONObject;
 import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.net.HttpURLConnection;
 import java.net.MalformedURLException;
 import java.net.URL;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "Woahpaper";
    public String word;
    public String sender;
    public final int MAXRESULTS = 64;

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        word = extras.get("word").toString();
        sender = extras.get("sender").toString();

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                int results = numberResults();

                if(results > MAXRESULTS){
                    int start;
                    int i = 0;
                    do{
                        start = (int) (Math.random() * MAXRESULTS);
                        i++;
                    }while(setWallPaper(start) && i <= 10);
                }else{
                    int i = 0;
                    do{
                        i++;
                    }while(setWallPaper(i) && i <= 10);
                }

                sendNotification("Received " + word.substring(0,1).toUpperCase() + word.substring(1) + " from " + sender.substring(0,1).toUpperCase() + sender.substring(1));
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.wp_logo)
                        .setContentTitle("WoahPaper")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public int numberResults(){
        if(word.isEmpty()){
            Log.e(TAG, "No word");
            return 0;
        }
        int numResults;
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=1&q=" + word;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject deliverable;

            try{
                deliverable = new JSONObject(response.toString());
                numResults = Integer.parseInt(deliverable.getJSONObject("responseData").getJSONObject("cursor").getString("estimatedResultCount"));
                Log.i(TAG, "Number of results: " + numResults);
                return numResults;
            }catch(JSONException ex) {
                Log.e(TAG, "Could not convert to object");
                ex.printStackTrace();
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Log.e(TAG, "Wrong url");
        } catch (IOException ey) {
            ey.printStackTrace();
            Log.e(TAG, "Server down");
        }
        return 0;
    }
    public boolean setWallPaper(int start){
        String size;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int minDimension;

        if(height >= width){
            minDimension = width;
        }else{
            minDimension = height;
        }

        if(minDimension >= 720){
            size = "huge";
        }else{
            size = "xxlarge";
        }

        Log.i(TAG, size);

        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=1&q=" + word + "&start=" + start + "&imgsz=" + size;
        String imageUrl;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject deliverable;

            try{
                deliverable = new JSONObject(response.toString());
                imageUrl = deliverable.getJSONObject("responseData").getJSONArray("results").getJSONObject(0).getString("url");
                Log.i(TAG, imageUrl);
                URL imageObj = new URL(imageUrl);

                Bitmap bmp = BitmapFactory.decodeStream(imageObj.openConnection().getInputStream());

                int x = bmp.getWidth();
                int y = bmp.getHeight();
                double ratioX = ((double) x) / ((double) width);
                double ratioY = ((double) y) / ((double) height);

                Log.i(TAG, "Width: " + width + " Height: " + height);
                Log.i(TAG, "X: " + x + " Y: " + y);
                Log.i(TAG, "RatioX: " + ratioX + " RatioY: " + ratioY);

                if(ratioX > ratioY){
                    bmp = Bitmap.createScaledBitmap(bmp, (int) (((double) x) / ratioY), height, false);
                }else{
                    bmp = Bitmap.createScaledBitmap(bmp, width, (int) (((double) y) / ratioX), false);
                }

                Log.i(TAG, "newX: " + bmp.getWidth() + " newY: " + bmp.getHeight());

                Bitmap bmpBack = Bitmap.createBitmap(getWallpaperDesiredMinimumWidth(), getWallpaperDesiredMinimumHeight(), Bitmap.Config.ARGB_8888);
                Bitmap bmOverlay = Bitmap.createBitmap(bmpBack.getWidth(), bmpBack.getHeight(), bmpBack.getConfig());

                Canvas canvas = new Canvas(bmOverlay);
                canvas.drawBitmap(bmpBack, new Matrix(), null);
                canvas.drawBitmap(bmp, 0, 0, null);

                WallpaperManager wpm = WallpaperManager.getInstance(this);
                wpm.setBitmap(bmOverlay);
            }catch(JSONException ex) {
                Log.e(TAG, "Could not convert to object");
                ex.printStackTrace();
                return true;
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Log.e(TAG, "Wrong url");
            return true;
        } catch (IOException ey) {
            ey.printStackTrace();
            Log.e(TAG, "Server down");
            return true;
        }
        return false;
    }
}