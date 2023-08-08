package com.example.watermyplants.feature_plant.presentation.home.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.watermyplants.feature_plant.domain.model.Plant


@Composable
fun PlantItem(
    onPlantClicked: () -> Unit,
    onPlantDeleted: () -> Unit,
    plant: Plant,
    modifier: Modifier = Modifier,
) {

    var expanded by remember{
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .clickable(onClick = onPlantClicked)
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = plant.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxHeight(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onPlantDeleted) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if(expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }
        }
        if(expanded){
            Text(
                text = plant.description,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
        Divider(
            thickness = 1.dp
        )
    }


}