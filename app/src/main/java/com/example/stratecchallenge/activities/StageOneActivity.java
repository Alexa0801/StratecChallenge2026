package com.example.stratecchallenge.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stratecchallenge.R;
import com.example.stratecchallenge.controller.PhysicsLogic;
import com.example.stratecchallenge.controller.ReadingLogic;
import com.example.stratecchallenge.domain.Planet;

import java.util.List;

public class StageOneActivity extends AppCompatActivity {

    private TextView escapeVelocities;
    private Button compute;
    private Button back;
    private PhysicsLogic controller;
    private ReadingLogic reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stage_one);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        controller = new PhysicsLogic();
        reader = new ReadingLogic();

        escapeVelocities = findViewById(R.id.escapeVelocities);
        compute = findViewById(R.id.compute);
        back = findViewById(R.id.back);

        back.setOnClickListener(v -> {
                Intent intent = new Intent(StageOneActivity.this, MainActivity.class);
                startActivity(intent);
                });

        compute.setOnClickListener(v -> handleCalculateEscapeVelocities());
    }

    private void handleCalculateEscapeVelocities() {
        List<Planet> planets = reader.readPlanets(this);

        StringBuilder result = controller.calculateEscapeVeolocities(planets);

        escapeVelocities.setText(result.toString());

    }

}