package com.backend.pennywise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.pennywise.entities.MerchantCategory;

@Repository
public interface MerchantCategoryRepository extends JpaRepository<MerchantCategory, Long> {

}
