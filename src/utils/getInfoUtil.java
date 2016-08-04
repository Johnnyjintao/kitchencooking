package utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;

import android.content.Context;
import android.os.Message;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

import com.example.kitchencooking.MessageActivity;

public class getInfoUtil {
	
	public synchronized void getQQInfo(Context con){
		Platform qq = ShareSDK.getPlatform(con, QQ.NAME);
		final String nickname = qq.getDb().get("nickname");
		final String picture = qq.getDb().getUserIcon();
		final String openId = qq.getDb().getUserId();
		Thread t = new Thread(){
			@Override
			public void run() {
				//提交的数据需要经过url编码，英文和数字编码后不变
				@SuppressWarnings("deprecation")
				String path = "http://10.0.1.201:8086/QqzonLogin";
				try {
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					
					//拼接出要提交的数据的字符串
//					final String pic = picture.replace(picture.charAt(picture.length()-2)+"", "10");
					String substring = picture.substring(0,picture.length()-2);
					String pic = substring+"100";
					String data = "openId=" + openId + "&picture=" + pic + "&name=" + URLEncoder.encode(nickname);
					
					//添加post请求的两行属性
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					conn.setRequestProperty("Content-Length", data.length() + "");
					
					//设置打开输出流
					conn.setDoOutput(true);
					
					//拿到输出流
					OutputStream os = conn.getOutputStream();
					//使用输出流往服务器提交数据
					os.write(data.getBytes());
					if(conn.getResponseCode() == 200){
						InputStream is = conn.getInputStream();
						String text = IoUtils.getTextFromStream(is);
						JSONArray jsonArray = new JSONArray(text);  
						System.out.println("session:"+jsonArray.get(0));
						
						String user = jsonArray.get(0).toString();
						
						System.out.println("1::user::::"+user);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
}
