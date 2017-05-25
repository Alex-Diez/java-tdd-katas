package kata.java;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class PersistentList<E> {
    private static final PersistentList<?> EMPTY = new PersistentList<>(null, null);

    private final E head;
    private final PersistentList<E> tail;

    public static <E> PersistentList<E> empty() {
        return (PersistentList<E>) EMPTY;
    }

    public PersistentList(E head, PersistentList<E> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        PersistentList list = this;
        if (list != EMPTY) {
            while (list.tail != EMPTY) {
                builder.append(list.head);
                builder.append(", ");
                list = list.tail;
            }
            builder.append(list.head);
        }
        builder.append(']');
        return builder.toString();
    }

    public PersistentList<E> prepend(E item) {
        return new PersistentList<>(item, this);
    }

    public Optional<E> head() {
        return Optional.ofNullable(head);
    }

    public PersistentList<E> tail() {
        return this == EMPTY ? empty() : tail;
    }

    public boolean forAll(Predicate<E> predicate) {
        return this == EMPTY || predicate.test(head) && tail.forAll(predicate);
    }

    public PersistentList<E> drop(int n) {
        if (this == EMPTY) return empty();
        else if (n == 0) return this;
        else return tail.drop(n - 1);
    }

    public PersistentList<E> dropWhile(Predicate<E> predicate) {
        if (this == EMPTY) return empty();
        else if (!predicate.test(head)) return this;
        else return tail.dropWhile(predicate);
    }

    public PersistentList<E> filter(Predicate<E> predicate) {
        return this == EMPTY ? empty() : predicate.test(head) ? new PersistentList<>(head, tail.filter(predicate)) : tail.filter(predicate);
    }

    public PersistentList<E> reverse() {
        return foldLeft(PersistentList::prepend, empty());
    }

    public <R> PersistentList<R> map(Function<E, R> map) {
        return reverse().foldLeft((acc, item) -> acc.prepend(map.apply(item)), empty());
    }

    public <A> A foldLeft(BiFunction<A, E, A> function, A accumulator) {
        return this == EMPTY ? accumulator : tail.foldLeft(function, function.apply(accumulator, head));
    }
}
