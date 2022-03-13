package edu.jiangxin.mess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Properties;

public class ProxySettingUtils {

	public static void global() throws IOException {
		Properties fileProperties = new Properties();

		FileInputStream fis = new FileInputStream(new File("proxy.properties"));
		fileProperties.load(fis);
		fis.close();

		Properties sysProperties = System.getProperties();

		// 设置http访问要使用的代理服务器的地址
		sysProperties.setProperty("http.proxyHost", fileProperties.getProperty("http.proxyHost"));
		// 设置http访问要使用的代理服务器的端口
		sysProperties.setProperty("http.proxyPort", "8080");
		// 设置不需要通过代理服务器访问的主机，可以使用*通配符，多个地址用|分隔
		sysProperties.setProperty("http.nonProxyHosts", "localhost|192.168.0.*");

		// 设置安全访问使用的代理服务器地址与端口
		// 它没有https.nonProxyHosts属性，它按照http.nonProxyHosts 中设置的规则访问
		sysProperties.setProperty("https.proxyHost", "183.45.78.31");
		sysProperties.setProperty("https.proxyPort", "443");

		// 使用ftp代理服务器的主机、端口以及不需要使用ftp代理服务器的主机
		sysProperties.setProperty("ftp.proxyHost", "183.45.78.31");
		sysProperties.setProperty("ftp.proxyPort", "21");
		sysProperties.setProperty("ftp.nonProxyHosts", "localhost|192.168.0.*");

		// socks代理服务器的地址与端口
		sysProperties.setProperty("socksProxyHost", "183.45.78.31");
		sysProperties.setProperty("socksProxyPort", "1080");

		// 设置登陆到代理服务器的用户名和密码
		Authenticator.setDefault(new ProxyAuthenticator("userName", "Password"));
	}

	public static void local() throws Exception {
		URL url = new URL("http://www.3lai8.com");
		// /创建代理服务器
		InetSocketAddress addr = new InetSocketAddress("192.168.0.254", 8080);
		// Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr); // Socket 代理
		Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); // http 代理
		Authenticator.setDefault(new ProxyAuthenticator("username", "password"));// 设置代理的用户和密码
		HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);// 设置代理访问
		InputStreamReader in = new InputStreamReader(connection.getInputStream());
		BufferedReader reader = new BufferedReader(in);
		while (true) {
			String s = reader.readLine();
			if (s != null) {
				System.out.println(s);
			}
		}
	}

}

class ProxyAuthenticator extends Authenticator {
	private String user;
	private String password;

	public ProxyAuthenticator() {
		this.user = "";
		this.password = "";
	}

	public ProxyAuthenticator(String user, String password) {
		this.user = user;
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}