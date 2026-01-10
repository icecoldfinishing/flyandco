# PostgreSQL 17 - Commandes de consultation (Windows)

# 1. Ouvrir PowerShell ou CMD

# 2. Se connecter au serveur PostgreSQL
"C:\Program Files\PostgreSQL\17\bin\psql.exe" -U postgres

# 3. Lister les bases de données
\l

# 4. Se connecter à une base existante
\c nom_de_la_base

# 5. Lister les tables de la base courante
\dt

# 6. Lister les utilisateurs / rôles
\du

# 7. Lister les schémas
\dn

# 8. Lister les colonnes d’une table
\d nom_de_la_table

# 9. Quitter psql
\q
