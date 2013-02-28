package com.tianji.r.core.job.task;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.hadoop.mapred.JobConf;
import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tianji.r.core.conf.MapRedAlgorithmConf;
import com.tianji.r.core.conf.TaskConf;
import com.tianji.r.core.conf.model.HdfsPathNew;
import com.tianji.r.core.etl.ImportHdfsService;
import com.tianji.r.core.storage.MapReduceService;

@Service
public class MapRedAlgorithmTask implements Tasklet, TaskConf<MapRedAlgorithmConf> {

    private static final Logger log = Logger.getLogger(MapRedAlgorithmTask.class);

    @Autowired
    MapReduceService mapRecudeService;
    @Autowired
    ImportHdfsService importHdfsService;

    MapRedAlgorithmConf jobConf;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("TASK: MapReduce Algorithm Task - " + jobConf.getMapReduceClass());
        newTableProcess(jobConf.getHdfsPath(), jobConf.getResultPath());
        importDataProcess();
        return RepeatStatus.FINISHED;
    }

    @Override
    public void setJobConf(MapRedAlgorithmConf jobConf) {
        this.jobConf = jobConf;
    }

    private void newTableProcess(HdfsPathNew hdfs, String path) throws SQLException, IOException {
        importHdfsService.setHdfsSource(hdfs.getHdfsSource());
        importHdfsService.exec("hadoop fs -rmr " + path);
    }

    private void importDataProcess() throws ClassNotFoundException, IOException {
        mapRecudeService.setHdfsSource(jobConf.getHdfsPath().getHdfsSource());
        
        JobConf conf = mapRecudeService.createJobConf(jobConf.getMapReduceClazz());
        conf.setOutputKeyClass(jobConf.getOutputKeyClazz());
        conf.setOutputValueClass(jobConf.getOutputValueClazz());
        conf.setMapperClass(jobConf.getMapperClazz());
        conf.setReducerClass(jobConf.getReducerClazz());
        mapRecudeService.setInputPath(jobConf.getHdfsPath().getStorePath());
        mapRecudeService.setOutputPath(jobConf.getResultPath());
        mapRecudeService.exec();
    }

}