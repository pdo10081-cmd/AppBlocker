package com.example.appblocker

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_enable).setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }

    override fun onResume() {
        super.onResume()
        updateStatus()
    }

    private fun updateStatus() {
        val tv = findViewById<TextView>(R.id.tv_status)
        val btn = findViewById<Button>(R.id.btn_enable)
        if (isServiceEnabled()) {
            tv.text = "✅ Сервис активен\nПриложения заблокированы с 00:00 до 08:00"
            tv.setTextColor(getColor(R.color.status_ok))
            btn.text = "Открыть настройки доступности"
        } else {
            tv.text = "⚠️ Сервис НЕ активен\nНажмите кнопку и включите «AppBlocker»"
            tv.setTextColor(getColor(R.color.status_warn))
            btn.text = "Включить сервис →"
        }
    }

    private fun isServiceEnabled(): Boolean {
        val name = "$packageName/${BlockerAccessibilityService::class.java.canonicalName}"
        val enabled = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return enabled.split(':').any { it.equals(name, ignoreCase = true) }
    }
}
