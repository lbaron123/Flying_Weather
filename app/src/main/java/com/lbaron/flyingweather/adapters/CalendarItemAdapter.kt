package com.lbaron.flyingweather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lbaron.flyingweather.DayAndAirport
import com.lbaron.flyingweather.R


/**
 * In agle brackets in RV.Adapter we have out MetarViewHolder defined as a nested class below
 * This shows the default Adapter what viewholder we have to use
 * Implemented onCreateViewHolder onBindViewHolder getItemCount by pressing CTRL-I inside the class
 * @param dayAndAirports list of our objects to go into the recyclerview - private because it only needs to be accessed in the class
 */
class CalendarItemAdapter(private var dayAndAirports: ArrayList<DayAndAirport>) : RecyclerView.Adapter<CalendarItemAdapter.DayViewHolder>() {

    /**
     * onCreateViewHolder is called by RV when it is time to create a new viewholder
     * Returns a MetarViewHolder that we created below
     * This will only be called a few times, when we have enough views to fill the screen
     * @param parent is the recyclerView
     * @param viewType used if more than one type of view in the RV
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        // LayoutInflator turns xml into the itemView of the required type
        // Parent is the recyclerview, context is the activity we are in
        return DayViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.calendar_recyclerview_item, parent, false))
    }

    /**
     * Called over and over again as we scroll, so need to be careful of what code we put in here
     * Changes the data in a view
     * @param holder One of our DayViewHolders to be filled with data
     * @param position of the item in the list
     */
    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        // Want position of item in dataset - is what we display
        // dayList from the class constructor - all the data
        val currentItem = dayAndAirports[position]
        // holder contains references to the views as defined below
        holder.tvDay.text = currentItem.day

        //Following code adds i textviews for each airport in the day
        var textView: TextView
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val layout = holder.itemView.findViewById<LinearLayout>(R.id.ll_airports_per_day)
        val numberOfAirports = dayAndAirports[position].airports.size
        val currentNumberOfTextViews = layout.childCount
        for (i in 0 until (numberOfAirports - currentNumberOfTextViews)) {
            textView = TextView(holder.tvDay.context)
            textView.text = dayAndAirports[position].airports[i]
            textView.layoutParams = layoutParams
            layout.addView(textView)
        }
    }

    override fun getItemCount(): Int {
        return dayAndAirports.size
    }

    /**
     * Viewholder represents a single row in the list
     *  One instance of DayViewHolder contains one instance of layout + metadata about row eg position
     *  It works by caching references to each view tv_xxx for example
     *  We will have as many viewholders as required to fill the screen plus a bit of margin
     * @param itemView is one instance of our row layout - it contains the references to our textviews
     */
    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDay: TextView = itemView.findViewById(R.id.tv_day)
    }

    /**
     * Takes a list of days and sets them to the class's internal structure
     * It then notifies any observers that the data has changed
     */
    fun setData(dayListInput: ArrayList<DayAndAirport>){
        this.dayAndAirports = dayListInput
        notifyDataSetChanged()
    }
}