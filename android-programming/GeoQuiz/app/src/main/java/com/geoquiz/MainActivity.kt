package com.geoquiz

import android.app.Activity
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.permissionx.geoquiz.R
import com.permissionx.geoquiz.databinding.ActivityCheatBinding
import com.permissionx.geoquiz.databinding.ActivityMainBinding

// const val means value has to be assigned during compile time
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityMainBinding

    private val quizViewModel : QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // to get references to each UI element in activity_main.xml layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.questionTextView.setOnClickListener { view: View ->
            updateQuestion()
            enableButtons()
        }

        binding.trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            disableButtons()
            displayGrade()
        }

        binding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            disableButtons()
            displayGrade()
        }

        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            enableButtons()
        }

        binding.prevButton.setOnClickListener {
//            Log.d("MainActivity", "$currentIndex")
            quizViewModel.moveToPrev()
            updateQuestion()
            enableButtons()
        }

        binding.cheatButton.setOnClickListener{
            // Start CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

        updateQuestion()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            blurCheatButton()
        }

    }

    private fun updateQuestion() {
        val questiontextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questiontextResId)
    }


    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        if (userAnswer == correctAnswer) {
            quizViewModel.correctNum++
        }
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId,Toast.LENGTH_SHORT).show()
    }

    private fun disableButtons() {
        binding.trueButton.setEnabled(false)
        binding.falseButton.setEnabled(false)
    }

    private fun enableButtons() {
        binding.trueButton.setEnabled(true)
        binding.falseButton.setEnabled(true)
    }

    private fun displayGrade() {
        if (quizViewModel.currentIndex == quizViewModel.questionBank.size - 1) {
            val grade = (quizViewModel.correctNum.toFloat() / quizViewModel.questionBank.size) * 100
            Toast.makeText(this, "You got $grade% of the questions right", Toast.LENGTH_SHORT).show()
            quizViewModel.correctNum = 0
        }
    }

    // use code from after API level 24
    // Lint will suggest add RequiresApi

    @RequiresApi(Build.VERSION_CODES.S)
    private fun blurCheatButton() {
        val effect = RenderEffect.createBlurEffect(
            10.0f,
            10.0f,
            Shader.TileMode.CLAMP
        )
        binding.cheatButton.setRenderEffect(effect)
    }
}