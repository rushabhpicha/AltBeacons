package com.example.implementingbeacons;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    protected static final String TAG = "MainActivity";
    private BeaconManager beaconManager;  // Declaring the BeaconManager.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beaconManager = BeaconManager.getInstanceForApplication(this); // Instantiong BeaconManager. It Uses Singleton pattern.
        beaconManager.bind(this); // Binding BeaconManager with the activity.

    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.removeAllMonitorNotifiers(); // Removing all the initial notifiers.
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw the beacon for the first time");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "No beacons in the range");
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+i);
            }
        });
        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this); // Unbinding the BeaconManager from the Activity.
    }
}
