package com.example.pkshrestha.wallclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.example.utils.services.RestCalls
import khronos.Dates
import khronos.toString
import kotlinx.android.synthetic.main.activity_fullscreen.*
import android.os.CountDownTimer
import android.support.v4.app.ActivityOptionsCompat
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import java.text.SimpleDateFormat
import java.util.*


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {
    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val mDelayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    inner class AsyncTaskExecutor: android.os.AsyncTask<String, String, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            Log.i("Weather", "preExecute here ")
        }

        override fun doInBackground(vararg p0: String?): String {

            var Result: String = "";
            //It will return current data and time.
            val API_URL = "http://androidpala.com/tutorial/http.php?get=1";

            try {

                textWeather.text=RestCalls.getCaryWeather() + " F"
            } catch(Ex: Exception) {
                Log.d("", "Error in doInBackground " + Ex.message);
            }
            return Result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            Log.i("Weather", "on Post Execute")

        }

    }

    private fun startTimer() {
        val mTimer = object : CountDownTimer(2000000, 1000) {
            override fun onFinish() {
                txtCounter.text="END of Timer"
            }

            override fun onTick(millisUntilFinished: Long) {
                    val str: String = txtCounter.toString()
                    //update the ui with the milliseconds left
                    txtCounter.text = (millisUntilFinished/1000).toString()
            }

        }.start()
    }

    private fun getCurrentTimeStamp(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        val now = Date()
        return simpleDateFormat.format(now)
    }

    // called when it receives a tick from system
    private fun makeBroadcastReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                if (intent?.action == Intent.ACTION_TIME_TICK) {
                    txtCounter.text = getCurrentTimeStamp()
                    // update weather
                    // fix it later since calling it every second is an overkill
                    AsyncTaskExecutor().execute();
                }
            }
        }
    }


    private fun showProductList (){
        val userId=1
        val options = null

        startActivity(productListIntent(userId = txtCounter.text.toString()))

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mVisible = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreen_content.setOnClickListener { toggle() }
        fullscreen_content.text= Dates.today.toString("dd/MM/yyyy")

        // show products
        btn_show_products.setOnClickListener {showProductList() }

        AsyncTaskExecutor().execute();


        // counter async , CommonPool means background UI means UI thread
         async (CommonPool) {
             val str: String = txtCounter.toString()
             txtCounter.text= "100"
         }
        // start the countdown timer
        startTimer()

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
        startTimer()
    }

    private fun show() {
        // Show the system bar
        fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300
    }
}
