# Guide d'Implémentation - Achat de Billet

## Vue d'ensemble
Implémentation d'un système d'achat de billets d'avion permettant aux utilisateurs de rechercher des vols, sélectionner une classe tarifaire et finaliser leur réservation.

---

## 1. Modèles de Données

### 1.1 Vol.java
**Fichier**: `src/main/java/com/fly/andco/model/vols/Vol.java`
- `@Table(name = "vol")`
- **Champs**:
  - `Long idVol`
  - `Compagnie compagnie`
  - `Aeroport aeroportDepart`
  - `Aeroport aeroportArrivee`
  - `Integer dureeMinutes`

### 1.2 VolInstance.java
**Fichier**: `src/main/java/com/fly/andco/model/vols/VolInstance.java`
- `@Table(name = "vol_instance")`
- **Champs**:
  - `Long idVolInstance`
  - `Vol vol`
  - `Avion avion`
  - `LocalDateTime dateDepart`
  - `LocalDateTime dateArrivee`
  - `String statut`

### 1.3 PrixVol.java
**Fichier**: `src/main/java/com/fly/andco/model/prix/PrixVol.java`
- `@Table(name = "prix_vol")`
- **Champs**:
  - `Long idPrix`
  - `Vol vol`
  - `String classe`
  - `Double prix`
  - `LocalDateTime dateMaj`

### 1.4 Reservation.java
**Fichier**: `src/main/java/com/fly/andco/model/reservations/Reservation.java`
- `@Table(name = "reservation")`
- **Champs**:
  - `Long idReservation`
  - `Passager passager`
  - `VolInstance volInstance`
  - `PrixVol prixVol`
  - `LocalDateTime dateReservation`
  - `String siege`
  - `String statut`

### 1.5 Passager.java
**Fichier**: `src/main/java/com/fly/andco/model/passagers/Passager.java`
- `@Table(name = "passager")`
- **Champs**:
  - `Long idPassager`
  - `String nom`
  - `String prenom`
  - `LocalDate dateNaissance`
  - `String email`

### 1.6 Aeroport.java
**Fichier**: `src/main/java/com/fly/andco/model/aeroports/Aeroport.java`
- `@Table(name = "aeroport")`
- **Champs**:
  - `Long idAeroport`
  - `String nom`
  - `String ville`
  - `String pays`
  - `String codeIata`
  - `String codeIcao`

---

## 2. Repositories

### 2.1 VolInstanceRepository
**Fichier**: `src/main/java/com/fly/andco/repository/VolInstanceRepository.java`
- **Méthodes**:
  - `List<VolInstance> findFlights(String depart, String arrivee, LocalDateTime start, LocalDateTime end)`

### 2.2 AeroportRepository
**Fichier**: `src/main/java/com/fly/andco/repository/aeroports/AeroportRepository.java`
- Hérite de `JpaRepository<Aeroport, Long>`

### 2.3 PrixVolRepository
**Fichier**: `src/main/java/com/fly/andco/repository/prix/PrixVolRepository.java`
- Hérite de `JpaRepository<PrixVol, Long>`

### 2.4 PassagerRepository
**Fichier**: `src/main/java/com/fly/andco/repository/passagers/PassagerRepository.java`
- Hérite de `JpaRepository<Passager, Long>`

### 2.5 ReservationRepository
**Fichier**: `src/main/java/com/fly/andco/repository/reservations/ReservationRepository.java`
- Hérite de `JpaRepository<Reservation, Long>`

---

## 3. Contrôleur

### FlightBookingController
**Fichier**: `src/main/java/com/fly/andco/controller/FlightBookingController.java`

**Endpoints**:

1. **GET** `/flights/search`
   - **Retour**: `String` (vue)
   - **Paramètres**: `Model model`
   - **Description**: Affiche le formulaire de recherche

2. **GET** `/flights/results`
   - **Retour**: `String` (vue)
   - **Paramètres**: 
     - `String origin`
     - `String destination`
     - `LocalDate date`
     - `Model model`
   - **Description**: Recherche et affiche les vols disponibles

3. **GET** `/flights/book/{id}`
   - **Retour**: `String` (vue)
   - **Paramètres**: 
     - `Long id` (PathVariable)
     - `Model model`
   - **Description**: Affiche le formulaire de réservation

4. **POST** `/flights/purchase`
   - **Retour**: `String` (vue)
   - **Paramètres**: 
     - `Long flightId`
     - `Long priceId`
     - `Passager passenger` (ModelAttribute)
     - `Model model`
   - **Description**: Finalise l'achat du billet

5. **GET** `/flights/fix-db`
   - **Retour**: `String` (texte)
   - **Description**: Outil de réparation de contraintes DB

---

## 4. Vues (Thymeleaf)

### 4.1 search.html
**Fichier**: `src/main/resources/templates/views/flights/search.html`
- Formulaire avec sélection d'aéroport de départ, arrivée et date

### 4.2 results.html
**Fichier**: `src/main/resources/templates/views/flights/results.html`
- Tableau des vols disponibles avec bouton "Réserver"

### 4.3 book.html
**Fichier**: `src/main/resources/templates/views/flights/book.html`
- Formulaire de saisie passager et sélection de classe/prix

### 4.4 confirmation.html
**Fichier**: `src/main/resources/templates/views/flights/confirmation.html`
- Affichage des détails de la réservation confirmée

### 4.5 sidebar.html (modifié)
**Fichier**: `src/main/resources/templates/fragments/sidebar.html`
- Ajout du lien "Acheter Billet" vers `/flights/search`

---

## 5. Configuration Base de Données

### Script SQL
**Fichier**: `src/bd/00_script.sql`
- Tables: `compagnie`, `aeroport`, `avion`, `vol`, `vol_instance`, `prix_vol`, `passager`, `reservation`

### Configuration Application
**Fichier**: `src/main/resources/application.properties`
- `spring.jpa.hibernate.ddl-auto=none` (important!)
- Configuration PostgreSQL (database: `pg10`)

---

## 6. Points Critiques

### 6.1 Nommage des Tables
- Utiliser **snake_case** en minuscules dans `@Table(name = "...")`
- Exemples: `prix_vol`, `vol_instance`, `reservation`

### 6.2 Contraintes Clés Étrangères
- `reservation.id_prix` → `prix_vol.id_prix`
- `reservation.id_vol_instance` → `vol_instance.id_vol_instance`
- `reservation.id_passager` → `passager.id_passager`

### 6.3 Réparation DB
- Endpoint `/flights/fix-db` pour corriger les contraintes en cas de problème
- Tronque la table `reservation` et recrée les contraintes FK

---

## 7. Flux Utilisateur

1. **Recherche**: Sélection origine, destination, date → `/flights/results`
2. **Sélection**: Choix d'un vol → `/flights/book/{id}`
3. **Réservation**: Saisie infos passager + classe → `/flights/purchase`
4. **Confirmation**: Affichage réservation → `/flights/confirmation`

---

## 8. Dépendances Techniques

- Spring Boot
- Spring Data JPA
- Thymeleaf
- PostgreSQL Driver
- Jakarta Persistence API
