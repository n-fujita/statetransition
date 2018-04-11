package jp.co.biglobe.isp.oss.statetransition.datasource;

import jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.*;
import jp.co.biglobe.isp.oss.statetransition.domain.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StateRepositoryImpl implements StateRepository {
    private final StateEventTableRepository stateEventTableRepository;
    private final StateEventIdFactory stateEventIdFactory;

    @Override
    public void upsert(InsertStateEvent insertStateEvent) {

        Optional<StateAndStateEventDateTime> latest = stateEventTableRepository
                .findAllLatestEvent(new FindContainer(insertStateEvent.getId(), insertStateEvent.getStateType()))
                .map(StateEvent::toStateAndStateEventDateTime);

        StateChangeLogic.validate(
                latest,
                new StateAndStateEventDateTime(insertStateEvent.getStateType(), insertStateEvent.getState(), insertStateEvent.getStateEventDateTime())
        ).ifPresent(v -> { throw v; });

        insertEvent(insertStateEvent);
    }

    void insertEvent(InsertStateEvent insertStateEvent) {
        StateEventId eventId = stateEventIdFactory.createStateEventId(insertStateEvent.getStateType());
        // insert to event table
        stateEventTableRepository.insertStateEvent(
                new InsertStateEventContainer(
                        eventId,
                        insertStateEvent
                )
        );

        stateEventTableRepository.refreshLatest(new FindContainer(insertStateEvent.getId(), insertStateEvent.getStateType()));
    }

    @Override
    public Optional<StateEvent> findLatest(String id, StateType stateType) {
        return stateEventTableRepository.findAllLatestEvent(new FindContainer(id, stateType));
    }

    @Override
    public List<StateEvent> findAllLatest(StateCustomSelectorContainer stateCustomSelectorContainer) {
        return stateEventTableRepository.findAllLatestEvent(stateCustomSelectorContainer);
    }

    public void delete(StateEventId stateEventId, String id, StateType stateType, LocalDateTime now) {
        StateEvent latest = stateEventTableRepository.findAllLatestEvent(new FindContainer(id, stateType)).orElseThrow(() -> new RuntimeException("stateEventIdが見つからない: " + stateEventId.getValue()));
        if(!latest.getEventId().getValue().equals(stateEventId.getValue())) {
            throw new RuntimeException("最新のイベント以外削除できません");
        }

        // delete event
        stateEventTableRepository.delete(stateEventId, stateType);

        // update state table
        stateEventTableRepository.refreshLatest(new FindContainer(id, stateType));
    }
}
