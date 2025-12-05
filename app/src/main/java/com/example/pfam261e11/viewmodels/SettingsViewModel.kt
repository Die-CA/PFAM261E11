package com.example.pfam261e11.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {

    private val _dailyNotificationsEnabled = MutableStateFlow(false)
    val dailyNotificationsEnabled: StateFlow<Boolean> = _dailyNotificationsEnabled

    fun setDailyNotifications(enabled: Boolean) {
        _dailyNotificationsEnabled.value = enabled
    }
}
