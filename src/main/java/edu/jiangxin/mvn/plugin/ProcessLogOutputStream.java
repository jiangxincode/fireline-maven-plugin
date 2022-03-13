package edu.jiangxin.mvn.plugin;

import org.apache.commons.exec.LogOutputStream;
import org.apache.maven.plugin.logging.Log;

public class ProcessLogOutputStream extends LogOutputStream {
    public static final int LOG_LEVEL_INFO = 0;

    public static final int LOG_LEVEL_ERROR = 1;

    private Log log;

    public ProcessLogOutputStream(Log log, int logLevel) {
        super(logLevel);
        this.log = log;
    }

    @Override
    protected void processLine(String line, int logLevel) {
        if (LOG_LEVEL_INFO == logLevel) {
            log.info("[FirelineReportPlugin] " + line);
        } else if (LOG_LEVEL_ERROR == logLevel) {
            log.error("[FirelineReportPlugin] " + line);
        } else {
            log.error("[FirelineReportPlugin] Invalid log level");
        }
    }
}
