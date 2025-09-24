package com.example.workmatetest.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.workmatetest.domain.model.Recipe

@Composable
fun RecipeCard(recipe: Recipe, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .heightIn(min = 150.dp, max = 250.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = recipe.image,
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .heightIn(min = 100.dp, max = 200.dp)
                    .fillMaxWidth()
            )
            Text(
                text = recipe.title,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}