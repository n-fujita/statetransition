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
    private final String id;
    @Getter
    private final StateType stateType;
    private final State state;
    private final StateEventDateTime stateEventDateTime;
    private final LocalDateTime now;
}
