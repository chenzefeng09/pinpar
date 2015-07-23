package com.ipinpar.app.util;


import com.ipinpar.app.PPApplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Android大小单位转换工具类
 * 
 * @author wader
 * 
 */
public class DisplayUtil {

	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth() {
		return PPApplication.getContext().getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 获取屏幕高度 实际显示的高度 (去除顶部栏)
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenShowHeight(Activity context) {
		Rect frame = new Rect();
		context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return PPApplication.getContext().getResources().getDisplayMetrics().heightPixels
				- frame.top;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight() {
		return PPApplication.getContext().getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @param pActivity
	 * @return bitmap
	 */
	public static int getStautsHeight(Activity activity) {
		View view = activity.getWindow().getDecorView();
		// 获取状态栏高度
		Rect frame = new Rect();
		// 测量屏幕宽和高
		view.getWindowVisibleDisplayFrame(frame);
		int stautsHeight = frame.top;

		return stautsHeight;
	}

	/**
	 * dip 转换成 px值
	 * 
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(float dipValue) {
		final float scale = PPApplication.getContext().getResources()
				.getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(float pxValue) {
		final float scale = PPApplication.getContext().getResources()
				.getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(float pxValue) {
		final float fontScale = PPApplication.getContext().getResources()
				.getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(float spValue) {
		final float fontScale = PPApplication.getContext().getResources()
				.getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 获取某个控件的宽高 调用方法后用View.getMeasuredWidth(),View.getMeasuredHeight() 来获取宽高
	 * 
	 * @param child
	 */
	public static void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	/**
	 * 截图工具类
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap convertViewToBitmap(View view) {

		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

		Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(),
				view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		view.draw(c);
		return b;
	}

	/**
	 * 是否是高分辨率 480*800为hdpi 760*1024为xhdpi
	 * 
	 * @return 如果是xhdpi或更高 返回true 否则返回false
	 */
	public static boolean isXhdpi() {
		float density = PPApplication.getContext().getResources().getDisplayMetrics().density;
		if (density >= 2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ScrollView嵌套ListView只显示一行 所以需要用这个方法重新计算listview的高度 用来全部显示
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = getListViewHeight(listView);
		listView.setLayoutParams(params);
	}

	/**
	 * 获取ListView 的高
	 * 
	 * @param listView
	 * @return
	 */
	public static int getListViewHeight(ListView listView) {
		ListAdapter adapter = listView.getAdapter();
		// 获取ListView对应的Adapter
		if (adapter == null) {
			return 0;
		}

		int totalHeight = 0;
		for (int i = 0, len = adapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = adapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}
		return totalHeight
				+ (listView.getDividerHeight() * (adapter.getCount() - 1));
	}
}

