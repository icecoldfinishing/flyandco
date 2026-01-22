package com.fly.andco.repository.publicite;

import com.fly.andco.model.publicite.Diffusion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DiffusionRepository extends JpaRepository<Diffusion, Integer> {
    @Query("SELECT d FROM Diffusion d WHERE MONTH(d.volInstance.dateDepart) = :month AND YEAR(d.volInstance.dateDepart) = :year")
    List<Diffusion> findByMonthAndYear(@Param("month") int month, @Param("year") int year);
}
