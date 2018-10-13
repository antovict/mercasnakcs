package com.example.antovict.mercasnacks

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.support.v4.content.ContextCompat.getColor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.net.URL
import java.text.NumberFormat
import java.util.*


/**
 * Created by bett on 8/21/17.
 */
class ProductListAdapter(private val activity: Activity, private var items: ArrayList<ProductGeneric>, val btnlistener: BtnClickListener) : BaseAdapter() {
    private val mInflator: LayoutInflater

    companion object {
        var mClickListener: BtnClickListener? = null
    }

    init {
        this.mInflator = LayoutInflater.from(activity)
    }

    private class ViewHolder(row: View?) {
        var btnAdd: Button? = null
        var ivPhoto: ImageView? = null
        var tvName: TextView? = null
        var tvPrice: TextView? = null


        init {
            this.btnAdd = row?.findViewById<Button>(R.id.item_product_btnAdd)
            this.ivPhoto = row?.findViewById<ImageView>(R.id.item_product_ivPhoto)
            this.tvName = row?.findViewById<TextView>(R.id.item_product_tvName)
            this.tvPrice = row?.findViewById<TextView>(R.id.item_product_tvPrice)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder: ViewHolder
        mClickListener = btnlistener
        if (convertView == null) {
            view = this.mInflator.inflate(R.layout.item_product, parent, false)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        ProductsCache.instance
        var product = items[position]
        viewHolder.tvName?.text = product.name

        if (product.special_price > 0.0) {
            //precio especial
            viewHolder.tvPrice?.text = getCurrency(product.special_price) + "(Oferta)"
            viewHolder.tvPrice?.setTextColor(Color.RED)
        } else {
            //precio normal
            viewHolder.tvPrice?.text = getCurrency(product.price)
            viewHolder.tvPrice?.setTextColor(getColor(activity, R.color.eggplant))
        }
        viewHolder.btnAdd?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (mClickListener != null)
                    mClickListener?.onBtnClick(product)
            }
        })
        if (product.bitmap == null){
            DownLoadImageTask(viewHolder.ivPhoto!!, position).execute(product.image_url)
        }else{
            viewHolder.ivPhoto?.setImageBitmap(product.bitmap)
        }
        //viewHolder.ivPhoto?.setImageResource(R.mipmap.ic_launcher)  //Reemplazarlo por las imagenes de url

        return view as View
    }

    override fun getItem(i: Int): ProductGeneric {
        return items[i]
    }

    open interface BtnClickListener {
        fun onBtnClick(product: ProductGeneric)
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

    fun getCurrency(value : Double) : String{
        var format = NumberFormat.getCurrencyInstance(Locale.CANADA)
        var currency = format.format(value)
        Log.i("Pesos",currency)
        return currency
    }
}

// Class to download an image from url and display it into an image view
private class DownLoadImageTask(internal val imageView: ImageView, var pos : Int) : AsyncTask<String, Void, Bitmap?>() {
    override fun doInBackground(vararg urls: String): Bitmap? {
        val urlOfImage = urls[0]
        return try {
            val inputStream = URL(urlOfImage).openStream()
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) { // Catch the download exception
            e.printStackTrace()
            null
        }
    }


    override fun onPostExecute(result: Bitmap?) {
        var producto= ProductsCache.products.get(pos)
        producto.bitmap = result
        ProductsCache.products.set(pos, producto)
        if (result != null) {
            // Display the downloaded image into image view
            imageView.setImageBitmap(result)
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher)
        }
    }
}