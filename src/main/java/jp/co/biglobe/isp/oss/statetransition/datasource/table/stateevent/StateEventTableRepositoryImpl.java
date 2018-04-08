package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent;

import jp.co.biglobe.isp.oss.statetransition.datasource.StateEvent;
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId;
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventTableNameFactory;
import jp.co.biglobe.isp.oss.statetransition.datasource.db.StateEventMapper;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    public Optional<StateEvent> find(FindLatestContainer container) {
        return Optional.ofNullable(stateEventMapper.findLatest(stateEventTableNameFactory.createStateEventTableName(container.getStateType()), container))
                .map(v -> v.toEntity(container.getStateType()));
    }

    public List<StateEvent> findAllEvent(
            FindLatestContainer container
    ) {
        return stateEventMapper.findAllEvent(
                stateEventTableNameFactory.createStateEventTableName(container.getStateType()),
                container
        ).stream()
                .map(v -> v.toEntity(container.getStateType()))
                .collect(Collectors.toList());
    }

    @Override
    public void refreshLatest(
            FindLatestContainer container
    ) {
        List<StateEvent> list = findAllEvent(container);
        if(list.size() == 0) {
            return;
        }
        StateEvent latest = list.remove(list.size() - 1); // 最後の要素を取得、listから最後の要素を削除

        // 最新にisLatestフラグを入れる
        stateEventMapper.setIsLatest(
                stateEventTableNameFactory.createStateEventTableName(container.getStateType()),
                latest.getEventId()
        );

        // isLatestフラグを落とす
        list.stream()
                .filter(StateEvent::isLatest)
                .forEach(e -> stateEventMapper.removeIsLatest(stateEventTableNameFactory.createStateEventTableName(container.getStateType()), e.getEventId()));
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
}
