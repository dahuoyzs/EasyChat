package com.bigfire.easychat.controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/22  0:20
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class ChatController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void sendButtonAction(){
        System.out.println("sendButtonAction被点击");
    }
    public void sendMethod(){
        System.out.println("sendButtonAction被点击");
    }
    public void closeApplication(){
        System.out.println("sendButtonAction被点击");
    }

}
