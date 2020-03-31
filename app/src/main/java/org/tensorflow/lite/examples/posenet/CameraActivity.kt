/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.posenet

import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity
import vip.example.plank.R

class CameraActivity : AppCompatActivity() {

  fun showHelp(){
    AlertDialog.Builder(this).apply {
      val imageView = ImageView(context)
      imageView.setImageResource(R.drawable.how2use)
      imageView.scaleType = ImageView.ScaleType.FIT_XY
      imageView.adjustViewBounds = true
      setView(  imageView )

      show()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    showHelp()

    setContentView(R.layout.activity_camera)
    savedInstanceState ?: supportFragmentManager.beginTransaction()
      .replace(R.id.container, PosenetActivity())
      .commit()

    val PhotoButton = findViewById<ImageButton>(R.id.helpPose)
    PhotoButton.setOnClickListener {
      showHelp()
    }

  }
}
