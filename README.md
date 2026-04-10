# 🧾 Suivi de Projet IHK      

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![React](https://img.shields.io/badge/React-18-blue)
![MySQL](https://img.shields.io/badge/MySQL-8-orange)
![Docker](https://img.shields.io/badge/Docker-✓-blue)

Application web complète de gestion de projets développée avec :

####  ⚙️ Backend : Spring Boot
####  🎨 Frontend : React (Vite + Tailwind)
####  🔐 Sécurité : JWT + Spring Security
####  🗄️ Base de données : MySQL

## 🏗️ Architecture

[React] ⇄ [Spring Boot] ⇄ [MySQL]

  (port 3000)  (port 8081)   (port 3306)
     
🚀 Fonctionnalités principales
🔹 Backend
API REST complète (CRUD)

Gestion des entités :

Organismes

Employés

Projets

Phases

Affectations

Livrables

Documents

Factures

Reporting (dashboard)

Validation et gestion des exceptions

Authentification JWT + rôles

🔹 Frontend
Interface moderne avec React

Routing sécurisé

Appels API avec Axios

Gestion des rôles (Admin, Comptable, etc.)

Dashboard + pages CRUD

🔐 Authentification
Login avec JWT

Stockage du token
Routes protégées :

PrivateRoute

RoleRoute

Gestion des rôles :

ADMIN

SECRETAIRE

DIRECTEUR

CHEF_PROJET

COMPTABLE


🧪 Démo vidéo

🎥 Voir la démonstration complète :


https://github.com/user-attachments/assets/6b547df4-eb9e-4adf-86f8-1dad27e4442a



Orchestration Docker – React, Spring Boot et MySQL

1. Se placer dans le dossier du projet
 
cd suiviprojet-ihk2

 2. Lancer avec Docker Compose:
 
        docker-compose up --build

📁 Structure du projet

backend/

<img width="613" height="592" alt="image" src="https://github.com/user-attachments/assets/7531a3d6-2177-4192-ae67-0d43bd5efd54" />

frontend/

<img width="619" height="754" alt="image" src="https://github.com/user-attachments/assets/4a48f5e2-b3ad-4613-b0ae-82bd09e574f8" />


📊 Visualisation des tables MySQL (Docker)

Pour afficher les tables de la base de données MySQL dans le conteneur Docker, utilisez la commande suivante :

      docker exec -it suiviprojet-mysql mysql -uroot -proot -e "USE suiviprojet_ihk; SHOW TABLES;"


<img width="1446" height="426" alt="image" src="https://github.com/user-attachments/assets/c176d199-f907-4df9-8b37-f6c00b4d5f08" />


📌 Auteurs

👤 Issam Aboussakkine
👤 Hafsa Ajabboune
👤 Khadija Bouraiss

🎓 Étudiants à la FST Marrakech











