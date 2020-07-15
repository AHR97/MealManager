package com.example.mealmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShowMealCost extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private TextView select;

    private ListView listView;

    private Spinner month,year;

    private  String[] monthlist,yearlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_meal_cost);

        listView=(ListView)findViewById(R.id.mealCostListView);
        listView.setVisibility(View.GONE);

        select=(TextView)findViewById(R.id.selectSpinner);
        select.setVisibility(View.GONE);

        month=(Spinner)findViewById(R.id.mealMonth);
        year=(Spinner)findViewById(R.id.mealYear);

        monthlist=getResources().getStringArray(R.array.Month);
        yearlist=getResources().getStringArray(R.array.Year);


        final ArrayAdapter<String>monthAdapter=new ArrayAdapter<>(this,R.layout.spinner_layout_design,R.id.sampleSpinner,monthlist);
        final ArrayAdapter<String>yearAdapter=new ArrayAdapter<>(this,R.layout.spinner_layout_design,R.id.sampleSpinner,yearlist);
        year.setAdapter(yearAdapter);






        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0)
                {
                    final String year=yearlist[position];
                    month.setAdapter(monthAdapter);
                    month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                            if(position!=0)
                            {
                                listView.setVisibility(View.VISIBLE);
                                showList(year,monthlist[position]);


                            }
                            else{
                                listView.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
                else{
                    listView.setVisibility(View.GONE);
                    select.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    void showList(final String year, final String month)
    {
        final ArrayList<MealDataListItem> mealList=new ArrayList<>();

        databaseReference= FirebaseDatabase.getInstance().getReference().child(year);

        final MealDataAdapter adapter=new MealDataAdapter(this,R.layout.meal_cost_list_item,mealList);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot contactSnapshot=dataSnapshot.child(month);
                Iterable<DataSnapshot> contactChildren=contactSnapshot.getChildren();

                for(DataSnapshot contact:contactChildren)
                {
                    MealDataListItem item=contact.getValue(MealDataListItem.class);

                    mealList.add(item);
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
