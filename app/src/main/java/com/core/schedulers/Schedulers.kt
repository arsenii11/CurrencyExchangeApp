package com.core.schedulers

import io.reactivex.Scheduler

interface Schedulers {
    fun ui(): Scheduler
    fun io(): Scheduler
}