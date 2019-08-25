package com.bigfire.easychat.common.util;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/23  18:37
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
public class JWTUtil {
    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);
    private static final String SECRET = "session_secret";
    private static final String ISSUER = "iss_user";

    public static String createToken(String data) {
        Integer oneDay = 60 * 60 * 24;//默认token失效时间为一天
        return createToken(data,oneDay);
    }
    public static String createToken(String data,Integer second) {
        HashMap<String,String> map = new HashMap();
        map.put("data",data);
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        map.put("isExpired","false");
        return createToken(map,second);
    }
    public static String getValueFromToken(String token) {
        Map<String, String> map = verifyToken(token);
        JSONObject jo = JSONObject.parseObject(JSONObject.toJSONString(map));
        if (jo.getString("isExpired").equals("false")){
            return jo.getString("data");
        }else {//token is expired ，You can choose Custom return values
            return jo.getString("data");
        }
    }
    //获取token的方法
    private static String createToken(Map<String, String> claims,Integer second) {
        try {
            //使用该加密算法
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.SECOND, second);//设置多少秒后失效
            Date expiresDate = nowTime.getTime();
            //Builder是JWTCreator的静态内部类
            //{静态内部类只能访问外部类的静态变量和静态方法，Outer.Inner inner = new Outer.Inner()}
            JWTCreator.Builder builder = JWT.create()
                    .withIssuer(ISSUER)         //设置发布者
                    .withExpiresAt(expiresDate);//过期一天
            claims.forEach((k, v) -> builder.withClaim(k, v));//将传入的claims设置到builder里面;
            return builder.sign(algorithm); //使用上面的加密算法进行签名，返回String，就是token
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
    //验证token方法
    private static Map<String, String> verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            DecodedJWT jwt = verifier.verify(token);
            Map<String, Claim> map = jwt.getClaims();
            Map<String, String> resultMap = new HashMap<>();
            map.forEach((k, v) -> resultMap.put(k, v.asString()));
            return resultMap;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            HashMap map = new HashMap<>();
            map.put("data",e.getMessage());
            map.put("isExpired","true");
            return map;
        }
    }
//    public static void main(String[] args) {
//        String data = "10086";
//        String token = createToken(data);
//        System.out.println(token);
//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJkYXRhIjoiMTAwODYiLCJpc3MiOiJpc3NfdXNlciIsInR5cCI6IkpXVCIsImV4cCI6MTU2NjU2NDc1MCwiYWxnIjoiSFMyNTYifQ.TPVU7OfuIdNEEZcvRdA08eyqQ5u-bKZne5_UgDX5DmA";
//        String value = getValueFromToken(token);
//        System.out.println(value);
//    }

}
