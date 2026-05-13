package ec.edu.uisek.githubclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.uisek.githubclient.models.RepositoryPayload
import ec.edu.uisek.githubclient.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepoFormViewModel : ViewModel() {

    private val apiService = RetrofitClient.apiService

    // Estado para controlar si estamos enviando datos
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Estado para saber si la operación fue exitosa (para cerrar el form)
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess = _isSuccess.asStateFlow()

    // Estado para errores
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun createRepository(name: String, description: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val payload = RepositoryPayload(name = name, description = description)
                apiService.createRepository(payload)
                _isSuccess.value = true // Notificamos éxito
            } catch (e: Exception) {
                _errorMessage.value = "Error al crear repositorio: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para resetear el estado de éxito después de navegar
    fun resetSuccess() {
        _isSuccess.value = false
    }
}