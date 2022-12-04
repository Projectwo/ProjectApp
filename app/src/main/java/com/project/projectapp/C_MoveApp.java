package com.project.projectapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import java.util.List;

public class C_MoveApp {

// 삭제대상 클래스
    /**
     * TODO [클래스 설명]
     * 1. 외부 링크 및 외부 앱 이동 클래스
     * 2. goAppRun : 외부 앱 메인 실행 및 마켓 이동 실시
     * 3. goChromeBrowser : 크롬 브라우저 외부 링크 이동 실시
     * 4. goNormalBrowser : 일반 브라우저 외부 링크 이동 실시
     * */



    // TODO [외부 앱이 설치되어 있으면 메인 실행, 아니면 마켓이동 실시 메소드]
    public static void goAppRun(Context mContext, final String packageNames){
        /**
         * [안드로이드 OS 11 및 타겟 30 이상 필수 사항]
         * [AndroidManifest.xml 파일 > manifest 부분 > 특정 앱 패키지명 등록 필요]
         * <queries>
         *         <package android:name="com.android.chrome" />
         * </queries>
         * */

        // [외부앱이 설치되었는지 확인]
        boolean isExist = false;
        PackageManager packageManager = mContext.getPackageManager();
        List<ResolveInfo> mApps;
        Intent mIntent = new Intent(Intent.ACTION_MAIN, null);
        mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = packageManager.queryIntentActivities(mIntent, 0);
        try {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_MoveApp >> goAppRun() :: 외부 앱 실행 및 마켓 이동 실시]");
            Log.i("","\n"+"[찾을려는 앱 :: "+packageNames+"]");
            Log.i("","\n"+"[설치된 앱 :: "+mApps.toString()+"]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
            for (int i = 0; i < mApps.size(); i++) {
                if(mApps.get(i).activityInfo.packageName.startsWith(packageNames)){
                    isExist = true;
                    break;
                }
            }
        } catch (Exception e) {
            isExist = false;
        }

        if(isExist == true){ // [외부 앱이 설치 된 경우]
            /**
             * 1) 구글 플레이스토어에서 앱이 설치된지 확인
             * 2) 설치되지 않은 경우 앱 설치 진행 / 설치된 경우 외부앱 실행 실시
             */
            // [메인으로 인텐트 이동]
            try {
                Log.i("---","---");
                Log.w("//===========//","================================================");
                Log.i("","\n"+"[C_MoveApp >> goAppRun() :: 외부 앱 실행 및 마켓 이동 실시]");
                Log.i("","\n"+"[찾을려는 앱 :: "+packageNames+"]");
                Log.i("","\n"+"[상태 :: "+"외부 앱 설치 됨"+"]");
                Log.w("//===========//","================================================");
                Log.i("---","---");
                Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(packageNames); //메인으로 그냥 이동
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mContext.startActivity(intent);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{ // [외부 앱이 설치되어있지 않은 경우]
            Log.i("---","---");
            Log.e("//===========//","================================================");
            Log.i("","\n"+"[C_MoveApp >> goAppRun() :: 외부 앱 실행 및 마켓 이동 실시]");
            Log.i("","\n"+"[찾을려는 앱 :: "+packageNames+"]");
            Log.i("","\n"+"[상태 :: "+"외부 앱 설치 안됨"+"]");
            Log.e("//===========//","================================================");
            Log.i("---","---");
            try {
                // [구글 플레이스토어 앱에서 이동한다]
                Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                marketLaunch.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                marketLaunch.setData(Uri.parse("market://details?id="+packageNames));
                mContext.startActivity(marketLaunch);
            }
            catch (Exception e){
                // [구글 플레이스토어 앱이 없을 경우 홈페이지에서 이동한다]
                Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                marketLaunch.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                marketLaunch.setData(Uri.parse("https://play.google.com/store/apps/details?id="+packageNames));
                mContext.startActivity(marketLaunch);
                e.printStackTrace();
            }
        }
    }



    // TODO [크롬 브라우저 사용해 외부 링크 이동 실시]
    public static void goChromeBrowser(Context mContext, String url){
        /**
         * [안드로이드 OS 11 및 타겟 30 이상 필수 사항]
         * [AndroidManifest.xml 파일 > manifest 부분 > 특정 앱 패키지명 등록 필요]
         * <queries>
         *         <package android:name="com.android.chrome" />
         *         <package android:name="kr.co.two2k.manager" />
         * </queries>
         * */

        // [크롬 브라우저 패키지명]
        final String packageNames = "com.android.chrome";

        // [URL 주소 확인 실시]
        String urlParse = String.valueOf(url);
        if(urlParse.startsWith("https:") || urlParse.startsWith("http:")){
            urlParse = urlParse.trim();
            urlParse = urlParse.replaceAll(" ","");
        }
        else {
            urlParse = "https:" + urlParse;
            urlParse = urlParse.trim();
            urlParse = urlParse.replaceAll(" ","");
        }
        final String urlData = urlParse;

        // [크롬 브라우저 설치 상태 확인 실시]
        boolean isExist = false;
        PackageManager packageManager = mContext.getPackageManager();
        List<ResolveInfo> mApps;
        Intent mIntent = new Intent(Intent.ACTION_MAIN, null);
        mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = packageManager.queryIntentActivities(mIntent, 0);

        try {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_MoveApp >> goChromeBrowser() :: 크롬 브라우저 외부 링크 이동 실시]");
            Log.i("","\n"+"[찾을려는 앱 :: "+packageNames+"]");
            Log.i("","\n"+"[설치된 앱 :: "+mApps.toString()+"]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
            for (int i = 0; i < mApps.size(); i++) {
                if(mApps.get(i).activityInfo.packageName.startsWith(packageNames)){
                    isExist = true;
                    break;
                }
            }
        } catch (Exception e) {
            isExist = false;
        }

        if(isExist == true){ // [크롬 브라우저가 설치된 경우]
            // [크롬 브라우저를 통해 외부 링크 호출]
            try {
                Log.i("---","---");
                Log.w("//===========//","================================================");
                Log.i("","\n"+"[C_MoveApp >> goChromeBrowser() :: 크롬 브라우저 외부 링크 이동 실시]");
                Log.i("","\n"+"[상태 : "+"크롬 설치 된 상태"+"]");
                Log.i("","\n"+"[주소 : "+urlData+"]");
                Log.w("//===========//","================================================");
                Log.i("---","---");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setPackage(packageNames); // [크롬 브라우저 지정]
                intent.setData(Uri.parse(urlData));
                mContext.startActivity(intent);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{ // [크롬 브라우저가 설치되지 않은 경우]
            try {
                Log.i("---","---");
                Log.e("//===========//","================================================");
                Log.i("","\n"+"[C_MoveApp >> goChromeBrowser() :: 크롬 브라우저 외부 링크 이동 실시]");
                Log.i("","\n"+"[상태 : "+"크롬 설치 안된 상태"+"]");
                Log.i("","\n"+"[주소 : "+urlData+"]");
                Log.e("//===========//","================================================");
                Log.i("---","---");

                // [일반 기본 설정된 브라우저로 이동]
                goNormalBrowser(mContext, urlData);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    // TODO [일반 브라우저 사용해 외부 링크 이동 실시]
    public static void goNormalBrowser(Context mContext, String url){
        try {
            Log.i("---","---");
            Log.w("//===========//","================================================");
            Log.i("","\n"+"[C_MoveApp >> goNormalBrowser() :: 일반 브라우저 외부 링크 이동 실시]");
            Log.i("","\n"+"[주소 : "+url+"]");
            Log.w("//===========//","================================================");
            Log.i("---","---");

            Intent go = new Intent(Intent.ACTION_VIEW);
            go.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            go.setData(Uri.parse(url));
            mContext.startActivity(go);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

} // TODO [클래스 종료]


