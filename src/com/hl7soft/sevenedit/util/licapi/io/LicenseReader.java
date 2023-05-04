package com.hl7soft.sevenedit.util.licapi.io;

import com.hl7soft.sevenedit.util.licapi.License;
import com.hl7soft.sevenedit.util.licapi.util.BitStreamReader;
import com.hl7soft.sevenedit.util.licapi.util.Crc8Util;
import com.hl7soft.sevenedit.util.licapi.util.DataHelper;
import com.hl7soft.sevenedit.util.licapi.util.XOREncryption;
import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.Random;

public class LicenseReader {
    public License readFromKey(String key, String password) {
        if (key == null)
            return null;
        try {
            key = DataHelper.removeDashes(key);
            if (key.length() != 24)
                throw new RuntimeException("Wrong key length!");
            byte[] buf = DataHelper.asciiKeyToBinary(key);
            if (buf.length != 12)
                throw new RuntimeException("Wrong key length!");
            XOREncryption encryption = new XOREncryption(password);
            encryption.decrypt(buf);
            byte crcExpected = buf[0];
            byte[] tmpBuf = new byte[buf.length - 1];
            System.arraycopy(buf, 1, tmpBuf, 0, tmpBuf.length);
            buf = tmpBuf;
            byte crc = Crc8Util.calculate(buf);
            if (crc != crcExpected)
                throw new RuntimeException("CRC check failed!");
            BitStreamReader reader = new BitStreamReader(new ByteArrayInputStream(buf));
            int version = reader.read(8);
            if (version != 1)
                throw new RuntimeException("Unknown key version: " + version);
            int serial = reader.read(8) << 16 | reader.read(16);
            int productId = reader.read(8) << 16 | reader.read(16);
            int expDateInt = reader.read(16);
            Calendar expDate = DataHelper.decodeDate(expDateInt);
            License license = new License(serial, productId, expDate);
            return license;
        } catch (Exception e) {
            throw new RuntimeException("Can't read license from key!", e);
        }
    }

    public static void main(String[] args) {
        try {
            String password = "Demo";
            Random rand = new Random(System.currentTimeMillis());
            for (int i = 0; i < 10000000; i++) {
                int serial = rand.nextInt(16777216);
                int product = rand.nextInt(16777216);
                Calendar expDate = DataHelper.generateRandomDate(rand);
                License license = new License(serial, product, expDate);
                System.out.println("Original license\n" + license);
                license.setExpirationDate(expDate);
                LicenseWriter writer = new LicenseWriter();
                String key = writer.writeToKey(license, password);
                LicenseReader reader = new LicenseReader();
                License license2 = reader.readFromKey(key, password);
                if (license.getSerialNumber() != license2.getSerialNumber())
                    throw new RuntimeException("Bad serial!\n" + license);
                if (license.getProductId() != license2.getProductId())
                    throw new RuntimeException("Bad product ID!\n" + license);
                if (license.getExpirationDate() == null) {
                    if (license2.getExpirationDate() != null)
                        throw new RuntimeException("Bad expiration date!\n" + license);
                } else {
                    if (license.getExpirationDate().get(1) != license2.getExpirationDate().get(1))
                        throw new RuntimeException("Bad expiration date!\n" + license);
                    if (license.getExpirationDate().get(2) != license2.getExpirationDate().get(2))
                        throw new RuntimeException("Bad expiration date!\n" + license);
                    if (license.getExpirationDate().get(5) != license2.getExpirationDate().get(5))
                        throw new RuntimeException("Bad expiration date!\n" + license);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
