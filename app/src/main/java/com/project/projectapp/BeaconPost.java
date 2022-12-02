package com.project.projectapp;

import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Arrays;

public class BeaconPost extends MainActivity {

    // TODO [비콘 신호 활성 관련 전역 변수]
    Beacon beacon; // [비콘 객체]
    BeaconParser beaconParser; // [비콘 파서 객체]
    BeaconTransmitter beaconTransmitter; // [BeaconTransmitter]
    String sendUuid = ""; // [UUID : 비콘 신호 활성 시 사용하는 값]
    String sendMajor = ""; // [MAJOR : 비콘 신호 활성 시 사용하는 값]
    String sendMinor = ""; // [MINOR : 비콘 신호 활성 시 사용하는 값]


    //[4]. 비콘 신호 활성 및 종료
    // TODO [SEARCH FAST] : [비콘 신호 활성]

    public void startBeaconSend(final String UUID, final String MAJOR, final String MINOR){
        Log.i("---","---");
        Log.d("//===========//","================================================");
        Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >> startBeaconSend() :: 비콘 신호 활성 수행 실시]");
        Log.i("","\n"+"[UUID :: "+String.valueOf(UUID)+"]");
        Log.i("","\n"+"[MAJOR :: "+String.valueOf(MAJOR)+"]");
        Log.i("","\n"+"[MINOR :: "+String.valueOf(MINOR)+"]");
        Log.d("//===========//","================================================");
        Log.i("---","---");

        // ------------------------------------------
        // [이미 비콘 신호 활성이 진행 중인 경우 먼저 종료 위함]
        try {
            if (beaconTransmitter != null) {
                stopBeaconSend();
            }
        }
        catch (Exception e) {
            //e.printStackTrace();
        }
        // ------------------------------------------
        // [비콘 신호 활성 수행 실시]
        try {
            // [beacon 객체 설정 실시]
            beacon = new org.altbeacon.beacon.Beacon.Builder()
                    .setId1(UUID.toLowerCase()) // [UUID 지정 : 소문자]
                    //.setId1(UUID.toUpperCase()) // [UUID 지정 : 대문자]
                    .setId2(MAJOR) // [major 지정]
                    .setId3(MINOR) // [minor 지정]
                    .setManufacturer(0x004c) // [제조사 지정 : IOS 호환]
                    //.setManufacturer(0x0118) // [제조사 지정]
                    .setTxPower(-59) // [신호 세기]
                    //.setTxPower(59) //[신호 세기]
                    .setDataFields(Arrays.asList(new Long[]{0L})) // [레이아웃 필드]
                    .build();

            // [레이아웃 지정 : IOS 호환 (ibeacon)]
            beaconParser = new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
            //beaconParser = new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");

            // [비콘 신호 활성 상태 확인 실시]
            beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
            beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {
                @Override
                public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                    super.onStartSuccess(settingsInEffect);
                    Log.i("---","---");
                    Log.w("//===========//","================================================");
                    Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >> startBeaconSend() :: 비콘 신호 활성 [성공]]");
                    Log.i("","\n"+"[UUID :: "+String.valueOf(UUID.toLowerCase())+"]");
                    Log.i("","\n"+"[MAJOR :: "+String.valueOf(MAJOR)+"]");
                    Log.i("","\n"+"[MINOR :: "+String.valueOf(MINOR)+"]");
                    // Log.i("","\n"+"[시작 시간 :: "+String.valueOf(C_Util.getNowDateTime24())+"]");
                    Log.w("//===========//","================================================");
                    Log.i("---","---");

                    // [팝업창 호출 실시]
//                    activityShowAlert(
//                            0, // [블루투스 신호 활성 종료]
//                            "[비콘 신호 활성]",
//                            "[정상] 비콘 신호를 활성했습니다." + "\n" + "\n"
//                                    + "UUID : " + String.valueOf(UUID.toLowerCase()) + "\n" + "\n"
//                                    + "MAJOR : " + String.valueOf(MAJOR) + "\n" + "\n"
//                                    + "MINOR : " + String.valueOf(MINOR) + "\n",
//                            "종료",
//                            "",
//                            "");
                }
                @Override
                public void onStartFailure(int errorCode) {
                    super.onStartFailure(errorCode);
                    Log.i("---","---");
                    Log.e("//===========//","================================================");
                    Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >> startBeaconSend() :: 비콘 신호 활성 [실패]]");
                    Log.i("","\n"+"[UUID :: "+String.valueOf(UUID)+"]");
                    Log.i("","\n"+"[MAJOR :: "+String.valueOf(MAJOR)+"]");
                    Log.i("","\n"+"[MINOR :: "+String.valueOf(MINOR)+"]");
                    Log.i("","\n"+"[Error :: "+String.valueOf(errorCode)+"]");
                    Log.e("//===========//","================================================");
                    Log.i("---","---");

                    // [팝업창 호출 실시]
//                    activityShowAlert(
//                            0, // [블루투스 신호 활성 종료]
//                            "[비콘 신호 활성]",
//                            "[에러] 비콘 신호 활성에 문제가 발생했습니다." + "\n" + "\n"
//                                    + "errorCode : " + String.valueOf(errorCode) + "\n",
//                            "확인",
//                            "",
//                            "");
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        // ------------------------------------------
    }


    // TODO [SEARCH FAST] : [비콘 신호 활성]
    public void stopBeaconSend(){
        Log.i("---","---");
        Log.e("//===========//","================================================");
        Log.i("","\n"+"["+String.valueOf(ACTIVITY_NAME)+" >> stopBeaconSend() :: 비콘 신호 활성 종료 실시]");
        Log.e("//===========//","================================================");
        Log.i("---","---");
        try {
            // [변수 값 초기화 지정 실시]
            beacon = null;
            if (beaconTransmitter != null) {
                beaconTransmitter.stopAdvertising();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
