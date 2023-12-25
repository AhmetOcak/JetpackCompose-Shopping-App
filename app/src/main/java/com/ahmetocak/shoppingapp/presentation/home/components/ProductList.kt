package com.ahmetocak.shoppingapp.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ahmetocak.shoppingapp.R
import com.ahmetocak.shoppingapp.designsystem.components.ShoppingProductItem
import com.ahmetocak.shoppingapp.model.shopping.Product

@Composable
fun ProductList(
    modifier: Modifier,
    productList: List<Product>,
    isProductListLoading: Boolean,
    selectedCatName: String,
    onProductClick: (Product) -> Unit,
    allCatText: String
) {
    if (isProductListLoading) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.one_level_margin)),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(dimensionResource(id = R.dimen.one_level_margin)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.one_level_margin))
        ) {
            items(
                if (selectedCatName == allCatText) productList
                else productList.filter { it.category?.uppercase() == selectedCatName.uppercase() },
                key = { it.id }
            ) {
                ShoppingProductItem(
                    modifier = modifier,
                    product = it,
                    onProductClick = onProductClick
                )
            }
        }
    }
}