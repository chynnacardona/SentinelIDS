package com.sentinelwatch.ids.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sentinelwatch.ids.model.BlacklistedIP;
import java.util.Optional;

@Repository
public interface BlacklistRepository extends JpaRepository<BlacklistedIP, Long> {
    // Custom query method to quickly look up whether an IP is already blocked
    Optional<BlacklistedIP> findByIpAddress(String ipAddress);
}