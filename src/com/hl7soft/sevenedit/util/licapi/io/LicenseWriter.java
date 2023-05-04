package com.hl7soft.sevenedit.util.licapi.io;

import com.hl7soft.sevenedit.util.licapi.License;
import com.hl7soft.sevenedit.util.licapi.util.BitStreamWriter;
import com.hl7soft.sevenedit.util.licapi.util.Crc8Util;
import com.hl7soft.sevenedit.util.licapi.util.DataHelper;
import com.hl7soft.sevenedit.util.licapi.util.XOREncryption;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Random;

public class LicenseWriter {
    public String writeToKey(License license, String password) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BitStreamWriter writer = new BitStreamWriter(os);
            if (license.getSerialNumber() < 0 || license
                    .getSerialNumber() >= 16777216)
                throw new RuntimeException("Serial number should be in 0..16777216 range!");
            if (license.getProductId() < 0 || license
                    .getProductId() >= 16777216)
                throw new RuntimeException("Product ID should be in 0..16777216 range!");
            if (license.getExpirationDate() != null) {
                int year = license.getExpirationDate().get(1);
//                if (year < 2000 || year > 2100)
//                    throw new RuntimeException("Expiration date year should be in range 2000..2100!");
            }
            int expDate = DataHelper.encodeDate(license.getExpirationDate());
            writer.write(8, 1);
            writer.write(8, license.getSerialNumber() >> 16 & 0xFF);
            writer.write(16, license.getSerialNumber() & 0xFFFF);
            writer.write(8, license.getProductId() >> 16 & 0xFF);
            writer.write(16, license.getProductId() & 0xFFFF);
            writer.write(16, expDate & 0xFFFF);
            writer.close();
            byte[] buf = os.toByteArray();
            Random rand = new Random(System.currentTimeMillis());
            byte[] tmpBuf = new byte[11];
            rand.nextBytes(tmpBuf);
            System.arraycopy(buf, 0, tmpBuf, 0, buf.length);
            buf = tmpBuf;
            byte crc = Crc8Util.calculate(buf);
            byte[] bufWithCrc = new byte[12];
            bufWithCrc[0] = crc;
            System.arraycopy(buf, 0, bufWithCrc, 1, buf.length);
            XOREncryption encryption = new XOREncryption(password);
            encryption.encrypt(bufWithCrc);
            String key = DataHelper.binaryKeyToAscii(bufWithCrc);
            if (key.length() != 24)
                throw new RuntimeException("Wrong key length!");
            key = DataHelper.addDashes(key, 6);
            return key;
        } catch (Exception e) {
            throw new RuntimeException("Can't convert license to String!", e);
        }
    }

    public static void main(String[] args) {
        try {
            int serialNumber = 1;
            int productId = 10101;
            Calendar expDate = Calendar.getInstance();
            expDate.add(Calendar.YEAR,99);
            String password = "com.7edit.2x";
            License license = new License(serialNumber, productId, expDate);
            LicenseWriter writer = new LicenseWriter();
            String key = writer.writeToKey(license, password);
            System.out.println("KEY: " + key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
