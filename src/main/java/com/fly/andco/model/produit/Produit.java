package com.fly.andco.model.produit;

import com.fly.andco.model.publicite.Societe;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "produit")
public class Produit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_produit")
	private Long idProduit;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_societe", nullable = false)
	private Societe societe;

	@Column(length = 255)
	private String description;

	@Column(nullable = false, length = 100)
	private String nom;

	@Column(nullable = false)
	private BigDecimal prix;

	@Column(name = "date_ajout")
	private LocalDate dateAjout;

	@Column
	private Boolean disponible;

	public Produit() {}

	public Long getIdProduit() {
		return idProduit;
	}

	public void setIdProduit(Long idProduit) {
		this.idProduit = idProduit;
	}

	public Societe getSociete() {
		return societe;
	}

	public void setSociete(Societe societe) {
		this.societe = societe;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public BigDecimal getPrix() {
		return prix;
	}

	public void setPrix(BigDecimal prix) {
		this.prix = prix;
	}

	public LocalDate getDateAjout() {
		return dateAjout;
	}

	public void setDateAjout(LocalDate dateAjout) {
		this.dateAjout = dateAjout;
	}

	public Boolean getDisponible() {
		return disponible;
	}

	public void setDisponible(Boolean disponible) {
		this.disponible = disponible;
	}
}

