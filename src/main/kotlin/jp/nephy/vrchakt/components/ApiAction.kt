package jp.nephy.vrchakt.components

import jp.nephy.vrchakt.models.VRChaKtModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

abstract class ApiAction<M: VRChaKtModel>(private val executor: ExecutorService) {
    private val logger = logger(javaClass)
    private val defaultCallback: (VRChaKtResponse<M>) -> Unit = {  }
    private val defaultFallback: (Throwable) -> Unit = {
        logger.error(it) { "An error occured while processing queue." }
    }

    abstract fun complete(): VRChaKtResponse<M>

    fun completeAfter(delay: Long, unit: TimeUnit): VRChaKtResponse<M> {
        unit.sleep(delay)
        return complete()
    }

    fun execute(onSuccess: (VRChaKtResponse<M>) -> Unit, onFailure: (Throwable) -> Unit) {
        try {
            complete().let(onSuccess)
        } catch (e: Exception) {
            try {
                e.let(onFailure)
            } catch (e: Exception) {
                e.let(defaultFallback)
            }
        }
    }
    fun execute(onSuccess: (VRChaKtResponse<M>) -> Unit) {
        execute(onSuccess, defaultFallback)
    }
    fun execute() {
        execute(defaultCallback, defaultFallback)
    }

    fun executeAfter(delay: Long, unit: TimeUnit, onSuccess: (VRChaKtResponse<M>) -> Unit, onFailure: (Throwable) -> Unit) {
        unit.sleep(delay)
        execute(onSuccess, onFailure)
    }
    fun executeAfter(delay: Long, unit: TimeUnit, onSuccess: (VRChaKtResponse<M>) -> Unit) {
        executeAfter(delay, unit, onSuccess, defaultFallback)
    }
    fun executeAfter(delay: Long, unit: TimeUnit) {
        executeAfter(delay, unit, defaultCallback, defaultFallback)
    }

    fun submit(onSuccess: (VRChaKtResponse<M>) -> Unit, onFailure: (Throwable) -> Unit): Future<*> {
        return executor.submit {
            execute(onSuccess, onFailure)
        }
    }
    fun submit(onSuccess: (VRChaKtResponse<M>) -> Unit): Future<*> {
        return submit(onSuccess, defaultFallback)
    }
    fun submit(): Future<*> {
        return submit(defaultCallback, defaultFallback)
    }

    fun submitAfter(delay: Long, unit: TimeUnit, onSuccess: (VRChaKtResponse<M>) -> Unit, onFailure: (Throwable) -> Unit): Future<*> {
        return executor.submit {
            executeAfter(delay, unit, onSuccess, onFailure)
        }
    }
    fun submitAfter(delay: Long, unit: TimeUnit, onSuccess: (VRChaKtResponse<M>) -> Unit): Future<*> {
        return submitAfter(delay, unit, onSuccess, defaultFallback)
    }
    fun submitAfter(delay: Long, unit: TimeUnit): Future<*> {
        return submitAfter(delay, unit, defaultCallback, defaultFallback)
    }

    fun queue(onSuccess: (VRChaKtResponse<M>) -> Unit, onFailure: (Throwable) -> Unit) {
        executor.execute {
            execute(onSuccess, onFailure)
        }
    }
    fun queue(onSuccess: (VRChaKtResponse<M>) -> Unit) {
        queue(onSuccess, defaultFallback)
    }
    fun queue() {
        queue(defaultCallback, defaultFallback)
    }

    fun queueAfter(delay: Long, unit: TimeUnit, onSuccess: (VRChaKtResponse<M>) -> Unit, onFailure: (Throwable) -> Unit) {
        executor.submit {
            executeAfter(delay, unit, onSuccess, onFailure)
        }
    }
    fun queueAfter(delay: Long, unit: TimeUnit, onSuccess: (VRChaKtResponse<M>) -> Unit) {
        queueAfter(delay, unit, onSuccess, defaultFallback)
    }
    fun queueAfter(delay: Long, unit: TimeUnit) {
        queueAfter(delay, unit, defaultCallback, defaultFallback)
    }
}
