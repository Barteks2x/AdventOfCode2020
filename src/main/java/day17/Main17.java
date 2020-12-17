package day17;

import it.unimi.dsi.fastutil.longs.*;

import java.io.*;
import java.nio.file.*;
import java.util.function.*;

public class Main17 {
    public static void main(String[] args) throws IOException {
        var in = Files.readAllLines(Paths.get("in17.txt")).toArray(String[]::new);

        var inGrid = new LongOpenHashSet(in.length * 4);
        for (int x = 0; x < in.length; x++) {
            char[] chars = in[x].toCharArray();
            for (int y = 0; y < chars.length; y++) {
                if (chars[y] == '#') {
                    inGrid.add(pack(x, y, 0, 0));
                }
            }
        }

        var grid = new LongOpenHashSet(in.length * 4);
        var newGrid = new LongOpenHashSet(in.length * 4);
        grid.addAll(inGrid);
        System.out.println(simulateAllSteps(grid, newGrid, Main17::forAllNeighbors3d));

        grid.clear();
        newGrid.clear();
        grid.addAll(inGrid);
        System.out.println(simulateAllSteps(grid, newGrid, Main17::forAllNeighbors4d));

    }

    private static int simulateAllSteps(LongOpenHashSet in, LongOpenHashSet out, NeighborIterator neighborIterator) {
        for (int i = 0; i < 6; i++) {
            simulate(in, out, neighborIterator);
            var t = in;
            in = out;
            out = t;
            out.clear();
        }
        return in.size();
    }

    private static void simulate(LongOpenHashSet in, LongOpenHashSet out, NeighborIterator neighborIterator) {
        in.forEach((long pos) -> neighborIterator.forAllNeighbors(pos, out::add));
        for (LongIterator iterator = out.iterator(); iterator.hasNext(); ) {
            long pos = iterator.nextLong();
            int neighborCount = neighborIterator.forAllNeighbors(pos, newPos -> newPos != pos && in.contains(newPos));
            if (in.contains(pos)) {
                if (neighborCount != 2 && neighborCount != 3) {
                    iterator.remove();
                }
            } else {
                if (neighborCount != 3) {
                    iterator.remove();
                }
            }
        }
    }

    private static int forAllNeighbors3d(long pos, LongPredicate test) {
        int x = x(pos);
        int y = y(pos);
        int z = z(pos);
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    long newPos = pack(x + dx, y + dy, z + dz, 0);
                    if (test.test(newPos)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private static int forAllNeighbors4d(long pos, LongPredicate test) {
        int x = x(pos);
        int y = y(pos);
        int z = z(pos);
        int w = w(pos);
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    for (int dw = -1; dw <= 1; dw++) {
                        long newPos = pack(x + dx, y + dy, z + dz, w + dw);
                        if (test.test(newPos)) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    private static long pack(int x, int y, int z, int w) {
        return (x & ((1L << 16) - 1)) | ((y & ((1L << 16) - 1)) << 16) | ((z & ((1L << 16) - 1)) << 32) | ((w & ((1L << 16) - 1)) << 48);
    }

    private static int x(long pos) {
        return ((((int) pos) & ((1 << 16) - 1)) << (32 - 16) >> (32 - 16));
    }

    private static int y(long pos) {
        return (((int) (pos >> 16) & ((1 << 16) - 1)) << (32 - 16) >> (32 - 16));
    }

    private static int z(long pos) {
        return (((int) (pos >> 32) & ((1 << 16) - 1)) << (32 - 16) >> (32 - 16));
    }

    private static int w(long pos) {
        return (int) (pos >> 48);
    }

    interface NeighborIterator {
        int forAllNeighbors(long pos, LongPredicate test);
    }
}
