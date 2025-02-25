package com.core.testing


import com.core.schedulers.Schedulers
import io.reactivex.Scheduler //TODO: Add necessary dependencies

object TestSchedulers {
    fun from(scheduler: Scheduler): Schedulers = TestScheduler(scheduler)

    private class TestScheduler(private val backingScheduler: Scheduler) : Schedulers {
        override fun ui(): Scheduler = backingScheduler
        override fun io(): Scheduler = backingScheduler
    }
}