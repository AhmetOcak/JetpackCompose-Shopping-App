package com.ahmetocak.shoppingapp.presentation.home.product

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CategoryItem(
    categoryName: String,
    selectedCatName: String,
    onCategoryClick: (String) -> Unit
) {
    Button(
        onClick = {
            onCategoryClick(categoryName)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (categoryName == selectedCatName) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            }
        )
    ) {
        Text(text = categoryName)
    }
}