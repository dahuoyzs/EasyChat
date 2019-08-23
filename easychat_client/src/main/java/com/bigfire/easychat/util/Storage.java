package com.bigfire.easychat.util;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
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


}
