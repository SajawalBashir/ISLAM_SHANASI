package com.example.islam_shanasi.custom_adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.islam_shanasi.Common
import com.example.islam_shanasi.Data_Classes.Scholar
import com.example.islam_shanasi.R
import com.example.islam_shanasi.databinding.ScholarTileRecyRowBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class scholarTileCustomAdapter(ctxt: Context, arrayList: ArrayList<Scholar>) :
    RecyclerView.Adapter<scholarTileCustomAdapter.viewHolder>() {

    val ctxt: Context
    val arrayList: ArrayList<Scholar>
    var storageReference: StorageReference

    init {
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(Common.storageURL)
        this.ctxt = ctxt
        this.arrayList = arrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(
            ScholarTileRecyRowBinding.inflate(
                LayoutInflater.from(ctxt),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        println("##### Line 41 scholarTileCustomAdapter.kt.kt list")
        storageReference.child(arrayList[position].profilePicRef).downloadUrl
            .addOnSuccessListener {
                Glide.with(ctxt)
                    .load(it)
                    .into(holder.binding.dp)

            }
            .addOnFailureListener {
                println("##### line 47 scholarTileCustomAdapter.kt Error: ${it.message}")
            }
        holder.binding.tvScholarName.text = arrayList[position].name
        val location = arrayList[position].city + ", " + arrayList[position].country
        holder.binding.tvLocation.text = location
        val popularity = arrayList[position].rating + " pts"
        holder.binding.tvPopularity.text = popularity
        if(arrayList[position].verified.equals("1")){
            holder.binding.tvVerified.text = "VERIFIED"
            holder.binding.tvVerified.setTextColor(ContextCompat.getColor(ctxt, R.color.black))
            holder.binding.tvVerified.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }
    }

    override fun getItemCount(): Int {
        println("##### Line 65 scholarTileCustomAdapter.kt.kt arrayList.size = ${arrayList.size}")
        return arrayList.size
    }

    class viewHolder(binding: ScholarTileRecyRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding: ScholarTileRecyRowBinding

        init {
            this.binding = binding
        }
    }
}