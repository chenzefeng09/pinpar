package com.ipinpar.app.network.api;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.ContentBody;
import internal.org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.Listener;

public class UploadActivityImgRequest{
 
    private Response.Listener mListener;
    private HttpEntity mEntity;
 
    public UploadActivityImgRequest(final int uid,final File fileBodyParamMap,final Response.Listener listener, Response.ErrorListener errorListener) {
 
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					post(uid, fileBodyParamMap.getAbsolutePath(),listener);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}}).start();;
       
    }
    
    public String post(int uid,String pathToOurFile,Listener<JSONObject> listener) throws ClientProtocolException, IOException, JSONException {
        HttpClient httpclient = new DefaultHttpClient();
        //设置通信协议版本
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
         
        //File path= Environment.getExternalStorageDirectory(); //取得SD卡的路径
         
        //String pathToOurFile = path.getPath()+File.separator+"ak.txt"; //uploadfile
        //String urlServer = "http://192.168.1.88/test/upload.php"; 
         
        HttpPost httppost = new HttpPost("http://api.ipinpar.com/pinpaV2/active/uploadImage.jsp");
        File file = new File(pathToOurFile);
     
        MultipartEntity mpEntity = new MultipartEntity(); //文件传输
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("file", cbFile); // <input type="file" name="file" />  对应的
     
     
        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
         
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
     
        System.out.println(response.getStatusLine());//通信Ok
        String json="";
        String path="";
        if (resEntity != null) {
          //System.out.println(EntityUtils.toString(resEntity,"utf-8"));
          json=EntityUtils.toString(resEntity,"utf-8");
          JSONObject p=null;
          try{
              p=new JSONObject(json);
              Log.e("result", json);
              int result = p.getInt("result");
              listener.onResponse(p);
          }catch(Exception e){
              e.printStackTrace();
          }
        }
        if (resEntity != null) {
          resEntity.consumeContent();
        }
     
        httpclient.getConnectionManager().shutdown();
        return path;
      }
}
