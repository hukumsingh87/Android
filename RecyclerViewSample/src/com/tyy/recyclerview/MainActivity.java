package com.tyy.recyclerview;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tyy.recyclerview.adapter.ItemAdapter;
import com.tyy.recyclerview.entity.ItemEntity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public class MainActivity extends ActionBarActivity implements OnRefreshListener {
	private SwipeRefreshLayout mSwipeRefreshWidget;
	private LinearLayoutManager mLinearLayoutManager;
	private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
	private RecyclerView mRecyclerView;
	private ItemAdapter adapter;

	private int firstVisibleItem, lastVisibleItem;

	private int max = 100;// 间距最大值
	private int min = 20;// 间距最小值
	private Random random = new Random();

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mSwipeRefreshWidget.setRefreshing(false);

				adapter.getList().clear();
				addList();
				break;
			case 1:
				addList();
				break;
			default:
				break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
		mRecyclerView = (RecyclerView) findViewById(android.R.id.list);

		mSwipeRefreshWidget.setColorScheme(R.color.color1, R.color.color2, R.color.color3, R.color.color4);
		mSwipeRefreshWidget.setOnRefreshListener(this);

		mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
					handler.sendEmptyMessageDelayed(1, 3000);
				}
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				// 横屏或竖屏状态下获取firstVisibleItem和lastVisibleItem
				if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
					lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
				} else {
					int[] firstVisibleItems = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(null);
					firstVisibleItem = Math.min(firstVisibleItems[0], firstVisibleItems[1]);
					int[] lastVisibleItems = mStaggeredGridLayoutManager.findLastVisibleItemPositions(null);
					lastVisibleItem = Math.max(lastVisibleItems[0], lastVisibleItems[1]);
				}
			}

		});
		mLinearLayoutManager = new LinearLayoutManager(this);
		mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		setLayoutManager(getResources().getConfiguration());

		adapter = new ItemAdapter();
		mRecyclerView.setAdapter(adapter);

		addList();

	}

	/**
	 * 横屏或竖屏状态下设置LayoutManager
	 * @param configuration
	 */
	private void setLayoutManager(Configuration configuration) {
		if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mRecyclerView.setLayoutManager(mLinearLayoutManager);
		} else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
		}
		mRecyclerView.scrollToPosition(firstVisibleItem);
	}

	private void addList() {
		List<ItemEntity> list = getList();
		adapter.getList().addAll(list);
		adapter.notifyDataSetChanged();
	}

	private List<ItemEntity> getList() {
		List<ItemEntity> list = new ArrayList<ItemEntity>();
		int size = adapter.getList().size();
		int lastPosition = size > 0 ? adapter.getList().get(size - 1).index : 0;
		for (int i = 1; i < 11; i++) {
			ItemEntity entity = new ItemEntity();
			entity.index = lastPosition + i;
			int height = getRandomHeight();
			entity.height = height;
			list.add(entity);
		}
		return list;
	}

	/**
	 * item随机高度
	 * @return
	 */
	private int getRandomHeight() {
		return random.nextInt(max) % (max - min + 1) + min;
	}

	@Override
	public void onRefresh() {
		handler.sendEmptyMessageDelayed(0, 3000);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setLayoutManager(newConfig);
	}

}
