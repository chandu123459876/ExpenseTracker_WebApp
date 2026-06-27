package com.softwareapp.expensestracker
import android.database.Cursor
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
class RecyclerViewActivity : AppCompatActivity() {
    companion object {
        const val MODE_EXPENSE = 0
        const val MODE_INCOME = 1
        const val MODE_OVERALL = 2
        var REC_VIEW_MODE = MODE_EXPENSE
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvTitle: TextView
    private lateinit var sqlite: ExpenseSqlite
    private lateinit var adapter: ExpenseAdapter
    private val list = mutableListOf<ExpenseAdapter.ExpenseItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        recyclerView = findViewById(R.id.recyclerView)
        tvTitle = findViewById(R.id.tvRecyclerTitle)
        sqlite = ExpenseSqlite(this)

        adapter = ExpenseAdapter(list) { item ->
            showDeleteDialog(item)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        refreshList()
    }
    private fun refreshList() {
        when (REC_VIEW_MODE) {
            MODE_EXPENSE -> {
                tvTitle.text = getString(R.string.title_expense_list)
                loadData(sqlite.showExpenseRecyclerView(), "expense")
            }
            MODE_INCOME -> {
                tvTitle.text = getString(R.string.title_income_list)
                loadData(sqlite.showIncomeRecyclerView(), "income")
            }
            MODE_OVERALL -> {
                tvTitle.text = getString(R.string.title_overall_list)
                loadData(sqlite.showOverallRecyclerView(), null)
            }
        }
    }
    private fun loadData(cursor: Cursor?, defaultType: String?) {
        list.clear()
        cursor?.let {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow("id"))
                val amount = it.getDouble(it.getColumnIndexOrThrow("amount"))
                val reason = it.getString(it.getColumnIndexOrThrow("reason"))
                val time = it.getLong(it.getColumnIndexOrThrow("time"))
                val type = if (defaultType != null) {
                    defaultType
                } else {
                    it.getString(it.getColumnIndexOrThrow("type"))
                }
                list.add(ExpenseAdapter.ExpenseItem(id, amount, reason, time, type))
            }
            it.close()
        }
        adapter.notifyDataSetChanged()
    }
    private fun showDeleteDialog(item: ExpenseAdapter.ExpenseItem) {
        AlertDialog.Builder(this)
            .setTitle("Delete Record")
            .setMessage("Are you sure you want to delete this record?")
            .setPositiveButton("Yes") { _, _ ->
                if (item.type == "expense") {
                    sqlite.deleteExpense(item.id)
                } else {
                    sqlite.deleteIncome(item.id)
                }
                Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show()
                refreshList()
            }
            .setNegativeButton("No", null)
            .show()
    }
}