package com.shoux_kream.admin.repository;

import com.shoux_kream.admin.entity.UserLog;
import com.shoux_kream.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminUserRepository extends JpaRepository<User, Long> {
}
