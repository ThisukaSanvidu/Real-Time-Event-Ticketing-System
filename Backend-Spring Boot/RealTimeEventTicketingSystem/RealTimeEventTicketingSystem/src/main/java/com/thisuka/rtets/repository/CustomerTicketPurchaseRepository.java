package com.thisuka.rtets.repository;

import com.thisuka.rtets.entity.CustomerTicketPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for managing CustomerTicketPurchase entities
 * Inherits common JPA methods such as save, findAll, and delete
 */
@Repository
public interface CustomerTicketPurchaseRepository extends JpaRepository<CustomerTicketPurchase, Long> {
}
