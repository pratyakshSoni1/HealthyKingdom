package com.pratyaksh.healthykingdom.di

import com.pratyaksh.healthykingdom.data.repositories.remote.FirebaseRepoImpl
import com.pratyaksh.healthykingdom.domain.repository.RemoteFirebaseRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseRepo(): RemoteFirebaseRepo{
        return FirebaseRepoImpl()
    }


}