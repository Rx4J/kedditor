package ru.lanik.kedditor.utils

import io.reactivex.rxjava3.core.Scheduler

interface SchedulerPolicy {
    fun mainThread(): Scheduler
    fun backThread(): Scheduler
}