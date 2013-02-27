package com.tianji.r.core.job.task;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tianji.r.core.conf.model.HiveTableNew;
import com.tianji.r.core.etl.ImportHiveService;

@Service
public class HiveImportTask implements Tasklet {// TaskConf<HiveJobConf>

    private static final Logger log = Logger.getLogger(HiveImportTask.class);

    @Autowired
    ImportHiveService importHiveService;

    // BasicDataSource dataSource;
    // HiveSource hiveSource;
    // HiveJobConf jobConf;

    List<HiveTableNew> hiveNewList;

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("TASK: Import Into Hive Task");

        for (HiveTableNew hiveTable : hiveNewList) {
            newTableProcess(hiveTable);
            importDataProcess(hiveTable);
        }

        // for (String table : jobConf.getImportIntoHiveTables()) {
        // StringBuilder sb = new StringBuilder();
        // sb.append(" sqoop import ");
        // sb.append(" --connect ").append(dataSource.getUrl().substring(0, dataSource.getUrl().indexOf("?")));
        // sb.append(" --username ").append(dataSource.getUsername());
        // sb.append(" --password ").append(dataSource.getPassword());
        // sb.append(" --table ").append(table);
        // sb.append(" --hive-import");
        // sb.append(" --hive-overwrite");
        // // sb.append(" --append");
        // // sb.append(" --fields-terminated-by '\t'");
        // // log.info(sb.toString());
        // importHiveService.exec(sb.toString());
        // }
        return RepeatStatus.FINISHED;
    }

    // @Override
    // public void setDataSource(BasicDataSource dataSource) {
    // this.dataSource = dataSource;
    // }
    //
    // public void setHiveSource(HiveSource hiveSource) {
    // importHiveService.setHiveSource(hiveSource);
    // }
    //
    // @Override
    // public void setJobConf(HiveJobConf jobConf) {
    // this.jobConf = jobConf;
    // }

    private void newTableProcess(HiveTableNew table) throws SQLException {
        
    }

    private void importDataProcess(HiveTableNew table) throws SQLException, IOException {
        BasicDataSource dataSource = table.getDbTable().getDataSource();
        StringBuilder sb = new StringBuilder();
        sb.append("sqoop import");
        sb.append(" --connect ").append(dataSource.getUrl().substring(0, dataSource.getUrl().indexOf("?")));
        sb.append(" --username ").append(dataSource.getUsername());
        sb.append(" --password ").append(dataSource.getPassword());
        sb.append(" --table ").append(table.getDbTable().getTableName());
        sb.append(" --hive-import");
        
        String way = table.getLoadWay() == null ? "OVERRIDE" : table.getLoadWay();
        log.info("HiveImportWay:" + way);
        if (way.equalsIgnoreCase("APPEND")) {
            sb.append(" --append");
        } else {// OVERRIDE
            sb.append(" --hive-overwrite");
        }
        sb.append(" --direct");
        // sb.append(" --fields-terminated-by '\t'");
        // log.info(sb.toString());
        
        importHiveService.setHiveSource(table.getHiveSource());
        importHiveService.exec(sb.toString());
    }

    public void setHiveNewList(List<HiveTableNew> hiveNewList) {
        this.hiveNewList = hiveNewList;
    }

}