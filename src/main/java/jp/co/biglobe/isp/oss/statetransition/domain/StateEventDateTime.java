package jp.co.biglobe.isp.oss.statetransition.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@EqualsAndHashCode
public class StateEventDateTime {
    @Getter
    private final LocalDateTime value;

    public StateEventDateTime(Timestamp value) {
        this.value = value.toLocalDateTime();
    }

    public boolean isAfter(StateEventDateTime other) {
        return value.isAfter(other.value);
    }
}
