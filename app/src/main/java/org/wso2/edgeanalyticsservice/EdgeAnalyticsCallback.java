package org.wso2.edgeanalyticsservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by root on 1/5/16.
 */
public abstract class EdgeAnalyticsCallback extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        callback(context, intent.getStringExtra("event"));
    }

    public abstract void callback(Context context, String event);
}
