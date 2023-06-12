package ru.lanik.kedditor.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class NetworkScheduler : SchedulerPolicy {
    override fun mainThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun backThread(): Scheduler {
        return Schedulers.io()
    }
}