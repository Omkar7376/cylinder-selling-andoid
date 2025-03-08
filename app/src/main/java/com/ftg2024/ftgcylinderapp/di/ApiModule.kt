package com.ftg2024.ftgcylinderapp.di

import com.ftg2024.ftgcylinderapp.auth.repo.LoginApiService
import com.ftg2024.ftgcylinderapp.constants.ApiConstants.BASE_URL
import com.ftg2024.ftgcylinderapp.distributionmanagement.repo.DistributionMgmtApiService
import com.ftg2024.ftgcylinderapp.retrofit.ApiKeyInterceptor
import com.ftg2024.ftgcylinderapp.retrofit.AuthTokenKeyInterceptor
import com.ftg2024.ftgcylinderapp.stockmanagement.repo.StockManagementApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    @Singleton
    @Provides
    fun provideRetrofitInstance(apiKeyInterceptor: ApiKeyInterceptor, authKeyInterceptor: AuthTokenKeyInterceptor) : Retrofit {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(67, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(authKeyInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideLoginApiService(retrofit: Retrofit): LoginApiService {
        return retrofit.create(LoginApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideStockManagementApiService(retrofit: Retrofit): StockManagementApiService {
        return retrofit.create(StockManagementApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDistributeManagementApiService(retrofit : Retrofit) : DistributionMgmtApiService {
        return retrofit.create(DistributionMgmtApiService :: class.java)
    }
}