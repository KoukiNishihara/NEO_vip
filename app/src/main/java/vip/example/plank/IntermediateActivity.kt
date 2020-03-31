package vip.example.plank

import android.content.ContentValues
import android.content.pm.ActivityInfo
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
//import android.support.v7.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase
import android.os.CountDownTimer
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale
import android.os.Handler
import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.components.XAxis
import java.util.*
import java.lang.*

import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context


class IntermediateActivity : AppCompatActivity(), SensorEventListener {


    private var helper: TestOpenHelper? = null
    private var db: SQLiteDatabase? = null
    private val df = SimpleDateFormat("yyyy/MM/dd ")
    private var sensorManager: SensorManager? = null
    private var timerText: TextView? = null//タイマーの表示ぶん
    private var timerText_trainig:TextView? = null
    private val dataFormat =
        SimpleDateFormat("mm:ss", Locale.US)//https://akira-watson.com/android/countdowntimer.html
    //"mm:ss.SSS", Locale.US
    private var textView: TextView? = null
    private val textInfo: TextView? = null
    private var soundPool: SoundPool? = null
    private var soundOne: Int = 0
    private var soundTwo: Int = 0
    private var soundThree: Int = 0
    private var soundFour: Int = 0
    internal var nextX = 0f
    internal var nextY = 0f
    internal var nextZ = 0f
    private val time: Long = 10000
    private var first = 0
    private var FirstX: Float = 0.toFloat()
    private var FirstY: Float = 0.toFloat()
    private var FirstZ = 0f
    private var frag = 0
    private var timing = 0
    private var stop_count = 0
    private var all_count = 0.0
    private var move_frag = 0
    internal val handler = Handler()
    private var set_frag = 1
    private var setCount: TextView? = null

    private var totalscore = 0
    private var totalmil = 0.0

    private var delay: Runnable? = null
    private var delayStartCountDown: Runnable? = null
    private var enablestart: Runnable? = null

    // 3分= 3x60x1000 = 180000 msec
    internal var countNumber: Long = 30000
    //スタート前
    internal var countbefore: Long = 10000
    // インターバル msec
    internal var interval: Long = 10
    internal val countDown = CountDown(countNumber, interval)
    internal lateinit var startButton: Button
    internal lateinit var stopButton: Button
    internal lateinit var setCountButton: Button
    //private Runnable;

    private val accel: Sensor? = null
    private var textGraph: TextView? = null
    private var mChart: LineChart? = null
    private val labels = arrayOf("姿勢の揺れ度", "Y軸の揺れ", "Z軸の揺れ")
    private val colors = intArrayOf(Color.BLUE, Color.GRAY, Color.MAGENTA)
    private val lineardata = true


