package com.example.bitfitv1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class OverviewFragment : Fragment() {
    private lateinit var averageHour: TextView
    private lateinit var minHour: TextView
    private lateinit var maxHour: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)
        averageHour = view.findViewById(R.id.tvAverageHours)
        minHour = view.findViewById(R.id.tvMinHours)
        maxHour = view.findViewById(R.id.tvMaxHour)
        lifecycleScope.launch {
            (activity?.application as BitFitApplication).db.habitDao().getAll().collect() {
                    databaseList ->
                databaseList.map { entity ->
                    Habit(
                        entity.date,
                        entity.hour,
                        entity.comment,
                        entity.rate
                    )
                }.also { mappedList ->
                    update(mappedList)

                }
            }
        }
        val clearButtonView: Button = view.findViewById(R.id.btnClear)
        clearButtonView.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                (activity?.application as BitFitApplication).db.habitDao().deleteAll()
            }
        }
        return view
    }

    private fun update(habits: List<Habit>) {
        if (habits.isEmpty()) {
            averageHour.text = "No Data"
            minHour.text = "No Data"
            maxHour.text = "No Data"
            return
        }
        var minHours : Int = Int.MAX_VALUE
        var maxHours : Int = Int.MIN_VALUE
        var sumHours : Long = 0
        for (habit in habits) {
            habit.hour?.let {
                sumHours += it
                if (it < minHours) minHours = it
                if (it > maxHours) maxHours = it
            }
        }
        averageHour.text = (sumHours /habits.size).toString()
        minHour.text = minHours.toString()
        maxHour.text = maxHours.toString()
    }

}