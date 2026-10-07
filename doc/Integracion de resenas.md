# Integración de reseñas con el backend

La app Android consulta lugares, usuarios, reseñas y comentarios del backend REST mediante Retrofit. Para esta etapa, el usuario de PostgreSQL usado como autor de reseñas y comentarios es el ID `1`, centralizado en `BackendSession`. Firebase Authentication conserva la sesión visual de la app y aún no está vinculada con los usuarios SQL.

## Fotos pendientes

La publicación y edición de reseñas envía por ahora calificación, título, descripción, lugar y usuario. La selección de fotos se oculta en el formulario hasta que el flujo pueda persistirlas. La siguiente etapa debe subir las imágenes a almacenamiento, obtener URLs, ampliar el modelo y el contrato de reseñas del backend para guardarlas, y mostrar esas URLs en Home y en el detalle. El borrador actual conserva `photoUris` y el límite de tres fotos para retomar ese trabajo.
