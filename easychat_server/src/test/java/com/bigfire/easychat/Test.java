package com.bigfire.easychat;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/23  15:21
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class Test {



    public static void main(String[] args) throws Exception{
//        Long userId = 10086L;
//        String token = TokensUtil.createToken(userId);
//        System.out.println(token);
//        Long id = TokensUtil.getUserId(token);
//        System.out.println(id);
        //创建缓存，默认4毫秒过期
        TimedCache<String, String> timedCache = new TimedCache(4);
        timedCache.put("token","myToken",10);
        System.out.println(timedCache.get("token"));
        Thread.sleep(5);
        System.out.println(timedCache.get("token"));
    }
}
