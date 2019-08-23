package com.bigfire.easychat.common.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/23  22:12
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class Storage {
    public static ConcurrentHashMap<String,String> userOnline = new ConcurrentHashMap<>();

}
