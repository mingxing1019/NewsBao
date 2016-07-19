package com.sharp.rssreader;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
/**Not use**/
public class HttpUtils {
    private static String TAG = "HttpUtils";
    
    public static InputStream httpMethod(String path, String encode) {
        HttpClient httpClient = new DefaultHttpClient();
        Log.d(TAG,"httpClient->" + httpClient);
        Log.d(TAG,"path->" + path);
        try {
            HttpPost httpPost = new HttpPost(path);
            HttpGet httpGet = new HttpGet(path);
            //HttpUriRequest 
            Log.d(TAG,"httpPost->" + httpPost);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            Log.d(TAG,"httpResponse->" + httpResponse);
            Log.d(TAG,"httpResponse.getStatusLine().getStatusCode()->" + httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity();
                Log.d(TAG,"httpEntity->" + httpEntity);
                return httpEntity.getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
            Log.d(TAG,"httpClient.getConnectionManager().shutdown");
        }
        Log.d(TAG,"HttpMethod return null");
        return null;
    }
}
