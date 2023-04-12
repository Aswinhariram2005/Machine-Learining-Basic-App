package com.aswinhariram2005.ml;

import android.graphics.Rect;

public class Box {
    public Rect rect;
    public String label;

    public Box(Rect rect, String label) {
        this.rect = rect;
        this.label = label;
    }
}
