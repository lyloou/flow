package com.lyloou.flow.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class PasswordUtil {
    public static String getEncodedPassword(String originPassword) {
        return String.valueOf(Hex.encodeHex(DigestUtils.sha1(originPassword)));
    }
}