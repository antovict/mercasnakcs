package com.example.antovict.mercasnacks

data class Url(var version:String, var active : Boolean, var url : String)
data class Urls(var urls : ArrayList<Url>)
data class Unit(var name:String, var abbreviation:String, var _id : Int)
data class Units(var conversion_enable:Boolean, var conversion_rate:Int, var value : Int, var unit : Unit)
data class Category(var name:String, var _id : String)

class Product : ProductGeneric {
    var category : ArrayList<String> = ArrayList<String>()
    var boost : Int = 0
    var slug : String = ""
    var images : ArrayList<Urls> = ArrayList<Urls>()
    lateinit var unit :  Units
    var retailer_sku : Long = 0
    var product_simple_active :Boolean = false
    var location :String= ""
    var product_simple:String = ""
    var categories : ArrayList<Category> = ArrayList<Category>()
    var hierarchicalCategories: ArrayList<String> = ArrayList<String>()
    var ean:Long=0
    var tags:ArrayList<String> = ArrayList<String>()
    var retailer : String = ""
    var new_objectID:String=""
    var brand:String = ""
    var objectID:String=""
    var manually_active:Boolean=false
    var iac : Int =0

    constructor(){}

    constructor(category: ArrayList<String>, boost: Int, slug: String, images: ArrayList<Urls>, unit: Units, retailer_sku: Long, product_simple_active: Boolean, location: String, product_simple: String, categories: ArrayList<Category>, hierarchicalCategories: ArrayList<String>, ean: Long, tags: ArrayList<String>, retailer: String, new_objectID: String, brand: String, objectID: String, manually_active: Boolean, iac: Int) : super() {
        this.category = category
        this.boost = boost
        this.slug = slug
        this.images = images
        this.unit = unit
        this.retailer_sku = retailer_sku
        this.product_simple_active = product_simple_active
        this.location = location
        this.product_simple = product_simple
        this.categories = categories
        this.hierarchicalCategories = hierarchicalCategories
        this.ean = ean
        this.tags = tags
        this.retailer = retailer
        this.new_objectID = new_objectID
        this.brand = brand
        this.objectID = objectID
        this.manually_active = manually_active
        this.iac = iac
    }
}