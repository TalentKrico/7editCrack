package com.hl7soft.sevenedit.util.licapi.util;

public class HexUtil {
    public static String toHex(String str) {
        if (str == null)
            return null;
        byte[] bytes = str.getBytes();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String s = Integer.toHexString(bytes[i] & 0xFF).toUpperCase();
            if (s.length() == 1)
                sb.append('0');
            sb.append(s);
        }
        return sb.toString();
    }
}
