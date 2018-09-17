package com.calak.jemmy.spiro.Services.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.calak.jemmy.spiro.Model.Notification;
import com.calak.jemmy.spiro.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        try {
            Log.d("data", remoteMessage.getData().get("data"));
            Log.d("notification", remoteMessage.getNotification().getBody());

            EventBus.getDefault().postSticky(new Notification(remoteMessage.getData().get("data")));

            JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
            defaultNotification("","", remoteMessage.getNotification().getBody());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    long[] pattern = {0, 300, 0};

    private void defaultNotification(String action, String lay, String body) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.icon_app);
        notificationBuilder.setLargeIcon(getLargeImage());
        notificationBuilder.setContentTitle(getResources().getString(R.string.app_name));
        notificationBuilder.setContentText(body);
        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setTicker("New Notification");
        notificationBuilder.setSound(getSound());
        notificationBuilder.setVibrate(pattern);
//        notificationBuilder.setNumber(++numb);

        PendingIntent pendingIntent;
        Intent intent;
        intent = new Intent(action);
        intent.putExtra("data", lay);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    private Bitmap getLargeImage() {
        Bitmap largerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_app);
        return largerIcon;
    }

    private Uri getSound() {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return sound;
    }
}
