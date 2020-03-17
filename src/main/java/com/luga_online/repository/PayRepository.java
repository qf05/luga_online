package com.luga_online.repository;

import com.luga_online.model.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayRepository extends JpaRepository<Pay, Integer> {

    Pay findTopByUserVkIdOrderByTimeDesc(Integer userId);

    List<Pay> getAllByUserVkIdOrderByTimeDesc(Integer userId);
}
