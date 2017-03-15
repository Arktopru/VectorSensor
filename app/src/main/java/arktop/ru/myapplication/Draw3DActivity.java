package arktop.ru.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import ru.arktop.services.SensorGyroscopeService;

public class Draw3DActivity extends AppCompatActivity {

    private Messenger mServiceGyro;
    private boolean mBoundGyro = false;

    private ThreeDBroadcastReceiver mBroadcastReciever;
    private Draw3D draw3D;

    private float screenWidth;
    private float screenHeight;

    private boolean isSensorOn = false;

    private Button plusButtonX;
    private Button minusButtonX;

    private Button plusButtonY;
    private Button minusButtonY;

    private Button plusButtonZ;
    private Button minusButtonZ;

    private Button axesB;
    private boolean isAhowAxes = true;
    private Button vertexB;
    private boolean isShowVertex = true;
    private Button figureB;
    private boolean isShowFigure = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw3_d);

        mBroadcastReciever = new ThreeDBroadcastReceiver();
        mBroadcastReciever.setActivity(this);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;

        draw3D = new Draw3D(
                this,
                screenHeight,
                screenWidth,
                screenWidth / 2,
                screenHeight / 2
        );
        draw3D.setAxisLength(150);
        draw3D.setDrawAxes(true);
        draw3D.setDrawFigure(true);
        draw3D.setDrawVertex(true);

        draw3D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSensorOn) {
                    Draw3DActivity.this.stopGyro();
                    isSensorOn = false;
                } else {
                    Draw3DActivity.this.getGyro();
                    isSensorOn = true;
                }
            }
        });
        FrameLayout main = (FrameLayout) findViewById(R.id.mainView);
        main.addView(draw3D);

        LinearLayout buttons = (LinearLayout) findViewById(R.id.buttons);
        buttons.bringToFront();
        final int angleDelta = 10;
        plusButtonX = (Button) findViewById(R.id.plusX);
        plusButtonX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw3D.setxAxisAngle(draw3D.getxAxisAngle() + angleDelta);
                draw3D.invalidate();
            }
        });
        minusButtonX = (Button) findViewById(R.id.minusX);

        minusButtonX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw3D.setxAxisAngle(draw3D.getxAxisAngle() - angleDelta);
                draw3D.invalidate();
            }
        });
        plusButtonY = (Button) findViewById(R.id.plusY);
        plusButtonY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw3D.setyAxisAngle(draw3D.getyAxisAngle() + angleDelta);
                draw3D.invalidate();
            }
        });
        minusButtonY = (Button) findViewById(R.id.minusY);
        minusButtonY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw3D.setyAxisAngle(draw3D.getyAxisAngle() - angleDelta);
                draw3D.invalidate();
            }
        });
        plusButtonZ = (Button) findViewById(R.id.plusZ);
        plusButtonZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw3D.setzAxisAngle(draw3D.getzAxisAngle() + angleDelta);
                draw3D.invalidate();
            }
        });
        minusButtonZ = (Button) findViewById(R.id.minusZ);
        minusButtonZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw3D.setzAxisAngle(draw3D.getzAxisAngle() - angleDelta);
                draw3D.invalidate();
            }
        });
        axesB = (Button) findViewById(R.id.axesB);
        axesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw3D.setDrawAxes(isAhowAxes);

                if(isAhowAxes) {
                    isAhowAxes = false;
                } else {
                    isAhowAxes = true;
                }
                draw3D.invalidate();
            }
        });
        vertexB = (Button) findViewById(R.id.vertexB);
        vertexB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw3D.setDrawVertex(isShowVertex);

                if(isShowVertex) {
                    isShowVertex = false;
                } else {
                    isShowVertex = true;
                }
                draw3D.invalidate();
            }
        });
        figureB = (Button) findViewById(R.id.figureB);
        figureB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw3D.setDrawFigure(isShowFigure);

                if(isShowFigure) {
                    isShowFigure = false;
                } else {
                    isShowFigure = true;
                }
                draw3D.invalidate();
            }
        });

    }

    public void setXYZ(float[] arrayXYZ) {
        draw3D.setxValueFromSensor(arrayXYZ[0] * draw3D.getAxisLength());
        draw3D.setyValueFromSensor(arrayXYZ[1] * draw3D.getAxisLength());
        draw3D.setzValueFromSensor(arrayXYZ[2] * draw3D.getAxisLength());
//        draw3D.setxAxisAngle(Math.toDegrees(arrayXYZ[0]) * 2);
//        draw3D.setyAxisAngle(Math.toDegrees(arrayXYZ[1]) * 3);
//        draw3D.setzAxisAngle(Math.toDegrees(arrayXYZ[2]) * 4);

        draw3D.invalidate();
    }

    public void getGyro() {

        if (!mBoundGyro) {
            return;
        }
        Message msg = Message.obtain(null, SensorGyroscopeService.GET_GYRO, 0, 0);

        try {
            mServiceGyro.send(msg);
            draw3D.setSensorOn(true);
            draw3D.invalidate();
        } catch (RemoteException e) {
            Log.e("MAIN", e.getMessage());
        }
    }

    public void stopGyro() {

        if (!mBoundGyro) {
            return;
        }
        Message msg = Message.obtain(null, SensorGyroscopeService.STOP_GYRO, 0, 0);

        try {
            mServiceGyro.send(msg);
            draw3D.setSensorOn(false);
            draw3D.invalidate();
        } catch (RemoteException e) {
            Log.e("MAIN", e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mBroadcastReciever, new IntentFilter("M_BROADCAST_S"));

        bindService(new Intent(this, SensorGyroscopeService.class), mConnectionGyro,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcastReciever);

        if (mBoundGyro) {
            unbindService(mConnectionGyro);
            mBoundGyro = false;
        }

    }

    private ServiceConnection mConnectionGyro = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            mServiceGyro = new Messenger(service);
            mBoundGyro = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            mServiceGyro = null;
            mBoundGyro = false;
        }
    };

}
