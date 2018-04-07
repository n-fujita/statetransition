package jp.co.biglobe.isp.oss.statetransition.datasource.table.state;

import jp.co.biglobe.isp.oss.statetransition.datasource.StateEvent;
import jp.co.biglobe.isp.oss.statetransition.datasource.db.StateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StateTableRepositoryImpl implements StateTableRepository {
    private final StateMapper stateMapper;
    private final Optional<String> stateTableName;


    public StateTableRepositoryImpl(
            @Autowired StateMapper stateMapper,
            @Value("${statetransition.table.state_table_name:#{null}}") Optional<String> stateTableName
    ) {
        this.stateMapper = stateMapper;
        this.stateTableName = stateTableName;
    }

    private String getStateTableName() {
        return stateTableName.orElseThrow(() -> new RuntimeException("StateTableがありません。state_table_nameを設定してください"));
    }

    @Override
    public void upsertState(
            UpsertStateContainer container
    ) {
        boolean isExist = this.find(new FindLatestContainer(container.getId(), container.getStateType())).isPresent();
        if(isExist) {
            stateMapper.updateState(
                    getStateTableName(),
                    container
            );
        } else {
            stateMapper.insertState(
                    getStateTableName(),
                    container
            );
        }


    }

    @Override
    public Optional<StateEvent> find(
            FindLatestContainer container
    ) {
        return Optional.ofNullable(stateMapper.find(
                getStateTableName(),
                container
        )).map(v -> v.toEntity(container.getStateType()));

    }
}
