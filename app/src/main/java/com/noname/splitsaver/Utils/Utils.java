package com.noname.splitsaver.Utils;

import android.content.Context;

import com.noname.splitsaver.R;

import java.math.BigDecimal;

public class Utils {
    public static String displayPrice(Context context, float value) {
        return context.getString(R.string.format_price, BigDecimal.valueOf(value).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
    }
}