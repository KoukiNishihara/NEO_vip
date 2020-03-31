
# PlankZER0-N
[Android端末にインストール](https://play.google.com/store/apps/details?id=vip.example.plank)



## 背景
「場所をとらず、一人ででき、負荷が比較的軽く、継続しやすい」初心者向けの筋トレの一つにプランクトレーニングがあります。
しかし、このプランクトレーニングをする際には、一人で筋トレをすると正しい姿勢で筋トレを行えているのか、効果のある筋トレをしているか分からないといった問題があります。これらの問題を解決するために、画像処理やジャイロセンサーを用いて姿勢を計測・推定します。


## 機能
+ ジャイロセンサーを用いることで、微小な体の動きを検知できます。これにより、より正確に姿勢を計測することができ、初心者だけでなく上級者にもしっかり負荷のかかったトレーニングをすることができます。
+ 画像処理のモードでは、姿勢推定機能があります。PoseNetと呼ばれる機械学習のモデルを用いて、リアルタイムで体の部位ごとの座標を取得することができます。これにより、コンピュータで姿勢の正誤の判別することができ、ユーザが正しい姿勢で筋トレできているか知ることができます。
+ その他の機能として、グラフやデータベースを用いて過去の記録を保存し、振り返ることができます。これにより、ユーザーが成長を感じることができモチベーションを保ことができます。




## LICENSE
このアプリには[tensorflow/examples](https://github.com/tensorflow/examples)を使用しています

## 備考
[チーム開発](https://github.com/Nishikoh/PlankZer0/tree/master)から派生して個人で開発


## Build the demo using Android Studio

### Prerequisites

* If you don't have it already, install **[Android Studio](
 https://developer.android.com/studio/index.html)** 3.2 or
 later, following the instructions on the website.

* Android device and Android development environment with minimum API 21.

### Building
* Open Android Studio, and from the `Welcome` screen, select
`Open an existing Android Studio project`.

* From the `Open File or Project` window that appears, navigate to and select
 the `PlankZer0` directory from wherever you
 cloned the PlankZer0 GitHub repo. Click `OK`.

* If it asks you to do a `Gradle Sync`, click `OK`.

* You may also need to install various platforms and tools, if you get errors
 like `Failed to find target with hash string 'android-21'` and similar. Click
 the `Run` button (the green arrow) or select `Run` > `Run 'android'` from the
 top menu. You may need to rebuild the project using `Build` > `Rebuild Project`.

* If it asks you to use `Instant Run`, click `Proceed Without Instant Run`.

* Also, you need to have an Android device plugged in with developer options
 enabled at this point. See **[here](
 https://developer.android.com/studio/run/device)** for more details
 on setting up developer devices.


### Model used
Downloading, extraction and placement in assets folder has been managed
 automatically by `download.gradle`.

If you explicitly want to download the model, you can download it from
 **[here](
 https://storage.googleapis.com/download.tensorflow.org/models/tflite/posenet_mobilenet_v1_100_257x257_multi_kpt_stripped.tflite)**.

### Additional Note
_Please do not delete the assets folder content_. If you explicitly deleted the
 files, then please choose `Build` > `Rebuild` from menu to re-download the
 deleted model files into assets folder.
