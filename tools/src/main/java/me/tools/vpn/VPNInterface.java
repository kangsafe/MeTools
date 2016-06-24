package me.tools.vpn;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import me.tools.vpn.ssl.ConfigReader;
import me.tools.vpn.ssl.SVLoginParam;
import me.tools.vpn.ssl.Shell;
import me.tools.vpn.ssl.TopSVParam;

public class VPNInterface {
    private static final String TAG = "VPNInterface";
    public static final int VPN_WAIT_CLIENT_MSG_PORT = 35900;
    public static final int VPN_AUTHLIB_MSG_GETSIDBYUSERPWD_DONE = 3;
    public static final int VPN_AUTHLIB_MSG_POSTLOGOUT_DONE = 7;
    public static final int VPN_AUTHLIB_MSG_GET_RESOURCELIST = 11;
    private ArrayList<HashMap<String, String>> ResList = new ArrayList();
    private int mUDPCmdPort = '貄';
    private String mSVClientPath;
    private String mSVAuthProg = "svauthprog";
    private String mSVClient = "svclient";
    private String mSVClientQuit = "svclientquit";
    private String mVPNStatus = "0";
    private Handler MsgHandler = null;
    private String mVPNError = "";
    private String VOneAddr;
    private int VOnePort;
    private String UserName;
    private String UserPwd;
    private AssetManager appam = null;
    private int VPNMsgID = 0;
    private int m_ResPort;
    private int m_ResLocalPort;
    private String m_ResAddr;
    private int m_GetClientMsgPort = '谼';
    private boolean bRunningStatusThread = false;
    private boolean bGetClientMsgSocketOK = false;
    private boolean bUsePreSetRes = false;
    private static VPNInterface singleinstance = null;

    private VPNInterface() {
    }

    public static synchronized VPNInterface getInstance() {
        if (singleinstance == null) {
            singleinstance = new VPNInterface();
        }

        return singleinstance;
    }

    public void setVPNMsgID(int vPNMsgID) {
        this.VPNMsgID = vPNMsgID;
    }

    public ArrayList<HashMap<String, String>> getResList() {
        return this.ResList;
    }

    public int SetResInfo(String sresip, int nresport, int nreslocalport) {
        if (sresip != null && sresip.length() != 0) {
            if (nresport > 0 && nresport <= '\uffff') {
                if (nreslocalport > 0 && nreslocalport <= '\uffff') {
                    this.m_ResAddr = sresip;
                    this.m_ResPort = nresport;
                    this.m_ResLocalPort = nreslocalport;
                    this.bUsePreSetRes = true;
                    return 0;
                } else {
                    Log.e("VPNInterface", "要使用的本地端口设置错误");
                    return -1;
                }
            } else {
                Log.e("VPNInterface", "资源端口设置错误");
                return -1;
            }
        } else {
            Log.e("VPNInterface", "资源地址设置错误");
            return -1;
        }
    }

    public int getResLocalPort(String ip, int port) {
        byte ret = 0;

        for (int i = 0; i < this.ResList.size(); ++i) {
            HashMap item = (HashMap) this.ResList.get(i);
            String itemip = (String) item.get("resip");
            String sport = (String) item.get("resport");
            String localport = (String) item.get("reslocalport");
            int itemport = Integer.parseInt(sport);
            int itemlocalport = Integer.parseInt(localport);
            if (ip.equalsIgnoreCase(itemip) && port == itemport) {
                return itemlocalport;
            }
        }

        return ret;
    }

    public void setAppam(AssetManager appam) {
        this.appam = appam;
    }

    public void setSVClientPath(String mSVClientPath) {
        this.mSVClientPath = mSVClientPath;
    }

