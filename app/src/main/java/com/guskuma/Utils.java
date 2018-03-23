package com.guskuma;

import android.content.Context;

/**
 * Created by Julio Guskuma on 11/02/2017.
 */

public class Utils {

    public static int toPxValue(Context context, int someDpValue){
        float density = context.getResources().getDisplayMetrics().density;
        return new Float(someDpValue * density).intValue();
    }

    public static int toDpValue(Context context, int somePxValue){
        float density = context.getResources().getDisplayMetrics().density;
        return new Float(somePxValue / density).intValue();
    }

}
