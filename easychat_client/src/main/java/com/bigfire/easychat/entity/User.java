package com.bigfire.easychat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/17  15:02
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */

@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    Long id;
    String username;
    String password;
    String headUrl;
    String ip;
    String address;
    Date createDate;
}
