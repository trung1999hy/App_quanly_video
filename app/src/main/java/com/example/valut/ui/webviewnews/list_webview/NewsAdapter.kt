package com.example.valut.ui.webviewnews.list_webview

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.valut.databinding.ItemLayoutNewBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException


class NewsAdapter(val onClickItem: (String) -> Unit) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private var list: ArrayList<String> = arrayListOf()

    inner class NewsViewHolder(val newBinding: ItemLayoutNewBinding) :
        RecyclerView.ViewHolder(newBinding.root) {
        @SuppressLint("SetJavaScriptEnabled")
        fun bind(string: String) {
            GetElementByIdTask().execute(string)

        }

        @Suppress("DEPRECATION")
        inner class GetElementByIdTask :
            AsyncTask<String?, Void?, String?>() {


            override fun onPostExecute(elementText: String?) {
                if (elementText != null) {
                    Log.d(ContentValues.TAG, "Element text: $elementText")
                    newBinding.title.text = elementText

                } else {
                    Log.e(ContentValues.TAG, "Unable to fetch element by ID.")
                }
            }

            override fun doInBackground(vararg params: String?): String? {
                val url = params[0]
                var elementText: String? = null
                try {
                    val document: Document = Jsoup.connect(url).get()
                    val element: Elements = document.getElementsByClass("title-detail")
                    if (element != null) {
                        elementText = element.text()
                    }
                } catch (e: IOException) {
                    Log.e(ContentValues.TAG, "Error fetching web page: " + e.message)
                }
                return elementText
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding =
            ItemLayoutNewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            onClickItem.invoke(list[position])
        }
    }

    fun setData(list: ArrayList<String>) {
        this.list = list
        notifyDataSetChanged()
    }
}