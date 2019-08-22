package com.bigfire.easychat.util;

//import cn.hutool.core.util.StrUtil;
//import cn.hutool.http.HttpUtil;
//import com.alibaba.fastjson.JSONObject;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/18  22:59
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class IPUtil {
    //判断字符串是否为ip地址
    public static boolean isIP(String str) {
        // 如果长度不符合条件 返回false
        if (str.length() < 7 || str.length() > 15) return false;
        String[] arr = str.split("\\.");
        //如果拆分结果不是4个字串 返回false
        if (arr.length != 4) return false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < arr[i].length(); j++) {
                char temp = arr[i].charAt(j);
                //如果某个字符不是数字就返回false
                if (!(temp > '0' && temp < '9')) return false;
            }
        }
        for (int i = 0; i < 4; i++) {
            int temp = Integer.parseInt(arr[i]);
            //如果某个数字不是0到255之间的数 就返回false
            if (temp < 0 || temp > 255) return false;
        }
        return true;
    }
    //判断端口
    public static Boolean isPort(String str) {
        try {
            Integer port = Integer.parseInt(str);
            if (port > 0 && port < 65535) {
                return true;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    //获取自己的公网IP
//    public static String getIp() {
//        String ipUrl = "https://www.ip.cn/";
//        String resStr = HttpUtil.get(ipUrl);
//        String fromIp = StrUtil.subBetween(resStr, "您现在的 IP：<code>", "</code></p><p>所在地理位置");
//        return fromIp;
//    }

    //获取当前ip的大概地址
//    public static String getAddress(String fromIp) {
//        String ipToAddrUrl = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query="+fromIp+"&resource_id=6006";
//        String res = HttpUtil.get(ipToAddrUrl);
//        String address = JSONObject.parseObject(res)
//                .getJSONArray("data")
//                .getJSONObject(0)
//                .getString("location");
//        return address;
//    }


    //获取自己的公网IP【无依赖】
    public static String getIp() {
        String ipUrl = "https://www.ip.cn/";
        return StrUtil.getBetweenfirst(HttpUtil.utf8Get(ipUrl), "您现在的 IP：<code>", "</code></p><p>所在地理位置");
    }
    //获取当前ip的大概地址
    public static String getAddress(String fromIp) {
        String ipToAddrUrl = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query="+fromIp+"&resource_id=6006";
        return StrUtil.getBetweenfirst(HttpUtil.gbkGet(ipToAddrUrl),"location\":\"","\",\"");
    }

    public static void main(String[] args) {
        System.out.println(getIp());//180.168.253.65
        System.out.println(getAddress(getIp()));//上海市浦东新区 电信
    }

}
