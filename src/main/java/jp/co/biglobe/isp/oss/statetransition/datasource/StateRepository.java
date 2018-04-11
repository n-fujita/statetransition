package jp.co.biglobe.isp.oss.statetransition.datasource;

import jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.InsertStateEvent;
import jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.StateCustomSelectorContainer;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;

import java.util.List;
import java.util.Optional;

/**
 * 状態遷移を管理する共通的なライブラリ
 *
 */
public interface StateRepository {
    void upsert(InsertStateEvent insertStateEvent);
    Optional<StateEvent> findLatest(String id, StateType stateType);
    List<StateEvent> findAllLatest(StateCustomSelectorContainer stateCustomSelectorContainer);
}
