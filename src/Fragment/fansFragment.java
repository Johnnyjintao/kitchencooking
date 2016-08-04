package Fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.kitchencooking.R;
import com.sun.java_cup.internal.runtime.virtual_parse_stack;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class fansFragment extends Fragment {
	
	private List<String> list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_fans, null);
		ListView lv = (ListView) v.findViewById(R.id.lv_fanslist);
		
		list = new ArrayList<String>();
		
		list.add("周杰伦");
		list.add("陈奕迅");
		list.add("张学友");
		list.add("刘德华");
		
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
			return view;
		}
		
	}
}
