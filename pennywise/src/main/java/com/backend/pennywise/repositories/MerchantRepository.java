package com.backend.pennywise.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.pennywise.entities.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long>{

	List<Merchant> findByMerchantCategory_MerchantCategoryCode(int merchantCategoryCode);

}
