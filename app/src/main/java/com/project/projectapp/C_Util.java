package com.project.projectapp;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class C_Util {


    /**
     * TODO [클래스 설명]
     * 1. 프로그램 상 필요한 유틸 파일 모음 클래스
     * 2. showAlert : alert 팝업창 호출
     * 3. getNowDateTime24 : 24 시간 형태 현재 날짜 확인
     * 4. showMobileBuildInfo : 모바일 빌드 정보 확인 실시
     * 5. getMobileCode : 모바일 버전 코드 확인 (ex : 1)
     * 6. getMobileVersion : 모바일 버전명 확인 (ex : 1.0.0)
     * 7. getMobilePackageName : 애플리케이션 패키지명 확인
     * 8. callDisplayWakeUp : 모바일 디스플레이 화면 강제 기상
     * 9. callVibrator : 모바일 진동 발생
     * 10. callRingtoneSound() : 모바일 기본 알림을 발생
     * 11. removeBadge : 노티피케이션 뱃지 지우기
     * 12. getMobileSize : 모바일 화면 크기 구하기 DP
     * 13. convert_HexString_To_Byte : Hex String convert byte array
     * */


    // TODO [팝업창 호출 처리 메소드]
    public static void showAlert(Context mContext, int setType ,String header, String content, String ok, String no, String normal){
        Log.i("---","---");
        Log.d("//===========//","================================================");
        Log.i("","\n"+"[C_Util >> showAlert() :: 팝업창 호출 실시]");
        Log.i("","\n"+"[setType :: "+String.valueOf(setType)+"]");
        Log.i("","\n"+"[header :: "+String.valueOf(header)+"]");
        Log.i("","\n"+"[content :: "+String.valueOf(content)+"]");
        Log.d("//===========//","================================================");
        Log.i("---","---");

        // [사용 방법 정의]
        /* C_Util.showAlert( // [팝업창 호출 실시 및 와이파이 설정창 이동]
                A_Main.this,
                1, // 와이파이 설정창 이동 코드
                "",
                "현재 연결된 네트워크가 없습니다. \n 설정에서 다시 확인해주세요.",
                "설정",
                "취소",
                ""); */


        // [파라미터 관련 설명]
        // 1. setType : 설정창 이동 여부 타입
        //  - [0=없음 / 1=와이파이 설정창 / 2=]
        // 2. header : 팝업창 타이틀
        // 3. content : 팝업창 내용
        // 4. ok : 확인 버튼
        // 5. no : 취소 버튼
        // 6. normal : 노멀 버튼


        // [타이틀 및 내용 표시]
        final String Tittle = String.valueOf(header);
        final String Message = String.valueOf(content);


        // [버튼 이름 정의]
        String buttonYes = String.valueOf(ok);
        String buttonNo = String.valueOf(no);
        String buttonNature = String.valueOf(normal);
        try {
            // [AlertDialog 팝업창 생성]
            new AlertDialog.Builder(mContext)
                    .setTitle(Tittle) //[팝업창 타이틀 지정]
                    //.setIcon(R.drawable.app_icon) //[팝업창 아이콘 지정]
                    .setMessage(Message) //[팝업창 내용 지정]
                    .setCancelable(false) //[외부 레이아웃 클릭시도 팝업창이 사라지지않게 설정]
                    .setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            // TODO [확인 버튼 클릭 이벤트 처리]
                            if (setType == 1){
                                C_Intent.goWifiIntent(mContext); // 와이파이 설정창 이동
                            }
                        }
                    })
                    .setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                        }
                    })
                    .setNeutralButton(buttonNature, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                        }
                    })
                    .show();
        }
        catch (Exception e){
            Toast.makeText(mContext, Tittle+"\n"+Message,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }



    // TODO [24 시간 형태 현재 날짜 확인]
    public static String getNowDateTime24() {
        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy.MM.dd kk:mm:ss E요일");
        String str = dayTime.format(new Date(time));
        Log.i("---","---");
        Log.d("//===========//","================================================");
        Log.i("","\n"+"[C_Util >> getNowDateTime24() :: 24 시간 형태 현재 날짜 확인]");
        Log.i("","\n"+"[날짜 :: "+String.valueOf(str)+"]");
        Log.d("//===========//","================================================");
        Log.i("---","---");
        return str;
    }



    // TODO [모바일 빌드 정보 확인]
    public static void showMobileBuildInfo(Context mContext) {
        try {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            // [고유 단말기 정보 확인]
            Log.i("","\n"+"[C_Util >> showMobileBuildInfo() :: 모바일 빌드 정보 확인]");
            Log.i("","\n"+"["+"BRAND :: "+String.valueOf(Build.BRAND)+"]");
            Log.i("","\n"+"["+"MODEL :: "+String.valueOf(Build.MODEL)+"]");
            Log.i("","\n"+"["+"MANUFACTURER :: "+String.valueOf(Build.MANUFACTURER)+"]");
            Log.i("","\n"+"["+"BOARD :: "+String.valueOf(Build.BOARD)+"]");
            Log.i("","\n"+"["+"ID - "+String.valueOf(Build.ID)+"]");
            Log.i("","\n"+"["+"BOOTLOADER :: "+String.valueOf(Build.BOOTLOADER)+"]");
            Log.i("","\n"+"["+"DEVICE :: "+String.valueOf(Build.DEVICE)+"]");
            Log.i("","\n"+"["+"DISPLAY :: "+String.valueOf(Build.DISPLAY)+"]");
            Log.i("","\n"+"["+"HARDWARE :: "+String.valueOf(Build.HARDWARE)+"]");
            Log.i("","\n"+"["+"HOST :: "+String.valueOf(Build.HOST)+"]");
            Log.i("","\n"+"["+"PRODUCT :: "+String.valueOf(Build.PRODUCT)+"]");
            Log.i("","\n"+"["+"TYPE :: "+String.valueOf(Build.TYPE)+"]");
            Log.i("","\n"+"["+"USER :: "+String.valueOf(Build.USER)+"]");
            Log.i("","\n"+"["+"TAGS :: "+String.valueOf(Build.TAGS)+"]");
            Log.i("","\n"+"["+"TIME :: "+String.valueOf(Build.TIME)+"]");
            Log.i("","\n"+"-"+""+""+"-");

            // [안드로이드 APK 파일 정보 확인]
            PackageInfo pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            Log.i("","\n"+"["+"versionName :: "+String.valueOf(pi.versionName)+"]");
            Log.i("","\n"+"["+"versionCode :: "+String.valueOf(pi.versionCode)+"]");
            Log.i("","\n"+"-"+""+""+"-");

            // [패키지명 확인]
            Log.i("","\n"+"["+"getPackageName :: "+String.valueOf(mContext.getPackageName())+"]");
            Log.i("","\n"+"["+"getPackage :: "+String.valueOf(mContext.getClass().getPackage().getName().trim())+"]");
            Log.i("","\n"+"-"+""+""+"-");

            // [현재 실행 중인 클래스 명 확인]
            ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> info = manager.getRunningTasks(1);
            ComponentName componentName = info.get(0).topActivity;
            String ActivityName = String.valueOf(componentName.getShortClassName().substring(1));
            Log.i("","\n"+"["+"ActivityName :: "+String.valueOf(ActivityName)+"]");
            Log.i("","\n"+"["+"getComponentName :: "+String.valueOf(mContext.getPackageName())+"]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }



    // TODO [모바일 버전 코드 확인]
    public static String getMobileCode(Context mContext) {
        String returnData = "";
        try {
            // [빌드 버전 코드 : ex) 1]
            PackageInfo pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            returnData = String.valueOf(pi.versionCode);
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Util >> getMobileCode() :: 모바일 버전 코드 확인]");
            Log.i("","\n"+"[code :: "+String.valueOf(returnData)+"]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return returnData;
    }



    // TODO [모바일 버전명 확인]
    public static String getMobileVersion(Context mContext) {
        String returnData = "";
        try {
            // [빌드 버전명 : ex) 1.0.0]
            PackageInfo pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            returnData = String.valueOf(pi.versionName);
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Util >> getMobileVersion() :: 모바일 버전명 확인]");
            Log.i("","\n"+"[version :: "+String.valueOf(returnData)+"]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return returnData;
    }



    // TODO [애플리케이션 패키지명 확인]
    public static String getMobilePackageName(Context mContext) {
        String returnData = "";
        try {
            // [패키지 명칭 : ex) com.test.app]
            PackageInfo pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            returnData = String.valueOf(mContext.getPackageName().trim());
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Util >> getMobilePackageName() :: 애플리케이션 패키지명 확인]");
            Log.i("","\n"+"[package :: "+String.valueOf(returnData)+"]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return returnData;
    }



    // TODO [모바일 디스플레이 화면 강제 기상 깨우기]
    public static void callDisplayWakeUp(Context mContext){
        try {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Util >> callDisplayWakeUp() :: 모바일 디스플레이 화면 강제 기상 실시]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "My:Tag");
            wakelock.acquire(); // [화면 즉시 기상 실시]
            wakelock.release(); // [WakeLock 자원 해제]
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    // TODO [모바일 진동 발생 시키는 메소드]
    public static void callVibrator(Context mContext){
        try {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Util >> callVibrator() :: 모바일 진동 발생 실시]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
            Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(1000); // miliSecond [지정한 시간동안 진동 / 1000 = 1초]
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    // TODO [기본 알림을 발생 시키는 메소드]
    public static void callRingtoneSound(Context mContext){
        try {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Util >> callRingtoneSound() :: 모바일 기본 알림을 발생 실시]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(mContext, defaultSoundUri);
            ringtone.play();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    // TODO [노티피케이션 뱃지 지우기 메소드]
    public static void removeBadge(Context mContext, int cancleID){
        try {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Util >> removeBadge() :: 노티피케이션 뱃지 지우기 실시]");
            Log.i("","\n"+"[cancleID :: "+String.valueOf(cancleID)+"]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(cancleID); // 캔슬 부분에 적힌 것이 노티피케이션 활성 id 값
            notificationManager.cancelAll();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    // TODO [모바일 화면 크기 구하기 dp]
    public static String getMobileSize(Context mContext){
        String size = "";
        try {
            Display display = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics ();
            display.getMetrics(outMetrics);
            float density = mContext.getResources().getDisplayMetrics().density;
            float dpHeight = outMetrics.heightPixels / density;
            float dpWidth = outMetrics.widthPixels / density;

            int dpWidthValue = (int)dpWidth;
            int dpHeightValue = (int)dpHeight;
            /**
             *    [장치]   [가로]   [세로]
             * 1) Watch =  250dp    250dp
             * 2) mobile = 320dp    569dp (s6)
             * 3) mobile = 400dp    730dp (note 5)
             * 4) mobile = 400dp    810dp (note 10 / LG Q9)
             * 5) mobile = 800dp    1280dp (Tab)
             * 6) Tablet = 960dp    600dp (Tab)
             *             1280dp   800dp
             */
            if(dpHeightValue < 320){ //0 ~ 319
                size = "W";
            }
            else if(dpHeightValue < 660){ //320 ~ 659
                size = "S";
            }
            else if(dpHeightValue < 750){ //660 ~ 749
                size = "M";
            }
            else if(dpHeightValue < 900){ //750 ~ 899
                size = "L";
            }
            else if(dpHeightValue < 1600){ //900 ~ 1599
                size = "XL";
            }
            else{
                size = "NO";
            }
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Util >> getMobileSize() :: 모바일 화면 크기 구하기 DP]");
            Log.i("","\n"+"["+"디바이스 :: "+String.valueOf("Android")+"]");
            Log.i("","\n"+"["+"Android OS :: "+String.valueOf(Build.VERSION.RELEASE)+"]");
            Log.i("","\n"+"["+"모델 :: "+String.valueOf(Build.MODEL)+"]");
            Log.i("","\n"+"["+"제조사 :: "+String.valueOf(Build.MANUFACTURER)+"]");
            Log.i("","\n"+"["+"가로 (DP) :: "+String.valueOf(dpWidthValue)+"]");
            Log.i("","\n"+"["+"세로 (DP) :: "+String.valueOf(dpHeightValue)+"]");
            Log.i("","\n"+"["+"화면 사이즈 :: "+String.valueOf(size)+"]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return size;
    }



    // TODO [Hex String convert byte array]
    //hex 표시된 데이터를 바이트 값으로 변경
    public static byte[] convert_HexString_To_Byte(String data) {
        String hex = data.replaceAll("0x", ""); //0x문자 제거
        String hex2 = hex.replaceAll("0X", ""); //0x문자 제거
        String Trim = hex2.replaceAll(" ", ""); //공백문자 제거
        byte arr[] = new byte[Trim.length()/2]; //전체 문자열 길이/2만큼 배열 생성
        int before = 0;
        int after = 2;
        for(int i=0; i<arr.length; i++) {
            String value = Trim.substring(before, after); //2글자씩 잘라서 데이터 출력
            arr[i] = new java.math.BigInteger(value, 16).byteValue(); //BigInteger사용해 hexString > byte 변환
            //다음 2글자씩 또 변환하기 위함
            before += 2;
            after += 2;
        }
        Log.i("---","---");
        Log.d("//===========//","================================================");
        Log.i("","\n"+"[C_Util >> convert_HexString_To_Byte() :: Hex String convert byte array]");
        Log.i("","\n"+"["+"input :: "+String.valueOf(data)+"]");
        Log.i("","\n"+"["+"return :: "+ Arrays.toString(arr) +"]");
        Log.d("//===========//","================================================");
        Log.i("---","---");
        return arr;
    }



    // TODO [byte array convert Hex String]
    public static String convert_ByteArray_To_HexString(byte buf[]) {
        String Hex_Value = "";
        for(int i=0; i<buf.length; i++) {
            //Hex_Value += String.format("0x%02x ", buf[i]); // [0xfg]
            //Hex_Value += String.format("%02x ", buf[i]); // [fg]

            Hex_Value += String.format("0X%02X ", buf[i]); // [0XFG]
            //Hex_Value += String.format("%02X ", buf[i]); // [FG]
        }
        Log.i("---","---");
        Log.d("//===========//","================================================");
        Log.i("","\n"+"[C_Util >> convert_ByteArray_To_HexString() :: byte array convert Hex String]");
        Log.i("","\n"+"["+"input :: "+ Arrays.toString(buf) +"]");
        Log.i("","\n"+"["+"return :: "+String.valueOf(Hex_Value)+"]");
        Log.d("//===========//","================================================");
        Log.i("---","---");
        return Hex_Value;
    }


} // TODO [클래스 종료]




