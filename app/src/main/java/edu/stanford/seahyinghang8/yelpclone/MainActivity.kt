package edu.stanford.seahyinghang8.yelpclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MainActivity"
private const val BASE_URL = "https://api.yelp.com/v3/"
private const val YELP_API_KEY = "EtL6fOp8e9PwBvKZsXdhZSK84q4NOGTXgi8vb3qir5vyle1_XWRBGsjHrDFdqbsdi8jsNZ5r0iw_8_HM0vR57NbdiZkf_aG2bqaOvn31ruFbxGd9J_iHJliO1XWoX3Yx"

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: RestaurantsAdapter
    private val restaurants = mutableListOf<YelpRestaurant>()
    private lateinit var yelpService: YelpService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = RestaurantsAdapter(this, restaurants)
        rvRestaurants.adapter = adapter
        rvRestaurants.layoutManager = LinearLayoutManager(this)

        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()
        yelpService = retrofit.create(YelpService::class.java)


        etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                yelpQuery()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        etLocation.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                yelpQuery()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        etLocation.requestFocus()
    }


    fun yelpQuery() {
        val searchTerm = etSearch.text.toString()
        val location = etLocation.text.toString()

        if (searchTerm.isBlank() || location.isBlank()) {
            Log.i(TAG, "Search Term or Location is blank")
            return
        }

        yelpService.searchRestaurants("BEARER $YELP_API_KEY", searchTerm, location).enqueue(object : Callback<YelpSearchResult> {
            override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                Log.i(TAG, "onResponse $response")
                val body = response.body()

                if (body == null) {
                    Log.w(TAG, "Did not receieve valid body")
                    return
                }

                restaurants.clear()
                restaurants.addAll(body.restaurants)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                Log.i(TAG, "Failure $t")
            }
        })
    }
}