package com.example.currencyExchangeApplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.currencyExchangeApplication.data.model.ApiResponse
import com.example.currencyExchangeApplication.presentation.vm.MainViewModel
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class MainUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockedRepository: DataUseCase
    private lateinit var testScheduler: TestScheduler

    @Mock
    private lateinit var mockedApiResponseObserver: Observer<Response<ApiResponse>>

    @InjectMocks
    private lateinit var mainViewModel: MainViewModel


    @Before
    fun setup() {
        testScheduler = TestScheduler()
    }



    // Add more tests for error scenarios, edge cases, etc.

    // Make sure to test the ViewModel's behavior under different scenarios
}