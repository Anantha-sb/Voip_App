package com.VoipAppp.wifimessenger.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.VoipAppp.wifimessenger.R;
import com.VoipAppp.wifimessenger.activities.MainActivity;
import com.VoipAppp.wifimessenger.utils.AppGlobals;
import com.VoipAppp.wifimessenger.utils.ServiceHelpers;

import java.net.InetAddress;

public class LongRunningService extends Service {

    private static LongRunningService sInstance;

    public static boolean isRunning() {
        return sInstance != null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sInstance = this;
        InetAddress ip = ServiceHelpers.getBroadcastIp();
        final String username = AppGlobals.getName();
        final String deviceId = AppGlobals.getDeviceId();
        String realMessage = String.format(
                "{\"name\": \"%s\", \"id\": \"%s\"}", username, deviceId);
        ServiceHelpers.broadcastName("ADD:", realMessage, ip);
        ServiceHelpers.startListeningForCommands();
        startForeground(999, longRunner().build());

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ServiceHelpers.stopNameBroadcast();
        ServiceHelpers.stopDiscover();
        sInstance = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private NotificationCompat.Builder longRunner() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Wifi Messenger");
        builder.setContentText("In listen mode for any incoming notifications");
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Intent startActivity = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, startActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        return builder;
    }
}
