package com.hanikorm.spacehub2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hanikorm.spacehub2.data.repository.ApodRepository
import com.hanikorm.spacehub2.domain.model.ApodEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ApodViewModel(
    private val repository: ApodRepository
) : ViewModel() {

    // STATE
    private val _items = MutableStateFlow<List<ApodEntry>>(emptyList())
    val items: StateFlow<List<ApodEntry>> = _items.asStateFlow()

    init {
        loadApods()
    }

    private fun loadApods() {
        _items.value = repository.getOfflineApods()
    }

    companion object {

        fun Factory(
            repository: ApodRepository
        ): ViewModelProvider.Factory {

            return object : ViewModelProvider.Factory {

                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>
                ): T {

                    return ApodViewModel(repository) as T
                }
            }
        }
    }
}