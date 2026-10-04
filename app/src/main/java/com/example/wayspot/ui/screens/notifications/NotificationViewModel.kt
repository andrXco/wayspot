    package com.example.wayspot.ui.screens.notifications

    import androidx.lifecycle.ViewModel
    import com.example.wayspot.data.local.PreviewData
    import dagger.hilt.android.lifecycle.HiltViewModel
    import javax.inject.Inject
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.flow.update

    /** Carga y expone la colección de notificaciones para su representación. */
    @HiltViewModel
    class NotificationsViewModel @Inject constructor() : ViewModel() {

        private val _uiState = MutableStateFlow(NotificationsState())

        val uiState: StateFlow<NotificationsState> = _uiState

        init {
            loadNotifications()
        }

        fun loadNotifications() {
            _uiState.update { currentState ->
                currentState.copy(
                    notifications = PreviewData.notifications
                )
            }
        }
    }
