package site.easytobuild.multipurpose.jenkins;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;


public class Jenkins {

    private final String projectName;

    private final String host;

    private final String userName;

    private final String token;
    private final String projectType;
    private final String databaseName;
    private final String sqlName;
    private final String sqlPassword;
    private final String tomcatDir;
    private final String clientId;
    private final String clientSecret;


    public Jenkins(String host, String userName, String token, String projectName, String projectType, String databaseName, String sqlName, String sqlPassword, String tomcatDir, String clientId, String clientSecret) {
        this.host = host;
        this.userName = userName;
        this.token = token;
        this.projectName = projectName;
        this.projectType = projectType;
        this.databaseName = databaseName;
        this.sqlName = sqlName;
        this.sqlPassword = sqlPassword;
        this.tomcatDir = tomcatDir;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }


    public void buildDatabaseProject(String fullConfigPath) {
        String itemCreationLink = createItemCreationLink(databaseName);
        String cmdGetDocId = "\n" +
                "curl -X POST " + itemCreationLink + " --user " + userName + ":" + token + " --data-binary " + fullConfigPath + " -H \"Content-Type:text/xml\"";
        try {
            Process process = Runtime.getRuntime().exec(cmdGetDocId);
            int exitCode = process.waitFor();
            if(exitCode == 0) {
                String buildLink = createDatabaseBuildLink();
                startBuildingProcess(buildLink);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void rebuildExistedDatabaseProject() {
        String buildLink = createDatabaseBuildLink();
        startBuildingProcess(buildLink);
    }
    public void buildSpringMavenProject(String fullConfigPath) {
        String itemCreationLink = createItemCreationLink(projectName);
        String cmdGetDocId = "\n" +
                "curl -X POST " + itemCreationLink + " --user " + userName + ":" + token + " --data-binary " + fullConfigPath + " -H \"Content-Type:text/xml\"";
        try {
            Process process = Runtime.getRuntime().exec(cmdGetDocId);
            int exitCode = process.waitFor();
            if(exitCode == 0) {
                String buildLink = getBuildLink();
                startBuildingProcess(buildLink);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void rebuildExistedSpringMavenProject() {
        String buildLink = getBuildLink();
        startBuildingProcess(buildLink);
    }
    public void startBuildingProcess(String buildLink) {
//        String buildLink = createBuildLink();
        URI uri = URI.create(buildLink);
        HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
                new UsernamePasswordCredentials(userName, token));
        AuthCache authCache = new BasicAuthCache();
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(host, basicAuth);

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
        HttpPost httpPost = new HttpPost(uri);
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setAuthCache(authCache);

        try {
            httpClient.execute(host, httpPost, localContext);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getBuildLink() {
        String encodedTomcatDir = null;
        try {
            encodedTomcatDir = URLEncoder.encode(tomcatDir, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String hostUrl = host + "/job/" + projectName + "/buildWithParameters";
        String url = String.format(hostUrl+"?BUSINESS_TYPE=%s&BUSINESS_NAME=%s&DATABASE_NAME=%s&SQL_NAME=%s&SQL_PASSWORD=%s&TOMCAT_DIR=%s&CLIENT_ID=%s&CLIENT_SECRET=%s",
                projectType, projectName, databaseName, sqlName, sqlPassword, encodedTomcatDir, clientId, clientSecret);

        return url;
    }

    private String createDatabaseBuildLink() {
        return host + "/job/" + databaseName + "/buildWithParameters?DATABASE_NAME="+databaseName+"&SQL_NAME="+sqlName+"&SQL_PASSWORD="+sqlPassword;
    }
    private String createItemCreationLink(String name) {
        return host + "/createItem?name=" + name;
    }

    private int getBuildProgress(String buildUrl) throws Exception {
        // Configure HTTP client with Jenkins credentials
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, token));
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();

        // Send HTTP GET request to fetch build information
        HttpGet httpGet = new HttpGet(buildUrl + "/api/json");
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        // Read the response and extract the build progress
        String responseString = EntityUtils.toString(entity);
        JSONObject jsonResponse = new JSONObject(responseString);
        int progress = jsonResponse.getJSONObject("executor").getInt("progress");

        // Cleanup resources
        EntityUtils.consume(entity);
        httpClient.close();

        return progress;
    }

}
