package com.bigfire.easychat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/18  23:32
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "head")
public class Head {
    @Id
    Long id;
    String headUrl;
    Date createDate;
}
