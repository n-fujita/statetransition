package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent

import jp.co.biglobe.isp.oss.statetransition.datasource.DbSpecCommon
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventList
import jp.co.biglobe.isp.oss.statetransition.datasource.exception.ListLatestIsNotLastException
import jp.co.biglobe.isp.oss.statetransition.sample.SampleStateType
import jp.co.biglobe.isp.oss.statetransition.sample.contract.ContractState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration

import static jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.InsertStateEventContainerBuilder.契約
import static jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.InsertStateEventContainerBuilder.申込
import static jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.InsertStateEventContainerBuilder.解約

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
        def act = sut.findAllEvent(new FindContainer("ID001", SampleStateType.contract))

        then:
        act == new StateEventList([申込Event])
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
        def act = sut.findAllEvent(new FindContainer("ID001", SampleStateType.contract))

        then:
        act == new StateEventList([
                申込Event,
                契約Event
        ])
    }

    def "不整合チェック"() {
        setup:
        def 申込Container = 申込("EVENT001").build()
        def 契約Container = 契約("EVENT002").stateEventDateTime(2018, 2, 1).eventDateTime(2018, 3, 1).build()

        when:
        sut.insertStateEvent(契約Container)
        sut.insertStateEvent(申込Container)
        sut.validate(new FindContainer("ID001", SampleStateType.contract))

        then:
        thrown(ListLatestIsNotLastException)

    }

    def "refreshLatest"() {
        setup:
        def find = new FindContainer("ID001", SampleStateType.contract)

        // 初期状態
        def 申込Container = 申込().build()
        def 契約Container = 契約().stateEventDateTime(2018, 2, 1).eventDateTime(2018, 3, 1).build()
        sut.insertStateEvent(契約Container)
        sut.insertStateEvent(申込Container)
        sut.refreshLatest(new FindContainer("ID001", SampleStateType.contract))
        sut.validate(find)
        assert sut.find(find).get().state == ContractState.contracted

        // 解約のイベントを入れる
        def 解約Container = 解約().stateEventDateTime(2018, 4, 1).eventDateTime(2018, 5, 1).build()
        sut.insertStateEvent(解約Container)

        when:
        // リフレッシュする
        sut.refreshLatest(new FindContainer("ID001", SampleStateType.contract))

        then:
        notThrown(RuntimeException)
        sut.find(find).get().state == ContractState.end // 解約になっていること

    }
}
