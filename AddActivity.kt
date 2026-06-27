package com.softwareapp.expensestracker
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
class AddActivity : AppCompatActivity() {
    companion object {
        var EXPENSE = true
    }
    private lateinit var etAmount: TextInputEditText
    private lateinit var etReason: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var addTitle: TextView
    private lateinit var sqlite: ExpenseSqlite
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        sqlite = ExpenseSqlite(this)
        etAmount = findViewById(R.id.etAmount)
        etReason = findViewById(R.id.etReason)
        btnSave = findViewById(R.id.btnSave)
        addTitle = findViewById(R.id.addTitle)
        if(EXPENSE) {
            addTitle.text = getString(R.string.title_add_expense)
        }else{
            addTitle.text = getString(R.string.title_add_income)
        }
        btnSave.setOnClickListener {
            val amountStr = etAmount.text.toString()
            val reason = etReason.text.toString()
            if(amountStr.isNotEmpty() && reason.isNotEmpty()) {
                val amount = try { amountStr.toDouble() } catch (e: Exception) { 0.0 }
                if(EXPENSE){
                    sqlite.addExpense(amount, reason)
                    Toast.makeText(this, getString(R.string.msg_expense_added), Toast.LENGTH_SHORT).show()
                }else{
                    sqlite.addIncome(amount, reason)
                    Toast.makeText(this, getString(R.string.msg_income_added), Toast.LENGTH_SHORT).show()
                }
                finish()
            }else{
                Toast.makeText(this, getString(R.string.msg_fill_fields), Toast.LENGTH_SHORT).show()
            }
        }
    }
}