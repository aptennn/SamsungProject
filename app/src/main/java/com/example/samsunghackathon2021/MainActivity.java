package com.example.samsunghackathon2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MainActivity extends AppCompatActivity {
    MqttHelper mqttHelperPrefs, mqttHelperPomp;

    TextView dataReceived;
    TextView temperature, percent1, percent2;
    RadioButton btn_manual, btn_ontime, btn_auto;
    ProgressBar ground, air;

    FloatingActionButton btn_water;

    ImageButton btn_settings;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temperature = (TextView) findViewById(R.id.temperature);
        dataReceived = (TextView) findViewById(R.id.dataReceived);
        btn_manual = (RadioButton) findViewById(R.id.radio_manual);
        btn_ontime = (RadioButton) findViewById(R.id.radio_ontime);
        btn_auto = (RadioButton) findViewById(R.id.radio_auto);
        percent1 = (TextView) findViewById(R.id.percent1);
        percent2 = (TextView) findViewById(R.id.percent2);
        btn_manual.setOnClickListener(radioButtonClickListener);
        btn_ontime.setOnClickListener(radioButtonClickListener);
        btn_auto.setOnClickListener(radioButtonClickListener);

        btn_settings = (ImageButton) findViewById(R.id.settings_button);

        ground = (ProgressBar) findViewById(R.id.progressGroundHum);
        air = (ProgressBar) findViewById(R.id.progressAirHum);

        btn_water = (FloatingActionButton) findViewById(R.id.btn_watering);

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                startPrefsMqtt("test/temp");
                startPompMqtt("test/pomp");
            }
        };
        handler.sendEmptyMessage(0);


        btn_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(4);
            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PreferencesActivity.class);
                startActivity(i);
            }
        });
    }

    private void startPrefsMqtt(String topic) {
        mqttHelperPrefs = new MqttHelper(getApplicationContext(),topic);
        mqttHelperPrefs.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                dataReceived.setText(mqttMessage.toString());
                //Toast t = Toast.makeText(getApplicationContext(),mqttMessage.toString(),Toast.LENGTH_SHORT);
                //t.show();
                String[] answer = mqttMessage.toString().split(" ");
                double d_temp = Double.valueOf(answer[0]);
                double d_air_hum = Double.valueOf(answer[1]);
                double d_ground_hum = Double.valueOf(answer[2]);
                int temp = (int) Math.ceil(d_temp);
                int air_hum = (int) Math.ceil(d_air_hum);
                int ground_hum = (int) Math.ceil(d_ground_hum);
                temperature.setText("+" + temp);

                ground.setProgress(ground_hum);
                percent1.setText(ground_hum+"%");
                air.setProgress(air_hum);
                percent2.setText(air_hum+"%");

                if(ground_hum<20){
                    sendMessage(4);
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }
    private void startPompMqtt(String topic) {
        mqttHelperPomp = new MqttHelper(getApplicationContext(),topic);
        mqttHelperPomp.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                //dataReceived.setText(mqttMessage.toString());
                String[] answer = mqttMessage.toString().split(" ");


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            clearRadioChecked();
            btn_water.setVisibility(View.INVISIBLE);
            switch (rb.getId()) {
                case R.id.radio_manual:
                    btn_manual.setChecked(!btn_manual.isChecked());
                    sendMessage(1);
                    btn_water.setVisibility(View.VISIBLE);
                    break;
                case R.id.radio_ontime:
                    btn_ontime.setChecked(!btn_ontime.isChecked());
                    sendMessage(2);
                    break;
                case R.id.radio_auto:
                    btn_auto.setChecked(!btn_auto.isChecked());
                    sendMessage(3);
                    break;

                default:
                    break;
            }
        }
    };
    public void clearRadioChecked() {
        btn_manual.setChecked(false);
        btn_auto.setChecked(false);
        btn_ontime.setChecked(false);
    }
    public void sendMessage(int opcode){
        Handler handler1 = new Handler();

        Log.i("water", " on");
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                mqttHelperPomp.publishMessage(""+opcode);
            }
        },10);
    }
}