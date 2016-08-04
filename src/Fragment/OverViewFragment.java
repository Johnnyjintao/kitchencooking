package Fragment;

import java.io.File;

import com.example.kitchencooking.CircleImageView;
import com.example.kitchencooking.MainActivity;
import com.example.kitchencooking.R;
import com.example.kitchencooking.ShoppingActivity;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import db.SqlHelper;
import domain.Menu;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 主页fragment
 * @author john
 *
 */
public class OverViewFragment extends Fragment {
	

	private WebView mWebView;

	private ImageView iv_splist;
	private ImageView iv_plus;
	private String path;
	private String owner;
	private String text;
	private SQLiteDatabase db;
	private String name;
	private View v;

	private ProgressBar pb_progress;

	private TextView tv_bt;


	

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		v = inflater.inflate(R.layout.activity_overview, container,false);
		initView();
		return v;
	}

	private void initView() {
		mWebView = (WebView) v.findViewById(R.id.wv_web);
		iv_splist = (ImageView) v.findViewById(R.id.iv_splist);
		iv_plus = (ImageView) v.findViewById(R.id.iv_plus);
		pb_progress = (ProgressBar) v.findViewById(R.id.pb_progress);
		tv_bt = (TextView) v.findViewById(R.id.tv_bt);
		
		path = ((MainActivity) getActivity()).getmPath();
		owner = ((MainActivity) getActivity()).getmOwner();
		text = ((MainActivity) getActivity()).getmText();
		name = ((MainActivity) getActivity()).getmName();
		
		//设置从主页面传过来的标题
		tv_bt.setText(text);
		
		CircleImageView iv_overhead = (CircleImageView) v.findViewById(R.id.iv_overhead);
		ImageView iv_over = (ImageView) v.findViewById(R.id.iv_over);
		BitmapUtils bu = new BitmapUtils(getActivity());
		bu.display(iv_overhead, owner);
		bu.display(iv_over, path);
		
		mWebView.loadUrl("http://10.0.1.201:8086/detail");
		
		
		mWebView.setWebViewClient(new WebViewClient(){
			//网页开始加载
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				pb_progress.setVisibility(view.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}
			
			//网页加载结束
			@Override
			public void onPageFinished(WebView view, String url) {
				pb_progress.setVisibility(view.GONE);
				super.onPageFinished(view, url);
			}
			
			
			
			/**
			 * 所有跳转的链接都会在此方法中回调
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				
				view.loadUrl(url);
				return true;
			}
		});
		
		
		
		
		
		iv_plus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
					Toast.makeText(getActivity(), "收藏成功!",0).show();
					
					SqlHelper sqlHelper = new SqlHelper(getActivity());
					ContentValues values = new ContentValues();
					values.put("text",text);
					values.put("name", name);
					values.put("path",path);
					values.put("item",owner);
					sqlHelper.insert(values,text);
					
					HttpUtils utils = new HttpUtils();
					String url = path;
					utils.download(url, "sdcard/downloadimage/"+text+"1.jpg", new RequestCallBack<File>() {
						
						@Override
						public void onSuccess(ResponseInfo<File> arg0) {
							System.out.println("1111111111111");
						}
						
						@Override
						public void onFailure(HttpException arg0, String arg1) {
						}
					});
					String url2 = owner;
					utils.download(url2, "sdcard/downloadimage/"+text+"2.jpg", new RequestCallBack<File>() {
						
						@Override
						public void onSuccess(ResponseInfo<File> arg0) {
							System.out.println("2222222222");
						}
						
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							
						}
					});
				}
				
				
				
		});
		
		iv_splist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),ShoppingActivity.class);
				startActivity(intent);
			}
		});
		
		
		
	}
		
	}
	
	 

	
