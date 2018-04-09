package jp.co.biglobe.isp.oss.statetransition.datasource;

import jp.co.biglobe.isp.oss.statetransition.domain.*;
import jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.FindContainer;
import jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.InsertStateEventContainer;
import jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.StateEventTableRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StateRepositoryImpl implements StateRepository {
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

        Optional<StateAndStateEventDateTime> latest = stateEventTableRepository
                .find(new FindContainer(id, stateType))
                .map(StateEvent::toStateAndStateEventDateTime);

        StateChangeLogic.validate(
                latest,
                new StateAndStateEventDateTime(stateType, state, stateEventDateTime)
        ).ifPresent(v -> { throw v; });

        insertEvent(id, stateType, state, stateEventDateTime, now);
    }

    void insertEvent(String id, StateType stateType, State state, StateEventDateTime stateEventDateTime, LocalDateTime now) {
        StateEventId eventId = stateEventIdFactory.createStateEventId(stateType);
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
        stateEventTableRepository.refreshLatest(new FindContainer(id, stateType));
    }

    @Override
    public Optional<StateEvent> findLatest(String id, StateType stateType) {
        return stateEventTableRepository.find(new FindContainer(id, stateType));
    }

    public void delete(StateEventId stateEventId, String id, StateType stateType, LocalDateTime now) {
        StateEvent latest = stateEventTableRepository.find(new FindContainer(id, stateType)).orElseThrow(() -> new RuntimeException("stateEventIdが見つからない: " + stateEventId.getValue()));
        if(!latest.getEventId().getValue().equals(stateEventId.getValue())) {
            throw new RuntimeException("最新のイベント以外削除できません");
        }

        // delete event
        stateEventTableRepository.delete(stateEventId, stateType);

        // update state table
        applyStateFromLatestEvent(id, stateType, now);
    }
}
