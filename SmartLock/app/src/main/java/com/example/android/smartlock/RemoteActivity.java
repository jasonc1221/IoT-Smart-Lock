package com.example.android.smartlock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.utils.Async;


public class RemoteActivity extends Activity {

    private static final String ARG_DEVICEID = "ARG_DEVICEID";

    public static Intent buildIntent(Context ctx, Integer value, String deviceid) {
        Intent intent = new Intent(ctx, StartActivity.class);
        intent.putExtra(ARG_DEVICEID, deviceid);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        final Button lock = (Button) findViewById(R.id.but_lock);
        final Button unlock = (Button) findViewById(R.id.but_unlock);
        final int green = Color.parseColor("#76FF03");
        final int gray = Color.parseColor("#E0E0E0");

        unlock.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Async.executeAsync(ParticleCloud.get(RemoteActivity.this), new Async.ApiWork<ParticleCloud, Integer>() {
                    @Override
                    public Integer callApi(ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                        ParticleDevice device = particleCloud.getDevice(getIntent().getStringExtra(ARG_DEVICEID));
                        List<String> list = new ArrayList<>();
                        list.add("unlock");
                        try{
                            return device.callFunction("checkLock", list);
                        }catch(ParticleDevice.FunctionDoesNotExistException e){
                            Log.e("ERR", e.toString());
                        }
                        return -2;
                    }

                    public void onSuccess(Integer result) {
                        unlock.setBackgroundColor(green);
                        lock.setBackgroundColor(gray);
                    }
                    public void onFailure(ParticleCloudException value) {
                        Log.e("ERR", "Unlock Button Fail : " + value.toString());
                    }
                });
            }
        });

        lock.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Async.executeAsync(ParticleCloud.get(RemoteActivity.this), new Async.ApiWork<ParticleCloud, Integer>() {
                    @Override
                    public Integer callApi(ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                        ParticleDevice device = particleCloud.getDevice(getIntent().getStringExtra(ARG_DEVICEID));
                        List<String> list = new ArrayList<>();
                        list.add("lock");
                        try{
                            return device.callFunction("checkLock", list);
                        }catch(ParticleDevice.FunctionDoesNotExistException e){
                            Log.e("ERR", e.toString());
                        }
                        return -2;
                    }

                    public void onSuccess(Integer result) {
                        unlock.setBackgroundColor(gray);
                        lock.setBackgroundColor(green);
                    }
                    public void onFailure(ParticleCloudException value) {
                        Log.e("ERR", "Lock Button Fail : " + value.toString());
                    }
                });
            }
        });
    }
}
