package com.example.ecoclan_v2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SellResourceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sell_resource);
    }

    public void PublishSellForm(View view) {
        Intent i = new Intent(SellResourceActivity.this,HomeActivity.class);
        Toast.makeText(getApplicationContext(), "Published Sell Request form to the Recycling Organization!", Toast.LENGTH_SHORT).show();
        startActivity(i);
    }
}