package vip.example.plank

import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TableRow
import android.widget.TextView
import android.widget.TableLayout

class ScoreCheckActivity : AppCompatActivity() {

    private val textView: TextView? = null
    private val editTextKey: EditText? = null
    private val editTextValue: EditText? = null
    private var helper: TestOpenHelper? = null
    private var db: SQLiteDatabase? = null

    private val cap: IntArray? = null
    private val caption: IntArray? = null
    private val haiten: IntArray? = null
    private val categoryPoint: IntArray? = null
    private val averagePoint: IntArray? = null
    private var categoryNum = 0
    private var dataNum = 0
    private val MP = TableLayout.LayoutParams.MATCH_PARENT
    private val WC = TableLayout.LayoutParams.WRAP_CONTENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scorecheck)

        //  textView = findViewById(R.id.text_view);
        readData()
        /**
         *
         * textView = findViewById(R.id.text_view);
         * readData();
         * Button readButton = findViewById(R.id.button_read);
         * readButton.setOnClickListener(new View.OnClickListener() {
         * @Override
         * public void onClick(View v) {
         *
         * if(helper == null){
         * helper = new TestOpenHelper(getApplicationContext());
         * }
         *
         * if(db == null){
         * db = helper.getWritableDatabase();
         * }
         * insertData(db, "2/1(例)", 100);
         * readData();
         * }
         * });
         */
    }


    private fun readData() {
        if (helper == null) {
            helper = TestOpenHelper(applicationContext)
        }

        if (db == null) {
            db = helper!!.readableDatabase
        }
        // Log.d("debug","**********Cursor");

        val cursor = db!!.query(
            "testdb",
            arrayOf("date", "score", "level", "sec"), null, null, null, null, null
        )

        cursor.moveToLast()

        val sbuilder = StringBuilder()

        val tableLayout = findViewById<View>(R.id.tableLayout) as TableLayout


        for (i in 1 until cursor.count + 1) {
            sbuilder.append(cursor.getString(0))
            sbuilder.append(": ")
            sbuilder.append(cursor.getInt(1))
            sbuilder.append("\n")

            //上四行使わん
            if (dataNum == 0) {//この中で表示
                val tableRow = layoutInflater.inflate(R.layout.table_row, null) as TableRow
                val name = tableRow.findViewById<View>(R.id.rowtext1) as TextView

                // name.setText(cap[i] + caption[i]);配列ないに何も入ってないからエラー出る
                name.text = cursor.getString(0)//日付
                val point = tableRow.findViewById<View>(R.id.rowtext2) as TextView

                //  point.setText(Integer.toString(haiten[i]));
                point.text = Integer.toString(cursor.getInt(1))//スコア
                val score = tableRow.findViewById<View>(R.id.rowtext3) as TextView
                //    score.setText(Integer.toString(categoryPoint[i]));
                score.text = cursor.getString(2)
                val ave = tableRow.findViewById<View>(R.id.rowtext4) as TextView
                // ave.setText(Integer.toString(averagePoint[i]));
                ave.text = Integer.toString(cursor.getInt(3))
                if ((i + 1) % 2 == 0) {
                    val color = resources.getColor(R.color.colorPrimary)
                    name.setBackgroundColor(color)
                    point.setBackgroundColor(color)
                    score.setBackgroundColor(color)
                    ave.setBackgroundColor(color)
                }

                tableLayout.addView(tableRow, TableLayout.LayoutParams(MP, WC))

            }
            categoryNum = cursor.count

            cursor.moveToPrevious()
        }

        dataNum = cursor.count
        // 忘れずに！
        cursor.close()

        Log.d("debug", "**********$sbuilder")
        // textView.setText(sbuilder.toString());
    }

    private fun insertData(db: SQLiteDatabase, com: String, price: Int) {

        val values = ContentValues()
        values.put("date", com)
        values.put("score", price)

        db.insert("testdb", null, values)

        val tableLayout = findViewById<View>(R.id.tableLayout) as TableLayout
        val tableRow = layoutInflater.inflate(R.layout.table_row, null) as TableRow
        val name = tableRow.findViewById<View>(R.id.rowtext1) as TextView

        // name.setText(cap[i] + caption[i]);配列ないに何も入ってないからエラー出る
        name.text = com
        val point = tableRow.findViewById<View>(R.id.rowtext2) as TextView

        //  point.setText(Integer.toString(haiten[i]));
        point.text = Integer.toString(price)
        val score = tableRow.findViewById<View>(R.id.rowtext3) as TextView
        //    score.setText(Integer.toString(categoryPoint[i]));
        score.text = "score111"
        val ave = tableRow.findViewById<View>(R.id.rowtext4) as TextView
        // ave.setText(Integer.toString(averagePoint[i]));
        ave.text = "ave"
        if ((dataNum + 1) % 2 == 0) {
            val color = resources.getColor(R.color.colorPrimary)
            name.setBackgroundColor(color)
            point.setBackgroundColor(color)
            score.setBackgroundColor(color)
            ave.setBackgroundColor(color)
        }
        dataNum++
        tableLayout.addView(tableRow, TableLayout.LayoutParams(MP, WC))
    }


}

