package com.bikes.greyp.udacitycapstoneproject

import android.app.Application
import com.bikes.greyp.udacitycapstoneproject.ui.news.source.NewsSourceViewModel
import com.bikes.greyp.udacitycapstoneproject.usecases.CheckConnectionUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class BikerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val viewModelModule = module {
            viewModel {
                NewsSourceViewModel(get())
            }
        }

        val useCaseModule = module {
            single { CheckConnectionUseCase(this@BikerApp) }
        }

        startKoin {
            androidContext(this@BikerApp)
            modules(listOf(viewModelModule, useCaseModule))
        }
    }
}
