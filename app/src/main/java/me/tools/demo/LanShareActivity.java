package me.tools.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import me.tools.utils.NetUtil;

public class LanShareActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lan_share);
        NetUtil netUtil = new NetUtil(this);
        netUtil.getRootPermission();
        List<String> ipList = netUtil.scanIpInSameSegment();
        Log.i(TAG, "扫描到IP");
        //String ips = "扫描到的ip:\n";
        for (String str : ipList) {
            //ips += str + "\n";
            Log.i(TAG, str);
        }
        //Toast.makeText(this, ips, Toast.LENGTH_SHORT).show();
    }
}
