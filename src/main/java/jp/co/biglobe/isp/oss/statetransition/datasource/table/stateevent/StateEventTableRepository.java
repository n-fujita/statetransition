package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent;

import jp.co.biglobe.isp.oss.statetransition.datasource.StateEvent;
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId;

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
    List<StateEvent> findAllEvent(
            FindLatestContainer container
    );

    void delete(StateEventId stateEventId);

    void refreshLatest(FindLatestContainer container);
}
