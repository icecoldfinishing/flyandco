package com.fly.andco.repository.publicite;

import com.fly.andco.model.publicite.Diffusion;
import com.fly.andco.model.vols.VolInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DiffusionRepository extends JpaRepository<Diffusion, Integer> {
    @Query("SELECT d FROM Diffusion d WHERE MONTH(d.dateDiffusion) = :month AND YEAR(d.dateDiffusion) = :year")
    List<Diffusion> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

    List<Diffusion> findByVolInstance(VolInstance volInstance);
}
