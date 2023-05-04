package com.hl7soft.sevenedit.util.licapi.util;

public class XOREncryption {
    private byte[] key = null;

    private int count = 0;

    private byte previous = 27;

    public XOREncryption(String privateKey) {
        int len = privateKey.length();
        this.key = privateKey.getBytes();
    }

    public XOREncryption() {
        this("bla##t5&*43,.:{=165#322Acv{}&&66!5%");
    }

    public void encrypt(byte[] data, int offset, int length) {
        reset();
        for (int i = offset, j = offset + length; i < j; i++) {
            byte b = data[i];
            data[i] = (byte)(data[i] ^ this.key[this.count++ % this.key.length] ^ this.previous);
            this.previous = b;
        }
    }

    public void encrypt(byte[] data) {
        encrypt(data, 0, data.length);
    }

    public String encrypt(String str) {
        byte[] data = str.getBytes();
        encrypt(data, 0, data.length);
        return new String(data);
    }

    public void decrypt(byte[] data, int offset, int length) {
        reset();
        for (int i = offset, j = offset + length; i < j; i++) {
            data[i] = (byte)(data[i] ^ this.previous ^ this.key[this.count++ % this.key.length]);
            this.previous = data[i];
        }
    }

    private void reset() {
        this.previous = 27;
        this.count = 0;
    }

    public void decrypt(byte[] data) {
        decrypt(data, 0, data.length);
    }

    public String decrypt(String str) {
        byte[] data = str.getBytes();
        decrypt(data, 0, data.length);
        return new String(data);
    }

    public static void main(String[] args) {
        try {
            String str = "This is just sample message!";
            System.out.println("Original: " + str);
            XOREncryption encryption = new XOREncryption("A1");
            byte[] data = str.getBytes();
            encryption.encrypt(data);
            System.out.println("Encrypted: " + new String(data));
            encryption.decrypt(data);
            System.out.println("Decrypted: " + new String(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
