package com.lbaron.flyingweather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.lbaron.flyingweather.utility.u

class CalendarViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
            return inflater.inflate(R.layout.calendar_view_layout, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn: Button = view.findViewById(R.id.btn)
        btn.setOnClickListener {
            context?.let { it1 -> u.l(it1, "Button Pressed") }
            findNavController().navigate(R.id.action_titleFragment_to_mainContent)
        }
    }
}