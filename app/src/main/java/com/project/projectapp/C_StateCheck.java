package com.project.projectapp;


import static android.content.Context.KEYGUARD_SERVICE;

import android.app.KeyguardManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.util.Log;

public class C_StateCheck {


    /**
     * TODO [클래스 설명]
     * 1. 기능 활성 비활성 체크 클래스
     * 2. getBleGpsStateCheck : 블루투스 및 gps 활성 여부 체크
     * 3. getWhatOfNetwork : 네트워크 활성 여부 체크
     * 4. getMobileLock : 모바일 잠금 화면 설정 상태 확인
     * 5. getNfcState : NFC 활성 상태 확인
     * */



    // TODO [블루투스 지원 기기 및 GPS 활성 상태 확인 실시]
    public static Boolean getBleGpsStateCheck(Context mContext) {
        boolean state_result = false;
        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter == null){ // [블루투스를 지원하는 기기인지 확인]
                Log.i("---","---");
                Log.e("//===========//","================================================");
                Log.i("","\n"+"[C_StateCheck >> getBleGpsStateCheck() :: 블루투스 지원 기기 확인]");
                Log.i("","\n"+"[디바이스 :: 블루투스를 지원하지 않는 기기]");
                Log.e("//===========//","================================================");
                Log.i("---","---");
            }
            else { // [블루투스가 켜져있는지 확인]
                Log.i("---","---");
                Log.d("//===========//","================================================");
                Log.i("","\n"+"[C_StateCheck >> getBleGpsStateCheck() :: 블루투스 지원 기기 확인]");
                Log.i("","\n"+"[디바이스 :: 블루투스를 지원하는 기기]");
                Log.d("//===========//","================================================");
                Log.i("---","---");
                if(mBluetoothAdapter.isEnabled() == true){
                    Log.i("---","---");
                    Log.w("//===========//","================================================");
                    Log.i("","\n"+"[C_StateCheck >> getBleGpsStateCheck() :: 블루투스 기능 활성 확인]");
                    Log.i("","\n"+"[상태 :: 블루투스 기능 활성]");
                    Log.w("//===========//","================================================");
                    Log.i("---","---");

                    // [GPS 활성 상태 확인 실시]
                    try {
                        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){ // [위치 권한 비활성인 경우]
                            Log.i("---","---");
                            Log.e("//===========//","================================================");
                            Log.i("","\n"+"[C_StateCheck >> getBleGpsStateCheck() :: GPS 위치 권한 활성 여부 확인]");
                            Log.i("","\n"+"[상태 :: 비활성]");
                            Log.e("//===========//","================================================");
                            Log.i("---","---");
                        }
                        else { // [위치 권한 활성인 경우]
                            Log.i("---","---");
                            Log.w("//===========//","================================================");
                            Log.i("","\n"+"[C_StateCheck >> getBleGpsStateCheck() :: GPS 위치 권한 활성 여부 확인]");
                            Log.i("","\n"+"[상태 :: 활성]");
                            Log.w("//===========//","================================================");
                            Log.i("---","---");
                            state_result = true;
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else {
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[C_StateCheck >> getBleGpsStateCheck() :: 블루투스 기능 활성 확인]");
                    Log.i("","\n"+"[상태 :: 블루투스 기능 비활성]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return state_result;
    }



    // TODO [현재 연결된 네트워크 상태 확인 메소드]
    public static Boolean getWhatOfNetwork(Context mContext){
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    Log.i("---","---");
                    Log.d("//===========//","================================================");
                    Log.i("","\n"+"[C_StateCheck >> getWhatOfNetwork() :: 현재 사용중인 네트워크 상태 확인 실시]");
                    Log.i("","\n"+"["+"연결 상태 :: "+String.valueOf("와이파이")+"]");
                    Log.d("//===========//","================================================");
                    Log.i("---","---");
                    return true;
                }
                else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    Log.i("---","---");
                    Log.d("//===========//","================================================");
                    Log.i("","\n"+"[C_StateCheck >> getWhatOfNetwork() :: 현재 사용중인 네트워크 상태 확인 실시]");
                    Log.i("","\n"+"["+"연결 상태 :: "+String.valueOf("모바일")+"]");
                    Log.d("//===========//","================================================");
                    Log.i("---","---");
                    return true;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Log.i("---","---");
        Log.e("//===========//","================================================");
        Log.i("","\n"+"[C_StateCheck >> getWhatOfNetwork() :: 현재 사용중인 네트워크 상태 확인 실시]");
        Log.i("","\n"+"["+"연결 상태 :: "+String.valueOf("없음")+"]");
        Log.e("//===========//","================================================");
        Log.i("---","---");
        return false;
    }



    // TODO [모바일 잠금 설정 상태 확인]
    public static Boolean getMobileLock(Context mContext){
        try {
            KeyguardManager keyguardManager = (KeyguardManager) mContext.getSystemService(KEYGUARD_SERVICE);
            if (keyguardManager.isKeyguardSecure() == true) {
                Log.i("---","---");
                Log.d("//===========//","================================================");
                Log.i("","\n"+"[C_StateCheck >> getMobileLock() :: 모바일 잠금 설정 상태 확인]");
                Log.i("","\n"+"["+"잠금 상태 :: "+String.valueOf("잠금 화면 설정 됨")+"]");
                Log.d("//===========//","================================================");
                Log.i("---","---");
                return true;
            }
            else {
                Log.i("---","---");
                Log.e("//===========//","================================================");
                Log.i("","\n"+"[C_StateCheck >> getMobileLock() :: 모바일 잠금 설정 상태 확인]");
                Log.i("","\n"+"["+"잠금 상태 :: "+String.valueOf("잠금 화면 설정 안됨")+"]");
                Log.e("//===========//","================================================");
                Log.i("---","---");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }



    // TODO [nfc 활성 상태 확인]
    public static Boolean getNfcState(Context mContext){
        boolean returnData = false;
        try {
            NfcAdapter nfcAdapter = null;
            nfcAdapter = NfcAdapter.getDefaultAdapter(mContext);
            if(nfcAdapter == null){ // [NFC를 지원하지 않는 기기인지 확인]
                Log.i("---","---");
                Log.e("//===========//","================================================");
                Log.i("","\n"+"[C_StateCheck >> getNfcState() :: NFC 지원하지 않는 모바일 기기]");
                Log.e("//===========//","================================================");
                Log.i("---","---");
            }
            else { //TODO NFC가 켜져있는지 확인 [NFC 지원 기기]
                Log.i("---","---");
                Log.d("//===========//","================================================");
                Log.i("","\n"+"[C_StateCheck >> getNfcState() :: NFC 지원하는 모바일 기기]");
                Log.d("//===========//","================================================");
                Log.i("---","---");
                if(nfcAdapter.isEnabled() == true){
                    Log.i("---","---");
                    Log.w("//===========//","================================================");
                    Log.i("","\n"+"[C_StateCheck >> getNfcState() :: NFC 활성 상태]");
                    Log.w("//===========//","================================================");
                    Log.i("---","---");
                    return true;
                }
                else {
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"[C_StateCheck >> getNfcState() :: NFC 비활성 상태]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");
                }
            }
        }
        catch (Exception e){
            Log.i("---","---");
            Log.e("//===========//","================================================");
            Log.i("","\n"+"[C_StateCheck >> getNfcState() :: NFC 지원하지 않는 모바일 기기]");
            Log.i("","\n"+"[Catch 메시지 :: "+String.valueOf(e.getMessage())+"]");
            Log.e("//===========//","================================================");
            Log.i("---","---");
        }
        return false;
    }

} // TODO [클래스 종료]

