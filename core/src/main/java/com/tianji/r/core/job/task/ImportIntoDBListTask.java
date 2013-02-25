package com.tianji.r.core.job.task;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tianji.r.core.conf.DatabaseJobConf;
import com.tianji.r.core.conf.DatabaseJobConfList;
import com.tianji.r.core.conf.TaskConf;
import com.tianji.r.core.conf.model.NewDBTable;
import com.tianji.r.core.etl.ImportMySQLService;
import com.tianji.r.core.etl.TransformMySQLService;

@Service
public class ImportIntoDBListTask implements TaskConf<DatabaseJobConfList>, Tasklet {

    private static final Logger log = Logger.getLogger(ImportIntoDBListTask.class);

    @Autowired
    ImportMySQLService importMySQLService;
    @Autowired
    TransformMySQLService transformMySQLService;

    DatabaseJobConfList jobConf;

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("TASK: Import Into DB Task");
        for (DatabaseJobConf conf : jobConf.getDbSyncConfList()) {
            NewDBTable table = conf.getDbTable();
            importTableWay(table);
            
            String localFile = table.getLocalFile();
            if (localFile == null) {
                localFile = conf.getLocalFilePath();
            }
            importMySQLService.setInput(localFile);
            importMySQLService.setTable(table.getTableName());
            importMySQLService.setDataSource(table.getDataSource());
            importMySQLService.exec();
        }
        return RepeatStatus.FINISHED;
    }

    private void importTableWay(NewDBTable table) throws SQLException {
        String way = table.getLoadWay();
        way = way == null ? "APPEND" : way.toUpperCase();
        log.info("ImportTableWay: " + way);
        if (way.equalsIgnoreCase("OVERRIDE")) {
            transformMySQLService.setDataSource(table.getDataSource());
            transformMySQLService.addSqlList(table.getDropSQLs());
            transformMySQLService.addSqlList(table.getCreateSQLs());
            transformMySQLService.exec();
        } else if (way.equalsIgnoreCase("update")) {// TODO next version
        } else {// append
        }
    }

    @Override
    public void setJobConf(DatabaseJobConfList jobConf) {
        this.jobConf = jobConf;
    }
}