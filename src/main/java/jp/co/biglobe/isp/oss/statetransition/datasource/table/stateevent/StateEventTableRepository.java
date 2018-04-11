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

    Optional<StateEvent> find(FindContainer container);

    /**
     * すべてのイベントをIDの昇順で取得する
     * @param container
     * @return
     */
    StateEventList findAllEvent(
            FindContainer container
    );

    /**
     * 状態に一致するイベントを取得する
     * @param container
     * @return
     */
    List<StateEvent> find(
            FindByStateContainer container
    );

    void delete(StateEventId stateEventId, StateType stateType);

    void refreshLatest(FindContainer container);

    void validate(FindContainer container);
}
