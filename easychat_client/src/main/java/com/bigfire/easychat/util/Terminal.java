package com.bigfire.easychat.util;



import java.io.IOException;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/9/11  9:19
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class Terminal {
    //java调用系统运行Terminal指令
    public static void run(String cmdStr) {
        try {
            Runtime.getRuntime().exec(cmdStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        run("calc");
    }
}
