package jp.co.biglobe.isp.oss.statetransition.datasource.db

import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

interface TestTableSetupMapper {
    @Update("""
    CREATE TABLE \${tableName} (
        state_event_id TEXT NOT NULL UNIQUE,
        id TEXT, 
        state_type TEXT NOT NULL, 
        state TEXT NOT NULL,  
        state_event_date_time DATE NOT NULL,
        event_date_time DATE NOT NULL,
        is_latest NUMBER
    );
    create index i_state_event_id on \${tableName}(state_event_id);
    create index i_id on \${tableName}(id);
    create index i_state_type on \${tableName}(state_type);
    create index i_state_event_date_time on \${tableName}(state_event_date_time);
    create index i_event_date_time on \${tableName}(event_date_time);
    create index i_is_latest on \${tableName}(is_latest);
    """)
    void createStateEventTable(@Param("tableName") String tableName);

    @Update("""
    CREATE TABLE \${tableName} (id INTEGER PRIMARY KEY, tmp NUMBER)
    """)
    void createEventIdTable(@Param("tableName") String tableName)

    @Update("""
    DROP TABLE \${tableName}
    """)
    void dropTable(@Param("tableName") String tableName)
}