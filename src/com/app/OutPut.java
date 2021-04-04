package com.app;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.MessageFormat;

public class OutPut {

    private final static int PREFIX_LENGTH = 3;
    private final static int SUFFIX_LENGTH = 3;

    private final byte[] text;
    private final long pointer;
    private final long offset;
    private final String fileName;
    private final RandomAccessFile reader;

    public OutPut(byte[] text, long pointer, String fileName, RandomAccessFile reader) {
        this.text = text;
        this.pointer = pointer;
        this.offset = Math.toIntExact(pointer - text.length + 1);
        this.fileName = fileName;
        this.reader = reader;
    }

    private String getSuffix() throws IOException {

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < SUFFIX_LENGTH; i++) {
            try {
                byte b = reader.readByte();

                if (b == '\n' || b == '\r' || b == '\t') {
                    escape(s, b);
                    continue;
                }

                s.append(new String(new byte[]{b}));
            } catch (IOException e) {
                break;
            }
        }

        reader.seek(pointer);
        return s.toString();

    }

    private String getPrefix() throws IOException {

        if (pointer - text.length == 0) {
            return "";
        }

        StringBuilder s = new StringBuilder();
        long tempPointer = pointer - text.length;
        for (int i = PREFIX_LENGTH; i > 0; i--) {
            try {
                reader.seek(tempPointer = tempPointer - 1);

                byte b = reader.readByte();

                if (b == '\n' || b == '\r' || b == '\t') {
                    escape(s, b, true);
                    continue;
                }

                s.append(new String(new byte[]{b}));
            } catch (IOException e) {
                break;
            }
        }

        reader.seek(pointer);

        return s.reverse().toString();

    }

    private void escape(StringBuilder s, byte b) {
        escape(s, b, false);
    }

    private void escape(StringBuilder s, byte b, boolean reverse) {

        if (b == '\n') {
            s.append(reverse ? "n\\" : "\\n");
        }

        if (b == '\r') {
            s.append(reverse ? "r\\" : "\\r");
        }

        if (b == '\t') {
            s.append(reverse ? "t\\" : "\\t");
        }
    }

    public String print() throws IOException {
        return MessageFormat.format("<{0}>(<{1}>):<{2}>...<{3}>",
                fileName, offset, getPrefix(), getSuffix());

    }
}
