package com.khushk.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.khushk.a7minuteworkout.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarHistoryActivity)

        val actionbar = supportActionBar //actionbar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true) //set back button
            actionbar.title = "HISTORY" // Setting a title in the action bar.
        }

        binding.toolbarHistoryActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getAllCompletedDates() {

        // Instance of the Sqlite Open Helper class.
        val dbHandler = SqliteOpenHelper(this, null)

        val allCompletedDatesList =
            dbHandler.getAllCompletedDatesList() // List of history table data

        // List items are printed in log.
        if (allCompletedDatesList.size > 0) {
            // Here if the List size is greater then 0 we will display the item in the recycler view or else we will show the text view that no data is available.
            binding.tvHistory.visibility = View.VISIBLE
            binding.rvHistory.visibility = View.VISIBLE
            binding.tvNoDataAvailable.visibility = View.GONE

            // Creates a vertical Layout Manager
            binding.rvHistory.layoutManager = LinearLayoutManager(this)

            // History adapter is initialized and the list is passed in the param.
            val historyAdapter = HistoryAdapter(this, allCompletedDatesList)

            // Access the RecyclerView Adapter and load the data into it
            binding.rvHistory.adapter = historyAdapter
        } else {
            binding.tvHistory.visibility = View.GONE
            binding.rvHistory.visibility = View.GONE
            binding.tvNoDataAvailable.visibility = View.VISIBLE
        }
    }
}

