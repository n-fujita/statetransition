package jp.co.biglobe.isp.oss.statetransition.datasource.table.state;

import jp.co.biglobe.isp.oss.statetransition.datasource.StateEvent;

import java.util.Optional;

public interface StateTableRepository {
    void upsertState(
            UpsertStateContainer container
    );

    Optional<StateEvent> find(
            FindLatestContainer container
    );
}
