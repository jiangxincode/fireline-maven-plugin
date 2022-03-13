package edu.jiangxin.mess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solve the problem of MD034. <p>
 * {@link https://github.com/markdownlint/markdownlint/blob/master/docs/RULES.md}
 * This program doesn't process any exception!
 * @author aloys
 *
 */
public class MD034Solver {
	public static final String regex = "\\b(https?|ftp|file)://[-A-Z0-9+&@#/%?=~_|$!:,.;]*[A-Z0-9+&@#/%=~_|$]";
	
	public static final String sourceDirStr = "D:\\temp\\cnblogs";
	
	public static final String targetDirStr = "D:\\temp\\cnblogsbak";

	public static void main(String[] args) throws IOException {
		// Source directory must be a valid directory which contains the text files to be processed.
		File sourceDir = new File(sourceDirStr);

		// Target directory must be a valid directory which will save the proecessed files.
		File targetDir = new File(targetDirStr);

		for (File file : sourceDir.listFiles()) {

			// take off the .git directory and .gitignore file
			if (file.getName().startsWith(".")) {
				continue;
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(targetDir, file.getName())));

			String temp = null;
			while ((temp = reader.readLine()) != null) {
				Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
				Matcher regexMatcher = pattern.matcher(temp);
				StringBuffer sb = new StringBuffer();
				while (regexMatcher.find()) {
					String group = regexMatcher.group();
					int start = regexMatcher.start();
					if (start >= 1 && (temp.charAt(start - 1) == '<' || temp.charAt(start - 1) == '(')) {
						regexMatcher.appendReplacement(sb, group);
					} else {
						regexMatcher.appendReplacement(sb, "<" + group + ">");
					}

				}
				regexMatcher.appendTail(sb);
				writer.write(sb.toString());
				writer.newLine();
			}
			reader.close();
			writer.close();
		}
	}

}
