package com.erp.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;



public class GooglePlay extends Application {

    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        upgradeSecurityProvider();
    }

    private void upgradeSecurityProvider() {
        ProviderInstaller.installIfNeededAsync(this, new ProviderInstaller.ProviderInstallListener() {
            @Override
            public void onProviderInstalled() {

            }

            @Override
            public void onProviderInstallFailed(int errorCode, Intent recoveryIntent) {
                GooglePlayServicesUtil.showErrorNotification(errorCode, context);
            }
        });
    }
}