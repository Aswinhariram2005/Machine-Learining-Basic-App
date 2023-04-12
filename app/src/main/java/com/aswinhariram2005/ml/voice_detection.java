package com.aswinhariram2005.ml;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aswinhariram2005.ml.databinding.ActivityVoiceDetectionBinding;

import org.tensorflow.lite.support.audio.TensorAudio;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.audio.classifier.AudioClassifier;
import org.tensorflow.lite.task.audio.classifier.Classifications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class voice_detection extends AppCompatActivity {

    private ActivityVoiceDetectionBinding binding;
    private String tfmodel = "audio.tflite";
    private AudioRecord audioRecord;
    private TimerTask timerTask;
    private AudioClassifier audioClassifier;
    private TensorAudio tensorAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoiceDetectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        check_permission();
        btn_click();
        setup_audio_detect();


    }

    private void setup_audio_detect() {
        try {
            audioClassifier = AudioClassifier.createFromFile(this, tfmodel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        tensorAudio = audioClassifier.createInputTensorAudio();
    }


    private void onstopbtn() {
        timerTask.cancel();
        audioRecord.stop();
    }

    private void onstartbtn() {
        TensorAudio.TensorAudioFormat format = audioClassifier.getRequiredTensorAudioFormat();
        String input = "Number of channels : " + format.getChannels() + "\n" + "Sample Rate : " + format.getSampleRate();
        binding.inputTxt.setText(input);

        audioRecord = audioClassifier.createAudioRecord();
        audioRecord.startRecording();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                List<Classifications> classificationsList = audioClassifier.classify(tensorAudio);
                List<Category> outputList = new ArrayList<>();
                for (Classifications classifications : classificationsList) {
                    for (Category category : classifications.getCategories()) {
                        if (category.getScore() > 0.1f) {
                            outputList.add(category);
                        }
                    }
                }
                StringBuilder builder = new StringBuilder();
                for (Category category : outputList) {
                    builder.append(category.getLabel() + " : ").append(category.getScore()).append("\n");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (builder.toString().isEmpty()) {
                            binding.outputTxt.setText("Could not classify");
                        }
                        binding.outputTxt.setText(builder.toString());
                    }
                });
            }
        };
        new Timer().scheduleAtFixedRate(timerTask, 1, 500);
    }

    private void btn_click() {
        binding.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.stopBtn.setEnabled(true);
                binding.startBtn.setEnabled(false);
                onstartbtn();
            }
        });
        binding.stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.stopBtn.setEnabled(false);
                binding.startBtn.setEnabled(true);
                onstopbtn();
            }
        });
    }


    private void check_permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission needed", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(voice_detection.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        } else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO);

        } else {
            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show();
        } else {
            onBackPressed();
        }

    }
}

