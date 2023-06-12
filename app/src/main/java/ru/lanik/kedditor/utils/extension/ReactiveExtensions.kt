package ru.lanik.kedditor.utils.extension

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

fun <T : Any> Single<T>.applySchedulerPolicy(scheduler: ru.lanik.kedditor.utils.SchedulerPolicy): Single<T> {
    return this.subscribeOn(scheduler.backThread())
        .observeOn(scheduler.mainThread())
}

fun <T : Any> Observable<T>.applySchedulerPolicy(scheduler: ru.lanik.kedditor.utils.SchedulerPolicy): Observable<T> {
    return this.subscribeOn(scheduler.backThread())
        .observeOn(scheduler.mainThread())
}

fun <T : Any> Observable<T>.performOnBack(scheduler: ru.lanik.kedditor.utils.SchedulerPolicy): Observable<T> {
    return this.subscribeOn(scheduler.backThread())
}