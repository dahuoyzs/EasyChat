package com.bigfire.easychat.fxaudio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/31  6:39
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class MyFrame extends JFrame {
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

        jButton1.addActionListener(e->{
            System.out.println("开始被点击");
            VoiceRecorder.captureAudio();
        });

        jButton2.addActionListener(e->{
            System.out.println("停止被点击");
            VoiceUtil.setRecording(false);
        });

    }
}
