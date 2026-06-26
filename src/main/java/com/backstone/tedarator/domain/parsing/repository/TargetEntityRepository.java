package com.backstone.tedarator.domain.parsing.repository;

import com.backstone.tedarator.domain.parsing.entity.TargetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetEntityRepository extends JpaRepository<TargetEntity, Long> {
}
