package com.thisuka.rtets.repository;

import com.thisuka.rtets.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for managing Vendor entities
 * Supports CRUD operations and additional query methods
 */
@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

}
