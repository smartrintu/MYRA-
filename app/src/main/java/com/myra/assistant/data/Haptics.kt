package com.myra.assistant.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Spinner

/** Light vibration for taps. Respects the Settings switch "Haptic feedback". */
object Haptics {

    @Suppress("DEPRECATION")
    fun tick(ctx: Context) {
        if (!SecurePrefs.hapticFeedback) return
        try {
            val v = ctx.applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return
            if (!v.hasVibrator()) return
            if (Build.VERSION.SDK_INT >= 26) v.vibrate(VibrationEffect.createOneShot(12, 80))
            else v.vibrate(12)
        } catch (_: Exception) {
        }
    }

    /** Adds a tap vibration to every button, switch, radio button and spinner under [root]. */
    @SuppressLint("ClickableViewAccessibility")
    fun attachTree(root: View) {
        if (root is Button || root is CompoundButton || root is Spinner) {
            root.setOnTouchListener { v, e ->
                if (e.action == MotionEvent.ACTION_DOWN) tick(v.context)
                false // never consume the touch, normal click still works
            }
        }
        if (root is ViewGroup) {
            for (i in 0 until root.childCount) attachTree(root.getChildAt(i))
        }
    }
}
