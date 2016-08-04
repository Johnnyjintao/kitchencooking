package com.example.kitchencooking;

import Fragment.attentionFragment;
import Fragment.fansFragment;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class AttentionActivity extends Activity{
	private TextView tv_fans;
	private TextView tv_atten;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_attention);
		initview();
		
	}
	
	
	private void initview() {
		tv_fans = (TextView) findViewById(R.id.tv_fans);
		tv_atten = (TextView) findViewById(R.id.tv_atten);
		
		fansFragment fs = new fansFragment();
	    	//获取fragment管理器
	    	FragmentManager fm = getFragmentManager();
	    	//打开事务
	    	FragmentTransaction ft = fm.beginTransaction();
	    	//把内容显示至帧布局
	    	ft.replace(R.id.fl, fs);
	    	//提交
	    	ft.commit();
		
		
		tv_fans.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				tv_fans.setBackgroundResource(R.drawable.tab_left_pressed);
				tv_atten.setBackgroundResource(R.drawable.tab_right_default);
				//把fragment01的界面显示至帧布局中
		    	//创建fragment对象
		    	fansFragment fs = new fansFragment();
		    	//获取fragment管理器
		    	FragmentManager fm = getFragmentManager();
		    	//打开事务
		    	FragmentTransaction ft = fm.beginTransaction();
		    	//把内容显示至帧布局
		    	ft.replace(R.id.fl, fs);
		    	//提交
		    	ft.commit();
				
			}
		});
		
		tv_atten.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				tv_fans.setBackgroundResource(R.drawable.tab_left_default);
				tv_atten.setBackgroundResource(R.drawable.tab_right_pressed);
				//把fragment01的界面显示至帧布局中
		    	//创建fragment对象
		    	attentionFragment fs = new attentionFragment();
		    	//获取fragment管理器
		    	FragmentManager fm = getFragmentManager();
		    	//打开事务
		    	FragmentTransaction ft = fm.beginTransaction();
		    	//把内容显示至帧布局
		    	ft.replace(R.id.fl, fs);
		    	//提交
		    	ft.commit();
				
			}
		});
	}
	
}
