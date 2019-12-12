/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package indi.key.mipsemulator.disassemble;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import indi.key.mipsemulator.util.IoUtils;
import indi.key.mipsemulator.util.RegexUtils;


public class CoeReader {

    private static String GROUP_RADIX = "radix";
    private static ThreadLocal<Matcher> RADIX_MATCHER =
            RegexUtils.makeThreadLocalMatcher("memory_initialization_radix\\s*=\\s*(?<" + GROUP_RADIX + ">.*?)\\s*;");

    private static String GROUP_VECTOR = "vector";
    private static ThreadLocal<Matcher> VECTOR_MATCHER = RegexUtils.makeThreadLocalMatcher(
            "memory_initialization_vector\\s*=\\s*(?<" + GROUP_VECTOR + ">.*?)\\s*;", Pattern.DOTALL);

    private static ThreadLocal<Matcher> VECTOR_IGNORE_MATCHER = RegexUtils.makeThreadLocalMatcher("[,\\s]+");

    public static byte[] hexToBytes(InputStream inputStream) throws IOException {
        String vector = IoUtils.readAsString(inputStream);
        Matcher vectorIgnoreMatcher = VECTOR_IGNORE_MATCHER.get().reset(vector);
        vector = vectorIgnoreMatcher.replaceAll("");
        byte[] bytes = new byte[(int) Math.ceil(vector.length() / 2d)];
        for (int i = 0, j = 0, k = 2; j < vector.length(); ++i, j = k, k += 2) {
            if (k > vector.length()) {
                k = vector.length();
            }
            bytes[i] = parseUnsignedByte(vector.substring(j, k), 16);
        }
        return bytes;
    }

    public static byte[] coeToBytes(InputStream inputStream) throws IOException, CoeReaderException {

        String input = IoUtils.readAsString(inputStream).toLowerCase();
        Matcher radixMatcher = RADIX_MATCHER.get().reset(input);
        if (!radixMatcher.find()) {
            throw new CoeReaderException("Radix not found");
        }
        int radix;
        try {
            radix = Integer.parseInt(radixMatcher.group(GROUP_RADIX));
        } catch (NumberFormatException e) {
            throw new CoeReaderException("Cannot parse radix", e);
        }
        Matcher vectorMatcher = VECTOR_MATCHER.get().reset(input);
        if (!vectorMatcher.find()) {
            throw new CoeReaderException("Vector not found");
        }
        String vector = vectorMatcher.group(GROUP_VECTOR);
        Matcher vectorIgnoreMatcher = VECTOR_IGNORE_MATCHER.get().reset(vector);
        vector = vectorIgnoreMatcher.replaceAll("");

        byte[] bytes;
        switch (radix) {
            case 2:
                bytes = new byte[(int) Math.ceil(vector.length() / 8d)];
                for (int i = 0, j = 0, k = 8; j < vector.length(); ++i, j = k, k += 8) {
                    if (k > vector.length()) {
                        k = vector.length();
                    }
                    bytes[i] = parseUnsignedByte(vector.substring(j, k), radix);
                }
                break;
            case 16:
                bytes = new byte[(int) Math.ceil(vector.length() / 2d)];
                for (int i = 0, j = 0, k = 2; j < vector.length(); ++i, j = k, k += 2) {
                    if (k > vector.length()) {
                        k = vector.length();
                    }
                    bytes[i] = parseUnsignedByte(vector.substring(j, k), radix);
                }
                break;
            default:
                throw new CoeReaderException("Unsupported radix: " + radix);
        }
        return bytes;
    }

    private static byte parseUnsignedByte(String s, int radix) {
        short sh = Short.parseShort(s, radix);
        if (sh > (Byte.MAX_VALUE - Byte.MIN_VALUE)) {
            throw new NumberFormatException(String.format("String value %s exceeds range of unsigned byte.", s));
        }
        return (byte) sh;
    }
}
