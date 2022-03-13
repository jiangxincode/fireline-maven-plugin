/**
 * a example that uses BufferWriter to speed the IO operation
 * @author jiangxin
 * @data 2014-6-20
 */
package edu.jiangxin.mess;

import java.io.*;

public class RandomTextOutput {
	public static void main(String[] args) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
				"temp/test.txt"));
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100; ++i) {
			for (int j = 0; j < 1024; ++j) {
				for (int k = 0; k < 1024; ++k) {
					stringBuilder.append("a");
				}
				stringBuilder.append("\n");
			}
			bufferedWriter.write(stringBuilder.toString()); // 积累1M数据，才写一次
			stringBuilder = stringBuilder.delete(0, stringBuilder.length());
		}
		bufferedWriter.flush();
		bufferedWriter.close();
		long end = System.currentTimeMillis();
		System.out.println((end - start) + "ms elapsed.");
	}
}
