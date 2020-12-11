package day11;

import java.io.*;
import java.nio.file.*;

public class Main11 {
    public static void main(String[] args) throws IOException {
        var input = Files.readAllLines(Path.of("in11.txt"))
                .stream().map(String::toCharArray)
                .toArray(char[][]::new);

        char[][] in = deepCopy(input);
        var out = new char[input.length][input[0].length];

        while (simulate(in, out, Main11::adjacent, 4) != 0) {
            char[][] t = in;
            in = out;
            out = t;
        }
        System.out.println(countOccupied(out));

        in = deepCopy(input);
        while (simulate(in, out, Main11::adjacent2, 5) != 0) {
            char[][] t = in;
            in = out;
            out = t;
        }
        System.out.println(countOccupied(out));
    }

    private static char[][] deepCopy(char[][] input) {
        var in = new char[input.length][input[0].length];
        for (int i = 0; i < input.length; i++) {
            char[] chars = input[i];
            in[i] = chars.clone();
        }
        return in;
    }

    private static int countOccupied(char[][] out) {
        int count = 0;
        for (char[] chars : out) {
            for (char x : chars) {
                if (x == '#') {
                    count++;
                }
            }
        }
        return count;
    }

    private static void print(char[][] in) {
        for (char[] chars : in) {
            System.out.println(String.valueOf(chars));
        }
        System.out.println();
    }

    private static int simulate(char[][] in, char[][] out, AdjacentCount count, int threshold) {
        int changed = 0;
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[i].length; j++) {
                if (in[i][j] == 'L' && count.countAt(i, j, in) == 0) {
                    out[i][j] = '#';
                    changed++;
                } else if (in[i][j] == '#' && count.countAt(i, j, in) >= threshold) {
                    out[i][j] = 'L';
                    changed++;
                } else {
                    out[i][j] = in[i][j];
                }
            }
        }
        return changed;
    }

    private static int adjacent(int x, int y, char[][] in) {
        int total = 0;
        for (int i = -1; i <= 1; i++) {
            if (x + i < 0 || x + i >= in.length)
                continue;
            for (int j = -1; j <= 1; j++) {
                if ((i == 0 && j == 0) || y + j < 0 || y + j >= in[x + i].length)
                    continue;
                total += in[x + i][y + j] == '#' ? 1 : 0;
            }
        }
        return total;
    }

    private static int adjacent2(int x, int y, char[][] in) {
        int total = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0)
                    continue;
                int cx = x + dx;
                int cy = y + dy;
                while (cx >= 0 && cx < in.length && cy >= 0 && cy < in[cx].length) {
                    if (in[cx][cy] == 'L') {
                        break;
                    }
                    if (in[cx][cy] == '#' ) {
                        total++;
                        break;
                    }
                    cx += dx;
                    cy += dy;
                }
            }
        }
        return total;
    }

    interface AdjacentCount {
        int countAt(int x, int t, char[][] in);
    }
}
