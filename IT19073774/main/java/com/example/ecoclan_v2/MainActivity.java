package com.example.ecoclan_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            Intent i = new Intent (MainActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }
        else {
            setContentView(R.layout.activity_main);
        }
    }
    public void openLoginActivity(View v) {
        Intent i = new Intent(MainActivity.this,LogInActivity.class);
        startActivity(i);
    }
    public void openSignupActivity(View v) {
        Intent i = new Intent(MainActivity.this,SignUpActivity.class);
        startActivity(i);
    }
}