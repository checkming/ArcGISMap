package com.gt.cscity.planning.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gt.cscity.planning.R;
import com.gt.cscity.planning.ui.view.DepthPageTransformer;
import com.gt.cscity.planning.utils.pxUtil;
import com.gt.cscity.planning.utils.spUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 红点移动
 */
public class GuideActivity extends Activity implements OnClickListener {
	private ViewPager vp_viewpager;
	private List<ImageView> images;
	private LinearLayout ll_point;
	private ImageView iv_redPoint;
	private Button btn_start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 将标题栏隐藏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);

		// 获取控件
		vp_viewpager = (ViewPager) findViewById(R.id.vp_viewpager);// v4.viewpager
		ll_point = (LinearLayout) findViewById(R.id.ll_point);// 灰点
		iv_redPoint = (ImageView) findViewById(R.id.iv_redPoint); // 红点覆盖的按钮
		btn_start = (Button) findViewById(R.id.btn_guide_start); // "开始体验"按钮
		// 设置点击事件
		btn_start.setOnClickListener(this);

		// 定义一个方法实现获取图片
		initData();

	}

	/**
	 * 对滑动的滑点进行处理
	 */
	private void initData() {
		// 准备数据 ,创建一个存储图片的集合
		images = new ArrayList<ImageView>();
		int[] drawables = new int[] { R.drawable.guide2, R.drawable.guide3,
				R.drawable.guide4 };
		// 利用for循环添加图片
		for (int i = 0; i < drawables.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(drawables[i]);
			// 往集合中添加
			images.add(imageView);

			// 根据viewpager图片的数量添加灰点的数量
			ImageView greyNumbers = new ImageView(this);
			greyNumbers.setBackgroundResource(R.drawable.guide_point_normal);
			// 将灰点添加到容器中
			ll_point.addView(greyNumbers);
			// 给灰点设置间距
			LayoutParams params = (LayoutParams) greyNumbers.getLayoutParams();
			if (i != 0) { // 即从第一个与第二个开始判断,并随之增加边距
				// 将设置控件的px 转换为dp对应的px
				params.leftMargin = pxUtil.dp2px(this, 10);  //10 对应的px值
			}
		}

		// 设置一个适配器
		vp_viewpager.setAdapter(new MyAdapter());
		
		//设置ViewPager设置切换动画  
		vp_viewpager.setPageTransformer(true, new DepthPageTransformer());

		// 设置滑动的事件
		vp_viewpager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 对滑动的页面进行判断 最后一页事调用
				if (position == images.size() - 1) {
					btn_start.setVisibility(View.VISIBLE);
				} else {
					btn_start.setVisibility(View.GONE);
				}
			}

			// 参1: 条目的索引值 参2: viewpager 滑动的距离 / 滑动间的间距 * (灰点间距) = positionOffset
			// 参3:viewpager滑动的距离
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// 红点移动的距离
				int redPointX = (int) (position + positionOffset);
				System.out.println("GuideActivity中  ,红点移动的距离:~" + redPointX);
				System.out.println("GuideActivity中" + "position:" + position
						+ "positionOffset" + positionOffset);
				// 设置红点移动
				iv_redPoint.setTranslationX(redPointX);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

	}

	/**
	 * 设置适配器
	 * 
	 * @author checkming
	 *
	 */
	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return images.size(); // 集合的长度
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 根据位置获取imageview
			ImageView imageView = images.get(position);
			// 添加到pagerVIew 中
			container.addView(imageView);
			return imageView;
		}

		// 则移除
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	@Override
	public void onClick(View v) {
		// 进入主页面
		spUtil.putBoolean(this, spUtil.IS_FIRST_OPEN, false);
		startActivity(new Intent(this, SlidingMenu.class));
		finish();
	}

}
