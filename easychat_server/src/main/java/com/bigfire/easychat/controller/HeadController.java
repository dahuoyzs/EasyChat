package com.bigfire.easychat.controller;

import com.bigfire.easychat.entity.Head;
import com.bigfire.easychat.entity.response.Result;
import com.bigfire.easychat.service.HeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/18  23:46
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
@RestController
@RequestMapping("/head")
public class HeadController {
    @Autowired
    HeadService headService;
    @GetMapping("/list")
    public Result<List<Head>> headList(){
        return headService.listResult();
    }
    @PostMapping("/add")
    public Result<Head> userAdd(Head head){
        return headService.headAddResult(head);
    }
}
