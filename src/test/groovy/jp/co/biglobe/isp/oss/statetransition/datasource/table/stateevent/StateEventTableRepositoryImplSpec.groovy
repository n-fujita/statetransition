package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent

import jp.co.biglobe.isp.oss.statetransition.datasource.DbSpecCommon
import jp.co.biglobe.isp.oss.statetransition.sample.SampleStateType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration

import static jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.InsertStateEventContainerBuilder.契約
import static jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.InsertStateEventContainerBuilder.申込

@SpringBootTest
@TestConfiguration
class StateEventTableRepositoryImplSpec extends DbSpecCommon {
    @Autowired
    StateEventTableRepository sut

    def "挿入したイベントが取れること"() {
        setup:
        def 申込Container = 申込("EVENT001").build()
        def 申込Event = 申込("EVENT001").buildToStateEvent(false)

        when:
        sut.insertStateEvent(申込Container)

        // 挿入したものが取れること
        def act = sut.findAllEvent(new FindLatestContainer("ID001", SampleStateType.contract))

        then:
        act == [申込Event]
    }

    def "複数挿入した場合、StateEventId順に取得できること"() {
        setup:
        def 申込Container = 申込("EVENT001").build()
        def 契約Container = 契約("EVENT002").stateEventDateTime(2018, 2, 1).eventDateTime(2018, 3, 1).build()

        def 申込Event = 申込("EVENT001").buildToStateEvent(false)
        def 契約Event = 契約("EVENT002").stateEventDateTime(2018, 2, 1).eventDateTime(2018, 3, 1).buildToStateEvent(false)

        when:
        sut.insertStateEvent(契約Container)
        sut.insertStateEvent(申込Container)


        // 挿入したものが取れること
        def act = sut.findAllEvent(new FindLatestContainer("ID001", SampleStateType.contract))

        then:
        act == [
                申込Event,
                契約Event
        ]
    }
}
