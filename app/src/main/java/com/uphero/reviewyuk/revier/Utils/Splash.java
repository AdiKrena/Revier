/*
Licensed by 2YoungDev
Copyleft 2017-2018
Surabaya 09-07-2017
*/

package com.uphero.reviewyuk.revier.Utils;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.uphero.reviewyuk.revier.Login.LoginActivity;
import com.uphero.reviewyuk.revier.R;

//This Script
public class Splash extends Activity {

    //Set waktu lama splashscreen
    private static int waktusplash = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.splash);

        //Script Animasi
        TextView judul = (TextView) findViewById(R.id.judul);
        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        judul.setAnimation(anim1);

        ImageView glass = (ImageView) findViewById(R.id.glass);
        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.move_up);
        glass.setAnimation(anim2);

        //Script Splash_Screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // Input First Intent with this Java Script Name, Then second java script activity
                Intent i = new Intent(Splash.this, LoginActivity.class);
                startActivity(i);

                //jeda selesai Splashscreen
                this.finish();
            }
            private void finish() {
            }
        }, waktusplash);
    }
}