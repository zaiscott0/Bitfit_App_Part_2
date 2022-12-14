package com.example.bitfitv1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class FeedFragment : Fragment() {
    private val habits = mutableListOf<Habit>()
    private lateinit var rvFeed: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        rvFeed = view.findViewById<RecyclerView>(R.id.rvHabits)
        val habitAdapter = HabitAdapter(view.context,habits)
        rvFeed.adapter = habitAdapter
        rvFeed.layoutManager = LinearLayoutManager(view.context).also {
            val dividerItemDecoration = DividerItemDecoration(view.context,it.orientation)
            rvFeed.addItemDecoration(dividerItemDecoration)
        }
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
                    habits.clear()
                    habits.addAll(mappedList)
                    habitAdapter.notifyDataSetChanged()

                }
            }
        }

        return view
    }

}