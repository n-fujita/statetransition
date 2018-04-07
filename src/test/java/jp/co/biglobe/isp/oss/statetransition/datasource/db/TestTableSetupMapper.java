package jp.co.biglobe.isp.oss.statetransition.datasource.db;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


public interface TestTableSetupMapper {
    @Update("CREATE TABLE ${tableName} (id TEXT, state_type TEXT, state TEXT, state_event_id TEXT, state_event_date_time DATE, event_date_time DATE)")
    void createStateTable(@Param("tableName") String tableName);

    @Update("DROP TABLE ${tableName}")
    void dropTable(@Param("tableName") String tableName);
}
