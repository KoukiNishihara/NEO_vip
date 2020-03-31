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
            // show dialog
            val fragmentManager = fragmentManager

            // DialogFragment を継承したAlertDialogFragmentのインスタンス
            val dialogFragment = MainActivity.AlertDialogFragment()
            // DialogFragmentの表示
            dialogFragment.show(fragmentManager, "test alert dialog")
        }

        val imageView = findViewById<ImageView>(R.id.image_view)
        //        imageView.setImageResource(R.drawable.plank01);

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

        // ボタンタップでAlertを表示させる
        helpButton.setOnClickListener {
            val fragmentManager = fragmentManager

            // DialogFragment を継承したAlertDialogFragmentのインスタンス
            val dialogFragment = MainActivity.AlertDialogFragment()
            // DialogFragmentの表示
            dialogFragment.show(fragmentManager, "test alert dialog")
        }

        //アンケートに移動
        val QuestionnaireButton = findViewById<ImageButton>(R.id.QuestionnaireButton)
        QuestionnaireButton.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/Zaq9huVe8jLWLCKG7"))
            startActivity(intent)
        }

    }

    fun setTextView(message: String) {

    }

    // DialogFragment を継承したクラス
    class AlertDialogFragment : DialogFragment() {
        // 選択肢のリスト
        private val menulist = arrayOf("選択(1)", "選択(2)", "選択(3)")

        @RequiresApi(api = Build.VERSION_CODES.M)
        override fun onCreateDialog(savedInstanceState: Bundle): Dialog {

            val alert = AlertDialog.Builder(activity)

            // タイトル
            alert.setTitle("このアプリの使い方")
            alert.setView(R.layout.activity_help_main)
            alert.setPositiveButton("OK", null)

            return alert.create()
        }

        private fun setMassage(message: String) {
            val mainActivity = activity as MainActivity
            // mainActivity.setTextView(message);
        }
    }

}

