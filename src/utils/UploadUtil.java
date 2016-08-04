package utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import android.text.Editable;
import android.widget.Toast;

import com.example.kitchencooking.MessageActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

public class UploadUtil {
	public static void UploadUtil(String path,String i,String s, String url) throws Exception  {  
		File file = new File(path);  
		if (file.exists() && file.length() > 0) {
		    AsyncHttpClient client = new AsyncHttpClient();  
		    RequestParams params = new RequestParams();
		    
		    params.put("uploadfile", file);
		    params.put("i", i);
		    params.put("s", s);
		    // 上传文件  
		    client.post(url, params, new AsyncHttpResponseHandler() {  

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					System.out.println("失败");
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					System.out.println("成功");
					
				}
		  
		    });  
		} else {  
//		    Toast.makeText(mContext, "文件不存在", Toast.LENGTH_LONG).show();  
			System.out.println("文件不存在");
		}  
}
	
	public static void Uploadtext(String text,String i,String s,String url){
		if (text != null || text.length()<=20) {
			AsyncHttpClient client = new AsyncHttpClient();  
		    RequestParams params = new RequestParams();
		    params.put("username", text);
		    params.put("i", i);
		    params.put("s", s);
		    params.put("sid", MessageActivity.sid.get("sid"));
		    client.post(url, params, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					System.out.println("请求修改用户名成功");
					System.out.println("arg0"+arg0);
					System.out.println("arg1"+arg1.toString());
					System.out.println("arg1"+arg2.toString());
					
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					System.out.println("请求修改用户名失败");
				}
			});
		}
	}
	
	
	public static void UploadCancel(String url){
			AsyncHttpClient client = new AsyncHttpClient();  
		    RequestParams params = new RequestParams();
		    client.post(url, params, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					System.out.println("注销成功");
					
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					System.out.println("注销失败");
				}
			});
	}
}
