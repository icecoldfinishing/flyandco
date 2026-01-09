package com.fly.andco.repository.pilotes;

import com.fly.andco.model.pilotes.Pilote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PiloteRepository extends JpaRepository<Pilote, Long> {
}
