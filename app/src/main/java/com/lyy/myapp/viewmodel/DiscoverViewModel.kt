package com.lyy.myapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyy.myapp.data.network.IPv6Addresses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class DiscoverState(
    val ipv6Addresses: List<String> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class DiscoverViewModel : ViewModel() {

    private val _state = MutableStateFlow(DiscoverState())
    val state: StateFlow<DiscoverState> = _state

    // 加载 IPv6 地址
    fun loadIPv6Addresses() {
        // 设置加载状态
        _state.value = DiscoverState(isLoading = true)

        viewModelScope.launch {
            try {
                val addresses = withContext(Dispatchers.IO) {
                    IPv6Addresses.getIPv6Addresses()
                }
                // 更新状态
                _state.value = DiscoverState(ipv6Addresses = addresses, isLoading = false)
            } catch (e: Exception) {
                _state.value = DiscoverState(
                    errorMessage = "获取 IPv6 地址时出错: ${e.message}",
                    isLoading = false
                )
            }
        }
    }
}
