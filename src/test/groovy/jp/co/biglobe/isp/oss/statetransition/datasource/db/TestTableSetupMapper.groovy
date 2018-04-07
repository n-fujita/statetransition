package jp.co.biglobe.isp.oss.statetransition.datasource.db

import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Update

interface TestTableSetupMapper {
    @Update("""
    CREATE TABLE \${tableName} (
        state_event_id TEXT UNIQUE,
        id TEXT, 
        state_type TEXT, 
        state TEXT,  
        state_event_date_time DATE, 
        event_date_time DATE
    );
    create index i_state_event_id on \${tableName}(state_event_id);
    create index i_id on \${tableName}(id);
    create index i_state_type on \${tableName}(state_type);
    create index i_state_event_date_time on \${tableName}(state_event_date_time);
    create index i_event_date_time on \${tableName}(event_date_time);
    """)
    void createStateTable(@Param("tableName") String tableName);

    @Update("""
    CREATE TABLE \${tableName} (
        state_event_id TEXT UNIQUE,
        id TEXT, 
        state_type TEXT, 
        state TEXT,  
        state_event_date_time DATE,
        event_date_time DATE
    );
    create index i_state_event_id on \${tableName}(state_event_id);
    create index i_id on \${tableName}(id);
    create index i_state_type on \${tableName}(state_type);
    create index i_state_event_date_time on \${tableName}(state_event_date_time);
    create index i_event_date_time on \${tableName}(event_date_time);
    """)
    void createStateEventTable(@Param("tableName") String tableName);

    @Update("""
    DROP TABLE \${tableName}
    """)
    void dropTable(@Param("tableName") String tableName);
}