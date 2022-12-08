package com.project.projectapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

//Firebase에서 메시지를 받으면 디바이스에 알림이 뜨게 하는 클래스
//앱이 실행되면 계속 이 서비스를 돌다가 메시지를 받을 시 동작함

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String token;

    @Override
    public void onNewToken(String token){

        Log.d("OnNewToken Log", "####token: " + token);
        sendRegistrationToServer(token);

    }

    public void sendRegistrationToServer(String token){

        //디바이스 토큰이 생성될 때 동작할 코드 작성
        //서버에 디바이스 토큰값을 넘겨주는 동작을 하고 싶으면 여기에 어떻게 하래...
    }

//    public String getToken(){
//        return token;
//    }
    //받은 메시지에서 title과 body를 추출
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("FirebaseMessagingService", "From: " + remoteMessage.getFrom());

        if(remoteMessage.getData().size() > 0) {
            Log.d("FirebaseMessagingService", "Message data payload: " + remoteMessage.getData());

            if(true) {

            }else {
                handleNow();
            }
        }

        if(remoteMessage.getNotification() != null) {
            Log.d("FirebaseMessagingService", "Message Notification Title: " + remoteMessage.getNotification().getTitle());
            Log.d("FirebaseMessagingService", "Message Notification Body: " + remoteMessage.getNotification().getBody());

            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());


        }
    }

    private void handleNow() {
        Log.d("FirebaseMessagingService", "Short lived task is done");
    }

    //받은 title과 body로 디바이스 알림 전송
    private void sendNotification(String messageTitle, String messageBody) {

        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());


    }

}