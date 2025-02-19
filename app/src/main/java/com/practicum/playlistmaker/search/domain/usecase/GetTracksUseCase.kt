package com.practicum.playlistmaker.search.domain.usecase

import com.practicum.playlistmaker.search.domain.consumer.Consumer
import com.practicum.playlistmaker.search.domain.api.RepositoryNetwork
import com.practicum.playlistmaker.search.domain.consumer.ConsumerData
import com.practicum.playlistmaker.search.domain.model.Resource
import com.practicum.playlistmaker.search.domain.model.Track
import java.util.concurrent.Executors

class GetTracksUseCase(
    private val repository: RepositoryNetwork
) {
    private val executor = Executors.newSingleThreadExecutor()

    fun execute(searchText: String, consumer: Consumer<List<Track>>){
        executor.execute{
            val tracksResource = repository.getTracks(searchText)
            when (tracksResource){
                is Resource.Error -> {
                    consumer.consume(ConsumerData.Error("Сетевая ошибка consumer"))
                }

                is Resource.Success -> {
                    consumer.consume(ConsumerData.Data(tracksResource.data))
                }
            }
        }
    }
}