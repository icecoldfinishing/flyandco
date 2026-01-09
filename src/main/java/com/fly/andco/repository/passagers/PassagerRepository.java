package com.fly.andco.repository.passagers;

import com.fly.andco.model.passagers.Passager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassagerRepository extends JpaRepository<Passager, Long> {
}
