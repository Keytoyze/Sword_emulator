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
}
