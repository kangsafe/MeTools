package me.tools.demo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import me.tools.vpn.VPNInterface;
import topsec.sslvpn.svsdklib.SVSDKLib;

/**
 * Created by Admin on 2016/6/24 0024.
 */
public class DemoApplication extends Application {

    private final String TAG = getClass().getSimpleName();
    // VPN服务器地址、端口号、用户名、密码
    private final String VPN_SERVER = "59.49.15.130";
    private final int VPN_PORT = 443;
    private final String VPN_USERNAME = "oa";
    private final String VPN_PASSWORD = "123456";

    @Override
    public void onCreate() {
        super.onCreate();
        initVPN();
    }

    private void initVPN() {
        // 获取VPN库实例
        VPNInterface vpnlib = VPNInterface.getInstance();

        // 设置VPN客户端的释放目录
        Context appContext = getApplicationContext();
        vpnlib.setSVClientPath(appContext.getFilesDir().getPath());

        // 设置应用程序的资产管理器
        vpnlib.setAppam(this.getAssets());


        // 设置VPNSDK库的消息处理器
        //vpnlib.setMsgHandler(MsgHandler);

        // 设置VPNSDK库的VPN状态变更消息号
        //vpnlib.setVPNMsgID(VPN_MSG_STATUS_UPDATE);

        // 设置VPN连接信息
        vpnlib.setVPNInfo(VPN_SERVER, VPN_PORT, VPN_USERNAME, VPN_PASSWORD);

        // VPN客户端连接前的准备
        vpnlib.prepareVPNSettings();
        vpnlib.startVPN();
        Log.i(TAG, "InitSVSDKLib done");
    }
}
