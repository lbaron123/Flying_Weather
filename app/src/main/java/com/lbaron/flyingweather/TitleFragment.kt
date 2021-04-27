package com.lbaron.flyingweather
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.findNavController
//
//
//class TitleFragment : Fragment() {
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//    ): View? {
//        //Inflate the layout file
//        val view = inflater.inflate(R.layout.fragment_title, container, false)
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val btn: Button = view.findViewById(R.id.button)
//        btn.setOnClickListener(View.OnClickListener { view -> findNavController().navigate(R.id.action_titleFragment_to_mainContent) })
//
//
//    }
//}


import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class TitleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_title, container, false)

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