package com.example.wayspot.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "WaySpot",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Blue
            )
            IconButton(onClick = { /* Lógica de notificaciones */ }) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notificaciones"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Busca un lugar o ciudad...") },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Buscar")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                PostCard(
                    nombre = stringResource(R.string.mar_a_gonz_lez),
                    usuario = stringResource(R.string.maria_g),
                    tiempo = stringResource(R.string.hace_2_horas),
                    categoria = stringResource(R.string.patrimonio),
                    titulo = stringResource(R.string.machu_picchu),
                    ubicacion = stringResource(R.string.cusco_per),
                    descripcion = stringResource(R.string.una_experiencia_que_te_cambia_la_vida_ver_el_amanecer_sobre_las_ruinas_con_la_neblina_entre_las_monta_as_es_algo_que_no_olvidar_s_jam_s_recomiendo_subir_temprano)
                )
            }
            item {
                PostCard(
                    nombre = stringResource(R.string.carlos_ram_rez),
                    usuario = stringResource(R.string.carlos_r),
                    tiempo = stringResource(R.string.hace_5_horas),
                    categoria = stringResource(R.string.arte),
                    titulo = stringResource(R.string.parque_g_ell),
                    ubicacion = stringResource(R.string.barcelona_espa_a),
                    descripcion = stringResource(R.string.el_dise_o_de_gaud_es_simplemente_surrealista_las_vistas_de_la_ciudad_desde_la_terraza_son_espectaculares_compra_entradas_con_anticipaci_n_hay)
                )
            }
        }
    }
}

@Composable
fun PostCard(
    nombre: String,
    usuario: String,
    tiempo: String,
    categoria: String,
    titulo: String,
    ubicacion: String,
    descripcion: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = usuario, color = Color.Gray, fontSize = 12.sp)
                Text(text = tiempo, color = Color.Gray, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = categoria, color = Color.Blue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row {
                    repeat(5) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Estrella",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = titulo, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = ubicacion, color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = descripcion,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}