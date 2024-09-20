package com.example.mynotepadapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var memoDatabaseHelper: MemoDatabaseHelper
    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText
    private lateinit var buttonSave: Button
    private lateinit var recyclerViewMemos: RecyclerView
    private lateinit var memoAdapter: MemoAdapter
    private var currentMemoId: Long? = null  // 현재 편집 중인 메모의 ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // MemoDatabaseHelper 초기화
        memoDatabaseHelper = MemoDatabaseHelper(this)

        // 뷰 초기화
        editTextTitle = findViewById(R.id.editTextTitle)
        editTextContent = findViewById(R.id.editTextContent)
        buttonSave = findViewById(R.id.buttonSave)
        recyclerViewMemos = findViewById(R.id.recyclerViewMemos)

        // RecyclerView 설정
        recyclerViewMemos.layoutManager = LinearLayoutManager(this)

        // 메모 목록 불러오기 및 표시
        loadMemos()

        // 저장 버튼 클릭 시 메모 저장 또는 수정
        buttonSave.setOnClickListener {
            val title = editTextTitle.text.toString()
            val content = editTextContent.text.toString()

            if (currentMemoId == null) {
                // 새 메모 저장
                memoDatabaseHelper.insertMemo(title, content)
            } else {
                // 기존 메모 수정
                memoDatabaseHelper.updateMemo(currentMemoId!!, title, content)
            }

            // 입력 필드 비우기
            clearInputFields()

            // 메모 목록 새로고침
            loadMemos()
        }
    }

    private fun loadMemos() {
        // 메모 목록을 불러와서 RecyclerView에 표시
        val memos = memoDatabaseHelper.getAllMemos()
        memoAdapter = MemoAdapter(memos, onEditClick = { memo ->
            // 메모 클릭 시 편집
            editMemo(memo)
        }, onDeleteClick = { memo ->
            // 메모 삭제
            deleteMemo(memo)
        })
        recyclerViewMemos.adapter = memoAdapter
    }

    private fun editMemo(memo: Memo) {
        // 클릭한 메모의 내용을 EditText에 표시하고 수정 모드로 변경
        editTextTitle.setText(memo.title)
        editTextContent.setText(memo.content)
        currentMemoId = memo.id  // 수정할 메모의 ID 저장
        buttonSave.text = "Update"  // 버튼 텍스트를 "Update"로 변경
    }

    private fun deleteMemo(memo: Memo) {
        // 메모 삭제 후 목록 새로고침
        memoDatabaseHelper.deleteMemo(memo.id)
        loadMemos()
    }

    private fun clearInputFields() {
        // 입력 필드 초기화 및 상태 리셋
        editTextTitle.text.clear()
        editTextContent.text.clear()
        currentMemoId = null
        buttonSave.text = "Save"  // 버튼 텍스트를 다시 "Save"로 변경
    }
}
