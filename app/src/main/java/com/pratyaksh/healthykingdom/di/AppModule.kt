package com.pratyaksh.healthykingdom.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pratyaksh.healthykingdom.data.repositories.remote.FirebaseAmbulanceRepoImpl
import com.pratyaksh.healthykingdom.data.repositories.remote.HospitalFbHospitalsRepoImpl
import com.pratyaksh.healthykingdom.data.repositories.remote.LifeFluidsRepoImpl
import com.pratyaksh.healthykingdom.data.repositories.remote.PublicUserFbRepoImpl
import com.pratyaksh.healthykingdom.data.repositories.remote.RequestsRepoImpl
import com.pratyaksh.healthykingdom.domain.repository.RemoteAmbulanceFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemoteHospitalFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemoteLifeFluidsFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemotePublicUserFbRepo
import com.pratyaksh.healthykingdom.domain.repository.RemoteRequestsRepo
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
    fun provideRequestsRepo(
        firestore: FirebaseFirestore
    ): RemoteRequestsRepo{
        return RequestsRepoImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseHospitalRepo(
        firestore: FirebaseFirestore
    ): RemoteHospitalFbRepo{
        return HospitalFbHospitalsRepoImpl(firestore) // Production data will be fetched from here
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
    @Provides
    @Singleton
    fun provideFBPublicUserRepo(
        firestore: FirebaseFirestore
    ): RemotePublicUserFbRepo{
        return PublicUserFbRepoImpl(firestore)
    }
    @Provides
    @Singleton
    fun provideFBFluidsRepo(
        firestore: FirebaseFirestore
    ): RemoteLifeFluidsFbRepo{
        return LifeFluidsRepoImpl(firestore)
    }





}