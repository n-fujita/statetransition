package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent;

import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId;
import jp.co.biglobe.isp.oss.statetransition.domain.State;
import jp.co.biglobe.isp.oss.statetransition.domain.StateEventDateTime;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
public class InsertStateEventContainer {
    private final StateEventId eventId;
    @Getter
    private final String id;
    @Getter
    private final StateType stateType;
    private final State state;
    private final StateEventDateTime stateEventDateTime;
    private final LocalDateTime now;

    public InsertStateEventContainer(
            StateEventId eventId,
            InsertStateEvent insertStateEvent
    ) {
        this.eventId = eventId;
        this.id = insertStateEvent.getId();
        this.stateType = insertStateEvent.getStateType();
        this.state = insertStateEvent.getState();
        this.stateEventDateTime = insertStateEvent.getStateEventDateTime();
        this.now = insertStateEvent.getNow();


    }
}
