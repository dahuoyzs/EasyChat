package com.bigfire.easychat.audio.util;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/29  15:24
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class Audio {

    AudioFormat audioFormat;
    TargetDataLine targetDataLine;
    boolean isRunning = true;
    private AudioFormat getAudioFormat() {
//        float sampleRate = 16000;
//        int sampleSizeInBits = 16;
//        int channels = 1;
//        boolean signed = true;
//        boolean bigEndian = false;
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
    public void stop() throws Exception{
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //取得录音输入流
        audioFormat = getAudioFormat();
        byte audioData[] = baos.toByteArray();
        bais = new ByteArrayInputStream(audioData);
        ais = new AudioInputStream(bais, audioFormat, audioData.length / audioFormat.getFrameSize());
        //定义最终保存的文件名
        System.out.println("开始生成语音文件");
        String path = "D:/work/test/cache.wav";
        File audioFile = new File(path);
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, audioFile);

        isRunning = false;
        targetDataLine.stop();
        targetDataLine.close();
    }
    AudioInputStream ais;
    public void startRecognize() {
        try {
            audioFormat = getAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            isRunning = true;

            Thread thread = new Thread(()->{
                try {

                    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

                    int weight = 2;
                    int downSum = 0;

                    targetDataLine.open(audioFormat);
                    targetDataLine.start();
                    byte[] fragment = new byte[1024];


                    ais = new AudioInputStream(targetDataLine);
                    while (isRunning) {
                        targetDataLine.read(fragment, 0, fragment.length);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            thread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        Audio audio = new Audio();
        audio.startRecognize();
        Thread.sleep(3000);
        audio.stop();
    }


}
