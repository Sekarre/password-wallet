package com.example.passwordwallet.auth.repositories;

import com.example.passwordwallet.domain.UserLoginEvent;
import com.example.passwordwallet.domain.helper.UserLoginEventByIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLoginEventRepository extends JpaRepository<UserLoginEvent, Long> {

    @Query("select new com.example.passwordwallet.domain.helper.UserLoginEventByIp(" +
            "ulg.userId, ulg.ipAddress, count(ulg.ipAddress)) " +
            "from UserLoginEvent ulg where ulg.userId =?1 group by ulg.ipAddress, ulg.userId")
    List<UserLoginEventByIp> findUserLoginsCountByIp(Long userId);
}
