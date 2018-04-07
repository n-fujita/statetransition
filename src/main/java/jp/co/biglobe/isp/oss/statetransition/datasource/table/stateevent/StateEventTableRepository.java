package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent;

import jp.co.biglobe.isp.oss.statetransition.datasource.StateEvent;
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId;

import java.util.List;

public interface StateEventTableRepository {
    void insertStateEvent(
            InsertStateEventContainer insertStateEventContainer
    );

    List<StateEvent> findAllEvent(
            FindLatestContainer container
    );

    void delete(StateEventId stateEventId);
}
