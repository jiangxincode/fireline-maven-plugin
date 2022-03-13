/**
 * String to InputStream or Reader
 * 关于字符串转化为输入流，一般有2种方法：用StringReader将字符串转化为Reader；用ByteArrayInputStream将字符串转化为InputStream。
 * 还有一个类StringBufferInputStream也可以将String转化为InputStream，但是由于它只支持字符串中每个字符的低八位，所以已经被遗弃了。
 * @author jiangxin
 * @data 2014-6-20
 */
package edu.jiangxin.mess;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

public class StringToInputStreamOrReader {

	public static InputStream toInputStream(String s) {
		if (s != null && !s.equals("")) {
			try {
				ByteArrayInputStream stringInputStream = new ByteArrayInputStream(
						s.getBytes());
				return stringInputStream;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Reader toReader(String s) {
		if (s != null && !s.equals("")) {
			try {
				StringReader stringReader = new StringReader(s);
				return stringReader;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] argv) {
		String string = new String("This is a test" + " 这是一个测试");
		Reader reader = toReader(string);
		InputStream inputStream = toInputStream(string);
		try {
			char[] cbuf = { '\0' };
			while ((reader.read(cbuf)) != -1) {
				System.out.print(cbuf);
			}
			
			System.out.println();
			
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream);
			while ((inputStreamReader.read(cbuf)) != -1) {
				System.out.print(cbuf);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}