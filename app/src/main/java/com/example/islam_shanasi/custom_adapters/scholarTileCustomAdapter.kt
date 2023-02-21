package com.example.islam_shanasi.custom_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.islam_shanasi.Data_Classes.Scholar
import com.example.islam_shanasi.databinding.ScholarTileRecyRowBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class scholarTileCustomAdapter(ctxt: Context, arrayList: ArrayList<Scholar>) :
    RecyclerView.Adapter<scholarTileCustomAdapter.viewHolder>() {

    val ctxt: Context
    val arrayList: ArrayList<Scholar>
    var storageReference: StorageReference

    init {
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://islam-shanasi.appspot.com")
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

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class viewHolder(binding: ScholarTileRecyRowBinding) : RecyclerView.ViewHolder(binding.root){
        val binding: ScholarTileRecyRowBinding

        init {
            this.binding = binding
        }
    }
}