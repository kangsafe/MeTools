package me.tools.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import me.tools.banner.YouthBanner;

public class YouthBannerActivity extends Activity {
    private YouthBanner banner;
    String[] images = new String[]{
            "http://img.zcool.cn/community/01ae5656e1427f6ac72531cb72bac5.jpg",
            "http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg",
            "http://img.zcool.cn/community/018fdb56e1428632f875520f7b67cb.jpg",
            "http://img.zcool.cn/community/01c8dc56e1428e6ac72531cbaa5f2c.jpg",
            "http://img.zcool.cn/community/01fda356640b706ac725b2c8b99b08.jpg",
            "http://img.zcool.cn/community/01fd2756e142716ac72531cbf8bbbf.jpg",
            "http://img.zcool.cn/community/0114a856640b6d32f87545731c076a.jpg"};
    String[] titles = new String[]{"十大星级品牌联盟，全场2折起", "2", "3", "4", "5", "嗨购5折不要停，12.12趁现在", "7"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_youth);
        banner = (YouthBanner) findViewById(R.id.banner);
        //设置样式
//        banner.setBannerStyle(Banner.NOT_INDICATOR);
//        banner.setBannerStyle(Banner.CIRCLE_INDICATOR);
//        banner.setBannerStyle(Banner.NUM_INDICATOR);
        banner.setBannerStyle(YouthBanner.NUM_INDICATOR_TITLE);
//        banner.setBannerStyle(Banner.CIRCLE_INDICATOR_TITLE);

        banner.setBannerTitle(titles);
        banner.setIndicatorGravity(YouthBanner.CENTER);
        banner.setDelayTime(5000);//设置轮播间隔时间
        banner.setImages(images);//可以选择设置图片网址，或者资源文件，默认用Glide加载
        banner.setOnBannerClickListener(new YouthBanner.OnBannerClickListener() {//设置点击事件
            @Override
            public void OnBannerClick(View view, int position) {

                Toast.makeText(getApplicationContext(), "你点击了：" + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("--", "onStart");
        banner.isAutoPlay(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("--", "onStop");
        banner.isAutoPlay(false);
    }
}