package com.bigfire.easychat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/20  22:58
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
@Accessors(chain = true)
@Data
//@Entity
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "cmd")
public class Cmd {
//    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    Integer type; //1为功能命令,2为windows命令，3为mac命令
    String action;//用于描述命令  只有type为1 的时候才会取action
    //而action为对应的功能,后面会在启动时,默认添加一些基础的功能,如下
    //通用  关机,打开制定网页,客户端踢下线,关闭QQ,
    String cmd;//具体的命令
    String fromUsername;
    String fromIp;
    String fromAddress;
    String toUsername;//如果username未空时，则广播发送给没一共客户端
    String toIp;
    String toAddress;
}
