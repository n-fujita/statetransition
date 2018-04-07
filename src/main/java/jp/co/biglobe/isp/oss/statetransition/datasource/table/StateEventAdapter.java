package jp.co.biglobe.isp.oss.statetransition.datasource.table;

import jp.co.biglobe.isp.oss.statetransition.datasource.StateEvent;
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId;
import jp.co.biglobe.isp.oss.statetransition.domain.StateEventDateTime;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;

import java.time.LocalDateTime;
import java.util.Optional;


public class StateEventAdapter {
    public String id;
    public String state_type;
    public String state;
    public String state_event_id;
    public LocalDateTime state_event_date_time;
    public LocalDateTime event_date_time;
    public Integer is_latest;

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
                event_date_time,
                isLatest()
        );
    }

    public boolean isLatest() {
        return Optional.ofNullable(is_latest).map(v -> v > 0).orElse(false);
    }
}
