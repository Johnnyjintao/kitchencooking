package Fragment;

import java.util.ArrayList;
import java.util.List;

import utils.CacheUtils;
import view.RefreshListView.OnRefreshListener;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kitchencooking.MainActivity;
import com.example.kitchencooking.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import domain.Menu;

/**
 * 主页fragment
 * @author john
 *
 */
public class FolloingFragment extends Fragment {
	
	private view.RefreshListView lv_list;
	private String path;
	private MyAdapter madapter;
	
	private View v;
	private List<Menu> menuList ;
	private ViewPager mPageVp;
	
	
	private View headerView2;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		v = inflater.inflate(R.layout.activity_following, container,false);
		madapter = new MyAdapter();
		menuList = new ArrayList<Menu>();
		lv_list = (view.RefreshListView) v.findViewById(R.id.lv_list);
		
		
		// 设置下拉刷新监听
		lv_list.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				getMenuInfo();
				madapter.notifyDataSetChanged();
			}

			@Override
			public void onLoadMore() {
			}
		});
		
		

		View headerView = inflater.inflate(R.layout.table_title, null);
		headerView2 = inflater.inflate(R.layout.activity_arror, null);
		lv_list.addHeaderView(headerView);
		
		lv_list.addHeaderView(headerView2);
		headerView2.measure(0, 0);//对控件进行测量
		measuredHeight = headerView2.getMeasuredHeight();
		headerView2.setPadding(0, -measuredHeight, 0, 0);
		
		
		lv_list.setAdapter(madapter);
		
		mPageVp = (ViewPager) getActivity().findViewById(R.id.id_page_vp);
		
		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position==1) {
					System.out.println("啦啦啦没有点击事件哦");
				}else {
					String path = menuList.get(position-3).getPath();
					String owner = menuList.get(position-3).getOwner();
					String text = menuList.get(position-3).getText();
					String name = menuList.get(position-3).getName();
					
				    ((MainActivity) getActivity()).setmPath(path);
				    ((MainActivity) getActivity()).setmOwner(owner);
				    ((MainActivity) getActivity()).setmText(text);
				    ((MainActivity) getActivity()).setmName(name);
					mPageVp.setCurrentItem(4,false);
				}
				
			}
		});
		
			
		
		
		initData();
		
		return v;
	}
	
	 
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}
	
	
	private void initData() {
		String cache = CacheUtils.getCache("data", getActivity());
		
		if (!(cache==null)) {
			parseData(cache);
			System.out.println("读取缓存");
		}
			getMenuInfo();
			System.out.println("读取服务器");
			
	}
	
	
	
	class MyAdapter extends BaseAdapter{

		private BitmapUtils bu;

		@Override
		public int getCount() {
			
			return menuList.size();
		}

		@Override
		public Object getItem(int position) {
			
			return menuList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			Menu menu = menuList.get(position);
			View v = null;
			ViewHolder mHolder;
			if(convertView == null){
				v = View.inflate(getActivity(), R.layout.listview_item, null);
				mHolder = new ViewHolder();
				mHolder.tv_title = (TextView) v.findViewById(R.id.tv_title);
				mHolder.tv_detail = (TextView) v.findViewById(R.id.tv_detail);
				mHolder.iv = (ImageView) v.findViewById(R.id.iv);
				mHolder.iv_avatar = (ImageView) v.findViewById(R.id.iv_avatar);
				v.setTag(mHolder);
			}
			else{
				v = convertView;
				mHolder = (ViewHolder) v.getTag();
			}
			
			mHolder.tv_title.setText(menu.getText());
			mHolder.tv_detail.setText(menu.getName());
			
			bu = new BitmapUtils(getActivity());
			bu.configDefaultLoadingImage(R.drawable.pic_item_list_default);
			bu.display(mHolder.iv, menu.getPath());
			bu.display(mHolder.iv_avatar, menu.getOwner());
			
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					notifyDataSetChanged();	
				}
			});
			
			return v;
		}
		
		class ViewHolder{
			TextView tv_title;
			TextView tv_detail;
			ImageView iv;
			ImageView iv_avatar;
		}
		
	
	}
	
	
	private void parseData(String result){
		String[] lines = result.split("\n");
		
		System.out.println("'---------------parseData-------------");
		int len = lines.length;
		for (int i=1;i<len;i++) {
			String[] split = lines[i].split(",");
			String item = split[0].trim();
			String text = split[1].trim();
			String owner = split[2].trim();
			String path = split[3].trim();
			String name = split[4].trim();
			String version = split[5].trim();
			
			Menu menu = new Menu();
			
			 menu.setItem(item);
			 menu.setText(text);
			 menu.setOwner("http://10.0.1.201:8086/"+path+"/avatar");
			 menu.setPath("http://10.0.1.201:8086/"+path+"/"+item+".banner");
			 menu.setName(name);
			 menu.setVersion(version);
		
			 menuList.add(menu);
			 
		}
		
		for (int i = 0; i < menuList.size(); i++) {
			for (int j = menuList.size() -1;j>i; j--) {
				if (menuList.get(j).getItem().equals(menuList.get(i).getItem())) {
					menuList.remove(j);
				}
			}
		}
		 
	}
	
	private int measuredHeight;
    
	    
	
	private void getMenuInfo() {
		HttpUtils utils = new HttpUtils();
		path = "http://10.0.1.201:8086/.csv";
		utils.send(HttpMethod.GET, path, new RequestCallBack<String>() {

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				System.out.println("加载中、。。。。。。。。。。。。。。。。。。");
				super.onLoading(total, current, isUploading);
			}
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				
				String result = (String)responseInfo.result;
				parseData(result);
				lv_list.onRefreshComplete();
				headerView2.setPadding(0, -measuredHeight, 0, 0);
				CacheUtils.setCache("data", result, getActivity());
				
				System.out.println("--------onSuccess-------------");
				System.out.println(result);
				
			}
			
			
			@Override
			public void onFailure(HttpException error, String msg) {
					headerView2.setPadding(0, 0, 0, 0);
					System.out.println("-----------onFailure-------------");
				  lv_list.onRefreshComplete();
			}
		});
	}
	
}
