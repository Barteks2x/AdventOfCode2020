package day18;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main18 {
    public static void main(String[] args) throws IOException {
        var in = Files.readAllLines(Paths.get("in18.txt")).toArray(String[]::new);
        System.out.println(Arrays.stream(in)
                .map(s -> s.replaceAll("\\(", " ( ").replaceAll("\\)", " ) "))
                .mapToLong(Main18::evaluate).sum());

        System.out.println(Arrays.stream(in)
                .map(s -> s.replaceAll("\\(", " ( ").replaceAll("\\)", " ) "))
                .mapToLong(Main18::evaluate2).sum());
    }

    private static long evaluate(String s) {
        StringReader reader = new StringReader(s);
        try {
            Scanner s1 = new Scanner(reader);
            return evaluate(s1, "");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static long evaluate2(String s) {
        StringReader reader = new StringReader(s);
        try {
            Scanner s1 = new Scanner(reader);
            return evaluate2(s1, "");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static long evaluate(Scanner s, String tab) throws IOException {
        boolean hasNextInt = s.hasNextInt();
        if (hasNextInt || s.hasNext("\\(")) {
            long v;
            if (hasNextInt) {
                v = s.nextInt();
            } else {
                s.next("\\(");
                v = evaluate(s, tab + "  ");
                s.next("\\)");
            }
            //System.out.println(tab+v);
            while (s.hasNext("[+*]")) {
                String operator = s.next("[+*]");
                //System.out.println(tab+operator);
                long n;
                if (s.hasNextInt()) {
                    n = s.nextInt();
                } else {
                    s.next("\\(");
                    n = evaluate(s, tab + "  ");
                    s.next("\\)");
                }
                //System.out.println(tab+n);
                if (operator.equals("*")) {
                    v *= n;
                } else {
                    v += n;
                }
            }
            return v;
        }
        if (s.hasNext()) {
            throw new IllegalStateException();
        }
        return 0;
    }


    private static long evaluate2(Scanner s, String tab) throws IOException {
        boolean hasNextInt = s.hasNextInt();
        if (hasNextInt || s.hasNext("\\(")) {
            long v;
            if (hasNextInt) {
                v = s.nextInt();
            } else {
                s.next("\\(");
                v = evaluate2(s, tab + "  ");
                s.next("\\)");
            }
            //System.out.println(tab+v);
            while (s.hasNext("[+*]")) {
                String operator = s.next("[+*]");
                //System.out.println(tab+operator);
                long n;
                if (s.hasNextInt() || operator.equals("*")) {
                    n = operator.equals("+") ? s.nextInt() : evaluate2(s, tab + "  ");
                } else {
                    s.next("\\(");
                    n = evaluate2(s, tab + "  ");
                    s.next("\\)");
                }
                //System.out.println(tab+n);
                if (operator.equals("*")) {
                    v *= n;
                } else {
                    v += n;
                }
            }
            return v;
        }
        if (s.hasNext()) {
            throw new IllegalStateException();
        }
        return 0;
    }
}
