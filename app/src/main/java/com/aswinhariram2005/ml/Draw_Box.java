package com.aswinhariram2005.ml;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.List;

public class Draw_Box {
    public void drawBox(List<Box> boxes, Bitmap bitmap, Draw_interface draw_interface){
        Bitmap out_bitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(out_bitmap);
        Paint paint_rec = new Paint();
        Paint paint_label = new Paint();
        paint_rec.setColor(Color.RED);
        paint_rec.setStyle(Paint.Style.STROKE);
        paint_rec.setStrokeWidth(8f);


        paint_label.setColor(Color.YELLOW);
        paint_label.setStrokeWidth(8f);
        paint_label.setStyle(Paint.Style.STROKE);
        paint_label.setTextSize(90f);

        for (Box box :boxes ){
            canvas.drawRect(box.rect,paint_rec);

            Rect labelsize = new Rect(0,0,0,0);
            paint_label.getTextBounds(box.label,0,box.label.length(),labelsize);

            float fontsize = paint_label.getTextSize() * box.rect.width()/labelsize.width();
            if (fontsize<paint_label.getTextSize()){
                paint_label.setTextSize(fontsize);
            }
            canvas.drawText(box.label,box.rect.left,box.rect.top+labelsize.height(),paint_label);
        }
        draw_interface.onfinish(out_bitmap);


    }


    interface Draw_interface{
        void onfinish(Bitmap out_bitmap);

    }
}
