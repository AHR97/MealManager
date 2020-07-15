package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MealCost extends AppCompatActivity  implements View.OnClickListener {

    private Spinner month,date,year;

    private String[] monthlist, datelist31, datelist30, datelist28,datelist29,yearlist;

    private Button cost;

    private EditText mealCost;

    private ProgressBar progressBar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference curUserRef, mealCostRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_cost);

        month = (Spinner) findViewById(R.id.month);
        date = (Spinner) findViewById(R.id.date);
        year = (Spinner) findViewById(R.id.year);

        cost =(Button)findViewById(R.id.addCost);
        cost.setOnClickListener(this);

        mealCost=(EditText)findViewById(R.id.mealCost);

        progressBar=(ProgressBar) findViewById(R.id.progressbar);

        monthlist = getResources().getStringArray(R.array.Month);
        datelist31 = getResources().getStringArray(R.array.Date31);
        datelist30 = getResources().getStringArray(R.array.Date30);
        datelist28 = getResources().getStringArray(R.array.Date28);
        datelist29 = getResources().getStringArray(R.array.Date29);
        yearlist = getResources().getStringArray(R.array.Year);

        firebaseDatabase=FirebaseDatabase.getInstance();


        final ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout_design, R.id.sampleSpinner, monthlist);
        final ArrayAdapter<String> dateAdapter31 = new ArrayAdapter<>(this, R.layout.spinner_layout_design, R.id.sampleSpinner, datelist31);
        final ArrayAdapter<String> dateAdapter30 = new ArrayAdapter<>(this, R.layout.spinner_layout_design, R.id.sampleSpinner, datelist30);
        final ArrayAdapter<String> dateAdapter28 = new ArrayAdapter<>(this, R.layout.spinner_layout_design, R.id.sampleSpinner, datelist28);
        final ArrayAdapter<String> dateAdapter29 = new ArrayAdapter<>(this, R.layout.spinner_layout_design, R.id.sampleSpinner, datelist29);
        final ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout_design, R.id.sampleSpinner, yearlist);
        year.setAdapter(yearAdapter);


        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position!=0)
                {
                    final String X=yearlist[position];
                    month.setAdapter(monthAdapter);
                    month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if(position==1 || position==3 || position==5 ||position==7 || position==8 || position==11 || position==13)
                            {
                                date.setAdapter(dateAdapter31);

                            }
                            else if(position==4 || position==6 || position==9 ||position==10 || position==12)
                            {
                                date.setAdapter(dateAdapter30);

                            }
                            else if(position==2)
                            {
                                if(leapYear(X))
                                {
                                    date.setAdapter(dateAdapter29);
                                }
                                else{
                                    date.setAdapter(dateAdapter28);
                                }

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.addCost:
                addMealCost();
                break;
        }



    }

    private  boolean leapYear(String year)
    {
        int val=Integer.parseInt(year);

        if(val%400==0 || val%4==0 && val%100!=0)
            return true;
        else
            return false;
    }

    private void addMealCost() {

        final String meal_year=year.getSelectedItem().toString().trim();
        final String meal_month=month.getSelectedItem().toString().trim();
        final String meal_date=date.getSelectedItem().toString().trim();
        final String meal_money=mealCost.getText().toString().trim();

        if(meal_year.length()>5)
        {
            year.requestFocus();
            Toast.makeText(MealCost.this,"Select a year",Toast.LENGTH_LONG).show();
            return;
        }

        else if(meal_month.length()>9)
        {
            month.requestFocus();
            Toast.makeText(MealCost.this,"Select a month",Toast.LENGTH_LONG).show();
            return;
        }
        else if(meal_date.length()>3)
        {
            date.requestFocus();
            Toast.makeText(MealCost.this,"Select a date",Toast.LENGTH_LONG).show();
            return;
        }
        else if(meal_money.length()==0)
        {
            mealCost.requestFocus();
            mealCost.setError("Enter the meal cost");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        cost.setVisibility(View.INVISIBLE);

        final String date=meal_date+ " " + meal_month+",2020";

        final String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        String id=firebaseDatabase.getReference().child(meal_year).child(meal_month).push().getKey();

        curUserRef=firebaseDatabase.getReference("Users").child(uid);
        mealCostRef=firebaseDatabase.getReference().child(meal_year).child(meal_month).child(id);

        Query mealQuery=firebaseDatabase.getReference().child(meal_year).child(meal_month).orderByChild("Date").equalTo(date);
        mealQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0)
                {
                    progressBar.setVisibility(View.GONE);
                    cost.setVisibility(View.VISIBLE);
                    Toast.makeText(MealCost.this,"Meal cost of this date is already added",Toast.LENGTH_LONG).show();
                }else{
                    curUserRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userName=dataSnapshot.child("UserName").getValue().toString();

                            Map curValue= new HashMap();
                            curValue.put("Date",date);
                            curValue.put("UserName",userName);
                            curValue.put("Cost",meal_money);

                            mealCostRef.setValue(curValue);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    Intent intent=new Intent(MealCost.this,UserProfile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
