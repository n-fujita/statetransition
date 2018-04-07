package jp.co.biglobe.isp.oss.statetransition.datasource;


import jp.co.biglobe.isp.oss.statetransition.domain.State;
import jp.co.biglobe.isp.oss.statetransition.domain.StateEventDateTime;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
public class StateEvent {
    @Getter
    private final StateEventId eventId;
    @Getter
    private final String id;

    private final State state;
    public <T extends State> T getState() {
        return (T)state;
    }

    @Getter
    private final StateType stateType;
    @Getter
    private final StateEventDateTime stateEventDateTime;
    @Getter
    private final LocalDateTime eventDateTime;



}
