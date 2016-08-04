package Fragment;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import utils.CacheUtils;

import com.example.kitchencooking.MainActivity;
import com.example.kitchencooking.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import domain.Menu;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 主页fragment
 * @author john
 *
 */
public class SearchFragment extends Fragment {
	
	
	private View v;
	private TextView mTextView;
	private EditText mEditText;
	private ImageView mImageView;
	private ImageView iv_toleft;
	private ListView lv_list;
	private Context context;
	private List<Menu> menuList;
	private Menu menu;
	private MyAdapter madapter;
	private ViewPager mPageVp;
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			lv_list.setAdapter(madapter);
            
            madapter.notifyDataSetChanged();
            for(int i=0;i<menuList.size();i++){
                for(int j=i+1;j<menuList.size();j++){
                    if(menuList.get(i).getItem().equals(menuList.get(j).getItem())){
                    	menuList.remove(j);
                    }
                }
            }
            madapter.notifyDataSetChanged();
		}
	};
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		v = inflater.inflate(R.layout.activity_search, container,false);
		
			menuList = new ArrayList<Menu>();
			
			initView();
			lv_list.setVisibility(View.GONE);
		return v;
	}
	
	private void initView() {  
		 mEditText = (EditText) v.findViewById(R.id.edittext);  
		 mImageView = (ImageView) v.findViewById(R.id.imageview);  
		 lv_list = (ListView) v.findViewById(R.id.listview);
		 madapter = new MyAdapter();
		 mPageVp = (ViewPager) getActivity().findViewById(R.id.id_page_vp);
		
	        //设置删除图片的点击事件
	        mImageView.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                //把EditText内容设置为空 
	                mEditText.setText("");
	                //把ListView隐藏
	                lv_list.setVisibility(View.GONE);
	            }
	        });
	  
	        //EditText添加监听  
	        mEditText.addTextChangedListener(new TextWatcher() {  
	            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}//文本改变之前执行  
	  
	            @Override
	            //文本改变的时候执行 
	            public void onTextChanged(CharSequence s, int start, int before, int count) {  
	            	String str = mEditText.getText().toString().trim();
	                
	            	menuList.clear();
	            	
	            	//如果长度为0
	                if (s.length() == 0) {
	                    mImageView.setVisibility(View.GONE);  //隐藏“删除”图片  
	                    menuList.clear();
	                    lv_list.setVisibility(View.GONE);//隐藏listview
	                } else {
	                    mImageView.setVisibility(View.VISIBLE);  //显示“删除图片”
	                    lv_list.setVisibility(View.VISIBLE);//显示listview
	                    
	                    getMenuInfo();
	                }
	            }
	  
	            public void afterTextChanged(Editable s) {}//文本改变之后执行 
	        });  
	  
	        
	        lv_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String text = menuList.get(position).getText();
					String path = menuList.get(position).getPath();
					String owner = menuList.get(position).getOwner();
					
					((MainActivity) getActivity()).setmPath(path);
				    ((MainActivity) getActivity()).setmOwner(owner);
				    ((MainActivity) getActivity()).setmText(text);
				    mPageVp.setCurrentItem(4);
					
				}
			});
	    }
	
	
	class MyAdapter extends BaseAdapter{


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

			menu = menuList.get(position);
			View v = null;
			ViewHolder mHolder;
			if(convertView == null){
				v = View.inflate(getActivity(), R.layout.item_listview, null);
				mHolder = new ViewHolder();
				//把布局文件中所有组件的对象封装至ViewHolder对象中
				mHolder.tv_title = (TextView) v.findViewById(R.id.textview);
				mHolder.tv_detail = (TextView) v.findViewById(R.id.textview2);
				
				//把ViewHolder对象封装至View对象中
				v.setTag(mHolder);
			}
			else{
				v = convertView;
				mHolder = (ViewHolder) v.getTag();
			}
			
			//给文本框设置内容
			mHolder.tv_title.setText(menu.getText());
			mHolder.tv_detail.setText(menu.getName());
			return v;
		}
		
		class ViewHolder{
			//条目的布局文件中有什么组件，这里就定义什么属性
			TextView tv_title;
			TextView tv_detail;
		}
	}

private void parseData(String result) {
	String[] lines = result.split("\n");//遍历出很多行，以行来分
	
	System.out.println("'----------------------------------");
	int len = lines.length;
	for (int i=0;i<len;i++) {
		try {
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
		} catch (Exception e) {
			
		}
		
	}
	
	handler.sendEmptyMessage(1);
}


private void getMenuInfo() { 
	try{
    //获得输入的内容  
    String str = mEditText.getText().toString().trim();
    
    HttpUtils utils = new HttpUtils();
	String path = "http://10.0.1.201:8086/.csv?fro=chaxun&str=" + str;

	utils.send(HttpMethod.GET, path, new RequestCallBack<String>() {

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			String result = (String)responseInfo.result;
			System.out.println(result);
			parseData(result);
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			Toast.makeText(getActivity(), "无法连接到服务器哦！", 0).show();
		}

		
	});
	}catch(ArrayIndexOutOfBoundsException e){
		
	}
    }
	
	
	

	
	
}
