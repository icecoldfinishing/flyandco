package com.fly.andco.controller.avions;

import com.fly.andco.model.avions.Avion;
import com.fly.andco.model.avions.Siege;
import com.fly.andco.model.vols.SiegeVol;
import com.fly.andco.model.vols.VolInstance;
import com.fly.andco.repository.avions.AvionRepository;
import com.fly.andco.repository.avions.SiegeRepository;
import com.fly.andco.repository.vols.SiegeVolRepository;
import com.fly.andco.repository.vols.VolInstanceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/sieges")
public class SiegeAdminController {

    private final AvionRepository avionRepository;
    private final SiegeRepository siegeRepository;
    private final VolInstanceRepository volInstanceRepository;
    private final SiegeVolRepository siegeVolRepository;

    public SiegeAdminController(AvionRepository avionRepository,
                                SiegeRepository siegeRepository,
                                VolInstanceRepository volInstanceRepository,
                                SiegeVolRepository siegeVolRepository) {
        this.avionRepository = avionRepository;
        this.siegeRepository = siegeRepository;
        this.volInstanceRepository = volInstanceRepository;
        this.siegeVolRepository = siegeVolRepository;
    }

    // Insertion dynamique de sièges
    @GetMapping("/insert")
    public String insertForm(Model model) {
        model.addAttribute("avions", avionRepository.findAll());
        model.addAttribute("pageTitle", "Insertion dynamique de sièges");
        return "views/avions/sieges_insert";
    }

    @PostMapping("/insert")
    public String insert(@RequestParam("idAvion") Long idAvion,
                         @RequestParam("classe") String classe,
                         @RequestParam("startNumero") Integer startNumero,
                         @RequestParam("quantite") Integer quantite) {
        Avion avion = avionRepository.findById(idAvion).orElse(null);
        if (avion != null && quantite != null && quantite > 0 && startNumero != null && startNumero >= 0) {
            for (int i = 0; i < quantite; i++) {
                Siege s = new Siege();
                s.setAvion(avion);
                s.setClasse(classe);
                s.setNumero(String.valueOf(startNumero + i));
                siegeRepository.save(s);
            }
        }
        return "redirect:/sieges/insert";
    }

    // Associer siège à vol_instance (siege_vol)
    @GetMapping("/associer")
    public String associerForm(Model model) {
        model.addAttribute("volInstances", volInstanceRepository.findAll());
        model.addAttribute("sieges", siegeRepository.findAll());
        model.addAttribute("pageTitle", "Associer siège à vol");
        return "views/avions/sieges_associer";
    }

    @PostMapping("/associer")
    public String associer(@RequestParam("idVolInstance") Long idVolInstance,
                           @RequestParam("idSiege") Long idSiege) {
        VolInstance vi = volInstanceRepository.findById(idVolInstance).orElse(null);
        Siege s = siegeRepository.findById(idSiege).orElse(null);
        if (vi != null && s != null && vi.getAvion() != null && s.getAvion() != null
                && vi.getAvion().getIdAvion().equals(s.getAvion().getIdAvion())) {
            SiegeVol sv = new SiegeVol();
            sv.setVolInstance(vi);
            sv.setSiege(s);
            sv.setStatut("LIBRE");
            siegeVolRepository.save(sv);
        }
        return "redirect:/sieges/associer";
    }

    @PostMapping("/associer-range")
    public String associerRange(@RequestParam("idVolInstance") Long idVolInstance,
                                @RequestParam("startNumero") Integer startNumero,
                                @RequestParam("endNumero") Integer endNumero) {
        if (idVolInstance == null || startNumero == null || endNumero == null || endNumero < startNumero) {
            return "redirect:/sieges/associer";
        }
        VolInstance vi = volInstanceRepository.findById(idVolInstance).orElse(null);
        if (vi == null || vi.getAvion() == null) {
            return "redirect:/sieges/associer";
        }
        Long idAvion = vi.getAvion().getIdAvion();
        var existing = siegeVolRepository.findByVolInstance_IdVolInstance(idVolInstance);
        java.util.Set<Long> existingSiegeIds = new java.util.HashSet<>();
        for (SiegeVol sv : existing) {
            if (sv.getSiege() != null && sv.getSiege().getIdSiege() != null) {
                existingSiegeIds.add(sv.getSiege().getIdSiege());
            }
        }
        java.util.List<Siege> siegesAvion = siegeRepository.findByAvion_IdAvion(idAvion);
        for (Siege s : siegesAvion) {
            try {
                int numero = Integer.parseInt(s.getNumero());
                if (numero >= startNumero && numero <= endNumero && !existingSiegeIds.contains(s.getIdSiege())) {
                    SiegeVol sv = new SiegeVol();
                    sv.setVolInstance(vi);
                    sv.setSiege(s);
                    sv.setStatut("LIBRE");
                    siegeVolRepository.save(sv);
                }
            } catch (NumberFormatException ignored) { }
        }
        return "redirect:/sieges/associer";
    }
}
