package com.app.todolist.helper

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.todolist.R
import java.util.*

open class ActivityBase : AppCompatActivity() {

    /**
     * @return the lifecycleState
     */
    /**
     * @param lifecycleState the lifecycleState to set
     */
    var lifecycleState: State? = null

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */


    /**
     * It saves the current state of the application.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    /**
     * Restores the saved application state, in case of be needed, and does default settings for the action bar.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this //        BlurKit.init(activity)
        //        if (Util.LOG_ENABLED) {
        //            Log.v(TAG, "protected void onCreate(Bundle savedInstanceState)")

    }

    @SuppressLint("NewApi")
    fun setApplicationLanguage(newLanguage: String) {
        val newLocale = Locale(newLanguage)
        val activityRes = resources
        val activityConf = activityRes.configuration
        activityConf.setLocale(newLocale)
        activityRes.updateConfiguration(activityConf, activityRes.displayMetrics)

        val applicationRes = activity.applicationContext.resources
        val applicationConf = applicationRes.configuration
        applicationConf.setLocale(newLocale)
        applicationRes.updateConfiguration(applicationConf, applicationRes.displayMetrics)
    }

    fun callFragmentWithAddBackStack(containerId: Int, fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().replace(containerId, fragment, tag).setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit).addToBackStack(tag).commit()
    }

    fun callFragmentWithReplace(containerId: Int, fragment: Fragment, tag: String?) {
        val transaction = activity.supportFragmentManager.beginTransaction().replace(containerId, fragment, tag).setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit)
        if (tag != null) {
            transaction.addToBackStack(tag).commit()
        }
        else {
            transaction.commit()
        }
    }

    /**
     * if you will pass tag as title of fragment it will add that
     * fragment to stack otherwise will not add on stack.
     *
     * @param containerId
     * @param fragment
     * @param tag
     */
    fun callFragment(containerId: Int, fragment: Fragment, tag: String?) {
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        val transaction = supportFragmentManager.beginTransaction().replace(containerId, fragment, tag).setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit)
        if (tag != null) transaction.addToBackStack(tag).commit()
        else transaction.commit()
    }

    fun callFragmentLossState(containerId: Int, fragment: Fragment, tag: String?) {
        val transaction = supportFragmentManager.beginTransaction().replace(containerId, fragment, tag).setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit)
        if (tag != null) transaction.addToBackStack(tag).commitAllowingStateLoss()
        else transaction.commitAllowingStateLoss()
    }

    /**
     * if you will pass tag as title of fragment it will add that
     * fragment to stack otherwise will not add on stack.
     *
     * @param containerId
     * @param fragment
     * @param tag
     */
    fun addFragment(containerId: Int, fragment: Fragment, tag: String?) {

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val transaction = supportFragmentManager.beginTransaction().add(containerId, fragment, tag).setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit)
        if (tag != null) transaction.addToBackStack(tag).commit()
        else transaction.commit()
    }

    /**
     * if you will pass tag as title of fragment it will add that
     * fragment to stack otherwise will not add on stack.
     *
     * @param fragment
     */
    fun removeFragment(fragment: Fragment) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val transaction = supportFragmentManager.beginTransaction().remove(fragment).setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit)
        transaction.commit()
    }

    /**
     * Pass Exact Uri of image to the method, and it will provide you the real path of image.
     *
     * @param contentUri
     * @return
     */
    fun getRealPathFromURI(contentUri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, projection, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor!!.moveToFirst()
        return cursor!!.getString(column_index)
    }

    override fun onStart() {
        super.onStart()
        lifecycleState = State.STARTED
    }

    override fun onResume() {
        super.onResume()
        lifecycleState = State.RESUMED
    }

    override fun onPause() {
        super.onPause()
        lifecycleState = State.PAUSED
    }


    override fun onStop() {
        super.onStop()
        lifecycleState = State.STOPPED
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleState = State.DESTROYED
    }

    /**
     * The possibles states of an activity lifecycle.
     */
    enum class State {
        CREATED, STARTED, RESUMED, PAUSED, STOPPED, DESTROYED
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    @SuppressLint("ServiceCast")
    fun showKeyboard() {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    fun hideKeyboard() {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }


    @Throws(Exception::class)
    private fun RGB565toARGB888(img: Bitmap): Bitmap {
        val numPixels = img.width * img.height
        val pixels = IntArray(numPixels)
        img.getPixels(pixels, 0, img.width, 0, 0, img.width, img.height)
        val result = Bitmap.createBitmap(img.width, img.height, Bitmap.Config.ARGB_8888)
        result.setPixels(pixels, 0, result.width, 0, 0, result.width, result.height)
        return result
    }

    fun noTrailingwhiteLines(text: CharSequence): CharSequence {
        var text = text

        while (text[text.length - 1] == '\n') {
            text = text.subSequence(0, text.length - 1)
        }

        while (text[text.length - 2] == '\n') {
            text = text.subSequence(0, text.length - 2)
        }
        return text
    }


    fun withSuffix(count: Long): String {
        if (count < 1000) return "" + count
        val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
        return String.format("%.1f %c", count / Math.pow(1000.0, exp.toDouble()), "kMGTPE"[exp - 1])
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarGradiant(flag: Int) { //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //                val window = activity.window
        //                val background = activity.resources.getDrawable(R.drawable.bg_tour_header)
        //                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //                window.statusBarColor = activity.resources.getColor(android.R.color.transparent)
        //                //window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
        //                activity.window.decorView.systemUiVisibility = flag
        //                window.setBackgroundDrawable(background)
        //            }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarColor(color: Int, flag: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
            activity.window.decorView.systemUiVisibility = flag

        }
    }

    companion object {
        lateinit var activity: AppCompatActivity
    }

    override fun onBackPressed() {

        super.onBackPressed()
    }


}