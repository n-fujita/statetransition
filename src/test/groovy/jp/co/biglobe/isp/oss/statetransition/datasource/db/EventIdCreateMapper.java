package jp.co.biglobe.isp.oss.statetransition.datasource.db;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface EventIdCreateMapper {
    static final String tableName = "event_id_create";
    @Update("INSERT INTO event_id_create (tmp) VALUES (1)")
    void increment();

    @Select("SELECT MAX(id) FROM event_id_create")
    int getMaxEventId();
}
