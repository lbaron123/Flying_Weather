package com.lbaron.flyingweather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lbaron.flyingweather.R
import com.lbaron.flyingweather.databaseStuff.Metar

/**
 * In agle brackets in RV.Adapter we have out MetarViewHolder defined as a nested class below
 * This shows the default Adapter what viewholder we have to use
 * Implemented onCreateViewHolder onBindViewHolder getItemCount by pressing CTRL-I inside the class
 * @param metarList list of our objects to go into the recyclerview - private because it only needs to be accessed in the class
 */
class MetarItemAdapter(val deleteMetar: (Metar) -> Unit) : RecyclerView.Adapter<MetarItemAdapter.MetarViewHolder>() {

    private var metarList = emptyList<Metar>()


    /**
     * onCreateViewHolder is called by RV when it is time to create a new viewholder
     * Returns a MetarViewHolder that we created below
     * This will only be called a few times, when we have enough views to fill the screen
     * @param parent is the recyclerView
     * @param viewType used if more than one type of view in the RV
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetarViewHolder {
        // LayoutInflator turns xml into the itemView of the required type
        // Parent is the recyclerview, context is the activity we are in

        return MetarViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.general_weather_recyclerview_item, parent, false))
    }

    /**
     * Called over and over again as we scroll, so need to be careful of what code we put in here
     * Changes the data in a view
     * @param holder One of our MetarViewHolders to be filled with data
     * @param position of the item in the list
     */
    override fun onBindViewHolder(holder: MetarViewHolder, position: Int) {
        // Want position of item in dataset - is what we display
        // metarList from the class constructor - all the data
        val currentItem = metarList[position]
        // holder contains references to the views as defined below
        holder.tvAirportName.text = currentItem.station
        holder.tvMetar.text = currentItem.metar
        holder.tvMetar.setOnClickListener {
            deleteMetar(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return metarList.size
    }

    /**
     * Viewholder represents a single row in the list
     *  One instance of MetarViewHolder contains one instance of layout + metadata about row eg position
     *  It works by caching references to each view tv_xxx for example
     *  We will have as many viewholders as required to fill the screen plus a bit of margin
     * @param itemView is one instance of our row layout - it contains the references to our textviews
     */
    class MetarViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        // imageView : ImageView = itemView.findViewById(R.id.iv_image)  <-- Syntax if the layout has a picture
        val tvAirportName: TextView = itemView.findViewById(R.id.tv_airport_name)
        val tvMetar : TextView = itemView.findViewById(R.id.tv_metar)
    }

    /**
     * Takes a list of metars and sets them to the class's internal structure
     * It then notifies any observers that the data has changed
     */
    fun setData(metarListInput: List<Metar>){
        this.metarList = metarListInput
        notifyDataSetChanged()
    }
}