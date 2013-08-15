package com.example.myanimtest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * �ο�github�ϵĴ��룬��������
 * 
 * ��ӭ����QQ����Ⱥ��88130145
 * 
 * @Description:
 * @see:
 * @since:
 */
public class MainActivity extends Activity {

	static final int ANIMATION_DURATION = 200;
	private static List<MyCell> mAnimList = new ArrayList<MyCell>();
	private MyAnimListAdapter mMyAnimListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		for (int i = 0; i < 50; i++) {
			MyCell cell = new MyCell();
			cell.name = "Cell No." + Integer.toString(i);
			mAnimList.add(cell);
		}

		mMyAnimListAdapter = new MyAnimListAdapter(this, R.layout.chain_cell, mAnimList);
		ListView myListView = (ListView) findViewById(R.id.chainListView);
		myListView.setAdapter(mMyAnimListAdapter);
	}

	private void deleteCell(final View v, final int index) {
		AnimationListener al = new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {
				mAnimList.remove(index);

				ViewHolder vh = (ViewHolder) v.getTag();
				vh.needInflate = true;

				mMyAnimListAdapter.notifyDataSetChanged();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		};

		animHideShowView(v, al, 0, false, ANIMATION_DURATION);
	}

	/**
	 * ��ֹ����ִ�й����б����հף���Ļ������ִ�У��߿���view�߶�
	 * 
	 * ��ӭ����QQ����Ⱥ��88130145
	 * 
	 * @Description:
	 * @param v
	 *            Ҫִ�ж�����View
	 * @param al
	 *            ��������������Ϊ��
	 * @param measureHeight
	 *            view��ʵ�ʸ߶ȣ��ɲ���������ʾʱ��Ҫ��֤�˸߶Ȳ�Ϊ0
	 * @param show
	 *            ����ʾ��������
	 * @param ainmTime
	 *            ����ʱ��
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 */
	public static void animHideShowView(final View v, AnimationListener al, int measureHeight, final boolean show, int ainmTime) {

		if (measureHeight == 0) {
			measureHeight = v.getMeasuredHeight();
		}
		final int heightMeasure = measureHeight;
		Animation anim = new Animation() {

			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {

				if (interpolatedTime == 1) {

					v.setVisibility(show ? View.VISIBLE : View.GONE);
				} else {
					int height;
					if (show) {
						height = (int) (heightMeasure * interpolatedTime);
					} else {
						height = heightMeasure - (int) (heightMeasure * interpolatedTime);
					}
					v.getLayoutParams().height = height;
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		if (al != null) {
			anim.setAnimationListener(al);
		}
		anim.setDuration(ainmTime);
		v.startAnimation(anim);
	}

	private class MyCell {

		public String name;
	}

	private class ViewHolder {

		public boolean needInflate;
		public TextView text;
		ImageButton imageButton;
	}

	public class MyAnimListAdapter extends ArrayAdapter<MyCell> {

		private LayoutInflater mInflater;
		private int resId;

		public MyAnimListAdapter(Context context, int textViewResourceId, List<MyCell> objects) {
			super(context, textViewResourceId, objects);
			this.resId = textViewResourceId;
			this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final View view;
			ViewHolder vh;
			MyCell cell = (MyCell) getItem(position);

			if (convertView == null) {
				view = mInflater.inflate(R.layout.chain_cell, parent, false);
				setViewHolder(view);
			} else if (((ViewHolder) convertView.getTag()).needInflate) {
				view = mInflater.inflate(R.layout.chain_cell, parent, false);
				setViewHolder(view);
			} else {
				view = convertView;
			}

			vh = (ViewHolder) view.getTag();
			vh.text.setText(cell.name);
			vh.imageButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					deleteCell(view, position);
				}
			});

			return view;
		}

		private void setViewHolder(View view) {
			ViewHolder vh = new ViewHolder();
			vh.text = (TextView) view.findViewById(R.id.cell_name_textview);
			vh.imageButton = (ImageButton) view.findViewById(R.id.cell_trash_button);
			vh.needInflate = false;
			view.setTag(vh);
		}
	}
}
