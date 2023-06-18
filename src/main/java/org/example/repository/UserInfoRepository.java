package org.example.repository;

import org.example.entity.UserId;
import org.example.entity.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
    @Query("SELECT 1")
    int check();

    List<UserId> findAllIdsByOrderById(Pageable pageable);

    List<UserInfo> findAllByOrderById(Pageable pageable);
}
