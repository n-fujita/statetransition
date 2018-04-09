package jp.co.biglobe.isp.oss.statetransition.datasource.exception;

import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId;

import java.util.List;
import java.util.stream.Collectors;

public class ListLatestIsNotLastException extends RuntimeException {
    public ListLatestIsNotLastException(StateEventId stateEventId) {
        super("リストの最終要素がisLatestでない: " + stateEventId.getValue());
    }
}
