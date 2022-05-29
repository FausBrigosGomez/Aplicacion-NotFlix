package com.example.aplicacionandroid.adapters

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacionandroid.R
import com.example.aplicacionandroid.activities.DetallesPeliculaActivity
import com.example.aplicacionandroid.entidades.Pelicula
import com.squareup.picasso.Picasso
import java.security.AccessControlContext

class ListAdapter(val listaPeliculas: List<Pelicula>?, val context: Context) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.item_pelicula, parent, false)
        return ListViewHolder(layoutInflater)
    }







    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val pelicula = listaPeliculas?.get(position)

        holder.tvTitulo.setText(pelicula?.titulo)
        holder.tvDirector.setText(pelicula?.director)
        holder.tvGenero.setText(pelicula?.genero)
        Picasso.get().load(pelicula?.caratula).into(holder.ivCaratula)

        holder.clPelicula.setOnClickListener{
            val intent = Intent(context, DetallesPeliculaActivity::class.java)
            intent.putExtra("id", pelicula?.id)
            context.startActivity(intent)
        }
    }

    /** 3 formas de hacer funciones que devuelven algo **/
//    override fun getItemCount(): Int {
//        if (listaPeliculas == null){
//            return 0
//        }
//        else{
//            return listaPeliculas.size
//        }
//    }

//    override fun getItemCount(): Int {
//        return listaPeliculas?.size ?: 0
//    }

    override fun getItemCount(): Int = listaPeliculas?.size ?: 0

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTitulo = itemView.findViewById<TextView>(R.id.tvTitulo)
        val tvDirector = itemView.findViewById<TextView>(R.id.tvDirector)
        val tvGenero = itemView.findViewById<TextView>(R.id.tvGenero)
        val ivCaratula = itemView.findViewById<ImageView>(R.id.ivCaratula)

        val clPelicula = itemView.findViewById<ConstraintLayout>(R.id.clPelicula)

    }

}