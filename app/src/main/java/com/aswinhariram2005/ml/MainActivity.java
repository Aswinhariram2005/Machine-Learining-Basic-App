package com.aswinhariram2005.ml;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.aswinhariram2005.ml.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ImageLabeler labeler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BitmapDrawable drawable = (BitmapDrawable) binding.img.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        setupLabeler();
        classify(bitmap);


    }

    private void classify(Bitmap bitmap) {
        InputImage inputImage = InputImage.fromBitmap(bitmap,0);
        labeler.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
            @Override
            public void onSuccess(List<ImageLabel> imageLabels) {
                if (imageLabels.size()>0){
                    StringBuilder builder = new StringBuilder();
                    for (ImageLabel res :imageLabels ) {

                        builder.append(res.getText())
                                .append(":")
                                .append(res.getConfidence())
                                .append("\n");
                    }
                    binding.txt1.setText(builder);
                }
                else{
                    Toast.makeText(MainActivity.this, "Could not satisfy", Toast.LENGTH_SHORT).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Classification Error", "onFailure: "+e.getLocalizedMessage());
                    }
                });
    }

    private void setupLabeler() {
        labeler = ImageLabeling.getClient(new ImageLabelerOptions.Builder().setConfidenceThreshold(0.3f).build());

    }
}