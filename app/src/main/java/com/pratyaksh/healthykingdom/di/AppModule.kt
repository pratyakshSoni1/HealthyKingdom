package com.pratyaksh.healthykingdom.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pratyaksh.healthykingdom.data.repositories.remote.FirebaseAmbulanceRepoImpl
import com.pratyaksh.healthykingdom.data.repositories.remote.FirebaseHospitalsRepoImpl
import com.pratyaksh.healthykingdom.domain.repository.RemoteAmbulanceFbRepo
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
    fun provideFirebaseHospitalRepo(
        firestore: FirebaseFirestore
    ): RemoteFirebaseRepo{
        return FirebaseHospitalsRepoImpl(firestore) // Production data will be fetched from here
//        return TestRepositoryImpl() //Providing test data
    }

    @Provides
    @Singleton
    fun provideFirestoreInstance(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }


    @Provides
    @Singleton
    fun provideFBAuthInstance(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFBAmbulanceRepo(
        firestore: FirebaseFirestore
    ): RemoteAmbulanceFbRepo{
        return FirebaseAmbulanceRepoImpl(firestore = firestore )
    }



}