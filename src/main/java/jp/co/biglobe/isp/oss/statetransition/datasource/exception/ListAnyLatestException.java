package jp.co.biglobe.isp.oss.statetransition.datasource.exception;

import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;

import java.util.List;
import java.util.stream.Collectors;

public class ListAnyLatestException extends RuntimeException {
    public ListAnyLatestException(List<StateEventId> stateEventIdList) {
        super("リストにisLatestが複数ある: " + stateEventIdList.stream().map(v -> v.getValue()).collect(Collectors.joining(", ")));
    }
}
