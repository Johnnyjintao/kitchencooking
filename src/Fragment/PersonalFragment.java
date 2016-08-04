package Fragment;


import java.io.File;
import java.util.List;

import com.example.kitchencooking.CircleImageView;
import com.example.kitchencooking.MainActivity;
import com.example.kitchencooking.R;



import db.SqlHelper;
import domain.Person;
import android.R.color;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 收藏ragment
 * @author john
 *
 */
public class PersonalFragment extends Fragment {
	
	private ListView lv_list;
	private View friendView;
	List<Person> personList;
	private ViewPager mPageVp;
	private SqlHelper sqlhelper;
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		friendView = inflater.inflate(R.layout.activity_personal, container,false);

		
		sqlhelper = new SqlHelper(getActivity());
		personList = sqlhelper.query();
		
		
		lv_list = (ListView) friendView.findViewById(R.id.lv_list);
		View v  = inflater.inflate(R.layout.top_pic, null);
		lv_list.addHeaderView(v);
		
		TextView tv_head = new TextView(getActivity());
		tv_head.setText("个人收藏夹");
		lv_list.addHeaderView(tv_head);
		
		lv_list.setAdapter(new MyAdapter());
		
		mPageVp = (ViewPager) getActivity().findViewById(R.id.id_page_vp);
		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (position==0) {
					System.out.println("嘻嘻");
				}else {
					String path = personList.get(position-2).getPath();
					String owner = personList.get(position-2).getItem();
					
					((MainActivity) getActivity()).setmPath(path);
				    ((MainActivity) getActivity()).setmOwner(owner);
					
				    mPageVp.setCurrentItem(4,false);
				}
				
			}
			
		});
		
		return friendView;
		
	}
	
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return personList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = View.inflate(getActivity(),
					R.layout.listview_item_person, null);
			
			final Person p = personList.get(position);
			TextView tv_title = (TextView) v
					.findViewById(R.id.tv_title);
			tv_title.setText(p.getText());
			
			TextView tv_detail = (TextView) v
					.findViewById(R.id.tv_detail);
			tv_detail.setText(p.getName());

			 ImageView iv = (ImageView) v.findViewById(R.id.iv);
			 CircleImageView iv_avatar = (CircleImageView)
			 v.findViewById(R.id.iv_avatar);
			 
			 ImageView iv_delete = (ImageView) v.findViewById(R.id.iv_delete);

			 //从sd卡拿到图片，并将它们显示到listview
			 String filepath = "sdcard/downloadimage/"+p.getText()+"1.jpg";
			 String filepath2 = "sdcard/downloadimage/"+p.getText()+"2.jpg";
			 File file = new File(filepath);
			
			 if (file.exists()) {
			 Bitmap bm = BitmapFactory.decodeFile(filepath);
			 Bitmap bm2 = BitmapFactory.decodeFile(filepath2);
			 iv.setImageBitmap(bm);
			 iv_avatar.setImageBitmap(bm2);
			 }
			 
			 
			 iv_delete.setOnClickListener(new OnClickListener() {
				 
					@Override
					public void onClick(View v) {
					    //通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
		                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		                //设置Title的内容
		                builder.setTitle("温馨提示");
		                //设置Content来显示一个信息
		                builder.setMessage("确定删除吗？");
		                //设置一个PositiveButton
		                builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
		                    @Override
		                    public void onClick(DialogInterface dialog, int which){
										getActivity().runOnUiThread(new Runnable() {
											
											@Override
											public void run() {
												sqlhelper.delete(p.getText());
												personList.remove(position);
												notifyDataSetChanged();//刷新界面
											}
										});
								dialog.dismiss();
							}
		                    
		                });
		                // 设置一个NegativeButton
		                builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
		                    @Override
		                    public void onClick(DialogInterface dialog, int which){
		                    	dialog.dismiss();
		                    }
		                });
		               
		                //    显示出该对话框
		                builder.show();
		            }
		        });
			return v;
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}
}
