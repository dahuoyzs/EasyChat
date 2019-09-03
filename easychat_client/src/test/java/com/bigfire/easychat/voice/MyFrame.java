package com.bigfire.easychat.voice;



import cn.hutool.core.util.StrUtil;
import com.baidu.aip.speech.AipSpeech;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/31  6:39
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class MyFrame extends JFrame {
    public static final String APP_ID = "10790983";
    public static final String API_KEY = "3jfvvxihnPyBBWSVykRd40kN";
    public static final String SECRET_KEY = "keID4x33wtzpgEvwG9DwZ6dOEyrZlZIl";
    public static void main(String[] args) {
        MyFrame myFrame = new MyFrame();
        myFrame.setBounds(300,300,300,300);
        myFrame.setLayout(new FlowLayout());
        JButton jButton1 = new JButton("开始");
        JButton jButton2 = new JButton("停止");
        myFrame.add(jButton1);
        myFrame.add(jButton2);
        myFrame.setDefaultCloseOperation(3);
        myFrame.setVisible(true);

        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        Audio audio = new Audio();
        audio.setVoiceStopListener(data -> {
            audio.playAudio(data);
            JSONObject res = client.asr(data, "pcm", 16000, null);
            JSONArray jsonArray = res.getJSONArray("result");
            String resultStr = jsonArray.getString(0);

            if (resultStr.contains("百度")){
                String content = StrUtil.subAfter(resultStr,"百度",false);
                String url = "https://www.baidu.com/s?word="+content;
                executeCmd("cmd /c start "+url);
            }
            if (resultStr.contains("官网")){
                executeCmd("cmd /c start "+"www.ibigfire.cn");
            }
            if (resultStr.contains("计算器")){
                executeCmd("calc");
            }
            if (resultStr.contains("笔记本")){
                executeCmd("notepad");
            }
            if (resultStr.contains("一分关机")){
                executeCmd("shutdown -s -f -t 60");
            }
            if (resultStr.contains("取消关机")){
                System.out.println("取消关机命令被执行");
                executeCmd("shutdown /a");
            }
            if (resultStr.contains("关掉QQ")){
                executeCmd("taskkill /f /im *qq*.exe");
            }
            if (resultStr.contains("我的电脑")){
                executeCmd("Explorer.exe /s,");
            }

//            System.out.println(res);
//            System.out.println(res.toString(2));
        });
        jButton1.addActionListener(e->{
            System.out.println("开始被点击");
            audio.captureAudio();
        });

        jButton2.addActionListener(e->{
            System.out.println("停止被点击");
            audio.stop();
        });

    }
    public static void executeCmd(String cmdStr) {
        try {
            Runtime.getRuntime().exec(cmdStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
