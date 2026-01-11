package com.fly.andco.repository.reservations;

import com.fly.andco.model.reservations.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    @Query("SELECT r FROM Reservation r WHERE " +
            "(r.passager.idPassager = :passagerId) " +
            "AND (:start IS NULL OR r.dateReservation >= :start) " +
            "AND (:end IS NULL OR r.dateReservation <= :end)")
    List<Reservation> findReservationsByPassagerAndDate(
            @Param("passagerId") Long passagerId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}