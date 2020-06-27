package tk.zedlabs.digitalaversion

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 12

        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), packageName)

        if (mode == AppOpsManager.MODE_ALLOWED) {
            tv_1.text = "Success"
        } else {
            startActivityForResult(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS)
            Toast.makeText(this, "Please Grant Permission for the app to work", Toast.LENGTH_SHORT).show()
        }

        val mUsageStatsManager: UsageStatsManager = this.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val startTime = endTime  - (24 * 60 * 60 * 1000)
        val lUsageStatsMap = mUsageStatsManager.queryAndAggregateUsageStats(startTime, endTime)

        val totalTimeUsageInMillis: Long = lUsageStatsMap["com.google.android.youtube"]!!.totalTimeInForeground

        val timeInHour: Float = totalTimeUsageInMillis.toFloat() / (60 * 60 * 1000)
        val remainingTimeInMin = timeInHour % 60
        tv_1.text = "${timeInHour.toInt()}  : ${remainingTimeInMin}"
    }
}