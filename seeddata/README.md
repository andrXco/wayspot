# Carga de datos falsos con Faker (Sprint Móvil)

Este script genera datos falsos para las entidades principales (`users`, `places`, `reviews`) usando `@faker-js/faker` y los sube a **Firebase CLI (emulador)** o a **Firebase Real** usando `firebase-admin` con batches de Firestore.

## Instalación
Desde esta carpeta:
```bash
npm install
```

---

## 1. Subir datos al Firebase CLI (Emulador local)
1. Iniciar los emuladores desde otra terminal:
   ```bash
   firebase emulators:start --only firestore,auth --project wayspot-60fd2
   ```
2. En Windows (PowerShell):
   ```powershell
   $env:FIRESTORE_EMULATOR_HOST="localhost:8080"
   node seed.js
   ```
   En Windows (CMD):
   ```cmd
   set FIRESTORE_EMULATOR_HOST=localhost:8080
   node seed.js
   ```

---

## 2. Subir datos a Firebase Real (Producción)
1. En Firebase Console (`wayspot-60fd2`) -> Configuración del proyecto -> Cuentas de servicio -> Generar nueva clave privada.
2. Guardar el archivo en esta carpeta como `serviceAccountKey.json`.
3. En `seed.js`:
   - Descomentar la línea del `import serviceAccount...`
   - Descomentar la línea `credential: admin.credential.cert(serviceAccount),`
   - Comentar `projectId: "wayspot-60fd2",`
4. En una terminal limpia (sin variable `FIRESTORE_EMULATOR_HOST`):
   ```bash
   node seed.js
   ```
