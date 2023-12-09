package com.example.stockmarketapp.presentation.company_listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketapp.domain.repository.StockRepository
import com.example.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/*La funcion del viewModel es acceder o llamar a las funciones de la capa Data
    no directamente , si no usando una interface (repository)
    Y luego mapear la data y pasarlo  por estados para poder mostrar la data
    mediante compose en nuestra UI y asegurarnos que la data se mantenga
    a los cambios de configuracion como rotacion de pantalla */
@HiltViewModel
class CompanyListingsViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

    var state by mutableStateOf(CompanyListingsState())

    //Le agregaremos 500milisegundos para realizar la busqueda de Company y asi
    //no saturar las consultas por cada caracter que escribamos y realizemos busqueda x caracter
    private var searchJob: Job? = null

    fun onEvent(event: CompanyListingsEvent) {
        when (event) {
            is CompanyListingsEvent.Refresh -> {
                getCompanyListings(fetchFromRemote = true)
            }

            is CompanyListingsEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    //la primera vez luego de tipear la query esperamos 500milisegundos
                    delay(500L)
                    //Para lanzar la funcion que me va a dar la lista de company
                    getCompanyListings( )

                    /*
                        Si el usuario vuelve a escribir luego de haber ya escrito la query ( le añade mas caracteres )
                        se activa recien el SearchJob.cancel lo que hace que no se lanze el delay ni la funcion y luego de terminar de tipear
                        se vuelva a lanzar el launch con el delay y la llamada a getCompanyListings
                     */
                }

            }
        }
    }

    private fun getCompanyListings(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            repository
                .getCompanyListings(fetchFromRemote, query)
                .collect { result ->
                    //Para recolectar el resultado de esta funcion ( que es poder obtener el listado de company
                    when (result) {
                        is Resource.Success -> {
                            // Revisamos que el result no sea null
                            result.data?.let { listings ->
                                state = state.copy(
                                    companies = listings
                                )

                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }

                }
        }

    }

}