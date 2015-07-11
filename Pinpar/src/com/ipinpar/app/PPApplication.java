package com.ipinpar.app;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.ipinpar.app.activity.MainActivity;
import com.ipinpar.app.util.DeviceUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class PPApplication extends Application implements UncaughtExceptionHandler{
	public static final String TAG = PPApplication.class.getSimpleName();
	private static Context applicationContext;
	
	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = this;
		initImageLoader();
		DeviceUtil.init(this);
		initChat();
	}
	
	private void initChat() {
		// TODO Auto-generated method stub
		EMChat.getInstance().init(applicationContext);

		/**
		 * debugMode == true 时为打开，sdk 会在log里输入调试信息
		 * @param debugMode
		 * 在做代码混淆的时候需要设置成false
		 */
		EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，如果未被关闭，则会出现程序无法运行问题
	
	}

	public void initImageLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisk(true)
				.cacheInMemory(true)
				.displayer(new FadeInBitmapDisplayer(50))
				.displayer(new RoundedBitmapDisplayer(15))
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY) // default
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this)
				.memoryCache(new UsingFreqLimitedMemoryCache(16 * 1024 * 1024))
				.diskCache(
						new UnlimitedDiskCache(getExternalCacheDir(),
								null,
								new HashCodeFileNameGenerator()))
				.defaultDisplayImageOptions(defaultOptions).build();
		ImageLoader.getInstance().init(config);
	}
	
	public static Context getContext() {
		return applicationContext;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		System.exit(0);
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		
	}
}
