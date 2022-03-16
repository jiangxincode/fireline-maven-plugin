package edu.jiangxin.mvn.plugin;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.maven.plugin.logging.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;

public class HttpClientUtils {

	public interface DownLoadProgress {
		void onProgress(float progress);
	}

	public static void downloadFile(String url, String filePath,
									DownLoadProgress progress, Log logger) {
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom()
				.setCookieSpec(CookieSpecs.STANDARD).build())
				.build();
		try {
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response1 = httpclient.execute(httpGet);
			try {
				System.out.println(response1.getStatusLine());
				HttpEntity httpEntity = response1.getEntity();
				long contentLength = httpEntity.getContentLength();
				logger.info("contentLength: " + contentLength);
				InputStream is = httpEntity.getContent();
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				FileOutputStream fos = new FileOutputStream(filePath);
				byte[] buffer = new byte[4096];
				int r = 0;
				long totalRead = 0;
				while ((r = is.read(buffer)) > 0) {
					fos.write(buffer, 0, r);
					totalRead += r;
					if (progress != null) {
						progress.onProgress(((float)totalRead / contentLength));
					}
				}

				output.writeTo(fos);
				output.flush();
				output.close();
				fos.close();
				EntityUtils.consume(httpEntity);
			} finally {
				response1.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
