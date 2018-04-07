package jp.co.biglobe.isp.oss.statetransition.datasource;

import jp.co.biglobe.isp.oss.statetransition.datasource.table.state.StateTableRepository;
import jp.co.biglobe.isp.oss.statetransition.datasource.table.state.UpsertStateContainer;
import jp.co.biglobe.isp.oss.statetransition.datasource.table.state.FindLatestContainer;
import jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.InsertStateEventContainer;
import jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.StateEventIdFactory;
import jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.StateEventTableRepository;
import jp.co.biglobe.isp.oss.statetransition.domain.State;
import jp.co.biglobe.isp.oss.statetransition.domain.StateEventDateTime;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StateRepositoryImpl implements StateRepository {
    private final StateTableRepository stateTableRepository;
    private final StateEventTableRepository stateEventTableRepository;
    private final StateEventIdFactory stateEventIdFactory;

    @Override
    public void upsert(
            String id,
            StateType stateType,
            State state,
            StateEventDateTime stateEventDateTime,
            LocalDateTime now
    ) {
        Optional<String> currentDbValue = Optional.empty();// from db

        if(currentDbValue.isPresent()) {// transition
            State current = stateType.convert(currentDbValue.get());
            if(!stateType.isValidTransition(current, state)) {
                throw new RuntimeException(String.format("undefined transition: %s -> %s", current, state));
            }
        } else {// entry
            if(!stateType.isEntry(state)) {
                throw new RuntimeException(String.format("undefined entry state: %s", state));
            }
        }

        insertEvent(id, stateType, state, stateEventDateTime, now);
    }

    void insertEvent(String id, StateType stateType, State state, StateEventDateTime stateEventDateTime, LocalDateTime now) {
        StateEventId eventId = stateEventIdFactory.createId(stateType);
        // insert to event table
        stateEventTableRepository.insertStateEvent(
                new InsertStateEventContainer(
                        eventId,
                        id,
                        stateType,
                        state,
                        stateEventDateTime,
                        now
                )
        );

        applyStateFromLatestEvent(id, stateType, now);
    }

    /**
     * イベントの最新の状態でstateを作成する
     * @param id
     * @param stateType
     * @param now
     */
    void applyStateFromLatestEvent(String id, StateType stateType, LocalDateTime now) {
        List<StateEvent> list = stateEventTableRepository.findAllEvent(new jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.FindLatestContainer(id, stateType));
        if(list.size() == 0) {
            return;
        }

        StateEvent latest = list.get(list.size() - 1);
        stateTableRepository.upsertState(
                new UpsertStateContainer(
                        latest.getEventId(),
                        latest.getId(),
                        latest.getStateType(),
                        latest.getState(),
                        latest.getStateEventDateTime(),
                        now
                )
        );
    }

    @Override
    public Optional<StateEvent> findLatest(String id, StateType stateType) {
        return stateTableRepository.find(
                new FindLatestContainer(id, stateType)
        );
    }

    public void delete(StateEventId stateEventId, String id, StateType stateType, LocalDateTime now) {
        StateEvent latestState = stateTableRepository.find(new FindLatestContainer(id, stateType)).get();
        if(!latestState.getEventId().getValue().equals(stateEventId.getValue())) {
            throw new RuntimeException("最新のイベント以外削除できません");
        }

        // delete event
        stateEventTableRepository.delete(stateEventId);

        // update state table
        applyStateFromLatestEvent(id, stateType, now);
    }
}
