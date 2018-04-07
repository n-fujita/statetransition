package jp.co.biglobe.isp.oss.statetransition.datasource

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

    def "test"() {
        when:
        sut.upsert(
                "ID001",
                SampleStateType.contract,
                ContractState.ordered,
                new StateEventDateTime(LocalDateTime.of(2018, 1, 1, 0, 0)),
                LocalDateTime.of(2018, 2, 1, 0, 0)
        )

        StateEvent act = sut.findLatest("ID001", SampleStateType.contract).get()

        then:
        act.eventId.value == "STT000000001CONTRACT" // StateEventIdFactoryの実装に依存するため、このフォーマットは仕様ではありません
        act.id == "ID001"

    }
}
