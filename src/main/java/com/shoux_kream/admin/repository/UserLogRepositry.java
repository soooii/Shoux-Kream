package com.shoux_kream.admin.repository;

import com.shoux_kream.admin.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepositry extends JpaRepository<UserLog, Long> {
}
