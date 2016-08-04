package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kitchencooking.R;

public class RefreshListView extends ListView implements OnScrollListener{
	
	
	RefreshListView list;
	private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
	private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
	private static final int STATE_REFRESHING = 2;// 正在刷新

	private View mHeadview;
	private int startY = -1; //滑动的起点Y坐标
	
	private int mCurrrentState = STATE_PULL_REFRESH;// 当前状态
	private int measuredHeight;
	private TextView tvTitle;
	private TextView tvTime;
	private ImageView ivArrow;
	private ProgressBar pbProgress;
	
	private RotateAnimation animUp;
	private RotateAnimation animDown;


	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeadview();
//		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeadview();
//		initFooterView();
	}

	public RefreshListView(Context context) {
		super(context);
		initHeadview();
//		initFooterView();
	}
	
	
	
	private void initHeadview() {
		mHeadview = View.inflate(getContext(), R.layout.refresh_header, null);
		this.addHeaderView(mHeadview);
		
		tvTitle = (TextView) mHeadview.findViewById(R.id.tv_title);
		tvTime = (TextView) mHeadview.findViewById(R.id.tv_time);
		ivArrow = (ImageView) mHeadview.findViewById(R.id.iv_arr);
		pbProgress = (ProgressBar) mHeadview.findViewById(R.id.pb_progress);
		
		
		mHeadview.measure(0, 0);//对控件进行测量
		measuredHeight = mHeadview.getMeasuredHeight(); //控件的高度
		
		mHeadview.setPadding(0, -measuredHeight, 0, 0);//负高度，即为隐藏控件
		
		
		initArrowAnim();//初始化箭头动画
		
		
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
				    startY=(int) ev.getY();
				    break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {// 确保startY有效
				startY = (int) ev.getRawY();
			}

			if (mCurrrentState == STATE_REFRESHING) {// 正在刷新时不做处理
				break;
			}

			int endY = (int) ev.getRawY();
			int dy = endY - startY;// 移动偏移量

			if (dy > 0 && getFirstVisiblePosition() == 0) {// 只有下拉并且当前是第一个item,才允许下拉
				int padding = dy - measuredHeight;// 计算padding
				mHeadview.setPadding(0, padding, 0, 0);// 设置当前padding

				if (padding > 0 && mCurrrentState != STATE_RELEASE_REFRESH) {// 状态改为松开刷新
					mCurrrentState = STATE_RELEASE_REFRESH;
					refreshState();
				} else if (padding < 0 && mCurrrentState != STATE_PULL_REFRESH) {// 改为下拉刷新状态
					mCurrrentState = STATE_PULL_REFRESH;
					refreshState();
				}
				
			}

			break;
		case MotionEvent.ACTION_UP:
			startY = -1;// 重置

			if (mCurrrentState == STATE_RELEASE_REFRESH) {
				mCurrrentState = STATE_REFRESHING;// 正在刷新
				mHeadview.setPadding(0, 0, 0, 0);// 显示
				refreshState();
			} else if (mCurrrentState == STATE_PULL_REFRESH) {
				mHeadview.setPadding(0, -measuredHeight, 0, 0);// 隐藏
			}

			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	/**
	 * 刷新下拉控件的布局
	 */
	private void refreshState() {
		switch (mCurrrentState) {
		case STATE_PULL_REFRESH:
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(animDown);
			
			break;
		case STATE_RELEASE_REFRESH:
			tvTitle.setText("松开刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(animUp);
			break;
		case STATE_REFRESHING:
			tvTitle.setText("正在刷新...");
			ivArrow.clearAnimation();// 必须先清除动画,才能隐藏
			ivArrow.setVisibility(View.INVISIBLE);
			pbProgress.setVisibility(View.VISIBLE);
			
			if (mListener != null) {
				mListener.onRefresh();
			}
			break;

		default:
			break;
		}
	}
	
	/**
	 * 初始化箭头动画
	 */
	private void initArrowAnim() {
		// 箭头向上动画
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		
		animUp.setDuration(200);
		animUp.setFillAfter(true);//是否保持状态

		// 箭头向下动画
		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(200);
		animDown.setFillAfter(true);
	}
	
	
	
	OnRefreshListener mListener;
	private int mFooterViewHeight;
	private View mFooterView;
	
	public void setOnRefreshListener(OnRefreshListener listener){
		mListener = listener;
	}
	
	//定义一个接口
	public interface OnRefreshListener{
		public void onRefresh();
		
		public void onLoadMore();
	}
	
	
	
	/*
	 * 收起下拉刷新的控件
	 */
	public void onRefreshComplete() {
//		if (isLoadingMore) {// 正在加载更多...
//			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏脚布局
//			isLoadingMore = false;
//		} else {
			mCurrrentState = STATE_PULL_REFRESH;
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);

			mHeadview.setPadding(0, -measuredHeight, 0, 0);// 隐藏
			
		}
//	}
	
	
//	/*
//	 * 初始化脚布局
//	 */
//	private void initFooterView() {
//		mFooterView = View.inflate(getContext(),
//				R.layout.refresh_listview_footer, null);
//		this.addFooterView(mFooterView);
//
//		mFooterView.measure(0, 0);
//		mFooterViewHeight = mFooterView.getMeasuredHeight();
//
//		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏
//		this.setOnScrollListener(this);
//	}

	

//	private boolean isLoadingMore;
//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
//			if (getLastVisiblePosition() == getCount() -1 &&!isLoadingMore) {
//				System.out.println("到底了");
//				mFooterView.setPadding(0, 0, 0, 0);
//				
//				setSelection(getCount() -1); //改变listview的显示位置
//				
//				isLoadingMore = true;
//				
//				if (mListener != null) {
//					mListener.onLoadMore();
//				}
//			}
//		}
//	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
