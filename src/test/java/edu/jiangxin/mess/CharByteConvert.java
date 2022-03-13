package edu.jiangxin.mess;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * 尚未测试成功
 * @author Administrator
 *
 */
public class CharByteConvert {
	public static byte[] getBytes(char[] chars) { // char转byte
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);
		return bb.array();

	}

	public static char[] getChars(byte[] bytes) { // byte转char
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);
		return cb.array();
	}
	public static void main(String[] args) {
		byte[] origin = CharByteConvert.getBytes("This is a test!".toCharArray());
		char[] target = CharByteConvert.getChars(origin);
		System.out.println(target.toString());
	}
}
