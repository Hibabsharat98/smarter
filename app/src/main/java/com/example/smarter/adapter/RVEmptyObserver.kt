package com.example.smarter.adapter

import android.view.View

class RVEmptyObserver
/**
 * Constructor to set an Empty View for the RV
 */
    (
    private val recyclerView: androidx.recyclerview.widget.RecyclerView, private val emptyView: View?) : androidx.recyclerview.widget.RecyclerView.AdapterDataObserver() {


    init {
        checkIfEmpty()
    }


    /**
     * Check if Layout is empty and show the appropriate view
     */
    private fun checkIfEmpty() {
        if (emptyView != null && recyclerView.adapter != null) {
            val emptyViewVisible = recyclerView.adapter!!.itemCount == 0
            emptyView.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
            recyclerView.visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
        }
    }


    /**
     * Abstract method implementations
     */
    override fun onChanged() {
        checkIfEmpty()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        checkIfEmpty()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        checkIfEmpty()
    }

}