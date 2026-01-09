package com.fly.andco.repository.equipages;

import com.fly.andco.model.equipages.Equipage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipageRepository extends JpaRepository<Equipage, Long> {
}
