package com.fly.andco.repository.compagnies;

import com.fly.andco.model.compagnies.Compagnie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompagnieRepository extends JpaRepository<Compagnie, Long> {
}
