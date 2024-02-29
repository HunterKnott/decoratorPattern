package dp.controller;
import java.util.Scanner;

public class StringPredicates {

    public static boolean notBlank(String s) {
        return !s.isEmpty();
    }

    public static boolean isLong(String s) {
        return s.length() > 40;
    }

    public static boolean isIn(String s, String toFind) {
        return s.contains(toFind);
    }

    public static boolean hasPound(String s) {
        return s.contains("#");
    }

    public static void main(String[] args) {
        // Sample usage
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a string:");
        String input = scanner.nextLine();
        scanner.close();

        if (notBlank(input)) {
            System.out.println("String is not blank");
        } else {
            System.out.println("String is blank");
        }

        if (isLong(input)) {
            System.out.println("String is longer than 40 characters");
        } else {
            System.out.println("String is not longer than 40 characters");
        }

        System.out.println("Enter a string to find:");
        String toFind = scanner.nextLine();
        if (isIn(input, toFind)) {
            System.out.println("String contains the specified substring");
        } else {
            System.out.println("String does not contain the specified substring");
        }

        if (hasPound(input)) {
            System.out.println("String contains '#' character");
        } else {
            System.out.println("String does not contain '#' character");
        }
    }
}