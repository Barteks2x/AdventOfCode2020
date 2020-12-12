package day12;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

public class Main12 {
    public static final Direction[] DIRECTIONS = {Direction.N, Direction.E, Direction.S, Direction.W};

    record Vec2(int x, int y) {
        public Vec2 add(Vec2 v) {
            return new Vec2(x + v.x, y + v.y);
        }

        public Vec2 mul(int a) {
            return new Vec2(x * a, y * a);
        }

        public int distance() {
            return Math.abs(x) + Math.abs(y);
        }

    }

    enum Direction {
        N(0, 1), E(1, 0), S(0, -1), W(-1, 0),
        R((rot, arg) -> new Vec2(0, 0), arg -> arg),
        L((rot, arg) -> new Vec2(0, 0), arg -> -arg),
        F((rot, arg) -> DIRECTIONS[Math.floorDiv(rot, 90) & 3].direction.apply(rot, arg), arg -> 0);

        private final BiFunction<Integer, Integer, Vec2> direction;
        private final IntUnaryOperator rotation;

        Direction(int x, int y) {
            this.direction = (rot, arg) -> new Vec2(arg * x, arg * y);
            this.rotation = (arg) -> 0;
        }

        Direction(BiFunction<Integer, Integer, Vec2> direction, IntUnaryOperator rotation) {
            this.direction = direction;
            this.rotation = rotation;
        }
    }

    record Instruction(Direction dir, int number) {
        public static Instruction parse(String in) {
            return new Instruction(Direction.valueOf(in.substring(0, 1)), Integer.parseInt(in.substring(1)));
        }
    }

    static class Ship {
        Vec2 pos = new Vec2(0, 0);
        int rotation = 90;

        void move(Instruction insn) {
            pos = pos.add(insn.dir.direction.apply(rotation, insn.number));
            rotation += insn.dir.rotation.applyAsInt(insn.number);
        }
    }

    static class Ship2 {
        Vec2 pos = new Vec2(0, 0);
        Vec2 waypoint = new Vec2(10, 1);

        void move(Instruction insn) {
            if (insn.dir == Direction.F) {
                pos = pos.add(waypoint.mul(insn.number));
                return;
            }
            Vec2 move = insn.dir.direction.apply(0, insn.number);
            int rot = insn.dir.rotation.applyAsInt(insn.number);
            waypoint = waypoint.add(move);
            rot = Math.floorDiv(rot, 90) & 3;
            switch (rot) {
                case 1 -> waypoint = new Vec2(+waypoint.y, -waypoint.x);
                case 2 -> waypoint = waypoint.mul(-1);
                case 3 -> waypoint = new Vec2(-waypoint.y, +waypoint.x);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        var input = Files.readAllLines(Path.of("in12.txt"))
                .stream().map(Instruction::parse)
                .toArray(Instruction[]::new);

        var ship = new Ship();
        Arrays.stream(input).forEach(ship::move);
        System.out.println(ship.pos.distance());

        var ship2 = new Ship2();
        Arrays.stream(input).forEach(ship2::move);
        System.out.println(ship2.pos.distance());
    }
}
