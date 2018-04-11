package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent;

import com.naosim.ddd.term.LocalDateTimeVO;
import com.naosim.ddd.term.Term;
import jp.co.biglobe.isp.oss.statetransition.domain.State;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 検索条件: 状態, 期間
 */
public class FindByStateContainer {
    private final StateType stateType;
    @Getter
    private final Optional<Term<LocalDateTimeVO, LocalDateTimeVO>> termOptional;
    private final List<State> targetStateList;


    public FindByStateContainer(
            StateType stateType,
            Optional<Term<LocalDateTimeVO, LocalDateTimeVO>> termOptional,
            State... targetStates
    ) {
        this.stateType = stateType;
        this.termOptional = termOptional;
        this.targetStateList = Stream.of(targetStates).collect(Collectors.toList());
    }

    public FindByStateContainer(
            StateType stateType,
            State... targetStates
    ) {
        this(stateType, Optional.empty(), targetStates);
    }

    public StateType getStateType() {
        return stateType;
    }

    public String getStateScript() {
        if(targetStateList.isEmpty()) {
            return "";
        }
        return " AND (" + targetStateList.stream().map(v -> String.format("state = '%s'", v.getValue())).collect(Collectors.joining(" OR ")) + ")";
    }

    public boolean notHaveTerm() {
        return !termOptional.isPresent();
    }

    public boolean notHaveEndDateTime() {
        return !termOptional.get().hasEndDateTime();
    }

    public LocalDateTime getFromDateTime() {
        return termOptional.get().getStartDateTime().getValue();
    }

    public LocalDateTime getToDateTime() {
        return termOptional.get().getEndDateTimeOptional().get().getValue();
    }
}
