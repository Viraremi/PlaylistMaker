package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val button_settings_back = findViewById<ImageView>(R.id.button_settings_back)
        button_settings_back.setOnClickListener{
            val backIntent = Intent(this@SettingsActivity, MainActivity::class.java)
            startActivity(backIntent)
        }
    }



}