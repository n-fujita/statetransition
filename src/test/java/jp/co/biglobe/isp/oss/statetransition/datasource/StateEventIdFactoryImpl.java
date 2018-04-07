package jp.co.biglobe.isp.oss.statetransition.datasource;

import jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.StateEventIdFactory;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;
import org.springframework.stereotype.Component;

@Component
public class StateEventIdFactoryImpl implements StateEventIdFactory {
    public int i = 1;

    @Override
    public StateEventId createId(StateType stateType) {
        return new StateEventId(String.format("STT%09d%s", i++, stateType.getValue().toUpperCase()));
    }
}
