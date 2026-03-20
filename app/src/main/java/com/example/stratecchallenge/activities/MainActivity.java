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
import com.example.stratecchallenge.domain.Planet;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Button stageOne;
    private Button stageTwo;
    private Button stageThree;
    private Button stageFour;
    private Button stageFive;
    private Button stageSix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        stageOne = findViewById(R.id.stage1Button);
        stageTwo = findViewById(R.id.stage2Button);
        stageThree = findViewById(R.id.stage3Button);
        stageFour = findViewById(R.id.stage4Button);
        stageFive = findViewById(R.id.stage5Button);
        stageSix = findViewById(R.id.stage6Button);

        stageOne.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StageOneActivity.class);
            startActivity(intent);
        });

        stageTwo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StageTwoActivity.class);
            startActivity(intent);
        });

        stageThree.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StageThreeActivity.class);
            startActivity(intent);
        });

        stageFour.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StageFourActivity.class);
            startActivity(intent);
        });

        stageFive.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StageFiveActivity.class);
            startActivity(intent);
        });

        stageSix.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StageSixActivity.class);
            startActivity(intent);
        });


    }




}