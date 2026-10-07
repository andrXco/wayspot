package com.example.wayspot.ui.screens.newreview.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.PlaceInfo
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewPlaceSummaryCard(
    places: List<PlaceInfo>,
    selectedPlace: PlaceInfo?,
    onPlaceSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selectPlaceText = stringResource(R.string.new_review_select_place)
    val selectedPlaceTitle = selectedPlace?.title.orEmpty()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { shouldExpand ->
            expanded = shouldExpand && places.isNotEmpty()
        },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedPlaceTitle,
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Text(text = selectPlaceText)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
                .semantics {
                    contentDescription = selectPlaceText
                },
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            places.forEach { place ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(
                                text = place.title,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = place.location,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        onPlaceSelected(place.id)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

@WayspotMultiPreview
@Composable
private fun ReviewPlaceSummaryCardPreview() {
    WayspotTheme {
        ReviewPlaceSummaryCard(
            places = listOf(PreviewDataPopular.previewPlaceInfo),
            selectedPlace = PreviewDataPopular.previewPlaceInfo,
            onPlaceSelected = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
