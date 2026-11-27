# Firebase y desarrollo móvil

Referencia técnica y guía de práctica: Firestore, arquitectura Android, datos en tiempo real, Cloud Functions y notificaciones.

## Contenido

- [Firestore: conceptos y arquitectura](#firestore-conceptos-y-arquitectura)
- [Firestore avanzado](#firestore-avanzado)
- [Fundamentos de Android y Kotlin](#fundamentos-de-android-y-kotlin)
- [Simulacros de parcial](#simulacros-de-parcial)
- [Solucionarios](#solucionarios-de-los-simulacros)
- [Comparaciones rápidas](#comparaciones-rápidas)

## Firestore: conceptos y arquitectura

### Definiciones, siglas y razones de uso

| Concepto | Descripción |
| --- | --- |
| Firebase | Plataforma de servicios administrados para aplicaciones: autenticación, bases de datos, archivos, funciones y mensajería. |
| BaaS (Backend as a Service) | Backend como servicio: Firebase ofrece infraestructura y servicios administrados. Evita tener que alojar un backend y una base de datos propios para las operaciones del ejemplo. |
| SQL (Structured Query Language) | Lenguaje de consulta estructurado, usado en el backend relacional previo. |
| NoSQL (Not Only SQL) | Familia de bases de datos no relacionales. La clase se centra en documentos flexibles, no en todos los modelos NoSQL. La expansión de la sigla es una aclaración de estudio. |
| Cloud Firestore | Base de datos documental NoSQL en la nube; colecciones, documentos y campos. |
| Realtime Database | Otra base de datos NoSQL de Firebase. Es anterior a Firestore; ambas coexisten. |
| CRUD (Create, Read, Update, Delete) | Crear, leer, actualizar y eliminar datos. |
| DTO (Data Transfer Object) | Objeto de transferencia que representa datos intercambiados con una fuente externa. No tiene que coincidir con el modelo visual. |
| MVVM (Model–View–ViewModel) | Patrón de presentación que separa datos, vistas y lógica de presentación. |
| DI (Dependency Injection) | Inyección de dependencias. El proyecto utiliza Hilt para proporcionar instancias como Firestore. |
| Desnormalización | Duplicar o anidar datos para facilitar consultas y reducir la necesidad de lecturas relacionadas. Puede introducir inconsistencias. |
| Integridad de datos | Coherencia y validez de los datos. La duplicación de campos exige estrategias para conservarla. |

### SQL frente a Firestore: ¿por qué usar NoSQL?

| Concepto | Descripción |
| --- | --- |
| Estructura | SQL: tablas, filas y columnas con esquema definido. Firestore: colecciones, documentos y campos con esquema flexible. |
| Relaciones | SQL: claves primarias, foráneas y JOIN. Firestore: IDs, referencias, mapas, subcolecciones y consultas adicionales; no JOIN relacional nativo. |
| Normalización | SQL: se suele organizar para evitar redundancia. Firestore: puede duplicar datos deliberadamente según las lecturas requeridas. |
| Hospedaje del ejemplo | Express y PostgreSQL necesitan infraestructura de hosting si se publican. Firestore ya se ofrece como servicio administrado. |
| Ventaja de NoSQL | Libertad de estructura; un documento puede contener mapas y listas o tener campos distintos; consultas adaptadas a lo que la UI necesita. |
| Costo de NoSQL | Diseño dependiente de patrones de acceso, redundancia, posibles inconsistencias, escrituras adicionales y costos. |
| Tipado | Firestore tiene tipos de valores, pero no exige que todos los documentos de una colección compartan el mismo esquema. Kotlin sí tiene tipado estático. |

> **Importante:** NoSQL no implica ausencia de tipos ni que SQL sea siempre más lento. Se elige para mostrar otra estrategia de modelado y un backend administrado. La desnormalización es una opción de diseño, no una obligación de todas las bases NoSQL.

### Modelo documental, tipos e IDs

```text
users (collection)
  user123 (document)
    username: "pepito"
    name: "Juan"
    country: "Colombia"
    bio: null
tweets (collection)
  tweet456 (document)
    userId: "user123"
    content: "Hola"
    user: { name: "Juan", username: "pepito" }
```

- Colección: agrupación de documentos; equivalente aproximado a una tabla SQL.

- Documento: unidad de información; equivalente aproximado a una fila. Un documento puede tener campos distintos de otro documento.

- ID: identificador de documento, único dentro de su colección; los IDs automáticos son String, no Int.

- Tipos que se muestran en la consola: String, Number, Boolean, Map, Array, Null, Timestamp, GeoPoint y Reference.

- Map: permite almacenar objetos anidados; Array: permite guardar una lista; Reference: apunta a otro documento.

- Al configurar el servicio, selecciona modo de prueba para facilitar el desarrollo. No es configuración segura de producción. En la clase 11 se reemplazan las reglas temporales.

### Arquitectura e intercambio de implementaciones

```text
Compose Screen -> ViewModel -> Repository -> DataSource interface
                                            |-- Retrofit implementation
                                            |-- Firestore implementation
```

- Screen: representa datos y envía eventos; ViewModel: controla estado y acciones; Repository: coordina fuentes, mapea DTO y maneja Result; DataSource: realiza consultas concretas.

- La interfaz define un contrato común. Las implementaciones cambian; las capas superiores deben permanecer lo más estables posible.

- Crear la implementación Firestore en su propia carpeta. Las operaciones de Retrofit que ya no se necesitan quedan sin completar; la intercambiabilidad total exige implementar el mismo contrato en ambas fuentes.

- Al migrar, cambiar la implementación inyectada en los repositorios. Si UserRepository continúa consultando Retrofit, revisar la configuración de inyección.

- Hilt proporciona la instancia de FirebaseFirestore con @Provides y @Singleton. @Inject se usa en los constructores de dependencias; @Module y @InstallIn pertenecen a la configuración de Hilt vista anteriormente.

- En el Repository conviene recibir parámetros individuales y construir el DTO internamente; usar try/catch y retorna Result.success(Unit) o Result.failure(exception) para operaciones sin un resultado de datos.

```kotlin
@Provides
@Singleton
fun provideFirestore(): FirebaseFirestore =
    FirebaseFirestore.getInstance()

interface UserRemoteDataSource {
    suspend fun registerUser(userId: String, dto: RegisterUserDto)
}

class UserFirestoreDataSourceImplementation @Inject constructor(
    private val db: FirebaseFirestore
) : UserRemoteDataSource {
    override suspend fun registerUser(userId: String, dto: RegisterUserDto) {
        db.collection("users").document(userId).set(dto).await()
    }
}
```

### Authentication, perfil y registro

- Authentication crea la identidad del usuario y maneja credenciales. Firestore guarda el nombre, username, país y biografía.

- Primero autenticar/registrar, después obtener UID y escribir el documento users/{userId}. No guardar la contraseña nuevamente en Firestore.

- Nombre y username son obligatorios en el DTO de registro; país y biografía son opcionales/nullables en el ejemplo.

- El ViewModel comprueba campos y longitud de contraseña antes de iniciar la operación. La validación exige que no sea menor a seis.

- Error real: currentUser se inicializaba una sola vez al crear el DataSource. Un getter vuelve a leer el usuario actual en el DataSource y el AuthRepository.

- No continuar usando el ID quemado 1 que correspondía al backend anterior; obtener el UID actual para las rutas de perfil y la creación de tweets.

```kotlin
val currentUser: FirebaseUser?
    get() = FirebaseAuth.getInstance().currentUser

val uid = authRepository.currentUser?.uid
    ?: throw Exception("No se pudo obtener el usuario actual")
db.collection("users").document(uid).set(registerUserDto).await()
```

### DTO, deserialización y mapeo

- toObject convierte un DocumentSnapshot a un objeto Kotlin. Se agrega un constructor sin parámetros en el DTO cuando Firestore no puede deserializarlo.

- Los tipos y nombres deben coincidir con los datos de origen: country frente a location, Long frente a String, IDs del padre y usuario. Los documentos de pruebas viejos pueden tener tipos incompatibles; revisar o migrar sus datos.

- Los DTO pueden ser nullable; el modelo de UI debe evitar nulos innecesarios y usar textos por defecto como «No hay biografía».

- Se crea UserDtoGeneric, clase abstracta con un método de conversión a UserProfileInfo; cada DTO concreto (Retrofit/Firestore) adapta su estructura.

- El ID generado automáticamente pertenece al documento, no necesariamente al conjunto de campos. Al mapear resultados de getAllTweets, respuestas y tweets de un usuario, copiar doc.id al DTO.

- Si el documento no puede convertirse o no existe, el ejemplo lanza una excepción y el Repository la captura. No confundir el manejo de error con el estado visual.

```kotlin
data class UserFirestoreDto(
    val name: String = "",
    val country: String? = null,
    val bio: String? = null
)
// Alternativa explicita: constructor() : this("", null, null)

abstract class UserDtoGeneric {
    abstract fun toUserProfileInfo(): UserProfileInfo
}

val snapshot = db.collection("users").document(uid).get().await()
val user = snapshot.toObject(UserFirestoreDto::class.java)
    ?: throw Exception("User not found")
val location = user.country ?: "No hay ubicacion"
```

### CRUD y consultas: sintaxis fundamental

| Concepto | Descripción |
| --- | --- |
| collection("tweets") | Obtener referencia a una colección. |
| document(tweetId) | Obtener referencia a un documento; por sí sola no realiza escritura. |
| add(dto) | Crear documento con ID automático en una colección. |
| set(dto) | Escribir en una referencia con ID definido. En la clase se utiliza tanto al registrar el perfil como al editar el tweet. |
| get() | Leer un documento o consultar una colección una vez. |
| whereEqualTo(campo, valor) | Restringir resultados a los documentos con igualdad en el campo indicado. |
| update(campo, valor) | Actualizar un campo concreto, sin reemplazar todo el documento. Aparece especialmente en las transacciones de clase 11. |
| delete() | Eliminar el documento referenciado. |
| await() | Suspender la corrutina hasta obtener el resultado de la Task de Firebase. |
| toObject(T::class.java) | Convertir el snapshot de documento a DTO. |
| map { ... } | Transformar todos los elementos de una colección. |
| copy(id = doc.id) | Copiar un data class sustituyendo su campo ID. |

```kotlin
// CREATE: ID automatico
val ref = db.collection("tweets").add(dto).await()
// READ: uno
val snap = db.collection("tweets").document(tweetId).get().await()
val tweet = snap.toObject(TweetDto::class.java)
    ?: throw Exception("Tweet not found")
// READ: todos y sus IDs
val all = db.collection("tweets").get().await()
val tweets = all.documents.map { doc ->
    val item = doc.toObject(TweetDto::class.java)
        ?: throw Exception("Tweet not found")
    item.copy(id = doc.id)
}
// READ: respuestas / tweets de un usuario

val replies = db.collection("tweets")
    .whereEqualTo("parentTweetId", tweetId).get().await()
val userTweets = db.collection("tweets")
    .whereEqualTo("userId", uid).get().await()
// UPDATE: estrategia de la clase 10
 db.collection("tweets").document(tweetId).set(updatedDto).await()
// DELETE
 db.collection("tweets").document(tweetId).delete().await()
```

> **Importante:** parentTweetId representa aquí el campo del tweet padre (la transcripción también lo abrevia como parentId). En un examen de código debe usarse el nombre literal que aparezca en el DTO o la base de datos. set reemplaza los datos salvo opciones como merge; update cambia campos y normalmente exige un documento existente. Este matiz de API complementa el ejemplo.

### Desnormalización y consistencia de la foto de perfil

En SQL el backend obtenía los datos del autor por JOIN. En Firestore se guarda una copia del name, username y profileImage dentro del objeto user de cada tweet. El Repository obtiene la información de usuario y la foto de Authentication antes de crear el DTO. Puede coordinar más de un DataSource.

- Duplicar solo los datos utilizados en la tarjeta del tweet: no copiar país si no se muestra.

- Preferir campos poco cambiantes. El username fijo sería un ejemplo; nombre y foto pueden cambiar.

- Si un usuario cambia su nombre, la copia en los tweets antiguos no cambia automáticamente; se pierde sincronización.

- En la demostración de Storage, reemplazar el archivo de perfil en la misma ubicación conserva la URL usada. Los tweets que ya tienen esa URL pueden mostrar la foto nueva; los tweets con una foto null no se corrigen por sí solos. No asumir que toda URL se conserva en todos los diseños.

- Alternativas: obligar al usuario a tener foto inicial; actualizar todos los tweets (costoso); actualizar solo los últimos 500 (compromiso); usar Cloud Functions para reaccionar a cambios.

### Respuestas, eliminación y actualización de pantallas

- Las respuestas son tweets con un ID de padre. No se necesita otra pantalla para crear, responder y editar si se interpreta correctamente el ID de tweet y el ID del padre.

- No hay borrado relacional en cascada automático de los tweets que apuntan al padre. Se buscan hijos y aplica recursividad para eliminar también respuestas anidadas.

- Se revisa init frente a LaunchedEffect para refrescar datos cuando se vuelve a una pantalla. init ocurre al crear el ViewModel; LaunchedEffect se ejecuta al entrar en composición o cambiar su clave, no en cualquier navegación por definición.

- La pantalla de perfil distingue si el usuario puede ver botones de edición. La decisión visual no sustituye las reglas de autorización en el servidor.

### Errores reales y reglas de oro

| Concepto | Descripción |
| --- | --- |
| Datos no cargan tras migrar | Revisar implementación seleccionada y llamadas aún no implementadas. |
| Perfil no encontrado | Revisar UID real y dejar de usar el valor 1 del backend de pruebas. |
| No se puede deserializar | Constructor sin argumentos y compatibilidad de los campos del DTO. |
| País vacío | country frente a location: resolver en DTO/mapeo, no en cada pantalla. |
| Long no convertible a String | IDs antiguos o DTO desactualizados; revisar documentos de desarrollo y migración. |
| Navegación falla con tweet ID null | Recuperar doc.id al mapear todos los resultados. |
| Datos de autor viejos | Desnormalización; elegir estrategia de sincronización y considerar escrituras. |
| Regla de diseño | Contratos claros, responsabilidad única, reducir acoplamiento, no duplicar credenciales, no desnormalizar datos innecesarios. |
| Regla de depuración | Usar logs y catch para localizar dónde falla la consulta; probar después de cada cambio. |

## Firestore avanzado

### Subcolecciones: por qué no usar un Array gigante

```text
tweets/tweet123
    likesCount: 2
    likes/userA { timestamp: ... }
    likes/userB { timestamp: ... }
```

- Una relación muchos a muchos se modela en SQL con tabla intermedia. Firestore permite colecciones dentro de documentos: subcolecciones.

- Se descarta guardar todos los IDs de los likes en un Array: el documento tiene límite de tamaño (aprox. 1 MiB) y cada lectura traerá datos que la tarjeta no necesita.

- El documento padre conserva likesCount; la subcolección contiene los usuarios que dieron like. Leer el padre no trae automáticamente todos los documentos de la subcolección.

- El userId como ID del like impide que existan dos documentos distintos para el mismo usuario y tweet bajo esa ruta.

- Diseñar la base de datos según consultas y UI. Si solo necesitas el número, lee el contador; si necesitas la lista de usuarios, consulta likes.

### Transacciones, atomicidad y toggle de likes

| Concepto | Descripción |
| --- | --- |
| Transacción | Grupo de operaciones que se confirma atómicamente. Si no puede completarse, no se aplican escrituras parciales. |
| Rollback | Reversión o ausencia de cambios parciales cuando falla una operación agrupada. |
| Toggle | Alternar entre dar y quitar like según exista el documento de relación. |
| runTransaction | Lee y escribe dentro de la transacción. Se usa cuando la escritura depende de una lectura previa. |
| FieldValue.increment | Incremento/decremento atómico de un campo numérico. |
| FieldValue.serverTimestamp | Marca de tiempo asignada por el servidor, no por el reloj local. |

```kotlin
val tweetRef = db.collection("tweets").document(tweetId)
val likeRef = tweetRef.collection("likes").document(userId)
db.runTransaction { tx ->
    val likeDoc = tx.get(likeRef)
    if (likeDoc.exists()) {
        tx.delete(likeRef)
        tx.update(tweetRef, "likesCount", FieldValue.increment(-1))
    } else {
        tx.set(likeRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
        tx.update(tweetRef, "likesCount", FieldValue.increment(1))
    }
}.await()
```

El DTO y el modelo visual deben incluir información de si el usuario actual dio like para pintar el icono correctamente. Se comprueba la existencia del documento con get().await().exists() y se ajusta la UI para no incrementar siempre cuando la operación realmente quitó un like.

### Follow y unfollow: cuatro escrituras

```text
A follows B:
users/A/following/B          create
users/B/followers/A          create
users/A.followingCount       +1
users/B.followersCount       +1
```

Al dejar de seguir, se eliminan los documentos y se decrementan ambos contadores. La función recibe currentUserId y targetUserId. El perfil necesita saber si el usuario actual sigue al usuario destino (booleano), para representar el estado del botón y el contador. La transacción mantiene coherentes las cuatro escrituras.

```kotlin
val current = db.collection("users").document(currentUserId)
val target = db.collection("users").document(targetUserId)
val following = current.collection("following").document(targetUserId)
val follower = target.collection("followers").document(currentUserId)
db.runTransaction { tx ->
    val exists = tx.get(following).exists()
    if (exists) {
        tx.delete(following); tx.delete(follower)
    } else {
        val data = mapOf("timestamp" to FieldValue.serverTimestamp())
        tx.set(following, data); tx.set(follower, data)
    }
    val delta = if (exists) -1L else 1L
    tx.update(current, "followingCount", FieldValue.increment(delta))
    tx.update(target, "followersCount", FieldValue.increment(delta))
}.await()
```

### Flow: consulta estática frente a escucha en vivo

| Concepto | Descripción |
| --- | --- |
| getAllTweets() | Consulta puntual: retorna List&lt;TweetDto&gt;; se necesita consultar otra vez para obtener cambios. |
| listenAllTweets() | Consulta reactiva: retorna Flow&lt;List&lt;TweetDto&gt;&gt;; emite nuevas listas al cambiar los datos observados. |
| Flow | Secuencia asíncrona de valores de Kotlin. |
| callbackFlow | Adapta la API de callbacks de Firestore a un Flow. |
| addSnapshotListener | Registra una escucha de snapshots y recibe snapshot y error. |
| trySend | Envía/emite un valor desde el callback hacia el canal del Flow. |
| close(error) | Cierra el canal y propaga el error. |
| awaitClose | Espera cierre/cancelación y permite ejecutar limpieza. |
| listener.remove() | Desregistra la escucha de Firestore. |
| map | Transforma los DTO en datos de UI dentro del Repository. |
| catch | Maneja excepciones del flujo aguas arriba. |
| collect | Consume las emisiones en el ViewModel. |
| viewModelScope.launch | Lanza una corrutina asociada al ViewModel para recolectar el flujo. |

```kotlin
fun listenAllTweets(): Flow<List<TweetDto>> = callbackFlow {
    val listener = db.collection("tweets")
        .addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val tweets = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(TweetDto::class.java)?.copy(id = doc.id)
                }
                trySend(tweets)
            }
        }
    awaitClose { listener.remove() }
}
// Repository: mapear DTO a modelo UI
fun getTweetsLive() = dataSource.listenAllTweets()
    .map { list -> list.map { it.toTweetInfo() } }
// ViewModel: consumir emisiones
viewModelScope.launch {
    repository.getTweetsLive()
        .catch { error -> /* actualizar estado de error */ }
        .collect { tweets -> /* actualizar estado */ }
}
```

> **Importante:** El listener sobre tweets observa los documentos de esa consulta, no todos los documentos de sus subcolecciones. Cambiar solo likes/userId no cambia por sí mismo el documento padre; actualizar likesCount sí puede provocar una nueva emisión.

- El ejemplo compara el resultado con el get tradicional y mantiene ambas funciones porque tienen contratos diferentes.

- Escuchar cambios no es necesariamente apropiado para todo el feed: más lecturas, recursos y costos. Para chat o casos de ubicación/notificaciones la escucha puede estar justificada.

- El ejemplo de WhatsApp y costos de ubicación es una hipótesis explicativa de clase, no un dato verificado sobre esa empresa.

- No confundir collect() con collection(): uno consume emisiones Kotlin, el otro selecciona una colección Firestore.

### Cloud Functions y batch

Cloud Functions ejecuta lógica en un entorno administrado al producirse un evento. Se utiliza para sincronizar las fotos duplicadas de todos los tweets y luego para enviar notificaciones de nuevos seguidores.

| Concepto | Descripción |
| --- | --- |
| Node.js / npm | Entorno de ejecución de JavaScript y gestor de paquetes. |
| Firebase CLI | Herramienta de terminal para autenticarse, inicializar y desplegar servicios. |
| firebase init | Configurar el proyecto local y seleccionar Functions. |
| functions/index.js | Archivo de JavaScript utilizado en el ejemplo. También se mencionan otros lenguajes disponibles. |
| firebase deploy | Publicar las funciones. Complemento: firebase deploy --only functions limita el despliegue a Functions. |
| firebase-admin | SDK de administración para acceder a Firestore y Messaging desde servidor. |
| firebase-functions/v2/firestore | Módulo de triggers de Firestore de segunda generación. |
| firebase-functions/logger | Logs de diagnóstico de las funciones desplegadas. |
| onDocumentUpdated | Evento disparado cuando se modifica un documento. |
| onDocumentCreated | Evento disparado al crearse un documento. |
| event.params | Parámetros de la ruta (por ejemplo userId). |
| event.data.before / after | Snapshots anteriores y posteriores a la actualización. |
| db.batch() | Agrupa escrituras preparadas; no depende de leer dentro de la operación. |
| batch.update() / commit() | Preparar modificaciones y luego confirmarlas atómicamente. |

```javascript
const admin = require("firebase-admin");
const logger = require("firebase-functions/logger");
const {onDocumentUpdated, onDocumentCreated} =
    require("firebase-functions/v2/firestore");
admin.initializeApp();
const db = admin.firestore();

exports.updateUserInfoInTweets = onDocumentUpdated(
  "users/{userId}", async (event) => {
    const {userId} = event.params;
    const before = event.data.before.data();
    const after = event.data.after.data();
    if (before.profileImage === after.profileImage) {
      logger.info("No profile image change");
      return;
    }
    const tweets = await db.collection("tweets")
      .where("userId", "==", userId).get();
    const batch = db.batch();
    tweets.forEach(doc => {
      batch.update(doc.ref, {"user.profileImage": after.profileImage});
    });
    await batch.commit();
    logger.info("Tweets updated");
  });
```

- Comparar before y after antes de consultar todos los tweets: evitar escrituras por cambios de campos irrelevantes.

- En JavaScript Admin SDK se usa where("userId", "==", userId); en Kotlin Android SDK se usa whereEqualTo("userId", userId).

- La función no se ejecuta por el simple hecho de escribirla: hay que desplegarla y producir el evento.

- Error de la demostración: intentar actualizar una ruta de campo equivocada. El campo de imagen está anidado dentro de user; usar user.profileImage. Revisar logs en la consola.

- El ejemplo desactiva ESLint durante la configuración del ejercicio para simplificar. No es una regla general de calidad.

- Matiz técnico: un batch tiene límites, y una Cloud Function no garantiza consistencia instantánea con el documento original. Para gran cantidad de tweets se requiere una estrategia de procesamiento por lotes y reintentos.

### Firebase Cloud Messaging: notificaciones push

| Concepto | Descripción |
| --- | --- |
| FCM (Firebase Cloud Messaging) | Servicio para enviar mensajes/notificaciones a instalaciones de una aplicación. |
| Push notification | Notificación enviada desde un servicio remoto al dispositivo, no una consulta manual de la aplicación. |
| Campaña | Mensaje distribuido a un conjunto de instalaciones desde la consola. |
| Token FCM | Identificador de registro de una instalación para direccionar mensajes; no es el UID de Authentication. |
| FirebaseMessagingService | Clase base del servicio Android de mensajería. |
| getInstance().token | Task de FirebaseMessaging para obtener el token de la instalación. |
| addOnCompleteListener | Callback que permite inspeccionar éxito y resultado de una Task. |
| await() | Alternativa usada cuando se obtiene el token dentro del registro asíncrono. |
| POST_NOTIFICATIONS | Permiso de notificaciones en Android 13/API 33 y posteriores según corresponda. |
| Firebase Analytics | Dependencia que se agrega al habilitar Messaging desde la consola. |
| Google Services | Plugin/configuración de integración del proyecto Firebase con Android. |

```kotlin
class MyFirebaseMessagingService : FirebaseMessagingService()
```

En `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<!-- Dentro de application -->
<service android:name=".data.datasource.services.MyFirebaseMessagingService"
    android:exported="false">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>
```

```kotlin
val token = FirebaseMessaging.getInstance().token.await()
// Alternativa con callback:
FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
    if (task.isSuccessful) Log.d("FCM", task.result)
}
```

### Permiso de notificaciones y versión de Android

```kotlin
val context = LocalContext.current
val launcher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
) { granted ->
    // reaccion ante permiso concedido o denegado
}
LaunchedEffect(Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
```

- TIRAMISU corresponde a API 33. El permiso debe declararse en el Manifest y solicitarse en ejecución cuando aplique.

- Verificar si ya fue concedido; no pedirlo innecesariamente. Se recomienda desinstalar/reinstalar para probar el diálogo desde cero.

- El primer intento de campaña no muestra la notificación porque el permiso no estaba concedido; habilitarlo manualmente permite comprobar la causa.

- El ejemplo prueba mensajes de notificación con la app en segundo plano o cerrada y advierte que el envío no siempre llega instantáneamente. En primer plano se requiere código de recepción/visualización adicional, no desarrollado por falta de tiempo.

- Guardar el token en Firestore al registrar o iniciar sesión. Aclaración técnica: puede rotar y un usuario puede tener varios dispositivos; un único token es una simplificación de clase.

### Notificación al recibir un nuevo seguidor

```javascript
exports.notifyFollower = onDocumentCreated(
  "users/{userId}/followers/{followerId}", async (event) => {
    const {userId, followerId} = event.params;
    const target = await db.collection("users").doc(userId).get();
    const token = target.data()?.fcmToken;
    if (!token) { logger.warn("No FCM token"); return; }
    const follower = await db.collection("users").doc(followerId).get();
    const name = follower.data()?.username;
    const message = {
       token: token,
       notification: {title: "Nuevo seguidor",
         body: `${name} ha comenzado a seguirte`}
    };
    try { await admin.messaging().send(message); }
    catch (error) { logger.error("Error al enviar", error); }
  });
```

> **Importante:** El nombre exacto del campo de token debe respetar el documento: una discrepancia de mayúsculas/minúsculas puede impedir que se lea el token. fcmToken es un identificador ilustrativo; si el proyecto utiliza fcmtoken, debe usarse ese nombre. userId recibe la notificación; followerId identifica a quien siguió al usuario.

### Reglas de seguridad de Firestore

El modo de prueba permite operaciones durante un periodo temporal (aproximadamente 30 días en el ejemplo). Después hay que establecer reglas de seguridad apropiadas. La UI puede limitar caracteres o evitar acciones, pero un cliente modificado puede saltarse esas comprobaciones.

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

La regla anterior es un ejemplo básico de autenticación, no una política completa. El siguiente ejemplo independiente restringe creación de tweets; no se debe combinar ingenuamente con la regla general permisiva.

```javascript
match /tweets/{tweetId} {
  allow create: if request.auth != null
    && request.resource.data.content.size() < 200

      && request.resource.data.userId == request.auth.uid
      && request.resource.data.likesCount is int
      && request.resource.data.likesCount >= 0;
}
```

| Concepto | Descripción |
| --- | --- |
| match /tweets/{tweetId} | Un segmento variable; aplica a documentos de tweets, no automáticamente a sus subcolecciones. |
| match /tweets/{document=**} | Comodín recursivo; puede afectar documentos descendientes bajo tweets. |
| allow read / write | Autorización de lectura o escritura; create, update y delete pueden tratarse por separado. |
| request.auth != null | Exige un usuario autenticado. |
| request.auth.uid | UID de la identidad que hace la petición. |
| request.resource.data | Datos propuestos en la escritura. |
| resource.data | Datos actuales almacenados. |
| size() | Longitud del contenido (u otro tamaño según tipo). |
| is int | Comprobación de tipo entero; int se escribe en minúscula. |
| &gt;= 0 | Evita contadores negativos. |
| && | Conjunción lógica: deben cumplirse todas las condiciones. |

> **Importante:** Complemento técnico: cuando varias reglas match aplican, basta que alguna autorice la solicitud. Una regla global de escritura para usuarios autenticados puede anular la protección práctica de una regla específica. Además, verificar que likesCount sea no negativo no impide cualquier manipulación; una política real requiere reglas adicionales.

### Reglas de oro y diferencias clave

| Concepto | Descripción |
| --- | --- |
| Array vs subcolección | Array crece dentro del documento y se lee con él; subcolección permite consultas separadas. |
| set vs add | set usa una referencia con ID definido; add genera automáticamente el ID. |
| Transacción vs batch | Transacción: decisiones basadas en lecturas y escrituras agrupadas. Batch: agrupa escrituras ya decididas. |
| get vs listener | Lectura puntual frente a escucha de cambios. |
| Flow vs StateFlow | Secuencia asíncrona frente a flujo de estado que conserva valor actual. |
| trySend vs collect | Emisión desde callback frente a consumo en la capa superior. |
| await vs awaitClose | Espera de Task/resultado frente a limpieza al cerrar o cancelar callbackFlow. |
| Cloud Functions vs FCM | Funciones reaccionan a eventos y ejecutan lógica; FCM transporta notificaciones. |
| onDocumentCreated vs Updated | Creación de un documento frente a modificación de uno existente. |
| UID vs token FCM | Identidad de usuario frente a registro de una instalación para mensajería. |
| Frontend vs reglas | La UI orienta al usuario; las reglas autorizan o rechazan operaciones de datos. |
| Errores reales | Ruta de campo mal escrita, token con capitalización distinta, permiso no concedido, listener de padre que no observa subcolecciones. |

## Fundamentos de Android y Kotlin

### Definiciones y diferencias transversales

| Concepto | Descripción |
| --- | --- |
| Kotlin | Lenguaje de tipado estático con inferencia de tipos. Que no escribas Int no significa que la variable pueda cambiar a String. |
| val vs var | val: referencia no reasignable; var: reasignable. Se recomienda comenzar con val. Un objeto referenciado por val puede ser mutable. |
| String? vs String | Tipo nullable frente a tipo no nullable. |
| ?. vs ?: vs !! | Acceso seguro, valor alternativo (Elvis), afirmación de no nulidad que puede fallar. |
| Unit vs Nothing | Unit: retorno sin valor útil. Nothing: expresión que no retorna normalmente. Matiz técnico. |
| data class | Genera funciones como copy, toString y equals. |
| @Composable vs @Preview | Función de UI Compose frente a vista de diseño en Android Studio. |
| Row vs Column vs Box | Horizontal, vertical y superposición/alineación de hijos, respectivamente. |
| remember vs mutableStateOf | remember conserva el valor entre recomposiciones; mutableStateOf hace observable el estado para Compose. |
| State hoisting | Elevar estado al padre; pasar valores hacia abajo y eventos hacia arriba. Favorece componentes reutilizables. |
| Screen vs ScreenContent | Screen conecta dependencias y estado; Content recibe datos y callbacks. Se recomienda separar y facilitar previews. |
| NavHost vs NavController | Host asocia rutas a composables; controlador ejecuta navegación. rememberNavController crea/recuerda el controlador. |
| navigate vs popBackStack | Navegar a destino frente a volver quitando destinos de la pila. |
| Single Activity | Una actividad aloja múltiples pantallas Compose. Se recomienda centralizar la navegación y no pasar NavController a cada hijo. |
| MVVM vs State hoisting | MVVM separa modelo, vista y lógica de presentación; state hoisting organiza la propiedad del estado en componentes. |
| MutableLiveData vs LiveData | Estado observable mutable frente a interfaz pública de lectura; observeAsState para Compose. |
| MutableStateFlow vs StateFlow | Estado mutable privado frente a interfaz observable de lectura pública; conserva valor actual. |
| Flow vs StateFlow | Flow emite una secuencia; StateFlow conserva valor actual y requiere valor inicial. |
| collect vs collectAsState | Consumo en corrutina frente a adaptación a State de Compose. |
| launch vs async | launch retorna Job y ejecuta trabajo; async retorna Deferred&lt;T&gt; y permite obtener resultado con await. |
| suspend vs launch | suspend permite suspender una función; no crea una corrutina ni cambia de hilo por sí sola. launch inicia una corrutina. |
| delay vs Thread.sleep | delay suspende la corrutina sin bloquear el hilo; Thread.sleep bloquea el hilo. Aclaración técnica a la analogía de clase. |
| viewModelScope | Ámbito de corrutinas asociado al ViewModel; se cancela al eliminarlo. |
| Dispatchers.Main / IO / Default | Main para UI; IO para operaciones bloqueantes de E/S; Default para cálculo intensivo. No todas las llamadas Firebase necesitan cambiar dispatcher. |
| Firebase Authentication vs Firestore | Identidad y credenciales frente a documentos de datos de aplicación. |
| Firebase Storage vs Firestore | Archivos binarios frente a documentos; guardar URL de descarga en los datos cuando corresponda. |
| URI vs URL | URI identifica un recurso; en el ejemplo Android usa URI de galería y URL de descarga de Storage. |
| Coil vs Retrofit | Coil carga imágenes (AsyncImage); Retrofit consume APIs HTTP. |
| GsonConverterFactory vs ScalarsConverterFactory | Conversión de JSON frente a valores simples/texto. En clase se agregan ambos a Retrofit. |
| Retrofit vs Firebase SDK | Retrofit llama endpoints HTTP definidos en una API; el SDK Firebase expone métodos de sus servicios. |
| REST vs SQL | REST organiza una API HTTP; SQL consulta datos relacionales. No son tecnologías equivalentes. |
| GET / POST / PUT / DELETE | Leer, crear, actualizar, eliminar en el CRUD REST del ejemplo. |
| Express vs Node.js | Framework de backend frente a entorno de ejecución de JavaScript. |
| Sequelize vs PostgreSQL | ORM/acceso a datos frente a motor relacional. |
| Morgan / nodemon / Postman / DBeaver | Logs HTTP; recarga de backend; pruebas HTTP; interfaz de bases de datos. |
| Emulador Android vs teléfono físico | Entorno virtual frente a dispositivo real. Se recomienda un dispositivo moderado y el mismo modelo en el equipo de trabajo. |
| localhost vs 10.0.2.2 | Desde el emulador Android, 10.0.2.2 apunta al host de desarrollo para consumir Express; localhost apunta al propio emulador. |
| Gradle vs AndroidManifest | Dependencias y compilación frente a permisos, servicios y configuración de aplicación. |

### Recomendaciones prácticas previas

- Usar Device Manager para crear un emulador moderado, como Pixel 6a, y detenerlo al terminar para liberar recursos. También se puede usar un teléfono físico.

- Evitar tildes y espacios en nombres/rutas de proyectos según la recomendación práctica de clase. No es una restricción universal del lenguaje.

- Tras sincronizar Gradle, ejecutar la aplicación completa: un Preview exitoso no prueba ausencia de incompatibilidades de dependencias.

- Separar screens, components, utils, navigation e injection. Componentes compartidos van en utils; los propios de una pantalla, en sus components. Mantener MainActivity limpia.

- Centralizar navegación en AppNavigation y pasar callbacks a las pantallas, no NavController por todos los componentes.

- Para acceder al backend local desde el emulador, usar la dirección 10.0.2.2 con puerto 3000. Configurar network_security_config.xml y referenciarlo desde AndroidManifest. No habilitar HTTP inseguro globalmente en producción.

- Para la clase de corrutinas se recomienda ejecutar IntelliJ, no el compilador online, para observar paso a paso el comportamiento de la concurrencia.

- La referencia de emulador aquí es el emulador Android. No se atribuye al profesor el uso de Firebase Emulator Suite en las dos clases principales sin evidencia de ello.

```kotlin
// StateFlow: escritura privada, lectura publica
private val _uiState = MutableStateFlow(HomeState())
val uiState: StateFlow<HomeState> = _uiState
fun changeContent(value: String) {
    _uiState.update { it.copy(content = value) }

}
// Compose
val state by viewModel.uiState.collectAsState()
var text by remember { mutableStateOf("") }
// State hoisting: valores hacia abajo, eventos hacia arriba
TextField(value = state.content, onValueChange = viewModel::changeContent)
// Hilt / Retrofit
@Provides
@Singleton
fun provideRetrofit(): Retrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:3000/")
    .addConverterFactory(GsonConverterFactory.create())
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()
```

### Librerías y paquetes: reconocimiento rápido

Los nombres de artefactos siguientes son referencias de estudio habituales, no una transcripción literal de las líneas Gradle ni una afirmación de versión. Las versiones exactas no están disponibles.

| Concepto | Descripción |
| --- | --- |
| Hilt / Dagger | DI; com.google.dagger:hilt-android; anotaciones @Inject, @Provides, @Singleton, @HiltViewModel. |
| Firestore SDK | com.google.firebase:firebase-firestore; FirebaseFirestore, collection, document. |
| Authentication SDK | com.google.firebase:firebase-auth; FirebaseAuth, currentUser, uid. |
| Storage SDK | com.google.firebase:firebase-storage; FirebaseStorage, putFile, downloadUrl. |
| Messaging SDK | com.google.firebase:firebase-messaging; FirebaseMessaging, FirebaseMessagingService. |
| Firebase Analytics | com.google.firebase:firebase-analytics; integración del ejemplo de Messaging. |
| Kotlin Coroutines | kotlinx.coroutines; Flow, StateFlow, launch, async, callbackFlow. |
| Coroutines Play Services | org.jetbrains.kotlinx:kotlinx-coroutines-play-services; kotlinx.coroutines.tasks.await. |
| Navigation Compose | androidx.navigation:navigation-compose; NavHost, composable, rememberNavController. |
| Lifecycle ViewModel Compose | androidx.lifecycle:lifecycle-viewmodel-compose; viewModel(). |
| LiveData Compose | androidx.compose.runtime:runtime-livedata; observeAsState(). |
| Retrofit | com.squareup.retrofit2:retrofit; @GET, @POST, @PUT, @DELETE, @Path, @Body. |
| Coil | Carga imágenes mediante AsyncImage e ImageRequest.Builder. El artefacto exacto depende de la versión usada. |
| Firebase Admin / Functions | firebase-admin y firebase-functions (npm), no son dependencias Android. |

## Simulacros de parcial

Marca una sola opción por pregunta. Las claves están al final.

### Simulacro 1. Clase 10: Firestore y arquitectura

**1. ¿Qué tipo de base de datos es Firestore?**

- A. ORM
- B. Relacional SQL
- C. Grafo únicamente
- D. Documental NoSQL

**2. ¿Qué significa NoSQL (expansión habitual)?**

- A. No Structured Query Layer
- B. Not Only SQL
- C. Network SQL
- D. New Object SQL

**3. ¿Por qué se utiliza NoSQL en la clase?**

- A. Esquema flexible
- B. Normalización obligatoria
- C. Tablas rígidas
- D. JOIN obligatorio

**4. ¿Qué ventaja concreta tiene Firebase como BaaS?**

- A. Emulador integrado
- B. Backend administrado
- C. UI automática
- D. SQL obligatorio

**5. ¿Qué otra base NoSQL de Firebase se menciona?**

- A. PostgreSQL
- B. Realtime Database
- C. SQLite
- D. Room

**6. ¿Qué equivale aproximadamente a una tabla SQL?**

- A. Timestamp
- B. Colección
- C. Campo
- D. Documento

**7. ¿Qué equivale aproximadamente a una fila SQL?**

- A. Referencia
- B. Colección
- C. Mapa
- D. Documento

**8. ¿Qué tipo tienen los IDs automáticos Firestore?**

- A. Boolean
- B. String
- C. Double
- D. Int

**9. ¿Todos los documentos deben tener los mismos campos?**

- A. Solo con Hilt
- B. Solo en pruebas
- C. Siempre
- D. No necesariamente

**10. ¿Qué tipo permite almacenar un objeto anidado?**

- A. Boolean
- B. Map
- C. Timestamp
- D. GeoPoint

**11. ¿Qué tipo permite guardar una lista?**

- A. String obligatorio
- B. DocumentId
- C. Reference
- D. Array

**12. ¿Qué tipo identifica coordenadas geográficas?**

- A. GeoPoint
- B. Timestamp
- C. Number obligatorio
- D. Map obligatorio

**13. ¿Firestore carece de tipos de datos?**

- A. No, tiene tipos
- B. Sí, todos Object
- C. Solo con Kotlin
- D. Sí, todos String

**14. ¿Qué operación SQL no tiene equivalente relacional nativo en Firestore?**

- A. Escritura
- B. JOIN
- C. Eliminación
- D. Lectura

**15. ¿Qué servicio maneja credenciales?**

- A. Firestore
- B. Authentication
- C. Cloud Functions
- D. Storage

**16. ¿Dónde se guarda el perfil adicional?**

- A. Authentication únicamente
- B. Retrofit
- C. Firestore
- D. Gradle

**17. ¿Qué dato no debe duplicarse en Firestore?**

- A. Biografía
- B. Contraseña
- C. Username
- D. País

**18. ¿Qué dato se usa como ID de perfil?**

- A. Email obligatorio
- B. Tweet ID
- C. UID
- D. Token FCM

**19. ¿Qué modo elige al iniciar Firestore durante el desarrollo?**

- A. Producción restringida
- B. Modo SQL
- C. Modo offline
- D. Modo de prueba

**20. ¿Qué librería consumía el backend Express?**

- A. Room
- B. Coil
- C. Retrofit
- D. Hilt

**21. ¿Qué biblioteca inyecta dependencias?**

- A. Hilt
- B. Gson
- C. Coil
- D. Postman

**22. ¿Qué anotación proporciona una dependencia?**

- A. @GET
- B. @Composable
- C. @Provides
- D. @Preview

**23. ¿Qué anotación define alcance compartido?**

- A. @Preview
- B. @Body
- C. @Singleton
- D. @State

**24. ¿Qué significa DTO?**

- A. Data Transfer Object
- B. Data Type Output
- C. Direct Table Object
- D. Dynamic Task Operation

**25. ¿Qué responsabilidad tiene DataSource?**

- A. Permisos visuales
- B. Consultas concretas
- C. Navegar
- D. Renderizar UI

**26. ¿Qué responsabilidad tiene la interfaz DataSource?**

- A. Crear Activity
- B. Compilar Gradle
- C. Definir contrato
- D. Pintar lista

**27. ¿Qué capa convierte los DTO a información visual?**

- A. Activity
- B. Manifest
- C. Repository
- D. NavHost

**28. ¿Qué capa administra el estado de pantalla?**

- A. Firestore Console
- B. DTO
- C. Gradle
- D. ViewModel

**29. ¿Qué técnica usa para DTO específicos con contrato común?**

- A. Preview
- B. Herencia abstracta
- C. State hoisting
- D. JOIN

**30. ¿Qué debe cambiar al migrar Retrofit a Firestore?**

- A. Todos los estados
- B. Implementación seleccionada
- C. Lenguaje Kotlin
- D. Todas las pantallas

**31. ¿Qué resultado tiene una operación exitosa sin datos?**

- A. Result.success(Unit)
- B. Flow.failure()
- C. Result.failure(Unit)
- D. Result.null()

**32. ¿Qué falla cuando un DTO no tiene constructor adecuado?**

- A. Inyección siempre
- B. Permiso FCM
- C. Navegación siempre
- D. Deserialización

**33. ¿Qué corrige country vs location?**

- A. DTO/mapeo
- B. Gradle
- C. Coil
- D. Manifest

**34. ¿Qué problema causa Long antiguo frente a String?**

- A. Incompatibilidad de tipo
- B. Falta de token
- C. Fallo de Storage
- D. Duplicación de UI

**35. ¿Cómo obtiene el usuario actual sin capturar un valor viejo?**

- A. Array
- B. Constructor vacío
- C. Getter
- D. Batch

**36. ¿Qué método referencia una colección?**

- A. document
- B. collection
- C. collect
- D. set

**37. ¿Qué método referencia un documento?**

- A. collection
- B. update
- C. document
- D. map

**38. ¿Qué método genera ID automático?**

- A. get
- B. set
- C. add
- D. update

**39. ¿Qué método usa el profesor para editar el tweet completo?**

- A. set
- B. collect
- C. delete
- D. add

**40. ¿Qué método lee una sola vez?**

- A. get
- B. trySend
- C. listen
- D. commit

**41. ¿Qué método filtra por igualdad?**

- A. whereEqualTo
- B. filterSQL
- C. whereLike
- D. selectEqual

**42. ¿Qué método convierte snapshot a DTO?**

- A. toJSON
- B. toFlow
- C. toObject
- D. toState

**43. ¿Qué función permite esperar una Task Firebase?**

- A. await
- B. runTransaction
- C. remember
- D. collect

**44. ¿Qué expresión recupera el identificador real?**

- A. doc.key
- B. doc.name
- C. doc.id
- D. doc.uid

**45. ¿Qué método copia un data class modificando un campo?**

- A. cloneAll
- B. replaceObject
- C. updateState
- D. copy

**46. ¿Qué riesgo introduce la desnormalización?**

- A. Error de compilación
- B. Falta de permisos
- C. Pérdida de sintaxis
- D. Inconsistencia

**47. ¿Qué campos conviene duplicar?**

- A. Datos aleatorios
- B. Necesarios para UI
- C. Todos
- D. Credenciales

**48. ¿Qué datos son preferibles para desnormalizar?**

- A. Poco cambiantes
- B. Tokens secretos
- C. Contraseñas
- D. Muy cambiantes

**49. ¿Qué información se duplica en el tweet del ejemplo?**

- A. Nombre, username, foto
- B. Contraseña, email
- C. País, biografía
- D. Token FCM, país

**50. ¿Qué pasa con tweets antiguos que tienen foto null?**

- A. No se corrigen solos
- B. Se actualizan por JOIN
- C. Se crean otra vez
- D. Se eliminan

**51. ¿Qué propone para reducir las escrituras al actualizar fotos?**

- A. Todos los usuarios
- B. Duplicar contraseñas
- C. Eliminar colecciones
- D. Últimos 500 tweets

**52. ¿Qué técnica usa para borrar respuestas anidadas?**

- A. Coil
- B. Recursividad
- C. Flow
- D. Inyección

**53. ¿Qué distingue una respuesta de un tweet original?**

- A. Email
- B. País
- C. ID del padre
- D. FCM token

**54. ¿Cuándo se ejecuta init del ViewModel?**

- A. En cada escritura
- B. En cada lectura
- C. Al crearse
- D. En cada clic

**55. ¿Qué mecanismo de Compose usa para efectos de entrada?**

- A. add
- B. whereEqualTo
- C. @Inject
- D. LaunchedEffect

**56. ¿Qué recomienda para campos null en la UI?**

- A. Valores por defecto
- B. Ignorar todos
- C. Cambiar a Int
- D. Duplicar DTO

**57. ¿Qué busca el profesor con las interfaces?**

- A. Más JOIN
- B. Desacoplamiento
- C. Ocultar UI
- D. Mayor duplicación Completar código

**58. Completa la instancia.**

```kotlin
FirebaseFirestore._____()
```

- A. open
- B. instance
- C. create
- D. getInstance

**59. Completa la anotación DI.**

```kotlin
@_____
fun provideFirestore(): FirebaseFirestore
```

- A. Preview
- B. Composable
- C. Path
- D. Provides

**60. Completa la colección.**

```kotlin
db._____("users").document(uid)
```

- A. query
- B. collect
- C. table
- D. collection

**61. Escribe con ID definido.**

```kotlin
userRef._____(dto).await()
```

- A. set
- B. get
- C. add
- D. collect

**62. Crea con ID automático.**

```kotlin
db.collection("tweets")._____(dto).await()
```

- A. add
- B. update
- C. get
- D. set

**63. Espera la Task.**

```kotlin
ref.get()._____()
```

- A. collect
- B. wait
- C. join
- D. await

**64. Deserializa.**

```kotlin
snap._____(TweetDto::class.java)
```

- A. toJson
- B. toState
- C. toObject
- D. toFlow

**65. Filtra por autor.**

```kotlin
db.collection("tweets")._____("userId", uid)
```

- A. whereLike
- B. equalsTo
- C. filterSQL
- D. whereEqualTo

**66. Recupera ID.**

```text
tweet.copy(id = doc._____)
```

- A. id
- B. key
- C. name
- D. uid

**67. Elimina documento.**

```kotlin
ref._____().await()
```

- A. drop
- B. destroy
- C. delete
- D. clear

**68. Crea retorno exitoso.**

```kotlin
Result._____(Unit)
```

- A. complete
- B. valid
- C. ok
- D. success

**69. Captura identidad.**

```text
auth.currentUser?._____
```

- A. email
- B. uid
- C. token
- D. idToken

**70. Marca una función suspendible.**

```kotlin
_____ fun getTweet() { /* ... */ }
```

- A. flow
- B. launch
- C. suspend
- D. async

**71. Resuelve un valor nulo.**

```kotlin
val location = dto.country _____ "Sin ubicacion"
```

- A. ?.
- B. ==
- C. !!
- D. ?:

**72. Ejecuta efecto Compose.**

```text
_____(Unit) { viewModel.getAllTweets() }
```

- A. init
- B. remember
- C. Provides
- D. LaunchedEffect

### Simulacro 2. Clase 11: Firestore avanzado

**1. ¿Cómo se representa muchos a muchos en SQL?**

- A. DTO
- B. ViewModel
- C. NavHost
- D. Tabla intermedia

**2. ¿Qué usa Firestore en el ejemplo de likes?**

- A. Subcolecciones
- B. JOIN
- C. ORM
- D. Tabla SQL

**3. ¿Por qué no guardar todos los likes en un Array?**

- A. Necesita Hilt
- B. Tamaño y lecturas
- C. No admite String
- D. No admite listas

**4. ¿Cuál es el tamaño máximo aproximado de un documento?**

- A. 1 MiB
- B. 1 GiB
- C. 1 KiB
- D. Sin límite

**5. ¿Qué identifica un documento de like?**

- A. Token FCM
- B. Email
- C. Tweet ID aleatorio
- D. User ID

**6. ¿Qué campo conserva el total de likes?**

- A. likesCount
- B. followersCount
- C. likeToken
- D. likeId

**7. ¿Qué técnica agrupa cambios consistentes?**

- A. State hoisting
- B. Navegación
- C. Desnormalización
- D. Transacción

**8. ¿Qué propiedad evita escrituras parciales?**

- A. Inferencia
- B. Inyección
- C. Atomicidad
- D. Recomposición

**9. ¿Qué método inicia una transacción?**

- A. collect
- B. runBlocking
- C. runTransaction
- D. callbackFlow

**10. ¿Qué método verifica la existencia del like?**

- A. exists
- B. hasLiked
- C. isLiked
- D. containsUser

**11. ¿Qué método lee dentro de una transacción?**

- A. transaction.collect
- B. transaction.map
- C. transaction.readAll
- D. transaction.get

**12. ¿Qué función incrementa un campo numérico?**

- A. FieldValue.sum
- B. FieldValue.add
- C. FieldValue.increment
- D. FieldValue.plus

**13. ¿Qué función asigna fecha de servidor?**

- A. remember
- B. localTimestamp
- C. serverTimestamp
- D. nowLocal

**14. Si ya existe el like, ¿qué se hace?**

- A. Crear y aumentar
- B. Eliminar y aumentar
- C. Eliminar y decrementar
- D. Crear y decrementar

**15. ¿Cuántas escrituras necesita el toggle de like?**

- A. Una
- B. Dos
- C. Tres
- D. Cuatro

**16. ¿Cuántas escrituras requiere follow?**

- A. Cuatro
- B. Dos
- C. Una
- D. Seis

**17. Si A sigue a B, ¿qué subcolección de A se modifica?**

- A. followers
- B. following
- C. likes
- D. tweets

**18. Si A sigue a B, ¿qué subcolección de B se modifica?**

- A. likes
- B. tweets
- C. following
- D. followers

**19. ¿Qué contador aumenta en A?**

- A. followersCount
- B. followingCount
- C. likesCount
- D. tweetCount

**20. ¿Qué contador aumenta en B?**

- A. followingCount
- B. tweetCount
- C. likesCount
- D. followersCount

**21. ¿Por qué guardar también el contador en el padre?**

- A. Duplicar usuarios
- B. Lectura rápida
- C. Evitar String
- D. Generar token

**22. ¿Qué devuelve una consulta tradicional de tweets?**

- A. StateFlow<Boolean>
- B. Job
- C. Flow<List<TweetDto>>
- D. List<TweetDto>

**23. ¿Qué devuelve listenAllTweets?**

- A. Int
- B. List<TweetDto>
- C. Flow<List<TweetDto>>
- D. Deferred<Unit>

**24. ¿Qué método adapta callbacks a Flow?**

- A. remember
- B. callbackFlow
- C. MutableLiveData
- D. runBlocking

**25. ¿Qué método observa cambios Firestore?**

- A. toObject
- B. set
- C. get
- D. addSnapshotListener

**26. ¿Qué recibe el callback?**

- A. uid, email
- B. token, id
- C. snapshot, error
- D. scope, job

**27. ¿Qué función emite los datos?**

- A. collect
- B. awaitClose
- C. trySend
- D. set

**28. ¿Qué función gestiona limpieza al cerrar?**

- A. await
- B. awaitClose
- C. delete
- D. commit

**29. ¿Qué función elimina el listener?**

- A. destroy
- B. remove
- C. drop
- D. delete

**30. ¿Qué método cierra el flujo ante un error?**

- A. await(error)
- B. map(error)
- C. close(error)
- D. trySend(error)

**31. ¿Qué operador maneja errores del flujo?**

- A. map
- B. catch
- C. collect
- D. remember

**32. ¿Qué operador consume emisiones?**

- A. get
- B. collect
- C. send
- D. collection

**33. ¿Qué ámbito usa el ViewModel para recolectar?**

- A. GlobalScope
- B. ComposeScope
- C. viewModelScope
- D. FirebaseScope

**34. ¿Un listener del padre observa directamente todas sus subcolecciones?**

- A. Solo con SQL
- B. Siempre
- C. No
- D. Solo con Hilt

**35. ¿Qué cambio sí puede activar el listener del tweet padre?**

- A. Solo un like hijo
- B. Token local
- C. Cambio de tema
- D. likesCount

**36. ¿Qué desventaja de datos en vivo recalca el profesor?**

- A. No hay IDs
- B. Costos
- C. Falta de UI
- D. Falta de Kotlin

**37. ¿Dónde corre Cloud Functions?**

- A. ViewModel
- B. AndroidManifest
- C. Composable
- D. Nube

**38. ¿Qué activa onDocumentUpdated?**

- A. Inicio de sesión
- B. Creación
- C. Lectura
- D. Modificación

**39. ¿Qué activa onDocumentCreated?**

- A. Navegación
- B. Creación
- C. Lectura
- D. Modificación

**40. ¿Qué obtiene parámetros de ruta?**

- A. event.route
- B. event.user
- C. event.body
- D. event.params

**41. ¿Qué representa el snapshot anterior?**

- A. event.data.before
- B. event.params
- C. event.current
- D. event.data.after

**42. ¿Qué representa el snapshot posterior?**

- A. event.data.after
- B. event.params
- C. event.old
- D. event.data.before

**43. ¿Por qué comparar before y after?**

- A. Pedir permisos
- B. Generar IDs
- C. Evitar escrituras innecesarias
- D. Crear Activity

**44. ¿Qué crea un lote de escrituras?**

- A. db.createFlow
- B. db.batch
- C. db.collect
- D. db.map

**45. ¿Qué confirma el lote?**

- A. awaitClose
- B. send
- C. commit
- D. executeAll

**46. ¿Qué librería da acceso administrativo a Firebase?**

- A. Coil
- B. Retrofit
- C. firebase-admin
- D. firebase-ui

**47. ¿De dónde se importan triggers v2?**

- A. kotlinx.coroutines
- B. android.util.Log
- C. firebase-functions/v2/firestore
- D. firebase-admin/logger

**48. ¿De dónde se importa logger?**

- A. firebase-functions/logger
- B. Hilt
- C. firebase-admin/firestore
- D. androidx.compose

**49. ¿Qué comando inicializa configuración local?**

- A. gradle sync
- B. firebase deploy
- C. npm run dev
- D. firebase init

**50. ¿Qué comando publica funciones?**

- A. npm init
- B. firebase deploy
- C. firebase init
- D. node index.js

**51. ¿En qué archivo escribe la función?**

- A. MainActivity.kt
- B. AndroidManifest.xml
- C. functions/index.js
- D. HomeState.kt

**52. ¿Qué ruta representa la foto anidada en tweet?**

- A. profileImage.user
- B. user.profileImage
- C. user/profileImage
- D. userProfileImage

**53. ¿Qué significa FCM?**

- A. Flow Channel Method
- B. Firebase Cloud Messaging
- C. Firestore Cloud Map
- D. Firebase Code Manager

**54. ¿Qué identifica el token FCM?**

- A. Instalación de app
- B. Contraseña
- C. Colección
- D. Tweet

**55. ¿Qué clase hereda el servicio receptor?**

- A. FirebaseFirestore
- B. FirebaseAuth
- C. ViewModel
- D. FirebaseMessagingService

**56. ¿En qué archivo registra el servicio Android?**

- A. index.js
- B. AndroidManifest.xml
- C. DTO.kt
- D. settings.gradle

**57. ¿Qué API introduce el permiso de notificaciones del ejemplo?**

- A. 21
- B. 29
- C. 33
- D. 36

**58. ¿Qué permiso se solicita?**

- A. READ_MESSAGES
- B. POST_NOTIFICATIONS
- C. FIREBASE_ACCESS
- D. SEND_PUSH

**59. ¿Qué constante representa API 33?**

- A. LOLLIPOP
- B. PIE
- C. TIRAMISU
- D. OREO

**60. ¿Por qué no llegó la primera notificación?**

- A. Permiso no concedido
- B. Falta DTO
- C. Falta Coil
- D. Falta JOIN

**61. ¿Qué usa para direccionar un mensaje particular?**

- A. Email
- B. UID solamente
- C. Nombre
- D. Token FCM

**62. ¿A quién se notifica en users/{userId}/followers/{followerId}?**

- A. followerId
- B. Administrador
- C. userId
- D. Todos

**63. ¿De quién obtiene el username para el texto del mensaje?**

- A. Admin
- B. userId siempre
- C. followerId
- D. Tweet ID

**64. ¿Qué hace Cloud Functions ante token ausente en el ejemplo?**

- A. Registra aviso
- B. Inventa token
- C. Borra usuario
- D. Crea tweet

**65. ¿Por qué aplicar seguridad también en backend?**

- A. FCM obligatorio
- B. Frontend eludible
- C. Kotlin no tiene tipos
- D. SQL sin tablas

**66. ¿Qué condición exige autenticación?**

- A. request.uid == 0
- B. resource.auth == null
- C. request.auth == null
- D. request.auth != null

**67. ¿Qué comodín es recursivo?**

- A. {tweetId}
- B. {document=*}
- C. {userId}
- D. {document=**}

**68. ¿Qué comodín corresponde a un segmento?**

- A. request.auth
- B. resource.data
- C. {tweetId}
- D. {document=**}

**69. ¿Qué obtiene el UID autenticado?**

- A. resource.uid
- B. request.uid
- C. event.uid
- D. request.auth.uid

**70. ¿Qué representa datos propuestos de escritura?**

- A. request.resource.data
- B. request.auth
- C. resource.data
- D. event.params

**71. ¿Qué representa datos actuales del documento?**

- A. request.resource.data
- B. request.auth
- C. resource.data
- D. event.token

**72. ¿Qué función comprueba longitud del contenido?**

- A. len()
- B. lengthOf()
- C. countAll()
- D. size()

**73. ¿Qué comprobación exige entero?**

- A. is Integer
- B. typeof Integer
- C. is Int
- D. is int

**74. ¿Qué condición impide likes negativos?**

- A. != 0
- B. <= 0
- C. == -1
- D. >= 0

**75. ¿Una regla general permisiva puede autorizar aunque otra sea restrictiva?**

- A. Solo SQL
- B. Solo FCM
- C. No
- D. Sí

**76. ¿Qué diferencia batch de transacción?**

- A. No atomicidad
- B. Solo Android
- C. Solo lectura
- D. Escrituras sin lectura dependiente Completar código

**77. Inicia la transacción.**

```text
db._____ { tx -> /* ... */ }
```

- A. launch
- B. callbackFlow
- C. runBlocking
- D. runTransaction

**78. Lee dentro de la transacción.**

```kotlin
val doc = tx._____(likeRef)
```

- A. readAll
- B. collect
- C. get
- D. query

**79. Verifica existencia.**

```kotlin
if (doc._____()) { /* ... */ }
```

- A. exists
- B. contains
- C. valid
- D. isPresent

**80. Crea documento de like.**

```kotlin
tx._____(likeRef, data)
```

- A. set
- B. insert
- C. add
- D. push

**81. Decrementa contador.**

```kotlin
FieldValue._____(-1)
```

- A. decrease
- B. minus
- C. increment
- D. decrementBy

**82. Asigna fecha de servidor.**

```kotlin
FieldValue._____()
```

- A. serverTimestamp
- B. now
- C. currentDate
- D. getTime

**83. Subcolección de usuarios que sigue A.**

```text
current.collection("_____").document(targetId)
```

- A. users
- B. followers
- C. likes
- D. following

**84. Crea Flow desde callbacks.**

```kotlin
fun listen(): Flow<List<TweetDto>> = _____ { /* ... */ }
```

- A. launch
- B. callbackFlow
- C. async
- D. remember

**85. Escucha Firestore.**

```text
db.collection("tweets")._____ { snapshot, error -> }
```

- A. getAll
- B. collect
- C. observeState
- D. addSnapshotListener

**86. Emite elementos.**

```text
_____(tweets)
```

- A. collect
- B. get
- C. trySend
- D. set

**87. Cierra el Flow con error.**

```text
_____(error)
```

- A. cancelAll
- B. send
- C. await
- D. close

**88. Limpia al cerrar.**

```text
_____ { listener.remove() }
```

- A. onStop
- B. finally
- C. awaitClose
- D. await

**89. Libera el listener.**

```kotlin
listener._____()
```

- A. delete
- B. remove
- C. destroy
- D. stop

**90. Maneja error del Flow.**

```text
repo.listen()._____ { error -> }
```

- A. except
- B. await
- C. remember
- D. catch

**91. Consume el Flow.**

```text
repo.listen()._____ { tweets -> }
```

- A. fetch
- B. observeOnce
- C. get
- D. collect

**92. Lanza desde ViewModel.**

```text
_____.launch { /* ... */ }
```

- A. FirebaseScope
- B. GlobalScope
- C. viewModelScope
- D. AppScope

**93. Trigger de edición.**

```text
_____("users/{userId}", async (event) => {})
```

- A. onDocumentUpdated
- B. onDocumentDeleted
- C. onDocumentCreated
- D. onDocumentRead

**94. Trigger de nuevo follower.**

```text
_____("users/{userId}/followers/{followerId}", async (event) => {})
```

- A. onDocumentCreated
- B. onLogin
- C. onDocumentRead
- D. onDocumentUpdated

**95. Parámetros de ruta.**

```javascript
const userId = event._____.userId;
```

- A. data
- B. route
- C. params
- D. body

**96. Datos anteriores.**

```javascript
const old = event.data._____.data();
```

- A. after
- B. before
- C. params
- D. previous

**97. Datos nuevos.**

```javascript
const next = event.data._____.data();
```

- A. current
- B. params
- C. after
- D. before

**98. Crea batch.**

```javascript
const batch = db._____();
```

- A. group
- B. transaction
- C. batch
- D. collect

**99. Confirma batch.**

```javascript
await batch._____();
```

- A. send
- B. executeAll
- C. commit
- D. saveAll

**100. Envía mensaje FCM con Admin SDK.**

```kotlin
await admin.messaging()._____(message);
```

- A. update
- B. write
- C. commit
- D. send

**101. Obtén token FCM.**

```kotlin
FirebaseMessaging.getInstance()._____.await()
```

- A. uid
- B. email
- C. userId
- D. token

**102. Clase base de mensajería.**

```kotlin
class MyService : _____()
```

- A. FirebaseMessagingService
- B. FirebaseAuth
- C. ViewModel
- D. FirebaseFirestore

**103. Permiso de notificaciones.**

```kotlin
Manifest.permission._____
```

- A. FIREBASE_ACCESS
- B. SEND_PUSH
- C. READ_MESSAGES
- D. POST_NOTIFICATIONS

**104. Constante de Android 13.**

```text
Build.VERSION_CODES._____
```

- A. PIE
- B. LOLLIPOP
- C. OREO
- D. TIRAMISU

**105. Verifica permiso.**

```text
PackageManager._____
```

- A. ACCESS_ALLOWED
- B. GRANTED
- C. PERMISSION_GRANTED
- D. PERMISSION_ACCEPTED

**106. Exige autenticación.**

```javascript
allow read: if request.auth _____ null;
```

- A. ==
- B. =
- C. !=
- D. <=

**107. Verifica propiedad del tweet.**

```text
request.resource.data.userId == request.auth._____
```

- A. uid
- B. id
- C. email
- D. token

**108. Comprueba entero.**

```text
request.resource.data.likesCount is _____
```

- A. Int
- B. Integer
- C. int
- D. number

### Simulacro 3. Repaso transversal

**1. ¿Kotlin tiene qué tipo de tipado?**

- A. Dinámico
- B. Estático con inferencia
- C. Solo explícito
- D. Sin tipos

**2. ¿Qué palabra declara referencia no reasignable?**

- A. fun
- B. val
- C. var
- D. lateinit

**3. ¿Qué palabra declara variable reasignable?**

- A. fun
- B. var
- C. val
- D. const

**4. ¿Qué indica String sin signo de interrogación?**

- A. Entero
- B. Tipo nullable
- C. Tipo no nullable
- D. Lista

**5. ¿Qué indica el signo ? al final de String?**

- A. Inmutable
- B. Abstracto
- C. Suspendible
- D. Nullable

**6. ¿Qué palabra declara una función Kotlin?**

- A. fun
- B. void
- C. def
- D. function

**7. ¿Qué tipo equivale a retorno sin valor útil?**

- A. Unit
- B. Nothing
- C. Null
- D. Voidable

**8. ¿Qué clase genera copy automáticamente?**

- A. object
- B. Activity
- C. data class
- D. interface

**9. ¿Qué anotación declara UI Compose?**

- A. @Composable
- B. @Module
- C. @Inject
- D. @Preview solamente

**10. ¿Qué función conserva estado entre recomposiciones?**

- A. launch
- B. remember
- C. mutableStateOf
- D. collect

**11. ¿Qué función crea estado observable de Compose?**

- A. copy
- B. mutableStateOf
- C. launch
- D. remember por sí solo

**12. ¿Qué patrón eleva estado al padre?**

- A. Dependency injection
- B. State hoisting
- C. Recursividad
- D. Desnormalización

**13. ¿Qué contenedor organiza verticalmente?**

- A. Box
- B. Row
- C. Scaffold obligatorio
- D. Column

**14. ¿Qué contenedor organiza horizontalmente?**

- A. Row
- B. NavHost
- C. Box
- D. Column

**15. ¿Qué contenedor facilita superposición?**

- A. Column
- B. ViewModel
- C. Box
- D. Row

**16. ¿Qué componente vincula rutas con composables?**

- A. NavHost
- B. NavController
- C. Manifest
- D. Repository

**17. ¿Qué función crea controlador de navegación?**

- A. rememberNavController
- B. rememberState
- C. navigate
- D. NavHost

**18. ¿Qué se recomienda pasar a hijos para navegación?**

- A. NavController a todos
- B. Context global
- C. Firestore
- D. Callbacks

**19. ¿Qué significa MVVM?**

- A. Model View Visual Module
- B. Model Variable View Module
- C. Model View ViewModel
- D. Main View Model Value

**20. ¿Qué debe ser privado en patrón de StateFlow?**

- A. MutableStateFlow
- B. StateFlow público
- C. Screen
- D. DTO

**21. ¿Qué interfaz de solo lectura expone el estado?**

- A. Job
- B. Array
- C. StateFlow
- D. MutableStateFlow

**22. ¿Qué función adapta StateFlow a Compose?**

- A. observeAsState para LiveData
- B. await
- C. remember sin Flow
- D. collectAsState

**23. ¿Qué función adapta LiveData a Compose?**

- A. observeAsState
- B. trySend
- C. collectAsState exclusivamente
- D. set

**24. ¿Qué retorna launch?**

- A. Deferred
- B. Job
- C. String
- D. Flow

**25. ¿Qué retorna async?**

- A. State
- B. Deferred
- C. Job
- D. Unit siempre

**26. ¿Qué método obtiene el resultado de Deferred?**

- A. getInstance
- B. await
- C. remember
- D. collect

**27. ¿Qué keyword permite suspensión?**

- A. suspend
- B. static
- C. sync
- D. blocking

**28. ¿Qué función simula espera sin bloquear el hilo?**

- A. joinThread
- B. delay
- C. Thread.sleep
- D. println

**29. ¿Qué ámbito se usa en un ViewModel?**

- A. viewModelScope
- B. MainActivityScope
- C. GlobalScope
- D. FirebaseScope

**30. ¿Qué biblioteca usa el profesor para DI?**

- A. Coil
- B. Sequelize
- C. Hilt
- D. Retrofit

**31. ¿Qué biblioteca carga imágenes por URL?**

- A. Retrofit
- B. Hilt
- C. Morgan
- D. Coil

**32. ¿Qué componente carga una imagen remota con Coil?**

- A. NavHost
- B. AsyncImage
- C. LiveData
- D. ImageButton

**33. ¿Qué servicio almacena archivos?**

- A. Authentication
- B. Firestore
- C. Hilt
- D. Firebase Storage

**34. ¿Qué recibe el selector de imágenes de Android?**

- A. SQL
- B. Tweet ID
- C. URI
- D. FCM token

**35. ¿Qué biblioteca consume API REST Android?**

- A. Room
- B. Coil
- C. Hilt
- D. Retrofit

**36. ¿Qué conversor se usa para JSON?**

- A. HiltFactory
- B. ScalarsConverterFactory
- C. CoilFactory
- D. GsonConverterFactory

**37. ¿Qué conversor se usa para texto simple?**

- A. GsonConverterFactory
- B. NavFactory
- C. ScalarsConverterFactory
- D. FirestoreConverter

**38. ¿Qué tipo de petición obtiene datos?**

- A. GET
- B. POST
- C. DELETE
- D. PUT

**39. ¿Qué tipo de petición crea datos?**

- A. POST
- B. DELETE
- C. GET
- D. PUT

**40. ¿Qué tipo de petición actualiza datos en el ejemplo?**

- A. POST
- B. DELETE
- C. GET
- D. PUT

**41. ¿Qué tipo de petición elimina datos?**

- A. POST
- B. DELETE
- C. PUT
- D. GET

**42. ¿Qué tecnología ejecuta JavaScript en servidor?**

- A. Retrofit
- B. Hilt
- C. Node.js
- D. DBeaver

**43. ¿Qué ORM usa el backend SQL?**

- A. nodemon
- B. Morgan
- C. Postman
- D. Sequelize

**44. ¿Qué herramienta prueba peticiones HTTP?**

- A. Postman
- B. DBeaver
- C. Hilt
- D. Coil

**45. ¿Qué dirección usa el emulador para llegar al host?**

- A. 10.0.2.2
- B. 0.0.0.0
- C. localhost
- D. 127.0.0.1

**46. ¿Qué archivo declara permisos Android?**

- A. AndroidManifest.xml
- B. TweetDto.kt
- C. build.gradle
- D. index.js

**47. ¿Qué herramienta gestiona dependencias y build Android?**

- A. DBeaver
- B. Gradle
- C. Firebase CLI
- D. Postman

**48. ¿Qué recomienda hacer tras sincronizar Gradle?**

- A. Solo revisar Preview
- B. Eliminar emulador
- C. Ejecutar la app
- D. Borrar Manifest

**49. ¿Qué recomienda hacer al terminar con el emulador?**

- A. Crear otro
- B. Detenerlo
- C. Dejarlo siempre abierto
- D. Cambiar RAM siempre

**50. ¿Qué patrón recomienda para construir Retrofit?**

- A. Builder
- B. Observer
- C. Singleton únicamente
- D. Recursividad Completar código

**51. Expone un estado mutable privado.**

```kotlin
private val _state = _____(HomeState())
```

- A. MutableStateFlow
- B. StateFlow
- C. LiveData
- D. Flow

**52. Expone interfaz de lectura.**

```kotlin
val state: _____<HomeState> = _state
```

- A. StateFlow
- B. Deferred
- C. MutableStateFlow
- D. Job

**53. Observa estado Compose.**

```kotlin
val state by vm.state._____()
```

- A. collectAsState
- B. collect
- C. await
- D. observe

**54. Conserva estado local.**

```kotlin
var count by _____ { mutableStateOf(0) }
```

- A. async
- B. copy
- C. remember
- D. launch

**55. Crea estado observable.**

```kotlin
var count by remember { _____(0) }
```

- A. copy
- B. runBlocking
- C. mutableStateOf
- D. StateFlow

**56. Crea controlador.**

```kotlin
val navController = _____()
```

- A. rememberRoute
- B. navigate
- C. rememberNavController
- D. NavHost

**57. Navega a destino.**

```kotlin
navController._____("home")
```

- A. collect
- B. launch
- C. start
- D. navigate

**58. Obtiene Deferred.**

```kotlin
val result = _____ { fetchUser() }
```

- A. remember
- B. await
- C. launch
- D. async

**59. Carga imagen remota.**

```text
_____(model = imageUrl, contentDescription = null)
```

- A. AsyncImage
- B. TextField
- C. NavHost
- D. Column

**60. Define URL base Retrofit.**

```kotlin
Retrofit.Builder()._____("http://10.0.2.2:3000/")
```

- A. url
- B. baseUrl
- C. path
- D. host

## Solucionarios de los simulacros

### Simulacro 1. Clase 10: Firestore y arquitectura (72 preguntas)

| N.º | Opción | Respuesta |
| ---: | :---: | --- |
| 1 | D | Documental NoSQL |
| 2 | B | Not Only SQL |
| 3 | A | Esquema flexible |
| 4 | B | Backend administrado |
| 5 | B | Realtime Database |
| 6 | B | Colección |
| 7 | D | Documento |
| 8 | B | String |
| 9 | D | No necesariamente |
| 10 | B | Map |
| 11 | D | Array |
| 12 | A | GeoPoint |
| 13 | A | No, tiene tipos |
| 14 | B | JOIN |
| 15 | B | Authentication |
| 16 | C | Firestore |
| 17 | B | Contraseña |
| 18 | C | UID |
| 19 | D | Modo de prueba |
| 20 | C | Retrofit |
| 21 | A | Hilt |
| 22 | C | @Provides |
| 23 | C | @Singleton |
| 24 | A | Data Transfer Object |
| 25 | B | Consultas concretas |
| 26 | C | Definir contrato |
| 27 | C | Repository |
| 28 | D | ViewModel |
| 29 | B | Herencia abstracta |
| 30 | B | Implementación seleccionada |
| 31 | A | Result.success(Unit) |
| 32 | D | Deserialización |
| 33 | A | DTO/mapeo |
| 34 | A | Incompatibilidad de tipo |
| 35 | C | Getter |
| 36 | B | collection |
| 37 | C | document |
| 38 | C | add |
| 39 | A | set |
| 40 | A | get |
| 41 | A | whereEqualTo |
| 42 | C | toObject |
| 43 | A | await |
| 44 | C | doc.id |
| 45 | D | copy |
| 46 | D | Inconsistencia |
| 47 | B | Necesarios para UI |
| 48 | A | Poco cambiantes |
| 49 | A | Nombre, username, foto |
| 50 | A | No se corrigen solos |
| 51 | D | Últimos 500 tweets |
| 52 | B | Recursividad |
| 53 | C | ID del padre |
| 54 | C | Al crearse |
| 55 | D | LaunchedEffect |
| 56 | A | Valores por defecto |
| 57 | B | Desacoplamiento |
| 58 | D | getInstance |
| 59 | D | Provides |
| 60 | D | collection |
| 61 | A | set |
| 62 | A | add |
| 63 | D | await |
| 64 | C | toObject |
| 65 | D | whereEqualTo |
| 66 | A | id |
| 67 | C | delete |
| 68 | D | success |
| 69 | B | uid |
| 70 | C | suspend |
| 71 | D | ?: |
| 72 | D | LaunchedEffect |

### Simulacro 2. Clase 11: Firestore avanzado (108 preguntas)

| N.º | Opción | Respuesta |
| ---: | :---: | --- |
| 1 | D | Tabla intermedia |
| 2 | A | Subcolecciones |
| 3 | B | Tamaño y lecturas |
| 4 | A | 1 MiB |
| 5 | D | User ID |
| 6 | A | likesCount |
| 7 | D | Transacción |
| 8 | C | Atomicidad |
| 9 | C | runTransaction |
| 10 | A | exists |
| 11 | D | transaction.get |
| 12 | C | FieldValue.increment |
| 13 | C | serverTimestamp |
| 14 | C | Eliminar y decrementar |
| 15 | B | Dos |
| 16 | A | Cuatro |
| 17 | B | following |
| 18 | D | followers |
| 19 | B | followingCount |
| 20 | D | followersCount |
| 21 | B | Lectura rápida |
| 22 | D | List&lt;TweetDto&gt; |
| 23 | C | Flow&lt;List&lt;TweetDto&gt;&gt; |
| 24 | B | callbackFlow |
| 25 | D | addSnapshotListener |
| 26 | C | snapshot, error |
| 27 | C | trySend |
| 28 | B | awaitClose |
| 29 | B | remove |
| 30 | C | close(error) |
| 31 | B | catch |
| 32 | B | collect |
| 33 | C | viewModelScope |
| 34 | C | No |
| 35 | D | likesCount |
| 36 | B | Costos |
| 37 | D | Nube |
| 38 | D | Modificación |
| 39 | B | Creación |
| 40 | D | event.params |
| 41 | A | event.data.before |
| 42 | A | event.data.after |
| 43 | C | Evitar escrituras innecesarias |
| 44 | B | db.batch |
| 45 | C | commit |
| 46 | C | firebase-admin |
| 47 | C | firebase-functions/v2/firestore |
| 48 | A | firebase-functions/logger |
| 49 | D | firebase init |
| 50 | B | firebase deploy |
| 51 | C | functions/index.js |
| 52 | B | user.profileImage |
| 53 | B | Firebase Cloud Messaging |
| 54 | A | Instalación de app |
| 55 | D | FirebaseMessagingService |
| 56 | B | AndroidManifest.xml |
| 57 | C | 33 |
| 58 | B | POST_NOTIFICATIONS |
| 59 | C | TIRAMISU |
| 60 | A | Permiso no concedido |
| 61 | D | Token FCM |
| 62 | C | userId |
| 63 | C | followerId |
| 64 | A | Registra aviso |
| 65 | B | Frontend eludible |
| 66 | D | request.auth != null |
| 67 | D | {document=**} |
| 68 | C | {tweetId} |
| 69 | D | request.auth.uid |
| 70 | A | request.resource.data |
| 71 | C | resource.data |
| 72 | D | size() |
| 73 | D | is int |
| 74 | D | &gt;= 0 |
| 75 | D | Sí |
| 76 | D | Escrituras sin lectura dependiente |
| 77 | D | runTransaction |
| 78 | C | get |
| 79 | A | exists |
| 80 | A | set |
| 81 | C | increment |
| 82 | A | serverTimestamp |
| 83 | D | following |
| 84 | B | callbackFlow |
| 85 | D | addSnapshotListener |
| 86 | C | trySend |
| 87 | D | close |
| 88 | C | awaitClose |
| 89 | B | remove |
| 90 | D | catch |
| 91 | D | collect |
| 92 | C | viewModelScope |
| 93 | A | onDocumentUpdated |
| 94 | A | onDocumentCreated |
| 95 | C | params |
| 96 | B | before |
| 97 | C | after |
| 98 | C | batch |
| 99 | C | commit |
| 100 | D | send |
| 101 | D | token |
| 102 | A | FirebaseMessagingService |
| 103 | D | POST_NOTIFICATIONS |
| 104 | D | TIRAMISU |
| 105 | C | PERMISSION_GRANTED |
| 106 | C | != |
| 107 | A | uid |
| 108 | C | int |

### Simulacro 3. Repaso transversal (60 preguntas)

| N.º | Opción | Respuesta |
| ---: | :---: | --- |
| 1 | B | Estático con inferencia |
| 2 | B | val |
| 3 | B | var |
| 4 | C | Tipo no nullable |
| 5 | D | Nullable |
| 6 | A | fun |
| 7 | A | Unit |
| 8 | C | data class |
| 9 | A | @Composable |
| 10 | B | remember |
| 11 | B | mutableStateOf |
| 12 | B | State hoisting |
| 13 | D | Column |
| 14 | A | Row |
| 15 | C | Box |
| 16 | A | NavHost |
| 17 | A | rememberNavController |
| 18 | D | Callbacks |
| 19 | C | Model View ViewModel |
| 20 | A | MutableStateFlow |
| 21 | C | StateFlow |
| 22 | D | collectAsState |
| 23 | A | observeAsState |
| 24 | B | Job |
| 25 | B | Deferred |
| 26 | B | await |
| 27 | A | suspend |
| 28 | B | delay |
| 29 | A | viewModelScope |
| 30 | C | Hilt |
| 31 | D | Coil |
| 32 | B | AsyncImage |
| 33 | D | Firebase Storage |
| 34 | C | URI |
| 35 | D | Retrofit |
| 36 | D | GsonConverterFactory |
| 37 | C | ScalarsConverterFactory |
| 38 | A | GET |
| 39 | A | POST |
| 40 | D | PUT |
| 41 | B | DELETE |
| 42 | C | Node.js |
| 43 | D | Sequelize |
| 44 | A | Postman |
| 45 | A | 10.0.2.2 |
| 46 | A | AndroidManifest.xml |
| 47 | B | Gradle |
| 48 | C | Ejecutar la app |
| 49 | B | Detenerlo |
| 50 | A | Builder |
| 51 | A | MutableStateFlow |
| 52 | A | StateFlow |
| 53 | A | collectAsState |
| 54 | C | remember |
| 55 | C | mutableStateOf |
| 56 | C | rememberNavController |
| 57 | D | navigate |
| 58 | D | async |
| 59 | A | AsyncImage |
| 60 | B | baseUrl |

## Comparaciones rápidas

| Concepto | Descripción |
| --- | --- |
| NoSQL / SQL | Esquema flexible documental frente a relacional normalizado en el ejemplo. |
| Collection / Document | Agrupación de documentos frente a registro individual. |
| Document ID / campo ID | Metadato de identificación frente a valor guardado dentro del documento. |
| Authentication / Firestore / Storage | Identidad / documentos / archivos. |
| get / addSnapshotListener | get puntual / actualizaciones continuas. |
| set / update / add | Escribir referencia / modificar campos / generar ID. |
| runTransaction / batch | Lectura dependiente y atomicidad / escrituras agrupadas. |
| Flow / StateFlow / MutableStateFlow | Secuencia / estado de lectura / estado modificable. |
| await / awaitClose | Esperar resultado / limpiar escucha. |
| trySend / collect | Emitir / consumir. |
| UID / FCM token | Usuario / instalación. |
| Created / Updated | Crear / modificar documento. |
| request.resource / resource | Propuesto / actual. |
| remember / mutableStateOf | Conservar / observar. |
| val / var | No reasignable / reasignable. |
| launch / async | Job / Deferred. |
| Hilt / Retrofit / Coil | Inyección / HTTP / imágenes. |

---

**Fuentes y alcance:** guía de estudio anterior y transcripciones de las clases 10 y 11 (`11ce.txt`, `12ce.txt`), con repaso de las clases previas. El código es didáctico y no se ha ejecutado en Android. Las coordenadas de dependencias son referencias sin versiones exactas. Los simulacros no son preguntas oficiales.
