package com.bigfire.easychat.voice;

import javax.sound.sampled.*;
import java.io.*;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/29  22:22
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class Audio {
    private AudioStopListener voiceStopListener;
    private boolean isRecording = false;
    private ByteArrayOutputStream out;

    public void setVoiceStopListener(AudioStopListener voiceStopListener) {
        this.voiceStopListener = voiceStopListener;
    }

    public void setRecording(boolean flag) {
        isRecording = flag;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void stop() {
        setRecording(false);
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    public void captureAudio() {
        try {
            final AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            Runnable runner = new Runnable() {
                int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
                byte buffer[] = new byte[bufferSize];

                public void run() {
                    out = new ByteArrayOutputStream();
                    isRecording = true;
                    try {
                        while (isRecording) {
                            int count = line.read(buffer, 0, buffer.length);
                            if (count > 0) {
                                out.write(buffer, 0, count);
                            }
                        }
                    } finally {
                        try {
                            out.close();
                            out.flush();
                            line.close();
                            line.flush();
                            if (voiceStopListener != null) {
                                voiceStopListener.onStop(out.toByteArray());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            Thread captureThread = new Thread(runner);
            captureThread.start();
        } catch (LineUnavailableException e) {
            System.out.println("已经在录音了,不能重复开启，先调用stop()方法停止录音");
            e.printStackTrace();
        }
    }

    public void playAudio(byte[] audio) {
        try {
            InputStream input = new ByteArrayInputStream(audio);
            final AudioFormat format = getAudioFormat();
            final AudioInputStream ais = new AudioInputStream(input, format, audio.length / format.getFrameSize());
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            Runnable runner = new Runnable() {
                int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
                byte buffer[] = new byte[bufferSize];

                public void run() {
                    try {
                        int count;
                        while ((count = ais.read(
                                buffer, 0, buffer.length)) != -1) {
                            if (count > 0) {
                                line.write(buffer, 0, count);
                            }
                        }
                    } catch (IOException e) {
                        System.err.println("I/O problems: " + e);
                    } finally {
                        line.drain();
                        line.close();
                    }
                }
            };
            Thread playThread = new Thread(runner);
            playThread.start();
        } catch (LineUnavailableException e) {
            System.err.println("Line unavailable: " + e);
        }
    }

    public void save(byte[] voiceData, String dirPath) {
        //取得录音输入流
        AudioFormat audioFormat = getAudioFormat();
        ByteArrayInputStream bais = new ByteArrayInputStream(voiceData);
        AudioInputStream ais = new AudioInputStream(bais, audioFormat, voiceData.length / audioFormat.getFrameSize());
        try {
            File filePath = new File(dirPath);
            if (!filePath.exists()) {//如果文件不存在，则创建该目录
                filePath.mkdir();
            }
            String fileName = filePath.getPath() + "/" + System.currentTimeMillis() + ".mp3";
            File file = new File(fileName);
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
            System.out.println("音频文件存储成功,路径:"+fileName);
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
