package com.lyloou.flow.util;

public class ImageHelper {
    public static String getImage(int image) {
        return "https://meiriyiwen.com/images/new_feed/bg_" + image + ".jpg";
    }

    public static String getBigImage(String day) {
        int image = Integer.parseInt(day) % 98;
        return getImage(image);
    }

    public static String getTodayBigImage() {
        return getOneArticleImg();
    }

    private static String getOneArticleImg() {
        String day = Utime.getDayWithFormatTwo();
        int image = Integer.parseInt(day) % 98;
        return getImage(image);
    }

}
