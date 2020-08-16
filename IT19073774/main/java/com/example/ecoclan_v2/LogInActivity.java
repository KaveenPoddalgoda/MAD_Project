package com.example.ecoclan_v2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    EditText e1_email, e2_psw;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);

        e1_email = findViewById(R.id.email);
        e2_psw = findViewById(R.id.psw);
        auth = FirebaseAuth.getInstance();
        dialog =new ProgressDialog(this);
    }

    public void LogInUser (View v) {
        dialog.setMessage("Authenticating ... Please Wait!");
        dialog.show();
        if (e1_email.getText().toString().equals("") || e2_psw.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),"Error : Fields cannot be empty", Toast.LENGTH_SHORT).show();
            dialog.hide();
        } else {
            auth.signInWithEmailAndPassword(e1_email.getText().toString(),e2_psw.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.hide();
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"User Successfully SignedIn", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LogInActivity.this,HomeActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),"Invalid Email or Password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}