    private boolean IsClientExsist(String sfilename) {
        boolean ret = false;

        try {
            String exp = this.mSVClientPath + "/" + sfilename;
            if ((new File(exp)).exists()) {
                ret = true;
            } else {
                Log.d("VPNInterface", "File not found,should get it from asserts directory");
                ret = false;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            ret = false;
        }

        return ret;
    }

    private boolean PutClientProg(String sfilename) {
        if (this.IsClientExsist(sfilename)) {
            return true;
        } else {
            try {
                if (this.appam == null) {
                    return false;
                } else {
                    InputStream exp = this.appam.open(sfilename);
                    String filepath = this.mSVClientPath + "/" + sfilename;
                    FileOutputStream fos = new FileOutputStream(filepath);
                    byte[] buffer = new byte[8192];
                    boolean count = false;

                    int total;
                    int count1;
                    for (total = 0; (count1 = exp.read(buffer)) > 0; total += count1) {
                        fos.write(buffer, 0, count1);
                    }

                    fos.close();
                    exp.close();
                    Log.d("VPNInterface", "Total write " + total + " bytes into " + filepath);
                    Shell mRunCmd = new Shell("SAM_TEST", "chmod 755 " + filepath, false);
                    mRunCmd.start();
                    return true;
                }
            } catch (Exception var9) {
                var9.printStackTrace();
                return false;
            }
        }
    }

    public String getVPNStatus() {
        return this.mVPNStatus;
    }

    public void setMsgHandler(Handler msgHandler) {
        this.MsgHandler = msgHandler;
    }

    public void setVPNInfo(String vpnaddr, int vpnport, String uname, String upwd) {
        this.VOneAddr = vpnaddr;
        this.VOnePort = vpnport;
        this.UserName = uname;
        this.UserPwd = upwd;
    }

    public void prepareVPNSettings() {
        this.PutClientProg(this.mSVAuthProg);
        this.PutClientProg(this.mSVClient);
        this.PutClientProg(this.mSVClientQuit);
        SVLoginParam LoginParam = SVLoginParam.getInstance();
        LoginParam.setAppFilesDir(this.mSVClientPath);
        LoginParam.setIsUseTop1(false);
        LoginParam.setVOneAddr(this.VOneAddr);
        LoginParam.setVOnePort(this.VOnePort);
        LoginParam.setUserName(this.UserName);
        LoginParam.setUserPwd(this.UserPwd);
        Log.i("VPNInterface", "prepareVPNSettings done");
    }

    public String getVPNError() {
        return this.mVPNError;
    }

    public void startVPN() {
        this.mVPNStatus = "0";
        this.mVPNError = "0";
        this.bRunningStatusThread = false;

        try {
            Thread.sleep(300L);
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        }

        if (!this.bRunningStatusThread) {
            new VPNInterface.GetVPNClientStatusrThread().start();
        }

        this.bRunningStatusThread = true;
        new VPNInterface.LoginVPNThread().start();
    }

    public void reLoginVPN() {
        this.mVPNStatus = "0";
        this.mVPNError = "0";
        Log.i("VPNInterface", "relogin to vpn");
        this.bRunningStatusThread = false;

        try {
            Thread.sleep(300L);
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        }

        if (!this.bRunningStatusThread) {
            new VPNInterface.GetVPNClientStatusrThread().start();
        }

        this.bRunningStatusThread = true;
        new VPNInterface.ReLoginVPNThread().start();
    }

    public void stopVPN() {
        this.mVPNStatus = "0";
        this.mVPNError = "0";
        this.bRunningStatusThread = false;
        this.bUsePreSetRes = false;
        this.ResList.clear();
        SVLoginParam LoginParam = SVLoginParam.getInstance();
        String svClientPath = LoginParam.getAppFilesDir() + "/quitvpn.flag";

        try {
            File e = new File(svClientPath);
            if (e.exists()) {
                e.delete();
            }

            FileWriter fileWriter = new FileWriter(svClientPath);
            fileWriter.write("1\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    private void writeConfigfile() throws IOException {
        SVLoginParam LoginParam = SVLoginParam.getInstance();
        String svClientPath = LoginParam.getAppFilesDir() + "/vonepf.conf";
        File file = new File(svClientPath);
        TopSVParam svparam = LoginParam.getTopSVParam();
        boolean nProtocolType = false;
        byte var14;
        if (svparam.UseInternationalProtocol) {
            var14 = 1;
        } else {
            var14 = 0;
        }

        if (file.exists()) {
            Log.v("VPNInterface", "exsit file delete");
            file.delete();
        }

        FileWriter fileWriter = new FileWriter(svClientPath);
        String[] param = new String[]{"voneaddr=" + LoginParam.getVOneAddr() + " ", "voneport=" + LoginParam.getVOnePort(), "username=" + LoginParam.getUserName() + " ", "userpwd=" + LoginParam.getUserPwd() + " ", "cgid= ", "Gid= ", "enablelog=1 ", "smiencrypt=0  ", "algoopt=0 ", "protocal=" + var14, "compress=0 ", "UseGM=0 ", "ProtocalType=" + var14, "UseAlgOptFlag=0"};

        for (int svauthretfile = 0; svauthretfile < param.length; ++svauthretfile) {
            fileWriter.write(String.valueOf(param[svauthretfile]) + "\n");
        }

        String var15 = LoginParam.getAppFilesDir();
        var15 = var15 + "/svauthretfile";
        ConfigReader reader = new ConfigReader(var15);
        if (this.bUsePreSetRes) {
            fileWriter.write("pfaclcnt=1\n");
            fileWriter.write("acladdr0 =" + this.m_ResAddr + "\n");
            fileWriter.write("aclport0=" + Integer.toString(this.m_ResPort) + "\n");
            fileWriter.write("acllocalport0=" + Integer.toString(this.m_ResLocalPort) + "\n");
        } else {
            int retNum = reader.getIntValue("ret", "ResNum");
            fileWriter.write("pfaclcnt=" + retNum + "\n");

            for (int i = 0; i < retNum; ++i) {
                String resIp = reader.getStringValue("ret", "IP" + i);
                int resPort = reader.getIntValue("ret", "Port" + i);
                fileWriter.write("acladdr" + Integer.toString(i) + "=" + resIp + "\n");
                fileWriter.write("aclport" + Integer.toString(i) + "=" + resPort + "\n");
                fileWriter.write("acllocalport" + Integer.toString(i) + "=" + Integer.toString(30000 + resPort + i) + "\n");
            }
        }

        fileWriter.write("sessionid=" + svparam.sessionid + "\n");
        fileWriter.write("reportjavamsgport=" + Integer.toString(this.m_GetClientMsgPort) + "\n");
        fileWriter.flush();
        fileWriter.close();
    }

    private static String escape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);

        for (int i = 0; i < src.length(); ++i) {
            char j = src.charAt(i);
            if (!Character.isDigit(j) && !Character.isLowerCase(j) && !Character.isUpperCase(j)) {
                if (j < 256) {
                    tmp.append("%");
                    if (j < 16) {
                        tmp.append("0");
                    }

                    tmp.append(Integer.toString(j, 16));
                } else {
                    tmp.append("%u");
                    tmp.append(Integer.toString(j, 16));
                }
            } else {
                tmp.append(j);
            }
        }

        return tmp.toString();
    }

    private int doGetSidByPwd() {
        SVLoginParam LoginParam = SVLoginParam.getInstance();
        TopSVParam param = LoginParam.getTopSVParam();
        String mProgName = LoginParam.getAppFilesDir() + "/svauthprog";
        boolean bTop1 = false;
        byte bTop11;
        if (LoginParam.getIsUseTop1()) {
            bTop11 = 1;
        } else {
            bTop11 = 0;
        }

        String mCmd = String.format("%s  cmdtype=%d voneaddr=%s voneport=%d bTop1=%d username=%s userpwd=%s scode=%s gsid=%s gcid=%s", new Object[]{mProgName, Integer.valueOf(3), LoginParam.getVOneAddr(), Integer.valueOf(LoginParam.getVOnePort()), Integer.valueOf(bTop11), escape(LoginParam.getUserName()), escape(LoginParam.getUserPwd()), LoginParam.getScode(), "", ""});
        Runtime runtime = Runtime.getRuntime();
        int exitCode = 2147483647;

        String svauthretfile;
        try {
            Process mProcess = runtime.exec(mCmd);
            mProcess.getOutputStream().close();
            mProcess.getErrorStream().close();
            InputStream is = mProcess.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr, 8192);
            svauthretfile = null;
            if (br != null) {
                while (br.readLine() != null) {
                    ;
                }
            }

            is.close();
            isr.close();
            br.close();

            try {
                exitCode = mProcess.waitFor();
            } catch (InterruptedException var15) {
                ;
            }
        } catch (IOException var16) {
            ;
        }

        svauthretfile = LoginParam.getAppFilesDir();
        svauthretfile = svauthretfile + "/svauthretfile";
        ConfigReader reader = new ConfigReader(svauthretfile);
        String ret = reader.getStringValue("ret", "PFOpen");
        param.PFOpen = reader.getBoolValue("ret", "PFOpen");
        param.NAOpen = reader.getBoolValue("ret", "NAOpen");
        param.UseSCBII = reader.getBoolValue("ret", "UseSCBII");
        param.UseCompress = reader.getBoolValue("ret", "UseCompress");
        param.UseAlgOpt = reader.getBoolValue("ret", "UseAlgOpt");
        param.UseInternationalProtocol = reader.getBoolValue("ret", "UseInternationalProtocol");
        param.sessionid = reader.getStringValue("ret", "sessionid");
        param.pMsg = reader.getStringValue("ret", "pMsg");
        param.CallFuncRet = reader.getIntValue("ret", "CallFuncRet");
        param.useSMSCode = reader.getIntValue("ret", "UseSMS");
        param.useChallengeCode = reader.getIntValue("ret", "UseChallenge");
        param.szCryptSMS = reader.getStringValue("ret", "szCryptSMS");
        param.szCryptServerTime = reader.getStringValue("ret", "szCryptServerTime");
        param.ChallengeState = reader.getStringValue("ret", "szChallenge_state");
        param.szCryptUserName = reader.getStringValue("ret", "szCryptUserName");
        param.szCryptPwd = reader.getStringValue("ret", "szCryptPwd");
        LoginParam.setGetSidByUPwdRet(param);
        return param.CallFuncRet;
    }

