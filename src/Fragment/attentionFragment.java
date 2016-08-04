package Fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.kitchencooking.CircleImageView;
import com.example.kitchencooking.R;

import Fragment.fansFragment.Adapter;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class attentionFragment extends Fragment {
	private List<String> list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_attention, null);
		ListView lv = (ListView) v.findViewById(R.id.lv_attentionlist);
		list = new ArrayList<String>();
		
		list.add("张曼玉");
		list.add("林青霞");
		list.add("赵雅芝");
		list.add("刘嘉玲");
		
		lv.setAdapter(new Adapter());
		return v;
		
	}
	
	class Adapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View v, ViewGroup arg2) {
			View view = View.inflate(getActivity(),R.layout.listview_item_attention, null);
			
			String text = list.get(position);
			
			TextView tv = (TextView) view.findViewById(R.id.tv_atdetail);
			tv.setText(text);
			CircleImageView iv = (CircleImageView) view.findViewById(R.id.iv_athead);
			iv.setImageResource(R.drawable.athead2);
			return view;
		}
		
	}
}
