package com.example.secondlinetext

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textView = findViewById<TextView>(R.id.textview)

        // 긴 텍스트 설정 (줄바꿈 문자 없음)
        textView.text = "1. 이것은 매우 긴 텍스트입니다 TextView의 너비에 따라 자동으로 줄바꿈이 발생하게 됩니다 이런 경우에도 정확하게 두 번째 줄의 첫 단어를 찾을 수 있습니다"

        // TextView 너비 설정
//        textView.layoutParams = textView.layoutParams.apply {
//            width = resources.displayMetrics.widthPixels / 2  // 화면 너비의 절반으로 설정
//        }

        // 결과 확인
        textView.post {
            val textLineInfo = TextLineInfo()
            val result = textLineInfo.findFirstWordOfWrappedLine(textView)

            result?.let { (position, word) ->
                Log.d("TextLineInfo", "두 번째 줄의 첫 단어 '${word}'는 ${position}번째 위치에서 시작합니다")

                // 시각적으로 표시
                val spannableString = SpannableString(textView.text)
                spannableString.setSpan(
                    BackgroundColorSpan(Color.YELLOW),
                    position,
                    position + word.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                textView.text = spannableString
            }

            // 디버깅을 위한 전체 줄 정보 출력
            val layout = textView.layout
            for (i in 0 until layout.lineCount) {
                val start = layout.getLineStart(i)
                val end = layout.getLineEnd(i)
                Log.d("TextLineInfo", "${i + 1}번째 줄: ${textView.text.substring(start, end)}")
            }
        }
    }
}