package com.aswinhariram2005.ml;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aswinhariram2005.ml.databinding.ActivityObjectDectBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;

import java.util.ArrayList;
import java.util.List;

public class object_dect extends AppCompatActivity {

    private ActivityObjectDectBinding binding;
    private ObjectDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityObjectDectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BitmapDrawable drawable = (BitmapDrawable) binding.img.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        setup_obj_dec();
        classify(bitmap);

    }

    private void classify(Bitmap bitmap) {
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        detector.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<DetectedObject>>() {
            @Override
            public void onSuccess(List<DetectedObject> detectedObjects) {
                if (!detectedObjects.isEmpty()) {
                    StringBuilder builder = new StringBuilder();
                    List<Box> boxes = new ArrayList<>();
                    for (DetectedObject object : detectedObjects) {
                        if (!object.getLabels().isEmpty()) {
                            String label = object.getLabels().get(0).getText();
                            builder.append(label + " : ").append(object.getLabels().get(0).getConfidence()).append("\n");
                            boxes.add(new Box(object.getBoundingBox(),label));

                        }
                        else{
                            builder.append("Unknown").append("\n");
                        }


                    }
                    binding.txt1.setText(builder.toString());

                    Draw_Box drawBox = new Draw_Box();
                    drawBox.drawBox(boxes, bitmap, new Draw_Box.Draw_interface() {
                        @Override
                        public void onfinish(Bitmap out_bitmap) {
                            binding.img.setImageBitmap(out_bitmap);
                        }
                    });
                } else {
                    Toast.makeText(object_dect.this, "Unable to classify", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(object_dect.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setup_obj_dec() {
        ObjectDetectorOptions options = new ObjectDetectorOptions.Builder()
                .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .build();
        detector = ObjectDetection.getClient(options);
    }

}