package com.eventdee.stepswithbenefits;

import android.content.res.Resources;

public final class Utils {
    
    private Utils() {
    }
    
    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static int steps2amt(String count){
        double steps = Integer.parseInt(count);
        int amount = (int) (steps*0.0041705);
        return amount;
    }
}
