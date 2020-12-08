package day8;

import it.unimi.dsi.fastutil.ints.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

public class Main8 {
    public static void main(String[] args) throws IOException {
        var code = Files.readAllLines(Paths.get("in8.txt")).stream().map(Main8::parseInsn).toArray(Insn[]::new);
        IntSet executedInsntructions = new IntOpenHashSet();

        new Cpu(code).run((c) -> {
            if (!executedInsntructions.add(c.pc)) {
                System.out.println(c.acc);
                return -1;
            }
            return 0;
        });

        for (int i = 0; i < code.length; i++) {
            if (code[i].opcode == Opcode.ACC) {
                continue;
            }
            var modified = code.clone();
            modified[i] = new Insn(modified[i].opcode == Opcode.JMP ? Opcode.NOP : Opcode.JMP, modified[i].arg);

            executedInsntructions.clear();
            Cpu cpu = new Cpu(modified);
            int result = cpu.run((c) -> executedInsntructions.add(c.pc) ? 0 : -1);
            if (result == 0) {
                System.out.println(cpu.acc);
            }
        }

    }

    private static Insn parseInsn(String s) {
        String[] parts = s.split(" ");
        return new Insn(Opcode.valueOf(parts[0].toUpperCase(Locale.ROOT)), Integer.parseInt(parts[1]));
    }

    private static class Cpu {
        final Insn[] code;
        int pc = 0;
        int acc = 0;

        private Cpu(Insn[] code) {
            this.code = code;
        }

        public int run(ToIntFunction<Cpu> beforeExecute) {
            while (pc < code.length) {
                int i = beforeExecute.applyAsInt(this);
                if (i != 0) {
                    return i;
                }
                code[pc].execute(this);
            }
            return 0;
        }
    }

    private enum Opcode {
        ACC((cpu, arg) -> {
            cpu.acc += arg;
            cpu.pc++;
        }),
        JMP((cpu, arg) -> cpu.pc += arg),
        NOP((cpu, arg) -> cpu.pc++);

        private final BiConsumer<Cpu, Integer> execute;

        Opcode(BiConsumer<Cpu, Integer> execute) {
            this.execute = execute;
        }
    }

    private record Insn(Opcode opcode, int arg) {
        public void execute(Cpu cpu) {
            opcode.execute.accept(cpu, arg);
        }
    }
}
