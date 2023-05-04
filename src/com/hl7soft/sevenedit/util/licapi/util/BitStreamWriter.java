package com.hl7soft.sevenedit.util.licapi.util;

import java.io.IOException;
import java.io.OutputStream;

public class BitStreamWriter {
    private static final int[] mask = new int[] {
            1, 3, 7, 15, 31, 63, 127, 255, 511, 1023,
            2047, 4095, 8191, 16383, 32767, 65535 };

    private OutputStream os;

    private int bitBuf;

    private byte bitBufPtr;

    public BitStreamWriter(OutputStream os) {
        this.os = os;
    }

    private void flushBytes() throws IOException {
        while (this.bitBufPtr >= 8) {
            this.os.write(this.bitBuf >> 24);
            this.bitBuf <<= 8;
            this.bitBufPtr = (byte)(this.bitBufPtr - 8);
        }
    }

    public void write(boolean bit) throws IOException {
        if (this.bitBufPtr >= 8)
            flushBytes();
        if (bit)
            this.bitBuf |= 1 << 31 - this.bitBufPtr;
        this.bitBufPtr = (byte)(this.bitBufPtr + 1);
    }

    public void write(int len, int code) throws IOException {
        if (len > 16 || len < 1)
            throw new RuntimeException("Not supported symbol size: " + len);
        if (this.bitBufPtr >= 8)
            flushBytes();
        if (code < 0)
            code &= mask[len - 1];
        this.bitBuf |= code << 32 - this.bitBufPtr - len;
        this.bitBufPtr = (byte)(this.bitBufPtr + len);
    }

    public void align() throws IOException {
        int bitsToFill = 8 - this.bitBufPtr % 8;
        if (bitsToFill != 0 && bitsToFill != 8)
            this.bitBufPtr = (byte)(this.bitBufPtr + bitsToFill);
    }

    public void close() throws IOException {
        if (this.bitBufPtr != 0) {
            align();
            flushBytes();
        }
        this.bitBuf = 0;
        this.bitBufPtr = 0;
    }

    public void reset() {
        this.bitBuf = 0;
        this.bitBufPtr = 0;
    }

    private static int revert(int size, int sym) {
        int revSym = 0;
        for (int i = 0; i < size; i++) {
            if ((sym & 1 << i) != 0)
                revSym |= 1 << size - i - 1;
        }
        return revSym;
    }

    public OutputStream getStream() {
        return this.os;
    }
}
