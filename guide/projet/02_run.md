# Cheat Sheet - Projet Spring Boot avec Maven et PostgreSQL

## Pré-requis
- Java 21 installé et JAVA_HOME configuré
- Maven 21 installé et mvn disponible dans le PATH
- PostgreSQL 17 lancé et base créée
- Vérifier application.properties / application.yml :

spring.datasource.url=jdbc:postgresql://localhost:5432/nom_de_la_base
spring.datasource.username=postgres
spring.datasource.password=ton_mdp
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

---

## Commandes Maven essentielles

### 1. Compiler le projet
mvn compile
# Compile uniquement le code Java

### 2. Lancer le projet (dev rapide)
mvn spring-boot:run
# Compile automatiquement si nécessaire
# Démarre le serveur Tomcat intégré sur http://localhost:8080

### 3. Nettoyer et reconstruire complètement
mvn clean install
# Supprime target/ et recompilation complète
# Génère le JAR dans target/ (ex: bibliotheque-0.0.1-SNAPSHOT.jar)
# Utile pour déploiement ou résoudre des erreurs persistantes

### 4. Lancer le JAR généré
java -jar target/bibliotheque-0.0.1-SNAPSHOT.jar
# Lance l'application compilée sans Maven

---

## Accès au projet
- Frontend JSP : http://localhost:8080/
- Endpoints Spring Boot (REST / MVC) : http://localhost:8080/
- Fichiers compilés : dossier target/

---

## Astuce Dev
- mvn spring-boot:run suffit pour la plupart des modifications
- Si modification du pom.xml ou dépendances : faire mvn clean install avant de rerun
