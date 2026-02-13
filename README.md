# RAWG Games App

Application Android Kotlin + Jetpack Compose pour explorer des jeux vidéo via l’API RAWG, avec gestion de backlog et favoris en local.

---

## Installation

1. Ouvrir le projet dans **Android Studio**.  
2. Synchroniser les dépendances **Gradle**.  
3. Lancer sur un émulateur ou appareil.

---

## Configuration

### local.properties
Ajouter votre clé RAWG API :  
RAWG_API_KEY=clé_api

---

## Architecture

L’application suit le pattern **MVVM + Repository + DataSource** :
[API / Local Storage] -> DataSources -> Repositories -> ViewModels -> Composables (UI)


- **DataSources** : accès aux données (RAWG API ou JSON local).  
- **Repositories** : logique métier, combinaison des sources et cache.  
- **ViewModels** : gestion de l’état exposé via `StateFlow` / `LiveData`.  
- **UI (Compose)** : observe les ViewModels et affiche l’interface.

---

## Principaux composants

- **ApiModule / GameApiService** : configuration Retrofit + OkHttp pour RAWG API.  
- **GameRemoteDataSource** : transforme les réponses JSON en objets `Game`.  
- **BacklogDataSource / FavoritesDataSource** : stockage local JSON pour backlog et favoris.  
- **Repositories** : `GameRepository`, `BacklogRepository`, `FavoritesRepository`.  
- **ViewModels** : `GameListViewModel` (liste, recherche, filtrage), `GameDetailViewModel` (détails, backlog, favoris).  
- **UI (Compose)** : composables réutilisables (`GameItem`, `SearchBar`, `GameDetailContent`) et navigation via `NavHost`.

---

## Flux de données

- **Liste de jeux** : GameListViewModel → GameRepository → GameRemoteDataSource → API → Game → UI.  
- **Détail d’un jeu** : GameDetailViewModel → Repositories → DataSources → JSON/API → UI.  
- **Backlog / Favoris** : ViewModel → Repository → DataSource → JSON local.
