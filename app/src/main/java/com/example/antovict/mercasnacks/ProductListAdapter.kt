package com.example.antovict.mercasnacks

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.net.URL


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

        var product = items[position]
        viewHolder.tvName?.text = product.name
        if (product.special_price > 0) {
            //precio especial
            viewHolder.tvPrice?.text = product.special_price.toString()
        } else {
            //precio normal
            viewHolder.tvPrice?.text = product.price.toString()
        }
        viewHolder.btnAdd?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (mClickListener != null)
                    mClickListener?.onBtnClick(product)
            }
        })
        viewHolder.ivPhoto?.setImageResource(R.mipmap.ic_launcher)  //Reemplazarlo por las imagenes de url
        DownLoadImageTask(viewHolder.ivPhoto!!).execute(product.image_url)
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
}

// Class to download an image from url and display it into an image view
private class DownLoadImageTask(internal val imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
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
        if (result != null) {
            // Display the downloaded image into image view
            imageView.setImageBitmap(result)
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher)
        }
    }
}