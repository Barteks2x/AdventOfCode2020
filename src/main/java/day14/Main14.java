package day14;

import it.unimi.dsi.fastutil.longs.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main14 {
    public static void main(String[] args) throws IOException {
        var in = Files.readAllLines(Paths.get("in14.txt")).toArray(String[]::new);

        var cpu = new CpuV1(1024 * 128);
        runCode(in, cpu);
        System.out.println(Arrays.stream(cpu.memory).sum());

        var cpu2 = new CpuV2();
        runCode(in, cpu2);
        System.out.println(cpu2.memory.values().stream().mapToLong(x -> x).sum());
    }

    private static void runCode(String[] in, Cpu cpu) {
        Arrays.stream(in).forEachOrdered(s -> {
            if (s.startsWith("mask = ")) {
                String mask = s.substring("mask = ".length());
                cpu.setMask(mask);
            } else {
                int addr = Integer.parseInt(s.substring(s.indexOf('[') + 1, s.indexOf(']')));
                long value = Long.parseLong(s.substring(s.indexOf('=') + 2));
                cpu.write(addr, value);
            }
        });
    }

    static class CpuV1 implements Cpu {
        private final long[] memory;
        long maskAnd = -1, maskOr = 0;

        CpuV1(int memory) {
            this.memory = new long[memory];
        }

        @Override public void setMask(String mask) {
            this.maskAnd = Long.parseLong(mask.replace('X', '1'), 2);
            this.maskOr = Long.parseLong(mask.replace('X', '0'), 2);
        }

        @Override public void write(long address, long value) {
            memory[(int) address] = (value & maskAnd) | maskOr;
        }
    }

    static class CpuV2 implements Cpu {
        private final Long2LongOpenHashMap memory = new Long2LongOpenHashMap();

        long maskOr;
        long maskAnd;
        long[] floatingBits = new long[36];
        int floatingBitsCount = 0;

        CpuV2() {
        }

        @Override public void setMask(String mask) {
            this.maskOr = Long.parseLong(mask.replace('X', '0'), 2);
            this.maskAnd = Long.parseLong(mask.replace('0', '1').replace('X', '0'), 2);
            long floatingBits = Long.parseLong(mask.replace('1', '0').replace('X', '1'), 2);
            floatingBitsCount = Long.bitCount(floatingBits);
            for (int i = 0, bit = 0; bit < 36; bit++) {
                if ((floatingBits & (1L << bit)) != 0) {
                    this.floatingBits[i++] = 1L << bit;
                }
            }
        }

        @Override public void write(long address, long value) {
            address |= maskOr;
            address &= maskAnd;
            int max = 1 << floatingBitsCount;
            for (int i = 0; i < max; i++) {
                long m = 0;
                for (int j = 0; j < floatingBitsCount; j++) {
                    if ((i & (1L << j)) != 0) {
                        m |= floatingBits[j];
                    }
                }
                memory.put(address | m, value);
            }
        }
    }


    public interface Cpu {
        void setMask(String mask);

        void write(long address, long value);
    }
}
