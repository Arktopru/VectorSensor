package ru.arktop.services;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import java.text.DecimalFormat;

public class SensorGyroscopeService extends Service implements SensorEventListener {

    private SensorManager sensorManager = null;
    private Sensor sensor = null;
    public static final int GET_GYRO = 3;
    public static final int STOP_GYRO = 4;
    private final Messenger mMessenger = new Messenger(new SensorGyroscopeService.IncomingHandler());

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case GET_GYRO:
                    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                    sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
                    sensorManager.registerListener(SensorGyroscopeService.this, sensor, SensorManager.SENSOR_DELAY_GAME);
                    break;
                case STOP_GYRO:

                    if (sensorManager != null) {
                        sensorManager.unregisterListener(SensorGyroscopeService.this);
                        stopSelf();
                    }
                default:
                    super.handleMessage(msg);
            }
        }
    }

    public void sendBroadcast(float[] values) {
        Intent intent = new Intent("M_BROADCAST_S");
        intent.putExtra("GYROSCOPE_X", values[0]);
        intent.putExtra("GYROSCOPE_Y", values[1]);
        intent.putExtra("GYROSCOPE_Z", values[2]);
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] gyroscopeVal = event.values;
        sendBroadcast(gyroscopeVal);
    }
}
