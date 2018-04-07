package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent;

import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId;
import jp.co.biglobe.isp.oss.statetransition.domain.State;
import jp.co.biglobe.isp.oss.statetransition.domain.StateEventDateTime;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;

import java.time.LocalDateTime;

public class InsertStateEventContainer {
    private final StateEventId eventId;
    private final String id;
    private final StateType stateType;
    private final State state;
    private final StateEventDateTime stateEventDateTime;
    private final LocalDateTime now;

    public InsertStateEventContainer(StateEventId eventId, String id, StateType stateType, State state, StateEventDateTime stateEventDateTime, LocalDateTime now) {
        this.eventId = eventId;
        this.id = id;
        this.stateType = stateType;
        this.state = state;
        this.stateEventDateTime = stateEventDateTime;
        this.now = now;
    }
}
