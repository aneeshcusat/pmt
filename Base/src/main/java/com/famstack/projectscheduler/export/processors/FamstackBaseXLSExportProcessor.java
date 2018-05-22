package com.famstack.projectscheduler.export.processors;

import java.util.Map;

public interface FamstackBaseXLSExportProcessor
{

    public void renderReport(Map<String, Object> dataMap);

}
