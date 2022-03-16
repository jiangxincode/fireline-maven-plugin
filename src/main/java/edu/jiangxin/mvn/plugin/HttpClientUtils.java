package edu.jiangxin.mvn.plugin;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.maven.plugin.logging.Log;

import java.io.*;

public class HttpClientUtils {

    public interface DownLoadProgress {
        void onProgress(float progress);
    }

    public static void downloadFile(String url, String filePath,
                                    DownLoadProgress progress, Log logger) {
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();

        try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
             CloseableHttpResponse response = httpclient.execute(new HttpGet(url))) {
            if (response == null) {
                logger.error("response is null");
                return;
            }
            logger.info("statusLine: " + response.getStatusLine());
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity == null) {
                logger.error("httpEntity is null");
                return;
            }
            long contentLength = httpEntity.getContentLength();
            logger.info("contentLength: " + contentLength);
            try (InputStream is = httpEntity.getContent();
                 BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(filePath));
            ) {
                byte[] buffer = new byte[4096];
                int readLength = 0;
                long totalRead = 0;
                while ((readLength = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, readLength);
                    totalRead += readLength;
                    if (progress != null) {
                        progress.onProgress(((float) totalRead / contentLength));
                    }
                }
                EntityUtils.consume(httpEntity);
            } catch (FileNotFoundException e) {
                logger.error("FileNotFoundException" + e.getMessage());
            } catch (IOException e) {
                logger.error("IOException" + e.getMessage());
            }
        } catch (ClientProtocolException e) {
            logger.error("ClientProtocolException" + e.getMessage());
        } catch (IOException e) {
            logger.error("IOException" + e.getMessage());
        }
    }
}
