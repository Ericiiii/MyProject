package com.mayday.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;


public class HttpClientHelper {
    private static PoolingHttpClientConnectionManager cm = null;

    static {
        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("创建SSL连接失败");
            e.printStackTrace();
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory()).build();
        cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);
    }

    private static CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        return httpClient;
    }

    public static HttpClientResponseModel get(String url, String param) {
        HttpClientResponseModel resultModel = new HttpClientResponseModel();
        // 创建默认的httpClient实例
        CloseableHttpClient httpClient = HttpClientHelper.getHttpClient();
        CloseableHttpResponse httpResponse = null;
        String httpUrl = url;
        try {
            if (param != null) httpUrl += URLEncoder.encode(param, "UTF-8");
            HttpGet get = new HttpGet(httpUrl);
            RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                    .setSocketTimeout(5000).build();
            get.setConfig(config);
            get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            get.setHeader("Accept-Language", "zh-cn");
            get.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
            get.setHeader("Accept-Charset", "UTF-8");
            get.setHeader("Keep-Alive", "300");
            get.setHeader("Connection", "Keep-Alive");
            get.setHeader("Cache-Control", "no-cache");
            System.out.println("执行get请求, uri: " + get.getURI());
            httpResponse = httpClient.execute(get);
            // response实体
            HttpEntity entity = httpResponse.getEntity();
            if (null != entity) {
                String response = EntityUtils.toString(entity);
                //response=new String(response.getBytes("ISO-8859-1"),"UTF-8");
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    resultModel.setHttpCode(statusCode);
                    resultModel.setResponseContent(response);
                    return resultModel;
                }
                resultModel.setHttpCode(statusCode);
            }
            resultModel.setResponseContent("HttpEntity is null");
        } catch (IOException e) {
            resultModel.setResponseContent(e.getMessage());
        } finally {
            if (httpResponse != null) {
                try {
                    EntityUtils.consume(httpResponse.getEntity());
                    httpResponse.close();
                } catch (IOException e) {
                    System.out.println("关闭response失败");
                    e.printStackTrace();
                }
            }
        }
        resultModel.setHttpCode(-1);
        return resultModel;
    }
}
