package me.tools.vpn.ssl;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public class TopSVParam {
    public boolean PFOpen = false;
    public boolean NAOpen = false;
    public boolean UseSCBII = false;
    public boolean UseCompress = false;
    public boolean UseAlgOpt = false;
    public boolean UseInternationalProtocol = true;
    public boolean Auth_Passwd_Enable = true;
    public boolean Auth_Cert_Enable = false;
    public boolean Auth_TwoFactor_Enable = false;
    public int GidType = 0;
    public int LoginErrorCount = 0;
    public String ServerGid = "";
    public String sessionid = "";
    public int GidDatLen = 0;
    public byte[] GidData;
    public String pMsg = "";
    public String certmd5 = "";
    public int CallFuncRet;
    public int maxSMSTimes = 0;
    public int maxSMSTimeOut = 0;
    public int useSMSCode = 0;
    public int useChallengeCode = 0;
    public String ChallengeState = "";
    public String szCryptUserName = "";
    public String szCryptServerTime = "";
    public String szCryptSMS = "";
    public String szCryptPwd = "";

    public TopSVParam() {
    }
}