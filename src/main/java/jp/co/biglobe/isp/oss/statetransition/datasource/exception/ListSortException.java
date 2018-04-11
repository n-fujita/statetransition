package jp.co.biglobe.isp.oss.statetransition.datasource.exception;

import jp.co.biglobe.isp.oss.statetransition.domain.StateType;

public class ListSortException extends RuntimeException {
    public ListSortException(StateType stateType) {
        super("リストがイベント日時の昇順でない: " + stateType.getValue());
    }
}
