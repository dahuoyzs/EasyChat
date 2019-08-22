package com.bigfire.easychat.service;

import com.bigfire.easychat.entity.Head;
import com.bigfire.easychat.entity.response.Result;
import com.bigfire.easychat.repository.HeadDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/18  23:36
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */
@Service
public class HeadService {
    @Autowired
    private HeadDao headDao;

    public Head headAdd(Head head){
        return headDao.save(head);
    }
    public Result<Head> headAddResult(Head head){
        return Result.getSuccessResult(headDao.save(head));
    }
    //Dao层返回都是各种数据，或者List，Service就是用来处理业务的，结果拼装好了还不组装Result吗？
    public List<Head> list(){
        return headDao.findAll();
    };
    public Result<List<Head>> listResult(){
        Result<List<Head>> result = Result.getSuccessResult(headDao.findAll());
        return result;
    };
    
}
