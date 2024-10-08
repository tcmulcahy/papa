package papa.internal

import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.core.os.MessageCompat

// Thx @chet and @jreck
// https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:metrics/metrics-performance/src/main/java/androidx/metrics/performance/JankStatsApi16Impl.kt;l=66;drc=523d7a11e46390281ed3f77893671730cd6edb98
internal fun Handler.postAtFrontOfQueueAsync(callback: () -> Unit) {
  sendMessageAtFrontOfQueue(Message.obtain(this, callback).apply {
    MessageCompat.setAsynchronous(this, true)
  })
}

internal val isMainThread: Boolean get() = Looper.getMainLooper() === Looper.myLooper()

internal fun checkMainThread() {
  check(isMainThread) {
    "Should be called from the main thread, not ${Thread.currentThread()}"
  }
}

internal val mainHandler by lazy {
  Handler(Looper.getMainLooper())
}
