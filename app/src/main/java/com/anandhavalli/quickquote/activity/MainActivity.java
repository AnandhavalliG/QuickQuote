package com.anandhavalli.quickquote.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.anandhavalli.quickquote.R;
import com.anandhavalli.quickquote.constants.QuoteClass;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView quoteText;
    private SensorManager mSensorManager;

    private boolean isShaken = false;
    private long lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quoteText = (TextView) findViewById(R.id.quoteText);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;
        if (accelationSquareRoot >= 2) {
            if (actualTime - lastUpdate < 5000) {
                return;
            }
            lastUpdate = actualTime;
            if (isShaken) {
                getNewQuote();
            }
            isShaken = !isShaken;
        }
    }

    private void getNewQuote() {
        Random rand = new Random();
        int n = rand.nextInt(QuoteClass.QUOTES.length);
        quoteText.setText(QuoteClass.QUOTES[n]);
        animateText();
    }

    private void animateText() {
        Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1200);
        alphaAnimation.setFillAfter(true);
        quoteText.startAnimation(alphaAnimation);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }
}