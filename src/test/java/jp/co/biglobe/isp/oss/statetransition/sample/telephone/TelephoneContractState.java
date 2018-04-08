package jp.co.biglobe.isp.oss.statetransition.sample.telephone;


import jp.co.biglobe.isp.oss.statetransition.domain.State;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
public enum TelephoneContractState implements State {
    contracted("あり"),
    end("なし");

    @Getter
    private final String label;

    @Override
    public String getValue() {
        return name();
    }

    public static TelephoneContractState create(String dbValue) {
        return Stream.of(values()).filter(v -> v.getValue().equals(dbValue)).findFirst().get();
    }
}
