package day2;

import java.nio.file.*;
import java.util.stream.*;

public class Main2 {

    record PasswordEntry(String password, char letter, int num1, int num2) {
    }

    // 620, 727
    public static void main(String[] args) throws Throwable {
        var lines = Files.readAllLines(Paths.get("in2.txt")).stream()
                .map(Main2::parseEntry).collect(Collectors.toList());
        System.out.println(lines.stream().filter(entry -> {
            int count = entry.password.length() - entry.password.replaceAll("" + entry.letter, "").length();
            return count >= entry.num1 && count <= entry.num2;
        }).count());
        System.out.println(lines.stream().filter(entry ->
                (entry.password.charAt(entry.num1 - 1) == entry.letter)
                        ^ (entry.password.charAt(entry.num2 - 1) == entry.letter)
        ).count());
    }

    private static PasswordEntry parseEntry(String entry) {
        var parts = entry.split("[\\s:]+");
        var numbers = parts[0].split("-");
        return new PasswordEntry(parts[2], parts[1].charAt(0), Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
    }
}
