package com.demo.fcmnotificationsdemo;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

  private static final String TAG = "MyFirebaseMessagingService";
  private static final String CHANNEL_ID = "FcmDemo";
  private static final int NOTIFICATION_ID = 100;

  @SuppressLint("LongLogTag") @Override
  public void onNewToken(String token) {
    Log.d(TAG, "Refreshed token: " + token);

    // If you want to send messages to this application instance or
    // manage this apps subscriptions on the server side, send the
    // Instance ID token to your app server.
    //sendRegistrationToServer(token);
  }

  @SuppressLint("LongLogTag") @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    // ...

    // TODO(developer): Handle FCM messages here.
    // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
    Log.d(TAG, "From: " + remoteMessage.getFrom());

    // Check if message contains a data payload.
    if (remoteMessage.getData().size() > 0) {
      Log.d(TAG, "Message data payload: " + remoteMessage.getData());
    }

    // Check if message contains a notification payload.
    if (remoteMessage.getNotification() != null) {
      Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
      Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());

      createNotificationChannel();

      NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
          .setSmallIcon(R.drawable.ic_launcher_foreground)
          .setContentTitle(remoteMessage.getNotification().getTitle())
          .setContentText(remoteMessage.getNotification().getBody())
          .setPriority(NotificationCompat.PRIORITY_DEFAULT);

      NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

      // notificationId is a unique int for each notification that you must define
      notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
  }

  private void createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        //using same string for both channelid and name. ideally use different strings
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }
}
