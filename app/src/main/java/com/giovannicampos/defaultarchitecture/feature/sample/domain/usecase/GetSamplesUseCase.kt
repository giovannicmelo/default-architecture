package com.giovannicampos.defaultarchitecture.feature.sample.domain.usecase

import com.giovannicampos.defaultarchitecture.feature.sample.domain.model.Sample
import com.giovannicampos.defaultarchitecture.feature.sample.domain.repository.SampleRepository

class GetSamplesUseCase(
    private val repository: SampleRepository,
) {

    suspend operator fun invoke(): List<Sample> = repository.getSamples()
}
