package edu.jiangxin.mvn.plugin;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.reporting.AbstractMavenReport;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Mojo(name = "fireline-report", defaultPhase = LifecyclePhase.SITE, threadSafe = true, requiresProject = true)
public class FireLineReport extends AbstractMavenReport {
    private final Log logger = getLog();

    @Parameter(defaultValue = "${basedir}", property = "projectDirectory", required = true)
    private File projectDirectory;

    @Parameter(defaultValue = "${project.build.sourceDirectory}", property = "sourceDirectory", required = true)
    private File sourceDirectory;

    @Parameter(defaultValue = "${project.build.testSourceDirectory}", property = "testSourceDirectory", required = true)
    private File testSourceDirectory;

    @Parameter(defaultValue = "${project.build.directory}\\site", property = "reportDirectory", required = true)
    private File reportDirectory;

    @Parameter(defaultValue = "${project.build.directory}\\fireline.jar", property = "fireLinePath", required = true)
    private String fireLinePath;

    @Parameter(defaultValue = "http://magic.360.cn/fireline.jar", property = "fireLineDownloadUrl", required = true)
    private String fireLineDownloadUrl;

    @Override
    public void executeReport(Locale locale) {
        if (StringUtils.isEmpty(fireLinePath)) {
            logger.error("firelinePath is empty");
            return;
        }
        File file = new File(fireLinePath);
        if (file.exists() && file.isDirectory()) {
            logger.warn("firelinePath is a directory, delete it anyway");
            file.delete();
        }

        if (!file.exists()) {
            logger.info("firelinePath does not exist, download it");
            downloadFireLine();
        }
        if (!file.exists()) {
			logger.info("firelinePath does not exist, download failed");
		}
        onFinishedDownload();
    }

    private void onFinishedDownload() {
        StringBuilder cmdBuilder = new StringBuilder();
        cmdBuilder.append("java -jar ").append(fireLinePath)
                .append(" -s=").append(sourceDirectory)
                .append(" -r=").append(reportDirectory);
        try (ProcessLogOutputStream outStream = new ProcessLogOutputStream(logger, ProcessLogOutputStream.LOG_LEVEL_INFO);
             ProcessLogOutputStream errStream = new ProcessLogOutputStream(logger, ProcessLogOutputStream.LOG_LEVEL_ERROR)
        ) {
            CommandLine commandLine = CommandLine.parse(cmdBuilder.toString());
            DefaultExecutor exec = new DefaultExecutor();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outStream, errStream);
            exec.setStreamHandler(streamHandler);
            int exitValue = exec.execute(commandLine);
            logger.info("exitValue: [" + exitValue + "]");
        } catch (IOException ioe) {
            logger.error("executeReport fail: " + ioe.getMessage());
        }
    }

    @Override
    public String getOutputName() {
        return "testReport";
    }

    @Override
    public String getName(Locale locale) {
        getLog().info(getBundle(locale).getString("report.dashboard.title.name"));
        return getBundle(locale).getString("report.dashboard.title.name");
    }

    @Override
    public String getDescription(Locale locale) {
        getLog().info(getBundle(locale).getString("report.dashboard.title.description"));
        return getBundle(locale).getString("report.dashboard.title.description");
    }

    private ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle("fireline-report", locale, this.getClass().getClassLoader());
    }

    @Override
    public boolean isExternalReport() {
        return true;
    }

    class MyDownloadProgress implements HttpClientUtils.DownLoadProgress {

        @Override
        public void onProgress(float progress) {
            NumberFormat numberFormat = NumberFormat.getPercentInstance();
            numberFormat.setMaximumIntegerDigits(3);
            numberFormat.setMaximumFractionDigits(2);
            logger.info("download progress = " + numberFormat.format(progress));
        }
    }

    private void downloadFireLine() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Thread t = new Thread(() -> HttpClientUtils.downloadFile(fireLineDownloadUrl, fireLinePath, new MyDownloadProgress(), logger));
        long start = System.currentTimeMillis();
        logger.info("download FireLine start: " + start);
        Future future = executorService.submit(t);
        try {
            future.get();
        } catch (InterruptedException e) {
            logger.error("InterruptedException" + e.getMessage());
        } catch (ExecutionException e) {
            logger.error("ExecutionException" + e.getMessage());
        }
        long end = System.currentTimeMillis();
        logger.info("download FireLine end: " + end);
        executorService.shutdown();
    }
}