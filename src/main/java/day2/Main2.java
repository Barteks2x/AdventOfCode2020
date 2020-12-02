package day2;

import java.nio.file.*;
import java.util.stream.*;

public class Main2 {
    record PasswordEntry(String password, char letter, int num1, int num2) {
        boolean valid1() {
            int count = password.length() - password.replaceAll("" + letter, "").length();
            return count >= num1 && count <= num2;
        }

        boolean valid2() {
            return (password.charAt(num1 - 1) == letter) ^ (password.charAt(num2 - 1) == letter);
        }
    }

    // 620, 727
    public static void main(String[] args) throws Throwable {
        var lines = Files.readAllLines(Paths.get("in2.txt")).stream().map(Main2::parseEntry).collect(Collectors.toList());
        System.out.println(lines.stream().filter(PasswordEntry::valid1).count());
        System.out.println(lines.stream().filter(PasswordEntry::valid2).count());
    }

    private static PasswordEntry parseEntry(String entry) {
        var parts = entry.split("[\\s:]+");
        var numbers = parts[0].split("-");
        return new PasswordEntry(parts[2], parts[1].charAt(0), Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
    }
}
