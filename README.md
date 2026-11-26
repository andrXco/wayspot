<div align="center">

<img src="./doc/icono-claro-wayspot.png" width="110" alt="WaySpot icon">

**Discover places. Share experiences. Find your next favorite spot.**

<a href="./README.md"><img src="https://img.shields.io/badge/ENGLISH-CURRENT-1F6B57?style=for-the-badge&labelColor=163D33" alt="English version, current page"></a>
<a href="./README.es.md"><img src="https://img.shields.io/badge/ESPA%C3%91OL-VER%20EN%20ESPA%C3%91OL-DDE8E1?style=for-the-badge&labelColor=50665B" alt="Leer en español"></a>

</div>

## Promotional video

https://github.com/user-attachments/assets/3c64ea2a-a61d-4401-9707-049da7fe5c72

---

## About WaySpot

WaySpot is an Android app designed to bring people together around discovering tourist destinations and special places. Users can explore places, share experiences through reviews and ratings, interact with community recommendations, and follow other travelers.

### Our value proposition

WaySpot helps travelers discover, rate, and share places through authentic community reviews. These experiences help people find destinations, make more informed travel plans, and connect with others who enjoy exploring.

---

## Main features

- Explore tourist destinations and special places by category.
- Find places on an interactive map.
- Publish reviews, ratings, and photos.
- Interact with reviews and follow other users.
- Save places for later.
- Browse a community feed and receive activity notifications.

---

## App mockups in motion

The animated preview cycles through the explore, map, and place-detail screens inside a WaySpot phone frame.

<div align="center">
  <img src="./doc/readme-assets/wayspot-mockups-motion.gif" width="500" alt="WaySpot app screens animated inside a phone mockup">
</div>

---

## Technologies

- Kotlin and Android
- Jetpack Compose and Material 3
- Firebase Authentication, Firestore, Cloud Functions, Storage, Analytics, Cloud Messaging, and Crashlytics
- MapLibre for maps
- Retrofit for REST API communication
- Hilt for dependency injection
- Git, GitHub, and Android Studio

---

## Technical overview: the `data` layer

The `data` package connects ViewModels to Firebase and REST services, keeping remote access and response conversion out of the UI layer.

### How a place request travels through the app

1. A ViewModel asks `PlaceRepository` for places or a place by ID.
2. Hilt provides the repository through its `@Inject` constructor and resolves its `PlaceRemoteDataSource` dependency.
3. `DataSourceModule` binds `PlaceRemoteDataSource` to `PlaceFirestoreDataSourceImplementation`. That implementation receives `FirebaseFirestore` from Hilt and reads the `places` collection; Firebase tasks are bridged to coroutines with `await()`.
4. Firestore documents are decoded as DTOs. The repository maps them into app models such as `PlaceInfo` and returns a Kotlin `Result` to the caller.

### How dependency injection is configured

- `FirebaseHiltModule` is installed in Hilt's `SingletonComponent`. Its `@Provides` methods create the Firebase Authentication, Storage, Firestore, and Functions dependencies; Firestore and Functions are marked as singletons.
- `AppModule` provides a singleton Retrofit client and the Place, Review, and User REST service interfaces.
- `DataSourceModule` uses `@Binds` to connect data-source interfaces to concrete implementations, including Firestore-backed place, review, and user sources and the callable source for nearby places.
- Repositories receive these interfaces in `@Inject` constructors. Hilt builds the dependency graph, so ViewModels can depend on repositories without constructing Firebase or Retrofit objects themselves.

Nearby-place lookup follows the same pattern: `NearbyPlacesRepository` calls the callable data source, maps each response to a `NearbyPlace`, and returns a `Result`. Repositories convert ordinary exceptions to failures and rethrow coroutine cancellation so cancellation keeps working correctly.

The package is organized into `datasource` (remote access), `dto` (service payloads), `mapper` (conversions), `model` (app data), `repository` (coordination and error handling), and `injection` (Hilt providers and bindings). `data/local/PreviewData.kt` and `PreviewDataPopular.kt` contain preview-only sample content; they are not a persistent local database.

---

## Visual identity

WaySpot's palette combines nature-inspired greens, warmth, and contrast to create a recognizable and welcoming visual identity.

<div align="center">
  <img src="./doc/paleta-wayspot.png" width="650" alt="WaySpot visual identity color palette">
</div>

---

## Project team

| Team member | Role |
| --- | --- |
| Andrés Cortés | Technical Project Manager and Developer |
| Santiago Bautista Velásquez | Scrum Master and Developer |
| Iván Fuenmayor | Software Developer |
| Andrés Rodríguez Díaz | Software Developer |

