package com.sriram.vidyarthibus

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sriram.vidyarthibus.adapter.RouteAdapter
import com.sriram.vidyarthibus.model.BusRoute
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RouteListActivity : AppCompatActivity() {

    private lateinit var adapter: RouteAdapter
    private val routesList = mutableListOf<BusRoute>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_list)

        // Toolbar title is now fixed as "Choose Your Bus"
        findViewById<TextView>(R.id.tvToolbarTitle).text = getString(R.string.title_choose_bus)

        val rvRoutes: RecyclerView = findViewById(R.id.rvRoutes)
        rvRoutes.layoutManager = LinearLayoutManager(this)
        
        adapter = RouteAdapter(routesList) { route ->
            val intent = Intent(this, CrowdMeterActivity::class.java)
            intent.putExtra("DB_KEY", route.dbKey)
            intent.putExtra("ROUTE_ID", route.routeId)
            intent.putExtra("ROUTE_NAME", route.routeName)
            startActivity(intent)
        }
        rvRoutes.adapter = adapter

        fetchRoutesFromFirebase()
    }

    private fun fetchRoutesFromFirebase() {
        val database = FirebaseDatabase.getInstance().reference.child("routes")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                routesList.clear()
                val selectedCollege = intent.getStringExtra("COLLEGE_NAME") ?: ""
                // No longer need tvNoRoutes as it's not in the simplified layout

                for (child in snapshot.children) {
                    val route = child.getValue(BusRoute::class.java)
                    if (route != null && route.college == selectedCollege) {
                        val filteredRoute = route.copy(routeId = child.key ?: "")
                        filteredRoute.dbKey = child.key ?: ""
                        routesList.add(filteredRoute)
                    }
                }
                
                // No longer show empty state via TextView, just update adapter
                adapter.updateData(routesList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RouteListActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}