    private int RedoGetSidByPwd() {
        SVLoginParam LoginParam = SVLoginParam.getInstance();
        TopSVParam param = LoginParam.getTopSVParam();
        String mProgName = LoginParam.getAppFilesDir() + "/svauthprog";
        boolean bTop1 = false;
        byte bTop11;
        if (LoginParam.getIsUseTop1()) {
            bTop11 = 1;
        } else {
            bTop11 = 0;
        }

        String mCmd = String.format("%s  cmdtype=%d voneaddr=%s voneport=%d bTop1=%d username=%s userpwd=%s scode=%s gsid=%s gcid=%s sv_un=%s ReLoginFlag=1", new Object[]{mProgName, Integer.valueOf(3), LoginParam.getVOneAddr(), Integer.valueOf(LoginParam.getVOnePort()), Integer.valueOf(bTop11), escape(LoginParam.getUserName()), escape(LoginParam.getUserPwd()), LoginParam.getScode(), "", "", LoginParam.getUserName()});
        Log.i("VPNInterface", "relogin mCmd = " + mCmd);
        Runtime runtime = Runtime.getRuntime();
        int exitCode = 2147483647;

        String svauthretfile;
        try {
            Process mProcess = runtime.exec(mCmd);
            mProcess.getOutputStream().close();
            mProcess.getErrorStream().close();
            InputStream is = mProcess.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr, 8192);
            svauthretfile = null;
            if (br != null) {
                while ((svauthretfile = br.readLine()) != null) {
                    Log.i("VPNInterface", svauthretfile);
                }
            }

            is.close();
            isr.close();
            br.close();

            try {
                exitCode = mProcess.waitFor();
            } catch (InterruptedException var15) {
                ;
            }
        } catch (IOException var16) {
            ;
        }

