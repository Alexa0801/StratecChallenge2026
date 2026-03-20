package com.example.stratecchallenge.activities;

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
import com.example.stratecchallenge.domain.Rocket;

import java.util.List;

public class StageTwoActivity extends AppCompatActivity {

    private TextView timeToReach;
    private Button compute;
    private Button back;

    private PhysicsLogic controller;
    private ReadingLogic reader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stage_two);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        controller = new PhysicsLogic();
        reader = new ReadingLogic();

        timeToReach = findViewById(R.id.timeToReach);
        compute = findViewById(R.id.compute);
        back = findViewById(R.id.back);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(StageTwoActivity.this, MainActivity.class);
            startActivity(intent);
        });

        compute.setOnClickListener(v -> handleCalculateTimeToReach());

    }

    private void handleCalculateTimeToReach() {
        List<Planet> planets = reader.readPlanets(this);
        Rocket rocket = reader.readRocketData(this);
        StringBuilder result = controller.calculateTimeToReachSpace(this, planets, rocket);
        timeToReach.setText(result);
    }
}