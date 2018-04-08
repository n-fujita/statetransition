package jp.co.biglobe.isp.oss.statetransition.datasource;

import jp.co.biglobe.isp.oss.statetransition.domain.StateType;

public interface StateEventTableNameFactory {
    String createStateEventTableName(StateType stateType);
}
