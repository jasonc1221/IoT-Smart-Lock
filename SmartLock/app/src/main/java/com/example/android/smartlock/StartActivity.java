package com.example.android.smartlock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.utils.Async;
import io.particle.android.sdk.utils.Toaster;


/**
 * Created by Jason Chang on 7/1/2017.
 */

public class StartActivity extends Activity {

    private static final String ARG_DEVICEID = "ARG_DEVICEID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViewById(R.id.connect_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login(){
        Async.executeAsync(ParticleCloud.get(StartActivity.this), new Async.ApiWork<ParticleCloud, Object>() {

            private ParticleDevice mDevice;

            @Override
            public Object callApi(ParticleCloud sparkCloud) throws ParticleCloudException, IOException {
                sparkCloud.logIn("username@email.com", "PASSWORD");
                mDevice = sparkCloud.getDevice("DEVICE_ID");
                return -1;
            }

            @Override
            public void onSuccess(Object value) {
                Toaster.l(StartActivity.this, "Logged in");
                // Checks if we can connect to device after logging on
                if(mDevice.isConnected()){
                    Intent intent = new Intent(StartActivity.this, RemoteActivity.class);
                    intent.putExtra(ARG_DEVICEID, "DEVICE_ID");
                    startActivity(intent);
                }else{
                    Toaster.l(StartActivity.this, "Unable to connect device");
                }
            }

            @Override
            public void onFailure(ParticleCloudException e) {
                Toaster.l(StartActivity.this, "Something has gone horribly wrong!!! ");
            }
        });
    }
}

