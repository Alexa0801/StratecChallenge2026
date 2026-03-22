package com.example.stratecchallenge.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.stratecchallenge.R;
import com.example.stratecchallenge.controller.PhysicsLogic;
import com.example.stratecchallenge.controller.ReadingLogic;

public class StageFourActivity extends AppCompatActivity {

    private TextView output;
    private Button calculate;
    private Button back;
    private EditText input;

    private PhysicsLogic controller;
    private ReadingLogic reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stage_four);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        controller = new PhysicsLogic();
        reader = new ReadingLogic();

        output = findViewById(R.id.output);
        calculate = findViewById(R.id.calculate);
        back = findViewById(R.id.back);
        input = findViewById(R.id.input);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(StageFourActivity.this, MainActivity.class);
            startActivity(intent);
        });

        calculate.setOnClickListener(v -> handleCalculatePosition());
    }

    private void handleCalculatePosition() {
        String daysStr = input.getText().toString();
        if (daysStr.isEmpty()) {
            output.setText("Please enter a number of days.");
            return;
        }

        double days = Double.parseDouble(daysStr);
        StringBuilder result = controller.calculatePositions(reader.readSolarPlanets(this), days);
        output.setText(result);
    }
}