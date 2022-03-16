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
public class FirelineReport extends AbstractMavenReport {
	private final Log mavenLog = getLog();

	@Parameter(defaultValue = "${basedir}", property = "projectDirectory", required = true)
	private File projectDirectory;

	@Parameter(defaultValue = "${project.build.sourceDirectory}", property = "sourceDirectory", required = true)
	private File sourceDirectory;

	@Parameter(defaultValue = "${project.build.testSourceDirectory}", property = "testSourceDirectory", required = true)
	private File testSourceDirectory;

	@Parameter(defaultValue = "${project.build.directory}\\fireline.jar", property = "firelinePath", required = true)
	private String firelinePath;

	@Parameter(defaultValue = "http://magic.360.cn/fireline_1.7.3.jar", property = "downloadUrl", required = true)
	private String downloadUrl;

	@Override
	public void executeReport(Locale locale) {
		if (StringUtils.isEmpty(firelinePath)) {
			mavenLog.error("firelinePath is empty");
			return;
		}
		File file = new File(firelinePath);
		if (file.exists() && file.isDirectory()) {
			mavenLog.warn("firelinePath is a directory, delete it anyway");
			file.delete();
		}

		if (!file.exists()) {
			mavenLog.info("firelinePath does not exist, download it");
			downloadFireline();
		}
		//onFinishedDownload();
	}

	private void onFinishedDownload() {
		StringBuilder cmdBuilder = new StringBuilder();
		cmdBuilder.append("java -jar ").append(firelinePath).append(" -s=D:\\Code\\Maven\\fireline-maven-plugin -r=D:\\Code\\Maven\\fireline-maven-plugin\\target\\site");

		try (ProcessLogOutputStream outStream = new ProcessLogOutputStream(mavenLog, ProcessLogOutputStream.LOG_LEVEL_INFO);
			 ProcessLogOutputStream errStream = new ProcessLogOutputStream(mavenLog, ProcessLogOutputStream.LOG_LEVEL_ERROR)
		) {
			CommandLine commandLine = CommandLine.parse(cmdBuilder.toString());
			DefaultExecutor exec = new DefaultExecutor();
			PumpStreamHandler streamHandler = new PumpStreamHandler(outStream, errStream);
			exec.setStreamHandler(streamHandler);
			int exitValue = exec.execute(commandLine);
			mavenLog.info("exitValue: [" + exitValue + "]");
		} catch (IOException ioe) {
			mavenLog.error("executeReport fail: " + ioe.getMessage());
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
			mavenLog.info("download progress = " + numberFormat.format(progress));
		}
	}

	private void downloadFireline() {
		ExecutorService executorService = Executors.newFixedThreadPool(1);
		Thread t = new Thread(() -> HttpClientUtils.downloadFile(downloadUrl, firelinePath, new MyDownloadProgress(), mavenLog));
		long start = System.currentTimeMillis();
		System.out.println("start = " + start);
		Future future = executorService.submit(t);
		try {
			future.get();
		}catch (InterruptedException e){
			e.printStackTrace();
		}catch (ExecutionException e){
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("end = " + end);
		System.out.println("end - start = " + (end - start));
		executorService.shutdown();
	}
}