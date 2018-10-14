package com.example.antovict.mercasnacks

import java.util.ArrayList

internal class ProductsCache private constructor() {
    init {
        products = ArrayList()
        productsCart = ArrayList()
    }

    companion object {
        val instance = ProductsCache()
        lateinit var products: ArrayList<ProductGeneric>
        lateinit var productsCart: ArrayList<ProductGeneric>
    }
}