package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent

import com.naosim.ddd.term.LocalDateTimeVOImpl
import com.naosim.ddd.term.Term
import jp.co.biglobe.isp.oss.statetransition.datasource.DbSpecCommon
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventList
import jp.co.biglobe.isp.oss.statetransition.datasource.exception.ListLatestIsNotLastException
import jp.co.biglobe.isp.oss.statetransition.domain.State
import jp.co.biglobe.isp.oss.statetransition.sample.SampleStateType
import jp.co.biglobe.isp.oss.statetransition.sample.contract.ContractState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import spock.lang.Unroll

import java.time.LocalDateTime

import static jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.InsertStateEventContainerBuilder.契約
import static jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.InsertStateEventContainerBuilder.申込
import static jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.InsertStateEventContainerBuilder.解約
import static jp.co.biglobe.isp.oss.statetransition.sample.contract.ContractState.contracted
import static jp.co.biglobe.isp.oss.statetransition.sample.contract.ContractState.ordered

@Unroll
@SpringBootTest
@TestConfiguration
class StateEventTableRepositoryImplSpec extends DbSpecCommon {
    @Autowired
    StateEventTableRepository sut

    def "挿入したイベントが取れること"() {
        setup:
        def 申込Container = 申込().build()
        def 申込Event = 申込().buildToStateEvent(false)

        when:
        sut.insertStateEvent(申込Container)

        // 挿入したものが取れること
        def act = sut.findAllEvent(new FindContainer("ID001", SampleStateType.contract))

        then:
        act == new StateEventList([申込Event])
    }

    def "複数挿入した場合、StateEventId順に取得できること"() {
        setup:
        def 申込Container = 申込().build()
        def 契約Container = 契約().stateEventDateTime(2018, 2, 1).eventDateTime(2018, 3, 1).build()

        def 申込Event = 申込().buildToStateEvent(false)
        def 契約Event = 契約().stateEventDateTime(2018, 2, 1).eventDateTime(2018, 3, 1).buildToStateEvent(false)

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
        def 申込Container = 申込().build()
        def 契約Container = 契約().stateEventDateTime(2018, 2, 1).eventDateTime(2018, 3, 1).build()

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
        assert sut.find(find).get().state == contracted

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

    def "find"() {
        setup:
        def builder1 = 申込("EVENT01").id("ID001").stateEventDateTime(2018, 1, 1)
        def builder2 = 申込("EVENT02").id("ID002").stateEventDateTime(2018, 2, 1)
        def builder3 = 申込("EVENT03").id("ID003").stateEventDateTime(2018, 1, 1)
        def builder4 = 契約("EVENT04").id("ID003").stateEventDateTime(2018, 2, 1)
        def builder5 = 申込("EVENT05").id("ID004").stateEventDateTime(2018, 5, 1)
        def builder6 = 契約("EVENT06").id("ID004").stateEventDateTime(2018, 6, 1)
        [builder1, builder2, builder3, builder4, builder5, builder6].forEach({
            sut.insertStateEvent(it.build())
            sut.refreshLatest(new FindContainer(it.build().id, SampleStateType.contract))
        })

        when:
        def act = sut.find(new StateCustomSelectorContainer(
                SampleStateType.contract,
                term(startMonth, endMonth),
                states as State[]
        ))

        then:
        act.collect { it.eventId.value } == expected

        where:
        startMonth | endMonth | states                || expected
        null       | null     | [ordered]             || ["EVENT01", "EVENT02"]
        null       | null     | [ordered, contracted] || ["EVENT01", "EVENT02", "EVENT04", "EVENT06"]
        2          | null     | [ordered]             || ["EVENT02"]
        2          | null     | [ordered, contracted] || ["EVENT02", "EVENT04", "EVENT06"]
        2          | 5        | [ordered]             || ["EVENT02"]
        2          | 7        | [ordered, contracted] || ["EVENT02", "EVENT04", "EVENT06"]
        2          | 6        | [ordered, contracted] || ["EVENT02", "EVENT04"]

    }

    static Optional<Term> term(Integer startMonth, Integer endMonth) {
        if(startMonth == null) {
            return Optional.empty()
        }
        if(endMonth == null) {
            return Optional.of(Term.termOf(new LocalDateTimeVOImpl(LocalDateTime.of(2018, startMonth.intValue(), 1, 0, 0))))
        }

        return Optional.of(Term.termOf(
                new LocalDateTimeVOImpl(LocalDateTime.of(2018, startMonth.intValue(), 1, 0, 0)),
                new LocalDateTimeVOImpl(LocalDateTime.of(2018, endMonth.intValue(), 1, 0, 0))
        ))

    }
}
