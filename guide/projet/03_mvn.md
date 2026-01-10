### Compiler le projet
mvn compile
# Compile uniquement le code Java
# Vérifie les erreurs de compilation

### Nettoyer le projet
mvn clean
# Supprime le dossier target/ pour repartir à zéro

### Construire / package
mvn package
# Compile et génère le JAR ou WAR dans target/
# Exemple : target/mon-projet-0.0.1-SNAPSHOT.jar

### Installer localement
mvn install
# Compile + package + installe le JAR dans le dépôt local Maven (~/.m2)

### Lancer le projet en mode développement
mvn spring-boot:run
# Compile automatiquement si nécessaire
# Démarre le serveur intégré sur http://localhost:8080
