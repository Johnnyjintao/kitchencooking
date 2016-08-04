package com.example.kitchencooking;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import utils.IoUtils;
import utils.PrefUtils;
import utils.UploadUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tpl.ThreeWayLoginActivity;

import com.lidroid.xutils.BitmapUtils;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class MessageActivity extends Activity{
	private TextView tvMename;
	private ImageView ivMeicon;
	private List<String> list;
	private Bitmap bm;//头像Bitmap
	private int mWH = 90;// 单位dp
	private String imageFilePath;
	private static String path="/sdcard/myHead/";//sd路径
	
	private String s;
	private String i;
	private BitmapUtils bu;
	
	Handler handler = new Handler() { 

		@Override 

        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String result=(String)msg.obj; 
            System.out.println(result);
            
            JSONArray resultArray;
			try {
				resultArray = new JSONArray(result);
				JSONObject resultObj = resultArray.optJSONObject(0);
				String name = resultObj.getString("username");
				i = resultObj.getString("i");
				s = resultObj.getString("s");
				
				bu = new BitmapUtils(MessageActivity.this);

				
//				
				bu.display(ivMeicon, "http://10.0.1.201:8086/"+i+"."+s+"/avatar");
				
				tvMename.setText(name);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

        }

    };
    
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ShareSDK.initSDK(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_message);
		
		Platform qq = ShareSDK.getPlatform(MessageActivity.this, QQ.NAME);
		String nm = qq.getDb().get("nickname");
		if (!nm.isEmpty()) {
			System.out.println("QQ");
			getQQInfo();
		}else{
			System.out.println("微博");
			getWeiboInfo();
		}
		initView();
	}
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bu!= null) {
			bu.clearCache();
		}
	}
	
	private void initView(){
		tvMename = (TextView) findViewById(R.id.tv_mename);
		ivMeicon = (ImageView) findViewById(R.id.iv_meicon);
		
	}
	
	public void cancel(View v){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("退出当前账号");
		builder.setMessage("您确定退出当前账号吗？");
		builder.setPositiveButton("确认退出", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Platform qq = ShareSDK.getPlatform(MessageActivity.this, QQ.NAME);
				if (qq.isAuthValid()) {
					qq.removeAccount();
					UploadUtil.UploadCancel("http://10.0.1.201:8086/Logout?i="+i);
				}
				
				Intent intent = new Intent(MessageActivity.this, ThreeWayLoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				
				PrefUtils.setBoolean(MessageActivity.this,"is_user_guide_showed", false);
			}
		});
		
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.show();
	}
	
	
	
	public void rluser(View v) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("修改用户名");
		builder.setMessage("请控制在2-20个字符，仅允许使用汉字、英文、数字。");
		//创建一个EditText对象设置为对话框中显示的View对象
		final EditText ed = new EditText(MessageActivity.this);
		ed.setHint("请输入新用户名");
		builder.setView(ed);
		
		//用户选好要选的选项后，点击确定按钮
		builder.setPositiveButton("确认修改", new OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	
		        	String text = ed.getText().toString();
		        	
		        	if (text.length()>20 &&text.length()==0) {
						Toast.makeText(MessageActivity.this, "您输入的用户名过长，请重新输入", 0).show();
					}
		        	
		        	
		        	UploadUtil.Uploadtext(text,i,s, "http://10.0.1.201:8086/UpdateUser");
		        	
		        	tvMename.setText(text);
		        }
		});
		// 取消选择
		builder.setNegativeButton("取消", new OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	
		        }
		});
		builder.show();
	}
	
	
	
	public void ivImage(View v) {
		AlertDialog.Builder builder = new Builder(this);
		//定义列表中的选项
		final String[] items = new String[]{
		        "从手机相册选择",
		        "拍照",
		};
		//设置列表选项
		builder.setItems(items, new OnClickListener() {
		        

				//点击任何一个列表选项都会触发这个方法
		        //arg1：点击的是哪一个选项
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	if (which == 0) {
						System.out.println("从手机相册选择");
						Intent intent1 = new Intent(Intent.ACTION_PICK, null);
						//设定文件选择的类型为图像类型
			            intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			            startActivityForResult(intent1, 1);
						
					}else if (which == 1) {
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 请求拍照的Action
						// 在onActivityResult中处理拍照结果
						startActivityForResult(intent, 2);
					}
		        	
		        }
		});
		
		// 取消选择
		builder.setNegativeButton("取消", new OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	
		        }
		});
		builder.show();
	}
	
	
	
    //第一个参数为请求码，即调用startActivityForResult()传递过去的值
    //第二个参数为结果码，结果码用于标识返回数据来自哪个新Activity
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && data != null)
		{
			Uri uri = data.getData();
			Bitmap bitmap = null;
			if (uri != null)
			{
				String imgPath = null;
				ContentResolver resolver = this.getContentResolver();
				String[] columns = { MediaStore.Images.Media.DATA };//查询条件
				Cursor cursor = null;
				cursor = resolver.query(uri, columns, null, null, null);
				if (Build.VERSION.SDK_INT > 18)// 4.4以后文件选择发生变化，判断处理
				{
					if (requestCode == 1)// 选择图片
					{
						imgPath = uri.getPath();
						System.out.println("imgPath1"+imgPath);
						if (!TextUtils.isEmpty(imgPath)&& imgPath.contains(":"))
						{
							String imgIndex = imgPath.split(":")[1];
							cursor = resolver.query(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								columns, "_id=?", new String[] { imgIndex },
								null);
						}
					}
				}
				if (null != cursor && cursor.moveToFirst())
				{
					int columnIndex = cursor.getColumnIndex(columns[0]);
					imgPath = cursor.getString(columnIndex);
					cursor.close();
					System.out.println("imgPath2"+imgPath);
				}
				if (!TextUtils.isEmpty(imgPath))
				{
					bitmap = genBitmap(imgPath);//通过给定的图片路径生成对应的bitmap
					ivMeicon.setImageBitmap(bitmap);//设置图片
					try {
						UploadUtil.UploadUtil(imgPath,i,s, "http://10.0.1.201:8086/UpdateUser");//访问服务器
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
			else if (requestCode == 2)// 拍照
			{
				// 拍照时，注意小米手机不会保存图片到本地，只可以从intent中取出bitmap, 要特殊处理
				Object object = data.getExtras().get("data");
				if (null != object && object instanceof Bitmap)
				{
					bitmap = (Bitmap) object;
					ivMeicon.setImageBitmap(bitmap);
					
				}
			}
			if (null != bitmap)
			{
				FileOutputStream fileOutputStream = null;
			    try {
			        // 获取 SD 卡根目录
			        String saveDir = Environment.getExternalStorageDirectory() + "/meitian_photos";
			        // 新建目录
			        File dir = new File(saveDir);
			        if (! dir.exists()) dir.mkdir();
			        // 通过拿到拍照的时间生成文件名
			        SimpleDateFormat t = new SimpleDateFormat("yyyyMMddssSSS");
			        String filename = "MT" + (t.format(new Date())) + ".jpg";
			        // 新建文件
			        File file = new File(saveDir, filename);
			        // 打开文件输出流
			        fileOutputStream = new FileOutputStream(file);
			        // 生成图片文件
			        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
			        
			        // 相片的完整路径
			        String imgpath = file.getPath();
			        
			        System.out.println(imgpath);
			        System.out.println(imgpath);
			        System.out.println(imgpath);
			        System.out.println(imgpath);
			        System.out.println(imgpath);
			        
			        UploadUtil.UploadUtil(imgpath, i, s, "http://10.0.1.201:8086/UpdateUser");
			        
			        
			    } catch (Exception e) {
			        e.printStackTrace();
			    } finally {
			        if (fileOutputStream != null) {
			            try {
			                fileOutputStream.close();
			            } catch (Exception e) {
			                e.printStackTrace();
			            }
			        }
			    }
			}
			
		}
    }
	

	
	
	/**通过给定的图片路径生成对应的bitmap*/
	private Bitmap genBitmap(String imgPath)
	{
		BitmapFactory.Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgPath, options);
		int imageWidth = options.outWidth;
		int imageHeight = options.outHeight;

		int widthSample = (int) (imageWidth / mWH);
		int heightSample = (int) (imageHeight / mWH);
		// 计算缩放比例
		options.inSampleSize = widthSample < heightSample ? heightSample
			: widthSample;
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(imgPath, options);
	}
	
	
	
	public synchronized void getInfo2(final String U_D){
		Thread t1 = new Thread(){

			@Override
			public void run() {
				//提交的数据需要经过url编码，英文和数字编码后不变
				@SuppressWarnings("deprecation")
				String path = "http://10.0.1.201:8086/UserDisplay";
				try {
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					
					//拼接出要提交的数据的字符串
					String data = "U_D=" + U_D ;
					
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
						 System.out.println("2::::::"+text);
						 
						 JSONArray resultArray = new JSONArray(text);
						 JSONObject resultObj = resultArray.optJSONObject(0);
						 String i = resultObj.getString("i");
						 String s = resultObj.getString("s");
						 String username = resultObj.getString("username");
						 
						 Message msg = Message.obtain();

						 msg.obj = text;

						 handler.sendMessage(msg);
					}
					
					

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t1.start();
	}
	public static final Map<String, String> sid= new HashMap<String, String>();
	public synchronized void getQQInfo(){
		Platform qq = ShareSDK.getPlatform(MessageActivity.this, QQ.NAME);
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
					System.out.println(openId);
					System.out.println(openId);
					System.out.println(openId);
					System.out.println(openId);
					System.out.println(openId);
					System.out.println(openId);
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
						sid.put("sid", user);
						getInfo2(user);
						System.out.println("1::user::::"+user);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
	
	
	private void getWeiboInfo() {
		Platform weibo = ShareSDK.getPlatform(MessageActivity.this, SinaWeibo.NAME);
		final String WBnickname = weibo.getDb().get("nickname");
		final String WBpicture = weibo.getDb().getUserIcon();
		final String WBopenId = weibo.getDb().getUserId();
		
		
		Thread t = new Thread(){
			@Override
			public void run() {
				//提交的数据需要经过url编码，英文和数字编码后不变
				@SuppressWarnings("deprecation")
				String path = "http://10.0.1.201:8086/WeiBoLogin";
				try {
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					
					//拼接出要提交的数据的字符串
//					final String pic = picture.replace(picture.charAt(picture.length()-2)+"", "10");
					String data = "openId=" + WBopenId + "&picture=" + WBpicture + "&name=" + URLEncoder.encode(WBnickname);
					System.out.println(WBopenId);
					System.out.println(WBpicture);
					System.out.println(WBnickname);
					
					System.out.println(WBopenId);
					System.out.println(WBpicture);
					System.out.println(WBnickname);
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
						sid.put("sid", user);
						getInfo2(user);
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
