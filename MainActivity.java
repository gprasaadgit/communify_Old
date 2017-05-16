package com.gap22.community.apartment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private Button Login;
    private EditText Email;
    private EditText Pwd;
    private ProgressDialog progress;
    private FirebaseAuth fireauth;
    private TextView newuser;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Login =(Button)findViewById(R.id.login);
        Email =(EditText)findViewById(R.id.email);
        Pwd =(EditText) findViewById(R.id.password);
        newuser =(TextView)findViewById(R.id.NewUser);
        progress = new ProgressDialog(this);
        fireauth = FirebaseAuth.getInstance();


newuser.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent menu = new Intent(MainActivity.this, UserCreation.class);

        finish();
        startActivity(menu);

        return;

    }
});
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final String email = Email.getText().toString().trim();
                String password = Pwd.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(MainActivity.this,"Please Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(MainActivity.this,"Please Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }

                progress.setMessage("Loggin in Please Wait");
                progress.show();
                fireauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progress.dismiss();
                        if(task.isSuccessful()) {

                                Intent menu = new Intent(MainActivity.this, ViewPostActivity.class);

                            finish();
                                startActivity(menu);

                                return;



                        }
                        else
                        {

                            Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });
    }




}
