package jp.co.biglobe.isp.oss.statetransition.sample.contract;

import jp.co.biglobe.isp.oss.statetransition.domain.State;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
public enum ContractState implements State {
    ordered("申込中"),
    orderedCancel("申込キャンセル"),
    contracted("契約中"),
    end("解約");

    @Getter
    private final String label;

    @Override
    public String getValue() {
        return name();
    }

    public static ContractState create(String dbValue) {
        return Stream.of(values()).filter(v -> v.getValue().equals(dbValue)).findFirst().get();
    }
}
