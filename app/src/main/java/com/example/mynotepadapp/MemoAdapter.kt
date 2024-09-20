package com.example.mynotepadapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MemoAdapter(
    private val memos: List<Memo>,
    private val onEditClick: (Memo) -> Unit,
    private val onDeleteClick: (Memo) -> Unit
) : RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {

    inner class MemoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.textViewTitle)
        val contentTextView: TextView = view.findViewById(R.id.textViewContent)
        val buttonEdit: Button = view.findViewById(R.id.buttonEdit)  // 수정 버튼
        val buttonDelete: Button = view.findViewById(R.id.buttonDelete)  // 삭제 버튼
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_memo, parent, false)
        return MemoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        val memo = memos[position]
        holder.titleTextView.text = memo.title
        holder.contentTextView.text = memo.content

        // 수정 버튼 클릭 시
        holder.buttonEdit.setOnClickListener {
            onEditClick(memo)
        }

        // 삭제 버튼 클릭 시
        holder.buttonDelete.setOnClickListener {
            onDeleteClick(memo)
        }
    }

    override fun getItemCount(): Int = memos.size
}
