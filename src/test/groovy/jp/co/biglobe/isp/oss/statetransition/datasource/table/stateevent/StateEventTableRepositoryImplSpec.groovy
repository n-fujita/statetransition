package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent

import jp.co.biglobe.isp.oss.statetransition.datasource.DbSpecCommon
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEvent
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId
import jp.co.biglobe.isp.oss.statetransition.domain.StateEventDateTime
import jp.co.biglobe.isp.oss.statetransition.sample.SampleStateType
import jp.co.biglobe.isp.oss.statetransition.sample.contract.ContractState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration

import java.time.LocalDateTime

@SpringBootTest
@TestConfiguration
class StateEventTableRepositoryImplSpec extends DbSpecCommon {
    @Autowired
    StateEventTableRepository sut

    def "insertAndFind"() {
        when:
        def container = new InsertStateEventContainer(
                new StateEventId("EVENT001"),
                "ID001",
                SampleStateType.contract,
                ContractState.ordered,
                new StateEventDateTime(LocalDateTime.of(2017, 12, 30, 0, 0)),
                LocalDateTime.of(2018, 1, 1, 0, 0)
        )
        sut.insertStateEvent(container)

        // 挿入したものが取れること
        def act = sut.findAllEvent(new FindLatestContainer("ID001", SampleStateType.contract))

        then:
        act.size() == 1
        act == [
                new StateEvent(
                    new StateEventId("EVENT001"),
                    "ID001",
                    ContractState.ordered,
                    SampleStateType.contract,
                    new StateEventDateTime(LocalDateTime.of(2017, 12, 30, 0, 0)),
                    LocalDateTime.of(2018, 1, 1, 0, 0),
                    false
                )
        ]
    }

    def "insert2AndFind"() {
        when:
        def container = new InsertStateEventContainer(
                new StateEventId("EVENT001"),
                "ID001",
                SampleStateType.contract,
                ContractState.ordered,
                new StateEventDateTime(LocalDateTime.of(2017, 12, 30, 0, 0)),
                LocalDateTime.of(2018, 1, 1, 0, 0)
        )
        def container2 = new InsertStateEventContainer(
                new StateEventId("EVENT002"),
                "ID001",
                SampleStateType.contract,
                ContractState.contracted,
                new StateEventDateTime(LocalDateTime.of(2018, 2, 1, 0, 0)),
                LocalDateTime.of(2018, 3, 1, 0, 0)
        )
        sut.insertStateEvent(container)
        sut.insertStateEvent(container2)

        // 挿入したものが取れること
        def act = sut.findAllEvent(new FindLatestContainer("ID001", SampleStateType.contract))

        then:
        act == [
                new StateEvent(
                        new StateEventId("EVENT001"),
                        "ID001",
                        ContractState.ordered,
                        SampleStateType.contract,
                        new StateEventDateTime(LocalDateTime.of(2017, 12, 30, 0, 0)),
                        LocalDateTime.of(2018, 1, 1, 0, 0),
                        false
                ),
                new StateEvent(
                        new StateEventId("EVENT002"),
                        "ID001",
                        ContractState.contracted,
                        SampleStateType.contract,
                        new StateEventDateTime(LocalDateTime.of(2018, 2, 1, 0, 0)),
                        LocalDateTime.of(2018, 3, 1, 0, 0),
                        false
                )
        ]
    }
}
