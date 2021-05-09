package com.example.samsunghackathon2021.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.samsunghackathon2021.MainActivity;
import com.example.samsunghackathon2021.MqttHelper;
import com.example.samsunghackathon2021.PreferencesActivity;
import com.example.samsunghackathon2021.R;
import com.example.samsunghackathon2021.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.example.samsunghackathon2021.databinding.ActivityMainBinding;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;



import android.os.Bundle;
import android.view.View;
import android.view.Menu;



import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


public class HomeFragment extends Fragment {

    MqttHelper mqttHelperPrefs, mqttHelperPomp;

    TextView dataReceived;
    TextView temperature, temperature2, percent1, percent2;
    RadioButton btn_manual, btn_ontime, btn_auto;
    ProgressBar ground, air;

    FloatingActionButton btn_water;

    ImageButton btn_settings;

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);



        View va = inflater.inflate(R.layout.fragment_home, container, false);
        temperature = (TextView) va.findViewById(R.id.temperature);// findViewById(R.id.temperature);
        dataReceived = (TextView) va.findViewById(R.id.dataReceived);
        //btn_manual = (RadioButton) va.findViewById(R.id.radio_manual);
        //btn_ontime = (RadioButton) va.findViewById(R.id.radio_ontime);
        //btn_auto = (RadioButton) va.findViewById(R.id.radio_auto);
        percent1 = (TextView) va.findViewById(R.id.percent1);
        percent2 = (TextView) va.findViewById(R.id.percent2);

                /*final TextView textView = binding.te;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */
        //btn_manual.setOnClickListener(radioButtonClickListener);
        //btn_ontime.setOnClickListener(radioButtonClickListener);
        //btn_auto.setOnClickListener(radioButtonClickListener);

        //btn_settings = (ImageButton) va.findViewById(R.id.settings_button);

        ground = (ProgressBar) va.findViewById(R.id.progressGroundHum);
        air = (ProgressBar) va.findViewById(R.id.progressAirHum);

        //btn_water = (FloatingActionButton) va.findViewById(R.id.btn_watering);

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                startPrefsMqtt("test/temp");
                startPompMqtt("test/pomp");
            }
        };
        handler.sendEmptyMessage(0);

        /*
        btn_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(4);
            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PreferencesActivity.class);
                startActivity(i);
            }
        })*/

        return va;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void startPrefsMqtt(String topic) {
        mqttHelperPrefs = new MqttHelper(getActivity().getApplicationContext(),topic);
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
        mqttHelperPomp = new MqttHelper(getActivity().getApplicationContext(),topic);
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


    /*View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
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
    }*/
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