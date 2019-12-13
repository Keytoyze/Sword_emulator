package indi.key.mipsemulator.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import indi.key.mipsemulator.model.info.BitArray;

import static java.lang.Integer.parseUnsignedInt;

public class IoUtils {

    public static byte[] read(String resName) {
        try {
            return read(IoUtils.class.getResourceAsStream(resName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] read(File file) {
        try {
            return read(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readAsString(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        char[] buffer = new char[4096];
        int length;
        while ((length = reader.read(buffer)) != -1) {
            builder.append(buffer, 0, length);
        }
        return builder.toString();
    }

    private static byte[] read(InputStream inputStream) {
        byte[] buffer = null;
        ByteArrayOutputStream bos = null;

        try {
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = inputStream.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            close(inputStream);
            close(bos);
        }

        return buffer;
    }

    private static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final BitArray CONVERT_UTIL = BitArray.ofEmpty();

    public static int bytesToInt(byte[] bytes) {
        if (bytes.length > 4) {
            throw new IllegalArgumentException("Bytes length > 4");
        }
        int value = 0;
        for (byte b : bytes) {
            value <<= 8;
            value |= b & 0xFF;
        }
        return value;
    }

    public synchronized static int bytesToUnsignedInt(byte[] bytes) {
//        return BitArray.of(bytesToInt(bytes), bytes.length * 8).integerValue();
        synchronized (CONVERT_UTIL) {
            return CONVERT_UTIL.setTo(bytesToInt(bytes), bytes.length * 8).integerValue();
        }
    }

    public synchronized static byte[] intToBytes(int value, int length) {
        synchronized (CONVERT_UTIL) {
            return CONVERT_UTIL.setTo(value, length).bytes();
        }
    }

    public static String completeWithZero(String content, int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length - content.length(); i++) {
            stringBuilder.append("0");
        }
        return stringBuilder.append(content).toString();
    }

    public static int parseUnsignedInteger(String string) {

        if (string.length() == 0) {
            throw new NumberFormatException("Zero length string");
        }

        int index = 0;
        int radix = 10;

        // Handle radix specifier, if present
        if (string.startsWith("0x", index) || string.startsWith("0X", index)) {
            index += 2;
            radix = 16;
        } else if (string.startsWith("0b", index) || string.startsWith("0B", index)) {
            index += 2;
            radix = 2;
        } else if (string.startsWith("#", index)) {
            ++index;
            radix = 16;
        } else if (string.startsWith("0", index) && string.length() > 1 + index) {
            ++index;
            radix = 8;
        }

        if (string.startsWith("-", index) || string.startsWith("+", index)) {
            throw new NumberFormatException("Illegal sign character found");
        }

        return parseUnsignedInt(string.substring(index), radix);
    }

    public static String longToString(long[] longs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < longs.length; i++) {
            sb.append("0x").append(completeWithZero(Long.toHexString(longs[i]), 8).toUpperCase());
            if (i != longs.length - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    public static long[] stringToLong(String value) {
        String[] splited = value.split(";");
        long[] result = new long[splited.length];
        for (int i = 0; i < splited.length; i++) {
            result[i] = Integer.toUnsignedLong(parseUnsignedInteger(splited[i].trim()));
        }
        return result;
    }
}
