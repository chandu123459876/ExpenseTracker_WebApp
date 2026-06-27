package com.softwareapp.expensestracker

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var mainBalance: TextView
    private lateinit var totalExpense: TextView
    private lateinit var addExpense: TextView
    private lateinit var showExpense: TextView
    private lateinit var totalIncome: TextView
    private lateinit var addIncome: TextView
    private lateinit var showIncome: TextView
    private lateinit var overallShow: TextView
    private lateinit var sqlite: ExpenseSqlite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        sqlite = ExpenseSqlite(this)

        mainBalance = findViewById(R.id.mainBalance)
        totalExpense = findViewById(R.id.totalExpense)
        addExpense = findViewById(R.id.addExpense)
        showExpense = findViewById(R.id.expenseShow)
        totalIncome = findViewById(R.id.totalIncome)
        addIncome = findViewById(R.id.addIncome)
        showIncome = findViewById(R.id.incomeShow)
        overallShow = findViewById(R.id.overallShow)

        addExpense.setOnClickListener {
            AddActivity.EXPENSE = true
            startActivity(Intent(this, AddActivity::class.java))
        }
        addIncome.setOnClickListener {
            AddActivity.EXPENSE = false
            startActivity(Intent(this, AddActivity::class.java))
        }
        showExpense.setOnClickListener {
            RecyclerViewActivity.REC_VIEW_MODE = RecyclerViewActivity.MODE_EXPENSE
            startActivity(Intent(this, RecyclerViewActivity::class.java))
        }
        showIncome.setOnClickListener {
            RecyclerViewActivity.REC_VIEW_MODE = RecyclerViewActivity.MODE_INCOME
            startActivity(Intent(this, RecyclerViewActivity::class.java))
        }
        overallShow.setOnClickListener {
            RecyclerViewActivity.REC_VIEW_MODE = RecyclerViewActivity.MODE_OVERALL
            startActivity(Intent(this, RecyclerViewActivity::class.java))
        }
        loadData()
    }
    override fun onResume() {
        super.onResume()
        loadData()
    }
    private fun loadData() {
        val income = sqlite.showIncome()
        val expense = sqlite.showExpense()
        val balance = income - expense

        totalIncome.text = getString(R.string.amount_format, income.toString())
        totalExpense.text = getString(R.string.amount_format, expense.toString())
        mainBalance.text = getString(R.string.amount_format, balance.toString())
    }
}