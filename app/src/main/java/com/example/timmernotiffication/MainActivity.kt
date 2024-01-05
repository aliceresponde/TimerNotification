package com.example.timmernotiffication

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.timmernotiffication.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var notiTimer: NotificationTimer.Builder
    lateinit var binding: ActivityMainBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Sets up permissions request launcher.
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (!it) {
                    Snackbar.make(
                        binding.root,
                        "Please grant Notification permission from App Settings",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val pendingIntent = Intent(this, MainActivity::class.java).let {
            PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        binding.apply {
            buildBtn.setOnClickListener {
                notiTimer = NotificationTimer.Builder(this@MainActivity)
                    .setSmallIcon(R.drawable.ic_timer)
                    .setPlayButtonIcon(R.drawable.ic_play_noti)
                    .setPauseButtonIcon(R.drawable.ic_pause_noti)
                    .setStopButtonIcon(R.drawable.ic_stop_noti)
                    .setControlMode(true)
                    .setColor(R.color.sexy_blue)
                    .setShowWhen(false)
                    .setAutoCancel(false)
                    .setOnlyAlertOnce(true)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentIntent(pendingIntent)
                    .setOnTickListener { timeUntilFinishText.text = it.toString() }
                    .setOnFinishListener {
                        Toast.makeText(this@MainActivity, "timer finished", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .setOnRequestPermissionListener {
                        requestPermissionLauncher.launch(POST_NOTIFICATIONS)
                    }
                    .setContentTitle("Timer :)")
            }

            playBtn.setOnClickListener {
                val timerTime = timeEditText.text.toString().toLong()
                notiTimer.play(timerTime)
            }

            pauseBtn.setOnClickListener {
                notiTimer.pause()
            }

            stopBtn.setOnClickListener {
                notiTimer.stop()
                timeUntilFinishText.text = null
            }

            terminateBtn.setOnClickListener {
                notiTimer.terminate()
            }
        }
    }
}