package com.partyrock.temp;

import java.util.Arrays;

public class Temp1 {
    public static void main(String... args) {
        String x = (char) 128 + "";

        System.out.println(Arrays.toString(x.getBytes()));
    }
}
