package com.viettel.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viettel.api.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
