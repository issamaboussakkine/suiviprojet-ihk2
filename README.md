# 🧾 Suivi de Projet IHK

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![React](https://img.shields.io/badge/React-18-blue)
![MySQL](https://img.shields.io/badge/MySQL-8-orange)
![Docker](https://img.shields.io/badge/Docker-✓-blue)
![JWT](https://img.shields.io/badge/Auth-JWT-black)

Application web complète de **gestion de projets** permettant la planification, le suivi et la gestion financière des projets avec un système d'authentification sécurisé et gestion des rôles.

---

# 📌 Aperçu du Projet

- ⚙️ **Backend** : Spring Boot (API REST)
- 🎨 **Frontend** : React (Vite + TailwindCSS)
- 🔐 **Sécurité** : JWT + Spring Security
- 🗄️ **Base de données** : MySQL
- 🐳 **Déploiement** : Docker & Docker Compose

---

# 🏗️ Architecture

<img width="1024" height="549" alt="image" src="https://github.com/user-attachments/assets/f5af2239-7072-4437-933c-b595d1895cc3" />


---

# 🚀 Fonctionnalités

## 🔹 Backend (Spring Boot)

- ✅ API REST complète (CRUD)
- ✅ Architecture en couches (Controller / Service / Repository)
- ✅ Validation des données
- ✅ Gestion des exceptions
- ✅ Authentification JWT
- ✅ Autorisation basée sur les rôles
- ✅ Spring Security

---

## 🔹 Frontend (React)

- 🎨 Interface moderne avec TailwindCSS
- 🔐 Routing sécurisé
- 🔄 Appels API avec Axios
- 👤 Gestion des rôles utilisateurs
- 📊 Dashboard dynamique
- 📝 Pages CRUD complètes
- 🚫 Routes protégées

### Routes sécurisées

- `PrivateRoute` : utilisateur connecté
- `RoleRoute` : accès selon rôle

---

# 🔐 Authentification & Sécurité

- Login sécurisé avec JWT
- Génération de Token
- Stockage du token
- Intercepteur Axios
- Protection des routes
- Gestion des permissions

---

# 🗄️ Base de Données

## 📊 Modèle Entité-Relation
     
<img width="1625" height="593" alt="Screenshot 2026-04-10 140601" src="https://github.com/user-attachments/assets/4ee3ea11-cec5-49b0-9ba2-ff8a3abc39a5" />

---

# 👥 Gestion des Accès & Rôles

L'application implémente un système de gestion des permissions basé sur les rôles :

| Rôle | Description |
|------|-------------|
| 👑 ADMIN | Gestion complète du système |
| 📊 DIRECTEUR | Supervision globale des projets |
| 🛠️ CHEF_PROJET | Gestion opérationnelle des projets |
| 💰 COMPTABLE | Gestion financière |
| 🗂️ SECRETAIRE | Support administratif |

<img width="1503" height="548" alt="Screenshot 2026-04-10 160012" src="https://github.com/user-attachments/assets/6f75f5e4-9376-46c3-b1ed-9891c2145ce1" />

---

# 🧪 Démo vidéo

## 🎥 Voir la démonstration complète :





https://github.com/user-attachments/assets/5435527f-b7c2-417a-87a4-a96393ece01c








---

# 🐳 Orchestration Docker – React, Spring Boot et MySQL

 ## 1️⃣  Se placer dans le dossier du projet
 
cd suiviprojet-ihk2

 ## 2️⃣ Lancer avec Docker Compose
 
        docker-compose up --build

---

📁 Structure du projet

## 🔹 Backend

<img width="613" height="592" alt="image" src="https://github.com/user-attachments/assets/7531a3d6-2177-4192-ae67-0d43bd5efd54" />

## 🔹 Frontend

<img width="619" height="754" alt="image" src="https://github.com/user-attachments/assets/4a48f5e2-b3ad-4613-b0ae-82bd09e574f8" />

---

# 📊 Visualisation des tables MySQL (Docker)

Pour afficher les tables de la base de données MySQL dans le conteneur Docker, utilisez la commande suivante :

      docker exec -it suiviprojet-mysql mysql -uroot -proot -e "USE suiviprojet_ihk; SHOW TABLES;"


<img width="1446" height="426" alt="image" src="https://github.com/user-attachments/assets/c176d199-f907-4df9-8b37-f6c00b4d5f08" />

---

# 👨‍💻 Auteurs

- 👤 Issam Aboussakkine  
- 👤 Hafsa Ajabboune  
- 👤 Khadija Bouraiss  

🎓 Étudiants à la Faculté des Sciences et Techniques de Marrakech











