package jp.co.biglobe.isp.oss.statetransition.datasource;


import jp.co.biglobe.isp.oss.statetransition.datasource.exception.ListAnyLatestException;
import jp.co.biglobe.isp.oss.statetransition.datasource.exception.ListLatestIsNotLastException;
import jp.co.biglobe.isp.oss.statetransition.datasource.exception.ListSortException;
import jp.co.biglobe.isp.oss.statetransition.domain.StateEventDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@EqualsAndHashCode
public class StateEventList {
    @Getter
    private final List<StateEvent> list; // StateEventIdの昇順に並んでいること

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public Optional<RuntimeException> validate() {
        if(isEmpty()) {
            return Optional.empty();
        }

        return Stream.of(
                リストがイベント日時の昇順に並んでいること(),
                最終要素のみisLatestであること()
        ).filter(Optional::isPresent).map(Optional::get).findFirst();
    }

    public void validateAndThrow() {
        validate().ifPresent(v -> { throw v; });
    }

    /**
     * リストの最終要素を返す
     * @return
     */
    public StateEvent getLatest() {
        return list.get(list.size() - 1);
    }

    public List<StateEvent> getNotLatestList() {
        return list.subList(0, list.size() - 1);
    }

    private Optional<RuntimeException> リストがイベント日時の昇順に並んでいること() {
        if(isEmpty()) {
            return Optional.empty();
        }
        return list.stream()
                .map(StateEvent::getStateEventDateTime)
                .map(StateEventDateTime::getValue)
                .map(Either::<RuntimeException, LocalDateTime>right)
                .reduce((memo, v) -> {
                    if(memo.isLeft()) {
                        return memo;
                    }
                    if(v.get().isBefore(memo.get())) {
                        return Either.left(new ListSortException(list.get(0).getStateType()));
                    }
                    return v;
                }).flatMap(v -> v.left);
    }

    private Optional<RuntimeException> 最終要素のみisLatestであること() {
        if(isEmpty()) {
            return Optional.empty();
        }

        // 最終要素がisLatestであること
        StateEvent last = list.get(list.size() - 1);
        if(!list.get(list.size() - 1).isLatest()) {
            return Optional.of(new ListLatestIsNotLastException(last.getEventId()));
        }

        // isLatestが複数ないこと
        List<StateEvent> latestEventList =  list.stream().filter(StateEvent::isLatest).collect(Collectors.toList());
        if(latestEventList.size() > 1) {
            return Optional.of(new ListAnyLatestException(latestEventList.stream().map(StateEvent::getEventId).collect(Collectors.toList())));
        }

        return Optional.empty();
    }

    @AllArgsConstructor
    private static class Either<L, R> {
        Optional<L> left;
        Optional<R> right;
        boolean isLeft() {
            return left.isPresent();
        }
        R get() {
            return right.get();
        }
        static <L, R> Either<L, R> left(L left) {
            return new Either<L, R>(Optional.of(left), Optional.empty());
        }

        static <L, R> Either<L, R> right(R right) {
            return new Either<L, R>(Optional.empty(), Optional.of(right));
        }
    }

}
