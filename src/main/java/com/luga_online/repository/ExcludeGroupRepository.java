package com.luga_online.repository;

import com.luga_online.model.ExcludeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcludeGroupRepository extends JpaRepository<ExcludeGroup, Integer> {
}
