package com.VoipAppp.wifimessenger.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.VoipAppp.wifimessenger.services.LongRunningService;
import com.VoipAppp.wifimessenger.utils.AppGlobals;

public class BootStateListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (AppGlobals.isServiceOn()) {
            context.startService(new Intent(context, LongRunningService.class));
        }
    }
}
