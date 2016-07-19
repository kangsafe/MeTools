package me.tools.vpn.ssl;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Shell extends Thread {
    private static final boolean LOCAL_LOGD = true;
    public static final boolean SU = true;
    public static final boolean SH = false;
    private final String mTag;
    private final String mCmd;
    private final boolean mRoot;
    private Process mProcess;
    private DataOutputStream ostream;
    private BufferedReader br;
    private InputStream is;
    private InputStreamReader isr;

    public Shell(String tag, String cmd, boolean root) {
        super(tag + "-stdin");
        this.mTag = tag;
        this.mCmd = cmd;
        this.mRoot = root;
    }

    public final void run() {
        try {
            Runtime e = Runtime.getRuntime();
            if(this.mRoot) {
                this.mProcess = e.exec("su");
                this.ostream = new DataOutputStream(this.mProcess.getOutputStream());
                if(this.ostream != null) {
                    this.ostream.writeBytes(this.mCmd);
                    this.ostream.writeBytes("exit\n");
                    this.ostream.flush();
                    this.ostream.close();
                }
            } else {
                this.mProcess = e.exec(this.mCmd);
            }
        } catch (IOException var10) {
            Log.e(this.mTag, String.format("Run Cmd %s error: %s", new Object[]{this.mCmd, var10}));
        } finally {
            try {
                if(this.ostream != null) {
                    this.ostream.close();
                }
            } catch (Exception var9) {
                Log.e("SAM_TEST", "closing DataOutputStream", var9);
            }

            this.onCmdTerminated();
        }

    }

    protected void onStdout(String line) {
    }

    protected void onStderr(String line) {
    }

    protected void onCmdStarted() {
    }

    protected void onCmdTerminated() {
    }

    public final void joinLoggers() throws InterruptedException {
    }

    public final int waitForQuietly() {
        int exitCode = 2147483647;
        if(this.mProcess != null) {
            try {
                this.mProcess.getOutputStream().close();
                this.mProcess.getErrorStream().close();
                this.is = this.mProcess.getInputStream();
                this.isr = new InputStreamReader(this.is);
                this.br = new BufferedReader(this.isr, 8192);
                String e = null;
                if(this.br != null) {
                    while((e = this.br.readLine()) != null) {
                        this.onStdout(e);
                    }
                }

                this.is.close();
                this.isr.close();
                this.br.close();
            } catch (OutOfMemoryError var25) {
                Log.e("SAM_TEST", "OutOfMemoryError", var25);
            } catch (Exception var26) {
                ;
            } finally {
                try {
                    if(this.is != null) {
                        this.is.close();
                    }
                } catch (Exception var23) {
                    Log.e("SAM_TEST", "closing InputStream", var23);
                }

                try {
                    if(this.isr != null) {
                        this.isr.close();
                    }
                } catch (Exception var22) {
                    Log.e("SAM_TEST", "closing InputStreamReader", var22);
                }

                try {
                    if(this.br != null) {
                        this.br.close();
                    }
                } catch (Exception var21) {
                    Log.e("SAM_TEST", "closing BufferedReader", var21);
                }

            }

            try {
                exitCode = this.mProcess.waitFor();
            } catch (InterruptedException var24) {
                ;
            }

            this.mProcess.destroy();
        }

        return exitCode;
    }
}