package com.pratyaksh.healthykingdom.di

import com.google.firebase.firestore.FirebaseFirestore
import com.pratyaksh.healthykingdom.data.repositories.remote.FirebaseRepoImpl
import com.pratyaksh.healthykingdom.data.repositories.test.TestRepositoryImpl
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
    fun provideFirebaseRepo(
        firestore: FirebaseFirestore
    ): RemoteFirebaseRepo{
        return FirebaseRepoImpl(firestore) // Production data will be fetched from here
//        return TestRepositoryImpl() //Providing test data
    }

    @Provides
    @Singleton
    fun provideFirestoreInstance(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }


}