package com.example.kitchencooking;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;


import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;

import view.ViewPagerScroller;

import Fragment.FolloingFragment;
import Fragment.FragmentAdapter;
import Fragment.MoreFragment;
import Fragment.OverViewFragment;
import Fragment.PersonalFragment;
import Fragment.SearchFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private ViewPager mPageVp;

	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private FragmentAdapter mFragmentAdapter;

	/**
	 * ImageView
	 */
	private ImageView ivUser, ivBack, ivSearch,ivMore,ivCircle;
	/**
	 * Fragment
	 */
	private FolloingFragment mFollowFg;
	private PersonalFragment mPersonFg;
	private SearchFragment mSearchFg;
	private OverViewFragment mOverViewFg;
	private MoreFragment mMoreFg;
	private String mPath;
	private String mOwner;
	private String mText;
	private String mName;
	private long exitTime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//透明状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		//透明导航栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

		findById();
		init();
	}
	
	

	//再按一次退出程序的方法
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-exitTime) > 2000){  
	            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
	            exitTime = System.currentTimeMillis();   
	        } else {
	            finish();
	            System.exit(0);
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}
	

	public String getmPath() {
		return mPath;
	}

	public void setmPath(String mPath) {
		this.mPath = mPath;
	}

	public String getmOwner() {
		return mOwner;
	}

	public void setmOwner(String mOwner) {
		this.mOwner = mOwner;
	}

	public String getmText() {
		return mText;
	}

	public void setmText(String mText) {
		this.mText = mText;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}




	private void findById() {
		ivUser = (ImageView) this.findViewById(R.id.iv_user);
		ivBack = (ImageView) this.findViewById(R.id.iv_back);
		ivSearch = (ImageView) this.findViewById(R.id.iv_searchone);
		ivMore = (ImageView) this.findViewById(R.id.iv_list);
		ivCircle = (ImageView) this.findViewById(R.id.iv_circle);

		mPageVp = (ViewPager) this.findViewById(R.id.id_page_vp);
		
		
		
		
		ivUser.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mPageVp.getCurrentItem()==0) {
					mPageVp.setCurrentItem(1);
					ivUser.setImageResource(R.drawable.home);
				}else if (mPageVp.getCurrentItem()==1) {
					mPageVp.setCurrentItem(0);
					ivUser.setImageResource(R.drawable.user);
				}else {
					mPageVp.setCurrentItem(0);
					ivUser.setImageResource(R.drawable.user);
				}
				
			}
		});
		
		
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mPageVp.getCurrentItem()==0) {
					finish();
				}else if (mPageVp.getCurrentItem()==1) {
					mPageVp.setCurrentItem(0);
					ivUser.setImageResource(R.drawable.user);
				}else {
					mPageVp.setCurrentItem(0);
				}
			}
		});
		
		mPageVp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			//页卡改变监听
			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (mPageVp.getCurrentItem()==2) {
					ivSearch.setVisibility(View.GONE);
				}else if (mPageVp.getCurrentItem()==3) {
					ivSearch.setVisibility(View.GONE);
					ivMore.setVisibility(View.GONE);
				}else{
					ivSearch.setVisibility(View.VISIBLE);
					ivMore.setVisibility(View.VISIBLE);
				}
			}
		});
		
		ivSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					mPageVp.setCurrentItem(2);
			}
		});
		
		ivMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPageVp.setCurrentItem(3);
			}
		});
		
		ivCircle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showShare();
			}
		});

	}

	private void init() {
		mFollowFg = new FolloingFragment();
		mPersonFg = new PersonalFragment();
		mSearchFg = new SearchFragment();
		mMoreFg = new MoreFragment();
		mOverViewFg = new OverViewFragment();
		
		mFragmentList.add(mFollowFg);
		mFragmentList.add(mPersonFg);
		mFragmentList.add(mSearchFg);
		mFragmentList.add(mMoreFg);
		mFragmentList.add(mOverViewFg);

		mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
		mPageVp.setAdapter(mFragmentAdapter);
		mPageVp.setCurrentItem(0);
		
	}
	
	
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 
		 
		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//		 oks.setTitle(getString(R.string.share));
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("我是分享文本");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://sharesdk.cn");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://sharesdk.cn");
		 
		// 启动分享GUI
		 oks.show(this);
		 }

}
