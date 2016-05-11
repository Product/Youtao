package com.youyou.shopping;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.androidannotations.annotations.Background;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    private String uri = "http://120.26.75.225:8090/uumall/itf/mall/slider.json";

    @Background
    void text() {
        try {
            URL url = new URL(uri);
//            StringBuffer param = new StringBuffer();
//            param.append();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000); //设置连接超时为5秒
            conn.setRequestMethod("POST"); //设定请求方式
            conn.setDoOutput(true);
//            conn.getOutputStream().write();
            conn.connect(); //建立到远程对象的实际连接
            //返回打开连接读取的输入流
            InputStream inputStream = conn.getInputStream();

            byte[] bys = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bys)) != -1) {
                String s = new String(bys, "utf-8");
                System.err.println(s);
//                for (int i = 0; i <bys.length ; i++) {
//                    System.out.print((char)bys[i]);
////                    log.d(bys[i]);
//                }
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}