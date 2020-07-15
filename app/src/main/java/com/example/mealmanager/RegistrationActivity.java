package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView details, click;
    private Button create;

    private EditText fullName,userName,department,password,email;

    private Typeface typeface;

    private Spinner session;

    private String[] year;

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        details=(TextView)findViewById(R.id.textView);
        click=(TextView)findViewById(R.id.clickHere);

        create=(Button)findViewById(R.id.createAccount);
        create.setOnClickListener(this);

        typeface= Typeface.createFromAsset(getAssets(),"font/Sofia_Regular.otf");
        details.setTypeface(typeface);

        create.setOnClickListener(this);
        click.setOnClickListener(this);

        year=getResources().getStringArray(R.array.Session);

        session=(Spinner)findViewById(R.id.regSession);

        ArrayAdapter<String> s_year=new ArrayAdapter<>(this,R.layout.session_layout,R.id.regSessionSpinner,year);
        session.setAdapter(s_year);

        fullName=(EditText)findViewById(R.id.regFullName);
        userName=(EditText)findViewById(R.id.regUserName);
        department=(EditText)findViewById(R.id.regDepartment);
        email=(EditText)findViewById(R.id.regEmail);
        password=(EditText)findViewById(R.id.regPassword);

        progressBar=(ProgressBar)findViewById(R.id.progressbar);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.createAccount:
                userRegistration();
                break;

            case R.id.clickHere:
                Intent b=new Intent(RegistrationActivity.this,LoginActivity.class);
                b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(b);
                break;
        }
    }

    private void userRegistration() {

        final String full_name=fullName.getText().toString().trim();
        final String user_name=userName.getText().toString().trim();
        final String user_dept=department.getText().toString().trim();
        final String user_session=session.getSelectedItem().toString().trim();
        final String user_email=email.getText().toString().trim();
        final String user_password=password.getText().toString().trim();

        if(full_name.length()==0)
        {
            fullName.setError("Enter your full name");
            fullName.requestFocus();
            return;
        }

        if(user_name.length()==0)
        {
            userName.setError("Enter your user name");
            userName.requestFocus();
            return;
        }

        if(user_dept.length()==0)
        {
            department.setError("Enter your department");
            department.requestFocus();
            return;
        }

        if(user_session.length()>8)
        {
            Toast.makeText(RegistrationActivity.this,"Select your session",Toast.LENGTH_SHORT).show();
            fullName.requestFocus();
            return;
        }

        if(user_email.length()==0)
        {
            email.setError("Enter your email");
            email.requestFocus();
            return;
        }

        if(user_password.length()==0)
        {
            password.setError("Enter your password");
            password.requestFocus();
            return;
        }

        if(user_password.length()<6)
        {
            password.setError("Password length must be more than 6");
            password.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(user_email).matches())
        {
            email.setError("Email is not valid");
            email.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        create.setVisibility(View.INVISIBLE);
        firebaseDatabase=FirebaseDatabase.getInstance();

        databaseReference=firebaseDatabase.getReference().child("Users");
        firebaseAuth=FirebaseAuth.getInstance();

        Query userQuery=databaseReference.orderByChild("UserName").equalTo(user_name);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0)
                {
                    progressBar.setVisibility(View.GONE);
                    create.setVisibility(View.VISIBLE);
                    Toast.makeText(RegistrationActivity.this,"User name already registered",Toast.LENGTH_LONG).show();
                }
                else {
                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {

                            progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful())
                        {
                            finish();
                            Toast.makeText(RegistrationActivity.this,"Registration is successful",Toast.LENGTH_LONG).show();

                            String userId = firebaseAuth.getCurrentUser().getUid();
                            final DatabaseReference currentUserInfo = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                            final Map userInfo=new HashMap();

                            userInfo.put("FullName",full_name);
                            userInfo.put("UserName",user_name);
                            userInfo.put("Session",user_session);
                            userInfo.put("Department",user_dept);
                            userInfo.put("Email",user_email);

                            currentUserInfo.setValue(userInfo);


                            Intent a=new Intent(RegistrationActivity.this,UserProfile.class);
                            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(a);

                        }
                        else {

                            //Toast.makeText(getApplicationContext(), "Registration is not Successful", Toast.LENGTH_SHORT).show();
                            create.setVisibility(View.VISIBLE);

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "You have already registered with this email", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Registration is not Successful", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }


                });

            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });










    }
}
