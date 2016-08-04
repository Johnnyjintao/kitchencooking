package Fragment;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

import com.example.kitchencooking.AttentionActivity;
import com.example.kitchencooking.MessageActivity;
import com.example.kitchencooking.R;
import com.example.kitchencooking.ShoppingActivity;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 主页fragment
 * @author john
 *
 */
public class MoreFragment extends Fragment {
	

	private TextView tv_shopping;
	
	private View v;


	private TextView tv_denglu,tv_attention;


	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		v = inflater.inflate(R.layout.activity_more, container,false);
		
		initView();
		
		return v;
	}

	private void initView() {
		tv_denglu = (TextView) v.findViewById(R.id.tv_denglu);
		tv_attention = (TextView) v.findViewById(R.id.tv_attention);
		tv_shopping = (TextView) v.findViewById(R.id.tv_shopping);
		
		tv_shopping.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					Intent intent = new Intent(getActivity(),ShoppingActivity.class);
					startActivity(intent);
				}
		});
		
		tv_attention.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),AttentionActivity.class);
				startActivity(intent);
			}
		});
		
		
		tv_denglu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					Intent intent = new Intent(getActivity(),MessageActivity.class);
					startActivity(intent);
				}
		});
		
		
//		qq = ShareSDK.getPlatform(getActivity(), QQ.NAME);
//		nickname = qq.getDb().get("nickname");
//		
//		tv_name.setText(nickname);
		

	}
}
	
	 

	
