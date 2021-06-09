package com.lyloou.flow.util;

import java.util.Locale;

public class Ustr {
    public static void main(String[] args) {
        System.out.println(toPercentStr(0.283));
        System.out.println(toPercentStr(0.2));
        System.out.println(toPercentStr(1.2));
    }

    public static String toPercentStr(Double data) {
        return String.format(Locale.CHINA, "%.2f%%", data * 100);
    }
    public static String to2Decimal(Double data) {
        return String.format(Locale.CHINA, "%.2f", data );
    }
}
