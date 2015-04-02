package com.tyy.recyclerview.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tyy.recyclerview.R;
import com.tyy.recyclerview.entity.ItemEntity;

public class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {
	private List<ItemEntity> list;

	private static final int TYPE_ITEM = 0;
	private static final int TYPE_FOOTER = 1;

	public List<ItemEntity> getList() {
		return list;
	}

	public ItemAdapter() {
		list = new ArrayList<ItemEntity>();
	}

	@Override
	public int getItemCount() {
		return list.size() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (position + 1 == getItemCount()) {
			return TYPE_FOOTER;
		} else {
			return TYPE_ITEM;
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		if (holder instanceof ItemViewHolder) {
			int height = list.get(position).height;
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER;
			params.setMargins(height, height, height, height);
			((ItemViewHolder) holder).textView.setLayoutParams(params);
			((ItemViewHolder) holder).textView.setText(String.valueOf(list.get(position).index));
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_ITEM) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_text, null);
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			return new ItemViewHolder(view);
		} else if (viewType == TYPE_FOOTER) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footerview, null);
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			return new FooterViewHolder(view);
		}
		return null;
	}

	class FooterViewHolder extends ViewHolder {

		public FooterViewHolder(View view) {
			super(view);
		}

	}

	class ItemViewHolder extends ViewHolder {
		CardView cardview;
		TextView textView;

		public ItemViewHolder(View view) {
			super(view);
			cardview = (CardView) view.findViewById(R.id.cardview);
			textView = (TextView) view.findViewById(R.id.text);
		}

	}

}
