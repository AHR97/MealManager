package com.example.mealmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChoosingActivity extends AppCompatActivity  implements View.OnClickListener {

    private TextView name, theme;

    private Typeface typeface1, typeface2;

    private Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing);

        name=(TextView)findViewById(R.id.appName);
        theme=(TextView)findViewById(R.id.appTheme);

        login=(Button)findViewById(R.id.login);
        register=(Button)findViewById(R.id.join);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

        typeface1=Typeface.createFromAsset(getAssets(),"font/KaushanScript_Regular.otf");
        typeface2=Typeface.createFromAsset(getAssets(),"font/DancingScript_Regular.otf");

        name.setTypeface(typeface1);
        theme.setTypeface(typeface2);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.login:
                Intent a=new Intent(ChoosingActivity.this,LoginActivity.class);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
                break;

            case R.id.join:
                Intent b=new Intent(ChoosingActivity.this,RegistrationActivity.class);
                b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(b);
                break;
        }

    }
}
