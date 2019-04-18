package indi.key.mipsemulator.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import indi.key.mipsemulator.model.bean.BitArray;

public class IoUtils {

    public static byte[] read(File file) {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;

        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            close(fis);
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
}
