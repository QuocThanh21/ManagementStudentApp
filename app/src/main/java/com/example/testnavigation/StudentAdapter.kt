package com.example.testnavigation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso

class StudentAdapter(val context: Context, val items: MutableList<Student>,
                     private val onItemClicked: (std: Student) -> Unit) :
    RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_student,
                parent,
                false
            ), onItemClicked
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (items[position].isValid) {
            val item = items[position]

            holder.tvName.text = "Name: " + item.name
            holder.tvId.text = "ID: " + item.id.toString()
            Glide.with(context)
                .load(item.image)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.no_image_found)
                .fitCenter()
                .into(holder.ivImage)

        }

    }


    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    inner class ViewHolder(view: View , private val onItemClicked: (std: Student) -> Unit)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        // Holds the TextView that will add each item to
        val tvName: TextView = view.findViewById<TextView>(R.id.tvName)
        val tvId: TextView = view.findViewById<TextView>(R.id.tvId)
        val ivImage: ImageView = view.findViewById<ImageView>(R.id.ivImage)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val std = items[adapterPosition]
            onItemClicked(std)
        }

    }
}