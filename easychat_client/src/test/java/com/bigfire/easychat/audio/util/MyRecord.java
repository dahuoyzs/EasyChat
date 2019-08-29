package com.bigfire.easychat.audio.util;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/29  21:21
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;

public class MyRecord extends JFrame implements ActionListener{
    public static void main(String[] args) {
        //创造一个实例
        MyRecord mr = new MyRecord();
    }
    //定义录音格式
    AudioFormat af = null;
    //定义目标数据行,可以从中读取音频数据,该 TargetDataLine 接口提供从目标数据行的缓冲区读取所捕获数据的方法。
    TargetDataLine td = null;
    //定义源数据行,源数据行是可以写入数据的数据行。它充当其混频器的源。应用程序将音频字节写入源数据行，这样可处理字节缓冲并将它们传递给混频器。
    SourceDataLine sd = null;
    //定义字节数组输入输出流
    ByteArrayInputStream bais = null;
    ByteArrayOutputStream baos = null;
    //定义音频输入流
    AudioInputStream ais = null;
    //定义停止录音的标志，来控制录音线程的运行
    Boolean stopflag = false;
    //定义所需要的组件
    JButton captureBtn,stopBtn,playBtn,saveBtn;
    //构造函数
    public MyRecord()
    {
        captureBtn = new JButton("开始录音");
        captureBtn.addActionListener(this);
        captureBtn.setActionCommand("captureBtn");

        stopBtn = new JButton("停止录音");
        stopBtn.addActionListener(this);
        stopBtn.setActionCommand("stopBtn");

        playBtn = new JButton("播放录音");
        playBtn.addActionListener(this);
        playBtn.setActionCommand("playBtn");

        saveBtn = new JButton("保存录音");
        saveBtn.addActionListener(this);
        saveBtn.setActionCommand("saveBtn");

        this.add(captureBtn);
        this.add(stopBtn);
        this.add(playBtn);
        this.add(saveBtn);
        //设置按钮的属性
        captureBtn.setEnabled(true);
        stopBtn.setEnabled(false);
        playBtn.setEnabled(false);
        saveBtn.setEnabled(false);
        //设置窗口的属性
        this.setSize(400,300);
        this.setTitle("录音机");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("captureBtn"))
        {
            //修改按钮状态
            captureBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            playBtn.setEnabled(false);
            saveBtn.setEnabled(false);
            //调用录音的方法
            capture();
        }else if (e.getActionCommand().equals("stopBtn")) {
            //点击停止录音按钮的动作
            captureBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            playBtn.setEnabled(true);
            saveBtn.setEnabled(true);
            //调用停止录音的方法
            stop();

        }else if(e.getActionCommand().equals("playBtn"))
        {
            //调用播放录音的方法
            play();
        }else if(e.getActionCommand().equals("saveBtn"))
        {
            //调用保存录音的方法
            save();
        }

    }
    //开始录音
    public void capture()
    {
        try {
            //af为AudioFormat也就是音频格式
            af = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class,af);
            td = (TargetDataLine)(AudioSystem.getLine(info));
            //打开具有指定格式的行，这样可使行获得所有所需的系统资源并变得可操作。
            td.open(af);
            //允许某一数据行执行数据 I/O
            td.start();

            //创建播放录音的线程
            Record record = new Record();
            Thread t1 = new Thread(record);
            t1.start();

        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
            return;
        }
    }
    //停止录音
    public void stop()
    {
        stopflag = true;
    }
    //播放录音
    public void play()
    {
        //将baos中的数据转换为字节数据
        byte audioData[] = baos.toByteArray();
        //转换为输入流
        bais = new ByteArrayInputStream(audioData);
        af = getAudioFormat();
        ais = new AudioInputStream(bais, af, audioData.length/af.getFrameSize());

        try {
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, af);
            sd = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sd.open(af);
            sd.start();
            //创建播放进程
            Play py = new Play();
            Thread t2 = new Thread(py);
            t2.start();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                //关闭流
                if(ais != null)
                {
                    ais.close();
                }
                if(bais != null)
                {
                    bais.close();
                }
                if(baos != null)
                {
                    baos.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    //保存录音
    public void save()
    {
        //取得录音输入流
        af = getAudioFormat();

        byte audioData[] = baos.toByteArray();
        bais = new ByteArrayInputStream(audioData);
        ais = new AudioInputStream(bais,af, audioData.length / af.getFrameSize());
        //定义最终保存的文件名
        File file = null;
        //写入文件
        try {
            //以当前的时间命名录音的名字
            //将录音的文件存放到F盘下语音文件夹下
            File filePath = new File("d:/work/test");
            if(!filePath.exists())
            {//如果文件不存在，则创建该目录
                filePath.mkdir();
            }
            file = new File(filePath.getPath()+"/"+System.currentTimeMillis()+".mp3");
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //关闭流
            try {

                if(bais != null)
                {
                    bais.close();
                }
                if(ais != null)
                {
                    ais.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //设置AudioFormat的参数
    public AudioFormat getAudioFormat()
    {
        //下面注释部分是另外一种音频格式，两者都可以
        AudioFormat.Encoding encoding = AudioFormat.Encoding.
                PCM_SIGNED ;
        float rate = 8000f;
        int sampleSize = 16;
        String signedString = "signed";
        boolean bigEndian = true;
        int channels = 1;
        return new AudioFormat(encoding, rate, sampleSize, channels,
                (sampleSize / 8) * channels, rate, bigEndian);
//		//采样率是每秒播放和录制的样本数
//		float sampleRate = 16000.0F;
//		// 采样率8000,11025,16000,22050,44100
//		//sampleSizeInBits表示每个具有此格式的声音样本中的位数
//		int sampleSizeInBits = 16;
//		// 8,16
//		int channels = 1;
//		// 单声道为1，立体声为2
//		boolean signed = true;
//		// true,false
//		boolean bigEndian = true;
//		// true,false
//		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,bigEndian);
    }
    //录音类，因为要用到MyRecord类中的变量，所以将其做成内部类
    class Record implements Runnable
    {
        //定义存放录音的字节数组,作为缓冲区
        byte bts[] = new byte[10000];
        //将字节数组包装到流里，最终存入到baos中
        //重写run函数
        public void run() {
            baos = new ByteArrayOutputStream();
            try {
                System.out.println("ok3");
                stopflag = false;
                while(stopflag != true)
                {
                    //当停止录音没按下时，该线程一直执行
                    //从数据行的输入缓冲区读取音频数据。
                    //要读取bts.length长度的字节,cnt 是实际读取的字节数
                    int cnt = td.read(bts, 0, bts.length);
                    if(cnt > 0)
                    {
                        baos.write(bts, 0, cnt);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                    //关闭打开的字节数组流
                    if(baos != null)
                    {
                        baos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    td.drain();
                    td.close();
                }
            }
        }

    }
    //播放类,同样也做成内部类
    class Play implements Runnable
    {
        //播放baos中的数据即可
        public void run() {
            byte bts[] = new byte[10000];
            try {
                int cnt;
                //读取数据到缓存数据
                while ((cnt = ais.read(bts, 0, bts.length)) != -1)
                {
                    if (cnt > 0)
                    {
                        //写入缓存数据
                        //将音频数据写入到混频器
                        sd.write(bts, 0, cnt);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                sd.drain();
                sd.close();
            }


        }
    }
}

