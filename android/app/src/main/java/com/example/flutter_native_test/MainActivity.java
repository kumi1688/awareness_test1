package com.example.flutter_native_test;

import android.os.Build;

import androidx.annotation.NonNull;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.DetectedActivityResponse;
import com.google.android.gms.awareness.snapshot.HeadphoneStateResponse;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
    String _headphoneState = "";
    HashMap<String, String> _userState;

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);
        final MethodChannel channel = new MethodChannel(flutterEngine.getDartExecutor(), "com.example.flutterNativeTest");
        channel.setMethodCallHandler(handler);
    }

    private MethodChannel.MethodCallHandler handler = (methodCall, result) -> {
        if (methodCall.method.equals("getPlatformVersion")) {
            result.success("Android Version: " + Build.VERSION.RELEASE);
        }  else if(methodCall.method.equals("addition")){
            int a = methodCall.argument("a");
            int b = methodCall.argument("b");
            System.out.println("" + a + ", " + b);
            result.success(addition(a,b));
        } else if (methodCall.method.equals("getHeadphoneState")) {
            getHeadphoneState();
            result.success(_headphoneState);
        } else if (methodCall.method.equals("getUserState")){
            _userState = new HashMap<String, String>();
            getUserState();
            result.success(_userState);
        }  else {
            result.notImplemented();
        }
    };

    private void getHeadphoneState() {
        Awareness.getSnapshotClient(this).getHeadphoneState().addOnSuccessListener(new OnSuccessListener<HeadphoneStateResponse>() {
            @Override
            public void onSuccess(HeadphoneStateResponse headphoneStateResponse) {
                int state = headphoneStateResponse.getHeadphoneState().getState();

                if (state == HeadphoneState.PLUGGED_IN) {
                    _headphoneState = "헤드폰 연결 됨";
                } else if (state == HeadphoneState.UNPLUGGED) {
                    _headphoneState = "헤드폰 연결 되지 않음";
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                    _headphoneState = "헤드폰 상태 가져올 수 없음";
            }
        });
    }

    private void getUserState(){
        Awareness.getSnapshotClient(this).getDetectedActivity()
                .addOnSuccessListener(new OnSuccessListener<DetectedActivityResponse>() {
                    @Override
                    public void onSuccess(DetectedActivityResponse dar) {
                        ActivityRecognitionResult arr = dar.getActivityRecognitionResult();

                        DetectedActivity probableActivity = arr.getMostProbableActivity();

                        int confidence = probableActivity.getConfidence();
                        String activityStr = probableActivity.toString();
                        _userState.put("activity", probableActivity.toString());
                        _userState.put("confidence", "" + probableActivity.getConfidence());
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
            }
        });
    }

    private int addition(int a, int b){
        return a+b;
    }
}
