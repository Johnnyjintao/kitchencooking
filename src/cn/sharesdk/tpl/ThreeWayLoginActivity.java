package cn.sharesdk.tpl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;

import utils.PrefUtils;

import com.example.kitchencooking.MainActivity;
import com.example.kitchencooking.R;
import com.sun.istack.internal.Nullable;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by ybwang on 2016/5/25.
 */
public class ThreeWayLoginActivity extends Activity implements View.OnClickListener {

    private TextView tvMsgRegister, tvWeixin, tvWeibo, tvQq;
    private Context mContext;
    private static final int MSG_SMSSDK_CALLBACK = 1;
    private static final int MSG_AUTH_CANCEL = 2;
    private static final int MSG_AUTH_ERROR = 3;
    private static final int MSG_AUTH_COMPLETE = 4;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUTH_COMPLETE:
                    Toast.makeText(mContext, "授权成功正在跳转", Toast.LENGTH_LONG).show();
                    
                    PrefUtils.setBoolean(ThreeWayLoginActivity.this,"is_user_guide_showed", true);
                    
                    Intent intent = new Intent(ThreeWayLoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case MSG_AUTH_ERROR:
                    Toast.makeText(mContext, "授权失败", Toast.LENGTH_LONG).show();
                    break;
                case MSG_AUTH_CANCEL:
                    Toast.makeText(mContext, "授权已经被取消", Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tpl_login_page);
        mContext = this;
        initView();
        initSDK(mContext);
    }

    private void initSDK(Context mContext) {
        ShareSDK.initSDK(mContext);
    }

    private void initView() {
        tvWeixin = (TextView) findViewById(R.id.tvWeixin);
        tvWeibo = (TextView) findViewById(R.id.tvWeibo);
        tvQq = (TextView) findViewById(R.id.tvQq);
       
        tvQq.setOnClickListener(this);
        tvWeibo.setOnClickListener(this);
        tvWeixin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvQq:
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                sqLogin(qq);
                break;
            case R.id.tvWeibo:
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                sqLogin(weibo);
                break;
            case R.id.tvWeixin:
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                sqLogin(wechat);
                break;

        }
    }

    private void sqLogin(Platform platform) {
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                if (i == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_COMPLETE;
                    msg.obj = new Object[]{platform.getName(), hashMap};
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                if (i == Platform.ACTION_USER_INFOR) {
                    handler.sendEmptyMessage(MSG_AUTH_ERROR);
                }
            }

            @Override
            public void onCancel(Platform platform, int i) {
                if (i == Platform.ACTION_USER_INFOR) {
                    handler.sendEmptyMessage(MSG_AUTH_CANCEL);
                }

            }
        });
        platform.SSOSetting(true);
        platform.showUser(null);
    }
}
