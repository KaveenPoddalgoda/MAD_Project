package com.example.ecoclan_v2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class ResourceDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_resource_detail);

        String title = getIntent().getStringExtra("resID");
        Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
    }

    public void CollectForm(View v) {
        Intent i = new Intent(ResourceDetailActivity.this,CollectFormActivity.class);
        startActivity(i);
    }
}