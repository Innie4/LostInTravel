package com.example.lostintravel.data.mapper

import com.example.lostintravel.data.remote.util.Result as DataResult
import com.example.lostintravel.domain.util.Result as DomainResult
import javax.inject.Inject

class ResultMapper @Inject constructor(
    private val exceptionMapper: ExceptionMapper
) {
    
    fun <T> mapToDomainResult(dataResult: DataResult<T>): DomainResult<T> {
        return when (dataResult) {
            is DataResult.Success -> DomainResult.success(dataResult.data)
            is DataResult.Error -> DomainResult.error(
                exceptionMapper.mapToDomainException(dataResult.exception)
            )
            is DataResult.Loading -> DomainResult.loading()
        }
    }
}