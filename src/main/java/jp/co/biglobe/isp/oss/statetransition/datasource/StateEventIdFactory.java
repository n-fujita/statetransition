package jp.co.biglobe.isp.oss.statetransition.datasource;

import jp.co.biglobe.isp.oss.statetransition.domain.StateType;

/**
 * StateEventIdを採番する
 *
 * これを継承したコンポーネントを作成してください
 * IDは昇順ソートした時に生成順になるように採番してください
 * 例: SEI0000000001
 *
 */
public interface StateEventIdFactory {
    StateEventId createId(StateType stateType);
}
