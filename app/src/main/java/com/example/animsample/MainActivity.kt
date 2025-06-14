package com.example.animsample

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val shipView: ImageView = findViewById(R.id.shipView)
        val shipAnim = AnimationUtils.loadAnimation(this, R.anim.ship_anim)
        shipView.startAnimation(shipAnim)
    }
}
