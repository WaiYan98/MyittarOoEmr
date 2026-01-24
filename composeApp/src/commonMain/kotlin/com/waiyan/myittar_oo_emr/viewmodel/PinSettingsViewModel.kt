package com.waiyan.myittar_oo_emr.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyan.myittar_oo_emr.data.SettingsStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PinSettingsViewModel(
    private val settingsStorage: SettingsStorage
) : ViewModel() {

    private val PIN_KEY = "report_screen_pin"

    private val _isPinSet = MutableStateFlow(false)
    val isPinSet: StateFlow<Boolean> = _isPinSet.asStateFlow()

    private val _isUnlocked = MutableStateFlow(false)
    val isUnlocked: StateFlow<Boolean> = _isUnlocked.asStateFlow()

    init {
        checkPinStatus()
    }

    private fun checkPinStatus() {
        viewModelScope.launch {
            _isPinSet.value = settingsStorage.getString(PIN_KEY, "").isNotEmpty()
        }
    }

    fun setPin(pin: String) {
        viewModelScope.launch {
            settingsStorage.putString(PIN_KEY, pin)
            _isPinSet.value = true
        }
    }

    fun verifyPin(pin: String): Boolean {
        val storedPin = settingsStorage.getString(PIN_KEY, "")
        val result = storedPin == pin
        _isUnlocked.value = result
        return result
    }

    fun lockScreen() {
        _isUnlocked.value = false
    }
}
