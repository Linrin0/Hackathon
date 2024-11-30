package com.example.proto01hukson2024;

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proto01hukson2024.R

class MainActivity : AppCompatActivity() {

    private lateinit var speechRecognizer: SpeechRecognizer
    private val recognizedTextArray = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startRecognitionButton: Button = findViewById(R.id.startRecognitionButton)

        // SpeechRecognizerを初期化
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        // SpeechRecognizerのリスナー設定
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Toast.makeText(this@MainActivity, "音声認識を開始します...", Toast.LENGTH_SHORT).show()
            }

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                Toast.makeText(this@MainActivity, "音声認識終了", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: Int) {
                Toast.makeText(this@MainActivity, "エラーが発生しました: $error", Toast.LENGTH_SHORT).show()
            }

            override fun onResults(results: Bundle?) {
                // 認識結果を取得
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    recognizedTextArray.addAll(matches)
                    Toast.makeText(this@MainActivity, "認識成功: ${matches[0]}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        // ボタンのクリックリスナー
        startRecognitionButton.setOnClickListener {
            // 音声認識インテントを作成
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ja-JP")
            }
            speechRecognizer.startListening(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }
}
