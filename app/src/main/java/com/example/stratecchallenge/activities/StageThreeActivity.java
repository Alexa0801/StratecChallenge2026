package com.example.stratecchallenge.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stratecchallenge.R;
import com.example.stratecchallenge.controller.PhysicsLogic;
import com.example.stratecchallenge.controller.ReadingLogic;
import com.example.stratecchallenge.domain.Rocket;
import com.example.stratecchallenge.domain.SolarPlanet;

import java.util.ArrayList;
import java.util.List;

public class StageThreeActivity extends AppCompatActivity {

    private Spinner spinnerStart, spinnerDest;
    private Button calculate, back;
    private TextView output;

    private PhysicsLogic controller;
    private ReadingLogic reader;

    private List<SolarPlanet> planets;
    private Rocket rocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_three);

        spinnerStart = findViewById(R.id.spinnerStart);
        spinnerDest = findViewById(R.id.spinnerDest);
        calculate = findViewById(R.id.calculate);
        back = findViewById(R.id.back);
        output = findViewById(R.id.output);

        controller = new PhysicsLogic();
        reader = new ReadingLogic();

        planets = reader.readSolarPlanets(this);
        rocket = reader.readRocket(this);

        List<String> planetNames = new ArrayList<>();
        for (SolarPlanet p : planets) {
            planetNames.add(p.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, planetNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStart.setAdapter(adapter);
        spinnerDest.setAdapter(adapter);


        calculate.setOnClickListener(v -> handleJourney());
        back.setOnClickListener(v -> {
            Intent intent = new Intent(StageThreeActivity.this, MainActivity.class);
            startActivity(intent);
        });


    }

    private void handleJourney() {
        String startName = spinnerStart.getSelectedItem().toString();
        String destName = spinnerDest.getSelectedItem().toString();

        if (startName.equals(destName)) {
            output.setText("Start and destination planets cannot be the same!");
            return;
        }

        SolarPlanet startPlanet = null, destPlanet = null;
        for (SolarPlanet p : planets) {
            if (p.getName().equalsIgnoreCase(startName)) startPlanet = p;
            if (p.getName().equalsIgnoreCase(destName)) destPlanet = p;
        }

        if (startPlanet != null && destPlanet != null) {
            StringBuilder result = controller.calculateJourney(startPlanet, destPlanet, rocket);
            output.setText(result);
        }
    }

}
