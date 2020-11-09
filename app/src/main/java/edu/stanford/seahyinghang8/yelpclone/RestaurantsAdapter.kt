package edu.stanford.seahyinghang8.yelpclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_restaurant.view.*

class RestaurantsAdapter(val context: MainActivity, val restaurants: MutableList<YelpRestaurant>) :
    RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.bind(restaurant)
    }

    override fun getItemCount() = restaurants.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(restaurant: YelpRestaurant) {
            itemView.tvName.text = restaurant.name
            itemView.ratingBar.rating = restaurant.rating.toFloat()
            itemView.tvNumReviews.text = "${restaurant.numReviews} Reviews"
            itemView.tvAddress.text = restaurant.location.address
            itemView.tvCategory.text = if (restaurant.categories.isNotEmpty()) (restaurant.categories[0].title) else ""
            itemView.tvDistance.text = restaurant.displayDistance()
            itemView.tvPrice.text = restaurant.price
            Glide.with(context).load(restaurant.imageUrl).apply(RequestOptions().transforms(
                CenterCrop(), RoundedCorners(20)
            )).into(itemView.imageView)
        }
    }
}
