package com.pandaer.utils;

import java.util.Scanner;

public class InputUtil {
    public static String getUserInput(String hint) {
        System.out.println(hint);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
}
