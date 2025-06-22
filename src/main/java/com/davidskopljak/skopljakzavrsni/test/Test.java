package com.davidskopljak.skopljakzavrsni.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Formatter;

public class Test {

    public static void main(String[] args) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        strings.add("Lol");

        try (PrintWriter writer = new PrintWriter("test.txt")) {
            for (String str : strings) {
                writer.println(str);
            }
        } catch (IOException e) {
            System.out.println("File not found or IO error: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter("test2.txt")) {
            for (String str : strings) {
                writer.printf("%s%n", str); // Print with newline
            }
        } catch (IOException e) {
            System.out.println("File not found or IO error: " + e.getMessage());
        }

        try (Formatter formatter = new Formatter(new FileOutputStream("test3.txt"))) {
            for (String str : strings) {
                formatter.format("%s%n", str); // Each string on a new line
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

    }
}