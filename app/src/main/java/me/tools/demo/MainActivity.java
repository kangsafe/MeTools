package me.tools.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import info.lamatricexiste.network.ActivityMain;
import me.tools.times.SntpClient;
import me.tools.transform.FileHelper;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private Button btn = null;
    private Button vpn = null;
    private Button youth = null;
    private Button slideshow = null;
    private Button cyclepager = null;
    private Button lanshare = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn = (Button) findViewById(R.id.time_btn);
        vpn = (Button) findViewById(R.id.vpn);
        youth = (Button) findViewById(R.id.youthbanner);
        slideshow = (Button) findViewById(R.id.slideshow);
        cyclepager = (Button) findViewById(R.id.cyclepage);
        lanshare = (Button) findViewById(R.id.lanshare);
    }

    private Handler handler = new Handler() {
        // 处理具体的message,该方法由父类中进行继承.
        @Override
        public void handleMessage(Message msg) {
            int msgID = msg.what;

            Bundle bundle = (Bundle) msg.obj;

            switch (msg.what) {
                case 1: // VPN库发送消息处理
                {
                    if (null != bundle) {
                        String time = bundle.getString("time");
                        Log.i("Message", time);
                        btn.setText(time);
                        //Toast.makeText(getApplicationContext(), time, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.time_btn:
                new MyThread().start();
                //Toast.makeText(MainActivity.this, "str", Toast.LENGTH_SHORT).show();
                break;
            case R.id.vpn:
                intent.setClass(this, VPNActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.youthbanner:
                intent.setClass(MainActivity.this, YouthBannerActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.slideshow:
                intent.setClass(MainActivity.this, SlideShowActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.cyclepage:
                intent.setClass(MainActivity.this, CycleViewPagerActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.lanshare:
                //intent.setClass(MainActivity.this, LanShareActivity.class);
                intent.setClass(MainActivity.this, ActivityMain.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private class MyThread extends Thread {
        @Override
        public void run() {
            SntpClient client = new SntpClient();
            System.out.println("begin request");
            if (client.requestTime("cn.pool.ntp.org", 30000)) {
                System.out.println("end request");
                long now = client.getNtpTime() + System.nanoTime() / 1000 - client.getNtpTimeReference();
                String str = FileHelper.dateToString(now);
                Log.i("nihao", str);
                Message message = Message.obtain(handler);
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("time", str);
                if (bundle != null) {
                    message.obj = bundle;
                }
                message.sendToTarget();
            }
        }
    }
}
