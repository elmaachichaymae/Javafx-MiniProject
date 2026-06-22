#  CareDash - Application de Suivi de Traitements Médicaux

**CareDash** est une application de bureau (Desktop) développée en **JavaFX** permettant aux professionnels de santé de gérer leurs patients, de prescrire des traitements et de visualiser des statistiques en temps réel.  
Ce projet a été réalisé dans le cadre du module **Développement Java IHM** à l'ENSA d'Oujda (GI3).

---

## 📋 Fonctionnalités principales

- 🔐 **Authentification sécurisée** : Écrans de connexion et d'inscription avec validation des champs.
- 📊 **Tableau de bord dynamique** : Affichage des indicateurs clés (nombre de patients, traitements actifs, ordonnances) et visualisation graphique de la répartition des patients (camembert).
- 👤 **Gestion complète des patients (CRUD)** : Ajouter, modifier, supprimer et rechercher des patients avec filtrage en temps réel (Nom, Date de naissance, Sexe, Allergies).
- 💊 **Gestion des ordonnances et traitements** : Prescrire des médicaments, définir des posologies, et associer les traitements à un patient spécifique.
- 📄 **Génération d'ordonnances en PDF** : Export automatique d'une ordonnance officielle au format PDF, imprimable et transmissible au patient.
- ⚡ **Recherche instantanée** : Utilisation de `FilteredList` pour un filtrage réactif sans rechargement de la base de données.

---

## 📹 Vidéo de démonstration

▶️ **Cliquez ici pour visionner la démonstration complète de CareDash** :  
https://drive.google.com/file/d/1lm7V6VhnPP7gVjrlRcrJe5g7lCVWbAUe/view?usp=sharing

---

## Technologies utilisées

| Composant | Technologie |
| :--- | :--- |
| **Langage** | Java 8 / Java 11 |
| **Interface (IHM)** | JavaFX (FXML, CSS, Scene Builder) |
| **Base de données** | MySQL (via XAMPP / WampServer) |
| **Connectivité** | JDBC (Java Database Connectivity) |
| **IDE** | IntelliJ IDEA |
| **Gestion de version** | Git / GitHub |

---

## 📋 Prérequis (avant de lancer l'application)

Avant de lancer l'application, assurez-vous d'avoir installé :

1.  **Java JDK 8 ou 11** : [Télécharger Oracle JDK](https://www.oracle.com/java/technologies/downloads/) ou [OpenJDK](https://adoptium.net/).
2.  **MySQL Server** : Via [XAMPP](https://www.apachefriends.org/fr/index.html) ou [WampServer](https://www.wampserver.com/).
3.  **JavaFX SDK** (uniquement si vous utilisez Java 11+ ; pour Java 8, il est inclus). Téléchargez-le sur [Gluon](https://gluonhq.com/products/javafx/).

---

## 🚀 Installation et déploiement

### 1. Cloner / Ouvrir le projet
```bash
cd D:/mini-project/mini-project