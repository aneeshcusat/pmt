package com.famstack.projectscheduler.thread;

import java.util.Map;

import com.famstack.projectscheduler.export.processors.FamstackBaseXLSExportProcessor;

public class ExportProjectWorkerThread implements Runnable
{

    Map<String, Object> dataMap;

    FamstackBaseXLSExportProcessor baseXLSExportProcessor;

    public ExportProjectWorkerThread(Map<String, Object> dataMap, FamstackBaseXLSExportProcessor baseXLSExportProcessor)
    {
        this.dataMap = dataMap;
        this.baseXLSExportProcessor = baseXLSExportProcessor;
    }

    @Override
    public void run()
    {
        baseXLSExportProcessor.renderReport(dataMap);
    }
}
