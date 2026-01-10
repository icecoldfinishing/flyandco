package com.fly.andco.repository;

import com.fly.andco.model.vols.VolInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VolInstanceRepository extends JpaRepository<VolInstance, Long> {
    
    @Query("SELECT vi FROM VolInstance vi WHERE " +
           "vi.vol.aeroportDepart.codeIata = :depart " +
           "AND vi.vol.aeroportArrivee.codeIata = :arrivee " +
           "AND vi.dateDepart BETWEEN :start AND :end")
    List<VolInstance> findFlights(@Param("depart") String depart,
                                  @Param("arrivee") String arrivee,
                                  @Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end);
}
