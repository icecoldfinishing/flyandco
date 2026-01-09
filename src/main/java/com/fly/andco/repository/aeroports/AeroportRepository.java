package com.fly.andco.repository.aeroports;

import com.fly.andco.model.aeroports.Aeroport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AeroportRepository extends JpaRepository<Aeroport, Long> {
}