        svauthretfile = LoginParam.getAppFilesDir();
        svauthretfile = svauthretfile + "/svauthretfile";
        ConfigReader reader = new ConfigReader(svauthretfile);
        String ret = reader.getStringValue("ret", "PFOpen");
        param.PFOpen = reader.getBoolValue("ret", "PFOpen");
        param.NAOpen = reader.getBoolValue("ret", "NAOpen");
        param.UseSCBII = reader.getBoolValue("ret", "UseSCBII");
        param.UseCompress = reader.getBoolValue("ret", "UseCompress");
        param.UseAlgOpt = reader.getBoolValue("ret", "UseAlgOpt");
        param.UseInternationalProtocol = reader.getBoolValue("ret", "UseInternationalProtocol");
        param.sessionid = reader.getStringValue("ret", "sessionid");
        param.pMsg = reader.getStringValue("ret", "pMsg");
        param.CallFuncRet = reader.getIntValue("ret", "CallFuncRet");
        param.useSMSCode = reader.getIntValue("ret", "UseSMS");
        param.useChallengeCode = reader.getIntValue("ret", "UseChallenge");
        param.szCryptSMS = reader.getStringValue("ret", "szCryptSMS");
        param.szCryptServerTime = reader.getStringValue("ret", "szCryptServerTime");
        param.ChallengeState = reader.getStringValue("ret", "szChallenge_state");
        param.szCryptUserName = reader.getStringValue("ret", "szCryptUserName");
        param.szCryptPwd = reader.getStringValue("ret", "szCryptPwd");
        LoginParam.setGetSidByUPwdRet(param);
        return param.CallFuncRet;
    }

    private int doGetResList() {
        SVLoginParam LoginParam = SVLoginParam.getInstance();
        TopSVParam param = LoginParam.getTopSVParam();
        String sessionid = param.sessionid;
        sessionid = sessionid.replace('&', '.');
        String mProgName = LoginParam.getAppFilesDir() + "/svauthprog";
        boolean bTop1 = false;
        byte bTop11;
        if (LoginParam.getIsUseTop1()) {
            bTop11 = 1;
        } else {
            bTop11 = 0;
        }

        String mCmd = String.format("%s  cmdtype=%d voneaddr=%s voneport=%d bTop1=%d sessionid=%s", new Object[]{mProgName, Integer.valueOf(11), LoginParam.getVOneAddr(), Integer.valueOf(LoginParam.getVOnePort()), Integer.valueOf(bTop11), sessionid});
        Runtime runtime = Runtime.getRuntime();
        int exitCode = 2147483647;

        String svauthretfile;
        try {
            Process mProcess = runtime.exec(mCmd);
            mProcess.getOutputStream().close();
            mProcess.getErrorStream().close();
            InputStream is = mProcess.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr, 8192);
            svauthretfile = null;
            if (br != null) {
                while ((svauthretfile = br.readLine()) != null) {
                    ;
                }
            }

            is.close();
            isr.close();
            br.close();

            try {
                exitCode = mProcess.waitFor();
            } catch (InterruptedException var16) {
                ;
            }
        } catch (IOException var17) {
            ;
        }

        svauthretfile = LoginParam.getAppFilesDir();
        svauthretfile = svauthretfile + "/svauthretfile";
        ConfigReader reader = new ConfigReader(svauthretfile);
        param.CallFuncRet = reader.getIntValue("ret", "CallFuncRet");
        LoginParam.setGetRedListRet(param);
        return param.CallFuncRet;
    }

    private void SendMsg(Handler msghandler, int msgid, Bundle bundle) {
        if (msghandler != null) {
            if (msgid != 0) {
                Message message = Message.obtain(msghandler);
                message.what = msgid;
                if (bundle != null) {
                    message.obj = bundle;
                }

                message.sendToTarget();
            }
        }
    }

    private int GetErrCodeFromMsg(String msg) {
        boolean head = false;
        boolean end = false;
        String RETFLAG = "X-sv-ret:";
        int var6;
        if ((var6 = msg.indexOf(RETFLAG)) < 0) {
            return 0;
        } else {
            for (var6 += RETFLAG.length(); !Character.isDigit(msg.charAt(var6)); ++var6) {
                ;
            }

            int var7;
            for (var7 = var6; var7 < msg.length() && Character.isDigit(msg.charAt(var7)); ++var7) {
                ;
            }

            int nRet = Integer.parseInt(msg.substring(var6, var7).trim());
            return nRet;
        }
    }

    private class GetVPNClientStatusrThread extends Thread {
        private GetVPNClientStatusrThread() {
        }

        public void run() {
            try {
                byte[] ex = new byte[256];
                InetAddress IPAddress = InetAddress.getLocalHost();
                DatagramSocket socket = null;
                VPNInterface.this.m_GetClientMsgPort = '谼';
                VPNInterface.this.bGetClientMsgSocketOK = false;

                try {
                    socket = new DatagramSocket(VPNInterface.this.m_GetClientMsgPort, IPAddress);
                } catch (SocketException var18) {
                    Log.i("VPNInterface", "DatagramSocket err:  " + var18.getMessage());
                    VPNInterface.this.m_GetClientMsgPort = (int) (35900.0D + Math.random() * 100.0D);
                    socket = new DatagramSocket(VPNInterface.this.m_GetClientMsgPort, IPAddress);
                } finally {
                    ;
                }

                VPNInterface.this.bGetClientMsgSocketOK = true;

                while (VPNInterface.this.bRunningStatusThread) {
                    try {
                        DatagramPacket packet = new DatagramPacket(ex, ex.length);
                        socket.setSoTimeout(200);
                        socket.receive(packet);
                        String e = new String(packet.getData(), 0, packet.getLength());
                        SVLoginParam bundle;
                        String svportswiftPath;
                        ConfigReader reader;
                        int nResCnt;
                        HashMap resitem;
                        if (e.equalsIgnoreCase("6")) {
                            VPNInterface.this.mVPNError = "0";
                            VPNInterface.this.mVPNStatus = "6";
                            bundle = SVLoginParam.getInstance();
                            svportswiftPath = bundle.getAppFilesDir() + "/svauthretfile";
                            reader = new ConfigReader(svportswiftPath);
                            int ret = reader.getIntValue("ret", "ResNum");
                            VPNInterface.this.ResList.clear();
                            if (VPNInterface.this.bUsePreSetRes) {
                                HashMap var26 = new HashMap();
                                var26.put("resip", VPNInterface.this.m_ResAddr);
                                var26.put("resport", Integer.toString(VPNInterface.this.m_ResPort));
                                var26.put("reslocalport", Integer.toString(VPNInterface.this.m_ResLocalPort));
                                VPNInterface.this.ResList.add(var26);
                            } else {
                                for (nResCnt = 0; nResCnt < ret; ++nResCnt) {
                                    String bundle1 = reader.getStringValue("ret", "IP" + nResCnt);
                                    int valField = reader.getIntValue("ret", "Port" + nResCnt);
                                    resitem = new HashMap();
                                    resitem.put("resip", bundle1);
                                    resitem.put("resport", Integer.toString(valField));
                                    resitem.put("reslocalport", Integer.toString(30000 + valField + nResCnt));
                                    VPNInterface.this.ResList.add(resitem);
                                }
                            }

                            Bundle var24 = new Bundle();
                            var24.putString("vpnstatus", VPNInterface.this.mVPNStatus);
                            var24.putString("vpnerror", VPNInterface.this.mVPNError);
                            VPNInterface.this.SendMsg(VPNInterface.this.MsgHandler, VPNInterface.this.VPNMsgID, var24);
                            Log.i("Status", "VPN is OK");
                        }

                        if (!e.equalsIgnoreCase("7")) {
                            Bundle var22;
                            if (e.equalsIgnoreCase("200")) {
                                VPNInterface.this.mVPNError = "0";
                                VPNInterface.this.mVPNStatus = "200";
                                var22 = new Bundle();
                                var22.putString("vpnstatus", VPNInterface.this.mVPNStatus);
                                var22.putString("vpnerror", VPNInterface.this.mVPNError);
                                VPNInterface.this.SendMsg(VPNInterface.this.MsgHandler, VPNInterface.this.VPNMsgID, var22);
                                Log.i("Status", "VPN is timeout");
                                break;
                            }

                            if (e.equalsIgnoreCase("255")) {
                                VPNInterface.this.mVPNError = "0";
                                VPNInterface.this.mVPNStatus = "0";
                                var22 = new Bundle();
                                var22.putString("vpnstatus", VPNInterface.this.mVPNStatus);
                                var22.putString("vpnerror", VPNInterface.this.mVPNError);
                                VPNInterface.this.SendMsg(VPNInterface.this.MsgHandler, VPNInterface.this.VPNMsgID, var22);
                                Log.i("Status", "VPN is Closed");
                                break;
                            }
                        } else {
                            bundle = SVLoginParam.getInstance();
                            svportswiftPath = bundle.getAppFilesDir() + "/portswift.ini";
                            reader = new ConfigReader(svportswiftPath);
                            VPNInterface.this.mUDPCmdPort = reader.getIntValue("portswift", "udpcmdport");
                            nResCnt = reader.getIntValue("portswift", "rescnt");
                            VPNInterface.this.ResList.clear();

                            for (int var25 = 0; var25 < nResCnt; ++var25) {
                                resitem = new HashMap();
                                String var27 = String.format("resip%d", new Object[]{Integer.valueOf(var25)});
                                String var23 = reader.getStringValue("portswift", var27);
                                resitem.put("resip", var23);
                                var27 = String.format("resport%d", new Object[]{Integer.valueOf(var25)});
                                var23 = reader.getStringValue("portswift", var27);
                                resitem.put("resport", var23);
                                var27 = String.format("reslocalport%d", new Object[]{Integer.valueOf(var25)});
                                var23 = reader.getStringValue("portswift", var27);
                                resitem.put("reslocalport", var23);
                                VPNInterface.this.ResList.add(resitem);
                            }

                            Log.i("SDKLib", "ResList is : " + VPNInterface.this.ResList);
                            VPNInterface.this.mVPNError = "0";
                            VPNInterface.this.mVPNStatus = "6";
                            Bundle var28 = new Bundle();
                            var28.putString("vpnstatus", VPNInterface.this.mVPNStatus);
                            var28.putString("vpnerror", VPNInterface.this.mVPNError);
                            VPNInterface.this.SendMsg(VPNInterface.this.MsgHandler, VPNInterface.this.VPNMsgID, var28);
                            Log.i("Status", "VPN is OK with port swift");
                        }
                    } catch (SocketTimeoutException var20) {
                        if (!VPNInterface.this.bRunningStatusThread) {
                            VPNInterface.this.mVPNError = "0";
                            VPNInterface.this.mVPNStatus = "0";
                            break;
                        }
                    }
                }

                socket.close();
            } catch (Exception var21) {
                var21.printStackTrace();
            }

        }
    }

    private class LoginVPNThread extends Thread {
        private LoginVPNThread() {
        }

        public void run() {
            try {
                boolean e = false;
                int e2 = VPNInterface.this.doGetSidByPwd();
                SVLoginParam LoginParam = SVLoginParam.getInstance();
                TopSVParam param = LoginParam.getTopSVParam();
                int cmd;
                Bundle e1;
                if (e2 != 0) {
                    if (param.pMsg.length() > 0) {
                        cmd = VPNInterface.this.GetErrCodeFromMsg(param.pMsg);
                        if (cmd == '鲍') {
                            VPNInterface.this.bRunningStatusThread = false;
                            Thread.sleep(1000L);
                            e1 = new Bundle();
                            VPNInterface.this.mVPNError = "10";
                            VPNInterface.this.mVPNStatus = "0";
                            e1.putString("vpnerror", VPNInterface.this.mVPNError);
                            e1.putString("vpnstatus", VPNInterface.this.mVPNStatus);
                            VPNInterface.this.SendMsg(VPNInterface.this.MsgHandler, VPNInterface.this.VPNMsgID, e1);
                            Log.e("VPNInterface", "VPN登陆失败");
                            return;
                        }

                        VPNInterface.this.bRunningStatusThread = false;
                        Thread.sleep(1000L);
                        e1 = new Bundle();
                        VPNInterface.this.mVPNError = "" + cmd;
                        VPNInterface.this.mVPNStatus = "0";
                        e1.putString("vpnerror", VPNInterface.this.mVPNError);
                        e1.putString("vpnstatus", VPNInterface.this.mVPNStatus);
                        VPNInterface.this.SendMsg(VPNInterface.this.MsgHandler, VPNInterface.this.VPNMsgID, e1);
                        Log.e("VPNInterface", "VPN登陆失败");
                        VPNInterface.this.bRunningStatusThread = false;
                        return;
                    }
                } else if (param.pMsg.length() > 0) {
                    cmd = VPNInterface.this.GetErrCodeFromMsg(param.pMsg);
                    VPNInterface.this.bRunningStatusThread = false;
                    Thread.sleep(1000L);
                    e1 = new Bundle();
                    VPNInterface.this.mVPNError = "" + cmd;
                    VPNInterface.this.mVPNStatus = "0";
                    e1.putString("vpnerror", VPNInterface.this.mVPNError);
                    e1.putString("vpnstatus", VPNInterface.this.mVPNStatus);
                    VPNInterface.this.SendMsg(VPNInterface.this.MsgHandler, VPNInterface.this.VPNMsgID, e1);
                    Log.e("VPNInterface", "0000VPN登陆失败");
                    VPNInterface.this.bRunningStatusThread = false;
                    return;
                }

                e2 = VPNInterface.this.doGetResList();
                LoginParam = SVLoginParam.getInstance();
                if (e2 != 0) {
                    Bundle cmd2 = new Bundle();
                    VPNInterface.this.mVPNError = "" + e2;
                    VPNInterface.this.mVPNStatus = "0";
                    cmd2.putString("vpnerror", VPNInterface.this.mVPNError);
                    cmd2.putString("vpnstatus", VPNInterface.this.mVPNStatus);
                    VPNInterface.this.SendMsg(VPNInterface.this.MsgHandler, VPNInterface.this.VPNMsgID, cmd2);
                    Log.e("VPNInterface", "VPN获取资源失败");
                    VPNInterface.this.new PostLogOutThread().start();
                    VPNInterface.this.bRunningStatusThread = false;
                    return;
                }

                String cmd1 = LoginParam.getAppFilesDir();
                cmd1 = cmd1 + "/svauthretfile";
                ConfigReader e3 = new ConfigReader(cmd1);
                int resnum = e3.getIntValue("ret", "ResNum");
                if (resnum == 0) {
                    Log.e("VPNInterface", "没有可用的VPN资源，请与管理员联系查看VPN的配置是否正确");
                    VPNInterface.this.bRunningStatusThread = false;
                    VPNInterface.this.new PostLogOutThread().start();
                    return;
                }

                cmd1 = VPNInterface.this.mSVClientPath + "/" + VPNInterface.this.mSVClient;

                while (!VPNInterface.this.bGetClientMsgSocketOK) {
                    Thread.sleep(100L);
                }

                try {
                    VPNInterface.this.writeConfigfile();
                    Runtime.getRuntime().exec(cmd1);
                } catch (IOException var7) {
                    Log.i("VPNInterface", "writeConfigfile or run cmd err");
                }
            } catch (Exception var8) {
                var8.printStackTrace();
            }

        }
    }

    private class PostLogOutThread extends Thread {
        private PostLogOutThread() {
        }

        public void run() {
            try {
                SVLoginParam LoginParam = SVLoginParam.getInstance();
                TopSVParam param = LoginParam.getTopSVParam();
                String sessionid = param.sessionid;
                sessionid = sessionid.replace('&', '.');
                String mProgName = LoginParam.getAppFilesDir() + "/svauthprog";
                boolean bTop1 = false;
                byte bTop11;
                if (LoginParam.getIsUseTop1()) {
                    bTop11 = 1;
                } else {
                    bTop11 = 0;
                }

                String mCmd = String.format("%s  cmdtype=%d voneaddr=%s voneport=%d bTop1=%d sessionid=%s", new Object[]{mProgName, Integer.valueOf(7), LoginParam.getVOneAddr(), Integer.valueOf(LoginParam.getVOnePort()), Integer.valueOf(bTop11), sessionid});
                Runtime runtime = Runtime.getRuntime();
                int exitCode = 2147483647;

                try {
                    Process e = runtime.exec(mCmd);
                    e.getOutputStream().close();
                    e.getErrorStream().close();
                    InputStream is = e.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr, 8192);
                    String e1 = null;
                    if (br != null) {
                        while ((e1 = br.readLine()) != null) {
                            ;
                        }
                    }

                    is.close();
                    isr.close();
                    br.close();

                    try {
                        exitCode = e.waitFor();
                    } catch (InterruptedException var16) {
                        ;
                    }
                } catch (IOException var17) {
                    Log.e("VPNInterface", String.format("Run Cmd %s error: %s", new Object[]{mCmd, var17}));
                }
            } catch (Exception var18) {
                var18.printStackTrace();
            }

        }
    }

    private class ReLoginVPNThread extends Thread {
        private ReLoginVPNThread() {
        }

        public void run() {
            try {
                boolean e = false;
                int e2 = VPNInterface.this.RedoGetSidByPwd();
                SVLoginParam cmd;
                if (e2 != 0) {
                    cmd = SVLoginParam.getInstance();
                    TopSVParam e3 = cmd.getTopSVParam();
                    if (e3.pMsg.length() > 0) {
                        int reader1 = VPNInterface.this.GetErrCodeFromMsg(e3.pMsg);
                        if (reader1 == '鲍') {
                            Bundle resnum1 = new Bundle();
                            VPNInterface.this.mVPNError = "10";
                            VPNInterface.this.mVPNStatus = "0";
                            resnum1.putString("vpnerror", VPNInterface.this.mVPNError);
                            resnum1.putString("vpnstatus", VPNInterface.this.mVPNStatus);
                            VPNInterface.this.SendMsg(VPNInterface.this.MsgHandler, VPNInterface.this.VPNMsgID, resnum1);
                            Log.e("VPNInterface", "VPN登陆失败");
                            return;
                        }
                    }

                    Bundle reader2 = new Bundle();
                    VPNInterface.this.mVPNError = "" + e2;
                    VPNInterface.this.mVPNStatus = "0";
                    reader2.putString("vpnerror", VPNInterface.this.mVPNError);
                    reader2.putString("vpnstatus", VPNInterface.this.mVPNStatus);
                    VPNInterface.this.SendMsg(VPNInterface.this.MsgHandler, VPNInterface.this.VPNMsgID, reader2);
                    Log.e("VPNInterface", "VPN登陆失败");
                    VPNInterface.this.bRunningStatusThread = false;
                    return;
                }

                e2 = VPNInterface.this.doGetResList();
                if (e2 != 0) {
                    Bundle cmd2 = new Bundle();
                    VPNInterface.this.mVPNError = "" + e2;
                    VPNInterface.this.mVPNStatus = "0";
                    cmd2.putString("vpnerror", VPNInterface.this.mVPNError);
                    cmd2.putString("vpnstatus", VPNInterface.this.mVPNStatus);
                    VPNInterface.this.SendMsg(VPNInterface.this.MsgHandler, VPNInterface.this.VPNMsgID, cmd2);
                    Log.e("VPNInterface", "VPN获取资源失败");
                    VPNInterface.this.new PostLogOutThread().start();
                    VPNInterface.this.bRunningStatusThread = false;
                    return;
                }

                cmd = SVLoginParam.getInstance();
                String e1 = cmd.getAppFilesDir();
                e1 = e1 + "/svauthretfile";
                ConfigReader reader = new ConfigReader(e1);
                int resnum = reader.getIntValue("ret", "ResNum");
                if (resnum == 0) {
                    Log.e("VPNInterface", "没有可用的VPN资源，请与管理员联系查看VPN的配置是否正确");
                    VPNInterface.this.bRunningStatusThread = false;
                    VPNInterface.this.new PostLogOutThread().start();
                    return;
                }

                String cmd1 = VPNInterface.this.mSVClientPath + "/" + VPNInterface.this.mSVClient;

                try {
                    VPNInterface.this.writeConfigfile();
                    Runtime.getRuntime().exec(cmd1);
                } catch (IOException var6) {
                    Log.i("VPNInterface", "writeConfigfile or run cmd err");
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }

        }
    }
}