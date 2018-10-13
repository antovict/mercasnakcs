package com.example.antovict.mercasnacks

open class Retailer{
    var id : String = ""
    var name : String = ""
    var logo_url : String = ""
    var cover_image : String = ""
    var products : ArrayList<ProductGeneric> = ArrayList<ProductGeneric>()

    constructor(){}

    constructor(id: String, name: String, logo_url: String, cover_image: String, products: ArrayList<ProductGeneric>) {
        this.id = id
        this.name = name
        this.logo_url = logo_url
        this.cover_image = cover_image
        this.products = products
    }


}

data class JSONObjectProducts(var retailer : Retailer)