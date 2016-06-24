package me.tools.vpn.ssl;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;

public class SVLoginParam {
    private static SVLoginParam singleparam = null;
    private TopSVParam topsvparam = new TopSVParam();
    private String VOneAddr = "";
    private int VOnePort = 443;
    private String UserName = "";
    private String UserPwd = "";
    private String CertPath = "";
    private String CertPwd = "";
    private int LoginType = 0;
    private String Scode = "1010101010101010101010101010101";
    private String CertMD5 = "";
    private boolean IsUseTop1 = false;
    private String UserInputGid;
    private String UserInputSMS;
    private String AppFilesDir;
    private Context AppContext;

    private SVLoginParam() {
    }

    public static synchronized SVLoginParam getInstance() {
        if(singleparam == null) {
            singleparam = new SVLoginParam();
        }

        return singleparam;
    }

    public void setCertPwd(String certPwd) {
        this.CertPwd = certPwd;
    }

    public String getCertPwd() {
        return this.CertPwd;
    }

    public void setUserInputSMS(String userInputSMS) {
        this.UserInputSMS = userInputSMS;
    }

    public String getUserInputSMS() {
        return this.UserInputSMS;
    }

    public void setAppContext(Context appContext) {
        this.AppContext = appContext;
    }

    public Context getAppContext() {
        return this.AppContext;
    }

    public void setAppFilesDir(String appFilesDir) {
        this.AppFilesDir = appFilesDir;
    }

    public String getAppFilesDir() {
        return this.AppFilesDir;
    }

    public void setUserInputGid(String userInputGid) {
        this.UserInputGid = userInputGid;
    }

    public String getUserInputGid() {
        return this.UserInputGid;
    }

    public void setIsUseTop1(boolean isUseTop1) {
        this.IsUseTop1 = isUseTop1;
    }

    public boolean getIsUseTop1() {
        return this.IsUseTop1;
    }

    public void setVOneAddr(String VOneAddr) {
        this.VOneAddr = VOneAddr;
    }

    public String getVOneAddr() {
        return this.VOneAddr;
    }

    public void setVOnePort(int vOnePort) {
        this.VOnePort = vOnePort;
    }

    public int getVOnePort() {
        return this.VOnePort;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getUserName() {
        return this.UserName;
    }

    public void setUserPwd(String userPwd) {
        this.UserPwd = userPwd;
    }

    public String getUserPwd() {
        return this.UserPwd;
    }

    public void setLoginType(int loginType) {
        this.LoginType = loginType;
    }

    public int getLoginType() {
        return this.LoginType;
    }

    public void setCertPath(String certPath) {
        this.CertPath = certPath;
    }

    public String getCertPath() {
        return this.CertPath;
    }

    public void setCertMD5(String certMD5) {
        this.CertMD5 = certMD5;
    }

    public String getCertMD5() {
        return this.CertMD5;
    }

    public void setScode(String scode) {
        this.Scode = scode;
    }

    public String getScode() {
        return this.Scode;
    }

    public TopSVParam getTopSVParam() {
        return this.topsvparam;
    }

    public void setGetLoginParamRet(TopSVParam ret) {
        this.topsvparam.Auth_Cert_Enable = ret.Auth_Cert_Enable;
        this.topsvparam.Auth_Passwd_Enable = ret.Auth_Passwd_Enable;
        this.topsvparam.Auth_TwoFactor_Enable = ret.Auth_TwoFactor_Enable;
        this.topsvparam.GidType = ret.GidType;
        this.topsvparam.CallFuncRet = ret.CallFuncRet;
    }

    public void setGetSidByUPwdRet(TopSVParam ret) {
        this.topsvparam.PFOpen = ret.PFOpen;
        this.topsvparam.NAOpen = ret.NAOpen;
        this.topsvparam.UseSCBII = ret.UseSCBII;
        this.topsvparam.UseCompress = ret.UseCompress;
        this.topsvparam.UseAlgOpt = ret.UseAlgOpt;
        this.topsvparam.UseInternationalProtocol = ret.UseInternationalProtocol;
        this.topsvparam.sessionid = ret.sessionid;
        this.topsvparam.pMsg = ret.pMsg;
        this.topsvparam.CallFuncRet = ret.CallFuncRet;
        this.topsvparam.certmd5 = ret.certmd5;
        this.topsvparam.useSMSCode = ret.useSMSCode;
        this.topsvparam.useChallengeCode = ret.useChallengeCode;
        this.topsvparam.szCryptSMS = ret.szCryptSMS;
        this.topsvparam.szCryptServerTime = ret.szCryptServerTime;
        this.topsvparam.ChallengeState = ret.ChallengeState;
        this.topsvparam.szCryptUserName = ret.szCryptUserName;
        this.topsvparam.szCryptPwd = ret.szCryptPwd;
    }

    public void setGetRedListRet(TopSVParam ret) {
        this.topsvparam.CallFuncRet = ret.CallFuncRet;
    }

    public void setGetGidRet(TopSVParam ret) {
        this.topsvparam.CallFuncRet = ret.CallFuncRet;
        this.topsvparam.GidDatLen = ret.GidDatLen;
        this.topsvparam.GidData = ret.GidData;
    }
}
