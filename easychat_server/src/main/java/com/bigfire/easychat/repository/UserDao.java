package com.bigfire.easychat.repository;

import com.bigfire.easychat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Author ：dahuo.
 * @ Date   ：2019/8/17  15:06
 * @ Addr   ：China ShangHai
 * @ Email  ：835476090@qq.com
 * @ Desc   :
 */

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
    @Modifying
    @Query("update User u set u.headUrl = :headUrl where u.id = :userId")
    int update(@Param("headUrl") String headUrl, @Param("userId") @NonNull Long userId);

}
