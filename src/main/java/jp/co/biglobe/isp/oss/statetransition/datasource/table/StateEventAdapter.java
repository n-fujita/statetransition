package jp.co.biglobe.isp.oss.statetransition.datasource.table;

import jp.co.biglobe.isp.oss.statetransition.datasource.StateEvent;
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId;
import jp.co.biglobe.isp.oss.statetransition.domain.StateEventDateTime;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;

import java.time.LocalDateTime;

public class StateEventAdapter {
    public String id;
    public String state_type;
    public String state;
    public String state_event_id;
    public LocalDateTime state_event_date_time;
    public LocalDateTime event_date_time;

    public StateEvent toEntity(StateType stateType) {
        if(!stateType.getValue().equals(state_type)) {
            throw new IllegalArgumentException("stateTypeが合わない");
        }
        return new StateEvent(
                new StateEventId(state_event_id),
                id,
                stateType.convert(state),
                stateType,
                new StateEventDateTime(state_event_date_time),
                event_date_time
        );
    }
}
