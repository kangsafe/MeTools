package me.tools.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import me.tools.banner.bean.ADInfo;

import java.util.ArrayList;
import java.util.List;

import me.tools.banner.frag.CycleViewPagerFrag;
import me.tools.banner.frag.CycleViewPagerFrag.ImageCycleViewListener;
import me.tools.utils.ViewFactory;

/**
 * 描述：主页
 *
 * @author 胡笃勇
 * @version 2015年5月8日 上午10:47:37
 */
public class CycleViewPagerActivity extends Activity {

	private List<ImageView> views = new ArrayList<ImageView>();
	private List<ADInfo> infos = new ArrayList<ADInfo>();
	private CycleViewPagerFrag cycleViewPagerFrag;
	
	private String[] imageUrls = {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
			"http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
			"http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
			"http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
			"http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cycleviewpager);
		configImageLoader();
		initialize();
	}
	
	@SuppressLint("NewApi")
	private void initialize() {
		
		cycleViewPagerFrag = (CycleViewPagerFrag) getFragmentManager()
				.findFragmentById(R.id.fragment_cycle_viewpager_content);
		
		for(int i = 0; i < imageUrls.length; i ++){
			ADInfo info = new ADInfo();
			info.setUrl(imageUrls[i]);
			info.setContent("图片-->" + i );
			infos.add(info);
		}
		
		// 将最后一个ImageView添加进来
		views.add(ViewFactory.getImageView(this, infos.get(infos.size() - 1).getUrl()));
		for (int i = 0; i < infos.size(); i++) {
			views.add(ViewFactory.getImageView(this, infos.get(i).getUrl()));
		}
		// 将第一个ImageView添加进来
		views.add(ViewFactory.getImageView(this, infos.get(0).getUrl()));

		// 在加载数据前设置是否循环
		cycleViewPagerFrag.setCycle(true);

		// 设置循环，在调用setData方法前调用
		cycleViewPagerFrag.setData(views, infos, mAdCycleViewListener);
		//设置轮播
		cycleViewPagerFrag.setWheel(true);

	    // 设置轮播时间，默认5000ms
		cycleViewPagerFrag.setTime(2000);
		//设置圆点指示图标组居中显示，默认靠右
		cycleViewPagerFrag.setIndicatorCenter();
	}
	
	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			if (cycleViewPagerFrag.isCycle()) {
				position = position - 1;
				Toast.makeText(CycleViewPagerActivity.this,
						"position-->" + info.getContent(), Toast.LENGTH_SHORT)
						.show();
			}
			
		}

	};
	
	/**
	 * 配置ImageLoder
	 */
	private void configImageLoader() {
		// 初始化ImageLoader
		@SuppressWarnings("deprecation")
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.icon_stub) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);		
	}
}