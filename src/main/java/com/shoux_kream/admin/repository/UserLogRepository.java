package com.shoux_kream.admin.repository;

import com.shoux_kream.admin.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Long> {
    @Modifying
    @Query("delete from UserLog c where c.userLogId in :userLogIds")
    void deleteAllByIds(List<Long> userLogIds);
}
