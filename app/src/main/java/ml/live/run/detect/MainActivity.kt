package ml.live.run.detect

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.accessibility.AccessibilityManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder

class MainActivity : AppCompatActivity(), AccessibilityManager.AccessibilityStateChangeListener {


    @BindView(R.id.layout_control_accessibility_icon)
    lateinit var pluginStatusIcon: ImageView
    @BindView(R.id.layout_control_accessibility_text)
    lateinit var pluginStatusText: TextView

    lateinit private var accessibilityManager: AccessibilityManager
    lateinit private var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        unbinder = ButterKnife.bind(this)

        //监听AccessibilityService 变化
        accessibilityManager = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        accessibilityManager.addAccessibilityStateChangeListener(this)
        updateServiceStatus()
    }


    override fun onDestroy() {
        //移除监听服务
        accessibilityManager.removeAccessibilityStateChangeListener(this)
        unbinder.unbind()
        super.onDestroy()
    }

    @OnClick(R.id.layout_control_accessibility)
    fun openAccessibility() {
        try {
            Toast.makeText(this, "点击「通知监测」" + pluginStatusText.text, Toast.LENGTH_SHORT).show()
            val accessibleIntent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(accessibleIntent)
        } catch (e: Exception) {
            Toast.makeText(this, "遇到一些问题,请手动打开系统设置>无障碍服务>通知监测(ฅ´ω`ฅ)", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }


    override fun onAccessibilityStateChanged(b: Boolean) {
        updateServiceStatus()
        println("change listener")
    }

    /**
     * 更新当前 Service 显示状态
     */
    private fun updateServiceStatus(): Unit {
        if (isServiceEnabled) {
            pluginStatusText.setText(R.string.service_off)
            pluginStatusIcon.setBackgroundResource(R.mipmap.ic_stop)
        } else {
            pluginStatusText.setText(R.string.service_on)
            pluginStatusIcon.setBackgroundResource(R.mipmap.ic_start)
        }
    }


    private val isServiceEnabled: Boolean
        get() {

            val accessibilityServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            for (info in accessibilityServices) {
                println("info id" + info.id)
                if (info.id == packageName + "/.NotificationService") {
                    return true
                }
            }
            return false
        }
}
