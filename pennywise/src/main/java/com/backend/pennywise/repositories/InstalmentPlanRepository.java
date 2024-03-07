package com.backend.pennywise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.pennywise.entities.InstalmentPlan;

@Repository
public interface InstalmentPlanRepository extends JpaRepository<InstalmentPlan, Long>{

}
