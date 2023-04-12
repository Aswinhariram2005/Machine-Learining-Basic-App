package com.aswinhariram2005.ml;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.aswinhariram2005.ml.databinding.ActivitySpamdetectionBinding;

import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier;

import java.io.IOException;
import java.util.List;

public class spamdetection extends AppCompatActivity {

    private ActivitySpamdetectionBinding binding;
    private String tfmodel = "model_spam.tflite",comment;
    private NLClassifier classifier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpamdetectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       binding.postBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               comment = binding.commentEdt.getText().toString();
               if (!comment.isEmpty()){
                   classify();
               }
           }
       });
        setup();



    }

    private void classify() {
        List<Category> categoryList = classifier.classify(comment);
        float score = categoryList.get(1).getScore();
        if (score>0.8f){
            binding.outputTxt.setText("Spam detected \n Spam Score : "+score);
        }
        else {
            binding.outputTxt.setText("Spam not detected. \n Spam score : "+score);
        }
    }

    private void setup() {
        try {
            classifier = NLClassifier.createFromFile(getApplicationContext(),tfmodel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        classifier.close();
    }
}