package com.mljr.spring.redis.ext.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by junqing.li on 16/3/23.
 */
public class Utils {

    public static final byte[] EMPTY_ARRAY = new byte[0];

    public static boolean isEmpty(byte[] data) {
        return (data == null || data.length == 0);
    }

    /**
     * @param objects
     * @return
     */
    public static boolean isEmpty(Object[] objects) {

        return Objects.isNull(objects) || objects.length == 0;
    }

    public static boolean isEmpty(Map map) {

        return Objects.isNull(map) || map.isEmpty();
    }

    public static boolean isEmpty(String string) {

        return Objects.isNull(string) || string.trim().length() == 0;
    }

    public static void main(String[] args) {

        List<String> languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");

        System.out.println("Languages which starts with J :");
        languages.stream().filter(language -> language.startsWith("J")).forEach((name) -> {
            System.out.println(name + " ");
        });
    }

}
