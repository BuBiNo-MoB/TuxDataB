package com.database.TuxDataB.linuxDistribution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LinuxDistributionRepository extends JpaRepository<LinuxDistribution, Long> {
    List<LinuxDistribution> findBySupportedArchitecture(String architecture);

    @Query("SELECT d FROM LinuxDistribution d WHERE LOWER(d.name) LIKE LOWER(concat('%', :keyword,'%'))")
    List<LinuxDistribution> searchByNameContaining(@Param("keyword") String keyword);

    List<LinuxDistribution> findByDesktopEnvironment(String desktopEnvironment);
}
