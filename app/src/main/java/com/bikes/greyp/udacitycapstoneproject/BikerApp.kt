package com.bikes.greyp.udacitycapstoneproject

import android.app.Application
import com.bikes.greyp.udacitycapstoneproject.data.database.RssDatabase
import com.bikes.greyp.udacitycapstoneproject.data.repository.Repository
import com.bikes.greyp.udacitycapstoneproject.data.repository.RssRepository
import com.bikes.greyp.udacitycapstoneproject.ui.news.newsfeed.NewsFeedViewModel
import com.bikes.greyp.udacitycapstoneproject.usecases.CheckConnectionUseCase
import com.bikes.greyp.udacitycapstoneproject.usecases.GetFeedLocalUseCase
import com.bikes.greyp.udacitycapstoneproject.usecases.GetFeedRemoteUseCase
import com.bikes.greyp.udacitycapstoneproject.usecases.StoreFeedUseCase
import com.bikes.greyp.udacitycapstoneproject.utils.Parser
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class BikerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val viewModelModule = module {
            viewModel {
                NewsFeedViewModel(get())
            }
        }

        val useCaseModule = module {
            single { CheckConnectionUseCase(this@BikerApp) }
            single { GetFeedLocalUseCase(get()) }
            single { GetFeedRemoteUseCase() }
            single { StoreFeedUseCase(get()) }
        }

        val repositoryModule = module {
            single { RssRepository(get(), get(), get(), get(), get()) as Repository }
            single { RssDatabase.getInstance(this@BikerApp).rssDao }
        }

        val utilsModule = module {
            single { Parser() }
        }

        startKoin {
            androidContext(this@BikerApp)
            modules(
                listOf(
                    viewModelModule,
                    useCaseModule,
                    repositoryModule,
                    utilsModule
                )
            )
        }
    }
}
