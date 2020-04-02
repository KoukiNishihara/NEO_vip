package vip.example.plank

import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private// first time
    val isFirstTime: Boolean
        get() {
            val preferences = getPreferences(Context.MODE_PRIVATE)
            val ranBefore = preferences.getBoolean("RanBefore", false)
            if (!ranBefore) {
                val editor = preferences.edit()
                editor.putBoolean("RanBefore", true)
                editor.commit()
            }
            return !ranBefore
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Log.d("debug", "onCreate()")//10/2追加
        setContentView(R.layout.activity_main)

        if (isFirstTime) {
            AlertDialog.Builder(this)
                .setTitle("使い方！")
                .setView(R.layout.activity_help_main)
                .setPositiveButton( "OK", null )
                .show()
        }


        //カメラモードに移動
        val PhotoButton = findViewById<ImageButton>(R.id.ViewImg)
        PhotoButton.setOnClickListener {
            val intent = Intent(application, ImageActivity::class.java)
            startActivity(intent)
        }

        //センサーようの移動ボタン
        val sendButton_sensor = findViewById<ImageButton>(R.id.sensor_button)
        sendButton_sensor.setOnClickListener {
            val intent = Intent(application, SensorActivity::class.java)
            startActivity(intent)
        }

        val helpButton = findViewById<ImageButton>(R.id.helpButton)

        // ボタンタップでhelpを表示させる
        helpButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("使い方！")
                .setView(R.layout.activity_help_main)
                .setPositiveButton( "OK", null )
                .show()
        }


        //アンケートに移動
        val QuestionnaireButton = findViewById<ImageButton>(R.id.QuestionnaireButton)
        QuestionnaireButton.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/Zaq9huVe8jLWLCKG7"))
            startActivity(intent)
        }

    }



}

