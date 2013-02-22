package com.tianji.r.core.conf;

import java.util.List;

import javax.sql.DataSource;

import com.tianji.r.core.util.SCPConnection;

public class DatabaseJobConf extends JobConf {

    private String remoteExportFile;
    private String remoteExportFolder;
    private String remoteExportFileName;
    private String remoteExportSQL;
    private DataSource remoteExportDataSource;
    private SCPConnection remoteExportSCPConection;
    private String localFolder;
    private DataSource localImportDataSource;
    private String localImportTable;
    private String localImportTableWay;
    private List<String> localImportTableCreateSQL;
    private List<String> localImportTableDropSQL;

    public List<String> getLocalImportTableCreateSQL() {
        return localImportTableCreateSQL;
    }

    public String getLocalImportTableWay() {
        return localImportTableWay;
    }

    public void setLocalImportTableWay(String localImportTableWay) {
        this.localImportTableWay = localImportTableWay;
    }

    public void setLocalImportTableCreateSQL(List<String> localImportTableCreateSQL) {
        this.localImportTableCreateSQL = localImportTableCreateSQL;
    }

    public List<String> getLocalImportTableDropSQL() {
        return localImportTableDropSQL;
    }

    public void setLocalImportTableDropSQL(List<String> localImportTableDropSQL) {
        this.localImportTableDropSQL = localImportTableDropSQL;
    }

    public SCPConnection getRemoteExportSCPConection() {
        return remoteExportSCPConection;
    }

    public void setRemoteExportSCPConection(SCPConnection remoteExportSCPConection) {
        this.remoteExportSCPConection = remoteExportSCPConection;
    }

    public DataSource getLocalImportDataSource() {
        return localImportDataSource;
    }

    public void setLocalImportDataSource(DataSource localImportDataSource) {
        this.localImportDataSource = localImportDataSource;
    }

    public DataSource getRemoteExportDataSource() {
        return remoteExportDataSource;
    }

    public void setRemoteExportDataSource(DataSource remoteExportDataSource) {
        this.remoteExportDataSource = remoteExportDataSource;
    }

    public String getRemoteExportFilePath() {
        return getRemoteExportFolder() + this.remoteExportFileName;
    }

    public String getRemoteExportFile() {
        return remoteExportFile;
    }

    public void setRemoteExportFile(String remoteExportFile) {
        this.remoteExportFile = remoteExportFile;
        this.remoteExportFileName = "export_" + System.currentTimeMillis() + "_" + getRemoteExportFile();
    }

    public String getRemoteExportFolder() {
        return remoteExportFolder;
    }

    public void setRemoteExportFolder(String remoteExportFolder) {
        this.remoteExportFolder = remoteExportFolder;
    }

    public String getRemoteExportFileName() {
        return remoteExportFileName;
    }

    public String getRemoteExportSQL() {
        return remoteExportSQL;
    }

    public void setRemoteExportSQL(String remoteExportSQL) {
        this.remoteExportSQL = remoteExportSQL;
    }

    public String getLocalImportTable() {
        return localImportTable;
    }

    public void setLocalImportTable(String localImportTable) {
        this.localImportTable = localImportTable;
    }

    public String getLocalFolder() {
        return localFolder;
    }

    public void setLocalFolder(String localFolder) {
        this.localFolder = localFolder;
    }

    public String getLocalFilePath() {
        return getLocalFolder() + this.remoteExportFileName;
    }

}