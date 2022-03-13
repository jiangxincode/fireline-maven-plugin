package edu.jiangxin.mvn.plugin;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@Mojo(name = "fireline-report", defaultPhase = LifecyclePhase.SITE, threadSafe = true, requiresProject = true)
public class FirelineReport extends AbstractMavenReport {
	private final Log mavenLog = getLog();

	@Parameter(defaultValue = "${basedir}", property = "fireline.projectDirectory", required = true)
	private File projectDirectory;

	@Parameter(defaultValue = "${project.build.sourceDirectory}", property = "fireline.sourceDirectory", required = true)
	private File sourceDirectory;

	@Parameter(defaultValue = "${project.build.testSourceDirectory}", property = "fireline.testSourceDirectory", required = true)
	private File testSourceDirectory;

	/**
	 * Link the violation line numbers to the source xref. Defaults to true and will link automatically if jxr plugin is
	 * being used.
	 */
	@Parameter(defaultValue = "true", property = "linkXRef")
	private boolean linkXRef;

	@Parameter(defaultValue = "${project.build.directory}/site/xref")
	private File xrefLocation;

	@Parameter(defaultValue = "${project.build.directory}/site/xref-test")
	private File xrefTestLocation;

	@Parameter(defaultValue = "6", property = "fireline.threshold", required = true)
	private int threshold;

	@Override
	public void executeReport(Locale locale) throws MavenReportException {
        try (ProcessLogOutputStream outStream = new ProcessLogOutputStream(mavenLog, ProcessLogOutputStream.LOG_LEVEL_INFO);
			 ProcessLogOutputStream errStream = new ProcessLogOutputStream(mavenLog, ProcessLogOutputStream.LOG_LEVEL_ERROR)
        ) {
            CommandLine commandLine = CommandLine.parse("java -jar D:\\Code\\Maven\\fireline-maven-plugin\\lib\\fireline_1.7.3.jar -s=D:\\Code\\Maven\\fireline-maven-plugin -r=D:\\Code\\Maven\\fireline-maven-plugin\\target\\site");
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
}