package com.project.projectapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

public class C_Intent {


    /**
     * TODO [클래스 설명]
     * 1. 인텐트 이동 클래스
     * 2. goBleSettingsIntent : 블루투스 설정창 이동
     * 3. goGpsSettingsIntent : gps 설정창 이동
     * 4. goMobileDefaultIntent : 모바일 기본 설정창 이동
     * 5. goWifiIntent : 모바일 와이파이 설정창 이동
     * 6. goAppInfoIntent : 애플리케이션 설정창 이동
     * 7. goSoundIntent : 모바일 사운드 설정창 이동
     * 8. goMobileInfoIntent : 휴대전화 정보 설정창 이동
     * 9. goNfcIntent : NFC 설정창 이동
     * 10. goDisplayIntent : 디스플레이 설정창 이동
     * 11. goMailIntent : 메일 인텐트 이동 실시
     * 12. goTelIntent : 전화 다이얼 인텐트 이동 실시
     * 13. goSmsIntent : SMS 문자 인텐트 이동 실시
     * 14. goLinkIntent : 하이퍼링크 인텐트 이동 실시
     * 15. goIntentNotificationSetting : 애플리케이션 노티피케이션 알림 설정창 이동
     * */



    // TODO [블루투스 설정창 이동 메소드]
    public static void goBleSettingsIntent(Context mContext){
        try {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Intent >> goBleSettingsIntent() :: 블루투스 설정창 인텐트 이동 실시]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
            Intent go_ble = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
            go_ble.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            mContext.startActivity(go_ble);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    //TODO [GPS 설정창 이동]
    public static void goGpsSettingsIntent(Context mContext){
        try {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Intent >> goGpsSettingsIntent() :: 위치 권한 설정창 인텐트 이동 실시]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
            Intent go_gps = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            go_gps.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            mContext.startActivity(go_gps);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    //TODO [모바일 기본 설정창 이동]
    public static void goMobileDefaultIntent(Context mContext){
        try {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Intent >> goMobileDefaultIntent() :: 모바일 기본 설정창 인텐트 이동 실시]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
            Intent intent= new Intent(Settings.ACTION_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            mContext.startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    //TODO [모바일 와이파이 설정창 이동]
    public static void goWifiIntent(Context mContext){
        try {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Intent >> goWifiIntent() :: 모바일 와이파이 설정창 인텐트 이동 실시]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
            Intent intent= new Intent(Settings.ACTION_WIFI_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            mContext.startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    //TODO [모바일 사운드 설정창 이동]
    public static void goSoundIntent(Context mContext){
        try {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Intent >> goSoundIntent() :: 모바일 사운드 설정창 인텐트 이동 실시]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
            Intent intent= new Intent(Settings.ACTION_SOUND_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            mContext.startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    //TODO [애플리케이션 설정창 이동]
    public static void goAppInfoIntent(Context mContext){
        try {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Intent >> goAppInfoIntent() :: 애플리케이션 설정창 인텐트 이동 실시]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
            intent.setData(uri);
            mContext.startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    //TODO [휴대전화 정보 설정창 이동]
    public static void goMobileInfoIntent(Context mContext){
        try {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Intent >> goMobileInfoIntent() :: 휴대전화 정보 설정창 인텐트 이동 실시]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
            Intent intent= new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            mContext.startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    //TODO [NFC 설정창 이동]
    public static void goNfcIntent(Context mContext){
        try {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Intent >> goNfcIntent() :: NFC 설정창 인텐트 이동 실시]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
            Intent intent= new Intent(Settings.ACTION_NFC_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            mContext.startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    //TODO [디스플레이 설정창 이동]
    public static void goDisplayIntent(Context mContext){
        try {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Intent >> goDisplayIntent() :: 디스플레이 설정창 인텐트 이동 실시]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
            Intent intent= new Intent(Settings.ACTION_DISPLAY_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            mContext.startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    //TODO [메일 이동]
    public static void goMailIntent(Context mContext, String url){
        try {
            /**
             * url 형식 : mailto:honggildung@test.com?subject=tittle&body=content
             * */
            if(url.startsWith("mailto")){
                Log.i("---","---");
                Log.d("//===========//","================================================");
                Log.i("","\n"+"[C_Intent >> goMailIntent() :: 메일 인텐트 이동 실시]");
                Log.i("","\n"+"[url :: "+String.valueOf(url)+"]");
                Log.d("//===========//","================================================");
                Log.i("---","---");
                Intent mail_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mail_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mContext.startActivity(mail_intent);
            }
            else {
                Log.i("---","---");
                Log.e("//===========//","================================================");
                Log.i("","\n"+"[C_Intent >> goMailIntent() :: 메일 인텐트 이동 실패]");
                Log.i("","\n"+"[url :: "+String.valueOf(url)+"]");
                Log.e("//===========//","================================================");
                Log.i("---","---");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    //TODO [전화 다이얼 이동]
    public static void goTelIntent(Context mContext, String url){
        try {
            /**
             * url 형식 : tel:010-1234-5678
             * */
            if(url.startsWith("tel")){
                Log.i("---","---");
                Log.d("//===========//","================================================");
                Log.i("","\n"+"[C_Intent >> goTelIntent() :: 전화 다이얼 인텐트 이동 실시]");
                Log.i("","\n"+"[url :: "+String.valueOf(url)+"]");
                Log.d("//===========//","================================================");
                Log.i("---","---");
                Intent tel_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                tel_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mContext.startActivity(tel_intent);
            }
            else {
                Log.i("---","---");
                Log.e("//===========//","================================================");
                Log.i("","\n"+"[C_Intent >> goTelIntent() :: 전화 다이얼 인텐트 이동 실패]");
                Log.i("","\n"+"[url :: "+String.valueOf(url)+"]");
                Log.e("//===========//","================================================");
                Log.i("---","---");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    //TODO [SMS 문자 이동]
    public static void goSmsIntent(Context mContext, String url){
        try {
            /**
             * url 형식 : sms:010-5678-1234
             * */
            if(url.startsWith("sms")){
                Log.i("---","---");
                Log.d("//===========//","================================================");
                Log.i("","\n"+"[C_Intent >> goSmsIntent() :: SMS 문자 인텐트 이동 실시]");
                Log.i("","\n"+"[url :: "+String.valueOf(url)+"]");
                Log.d("//===========//","================================================");
                Log.i("---","---");
                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                sms_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mContext.startActivity(sms_intent);
            }
            else {
                Log.i("---","---");
                Log.e("//===========//","================================================");
                Log.i("","\n"+"[C_Intent >> goSmsIntent() :: SMS 문자 인텐트 이동 실패]");
                Log.i("","\n"+"[url :: "+String.valueOf(url)+"]");
                Log.e("//===========//","================================================");
                Log.i("---","---");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    //TODO [하이퍼링크 이동]
    public static void goLinkIntent(Context mContext, String url){
        try {
            /**
             * url 형식 : l:https://www.naver.com
             * */
            if(url.startsWith("l") && url.contains("http")){
                Log.i("---","---");
                Log.d("//===========//","================================================");
                Log.i("","\n"+"[C_Intent >> goLinkIntent() :: 하이퍼링크 인텐트 이동 실시]");
                Log.i("","\n"+"[url :: "+String.valueOf(url)+"]");
                Log.d("//===========//","================================================");
                Log.i("---","---");
                Intent link_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.substring(2))); // l: [2글자 자름]
                link_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mContext.startActivity(link_intent);
            }
            else {
                Log.i("---","---");
                Log.e("//===========//","================================================");
                Log.i("","\n"+"[C_Intent >> goLinkIntent() :: 하이퍼링크 인텐트 이동 실패]");
                Log.i("","\n"+"[url :: "+String.valueOf(url)+"]");
                Log.e("//===========//","================================================");
                Log.i("---","---");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    // TODO [애플리케이션 노티피케이션 알림 설정 창 이동 메소드]
    public static void goIntentNotificationSetting(Context mContext){
        // [애플리케이션 알림 설정 활성 및 비활성 확인 실시]
        boolean isNotificationEnable = NotificationManagerCompat.from(mContext).areNotificationsEnabled();
        Log.i("---","---");
        Log.w("//===========//","================================================");
        Log.i("","\n"+"[C_Intent >> goIntentNotificationSetting() :: 노티피케이션 알림 활성 및 비활성 확인]");
        Log.i("","\n"+"[상태 :: "+String.valueOf(isNotificationEnable)+"]");
        Log.w("//===========//","================================================");
        Log.i("---","---");

        Intent intent = null; // [초기 인텐트 변수 선언]
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ // [OS 가 오레오 이상인 경우]
                Log.i("---","---");
                Log.d("//===========//","================================================");
                Log.i("","\n"+"[C_Intent >> goIntentNotificationSetting() :: 노티피케이션 알림 설정창 이동]");
                Log.i("","\n"+"[버전 :: 오레오 이상]");
                Log.d("//===========//","================================================");
                Log.i("---","---");
                intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, mContext.getPackageName());
            }
            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){ //TODO [OS 가 롤리팝 이상인 경우]
                Log.i("---","---");
                Log.d("//===========//","================================================");
                Log.i("","\n"+"[C_Intent >> goIntentNotificationSetting() :: 노티피케이션 알림 설정창 이동]");
                Log.i("","\n"+"[버전 :: 롤리팝 이상]");
                Log.d("//===========//","================================================");
                Log.i("---","---");
                intent = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", mContext.getPackageName());
                intent.putExtra("app_uid", mContext.getApplicationInfo().uid);
            }
            else {
                Log.i("---","---");
                Log.d("//===========//","================================================");
                Log.i("","\n"+"[C_Intent >> goIntentNotificationSetting() :: 노티피케이션 알림 설정창 이동]");
                Log.i("","\n"+"[버전 :: 롤리팝 미만]");
                Log.d("//===========//","================================================");
                Log.i("---","---");
                intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + String.valueOf(mContext.getPackageName())));
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            mContext.startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


} // TODO [클래스 종료]

