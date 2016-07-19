package me.tools.banner;

import android.content.Context;
import android.os.Handler;

/**
 * 为了防止内存泄漏，定义外部类，防止内部类对外部类的引用
 */
public class ViewPagerHandler extends Handler {
	 Context context;

	public ViewPagerHandler(Context context) {
		this.context = context;
	}
};