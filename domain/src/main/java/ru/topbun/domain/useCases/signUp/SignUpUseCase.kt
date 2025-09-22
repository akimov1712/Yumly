package ru.topbun.domain.useCases.signUp

import ru.topbun.domain.entity.signUp.SignUpEntity
import ru.topbun.domain.repository.signUp.SignUpRepository

class SignUpUseCase(private val repository: SignUpRepository){

    suspend operator fun invoke(signUp: SignUpEntity) = repository.signUp(signUp)

}