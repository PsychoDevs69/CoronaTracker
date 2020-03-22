package psycho.developers.coronatracker.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import psycho.developers.coronatracker.R;
import psycho.developers.coronatracker.activity.MainActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final static String TAG = "FCM";
    Intent resultIntent;
    PendingIntent pendingIntent;
    String CHANNEL_ID = "psychoDevs_GeneralNotification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Log.d(TAG, "From: " + remoteMessage.getFrom());

            resultIntent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
            pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this, CHANNEL_ID)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setSmallIcon(R.drawable.corona_icon_svg)
                        .setAutoCancel(true)
                        .setColorized(true)
                        .setColor(getResources().getColor(R.color.colorPrimary))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVibrate(new long[]{100, 1000, 100, 1000, 100})
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()))
                        .setContentIntent(pendingIntent);

                createNotificationChannel(builder);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createNotificationChannel(NotificationCompat.Builder builder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "General", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("General Notification");
            channel.setShowBadge(true);


            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(12, builder.build());
        } else {
            // THIS IS THE WAY TO USE SYSTEM SERVICE BEFORE API 22
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(12, builder.build());
        }

    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }
}
