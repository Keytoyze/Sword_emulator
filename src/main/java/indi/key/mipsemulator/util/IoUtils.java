package indi.key.mipsemulator.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

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

    public static int bytesToInt(byte[] bytes) {
        return BitArray.of(bytes).value();
    }

    public static int bytesToUnsignedInt(byte[] bytes) {
        return BitArray.of(bytes).integerValue();
    }

    public static byte[] intToBytes(int value, int length) {
        return BitArray.of(value, length).bytes();
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

    public static String longToString(Long... longs) {
        return Stream.of(longs)
                .map(aLong -> "0x" + completeWithZero(Long.toHexString(aLong), 8).toUpperCase())
                .reduce((s, s2) -> s + "; " + s2)
                .orElse("");
    }

    public static Long[] stringToLong(String value) {
        return Stream.of(value.split(";"))
                .map(s -> Integer.toUnsignedLong(parseUnsignedInteger(s.trim())))
                .toArray(Long[]::new);
    }
}
