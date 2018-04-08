package jp.co.biglobe.isp.oss.statetransition.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StateTransition<T extends State> {
    final List<T> entryStates;
    final Map<T, List<T>> map;

    public StateTransition(List<T> entryStates, Map<T, List<T>> map) {
        this.entryStates = entryStates;
        this.map = map;
    }

    public boolean isValidTransition(State current, State next) {
        if(!current.getClass().getName().equals(next.getClass().getName())) {
            throw new IllegalArgumentException("state not matched");
        }
        return map.get(current).stream().anyMatch(next::equals);
    }

    public boolean isEntry(State next) {
        return entryStates.stream().anyMatch(next::equals);
    }

    public static class Builder<T extends State> {
        private List<T> entryStates;
        private final Map<T, List<T>> map = new HashMap<>();

        public Builder<T> entryStates(T... args) {
            this.entryStates = Stream.of(args).collect(Collectors.toList());
            return this;
        }

        public Builder<T> put(Entry<T> entry) {
            if(entry.next == null) {
                throw new IllegalArgumentException("transition not found: " + entry.current.toString());
            }
            this.map.put(entry.current, entry.next);
            return this;
        }


        public StateTransition<T> build() {
            if(entryStates == null) {
                throw  new RuntimeException("entryState is null");
            }
            return new StateTransition<T>(entryStates, map);
        }

        public static class Entry<T extends State> {
            private final T current;
            private List<T> next;

            public Entry(T current) {
                this.current = current;
            }
            public Entry<T> next(T... args) {
                this.next = Stream.of(args).collect(Collectors.toList());
                return this;
            }
        }
    }
}
