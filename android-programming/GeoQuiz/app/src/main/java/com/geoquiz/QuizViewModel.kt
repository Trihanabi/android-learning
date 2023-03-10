package com.geoquiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.permissionx.geoquiz.R

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val CURRENT_CORRECT_NUM = "CURRENT_CORRECT_NUM"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"

// SavedStateHandle like a key-value map
class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel(){
    val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    var currentIndex : Int
        // getter and setter
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)
    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)
    var correctNum : Int
        get() = savedStateHandle.get(CURRENT_CORRECT_NUM) ?: 0
        set(value) = savedStateHandle.set(CURRENT_CORRECT_NUM, value)

    val currentQuestionAnswer : Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText : Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        //fun: Log.d(String, String, Throwable)
//        Log.d(TAG, "Updating question text", Exception())
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = if (currentIndex == 0) {
            questionBank.size - 1
        } else {
            (currentIndex - 1) % questionBank.size
        }
    }
}