    private var No1: Int = 0
    private var No2: Int = 0
    private var No3: Int = 0

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intermediate)


        val sp = getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        val editor = sp.edit()
        No1 = sp.getInt("int_No1", 0)
        No2 = sp.getInt("int_No2", 0)
        No3 = sp.getInt("int_No3", 0)


        //レイアウト関連
        startButton = findViewById(R.id.start_button)//タイマーのボタン
        stopButton = findViewById(R.id.stop_button)//タイマーのボタン
        timerText = findViewById<TextView>(R.id.timer)
        setCountButton = findViewById(R.id.set_button)
        timerText_trainig = findViewById<TextView>(R.id.timer_training)
        timerText!!.setText(dataFormat.format(10000))
        timerText_trainig!!.setText(dataFormat.format(countNumber))
        textView = findViewById(R.id.text_view)
        textView!!.text = "トレーニングスコア：" + 0
        textGraph = findViewById(R.id.text_view)
        setCount = findViewById(R.id.settime)
        setCount!!.text = "×" + set_frag + "セット"

        val countDown_before = CountDown(countbefore, interval)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Get an instance of the SensorManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        mChart = findViewById(R.id.chart)
        // インスタンス生成
        mChart!!.data = LineData()
        // no description text
        mChart!!.description.isEnabled = false
        // Grid背景色
        mChart!!.setDrawGridBackground(true)
        // 右側の目盛り
        mChart!!.axisRight.isEnabled = false

        // Grid縦軸を破線 x軸に関する処理

        val xAxis = mChart!!.xAxis
        xAxis.enableGridDashedLine(10f, 10f, 0f)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.isEnabled = false//x軸のラベル消す

        //Y軸関連
        val leftAxis = mChart!!.axisLeft
        leftAxis.textColor = Color.BLACK
        leftAxis.setAxisMaxValue(4.0f)
        leftAxis.setAxisMinValue(-4.0f)
        leftAxis.setDrawGridLines(true)

        // Get an instance of the SensorManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // textInfo = findViewById(R.id.text_info);

        //スタートボタンの処理
        startButton.setOnClickListener {
            handler.removeCallbacks(delayStartCountDown!!)
            handler.removeCallbacks(delay!!)

            totalscore = 0
            totalmil = 0.0

            val fragmentManager2 = fragmentManager
            val dialogFragment_setpoketto = BiginnerActivity.AlertDialogFragment_setpoketto()
            // DialogFragmentの表示
            dialogFragment_setpoketto.show(fragmentManager2, "setpoketto alert dialog")
            startButton.isEnabled = false
            countDown_before.start()
            // timing =1;
            delayStartCountDown = Runnable{
                timing = 1
                if (timing == 1) {
                    soundPool!!.play(soundFour, 1.0f, 1.0f, 0, 0, 1f)
                }
            }

            enablestart = Runnable {
                startButton.isEnabled = false
            }

            delay = Runnable{
                mChart!!.data = LineData()
                soundPool!!.play(soundFour, 1.0f, 1.0f, 0, 0, 1f)
                first = 1
                frag = 1
                timing = 1
                countDown.start()
                timing = 0
                startButton.isEnabled = false
            }
                // }
            handler.postDelayed(delayStartCountDown!!, 7000)//遅延実行
            handler.postDelayed(delayStartCountDown!!, 8000)//遅延実行
            handler.postDelayed(delayStartCountDown!!, 9000)//遅延実行
            handler.postDelayed(delay!!, 10001)//遅延実行
            val set_frag_c = set_frag
            val xx = 0
            while (set_frag > 1) {
                handler.postDelayed(enablestart!!, ((set_frag - 1) * 40000 + 100).toLong())
                handler.postDelayed(
                    delayStartCountDown!!,
                    ((set_frag - 1) * 40000 + 7000).toLong()
                )//遅延実行
                handler.postDelayed(
                    delayStartCountDown!!,
                    ((set_frag - 1) * 40000 + 8000).toLong()
                )//遅延実行
                handler.postDelayed(
                    delayStartCountDown!!,
                    ((set_frag - 1) * 40000 + 9000).toLong()
                )//遅延実行
                handler.postDelayed(delay!!, ((set_frag - 1) * 40000 + 10001).toLong())//遅延実行
                set_frag--
            }
            set_frag = set_frag_c
            setCount!!.text = "×" + set_frag + "セット"
        }
        //ストップボタンの処理
        stopButton.setOnClickListener {
            startButton.isEnabled = true
            if (frag == 1) {
                countDown.cancel()
            }
            if (frag == 0) {
                countDown_before.cancel()
            }
            handler.removeCallbacks(delay!!)
            frag = 0
            timing = 0
            handler.removeCallbacks(delayStartCountDown!!)
            handler.removeCallbacks(delay!!)
            timerText!!.setText(dataFormat.format(10000))
            timerText_trainig!!.setText(dataFormat.format(countNumber))

            stop_count = 0
            all_count = 0.0
            setCountButton.isEnabled = true
            set_frag = 1
            setCount!!.text = "×" + set_frag + "セット"
        }


        val returnButton_sensor = findViewById<Button>(R.id.return_button_sensor)
        returnButton_sensor.setOnClickListener {
            if (frag == 1) {
                countDown.cancel()
            }
            if (frag == 0) {
                countDown_before.cancel()
            }
            handler.removeCallbacks(delay!!)
            frag = 0
            timing = 0
            handler.removeCallbacks(delayStartCountDown!!)
            handler.removeCallbacks(delay!!)
            finish()
        }


        setCountButton.setOnClickListener {
            set_frag += 1
            setCount!!.text = "×" + set_frag + "セット"
            if (set_frag == 9) {
                setCountButton.isEnabled = false
            }
        }

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()

        soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            // ストリーム数に応じて
            .setMaxStreams(2)
            .build()


        soundOne = soundPool!!.load(this, R.raw.one, 1)
        soundTwo = soundPool!!.load(this, R.raw.two, 1)
        soundThree = soundPool!!.load(this, R.raw.three, 1)
        soundFour = soundPool!!.load(this, R.raw.four, 1)

        // load が終わったか確認する場合
        soundPool!!.setOnLoadCompleteListener { soundPool, sampleId, status ->
            Log.d("debug", "sampleId=$sampleId")
            Log.d("debug", "status=$status")
        }

    }

    override fun onResume() {
        super.onResume()
        // Listenerの登録
        val accel = sensorManager!!.getDefaultSensor(
            Sensor.TYPE_ACCELEROMETER
        )

        sensorManager!!.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onBackPressed() {
        // 行いたい処理
        frag = 0
        timing = 2
        handler.removeCallbacks(delayStartCountDown!!)
        handler.removeCallbacks(delay!!)
        finish()
    }

    // 解除するコードも入れる!
    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }


    override fun onSensorChanged(event: SensorEvent) {
        val sensorX: Float
        val sensorY: Float
        val sensorZ: Float
        val gravity = FloatArray(3)
        val linear_acceleration = FloatArray(1)
        //final float alpha = 0.5f;
        if (first == 1) {
            FirstX = event.values[0]
            FirstY = event.values[1]
            FirstZ = event.values[2]
            first = 0
        }

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            sensorX = event.values[0]
            sensorY = event.values[1]
            sensorZ = event.values[2]


            if (frag == 1) {
                if (FirstZ - nextZ < -1 || FirstZ - nextZ > 1) {
                    soundPool!!.play(soundOne, 1.0f, 1.0f, 0, 0, 1f)
                    move_frag = 1
                } else if (FirstX - nextX < -1 || FirstX - nextX > 1) {
                    soundPool!!.play(soundOne, 1.0f, 1.0f, 0, 0, 1f)
                    move_frag = 1
                } else if (FirstY - nextY < -1 || FirstY - nextY > 1) {
                    soundPool!!.play(soundOne, 1.0f, 1.0f, 0, 0, 1f)
                    move_frag = 1
                } else {
                    move_frag = 0
                }

                gravity[0] = FirstX - nextX
                gravity[1] = FirstY - nextY
                gravity[2] = FirstZ - nextZ

                val x = Math.max(gravity[0], gravity[1])
                val y = Math.max(x, gravity[2])

                val x2 = Math.min(gravity[0], gravity[1])
                val y2 = Math.min(gravity[0], gravity[1])
                if (Math.abs(y) > Math.abs(y2)) {
                    linear_acceleration[0] = y
                } else {
                    linear_acceleration[0] = y2
                }


                val accelero: String


                val data = mChart!!.lineData

                if (data != null) {
                    for (i in 0..0) {
                        var set3: ILineDataSet? = data.getDataSetByIndex(i)
                        if (set3 == null) {
                            val set = LineDataSet(null, labels[i])
                            set.lineWidth = 2.0f
                            set.color = colors[i]
                            // liner line
                            set.setDrawCircles(false)
                            // no values on the chart
                            set.setDrawValues(false)
                            set3 = set
                            data.addDataSet(set3)
                        }

                        // data update
                        if (!lineardata) {
                            data.addEntry(Entry(set3.entryCount.toFloat(), event.values[i]), i)
                        } else {
                            data.addEntry(
                                Entry(set3.entryCount.toFloat(), linear_acceleration[i]),
                                i
                            )
                        }

                        data.notifyDataChanged()
                    }


                    mChart!!.notifyDataSetChanged() // 表示の更新のために変更を通知する
                    mChart!!.setVisibleXRangeMaximum(180f) // 表示の幅を決定する
                    mChart!!.moveViewToX(data.entryCount.toFloat()) // 最新のデータまで表示を移動させる
                }
            }
            nextX = sensorX
            nextY = sensorY
            nextZ = sensorZ


        }

    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }

    internal inner class CountDown(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {

        val nowDate: String
            get() {
                val date = Date(System.currentTimeMillis())
                return df.format(date)
            }

        override fun onFinish() {
            // 完了

            timerText!!.setText(dataFormat.format(10000))
            timerText_trainig!!.setText(dataFormat.format(countNumber))
            frag = 0
            var x = 100 * stop_count / all_count
            x = Math.floor(x)
            val mil = all_count * 1000 / countNumber
            val mil_count = stop_count / mil

            if (timing == 1) {
                startButton.isEnabled = true
            } else {
                // startButton.setEnabled(true);
                textView!!.setTextColor(Color.RED)

                if (set_frag > 1) {

                    totalmil += mil_count
                    totalscore += stop_count * 2
                    textView!!.text =
                        "合計スコア：" + totalscore + "\n" + totalmil.toInt().toString() + "秒キープできたよ！"
                } else {


                    if (No1 < stop_count * 2) {
                        val sp = getSharedPreferences("DataStore", Context.MODE_PRIVATE)
                        val editor = sp.edit()
                        No3 = No2
                        No2 = No1
                        No1 = stop_count * 2

                        editor.putInt("int_No1", No1) // int_1というキーに i の中身(2)を設定
                        editor.putInt("int_No2", No2) // int_1というキーに i の中身(2)を設定
                        editor.putInt("int_No3", No3) // int_1というキーに i の中身(2)を設定
                        editor.commit() // ここで実際にファイルに保存
                    } else if (No2 < stop_count * 2) {
                        val sp = getSharedPreferences("DataStore", Context.MODE_PRIVATE)
                        val editor = sp.edit()
                        No3 = No2
                        No2 = stop_count * 2

                        editor.putInt("int_No2", No2) // int_1というキーに i の中身(2)を設定
                        editor.putInt("int_No3", No3) // int_1というキーに i の中身(2)を設定
                        editor.commit()
                    } else if (No3 < stop_count * 2) {
                        val sp = getSharedPreferences("DataStore", Context.MODE_PRIVATE)
                        val editor = sp.edit()
                        No3 = stop_count * 2
                        editor.putInt("int_No3", No3) // int_1というキーに i の中身(2)を設定
                        editor.commit()
                    }

                    textView!!.text =
                        "ランキング！\n 1位:" + No1 + "\n 2位:" + No2 + "\n 3位:" + No3 + "\n\nトレーニングスコア：" + stop_count * 2 + "\n今回は" + mil_count.toInt().toString() + "秒キープできたよ！"
                }
                if (helper == null) {
                    helper = TestOpenHelper(applicationContext)
                }

                if (db == null) {
                    db = helper!!.writableDatabase
                }

                //String key = editTextKey.getText().toString();
                //String value = editTextValue.getText().toString();

                //insertData(db, key, Integer.valueOf(value));
                //現在の時刻
                val date = nowDate
                //return df.format(date);
                insertData(db!!, date, stop_count * 2, mil_count)
            }
            stop_count = 0
            all_count = 0.0

            if (mil_count > 28) {
                val fragmentManager = fragmentManager
                val dialogFragment = AlertDialogFragment()
                // DialogFragmentの表示
                dialogFragment.show(fragmentManager, "test alert dialog")
            }
            stop_count = 0
            all_count = 0.0

            if (timing == 2) {
            } else {
                soundPool!!.play(soundFour, 1.0f, 1.0f, 0, 0, 1f)
            }
        }

        //追加 dbにかきこみ
        private fun insertData(db: SQLiteDatabase, date: String, score: Int, sec: Double) {

            val values = ContentValues()
            values.put("date", date)
            values.put("score", score)
            values.put("level", "中級者")
            values.put("sec", sec)
            db.insert("testdb", null, values)
        }

        // インターバルで呼ばれる
        override fun onTick(millisUntilFinished: Long) {
            // 残り時間を分、秒、ミリ秒に分割
            //long mm = millisUntilFinished / 1000 / 60;
            //long ss = millisUntilFinished / 1000 % 60;
            //long ms = millisUntilFinished - ss * 1000 - mm * 1000 * 60;
            //timerText.setText(String.format("%1$02d:%2$02d.%3$03d", mm, ss, ms));
            if (millisUntilFinished > 10000) {
                frag = 1
            }

            if (frag == 0) {
                timerText!!.setText(dataFormat.format(millisUntilFinished))
            }
            if (frag == 1) {
                timerText_trainig!!.setText(dataFormat.format(millisUntilFinished))
                var x = 100 * stop_count / all_count
                x = Math.floor(x)
                textView!!.setTextColor(Color.BLUE)
                textView!!.text = "トレーニングスコア：" + stop_count * 2

                all_count++
                if (move_frag == 0) {
                    stop_count++
                }
            }
        }

    }

    class AlertDialogFragment : DialogFragment() {
        // 選択肢のリスト
        private val menulist = arrayOf("選択(1)", "選択(2)", "選択(3)")

        @RequiresApi(api = Build.VERSION_CODES.M)
        override fun onCreateDialog(savedInstanceState: Bundle): Dialog {

            val alert = AlertDialog.Builder(activity)

            val imageView = ImageView(context)
            imageView.setImageResource(R.drawable.level_up2)
            // タイトル
            alert.setTitle("上の難易度を目指しましょう！")
            alert.setView(imageView)
            alert.setPositiveButton("OK", null)
            //alert.show();
            //alert.setItems(menulist, new DialogInterface.OnClickListener() {
            // @Override
            //public void onClick(DialogInterface dialog, int idx) {

            //    }
            //}
            // });

            return alert.create()
        }

        private fun setMassage(message: String) {
            val mainActivity = activity as MainActivity
            // mainActivity.setTextView(message);
        }
    }


    class AlertDialogFragment_setpoketto : DialogFragment() {

        @RequiresApi(api = Build.VERSION_CODES.M)
        override fun onCreateDialog(savedInstanceState: Bundle): Dialog {

            val alert = AlertDialog.Builder(activity)

            val imageView = ImageView(context)
            imageView.setImageResource(R.drawable.poket)
            // タイトル
            alert.setTitle("ポケットか背中にスマホを入れましょう！")
            alert.setView(imageView)
            alert.setPositiveButton("OK", null)


            return alert.create()
        }

        private fun setMassage(message: String) {
            val mainActivity = activity as MainActivity
            // mainActivity.setTextView(message);
        }
    }


}
