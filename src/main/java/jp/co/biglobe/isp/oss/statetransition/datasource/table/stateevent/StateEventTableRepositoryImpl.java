package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent;

import jp.co.biglobe.isp.oss.statetransition.datasource.StateEvent;
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId;
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventList;
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventTableNameFactory;
import jp.co.biglobe.isp.oss.statetransition.datasource.db.StateEventMapper;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class StateEventTableRepositoryImpl implements StateEventTableRepository {
    private final StateEventMapper stateEventMapper;
    private final StateEventTableNameFactory stateEventTableNameFactory;

    public void insertStateEvent(
            InsertStateEventContainer insertStateEventContainer
    ) {
        stateEventMapper.insertStateEvent(
                stateEventTableNameFactory.createStateEventTableName(insertStateEventContainer.getStateType()),
                insertStateEventContainer
        );
    }

    @Override
    public Optional<StateEvent> findAllLatestEvent(FindContainer container) {
        return Optional.ofNullable(stateEventMapper.findLatest(stateEventTableNameFactory.createStateEventTableName(container.getStateType()), container))
                .map(v -> v.toEntity(container.getStateType()));
    }

    public List<StateEvent> findAllLatestEvent(StateCustomSelectorContainer container) {
        return container
                .getTermOptional()
                .map(term -> term.mapIfOnlyStart(
                        // 期間指定で開始日のみある場合
                        t -> stateEventMapper.findAllLatestByStateFromDateTime(
                                stateEventTableNameFactory.createStateEventTableName(container.getStateType()),
                                container.getStateType(),
                                t.getStartDateTime().getValue(),
                                container.getStateScript()
                        ),
                        // 期間指定で開始日と終了日がある場合
                        t -> stateEventMapper.findAllLatestByStateFromAndToDateTime(
                                stateEventTableNameFactory.createStateEventTableName(container.getStateType()),
                                container.getStateType(),
                                t.getStartDateTime().getValue(),
                                t.getEndDateTime().getValue(),
                                container.getStateScript()
                        )
                ))
                // 期間指定がない場合
                .orElseGet(() -> stateEventMapper.findAllLatestByState(
                        stateEventTableNameFactory.createStateEventTableName(container.getStateType()),
                        container.getStateType(),
                        container.getStateScript()
                ))
                .stream()
                .map(v -> v.toEntity(container.getStateType()))
                .collect(Collectors.toList());
    }

    public StateEventList findAllEvent(
            FindContainer container
    ) {
        return new StateEventList(
                stateEventMapper.findAllEvent(
                        stateEventTableNameFactory.createStateEventTableName(container.getStateType()),
                        container
                ).stream()
                        .map(v -> v.toEntity(container.getStateType()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void refreshLatest(
            FindContainer container
    ) {
        StateEventList list = findAllEvent(container);
        if(list.isEmpty()) {
            return;
        }

        // 最新にisLatestフラグを入れる
        stateEventMapper.setIsLatest(
                stateEventTableNameFactory.createStateEventTableName(container.getStateType()),
                list.getLatest().getEventId()
        );

        // isLatestフラグを落とす
        list.getNotLatestList().stream()
                .filter(StateEvent::isLatest)
                .forEach(e -> stateEventMapper.removeIsLatest(stateEventTableNameFactory.createStateEventTableName(container.getStateType()), e.getEventId()));

        validate(container);
    }

    public void delete(
            StateEventId stateEventId,
            StateType stateType
    ) {
        stateEventMapper.delete(
                stateEventTableNameFactory.createStateEventTableName(stateType),
                stateEventId
        );
    }

    public void validate(FindContainer container) {
        findAllEvent(container).validate().ifPresent(v -> {
            throw v;
        });
    }
}
