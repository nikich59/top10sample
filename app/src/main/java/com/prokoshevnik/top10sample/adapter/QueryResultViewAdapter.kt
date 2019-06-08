package com.prokoshevnik.top10sample.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prokoshevnik.top10sample.R
import com.prokoshevnik.top10sample.backend.entity.GoogleSearchItem
import com.prokoshevnik.top10sample.entity.GoogleSearchHeader
import kotlinx.android.synthetic.main.search_result_header_layout.view.*
import kotlinx.android.synthetic.main.search_result_item_layout.view.*


class QueryResultViewAdapter(private var dataset: List<Any>) :
    RecyclerView.Adapter<QueryResultViewAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = when (viewType) {
            RESULT_HEADER_TYPE ->
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.search_result_header_layout, parent, false)
            RESULT_ITEM_TYPE ->
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.search_result_item_layout, parent, false)
            else ->
                throw RuntimeException("Items of type $viewType are not allowed")
        }

        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        when (dataset[position]) {
            is GoogleSearchHeader ->
                return RESULT_HEADER_TYPE
            is GoogleSearchItem ->
                return RESULT_ITEM_TYPE
        }

        throw RuntimeException("Items of class ${dataset[position]::class.java} are not allowed")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (dataset[position]) {
            is GoogleSearchHeader ->
                onBindViewHolderResultHeader(holder, dataset[position] as GoogleSearchHeader)
            is GoogleSearchItem ->
                onBindViewHolderResultItem(holder, dataset[position] as GoogleSearchItem)
        }
    }

    private fun onBindViewHolderResultHeader(holder: ViewHolder, header: GoogleSearchHeader) {
        holder.view.searchResultHeaderView_queryView.text =
            holder.view.context.getString(R.string.searchHeaderTemplate).format(
                header.query
            )
    }

    private fun onBindViewHolderResultItem(holder: ViewHolder, item: GoogleSearchItem) {
        holder.view.queryResultItemView_titleView.text = item.title
        holder.view.queryResultItemView_linkView.text = item.link
        holder.view.queryResultItemView_snippetView.text = item.snippet

        holder.view.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(item.link)

            holder.view.context.startActivity(intent)
        }
    }

    override fun getItemCount() = dataset.size

    companion object {
        const val RESULT_HEADER_TYPE = 1
        const val RESULT_ITEM_TYPE = 2
    }
}

