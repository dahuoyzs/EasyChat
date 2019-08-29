package com.bigfire.easychat.audio.util;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/29  22:22
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class FireAudio {
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private SourceDataLine sourceDataLine;
    private ByteArrayInputStream bais;
    private ByteArrayOutputStream baos;
    private AudioInputStream ais;
    private Boolean stopflag = false;

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public void startCapture() {
        try {
            audioFormat = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) (AudioSystem.getLine(info));
            targetDataLine.open(audioFormat);
            targetDataLine.start();

            Thread t1 = new Thread(() -> {
                byte bts[] = new byte[1024];
                baos = new ByteArrayOutputStream();
                try {
                    stopflag = false;
                    while (stopflag != true) {
                        int cnt = targetDataLine.read(bts, 0, bts.length);
                        if (cnt > 0) {
                            baos.write(bts, 0, cnt);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        //关闭打开的字节数组流
                        if (baos != null) {
                            baos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        targetDataLine.drain();
                        targetDataLine.close();
                    }
                }
            });
            t1.start();

        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
            return;
        }
    }

    //停止录音
    public void stop() {
        stopflag = true;
    }

    public void play() {
        //将baos中的数据转换为字节数据
        byte audioData[] = baos.toByteArray();
        //转换为输入流
        bais = new ByteArrayInputStream(audioData);
        audioFormat = getAudioFormat();
        ais = new AudioInputStream(bais, audioFormat, audioData.length / audioFormat.getFrameSize());

        try {
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            //创建播放进程
//            MyRecord.Play py = new MyRecord.Play();
            Thread t2 = new Thread(() -> {
                byte bts[] = new byte[1024];
                try {
                    int cnt;
                    while ((cnt = ais.read(bts, 0, bts.length)) != -1) {
                        if (cnt > 0) {
                            sourceDataLine.write(bts, 0, cnt);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    sourceDataLine.drain();
                    sourceDataLine.close();
                }
            });
            t2.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ais != null) {
                    ais.close();
                }
                if (bais != null) {
                    bais.close();
                }
                if (baos != null) {
                    baos.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    //保存录音
    public void save() {
        audioFormat = getAudioFormat();

        byte audioData[] = baos.toByteArray();
        bais = new ByteArrayInputStream(audioData);
        ais = new AudioInputStream(bais, audioFormat, audioData.length / audioFormat.getFrameSize());
        try {
            File filePath = new File("d:/work/test");
            if (!filePath.exists()) {//如果文件不存在，则创建该目录
                filePath.mkdir();
            }
            File file = new File(filePath.getPath() + "/" + System.currentTimeMillis() + ".mp3");
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                }
                if (ais != null) {
                    ais.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
