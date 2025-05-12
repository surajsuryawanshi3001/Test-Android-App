package com.example.testapplication.ui.theme.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplication.generateRandomString
import com.example.testapplication.getCurrentTime
import com.example.testapplication.ui.theme.model.GeneratedString
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RandomStringViewModel :  ViewModel() {
    private val _generatedStrings = MutableStateFlow<List<GeneratedString>>(emptyList())
    val generatedStrings = _generatedStrings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun addString(string: GeneratedString) {
        _generatedStrings.value = _generatedStrings.value + string
    }

    fun deleteString(index: Int) {
        _generatedStrings.value = _generatedStrings.value.toMutableList().apply { removeAt(index) }
    }

    fun clearStrings() {
        _generatedStrings.value = emptyList()
    }

    fun generateString(length: Int) {
        try {
            viewModelScope.launch {
                _isLoading.value = true
                delay(1000)
                val randomStr = generateRandomString(length)
                addString(GeneratedString(randomStr, length, getCurrentTime()))
                _isLoading.value = false
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }

    }

}