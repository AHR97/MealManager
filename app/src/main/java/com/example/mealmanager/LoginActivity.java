package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView login, create;

    private Typeface typeface1, typeface2;

    private EditText login_email,login_password;

    private Button button_login;

    ProgressBar progressBar;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login=(TextView)findViewById(R.id.textView);
        create=(TextView)findViewById(R.id.createAcc);

        button_login=(Button)findViewById(R.id.loginButton);

        typeface1=Typeface.createFromAsset(getAssets(),"font/KaushanScript_Regular.otf");
        typeface2=Typeface.createFromAsset(getAssets(),"font/DancingScript_Regular.otf");

        login.setTypeface(typeface1);
        create.setTypeface(typeface2);

        login_email=(EditText)findViewById(R.id.loginEmail);
        login_password=(EditText)findViewById(R.id.loginPassword);

        progressBar=(ProgressBar)findViewById(R.id.progressbar);

        button_login.setOnClickListener(this);
        create.setOnClickListener(this);

        mAuth=FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.createAcc:
                Intent a=new Intent(LoginActivity.this,RegistrationActivity.class);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
                break;

            case R.id.loginButton:
                userLogin();
                break;
        }
    }

    private void userLogin() {

        final String email=login_email.getText().toString().trim();
        final String password=login_password.getText().toString().trim();

        if(email.length()==0)
        {
            login_email.setError("Enter your email");
            login_email.requestFocus();
            return;
        }

        else if(password.length()==0)
        {
            login_password.setError("Enter your password");
            login_password.requestFocus();
            return;
        }
        else if(password.length()<6)
        {
            login_password.setError("Password must be more than 6 letters");
            login_password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        button_login.setVisibility(View.INVISIBLE);


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Intent b=new Intent(LoginActivity.this,UserProfile.class);
                            b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(b);


                        } else {
                            progressBar.setVisibility(View.GONE);
                            button_login.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });





    }
}
