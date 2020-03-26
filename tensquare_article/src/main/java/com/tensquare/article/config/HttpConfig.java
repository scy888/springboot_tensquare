package com.tensquare.article.config;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.config
 * @date: 2020-03-19 21:01:19
 * @describe:
 */
public class HttpConfig {
    private String hostUrl;
    private int connectTimeout;
    private int readTimeout;

    public HttpConfig() {
    }

    public HttpConfig(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
