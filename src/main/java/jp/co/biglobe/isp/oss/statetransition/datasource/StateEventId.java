package jp.co.biglobe.isp.oss.statetransition.datasource;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
public class StateEventId {
    @Getter
    private final String value;
}
