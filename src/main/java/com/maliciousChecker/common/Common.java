/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maliciousChecker.common;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author santosh
 */
public class Common {

    public static final int READ_TIME_OUT = 8000;
    public static final int CONNECT_TIME_OUT = 15000;
    public static final int IMPORTANT_READ_TIME_OUT = 25000;
    public static final int IMPORTANT_CONNECT_TIME_OUT = 30000;
    public static final String USER_AGENT = "Mozilla/5.0 (compatible; LXRbot/1.0; http://lxrseo.com/, support@lxrseo.com)";

    public static Response getOkHttpResponse(String url) {
        
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(IMPORTANT_CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(IMPORTANT_READ_TIME_OUT, TimeUnit.MILLISECONDS);

        OkHttpClient okHttpClient = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException ex) {
        }
        return response;
    }
    
    
    public static int getStatusCode(String currentUrl, StringBuilder redirectedUrl,
            List<List<String>> cookieContainer) {
        int iteration = 0;
        int responseCode = 0;
        HttpURLConnection currentUrlConn = null;
        while ((responseCode == 0 || responseCode / 100 == 3) && iteration < 5) {
            try {
                URL currentUrlObj = new URL(currentUrl);
                if (!(currentUrlObj.equals(" ") || currentUrlObj.equals("null"))) {
                    currentUrlConn = (HttpURLConnection) currentUrlObj.openConnection();
                    currentUrlConn.setInstanceFollowRedirects(false);
                    currentUrlConn.setConnectTimeout(IMPORTANT_CONNECT_TIME_OUT);
                    currentUrlConn.setReadTimeout(IMPORTANT_READ_TIME_OUT);
                    currentUrlConn.setRequestProperty("User-Agent", USER_AGENT);
                    if (cookieContainer != null && cookieContainer.size() > 0
                            && cookieContainer.get(0) != null && cookieContainer.get(0).size() > 0) {
                        for (String cookie : cookieContainer.get(0)) {
                            currentUrlConn.addRequestProperty("Cookie", cookie);
                        }
                    }
                    responseCode = currentUrlConn.getResponseCode();

                    List<String> tempCookies = currentUrlConn.getHeaderFields().get("Set-Cookie");
                    if (tempCookies != null && tempCookies.size() > 0 && cookieContainer != null) {
                        if (cookieContainer.size() > 0) {
                            cookieContainer.set(0, tempCookies);
                        } else {
                            cookieContainer.add(tempCookies);
                        }
                    }
                    if (responseCode / 100 == 3) {
                        if (iteration != 4) {
                            String redirectedLocation = currentUrlConn.getHeaderField("Location");
                            String redirectedProtocol = currentUrlObj.getProtocol() + "://" + currentUrlObj.getHost();
                            if (redirectedLocation != null) {
                                if (!(redirectedLocation.startsWith("http"))) {
                                    if (redirectedLocation.startsWith("/")) {
                                        currentUrl = redirectedProtocol + redirectedLocation;
                                    } else if (!(redirectedLocation.startsWith("/"))) {
                                        if (currentUrl.endsWith("/")) {
                                            currentUrl = currentUrl + redirectedLocation;
                                        } else {
                                            currentUrl = currentUrl + "/" + redirectedLocation;
                                        }
                                    }
                                } else {
                                    currentUrl = redirectedLocation;
                                }
                            }
                        } else {
                            redirectedUrl.append("");
                        }
                    } else if (responseCode == 200 && iteration != 0) {
                        redirectedUrl.append(currentUrl);
                    }
                }
            } catch (Exception e) {
                if (e.getMessage().contains("PKIX path building failed") || e.getMessage().contains("CertificateException")) {
                    try {
                        sslValidation();
                        getStatusCodeForPKIX(currentUrl, redirectedUrl, cookieContainer);
                    } catch (Exception ex) {
                    }
                }
            }
            iteration++;
        }
        return responseCode;
    }

    public static int getStatusCodeForPKIX(String currentUrl, StringBuilder redirectedUrl,
            List<List<String>> cookieContainer) {
        int iteration = 0;
        int responseCode = 0;
        HttpURLConnection currentUrlConn = null;
        while ((responseCode == 0 || responseCode / 100 == 3) && iteration < 5) {
            try {
                URL currentUrlObj = new URL(currentUrl);
                if (!(currentUrlObj.equals(" ") || currentUrlObj.equals("null"))) {
                    currentUrlConn = (HttpURLConnection) currentUrlObj.openConnection();
                    currentUrlConn.setInstanceFollowRedirects(false);
                    currentUrlConn.setConnectTimeout(IMPORTANT_CONNECT_TIME_OUT);
                    currentUrlConn.setReadTimeout(IMPORTANT_READ_TIME_OUT);
                    currentUrlConn.setRequestProperty("User-Agent", USER_AGENT);
                    if (cookieContainer != null && cookieContainer.size() > 0
                            && cookieContainer.get(0) != null && cookieContainer.get(0).size() > 0) {
                        for (String cookie : cookieContainer.get(0)) {
                            currentUrlConn.addRequestProperty("Cookie", cookie);
                        }
                    }
                    responseCode = currentUrlConn.getResponseCode();

                    List<String> tempCookies = currentUrlConn.getHeaderFields().get("Set-Cookie");
                    if (tempCookies != null && tempCookies.size() > 0 && cookieContainer != null) {
                        if (cookieContainer.size() > 0) {
                            cookieContainer.set(0, tempCookies);
                        } else {
                            cookieContainer.add(tempCookies);
                        }
                    }
                    if (responseCode / 100 == 3) {
                        if (iteration != 4) {
                            String redirectedLocation = currentUrlConn.getHeaderField("Location");
                            String redirectedProtocol = currentUrlObj.getProtocol() + "://" + currentUrlObj.getHost();
                            if (redirectedLocation != null) {
                                if (!(redirectedLocation.startsWith("http"))) {
                                    if (redirectedLocation.startsWith("/")) {
                                        currentUrl = redirectedProtocol + redirectedLocation;
                                    } else if (!(redirectedLocation.startsWith("/"))) {
                                        if (currentUrl.endsWith("/")) {
                                            currentUrl = currentUrl + redirectedLocation;
                                        } else {
                                            currentUrl = currentUrl + "/" + redirectedLocation;
                                        }
                                    }
                                } else {
                                    currentUrl = redirectedLocation;
                                }
                            }
                        } else {
                            redirectedUrl.append("");
                        }
                    } else if (responseCode == 200 && iteration != 0) {
                        redirectedUrl.append(currentUrl);
                    }
                }
            } catch (Exception e) {
            }
            iteration++;
        }
        return responseCode;
    }
    
    public static void sslValidation() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException ex) {
        } catch (KeyManagementException ex) {
        }

        if (sc != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }

//        Create all-trusting host name verifier
//        HostnameVerifier allHostsValid = new HostnameVerifier() {
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        };
//
//        Install the all-trusting host verifier
//        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}
