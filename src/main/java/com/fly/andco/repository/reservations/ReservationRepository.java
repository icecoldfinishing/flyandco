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
        List<Reservation> findByVolInstance_IdVolInstance(Long idVolInstance);
        List<Reservation> findByPassager_IdPassager(Long passagerId);

        List<Reservation> findByPassager_IdPassagerAndDateReservationBetween(
                Long passagerId, LocalDateTime start, LocalDateTime end);

        boolean existsBySiegeVol_IdSiegeVol(Long idSiegeVol);
}
