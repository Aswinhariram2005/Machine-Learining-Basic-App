package com.aswinhariram2005.ml;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.aswinhariram2005.ml.databinding.ActivityNavBinding;

public class nav extends AppCompatActivity {

    ActivityNavBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        btn_click();
    }

    private void btn_click() {

        binding.imgDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_dect();
            }
        });
        binding.objDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj();
            }
        });
        binding.flowerDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tensor();
            }
        });

        binding.face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                face();
            }
        });
        binding.audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio();

            }
        });
        binding.spamDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spam();
            }
        });
    }

    private void audio() {
        startActivity(new Intent(getApplicationContext(), voice_detection.class));
    }
    private void spam() {
        startActivity(new Intent(getApplicationContext(), spamdetection.class));
    }

    private void face() {
        startActivity(new Intent(getApplicationContext(), face_detection.class));
    }

    private void tensor() {
        startActivity(new Intent(getApplicationContext(), tensor.class));
    }

    private void img_dect() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private void obj() {
        startActivity(new Intent(getApplicationContext(), object_dect.class));
    }


}