package com.comphy.photo.data.repository

import com.comphy.photo.data.source.local.room.location.LocationDao
import com.comphy.photo.data.source.local.entity.ProvinceEntity
import com.comphy.photo.data.source.local.entity.RegencyEntity
import com.comphy.photo.data.source.remote.client.ApiService
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val apiService: ApiService,
    private val locationDao: LocationDao,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun fetchLocation() = flow {
        val localLocation = locationDao.getLocation()

        if (localLocation.isEmpty()) {
            val provinceResponse =
                apiService.getProvinces("http://www.emsifa.com/api-wilayah-indonesia/api/provinces.json")

            provinceResponse.suspendOnSuccess {
                data.forEach { provinceItem ->
                    locationDao.insertProvince(ProvinceEntity(province = provinceItem.name))

                    val regencyResponse =
                        apiService.getRegencies("http://www.emsifa.com/api-wilayah-indonesia/api/regencies/${provinceItem.id}.json")

                    regencyResponse.suspendOnSuccess {
                        data.map {
                            locationDao.insertRegency(
                                RegencyEntity(
                                    provinceSourceId = it.province_id.toLong(),
                                    regencyName = it.name
                                )
                            )
                        }
                    }
                        .onError { Timber.tag("On Error").e(message()) }
                        .onException { Timber.tag("On Exception").e(message()) }
                }
            }
                .onError { Timber.tag("On Error").e(message()) }
                .onException { Timber.tag("On Exception").e(message()) }
        }

        emit(localLocation)

    }.flowOn(ioDispatcher)

    fun getRegencies() = flow { emit(locationDao.getRegencies()) }.flowOn(ioDispatcher)


}