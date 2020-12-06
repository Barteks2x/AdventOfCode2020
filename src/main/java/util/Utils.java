package util;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Utils {
    public static <T> Collector<T, List<T>, List<List<T>>> splittingBy(Predicate<T> match) {
        return new Collector<>() {
            @Override
            public Supplier<List<T>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<List<T>, T> accumulator() {
                return List::add;
            }

            @Override
            public BinaryOperator<List<T>> combiner() {
                return (a, b) -> {
                    a.addAll(b);
                    return a;
                };
            }

            @Override
            public Function<List<T>, List<List<T>>> finisher() {
                return list -> {
                    List<List<T>> result = new ArrayList<>();
                    List<T> curr = new ArrayList<>();
                    for (T t : list) {
                        if (match.test(t)) {
                            result.add(curr);
                            curr = new ArrayList<>();
                            continue;
                        }
                        curr.add(t);
                    }
                    result.add(curr);
                    return result;
                };
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Collections.emptySet();
            }
        };
    }
}
