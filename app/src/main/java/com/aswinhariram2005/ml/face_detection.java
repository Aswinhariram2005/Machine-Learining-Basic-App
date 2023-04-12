package com.aswinhariram2005.ml;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aswinhariram2005.ml.databinding.ActivityFaceDetectionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.ArrayList;
import java.util.List;

public class face_detection extends AppCompatActivity {


    private FaceDetector detector;
    private ActivityFaceDetectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFaceDetectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setup();
        classify(bitmap());


    }

    private void classify(Bitmap bitmap) {
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        detector.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                    @Override
                    public void onSuccess(List<Face> faces) {
                        List<Box> boxList = new ArrayList<>();
                        if (!faces.isEmpty()) {
                            for (Face face : faces) {
                                Box box = new Box(face.getBoundingBox(), face.getTrackingId() + "");
                                boxList.add(box);
                            }
                            Draw_Box drawBox = new Draw_Box();
                            drawBox.drawBox(boxList, bitmap, new Draw_Box.Draw_interface() {
                                @Override
                                public void onfinish(Bitmap out_bitmap) {
                                    binding.img.setImageBitmap(out_bitmap);
                                    binding.txt1.setText("Face detected");
                                }
                            });
                        } else {
                            binding.txt1.setText("No face was detected");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(face_detection.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private Bitmap bitmap() {
        BitmapDrawable drawable = (BitmapDrawable) binding.img.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        return bitmap;
    }

    private void setup() {
        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .enableTracking()
                .build();
        detector = FaceDetection.getClient(options);
    }
}