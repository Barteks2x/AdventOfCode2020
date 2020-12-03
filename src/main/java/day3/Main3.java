package day3;

import java.io.*;
import java.nio.file.*;

public class Main3 {
    record Vec2(int x, int y) {
        public Vec2 add(Vec2 v) {
            return new Vec2(x + v.x, y + v.y);
        }
    }

    public static void main(String[] args) throws IOException {
        var lines = Files.readAllLines(Paths.get("in3.txt")).toArray(String[]::new);

        var slope = new Vec2(3, 1);
        System.out.println(countTrees(lines, slope));

        var slopes = new Vec2[]{new Vec2(1, 1), new Vec2(3, 1), new Vec2(5, 1), new Vec2(7, 1), new Vec2(1, 2)};
        long result = 1;
        for (var s : slopes) {
            result *= countTrees(lines, s);
        }
        System.out.println(result);
    }

    private static int countTrees(String[] lines, Vec2 slope) {
        var position = new Vec2(0, 0);
        int count = 0;
        while (position.y < lines.length) {
            if (hasTree(lines, position)) count++;
            position = position.add(slope);
        }
        return count;
    }

    private static boolean hasTree(String[] lines, Vec2 position) {
        var line = lines[position.y];
        return line.charAt(Math.floorMod(position.x, line.length())) == '#';
    }
}
