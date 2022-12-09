package com.project.projectapp;


import static com.project.projectapp.MyFirebaseMessagingService.getToken;
import static com.project.projectapp.S_FinalData.MAIN_URL;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity  extends AppCompatActivity implements BeaconConsumer {

    private FirebaseAnalytics mFirebaseAnalytics;


    /*
    * TODO: webview 주소가 https://groupprojectwo.com/main일 때 버튼 출력
    *       출력버튼 클릭 시 로직(QR/Beacon)체크 수행
    *       출석 확인되었을 때 Spring Contorller의 mapping 주소로
    *       webview loadUrl 변경(main_webview.loadUrl(url);)
    *  */
    String currentUrl;      // webView내부 주소
    String ACTIVITY_NAME = "ACTIVITY_NAME";
    //eQqJhBxSQVuqPNOSgmEOfH:APA91bGA-aZ19LUzr-F65MQ05isTBJej3YCcJVMTkHapAeKENmH2VLAu4MFBoz1Hkl0DrVSQb9EIyBO6GhWzyyhPe8_B76scpiV2rZkrdLaRd5X4kQ0WweCs0H6oclrnWAIFuk5vbjGq

    // Beacon
    // TODO [비콘 스캔 관련 전역 변수]
    private BeaconManager beaconManager; // [비콘 매니저 객체]
    private List<Beacon> beaconList = new ArrayList<>(); // [실시간 비콘 감지 배열]
    int beaconScanCount = 1; // [비콘 스캔 횟수를 카운트하기 위함]
    ArrayList beaconFormatList = new ArrayList<>(); // [스캔한 비콘 리스트를 포맷해서 저장하기 위함]
    static int attendSwitch = 0;
    // by 장유란, attendSwitch == 1 -> 출석true 0 -> false

    //WebView
    ///////////////////////////////////////////////
    /**
     * TODO [클래스 설명]
     * 1. 메인 웹뷰 화면 호출 액티비티 화면
     * 2. 네트워크 연결 상태 체크
     * 3. 웹뷰 호출 실시 및 자바스크립트 통신 처리
     * 4. 퍼미션 권한 부여 체크
     * */



    // TODO [전역 변수 선언 부분]
    static WebView main_webview; // [웹뷰 컴포넌트]
    Handler js_handler = new Handler(); // [자바스크립트 통신 사용 핸들러]
    String currentToken = "currentToken";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //push 알림을 위한 토큰
        MyFirebaseMessagingService myFirebaseMessagingService = new MyFirebaseMessagingService();
        try {
            String token = getToken();
            Log.d("MyFirebaseMessagingService", "####MainActivity의 device token: " + token);

        }catch(NullPointerException e) {
            e.printStackTrace();
        }


//        Button qrBtn = findViewById(R.id.qrBtn);
//        qrBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               Intent intent = new Intent(MainActivity.this,ScanQR.class);
//               startActivity(intent);
//                //finish();
//            }
//        });

        Log.i("---","---");
        Log.d("//===========//","================================================");
        Log.i("","\n"+"[MainActivity >> onCreate() :: 액티비티 수행 실시]");
        Log.d("//===========//","================================================");
        Log.i("---","---");

        /**
         * [웹뷰 호출 로직]
         * 1. 네트워크 상태 체크
         * 2. 네트워크 연결 된 경우 >> 웹뷰 호출
         * 3. 네트워크 연결 안된 경우 >> 네트워크 비활성 상태 팝업창 표시
         * */
        if (C_StateCheck.getWhatOfNetwork(MainActivity.this)){ // [네트워크가 연결된 경우]
            // [웹뷰 초기 셋팅 및 로드 수행 메소드 호출]
            init_WebView(MAIN_URL);
        }
        else { // [네트워크가 연결되지 않은 경우]
            // [팝업창 호출 실시 및 와이파이 설정창 이동]
            C_Util.showAlert(
                    MainActivity.this,
                    1, // 와이파이 설정창 이동 코드
                    "",
                    "현재 연결된 네트워크가 없습니다. \n 설정에서 다시 확인해주세요.",
                    "설정",
                    "취소",
                    "");
        }
        // TODO [Beacon 초기값 설정]
        beaconSettiong();
        if(attendSwitch != 1) {
            startBeaconScan();
        }else{
            stopBeaconScan();
        }
    } // TODO [메인 종료]



    // TODO [웹뷰 초기값 설정 및 최초 웹뷰 로드 수행 부분]
    public void init_WebView(String url) {
        Log.i("---","---");
        Log.d("//===========//","================================================");
        Log.i("","\n"+"[MainActivity >> init_WebView() :: 웹뷰 초기값 설정 및 최초 웹뷰 로드 수행 실시]");
        Log.i("","\n"+"[url :: "+String.valueOf(url)+"]");
        Log.d("//===========//","================================================");
        Log.i("---","---");
        try {
            // [WebView 설정]
            main_webview = (WebView)findViewById(R.id.main_webview);

            // [JavaScript 허용]
            main_webview.getSettings().setJavaScriptEnabled(true);

            // [JavaScript의 window.open 허용]
            main_webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

            // [자바스크립트 웹 스토리지 사용]
            main_webview.getSettings().setDomStorageEnabled(true);

            // [web client 설정]
            main_webview.setWebViewClient(new MainWeb());

            // [웹뷰가 띄어질 브라우저 선택]
            main_webview.setWebChromeClient(new WebChromeClient()); // [크롬으로 설정]

            // [웹뷰 표시 크기 설정 및 터치시 확대 설정 및 글자 깨지는것 맞추기]
            /* main_webview.getSettings().setUseWideViewPort(true);
            main_webview.setInitialScale(8);
            main_webview.scrollTo(0,0);  //x, y
            main_webview.getSettings().setBuiltInZoomControls(true);
            main_webview.getSettings().setSupportZoom(true); */

            // [크롬 로딩할 동안 프로그레스 호출]
            main_webview.setWebChromeClient(new WebChromeClient() {
                // 웹 브라우저 콘솔 로그 확인
                @Override
                public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onConsoleMessage() :: 웹 브라우저 콘솔 로그 확인 실시]");
                    Log.i("","\n"+"[url :: "+String.valueOf(main_webview.getUrl())+"]");
                    Log.i("","\n"+"[message :: "+String.valueOf(consoleMessage.message())+"]");
                    Log.i("","\n"+"[sourceId :: "+String.valueOf(consoleMessage.sourceId())+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    return super.onConsoleMessage(consoleMessage);
                }

                // 웹 브라우저 로드 상태 확인
                @Override
                public void onProgressChanged(WebView view, int progress) {

                    Log.i("---","---");
                    Log.d("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onProgressChanged() :: 웹뷰 호출 상황 확인 실시]");
                    Log.i("","\n"+"[url :: "+String.valueOf(main_webview.getUrl())+"]");
                    Log.i("","\n"+"[호출 상황 :: "+String.valueOf(progress)+"]");
                    Log.d("//===========//","================================================");
                    Log.i("---","---");

                    // [웹뷰 로드 완료 상태 : 웹뷰의 호출 상황이 100 인 경우]
                    if(progress >= 100){
                        Log.i("---","---");
                        Log.w("//===========//","================================================");
                        Log.i("","\n"+"[MainActivity >> onProgressChanged() :: 웹뷰 호출 상황 확인 실시]");
                        Log.i("","\n"+"[url :: "+String.valueOf(main_webview.getUrl())+"]");
                        Log.i("","\n"+"[호출 상황 :: "+String.valueOf(progress)+"]");
                        Log.i("","\n"+"[상태 :: "+"웹뷰 로드 완료 상태"+"]");
                        Log.w("//===========//","================================================");
                        Log.i("---","---");
                    }
                }
            });

            // [자바스크립트에서 안드로이드를 호출하는 [window.클래스.메소드] 부분에서 클래스 경로 정의]
            Javascript_To_Android_Bridge(); // 자바스크립트 통신 모든 경로 추가

            // [웹뷰의 캐시를 지워준다]
            main_webview.clearCache(true);
            main_webview.clearHistory();

            // [웹뷰 로드 수행 실시 부분 : 내부 웹뷰 이동]
            main_webview.loadUrl(url);

            // [대기 프로그레스 호출 실시]
            //ProgressWaitStart();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    // TODO [WebViewClient 는 웹뷰에서 자유롭게 기능을 사용하기 위함]
    class MainWeb extends WebViewClient {
        // [로딩이 시작될 때]
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[MainActivity >> onPageStarted() :: [웹 클라이언트]]");
            Log.i("","\n"+"[설 명 :: "+String.valueOf("로딩 시작")+"]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
        }
        // [리소스를 로드하는 중 여러번 호출]
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[MainActivity >> onLoadResource() :: [웹 클라이언트]]");
            Log.i("","\n"+"[주 소 :: "+String.valueOf(url)+"]");
            Log.i("","\n"+"[설 명 :: "+String.valueOf("로드 중")+"]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
        }
        // [방문 내역을 히스토리에 업데이트 할 때]
        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[MainActivity >> doUpdateVisitedHistory() :: [웹 클라이언트]]");
            Log.i("","\n"+"[설 명 :: "+String.valueOf("방문내역 업데이트")+"]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
        }
        // [로딩이 완료됐을 때 한번 호출]
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.i("---","---");
            Log.w("//===========//","================================================");
            Log.i("","\n"+"[MainActivity >> onPageFinished() :: [웹 클라이언트]]");
            Log.i("","\n"+"[기본 주소 :: "+String.valueOf(url)+"]");
            Log.i("","\n"+"[getUrl 주소 :: "+String.valueOf(main_webview.getUrl())+"]");
            Log.i("","\n"+"[getOriginalUrl 주소 :: "+String.valueOf(main_webview.getOriginalUrl())+"]");
            Log.i("","\n"+"[설 명 :: "+String.valueOf("로드 완료")+"]");
            Log.w("//===========//","================================================");
            Log.i("---","---");

        }
        // [오류가 났을 경우, 오류는 복수할 수 없음]
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            switch (errorCode) {
                case ERROR_AUTHENTICATION:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("ERROR_AUTHENTICATION")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("서버에서 사용자 인증 실패")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;
                case ERROR_BAD_URL:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("ERROR_BAD_URL")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("잘못된 URL")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;
                case ERROR_CONNECT:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("ERROR_CONNECT")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("서버로 연결 실패")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;
                case ERROR_FAILED_SSL_HANDSHAKE:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("ERROR_FAILED_SSL_HANDSHAKE")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("SSL handshake 수행 실패")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;
                case ERROR_FILE:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("ERROR_FILE")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("일반 파일 오류")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;
                case ERROR_FILE_NOT_FOUND:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("ERROR_FILE_NOT_FOUND")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("파일을 찾을 수 없습니다")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;
                case ERROR_HOST_LOOKUP:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("ERROR_HOST_LOOKUP")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("서버 또는 프록시 호스트 이름 조회 실패")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;
                case ERROR_IO:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("ERROR_IO")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("서버에서 읽거나 서버로 쓰기 실패")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;
                case ERROR_PROXY_AUTHENTICATION:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("ERROR_PROXY_AUTHENTICATION")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("프록시에서 사용자 인증 실패")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;
                case ERROR_REDIRECT_LOOP:
                    Log.d("---","---");
                    Log.e("//===========//","================================================");
                    Log.d("","\n"+"[MainActivity >> onReceivedError() :: [웹 클라이언트]]");
                    Log.d("","\n"+"[타 입 :: "+String.valueOf("ERROR_REDIRECT_LOOP")+"]");
                    Log.d("","\n"+"[설 명 :: "+String.valueOf("너무 많은 리디렉션")+"]");
                    Log.e("//===========//","================================================");
                    Log.d("---","---");
                    break;
                case ERROR_TIMEOUT:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("ERROR_TIMEOUT")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("연결 시간 초과")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;
                case ERROR_TOO_MANY_REQUESTS:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("ERROR_TOO_MANY_REQUESTS")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("페이지 로드중 너무 많은 요청 발생")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;
                case ERROR_UNKNOWN:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("ERROR_UNKNOWN")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("일반 오류")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;
                case ERROR_UNSUPPORTED_AUTH_SCHEME:
                    Log.d("---","---");
                    Log.e("//===========//","================================================");
                    Log.d("","\n"+"[MainActivity >> onReceivedError() :: [웹 클라이언트]]");
                    Log.d("","\n"+"[타 입 :: "+String.valueOf("ERROR_UNSUPPORTED_AUTH_SCHEME")+"]");
                    Log.d("","\n"+"[설 명 :: "+String.valueOf("지원되지 않는 인증 체계")+"]");
                    Log.e("//===========//","================================================");
                    Log.d("---","---");
                    break;
                case ERROR_UNSUPPORTED_SCHEME:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("ERROR_UNSUPPORTED_SCHEME")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("URI가 지원되지 않는 방식")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;
            }
        }
        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            // [E/chromium: [ERROR:ssl_client_socket_impl.cc 에러 해결 위함]
            try {
                Log.i("---","---");
                Log.e("//===========//","================================================");
                Log.i("","\n"+"[MainActivity >> onReceivedSslError() :: [웹 클라이언트]]");
                Log.i("","\n"+"[설 명 :: "+String.valueOf("사이트 인증서 문제 발생")+"]");
                Log.i("","\n"+"[getUrl 주소 :: "+String.valueOf(main_webview.getUrl())+"]");
                Log.i("","\n"+"[getOriginalUrl 주소 :: "+String.valueOf(main_webview.getOriginalUrl())+"]");
                Log.e("//===========//","================================================");
                Log.i("---","---");
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("이 사이트의 보안 인증서는 신뢰하는 보안 인증서가 아닙니다. 계속하시겠습니까?");
                builder.setPositiveButton("계속하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            // [SSL 에러 타입 확인 실시]
            switch (error.getPrimaryError()) {
                case SslError.SSL_UNTRUSTED:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedSslError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("SSL_UNTRUSTED")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("이 사이트의 보안 인증서는 신뢰할 수 없습니다")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;

                case SslError.SSL_EXPIRED:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedSslError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("SSL_EXPIRED")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("보안 인증서가 만료되었습니다")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;

                case SslError.SSL_IDMISMATCH:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedSslError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("SSL_IDMISMATCH")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("보안 인증서가 ID 일치하지 않습니다")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;

                case SslError.SSL_NOTYETVALID:
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> onReceivedSslError() :: [웹 클라이언트]]");
                    Log.i("","\n"+"[타 입 :: "+String.valueOf("SSL_NOTYETVALID")+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("보안 인증서가 유효하지 않습니다")+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                    break;
            }
        }
        // [http 인증 요청이 있는 경우, 기본 동작은 요청 취소]
        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[MainActivity >> onReceivedHttpAuthRequest() :: [웹 클라이언트]]");
            Log.i("","\n"+"[설 명 :: "+String.valueOf("http 인증 요청")+"]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
        }
        // [확대나 크기 등의 변화가 있는 경우]
        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[MainActivity >> onScaleChanged() :: [웹 클라이언트]]");
            Log.i("","\n"+"[설 명 :: "+String.valueOf("화면 크기 변화")+"]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
        }
        // [잘못된 키 입력이 있는 경우]
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[MainActivity >> shouldOverrideKeyEvent() :: [웹 클라이언트]]");
            Log.i("","\n"+"[설 명 :: "+String.valueOf("잘못된 키 입력")+"]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
            return super.shouldOverrideKeyEvent(view, event);
        }
        // [새로운 URL이 webview에 로드되려 할 경우 컨트롤을 대신할 기회를 줌]
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("---","---");
            Log.w("//===========//","================================================");
            Log.i("","\n"+"[MainActivity >> shouldOverrideUrlLoading() :: [웹 클라이언트]]");
            Log.i("","\n"+"[기본 주소 :: "+String.valueOf(url)+"]");
            Log.i("","\n"+"[getUrl 주소 :: "+String.valueOf(main_webview.getUrl())+"]");
            Log.i("","\n"+"[getOriginalUrl 주소 :: "+String.valueOf(main_webview.getOriginalUrl())+"]");
            Log.i("","\n"+"[설 명 :: "+String.valueOf("새로운 URL이 webview에 로드")+"]");
            Log.w("//===========//","================================================");
            Log.i("---","---");
            currentUrl = String.valueOf(main_webview.getUrl());
//            if(currentUrl.equals("view-source:https://groupprojectwo.com/main")){
//                Log.i("","button 출력 필요"+currentUrl);
//                //main_webview.loadUrl(currentUrl+"/attempt");
//            }
            // [외부앱 실행 인텐트 이동 실시 : if(url.startsWith("intent:패키지명")]
            if(url.startsWith("intent:")) {
                Log.i("---","---");
                Log.w("//===========//","================================================");
                Log.i("","\n"+"[MainActivity >> shouldOverrideUrlLoading() :: [웹 클라이언트]]");
                Log.i("","\n"+"[타 입 :: "+String.valueOf("intent")+"]");
                Log.i("","\n"+"[설 명 :: "+String.valueOf("새로운 URL이 webview에 로드")+"]");
                Log.w("//===========//","================================================");
                Log.i("---","---");
                // [url 불필요한 문자 변경 실시]
                String data = url;
                data = data.replace("intent://","");
                data = data.trim();

                // [외부앱 실행 및 마켓 이동 실시]
                C_MoveApp.goAppRun(MainActivity.this, data);

                // [종 료]
                return true;
            }

            // [SMS 인텐트 이동 실시 : if(url.startsWith("sms:010-5678-1234")]
            if (url.startsWith("sms:")) {
                Log.i("---","---");
                Log.w("//===========//","================================================");
                Log.i("","\n"+"[MainActivity >> shouldOverrideUrlLoading() :: [웹 클라이언트]]");
                Log.i("","\n"+"[타 입 :: "+String.valueOf("sms")+"]");
                Log.i("","\n"+"[설 명 :: "+String.valueOf("새로운 URL이 webview에 로드")+"]");
                Log.w("//===========//","================================================");
                Log.i("---","---");

                // [sms 이동 메소드 호출]
                C_Intent.goSmsIntent(MainActivity.this, url);

                // [종 료]
                return true;
            }

            // [전화 다이얼 인텐트 이동 실시 : if(url.startsWith("tel:010-1234-5678")]
            if(url.startsWith("tel:")){
                Log.i("---","---");
                Log.w("//===========//","================================================");
                Log.i("","\n"+"[MainActivity >> shouldOverrideUrlLoading() :: [웹 클라이언트]]");
                Log.i("","\n"+"[타 입 :: "+String.valueOf("tel")+"]");
                Log.i("","\n"+"[설 명 :: "+String.valueOf("새로운 URL이 webview에 로드")+"]");
                Log.w("//===========//","================================================");
                Log.i("---","---");

                // [전화 다이얼 이동 메소드 호출]
                C_Intent.goTelIntent(MainActivity.this, url);

                // [종 료]
                return true;
            }

            // [Mail (Gmail) 인텐트 이동 실시 : if(url.startsWith("mailto:honggildung@test.com?subject=tittle&body=content")]
            if(url.startsWith("mailto:")){
                Log.i("---","---");
                Log.w("//===========//","================================================");
                Log.i("","\n"+"[MainActivity >> shouldOverrideUrlLoading() :: [웹 클라이언트]]");
                Log.i("","\n"+"[타 입 :: "+String.valueOf("mailto")+"]");
                Log.i("","\n"+"[설 명 :: "+String.valueOf("새로운 URL이 webview에 로드")+"]");
                Log.w("//===========//","================================================");
                Log.i("---","---");

                // [메일 이동 메소드 호출]
                C_Intent.goMailIntent(MainActivity.this, url);

                // [종 료]
                return true;
            }

            // [하이퍼링크 이동 실시 : if(url.startsWith("l:https://naver.com")]
            if(url.startsWith("l:")){
                Log.i("---","---");
                Log.w("//===========//","================================================");
                Log.i("","\n"+"[MainActivity >> shouldOverrideUrlLoading() :: [웹 클라이언트]]");
                Log.i("","\n"+"[타 입 :: "+String.valueOf("하이퍼링크")+"]");
                Log.i("","\n"+"[설 명 :: "+String.valueOf("새로운 URL이 webview에 로드")+"]");
                Log.w("//===========//","================================================");
                Log.i("---","---");

                // [하이퍼링크 이동 메소드 호출]
                C_Intent.goLinkIntent(MainActivity.this, url);

                // [종 료]
                return true;
            }

            // [새로운 웹뷰 로드 수행]
            view.loadUrl(url);

            // [종 료]
            return true;
        }
    }



    // TODO [자바스크립트 >> 안드로이드 - 경로 지정 : 브릿지 경로에 location 추가 시 웹뷰 무한 로딩 현상 발생]
    public void Javascript_To_Android_Bridge(){
        Log.i("---","---");
        Log.w("//===========//","================================================");
        Log.i("","\n"+"[MainActivity >> Javascript_To_Android_Bridge() :: 자바스크립트 통신 브릿지 경로 추가 실시]");
        Log.i("","\n"+"[경로 [1] :: "+String.valueOf("android")+"]");
        Log.i("","\n"+"[경로 [2] :: "+String.valueOf("main")+"]");
        Log.i("","\n"+"[경로 [3] :: "+String.valueOf("media")+"]");
        Log.w("//===========//","================================================");
        Log.i("---","---");

        // [서버 : window.android.함수();]
        main_webview.addJavascriptInterface(new AndroidBridge(), "android"); // [자바스크립트에 대응할 함수를 정의한 클래스 붙여줌]

        // [서버 : window.main.함수();]
        main_webview.addJavascriptInterface(new AndroidBridge(), "main"); // [자바스크립트에 대응할 함수를 정의한 클래스 붙여줌]

        // [서버 : window.media.함수();]
        main_webview.addJavascriptInterface(new AndroidBridge(), "media"); // [자바스크립트에 대응할 함수를 정의한 클래스 붙여줌]
    }



    // TODO [자바스크립트 >> 안드로이드 - 함수 지정]
    class AndroidBridge {
        // [자바스크립트에서 호출하는 안드로이드 메소드]
        @JavascriptInterface
        public void open(String UserIdx, String courseId, String date) { //courseId, date
            js_handler.post(new Runnable() {
                public void run() {
                    Log.i("---","---");
                    Log.w("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> AndroidBridge :: open() [NONE] :: JS >> Android]");
                    Log.i("","\n"+"[전달받은 데이터 :: "+String.valueOf(UserIdx)+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("이벤트 발생 전달")+"]");
                    Log.w("//===========//","================================================");
                    Log.i("---","---");
                    beaconChecking();

                    new Android_To_Javascript().mOpen(UserIdx, courseId, date);
                    // [서버 : window.경로.open() 요청이 들어오면 Android 에서 JS로 바로 데이터를 보내준다]

                }
            });
        }
        // [자바스크립트에서 호출하는 안드로이드 메소드]
        @JavascriptInterface
        public void close(String UserIdx, String courseId, String date) { //courseId, date
            js_handler.post(new Runnable() {
                public void run() {

                    Log.i("---","---");
                    Log.w("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> AndroidBridge :: close() [DATA] :: JS >> Android]");
                    Log.i("","\n"+"[전달받은 데이터 :: "+String.valueOf(UserIdx)+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("이벤트 발생 전달")+"]");
                    Log.w("//===========//","================================================");
                    Log.i("---","---");
                    QrChecking();
                    // [서버 : window.경로.close() 요청이 들어오면 Android 에서 JS로 바로 데이터를 보내준다]
                    new Android_To_Javascript().mClose(UserIdx, courseId, date);//courseId, date
                }
            });
        }

        // [자바스크립트에서 호출하는 안드로이드 메소드]
        @JavascriptInterface
        public void get_token(String UserToken) {
            js_handler.post(new Runnable() {
                public void run() {

                    Log.i("---","---");
                    Log.w("//===========//","================================================");
                    Log.i("","\n"+"[MainActivity >> AndroidBridge :: close() [DATA] :: JS >> Android]");
                    Log.i("","\n"+"[전달받은 데이터 :: "+currentToken+"]");
                    Log.i("","\n"+"[설 명 :: "+String.valueOf("이벤트 발생 전달")+"]");
                    Log.w("//===========//","================================================");
                    Log.i("---","---");
                    QrChecking();
                    // [서버 : window.경로.close() 요청이 들어오면 Android 에서 JS로 바로 데이터를 보내준다]
                    new Android_To_Javascript().putToken(currentToken);//courseId, date
                }
            });
        }
    }

    private void QrChecking() {

        Intent intent = new Intent(MainActivity.this,ScanQR.class);
        intent.setAction("android.QR_VIEW");
        startActivity(intent);

    }

    // TODO [안드로이드 >> 자바스크립트]
    class Android_To_Javascript{
        // [mOpen - 자바스크립트 함수만 호출]
        public void mOpen(String UserIdx, String courseId, String date){
            try {
                String returnData = "ㅎㅇㅎㅇ";
                Log.i("---","---");
                Log.w("//===========//","================================================");
                Log.i("","\n"+"[MainActivity >> mOpen() [NONE] :: Android >> JS]");
                Log.i("","\n"+"[JS 함수 :: "+String.valueOf("receive_Open")+"]");
                Log.i("","\n"+"[전달할 데이터 :: "+String.valueOf(returnData)+"]");
                Log.i("","\n"+"[설 명 :: "+String.valueOf("열기 결과 값 전송")+"]");
                Log.w("//===========//","================================================");
                Log.i("---","---");

                    // [서버 : function receive_Open() { }]
                if(beaconChecking()){
                    main_webview.loadUrl("javascript:beacon_true('"+String.valueOf(returnData)+"')");
                    stopBeaconScan();
                }else {
                    main_webview.loadUrl("javascript:beacon_false('"+String.valueOf(returnData)+"')");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        // [mClose - 자바스크립트 함수 데이터 전달]
        public void mClose(String UserIdx, String courseId, String date){
            try {
                String returnData = UserIdx;
                Log.i("---","---");
                Log.w("//===========//","================================================");
                Log.i("","\n"+"[MainActivity >> mClose() [DATA] :: Android >> JS]");
                Log.i("","\n"+"[JS 함수 :: "+String.valueOf("receive_Close")+"]");
                Log.i("","\n"+"[전달할 데이터 :: "+String.valueOf(returnData)+"]");
                Log.i("","\n"+"[설 명 :: "+String.valueOf("닫기 결과 값 전송")+"]");
                Log.w("//===========//","================================================");
                Log.i("---","---");

                // [서버 : function receive_Close(value) { }]
                main_webview.loadUrl("javascript:receive_Close('"+String.valueOf(returnData)+"')");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        public void putToken(String currentToken) {
            try {
                String returnData = currentToken;
                Log.i("---","---");
                Log.w("//===========//","================================================");
                Log.i("","\n"+"[MainActivity >> mClose() [DATA] :: Android >> JS]");
                Log.i("","\n"+"[JS 함수 :: "+String.valueOf("receive_token")+"]");
                Log.i("","\n"+"[전달할 데이터 :: "+currentToken+"]");
                Log.i("","\n"+"[설 명 :: "+String.valueOf("닫기 결과 값 전송")+"]");
                Log.w("//===========//","================================================");
                Log.i("---","---");

                // [서버 : function receive_Close(value) { }]
                main_webview.loadUrl("javascript:receive_token('"+currentToken+"')");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    // TODO [액티비티 생명 주기 상태 체크 메소드]
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // [모바일 디바이스의 뒤로가기 키 이벤트가 발생한 경우]
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i("---","---");
            Log.w("//===========//","================================================");
            Log.i("","\n"+"[MainActivity >> onKeyDown() :: 백버튼 터치시 뒤로 가기 이벤트 실시]");
            Log.i("","\n"+"[MAIN_URL :: "+String.valueOf(MAIN_URL)+"]");
            Log.i("","\n"+"[getUrl() :: "+String.valueOf(main_webview.getUrl())+"]");
            Log.i("","\n"+"[getOriginalUrl() :: "+String.valueOf(main_webview.getOriginalUrl())+"]");
            Log.w("//===========//","================================================");
            Log.i("---","---");
            // [더이상 웹뷰에서 뒤로갈 페이지가 없을 경우 이거나 메인 로드 주소인 경우]
            if (main_webview.canGoBack() == false
                    || String.valueOf(main_webview.getUrl()).equals(MAIN_URL)
                    || String.valueOf(main_webview.getOriginalUrl()).equals(MAIN_URL)){
                // [액티비티 종료 실시]
                finish(); // 현재 액티비티 종료
                finishAffinity(); // 스택에 포함된 액티비티 종료
                overridePendingTransition(0,0);
            }
            // [뒤로 갈 페이지가 있는 경우]
            else {
                main_webview.goBack(); // [웹뷰 뒤로가기]
            }
        }
        return true;
    }



    // TODO [바깥 레이아웃 클릭 시 키보드 내림]
    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        int action = event.getAction();
        switch(action){
            case(MotionEvent.ACTION_DOWN):
                try {
                    //TODO [창 내리는 용도]
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case(MotionEvent.ACTION_MOVE):
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }



    // TODO [액티비티 생명 주기 상태 체크 메소드]
    @Override
    public void onResume(){
        super.onResume();
        Log.i("---","---");
        Log.d("//===========//","================================================");
        Log.i("","\n"+"[MainActivity >> onResume() :: 액티비티 실행 준비]");
        Log.d("//===========//","================================================");
        Log.i("---","---");
    }



    // TODO [액티비티 생명 주기 상태 체크 메소드]
    @Override
    public void onPause(){
        super.onPause();
        Log.i("---","---");
        Log.e("//===========//","================================================");
        Log.i("","\n"+"[MainActivity >> onPause() :: 액티비티 정지 상태]");
        Log.e("//===========//","================================================");
        Log.i("---","---");
    }



    // TODO [액티비티 생명 주기 상태 체크 메소드]
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("---","---");
        Log.e("//===========//","================================================");
        Log.i("","\n"+"[MainActivity >> onDestroy() :: 액티비티 종료 상태]");
        Log.e("//===========//","================================================");
        Log.i("---","---");
    }

    public boolean beaconChecking() {

        //startBeaconSend(sendUuid, sendMajor, sendMinor);
        onBeaconServiceConnect();
        if (attendSwitch == 1) {
            return true;
        } else {
            return false;
        }
    }

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
            stopBeaconScan();

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
                            attendSwitch = 1;
                            // TODO [비콘 스캔 정보 추출 참고]
                            /*Log.d("//===========//", "================================================");
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
                            Log.d("//===========//", "================================================");*/
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
                }else{
                    attendSwitch = 0;
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

                if(Integer.parseInt(String.valueOf(beaconFormatList.size()))>=1){
                    attendSwitch = 1;
                }else{
                    attendSwitch = 0;
                }


                // [자바스크립트로 데이터 전송 실시]
                //new Android_To_Javascript().scanList(beaconFormatList.toString());
                //TextView beacon_info = findViewById(R.id.beacon_info); // [비콘정보 출력]
                //beacon_info.setText(beaconFormatList.toString());

                // [비콘 스캔 카운트 증가]
                beaconScanCount ++;

                // [핸들러 자기 자신을 1초마다 호출]
                beaconScanHandler.sendEmptyMessageDelayed(0, 3000);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    };


}