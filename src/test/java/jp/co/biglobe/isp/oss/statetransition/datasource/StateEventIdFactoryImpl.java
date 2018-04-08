package jp.co.biglobe.isp.oss.statetransition.datasource;

import jp.co.biglobe.isp.oss.statetransition.datasource.db.EventIdCreateMapper;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StateEventIdFactoryImpl implements StateEventIdFactory {
    @Autowired
    EventIdCreateMapper eventIdCreateMapper;

    @Override
    public StateEventId createStateEventId(StateType stateType) {
        eventIdCreateMapper.increment();
        return new StateEventId(String.format("STT%09d%s", eventIdCreateMapper.getMaxEventId(), stateType.getValue().toUpperCase()));
//        return new StateEventId(String.format("STT%09d%s", eventIdCreateMapper.getMaxEventId(), stateType.getValue().toUpperCase()));
    }
}
