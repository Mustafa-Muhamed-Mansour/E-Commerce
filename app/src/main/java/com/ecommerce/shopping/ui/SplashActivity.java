package com.ecommerce.shopping.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ecommerce.shopping.R;

public class SplashActivity extends AppCompatActivity
{

    Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initThread();


    }

    private void initThread()
    {
        thread = new Thread()

        {
            @Override
            public void run()
            {
                try
                {
                    thread.sleep(3000);

                    Intent intentLogin = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentLogin);
                    finish();
                }
                catch (InterruptedException e)
                {
                    Toast.makeText(SplashActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        thread.start();
    }
}