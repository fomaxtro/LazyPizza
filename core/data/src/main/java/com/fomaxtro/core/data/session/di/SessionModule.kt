package com.fomaxtro.core.data.session.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.fomaxtro.core.data.session.SecureSessionStorage
import com.fomaxtro.core.data.session.SessionStorage
import com.fomaxtro.core.data.session.secure.EncryptedSecureSessionSerializer
import com.fomaxtro.core.data.session.secure.SecureSessionData
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private object SessionQualifiers {
    val SESSION = named("session")
    val SECURE = named("secure")
}

val sessionModule = module {
    single<DataStore<Preferences>>(SessionQualifiers.SESSION) {
        PreferenceDataStoreFactory.create {
            androidContext().preferencesDataStoreFile("user_session")
        }
    }
    single<SessionStorage> {
        SessionStorage(
            dataStore = get(SessionQualifiers.SESSION)
        )
    }

    single<DataStore<SecureSessionData>>(SessionQualifiers.SECURE) {
        DataStoreFactory.create(
            serializer = EncryptedSecureSessionSerializer(),
            corruptionHandler = ReplaceFileCorruptionHandler {
                SecureSessionData()
            },
            produceFile = {
                androidContext().dataStoreFile("secure_session.encrypted")
            }
        )
    }

    single<SecureSessionStorage> {
        SecureSessionStorage(
            dataStore = get(SessionQualifiers.SECURE)
        )
    }
}