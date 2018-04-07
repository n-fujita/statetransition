package jp.co.biglobe.isp.oss.statetransition.datasource.table.state;

import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId;
import jp.co.biglobe.isp.oss.statetransition.domain.State;
import jp.co.biglobe.isp.oss.statetransition.domain.StateEventDateTime;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;

import java.time.LocalDateTime;

public class UpsertStateContainer {
    private final StateEventId eventId;
    private final String id;
    private final StateType stateType;
    private final State state;
    private final StateEventDateTime stateEventDateTime;
    private final LocalDateTime now;

    public UpsertStateContainer(StateEventId eventId, String id, StateType stateType, State state, StateEventDateTime stateEventDateTime, LocalDateTime now) {
        this.eventId = eventId;
        this.id = id;
        this.stateType = stateType;
        this.state = state;
        this.stateEventDateTime = stateEventDateTime;
        this.now = now;
    }

    public String getId() {
        return id;
    }

    public StateType getStateType() {
        return stateType;
    }
}
