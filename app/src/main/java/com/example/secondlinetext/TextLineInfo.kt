package com.example.secondlinetext

import android.view.View
import android.widget.TextView

class TextLineInfo {
    /**
     * TextView의 자동 줄바꿈된 두 번째 줄의 첫 단어 정보를 찾는 함수
     * @param textView 대상 TextView
     * @param lineNumber 찾고자 하는 줄 번호 (기본값: 2)
     * @return 단어의 시작 위치와 해당 단어를 포함하는 Pair, 해당 줄이 없으면 null
     */
    fun findFirstWordOfWrappedLine(textView: TextView, lineNumber: Int = 2): Pair<Int, String>? {
        // 먼저 TextView의 레이아웃을 강제로 생성
        val layout = textView.layout ?: run {
            // layout이 null인 경우에만 ensureLayout 호출
            ensureLayout(textView)
            textView.layout
        } ?: return null

        val lineCount = layout.lineCount

        // 요청한 줄이 존재하는지 확인
        if (lineNumber <= 0 || lineNumber > lineCount) {
            return null
        }

        // 해당 줄의 시작과 끝 위치를 가져오기
        val targetLine = lineNumber - 1  // 0-based index로 변환
        val lineStart = layout.getLineStart(targetLine)
        val lineEnd = layout.getLineEnd(targetLine)

        val text = textView.text.toString()

        // 줄의 텍스트를 가져와서 앞뒤 공백 제거
        var lineText = text.substring(lineStart, lineEnd).trim()

        // 첫 번째 단어 찾기
        val words = lineText.split("\\s+".toRegex())
        if (words.isEmpty()) return null

        val firstWord = words[0]
        // 줄 시작부터 첫 단어까지의 offset 계산
        val wordStartPosition = lineStart + lineText.indexOf(firstWord)

        return Pair(wordStartPosition, firstWord)
    }

    /**
     * TextView의 레이아웃을 강제로 생성하는 함수
     */
    private fun ensureLayout(textView: TextView) {
        if (textView.width <= 0) {
            // 부모 뷰의 너비를 가져오거나 기본값 설정
            val parentWidth = (textView.parent as? View)?.width ?: 1080
            val widthSpec = View.MeasureSpec.makeMeasureSpec(
                parentWidth - textView.paddingLeft - textView.paddingRight,
                View.MeasureSpec.AT_MOST
            )
            val heightSpec = View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
            textView.measure(widthSpec, heightSpec)
        }

        if (textView.layout == null) {
            textView.layout(0, 0, textView.measuredWidth, textView.measuredHeight)
        }
    }
}