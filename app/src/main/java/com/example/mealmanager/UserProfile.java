package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    private TextView mealcost,full_name, user_name, user_dept,user_session,user_email, mealList;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference, currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mealcost=(TextView)findViewById(R.id.mealCost);
        mealList=(TextView)findViewById(R.id.mealCostList);
        user_name=(TextView)findViewById(R.id.userName);
        full_name=(TextView)findViewById(R.id.userFullName);
        user_email=(TextView)findViewById(R.id.userEmail);
        user_dept=(TextView)findViewById(R.id.userDepartment);
        user_session=(TextView)findViewById(R.id.userSession);

        mealcost.setOnClickListener(this);
        mealList.setOnClickListener(this);

        showinfo();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.mealCost:
                Intent intent=new Intent(UserProfile.this,MealCost.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.mealCostList:
                Intent intent1=new Intent(UserProfile.this,ShowMealCost.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                break;
        }

    }

    private void showinfo() {
        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Users");
        firebaseUser=mAuth.getCurrentUser();

        String userId=firebaseUser.getUid();

        currentUser=databaseReference.child(userId);

        currentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String fullName=dataSnapshot.child("FullName").getValue().toString();
                String userName=dataSnapshot.child("UserName").getValue().toString();
                String department=dataSnapshot.child("Department").getValue().toString();
                String session=dataSnapshot.child("Session").getValue().toString();
                String email=dataSnapshot.child("Email").getValue().toString();

                full_name.setText(fullName);
                user_name.setText(userName);
                user_dept.setText(department);
                user_email.setText(email);
                user_session.setText(session);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Warning!!!");
        builder.setIcon(R.drawable.ic_skull);
        builder.setCancelable(false);


        builder.setMessage("Are you sure you want to Logout and Exit")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(UserProfile.this, ChoosingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
