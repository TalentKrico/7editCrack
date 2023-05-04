package com.hl7soft.sevenedit.util.licapi.util;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Random;

public class DataHelper {
    private static final char[] SYMBOL_TABLE = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F' };

    public static int charToCode(char c) {
        for (int i = 0; i < SYMBOL_TABLE.length; i++) {
            if (SYMBOL_TABLE[i] == c)
                return i;
        }
        return -1;
    }

    public static String addDashes(String key, int len) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            sb.append(c);
            if (i != 0 && i != key.length() - 1 && (i + 1) % len == 0)
                sb.append("-");
        }
        return sb.toString();
    }

    public static String removeDashes(String key) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (c != '-')
                sb.append(c);
        }
        return sb.toString();
    }

    public static String binaryKeyToAscii(byte[] data) {
        try {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < data.length; i++) {
                byte b = data[i];
                int high = (b & 0xF0) >> 4;
                int low = b & 0xF;
                char highc = SYMBOL_TABLE[high];
                char lowc = SYMBOL_TABLE[low];
                sb.append(highc);
                sb.append(lowc);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Can't convert key to ASCII!", e);
        }
    }

    public static byte[] asciiKeyToBinary(String str) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BitStreamWriter writer = new BitStreamWriter(os);
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                int cd = charToCode(c);
                writer.write(4, cd);
            }
            writer.close();
            byte[] buf = os.toByteArray();
            byte[] tmp = new byte[buf.length];
            System.arraycopy(buf, 0, tmp, 0, tmp.length);
            buf = tmp;
            return buf;
        } catch (Exception e) {
            throw new RuntimeException("can't convert key to binary", e);
        }
    }

    public static int encodeDate(Calendar date) {
        if (date == null)
            return 65535;
        int day = date.get(5);
        int month = date.get(2) + 1;
        int year = date.get(1);
        year -= 2000;
        int code = 0;
        code |= day << 11;
        code |= month << 7;
        code |= year;
        return code;
    }

    public static Calendar decodeDate(int code) {
        if (code == 65535)
            return null;
        int day = code >> 11 & 0x1F;
        int month = code >> 7 & 0xF;
        int year = 2000 + (code & 0x7F);
        Calendar date = Calendar.getInstance();
        date.set(1, year);
        date.set(2, month - 1);
        date.set(5, day);
        date.set(10, 0);
        date.set(11, 0);
        date.set(12, 0);
        date.set(13, 0);
        date.set(14, 0);
        return date;
    }

    public static String generateRandomSalt(Random rnd) {
        if (rnd == null)
            rnd = new Random(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 2; i++) {
            int cd = rnd.nextInt(32);
            sb.append(SYMBOL_TABLE[cd]);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            Random rnd = new Random(System.currentTimeMillis());
            byte[] buf = new byte[12];
            rnd.nextBytes(buf);
            String key = binaryKeyToAscii(buf);
            byte[] buf2 = asciiKeyToBinary(key);
            if (buf.length != buf2.length)
                throw new RuntimeException("LEN!");
            for (int i = 0; i < buf2.length; i++) {
                if (buf[i] != buf2[i])
                    throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Calendar generateRandomDate(Random rnd) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(rnd.nextLong());
        int year = date.get(1);
        if (year < 2000 || year > 2100) {
            int ny = 2000 + rnd.nextInt(100);
            if (year % 4 == 0)
                ny = 4 * ny / 4;
            date.set(1, ny);
        }
        int day = date.get(5);
        int month = date.get(2) + 1;
        year = date.get(1);
        if (month == 2 && day == 29)
            return generateRandomDate(rnd);
        date.set(10, 0);
        date.set(11, 0);
        date.set(12, 0);
        date.set(13, 0);
        date.set(14, 0);
        return date;
    }
}
