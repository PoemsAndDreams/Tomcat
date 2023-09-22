package com.dreams.http;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

public class ResponseServletOutputStream extends ServletOutputStream {

    private byte[] bytes = new byte[1024];
    private int index = 0;
    @Override
    public void write(int b) throws IOException {
        bytes[index] = (byte) b;
        index++;
    }

    @Override
    public void write(byte[] b) throws IOException {
        bytes = new byte[b.length];
        bytes = b;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getIndex() {
        return index;
    }
}
