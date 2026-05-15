package com.sriram.vidyarthibus

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sriram.vidyarthibus.model.BusRoute
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.*

class CrowdMeterActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var dbKey: String = ""
    private lateinit var routeId: String
    private lateinit var routeName: String

    private var routeLat: Double = 0.0
    private var routeLng: Double = 0.0

    private lateinit var tvToolbar: TextView
    private lateinit var cardMeter: CardView
    private lateinit var cardEmpty: CardView
    private lateinit var tvStatusLabel: TextView
    private lateinit var progressCrowd: ProgressBar
    private lateinit var tvLastReport: TextView
    private lateinit var btnReport: MaterialButton
    private lateinit var btnAlternatives: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crowd_meter)

        dbKey = intent.getStringExtra("DB_KEY") ?: ""
        routeId = intent.getStringExtra("ROUTE_ID") ?: ""
        routeName = intent.getStringExtra("ROUTE_NAME") ?: getString(R.string.app_name)

        initViews()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        tvToolbar.text = routeName

        if (dbKey.isNotEmpty()) {
            val database = FirebaseDatabase.getInstance().reference.child("routes").child(dbKey)
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val route = snapshot.getValue(BusRoute::class.java)
                    if (route == null) {
                        cardEmpty.visibility = View.VISIBLE
                        cardMeter.visibility = View.GONE
                    } else {
                        routeLat = route.reporterLat
                        routeLng = route.reporterLng
                        cardEmpty.visibility = View.GONE
                        cardMeter.visibility = View.VISIBLE
                        updateMeterUI(route)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@CrowdMeterActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            cardEmpty.visibility = View.VISIBLE
        }

        btnReport.setOnClickListener { handleReport() }
        btnAlternatives.setOnClickListener {
            startActivity(Intent(this, AlternativesActivity::class.java))
        }
    }

    private fun initViews() {
        tvToolbar = findViewById(R.id.tvToolbar)
        cardMeter = findViewById(R.id.cardMeter)
        cardEmpty = findViewById(R.id.cardEmpty)
        tvStatusLabel = findViewById(R.id.tvStatusLabel)
        progressCrowd = findViewById(R.id.progressCrowd)
        tvLastReport = findViewById(R.id.tvLastReport)
        btnReport = findViewById(R.id.btnReport)
        btnAlternatives = findViewById(R.id.btnAlternatives)
    }

    private fun updateMeterUI(route: BusRoute) {
        val status = route.crowdStatus.uppercase()
        
        // Explicitly typed Triple to resolve ambiguity
        val triple: Triple<Int, Int, Int> = when (status) {
            "EMPTY" -> Triple(15, R.color.statusEmpty, R.string.status_empty)
            "FILLING" -> Triple(60, R.color.statusFilling, R.string.status_filling)
            "FULL" -> Triple(100, R.color.statusFull, R.string.status_full)
            else -> Triple(0, R.color.textMuted, R.string.status_no_data)
        }

        progressCrowd.progress = triple.first
        tvStatusLabel.setText(triple.third)
        tvStatusLabel.setTextColor(ContextCompat.getColor(this, triple.second))

        val diffMinutes = (System.currentTimeMillis() - route.lastUpdated) / 60000
        tvLastReport.text = if (route.lastUpdated == 0L) getString(R.string.last_report_none) else getString(R.string.last_reported, diffMinutes.toInt())
    }

    private fun handleReport() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null && dbKey.isNotEmpty()) {
                val updates = hashMapOf<String, Any>(
                    "crowdStatus" to "FULL",
                    "lastUpdated" to ServerValue.TIMESTAMP,
                    "reporterLat" to location.latitude,
                    "reporterLng" to location.longitude
                )
                FirebaseDatabase.getInstance().reference.child("routes").child(dbKey).updateChildren(updates)
                    .addOnSuccessListener {
                        Toast.makeText(this, getString(R.string.toast_status_updated), Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, getString(R.string.toast_location_error), Toast.LENGTH_SHORT).show()
            }
        }
    }
}