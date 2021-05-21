 package com.example.samsunghackathon2021.ui.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsunghackathon2021.MqttHelper;
import com.example.samsunghackathon2021.R;
import com.example.samsunghackathon2021.State;
import com.example.samsunghackathon2021.StateAdapter;
import com.example.samsunghackathon2021.base.DBMatches;
import com.example.samsunghackathon2021.databinding.FragmentProfileBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

import static java.util.logging.Logger.global;

public class ProfileFragment extends Fragment {
    FloatingActionButton btn_add;
    ArrayList<State> arrayList;
    Context mContext;
    ListView mListView;
    SimpleCursorAdapter scAdapter;
    Cursor cursor;
    //myListAdapter myAdapter;

    DBMatches database_helper;
    MqttHelper mqttHelperPrefs, mqttHelperPomp;
    RecyclerView recyclerView;

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;

    int hum_ground = 0;
    boolean check = false;
    String name_plant = "";



    int a = 0;

    ArrayList<State> states = new ArrayList<State>();
    private byte[] ByteArrayOutputStream;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);


        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();






        setInitialData();

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.list);
        // создаем адаптер
        StateAdapter adapter = new StateAdapter(getContext(), states);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);

        database_helper = new DBMatches(getContext());
        displayNotes();



        btn_add = (FloatingActionButton) root.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(v);
            }
        });

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                startPompMqtt("test/mode");
            }
        };
        handler.sendEmptyMessage(0);





        return root;
    }

    public void displayNotes() {

        arrayList = new ArrayList<>(database_helper.getNotes());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        StateAdapter adapter = new StateAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);
    }


    public void showAlert(View v){

        AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
        alert.setTitle("Выбор");
        alert.setMessage("Выберите тип полива");
        alert.setButton(Dialog.BUTTON_POSITIVE,"Влажность",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Влажность", Toast.LENGTH_LONG).show();
                check = false;
                showAlertHum(v);
            }
        });

        alert.setButton(Dialog.BUTTON_NEGATIVE,"Время",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Время", Toast.LENGTH_LONG).show();
                check = true;
            }
        });

        alert.setButton(Dialog.BUTTON_NEUTRAL,"Отмена",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Отмена", Toast.LENGTH_LONG).show();
            }
        });

        alert.show();

        // показываем Alert

    }



    public void showAlertHum(View v){

        ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_hum, null);
        SeekBar humm = cl.findViewById(R.id.seekBar);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Выбор");


        builder.setPositiveButton("ОК",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), humm.getProgress() + " Влажность Выбрана", Toast.LENGTH_LONG).show();

                hum_ground = humm.getProgress();
                nameAlert(v);

            }
        });


        builder.setView(cl);
        builder.show();

    }


    public void nameAlert(View v){
        ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.name_alert, null);
        TextInputEditText namee = cl.findViewById(R.id.inputText);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Название");


        builder.setPositiveButton("ОК",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), namee.getText() + " Имя выбрано", Toast.LENGTH_LONG).show();
                name_plant = namee.getText() + "";
                database_helper.insert(name_plant, "x", "hum." + hum_ground);
                displayNotes();


            }
        });
        builder.setView(cl);
        builder.show();

    }


    private void setInitialData(){

        //states.add(new State ("Огурец", "полив если влажность 50% <", R.drawable.cucumber));
        //states.add(new State ("Помидор", "полив по пн чт вс", R.drawable.baseline_house_siding_24));
        //states.add(new State ("Колумбия", "Богота", R.drawable.baseline_agriculture_24));
        //states.add(new State ("Уругвай", "Монтевидео", R.drawable.baseline_window_24));
        //states.add(new State ("Чили", "Сантьяго", R.drawable.ic_baseline_ac_unit_24));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void startPompMqtt(String topic) {
        mqttHelperPomp = new MqttHelper(getActivity().getApplicationContext(), topic);
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

    public void sendMessage(String opcode) {
        Handler handler1 = new Handler();

        Log.i("water", " on");
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                mqttHelperPomp.publishMessage("" + opcode);
            }
        }, 10);
    }
}