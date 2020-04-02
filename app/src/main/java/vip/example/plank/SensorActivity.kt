package vip.example.plank

//AndroidX

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import android.widget.ImageButton
import android.content.Context

class SensorActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_sensor)



        if (isFirstTime) {
            val sp = getSharedPreferences("DataStore", Context.MODE_PRIVATE)
            val editor = sp.edit()

            val No1 = 0
            val No2 = 0
            val No3 = 0
            editor.putInt("int_No1", No1) // int_1というキーに i の中身(2)を設定
            editor.putInt("int_No2", No2) // int_1というキーに i の中身(2)を設定
            editor.putInt("int_No3", No3) // int_1というキーに i の中身(2)を設定
            editor.commit() // ここで実際にファイルに保存

            AlertDialog.Builder(this)
                .setTitle("使い方！")
                .setView(R.layout.activity_help1)
                .setPositiveButton( "OK", null )
                .show()

        }

        //初心者モード画面に遷移
        val BiginnerModeButton = findViewById<Button>(R.id.biginnermode_button)
        BiginnerModeButton.setOnClickListener {
            val intent = Intent(application, BiginnerActivity::class.java)
            startActivity(intent)
        }


        //中級者モード画面に遷移
        val IntermediateModeButton = findViewById<Button>(R.id.intermediatemode_button)
        IntermediateModeButton.setOnClickListener {
            val intent = Intent(application, IntermediateActivity::class.java)
            startActivity(intent)
        }


        //上級者モード画面に遷移
        val AdvancedModeButton = findViewById<Button>(R.id.advancedmode_button)
        AdvancedModeButton.setOnClickListener {
            val intent = Intent(application, AdvancedActivity::class.java)
            startActivity(intent)
        }

        //日々の記録画面に遷移
        val ScoreCheckModeButton = findViewById<ImageButton>(R.id.ScoreCheckButton)
        ScoreCheckModeButton.setOnClickListener {
            val intent = Intent(application, ScoreCheckActivity::class.java)
            startActivity(intent)
        }


        val helpButton = findViewById<ImageButton>(R.id.helpButton)

        // ボタンタップでAlertを表示させる
        helpButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("使い方！")
                .setView(R.layout.activity_help1)
                .setPositiveButton( "OK", null )
                .show()
        }
    }


}