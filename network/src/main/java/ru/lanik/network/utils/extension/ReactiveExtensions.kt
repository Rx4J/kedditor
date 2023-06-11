package ru.lanik.network.utils.extension

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.lanik.network.utils.SchedulerPolicy

fun <T : Any> Single<T>.applySchedulerPolicy(scheduler: SchedulerPolicy): Single<T> {
    return this.subscribeOn(scheduler.backThread())
        .observeOn(scheduler.mainThread())
}

fun <T : Any> Observable<T>.applySchedulerPolicy(scheduler: SchedulerPolicy): Observable<T> {
    return this.subscribeOn(scheduler.backThread())
        .observeOn(scheduler.mainThread())
}

fun <T : Any> Observable<T>.performOnBack(scheduler: SchedulerPolicy): Observable<T> {
    return this.subscribeOn(scheduler.backThread())
}