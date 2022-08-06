package com.ox5un5h1n3.zulo.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.ox5un5h1n3.zulo.MainActivity;
import com.ox5un5h1n3.zulo.R;

public class DashboardTabViewNewItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);


        Button button = findViewById(R.id.buttonRe);

        button.setOnClickListener(view -> {
            startActivity(new Intent(DashboardTabViewNewItem.this, MainActivity.class));
        });
    }
}

