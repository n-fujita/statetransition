package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent;

import jp.co.biglobe.isp.oss.statetransition.datasource.StateEvent;
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId;
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventList;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;

import java.util.List;
import java.util.Optional;

public interface StateEventTableRepository {
    void insertStateEvent(
            InsertStateEventContainer insertStateEventContainer
    );

    Optional<StateEvent> find(FindLatestContainer container);

    /**
     * すべてのイベントをIDの昇順で取得する
     * @param container
     * @return
     */
    StateEventList findAllEvent(
            FindLatestContainer container
    );

    void delete(StateEventId stateEventId, StateType stateType);

    void refreshLatest(FindLatestContainer container);

    void validate(FindLatestContainer container);
}
