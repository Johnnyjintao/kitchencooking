package com.example.kitchencooking;

import utils.PrefUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;


import cn.sharesdk.tpl.ThreeWayLoginActivity;

import com.example.kitchencooking.R;

/**
 * 闪屏页
 * 
 * @author Kevin
 * 
 */
public class SplashActivity extends Activity {

	RelativeLayout rlRoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		startAnim();
		
//		Thread t = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				SystemClock.sleep(2000);
//			}
//		});
//		
//		t.start();
		
//		jumpNextPage();
	}
		
		
	

	/**
	 *设置开始动画
	 */
	private void startAnim() {

		AnimationSet set = new AnimationSet(false);
		RotateAnimation rotate = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate.setDuration(1000);
		rotate.setFillAfter(true);

		ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scale.setDuration(1000);
		scale.setFillAfter(true);

	
		AlphaAnimation alpha = new AlphaAnimation(0, 88);
		alpha.setDuration(2000);
		alpha.setFillAfter(true);

//		set.addAnimation(rotate);
//		set.addAnimation(scale);
		set.addAnimation(alpha);
		
		

		
		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

	
			@Override
			public void onAnimationEnd(Animation animation) {
				jumpNextPage();
			}
		});

		rlRoot.startAnimation(set);
	}

	/**
	 * 
	 */
	private void jumpNextPage() {
		
		boolean userGuide = PrefUtils.getBoolean(this, "is_user_guide_showed",
				false);
				
		if (!userGuide) {
			startActivity(new Intent(SplashActivity.this, ThreeWayLoginActivity.class));
		} else {
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
		}

		finish();
	}

}
