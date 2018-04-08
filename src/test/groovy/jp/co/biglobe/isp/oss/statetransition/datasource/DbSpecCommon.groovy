package jp.co.biglobe.isp.oss.statetransition.datasource

import jp.co.biglobe.isp.oss.statetransition.datasource.db.EventIdCreateMapper
import jp.co.biglobe.isp.oss.statetransition.datasource.db.TestTableSetupMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import spock.lang.Specification

@SpringBootTest
@TestConfiguration
class DbSpecCommon extends Specification {
    @Value("\${statetransition.table.state_event_table_name:#{null}}")
    Optional<String> stateEventTableName

    @Value("\${spring.datasource.url:#{null}}")
    Optional<String> datasource

    @Autowired
    TestTableSetupMapper testTableSetupMapper

    File getDbFile() {
        return datasource
                .map({ it.split("jdbc:sqlite:")[1] })
                .map({ new File(it) })
                .orElseThrow({ new RuntimeException("datasource undefined") })
    }

    def setup() {
//        File dbFile = getDbFile()
//        if(dbFile.exists()) {
//            dbFile.delete()
//        }
        testTableSetupMapper.createStateEventTable(stateEventTableName.get())
        testTableSetupMapper.createEventIdTable(EventIdCreateMapper.tableName)
    }

    def cleanup() {
//        def dbFile = getDbFile()
//        if(dbFile.exists()) {
//            dbFile.delete()
//        }
        [stateEventTableName.get(), EventIdCreateMapper.tableName].forEach({ testTableSetupMapper.dropTable(it) })
    }
}
