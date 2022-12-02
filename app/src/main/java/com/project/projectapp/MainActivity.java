package com.project.projectapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private WebView mWebView;
    private WebSettings mWebSettings;

    String ACTIVITY_NAME = "ACTIVITY_NAME";
    private WebViewClient MyWebViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Permission.checkPermission(MainActivity.this);  // getPermation
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mWebView = findViewById(R.id.main_webview);

        // [쿠키 매니저 사용해 쿠키 값 셋팅 실시]
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(mWebView, true); // [웹뷰 지정]
//
//        cookieManager.setCookie("groupprojectwo.com", "JSESSIONID=6245179bad665fcda080087c;domain=groupprojectwo.com;path=/;"); // [쿠키 값 셋팅 실시]
//        cookieManager.getInstance().flush();


        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        mWebSettings.setSupportMultipleWindows(false);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDefaultTextEncodingName("UTF-8");
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl("https://groupprojectwo.com");



    }




    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if ("www.naver.com".equals(Uri.parse(url).getHost())) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }


    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }



    public void onButton1Clicked(View v){
        beaconSettiong();
        //startBeaconSend(sendUuid, sendMajor, sendMinor);
        startBeaconScan();
        onBeaconServiceConnect();
        Toast.makeText(this, "BeaconScan", Toast.LENGTH_SHORT).show();

    }




    // Beacon
    // TODO [비콘 스캔 관련 전역 변수]
    private BeaconManager beaconManager; // [비콘 매니저 객체]
    private List<Beacon> beaconList = new ArrayList<>(); // [실시간 비콘 감지 배열]
    int beaconScanCount = 1; // [비콘 스캔 횟수를 카운트하기 위함]
    ArrayList beaconFormatList = new ArrayList<>(); // [스캔한 비콘 리스트를 포맷해서 저장하기 위함]

    //[3]. 비콘 정보 셋팅
    //TODO [비콘 스캐닝을 위한 초기 설정]
    public void beaconSettiong(){
        Log.i("---","---");
        Log.d("//===========//","================================================");
        Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >> beaconSettiong() :: 비콘 매니저 초기 설정 수행]");
        Log.i("","\n"+"[레이아웃 :: m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25]");
        Log.d("//===========//","================================================");
        Log.i("---","---");
        try {
            // [비콘 매니저 생성]
            beaconManager = BeaconManager.getInstanceForApplication(MainActivity.this);
            beaconManager.setRegionStatePersistenceEnabled(false);
            // [블루투스가 스캔을 중지하지 않도록 설정]
            beaconManager.setEnableScheduledScanJobs(false); // TODO 이코드를 설정해야 지맘대로 블루투스가 스캔을 중지하지 않는다.
            //beaconManager.setRegionStatePeristenceEnabled(false);


            // [레이아웃 지정 - IOS , Android 모두 스캔 가능]
            beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


//[5]. 비콘 신호 스캔 및 종료

    // TODO [SEARCH FAST] : [비콘 스캔 수행]
    public void startBeaconScan(){
        Log.i("---","---");
        Log.w("//===========//","================================================");
        Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >> startBeaconScan() :: 실시간 비콘 스캔 [시작] 실시]");
        Log.w("//===========//","================================================");
        Log.i("---","---");
        try {
            // [이미 비콘이 진행 중인 경우 종료 실시]
            //stopBeaconScan();

            // [비콘 스캔 카운트 변수값 초기화 실시]
            beaconScanCount = 1;

            // [비콘 배열 데이터 초기화 실시]
            if (beaconList != null && beaconList.size()>0){
                beaconList.clear();
            }
            if (beaconFormatList != null && beaconFormatList.size()>0){
                beaconFormatList.clear();
            }

            // [beaconManager Bind 설정]
            beaconManager.bind(MainActivity.this);

            // [실시간 비콘 스캔 수행 핸들러 호출]
            beaconScanHandler.sendEmptyMessage(0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    // TODO [SEARCH FAST] : [비콘 스캔 수행]
    public void stopBeaconScan(){
        Log.i("---","---");
        Log.e("//===========//","================================================");
        Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >> stopBeaconScan() :: 실시간 비콘 스캔 [종료] 실시]");
        Log.e("//===========//","================================================");
        Log.i("---","---");
        try {
            // -----------------------------------------
            // [비콘 스캔 카운트 초기화]
            beaconScanCount = 1;
            // -----------------------------------------
            // [비콘 배열 데이터 초기화 실시]
            if (beaconList != null && beaconList.size()>0){
                beaconList.clear();
            }
            if (beaconFormatList != null && beaconFormatList.size()>0){
                beaconFormatList.clear();
            }
            // -----------------------------------------
            // [핸들러 사용 종료]
            try {
                if (beaconScanHandler != null){
                    beaconScanHandler.removeMessages(0);
                    beaconScanHandler.removeCallbacks(null);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            // -----------------------------------------
            // [beaconManager Bind 해제]
            try {
                if(beaconManager != null){
                    beaconManager.unbind(MainActivity.this);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            // -----------------------------------------
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    // TODO [SEARCH FAST] : [비콘 스캔 수행] : [실시간 비콘 스캐닝 감지 부분]
    @Override
    public void onBeaconServiceConnect() {
        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                // [비콘이 감지되면 해당 함수가 호출]
                // TODO [비콘들에 대응하는 Region 객체가 들어들어옴]
                if (beacons.size() > 0) {

                    if (beaconList != null){
                        beaconList.clear();
                    }

                    for (Beacon beacon : beacons) {
                        if (beaconList != null){
                            beaconList.add(beacon);
                        }
                    }
                }
                else {
                    // TODO [실시간 스캔 반영을 위해 스캔 된 것이 없어도 기존 목록 초기화 실시]
                    if (beaconList != null && beaconList.size() > 0){
                        beaconList.clear();
                    }
                }
            }
        };
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    Handler beaconScanHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.i("", "Scan step5//////msg"+msg);

            try {
                // [기존에 저장된 배열 데이터 초기화 실시]
                if(beaconFormatList != null && beaconFormatList.size() > 0){
                    beaconFormatList.clear();
                }

                // [for 문 사용해 실시간 스캔된 비콘 개별 정보 확인]
                if (beaconList != null){
                    for(Beacon beacon : beaconList){
                        if(String.valueOf(beacon.getId1().toString().toLowerCase()).equals("11111111-2222-2222-cccc-ccccdddddddd")) {
                            // TODO [비콘 스캔 정보 추출 참고]
                            Log.d("//===========//", "================================================");
                            Log.i("", "\n" + "[" + String.valueOf(ACTIVITY_NAME) + " >> beaconScanHandler() :: 실시간 비콘 스캔 [개별] 정보 확인 실시]");
                            Log.i("", "\n" + "[비콘 스캔 Name] " + " [" + String.valueOf(beacon.getBluetoothName()) + "]");
                            Log.i("", "\n" + "[비콘 스캔 MAC] " + " [" + String.valueOf(beacon.getBluetoothAddress()) + "]");
                            Log.i("", "\n" + "[비콘 스캔 UUID] " + " [" + String.valueOf(beacon.getId1().toString()) + "]");
                            Log.i("", "\n" + "[비콘 스캔 Major] " + " [" + String.valueOf(beacon.getId2().toString()) + "]");
                            Log.i("", "\n" + "[비콘 스캔 Minor] " + " [" + String.valueOf(beacon.getId3().toString()) + "]");
                            Log.i("", "\n" + "[비콘 스캔 MPower] " + " [" + String.valueOf(beacon.getTxPower()) + "]");
                            Log.i("", "\n" + "[비콘 스캔 RSSI] " + " [" + String.valueOf(beacon.getRssi()) + "]");
                            Log.i("", "\n" + "[비콘 스캔 ServiceUuid] " + " [" + String.valueOf(beacon.getServiceUuid()) + "]");
                            Log.i("", "\n" + "[비콘 스캔 beacon] " + " [" + String.valueOf(beacon.toString()) + "]");
                            Log.d("//===========//", "================================================");
                            //

                            // [스캔한 비콘 정보 포맷 실시]
                            JSONObject jsonBeacon = new JSONObject();

                            // [UUID : 소문자 변환]
                            jsonBeacon.put("uuid", String.valueOf(beacon.getId1().toString().toLowerCase()));

                            // [minor (36)]
                            jsonBeacon.put("minor", String.valueOf(beacon.getId3().toString()));

                            // [major (1)]
                            jsonBeacon.put("major", String.valueOf(beacon.getId2().toString()));

                            // [배열에 데이터 저장 실시]
                            beaconFormatList.add(jsonBeacon.toString());
                        }
                    } // [for 문 종료]
                }



                // [실시간 스캔된 비콘 정보 확인 실시]
                Log.i("---","---");
                Log.w("//===========//","================================================");
                Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >> beaconScanHandler() :: 실시간 비콘 스캔 [전체] 정보 확인 실시]");
                Log.i("","\n"+"[비콘 스캔 실행 횟수] "+" ["+String.valueOf(beaconScanCount)+"]");
                Log.i("","\n"+"[비콘 스캔 개수 확인] "+" ["+String.valueOf(beaconFormatList.size())+"]");
                Log.i("","\n"+"[비콘 스캔 정보 확인] [스캔 포맷 값] "+" ["+String.valueOf(beaconFormatList.toString())+"]");
                Log.w("//===========//","================================================");
                Log.i("---","---");



                // [자바스크립트로 데이터 전송 실시]
                //new Android_To_Javascript().scanList(beaconFormatList.toString());
                //TextView beacon_info = findViewById(R.id.beacon_info); // [비콘정보 출력]
                //beacon_info.setText(beaconFormatList.toString());


                // [비콘 스캔 카운트 증가]
                beaconScanCount ++;



                // [핸들러 자기 자신을 1초마다 호출]
                beaconScanHandler.sendEmptyMessageDelayed(0, 1000);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    };


}