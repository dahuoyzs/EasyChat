package com.bigfire.easychat.common.util;

import cn.hutool.cache.impl.TimedCache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/23  22:12
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   : redis存token的简化版
 */
public class Storage {//全局静态存储桶
//    public static TimedCache<String,String> onlineTokens = new TimedCache<>(30 * 1000);//测试时用10秒
    public static TimedCache<String,String> onlineTokens = new TimedCache<>(24 * 60 * 60 * 1000);//默认一个小时

}
