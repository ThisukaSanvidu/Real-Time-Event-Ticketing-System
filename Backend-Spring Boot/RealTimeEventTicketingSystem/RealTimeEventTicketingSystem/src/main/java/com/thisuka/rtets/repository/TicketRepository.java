package com.thisuka.rtets.repository;

import com.thisuka.rtets.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for managing Ticket entities
 * Provides built-in JPA methods for ticket data access and manipulation
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
