package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent;

import jp.co.biglobe.isp.oss.statetransition.datasource.StateEvent;
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId;
import jp.co.biglobe.isp.oss.statetransition.datasource.db.StateEventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StateEventTableRepositoryImpl implements StateEventTableRepository {
    private final StateEventMapper stateEventMapper;
    private final Optional<String> stateEventTable;

    public StateEventTableRepositoryImpl(
            @Autowired StateEventMapper stateEventMapper, @Value("${statetransition.table.state_event_table_name:#{null}}") Optional<String> stateEventTable
    ) {
        this.stateEventMapper = stateEventMapper;
        this.stateEventTable = stateEventTable;
    }

    private String getStateEventTableName() {
        return stateEventTable.orElseThrow(() -> new RuntimeException("StateEventTableがありません。state_event_table_nameを設定してください"));
    }

    public void insertStateEvent(
            InsertStateEventContainer insertStateEventContainer
    ) {
        stateEventMapper.insertStateEvent(
                getStateEventTableName(),
                insertStateEventContainer
        );
    }

    @Override
    public Optional<StateEvent> find(FindLatestContainer container) {
        return Optional.ofNullable(stateEventMapper.findLatest(getStateEventTableName(), container))
                .map(v -> v.toEntity(container.getStateType()));
    }

    public List<StateEvent> findAllEvent(
            FindLatestContainer container
    ) {
        return stateEventMapper.findAllEvent(
                getStateEventTableName(),
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
                getStateEventTableName(),
                latest.getEventId()
        );

        // isLatestフラグを落とす
        list.stream()
                .filter(StateEvent::isLatest)
                .forEach(e -> stateEventMapper.removeIsLatest(getStateEventTableName(), e.getEventId()));
    }

    public void delete(
            StateEventId stateEventId
    ) {
        stateEventMapper.delete(
                getStateEventTableName(),
                stateEventId
        );
    }
}
