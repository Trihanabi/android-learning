package com.geoquiz
import androidx.annotation.StringRes;

// data: to show it is a model data and denfine func equals(), hashCode(), toString()...
// StringRes: verify if string resource ID
data class Question(@StringRes val textResId: Int, val answer: Boolean){
}