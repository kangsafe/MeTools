package me.tools.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import me.tools.times.SntpClient;
import me.tools.transform.FileHelper;

public class MainActivity extends AppCompatActivity {

    private TextView tv = null;
    private Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.time);
        btn = (Button) findViewById(R.id.time_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        SntpClient client = new SntpClient();
                        System.out.println("begin request");
                        if (client.requestTime("cn.pool.ntp.org", 30000)) {
                            System.out.println("end request");
                            long now = client.getNtpTime() + System.nanoTime() / 1000 - client.getNtpTimeReference();
                            String str = FileHelper.dateToString(now);
                            Log.i("nihao", str);
                            //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                        }
                    }
                }.start();
            }
        });
    }
}
