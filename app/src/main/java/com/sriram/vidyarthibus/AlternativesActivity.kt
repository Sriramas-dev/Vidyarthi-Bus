package com.sriram.vidyarthibus

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sriram.vidyarthibus.adapter.AlternativeAdapter
import com.sriram.vidyarthibus.model.Alternative
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AlternativesActivity : AppCompatActivity() {

    private lateinit var adapter: AlternativeAdapter
    private val alternativesList = mutableListOf<Alternative>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alternatives)

        supportActionBar?.apply {
            title = "Shared Auto Contacts"
            setDisplayHomeAsUpEnabled(true)
        }

        val rvAlternatives: RecyclerView = findViewById(R.id.rvAlternatives)
        rvAlternatives.layoutManager = LinearLayoutManager(this)
        adapter = AlternativeAdapter(alternativesList)
        rvAlternatives.adapter = adapter

        fetchAlternativesFromFirebase()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun fetchAlternativesFromFirebase() {
        val database = FirebaseDatabase.getInstance().reference.child("alternatives")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                alternativesList.clear()
                for (altSnapshot in snapshot.children) {
                    val alt = altSnapshot.getValue(Alternative::class.java)
                    alt?.let { alternativesList.add(it) }
                }
                adapter.updateData(alternativesList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AlternativesActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}