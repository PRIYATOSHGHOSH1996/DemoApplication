package com.example.demoapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapplication.R
import com.example.demoapplication.api.ApiResponseModel
import com.example.demoapplication.ui.details.DetailsActivity


class PaginationAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ITEM = 0
    private val LOADING = 1

    private var dataList: ArrayList<ApiResponseModel?> = ArrayList()


    private var isLoadingAdded = false

    fun setData(data: ArrayList<ApiResponseModel?>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM -> {
                val v2: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_data, parent, false)
                DataVH(v2)
            }

            LOADING -> {
                val v2: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_progress, parent, false)
                LoadingVH(v2)
            }

            else -> {
                val v2: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_progress, parent, false)
                LoadingVH(v2)
            }
        }
    }

    override fun getItemCount(): Int {
        return  dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == dataList.size - 1 && isLoadingAdded) LOADING else ITEM
    }
    fun add(r: ApiResponseModel?) {
        dataList.add(r)
        notifyItemInserted(dataList.size - 1)
    }

    fun addAll(datas: List<ApiResponseModel>) {
        for (result in datas) {
            add(result)
        }
    }

    fun remove(r: ApiResponseModel) {
        val position: Int = dataList.indexOf(r)
        if (position > -1) {
            dataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }


    fun addLoadingFooter() {
        isLoadingAdded = true
        add(null)
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position: Int = dataList.size - 1
        val result: ApiResponseModel? = dataList[position]
        if (result == null) {
            dataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> {
                val dataVH: DataVH = holder as DataVH
                dataVH.userId.text ="User ID : "+dataList[position]?.userId.toString()
                dataVH.title.text = dataList[position]?.title
                dataVH.body.text = dataList[position]?.body
                dataVH.itemView.setOnClickListener {
                    val i: Intent =
                        Intent(context, DetailsActivity::class.java)
                    i.putExtra("userId", dataList[position]?.userId.toString())
                    i.putExtra("title", dataList[position]?.title.toString())
                    i.putExtra("body", dataList[position]?.body.toString())
                    context.startActivity(i)
                }
            }

            LOADING -> {}
        }
    }
}


class DataVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView
    val body: TextView
    val userId: TextView

    init {
        title = itemView.findViewById<View>(R.id.title) as TextView
        body = itemView.findViewById<View>(R.id.body) as TextView
        userId = itemView.findViewById<View>(R.id.userId) as TextView
    }
}

class LoadingVH(itemView: View?) : RecyclerView.ViewHolder(itemView!!)