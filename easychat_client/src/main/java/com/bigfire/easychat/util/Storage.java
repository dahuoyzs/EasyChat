package com.bigfire.easychat.util;

import com.bigfire.easychat.entity.User;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/21  22:24
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class Storage {
    public static ConcurrentHashMap<String, Parent> views = new ConcurrentHashMap();
    public static ConcurrentHashMap<String, Object> controllers = new ConcurrentHashMap();
    public static ConcurrentHashMap<String, Stage> stageViews = new ConcurrentHashMap();
    public static String token;
    public static User onlineUser;
    public static List<User> onlineUsers;
    public static String ip;
    public static String port;
    public static byte[] voiceData;

}
