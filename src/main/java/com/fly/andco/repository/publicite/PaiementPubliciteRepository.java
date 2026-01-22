package com.fly.andco.repository.publicite;

import com.fly.andco.model.publicite.PaiementPublicite;
import com.fly.andco.model.publicite.Diffusion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaiementPubliciteRepository extends JpaRepository<PaiementPublicite, Integer> {
    List<PaiementPublicite> findByDiffusion(Diffusion diffusion);
}
