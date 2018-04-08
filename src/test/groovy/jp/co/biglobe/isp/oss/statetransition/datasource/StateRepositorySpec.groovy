package jp.co.biglobe.isp.oss.statetransition.datasource

import jp.co.biglobe.isp.oss.statetransition.domain.exception.DenyChangeStateException
import jp.co.biglobe.isp.oss.statetransition.domain.exception.DenyChangeToPastException
import jp.co.biglobe.isp.oss.statetransition.domain.exception.DenyEntryStateException
import jp.co.biglobe.isp.oss.statetransition.domain.StateEventDateTime
import jp.co.biglobe.isp.oss.statetransition.sample.SampleStateType
import jp.co.biglobe.isp.oss.statetransition.sample.contract.ContractState
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Unroll

import java.time.LocalDateTime

@Unroll
class StateRepositorySpec extends DbSpecCommon {
    @Autowired
    StateRepository sut

    def "エントリーから状態遷移できること"() {
        when:
        // entry
        sut.upsert(
                "ID001",
                SampleStateType.contract,
                ContractState.ordered,
                new StateEventDateTime(LocalDateTime.of(2018, 1, 1, 0, 0)),
                LocalDateTime.of(2018, 2, 1, 0, 0)
        )
        StateEvent act = sut.findLatest("ID001", SampleStateType.contract).get()

        // next
        sut.upsert(
                "ID001",
                SampleStateType.contract,
                ContractState.contracted,
                new StateEventDateTime(LocalDateTime.of(2018, 3, 1, 0, 0)),
                LocalDateTime.of(2018, 4, 1, 0, 0)
        )
        StateEvent act2 = sut.findLatest("ID001", SampleStateType.contract).get()

        then:
        act == new StateEvent(
                new StateEventId("STT000000001CONTRACT"),
                "ID001",
                ContractState.ordered,
                SampleStateType.contract,
                new StateEventDateTime(LocalDateTime.of(2018, 1, 1, 0, 0)),
                LocalDateTime.of(2018, 2, 1, 0, 0),
                true
        )

        act2 == new StateEvent(
                new StateEventId("STT000000002CONTRACT"),
                "ID001",
                ContractState.contracted,
                SampleStateType.contract,
                new StateEventDateTime(LocalDateTime.of(2018, 3, 1, 0, 0)),
                LocalDateTime.of(2018, 4, 1, 0, 0),
                true
        )
    }

    def "許されていない状態へエントリーできないこと"() {
        when:
        // entry
        sut.upsert(
                "ID001",
                SampleStateType.contract,
                ContractState.end,
                new StateEventDateTime(LocalDateTime.of(2018, 1, 1, 0, 0)),
                LocalDateTime.of(2018, 2, 1, 0, 0)
        )

        then:
        thrown(DenyEntryStateException)
    }

    def "許可されていない状態遷移ができないこと"() {
        when:
        // entry
        sut.upsert(
                "ID001",
                SampleStateType.contract,
                ContractState.ordered,
                new StateEventDateTime(LocalDateTime.of(2018, 1, 1, 0, 0)),
                LocalDateTime.of(2018, 2, 1, 0, 0)
        )
        StateEvent act = sut.findLatest("ID001", SampleStateType.contract).get()

        // 申込から解約への状態遷移は許可されていない
        sut.upsert(
                "ID001",
                SampleStateType.contract,
                ContractState.end,
                new StateEventDateTime(LocalDateTime.of(2018, 3, 1, 0, 0)),
                LocalDateTime.of(2018, 4, 1, 0, 0)
        )

        then:
        // エントリーは正常
        act == new StateEvent(
                new StateEventId("STT000000001CONTRACT"),
                "ID001",
                ContractState.ordered,
                SampleStateType.contract,
                new StateEventDateTime(LocalDateTime.of(2018, 1, 1, 0, 0)),
                LocalDateTime.of(2018, 2, 1, 0, 0),
                true
        )
        // 状態遷移で異常発生
        thrown(DenyChangeStateException)
    }

    def "過去への状態遷移ができないこと"() {
        setup:
        LocalDateTime firstEventDate = LocalDateTime.of(2018, 1, 1, 0, 0)
        LocalDateTime secondEventDate = LocalDateTime.of(2017, 12, 31, 23, 59, 59)

        // entry
        sut.upsert(
                "ID001",
                SampleStateType.contract,
                ContractState.ordered,
                new StateEventDateTime(firstEventDate),
                LocalDateTime.of(2018, 2, 1, 0, 0)
        )

        when:
        sut.upsert(
                "ID001",
                SampleStateType.contract,
                ContractState.contracted,
                new StateEventDateTime(secondEventDate),
                LocalDateTime.of(2018, 4, 1, 0, 0)
        )
        StateEvent act2 = sut.findLatest("ID001", SampleStateType.contract).get()

        then:
        thrown(DenyChangeToPastException)
    }

    def "同時以降の状態遷移ができること"() {
        setup:
        LocalDateTime firstEventDate = LocalDateTime.of(2018, 1, 1, 0, 0)
        LocalDateTime secondEventDate = LocalDateTime.of(2018, 1, 1, 0, 0)

        // entry
        sut.upsert(
                "ID001",
                SampleStateType.contract,
                ContractState.ordered,
                new StateEventDateTime(firstEventDate),
                LocalDateTime.of(2018, 2, 1, 0, 0)
        )

        when:
        sut.upsert(
                "ID001",
                SampleStateType.contract,
                ContractState.contracted,
                new StateEventDateTime(secondEventDate),
                LocalDateTime.of(2018, 4, 1, 0, 0)
        )
        StateEvent act2 = sut.findLatest("ID001", SampleStateType.contract).get()

        then:
        notThrown(RuntimeException)
        act2 == new StateEvent(
                new StateEventId("STT000000002CONTRACT"),
                "ID001",
                ContractState.contracted,
                SampleStateType.contract,
                new StateEventDateTime(secondEventDate),
                LocalDateTime.of(2018, 4, 1, 0, 0),
                true
        )
    }
}
