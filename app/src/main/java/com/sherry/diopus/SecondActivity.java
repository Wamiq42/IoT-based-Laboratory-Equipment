package com.sherry.diopus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity {
    //TIMER VARIABLE
    private long START_TIME_3 = 10000;
    //private long START_TIME_3 = 180000;
    private long START_TIME_5 = 300000;
    private CountDownTimer countDownTimer = null;
    private long timeLeftInMillis3 = START_TIME_3;
    private long timeLeftInMillis5 = START_TIME_5;

    ImageView imgOne, imgTwo;
    CardView cardOne, cardTwo;
    TextView tvTimer;
    Button btnStart, btnStop, btnDcBluetooth;

    BluetoothSocket socket;

    Boolean tick_one = true, tick_two = true, isRunning = false;

    String command;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
        btnDcBluetooth = findViewById(R.id.btn_dc_bluetooth);

        socket = ((GlobalBluetoothSocket)getApplication()).getGlobalSocket();

        cardOne = findViewById(R.id.card_one);
        cardTwo = findViewById(R.id.card_two);

        imgOne = findViewById(R.id.tick_one);
        imgTwo = findViewById(R.id.tick_two);

        tvTimer = findViewById(R.id.textViewTimer);

        imgTwo.setVisibility(View.GONE);
        imgOne.setVisibility(View.GONE);


        cardOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tick_one){
                    imgOne.setVisibility(View.VISIBLE);
                    String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", 00,10);
                    tvTimer.setText(timeLeftFormatted);
                    tick_one = false;
                } else if (!tick_one){
                    imgOne.setVisibility(View.GONE);
                    tick_one = true;
                    String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", 00,00);
                    tvTimer.setText(timeLeftFormatted);

                }

                if (!tick_one){

                    cardTwo.setEnabled(false);
                    cardTwo.setCardBackgroundColor(Color.parseColor("#808080"));

                } else {
                    cardTwo.setEnabled(true);
                    cardTwo.setCardBackgroundColor(Color.parseColor("#11829E"));

                }

            }
        });


        cardTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tick_two){
                    imgTwo.setVisibility(View.VISIBLE);
                    String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", 05,00);
                    tvTimer.setText(timeLeftFormatted);
                    tick_two = false;
                } else if (!tick_two){
                    imgTwo.setVisibility(View.GONE);
                    String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", 00,00);
                    tvTimer.setText(timeLeftFormatted);
                    tick_two = true;
                }

                if (!tick_two){

                    cardOne.setEnabled(false);
                    cardOne.setCardBackgroundColor(Color.parseColor("#808080"));

                } else {
                    cardOne.setEnabled(true);
                    cardOne.setCardBackgroundColor(Color.parseColor("#11829E"));

                }
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tick_one && tick_two){

                    Toast.makeText(SecondActivity.this, "10 Sec", Toast.LENGTH_SHORT).show();
                    if (socket != null){
                            command = "*run,10#";
                            try {
                                socket.getOutputStream().write(command.toString().getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (timeLeftInMillis3 < 10000) {
                            } else {
                                startTimer3();

                            }
                    }
                        cardOne.setEnabled(false);


                }
                else if (!tick_two && tick_one){
                    Toast.makeText(SecondActivity.this, "5 mins", Toast.LENGTH_SHORT).show();
                    if (socket != null){
                        command = "*run,300#";
                        try {
                            socket.getOutputStream().write(command.toString().getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    cardTwo.setEnabled(false);

                    startTimer5();
                }
                else {
                    Toast.makeText(SecondActivity.this, "Select Any preset", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (socket != null){
                   command = "*stop#";

                   try {
                       socket.getOutputStream().write(command.toString().getBytes());
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }

                if (!tick_one && tick_two){
                    cardOne.setEnabled(true);
                }
                else if (!tick_two && tick_one){
                    cardTwo.setEnabled(true);
                }
                if (isRunning == true) {
                    resetTimer3();
                    resetTimer5();
                }
            }
        });


        btnDcBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (socket != null) {

                    try {socket.close();} catch (Exception e) {}
                    socket = null;

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

                }
            }
        });


    }

    // TIMER CODING START FROM HERE

    void resetTimer3(){
        timeLeftInMillis3 = START_TIME_3;
        countDownTimer.cancel();
        tvTimer.setText("00:00");
    }

    void  resetTimer5(){
        timeLeftInMillis5 = START_TIME_5;
        countDownTimer.cancel();
        tvTimer.setText("00:00");
    }

    void startTimer3(){
        countDownTimer = new CountDownTimer(timeLeftInMillis3, 1000) {
            @Override
            public void onTick(long l) {

               timeLeftInMillis3 = l;
                updateCountdownTimer3();
                isRunning = true;

            }

            @Override
            public void onFinish() {
                cardOne.setEnabled(true);
                isRunning = false;

                if (!tick_one && tick_two){
                    cardOne.setEnabled(true);
                }
                else if (!tick_two && tick_one){
                    cardTwo.setEnabled(true);
                }
            }
        }.start();
    }

    void startTimer5(){
        countDownTimer = new CountDownTimer(timeLeftInMillis5,1000) {
            @Override
            public void onTick(long l) {

                timeLeftInMillis5 = l;
                updateCountdownTimer5();
                isRunning = true;

            }

            @Override
            public void onFinish() {
                cardTwo.setEnabled(true);
                isRunning = false;

            }
        }.start();
    }



    void updateCountdownTimer3(){
        int minutes = (int) (timeLeftInMillis3 /1000)/60;
        int seconds = (int) (timeLeftInMillis3 /1000)%60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        tvTimer.setText(timeLeftFormatted);
    }

    void updateCountdownTimer5(){
        int minutes = (int) (timeLeftInMillis5 /1000)/60;
        int seconds = (int) (timeLeftInMillis5 /1000)%60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        tvTimer.setText(timeLeftFormatted);
    }

}