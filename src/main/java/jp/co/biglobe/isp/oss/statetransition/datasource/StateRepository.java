package jp.co.biglobe.isp.oss.statetransition.datasource;

import jp.co.biglobe.isp.oss.statetransition.domain.State;
import jp.co.biglobe.isp.oss.statetransition.domain.StateEventDateTime;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 状態遷移を管理する共通的なライブラリ
 *
 */
public interface StateRepository {
    void upsert(
            String id,
            StateType stateType,
            State state,
            StateEventDateTime stateEventDateTime,
            LocalDateTime now
    );

    Optional<StateEvent> findLatest(String id, StateType stateType);
}
