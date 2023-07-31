package com.pratyaksh.healthykingdom.utils

object Constants{

    object Collections{
        val HOSPITALS_COLLECTION = "hospitals"
        val AMBLANCE_DRIVERS = "ambulance"
        val PUBLIC_USERS = "public"
        val LIFE_FLUIDS = "lifefluids"
        val REQUESTS = "requests"
    }

    object RequestsDocField{
        val blood = "bloods"
        val plasma = "plasma"
        val platelets = "platelets"
        val hospitalId = "hospitalId"
    }

    object UserDocField{
        val userId = "userId"
        val goLive = "online"
        val location = "location"
    }

    object LifeFluidFieldNames{

        val blood = "bloods"
        val plasma = "plasma"
        val platelets = "platelets"
        object BloodFields{
            val A_POSITIVE = "aPos"
            val A_NEGATIVE = "aNeg"
            val B_POSITIVE = "bPos"
            val B_NEGATIVE = "bNeg"
            val AB_POSITIVE = "abPos"
            val AB_NEGATIVE = "abNeg"
            val O_POSITIVE = "oPos"
            val O_NEGATIVE = "oNeg"
        }

        object PlasmaFields{
            val PLASMA_A = "aGroup"
            val PLASMA_B = "bGroup"
            val PLASMA_AB = "abGroup"
            val PLASMA_O = "oGroup"
        }
    }

    val USER_LOGGED_DS = "healthykingdom.user_logged_datastore"
    val USER_LOGGED_KEY = "healthykingdom.user_logged_key"
    val OTP_VALIDATION_RESULTS_KEY = "healthykingdom.user_reg_varification_key"

}

enum class NotificationChannelsId{

    AMBULANCE_LIVE_LOC
}