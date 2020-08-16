package com.example.ecoclan_v2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText firstName, lastName, email, address, contact, city, psw, repsw;
    FirebaseFirestore db;
    FirebaseAuth auth;
    Spinner spinner;
    ProgressDialog dialog;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        contact = findViewById(R.id.contact);
        city = findViewById(R.id.city);
        psw = findViewById(R.id.psw);
        repsw = findViewById(R.id.rePsw);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        dialog =new ProgressDialog(this);

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<>();
        categories.add("Collector");
        categories.add("Recycler");
        categories.add("Thrower");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }
    public void backToMainActivity(View v) {
        Intent i = new Intent(SignUpActivity.this,MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        userType = spinner.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void registerUser (View v) {
        final String fName, lName, ema, addr, cont, cit, ps, reps;
        fName = firstName.getText().toString();
        lName = lastName.getText().toString();
        ema = email.getText().toString();
        addr = address.getText().toString();
        cont = contact.getText().toString();
        cit = city.getText().toString();
        ps = psw.getText().toString();
        reps = repsw.getText().toString();

        dialog.setMessage("Registering ... Please Wait!");
        dialog.show();
        if (fName.equals("") || lName.equals("") || ema.equals("") || addr.equals("") || cont.equals("") || cit.equals("") || ps.equals("") || reps.equals("")) {
            Toast.makeText(getApplicationContext(),"Error : Fields cannot be empty", Toast.LENGTH_SHORT).show();
            dialog.hide();
        }
        else if (ps.equals(reps)){


            auth.createUserWithEmailAndPassword(ema, ps)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Map<String, Object> user = new HashMap<>();
                                user.put("FirstName", fName);
                                user.put("LastName", lName);
                                user.put("Email", ema);
                                user.put("Address", addr);
                                user.put("Contact", cont);
                                user.put("City", cit);
                                user.put("Type",userType);
                                user.put("Password", ps);

                                db.collection("Users").document(ema).set(user);
                                Toast.makeText(getApplicationContext(), "User Registered Successfully!", Toast.LENGTH_SHORT).show();
                                dialog.hide();
                                Intent i = new Intent(SignUpActivity.this,MainActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), "Error : User Not registered", Toast.LENGTH_SHORT).show();
                                dialog.hide();
                                Intent i = new Intent(SignUpActivity.this,MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Error : Passwords does not match", Toast.LENGTH_SHORT).show();
            dialog.hide();
        }
    }
}