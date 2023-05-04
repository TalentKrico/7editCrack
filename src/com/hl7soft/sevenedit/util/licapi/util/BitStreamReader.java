package com.hl7soft.sevenedit.util.licapi.util;

import java.io.IOException;
import java.io.InputStream;

public class BitStreamReader {
    private static final int[] mask = new int[] {
            0, 1, 3, 7, 15, 31, 63, 127, 255, 511,
            1023, 2047, 4095, 8191, 16383, 32767, 65535 };

    private InputStream is;

    private int bitBuf;

    private byte bitBufPtr;

    private int pos;

    public BitStreamReader(InputStream is) {
        this.is = is;
    }

    public void reset() {
        this.pos = 0;
        this.bitBufPtr = 0;
        this.bitBuf = 0;
    }

    private void updateBuf(int minLength) throws IOException {
        while (this.bitBufPtr < minLength) {
            this.bitBuf <<= 8;
            this.bitBuf |= readByte();
            this.bitBufPtr = (byte)(this.bitBufPtr + 8);
        }
    }

    private int readByte() throws IOException {
        int b = this.is.read();
        if (b == -1)
            throw new IOException("End of bitstream data!");
        return b;
    }

    public boolean read() throws IOException {
        if (this.bitBufPtr == 0)
            updateBuf(1);
        this.bitBufPtr = (byte)(this.bitBufPtr - 1);
        this.pos++;
        return ((this.bitBuf >> this.bitBufPtr & 0x1) != 0);
    }

    public int read(int len) throws IOException {
        if (this.bitBufPtr < len)
            updateBuf(len);
        this.bitBufPtr = (byte)(this.bitBufPtr - len);
        int symb = this.bitBuf >> this.bitBufPtr;
        this.pos += len;
        return symb & mask[len];
    }

    public void align() throws IOException {
        int toSkip = 8 - this.pos % 8;
        if (toSkip != 0 && toSkip != 8)
            skip(toSkip);
    }

    public void skip(int n) throws IOException {
        for (int i = 0; i < n; i++)
            read();
    }

    private static int revSym(int size, int sym) {
        int revSym = 0;
        for (int i = 0; i < size; i++) {
            if ((sym & 1 << i) != 0)
                revSym |= 1 << size - i - 1;
        }
        return revSym;
    }

    public int pos() {
        return this.pos;
    }

    public InputStream getStream() {
        return this.is;
    }
}
