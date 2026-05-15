package com.sriram.vidyarthibus

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val toolbar: Toolbar = findViewById(R.id.toolbarSplash)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        val spinnerCollege: Spinner = findViewById(R.id.spinnerCollege)
        val btnGetStarted: Button = findViewById(R.id.btnGetStarted)

        val colleges = listOf(
            "SIT, Mangaluru",
            "Sahyadri College of Engineering and Management, Mangaluru",
            "Canara College, Mangaluru"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colleges)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCollege.adapter = adapter

        btnGetStarted.setOnClickListener {
            val selectedCollege = spinnerCollege.selectedItem.toString()
            val intent = Intent(this, RouteListActivity::class.java)
            intent.putExtra("COLLEGE_NAME", selectedCollege)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
