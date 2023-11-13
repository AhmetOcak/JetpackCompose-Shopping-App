package com.ahmetocak.shoppingapp.data.datasource.local.shopping.product

import com.ahmetocak.shoppingapp.common.Response
import com.ahmetocak.shoppingapp.common.caller.dbCall
import com.ahmetocak.shoppingapp.data.datasource.local.shopping.product.db.ProductDao
import com.ahmetocak.shoppingapp.model.shopping.ProductEntity
import javax.inject.Inject

class ProductLocalDataSourceImpl @Inject constructor(private val productDao: ProductDao) :
    ProductLocalDataSource {

    override suspend fun addProduct(productEntity: ProductEntity): Response<Unit> {
        return dbCall { productDao.addProduct(productEntity) }
    }

    override suspend fun getAllProducts(): Response<List<ProductEntity>> {
        return dbCall { productDao.getAllProducts() }
    }
}