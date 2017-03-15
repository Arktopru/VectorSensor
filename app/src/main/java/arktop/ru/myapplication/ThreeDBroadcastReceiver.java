package arktop.ru.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ThreeDBroadcastReceiver extends BroadcastReceiver {

    Draw3DActivity activity;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null && intent.getAction().equals("M_BROADCAST_S")) {

            float gyroX = intent.getFloatExtra("GYROSCOPE_X", 0);
            float gyroY = intent.getFloatExtra("GYROSCOPE_Y", 0);
            float gyroZ = intent.getFloatExtra("GYROSCOPE_Z", 0);

            if(activity != null) {
                activity.setXYZ(new float[]{gyroY, gyroX, gyroZ});
            }
        }
    }

    public void setActivity(Draw3DActivity activity) {
        this.activity = activity;
    }
}
