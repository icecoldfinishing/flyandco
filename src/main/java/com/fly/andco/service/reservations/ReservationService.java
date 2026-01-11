package com.fly.andco.service.reservations;

import com.fly.andco.model.reservations.Reservation;
import com.fly.andco.repository.reservations.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getById(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> findReservations(Long passagerId, LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findReservationsByPassagerAndDate(passagerId, start, end);
    }
}
