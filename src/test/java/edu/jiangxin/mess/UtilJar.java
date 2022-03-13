package edu.jiangxin.mess;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

/*实验结论:
 * 复制jar文件有3中方法, 
 * A是直接用BufferedInputStream和BufferedOutputStream复制,
 * B是用JarInputStream的getNextJarEntry()和jarOutputStream的putNextJarEntry()
 * C是用JarFile的entries()方法,遍寻JarEntry的InputStream,以此写出
 */

public class UtilJar {

	// 复制jar
	public void copyJar(File src, File des) throws FileNotFoundException, IOException {
		JarInputStream jarInputStream = new JarInputStream(new BufferedInputStream(new FileInputStream(src)));
		/*
		 * JarInputStream的getNextJarEntry()和jarOutputStream的putNextJarEntry()中
		 * 没有包括"META-INF/MANIFEST.MF"这一项,因此复制和解压都要注意
		 */
		Manifest manifest = jarInputStream.getManifest();
		JarOutputStream jarOutputStream = null;
		if (manifest == null) {
			jarOutputStream = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(des)));
		} else {
			jarOutputStream = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(des)), manifest);
		}

		byte[] bytes = new byte[1024];
		while (true) {
			ZipEntry entry = jarInputStream.getNextJarEntry();
			if (entry == null) {
				break;
			}
			jarOutputStream.putNextEntry(entry);

			int len = jarInputStream.read(bytes, 0, bytes.length);
			while (len != -1) {
				jarOutputStream.write(bytes, 0, len);
				len = jarInputStream.read(bytes, 0, bytes.length);
			}
			System.out.println("Copyed: " + entry.getName());
		}
		jarInputStream.close();
		jarOutputStream.finish();
		jarOutputStream.close();
	}

	// 解压jar
	public void unJar(File src, File desDir) throws FileNotFoundException, IOException {
		JarInputStream jarInputStream = new JarInputStream(new BufferedInputStream(new FileInputStream(src)));
		if (!desDir.exists()) {
			desDir.mkdirs();
		}
		byte[] bytes = new byte[1024];

		while (true) {
			ZipEntry entry = jarInputStream.getNextJarEntry();
			if (entry == null) {
				break;
			}

			File desTemp = new File(desDir.getAbsoluteFile() + File.separator + entry.getName());

			if (entry.isDirectory()) { // jar条目是空目录
				if (!desTemp.exists()) {
					desTemp.mkdirs();
				}
				System.out.println("MakeDir: " + entry.getName());
			} else { // jar条目是文件
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desTemp));
				int len = jarInputStream.read(bytes, 0, bytes.length);
				while (len != -1) {
					out.write(bytes, 0, len);
					len = jarInputStream.read(bytes, 0, bytes.length);
				}

				out.flush();
				out.close();
				
				System.out.println("Copyed: " + entry.getName());
			}
			jarInputStream.closeEntry();
		}

		// 解压Manifest文件
		Manifest manifest = jarInputStream.getManifest();
		if (manifest != null) {
			File manifestFile = new File(desDir.getAbsoluteFile() + File.separator + JarFile.MANIFEST_NAME);
			if (!manifestFile.getParentFile().exists())
				manifestFile.getParentFile().mkdirs();
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(manifestFile));
			manifest.write(out);
			out.close();
		}

		// 关闭JarInputStream
		jarInputStream.close();
	}

	// 复制jar by JarFile
	public void copyJarByJarFile(File src, File des) throws IOException {
		JarFile jarFile = new JarFile(src);
		/*
		 * JarFile的entries()方法包含了全部Entry,也包括"META-INF/MANIFEST.MF",没有"META-INF/"这一项,因此在解压的时候要先检测父文件存不存在
		 */
		Enumeration<JarEntry> jarEntrys = jarFile.entries();
		JarOutputStream jarOut = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(des)));
		byte[] bytes = new byte[1024];
		
		while (jarEntrys.hasMoreElements()) {
			JarEntry entryTemp = jarEntrys.nextElement();
			jarOut.putNextEntry(entryTemp);
			BufferedInputStream in = new BufferedInputStream(jarFile.getInputStream(entryTemp));
			int len = in.read(bytes, 0, bytes.length);
			while (len != -1) {
				jarOut.write(bytes, 0, len);
				len = in.read(bytes, 0, bytes.length);
			}
			in.close();
			jarOut.closeEntry();
			System.out.println("Copyed: " + entryTemp.getName());
		}

		jarOut.finish();
		jarOut.close();
		jarFile.close();
	}

	// 解压jar文件by JarFile
	public void unJarByJarFile(File src, File desDir) throws FileNotFoundException, IOException {
		JarFile jarFile = new JarFile(src);
		Enumeration<JarEntry> jarEntrys = jarFile.entries();
		if (!desDir.exists())
			desDir.mkdirs(); // 建立用户指定存放的目录
		byte[] bytes = new byte[1024];

		while (jarEntrys.hasMoreElements()) {
			ZipEntry entryTemp = jarEntrys.nextElement();
			File desTemp = new File(desDir.getAbsoluteFile() + File.separator + entryTemp.getName());

			if (entryTemp.isDirectory()) { // jar条目是空目录
				if (!desTemp.exists()) {
					desTemp.mkdirs();
				}
				System.out.println("makeDir" + entryTemp.getName());
			} else { // jar条目是文件
				// 因为manifest的Entry是"META-INF/MANIFEST.MF",写出会报"FileNotFoundException"
				File desTempParent = desTemp.getParentFile();
				if (!desTempParent.exists())
					desTempParent.mkdirs();

				BufferedInputStream in = new BufferedInputStream(jarFile.getInputStream(entryTemp));
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(desTemp));

				int len = in.read(bytes, 0, bytes.length);
				while (len != -1) {
					out.write(bytes, 0, len);
					len = in.read(bytes, 0, bytes.length);
				}

				in.close();
				out.flush();
				out.close();
				System.out.println("Copyed: " + entryTemp.getName());
			}
		}

		jarFile.close();
	}



	
}