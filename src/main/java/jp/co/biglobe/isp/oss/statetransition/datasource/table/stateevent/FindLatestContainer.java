package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent;

import jp.co.biglobe.isp.oss.statetransition.domain.StateType;

public class FindLatestContainer {
    private final String id;
    private final StateType stateType;


    public FindLatestContainer(String id, StateType stateType) {
        this.id = id;
        this.stateType = stateType;
    }

    public StateType getStateType() {
        return stateType;
    }
}
