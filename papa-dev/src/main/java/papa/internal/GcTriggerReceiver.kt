package papa.internal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Message
import android.os.Messenger
import android.util.Log
import papa.safeTrace

const val GC_TRIGGER_MESSENGER = "papa.GC_TRIGGER_MESSENGER"
const val WHAT_GC_TRIGGERED = 1

internal class GcTriggerReceiver : BroadcastReceiver() {
  override fun onReceive(
    context: Context,
    intent: Intent
  ) {
    safeTrace("force gc") {
      val messenger = intent.getParcelableExtra<Messenger>(GC_TRIGGER_MESSENGER)
      Log.d("GcTriggerReceiver", "Triggering GC with messenger=$messenger")
      gc()
      if (messenger != null) {
        val msg = Message.obtain()
        msg.what = WHAT_GC_TRIGGERED
        messenger.send(msg)
        msg.recycle()
      } else {
        context.sendBroadcast(Intent("papa.GC_TRIGGERED"))
      }
    }
  }

  private fun gc() {
    // Code borrowed from AOSP FinalizationTest:
    // https://android.googlesource.com/platform/libcore/+/master/support/src/test/java/libcore/
    // java/lang/ref/FinalizationTester.java
    Runtime.getRuntime().gc()
    Thread.sleep(100)
    System.runFinalization()
    Thread.sleep(200)
  }
}
