package ml.live.run.detect

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent

/**
 * Created by Bai on 10/11/16.
 * Detect notification
 */

class NotificationService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        println("onAccessibilityEvent" + event.toString())
        if (event.eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            println("notification: " + event.text + event.packageName)
        }
    }


    override fun onServiceConnected() {
        println("onServiceConnected")
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
        info.notificationTimeout = 100
        serviceInfo = info
    }

    override fun onInterrupt() {
        println("onInterrupt")
    }


}
