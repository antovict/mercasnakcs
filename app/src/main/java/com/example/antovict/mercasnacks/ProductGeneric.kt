package com.example.antovict.mercasnacks

open class ProductGeneric{
    var special_price : Double = 0.00
    var price : Double = 0.00
    var _id : String = ""
    var active:Boolean = false
    var fee:Double=0.00
    var stock:Double = 0.00
    var percentage :Double = 0.00
    var name:String=""
    var visible :Boolean=false
    var image_url : String = ""
    var modified:String=""
    var created : String =""
    var iva : Double=0.00

    constructor(){}

    fun selector(p: ProductGeneric): Double = p.price

    constructor(special_price: Double, price: Double, _id: String, active: Boolean, fee: Double, stock: Double, percentage: Double, name: String, visible: Boolean, image_url: String, modified: String, created: String, iva: Double) {
        this.special_price = special_price
        this.price = price
        this._id = _id
        this.active = active
        this.fee = fee
        this.stock = stock
        this.percentage = percentage
        this.name = name
        this.visible = visible
        this.image_url = image_url
        this.modified = modified
        this.created = created
        this.iva = iva
    }
}