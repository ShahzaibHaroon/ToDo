package com.app.todolist.helper

import android.annotation.TargetApi
import android.app.FragmentManager
import android.app.ProgressDialog
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateFormat
import android.transition.Fade
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.view.View
import android.view.WindowManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.app.todolist.R
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


open class BaseFragment : Fragment() {
    private val dialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun isContentNull(content: String): Boolean {
        return content.isEmpty()
    }

    fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }


    fun removeDialog() {
        dialog!!.dismiss()
    }

    fun changeLanguage(local_language: String) {
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = Locale(local_language)
        res.updateConfiguration(conf, dm)
    }

    fun getRealPathFromURI(contentUri: Uri): String {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION)
        val cursor = ActivityBase.activity.contentResolver.query(contentUri, projection, MediaStore.Images.Media.DATE_ADDED, null, "date_added ASC")
        cursor!!.moveToFirst()
        if (cursor != null && cursor.moveToFirst()) {
            val uri = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)))
            path = uri.toString()
            cursor.close()
        }
        return path!!.toString()
    }


    protected fun callFragmentWithAddBackStack(containerId: Int, fragment: Fragment, tag: String) {
        ActivityBase.activity.supportFragmentManager.beginTransaction().replace(containerId, fragment, tag).setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit).addToBackStack(tag).commit()
    }

    fun callFragmentWithBackStack(containerId: Int, fragment: Fragment, tag: String) {
        ActivityBase.activity!!.supportFragmentManager.beginTransaction().replace(containerId, fragment, tag).setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit).addToBackStack(tag).commit()
    }


    fun addFragment(containerId: Int, fragment: Fragment, tag: String?) {
        ActivityBase.activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        val transaction = ActivityBase.activity!!.supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit).add(containerId, fragment, tag)

        if (tag != null) transaction.addToBackStack(tag).commit()
        else transaction.commit()
    }


    fun addFragmentChild(containerId: Int, fragment: Fragment, tag: String?, manager: androidx.fragment.app.FragmentManager) {

        ActivityBase.activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val transaction = manager.beginTransaction().add(containerId, fragment, tag).setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit)

        if (tag != null) transaction.addToBackStack(tag).commit()
        else transaction.commit()
    }


    fun callFragmentWithReplace(containerId: Int, fragment: Fragment, tag: String?) {
        ActivityBase.activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val transaction = ActivityBase.activity!!.supportFragmentManager.beginTransaction().replace(containerId, fragment, tag).setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit)
        if (tag != null) transaction.addToBackStack(tag).commit()
        else transaction.commit()
    }

    fun callFragmentWithReplaceAndShareElement(containerId: Int, fragment: Fragment, shareElement: View, transitionName: String, tag: String?) {
        val transaction = ActivityBase.activity!!.supportFragmentManager.beginTransaction().addSharedElement(shareElement, transitionName).replace(containerId, fragment, tag).setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit)
        if (tag != null) transaction.addToBackStack(tag).commit()
        else transaction.commit()
    }


    fun clearBackStack() {
        ActivityBase.activity!!.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun DateFormatter(date: String): String {
        val a = date.replace("\\D+".toRegex(), "")
        val timeInMillis = java.lang.Long.parseLong(a)
        if (timeInMillis < 0) return ""
        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        calendar.timeInMillis = timeInMillis
        val _date = calendar.time
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH) + 1
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)
        return mDay.toString() + "/" + mMonth + "/" + mYear
    }

    fun DateFormatter1(date: String): String {
        val a = date.replace("\\D+".toRegex(), "")
        val timeInMillis = java.lang.Long.parseLong(a)
        if (timeInMillis < 0) return ""
        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        calendar.timeInMillis = timeInMillis
        val _date = calendar.time
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH) + 1
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)
        return mYear.toString() + "-" + mMonth + "-" + mDay
    }

    fun getUriFromUrl(thisUrl: String): Uri {
        val builder: Uri.Builder
        var url: URL? = null
        try {
            url = URL(thisUrl)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        builder = Uri.Builder().scheme(url!!.protocol).authority(url.authority).appendPath(url.path)
        return builder.build()
    }

    fun DateHeader(date: String): Long {
        val a = date.replace("\\D+".toRegex(), "")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = java.lang.Long.parseLong(a)
        return calendar.get(Calendar.DAY_OF_MONTH).toLong()
    }

    fun GetDateTime(): String {
        val calendar = Calendar.getInstance()
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)

        val month_date = SimpleDateFormat("MMM")
        val month_name = month_date.format(calendar.time)
        val delegate = "hh:mm aaa"
        val time = DateFormat.format(delegate, Calendar.getInstance().time) as String

        return "$month_name $mDay, $mYear $time"
    }


    fun Get24FormatTime(date: String): String {
        val a = date.replace("\\D+".toRegex(), "")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = java.lang.Long.parseLong(a)

        val delegate = "hh:mm"

        return DateFormat.format(delegate, calendar.time) as String
    }

    fun GetDateTimeComment(milli: String): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = java.lang.Long.parseLong(DateMilli(milli))
        val mYear = calendar.get(Calendar.YEAR)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)

        val month_date = SimpleDateFormat("MMM")
        val month_name = month_date.format(calendar.time)
        val delegate = "hh:mm aaa"
        val time = DateFormat.format(delegate, calendar.time) as String

        return "$month_name $mDay, $mYear $time"
    }

    fun DateMilli(date: String): String {
        return date.replace("\\D+".toRegex(), "")
    }


    fun ClearAllFragments() {
        for (loop in 0 until ActivityBase.activity!!.supportFragmentManager.backStackEntryCount) {
            ActivityBase.activity!!.supportFragmentManager.popBackStackImmediate()
        }
    }

    companion object {
        var searchView: SearchView? = null

        fun getDataColumn(context: Context, uri: Uri, selection: String, selectionArgs: Array<String>): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)
            try {
                cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val column_index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(column_index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun performTransition(containerId: Int, fragment: Fragment, tag: String?) {

        val MOVE_DEFAULT_TIME: Long = 0
        val FADE_DEFAULT_TIME: Long = 300

        val previousFragment = ActivityBase.activity.supportFragmentManager.findFragmentById(containerId)
        val nextFragment = fragment
        val fragmentTransaction = ActivityBase.activity.supportFragmentManager.beginTransaction()

        // 1. Exit for Previous Fragment
        val exitFade = Fade()
        exitFade.duration = FADE_DEFAULT_TIME
        previousFragment!!.exitTransition = exitFade

        // 2. Shared Elements Transition
        val enterTransitionSet = TransitionSet()
        enterTransitionSet.addTransition(TransitionInflater.from(ActivityBase.activity).inflateTransition(android.R.transition.explode))
        enterTransitionSet.duration = MOVE_DEFAULT_TIME
        enterTransitionSet.startDelay = FADE_DEFAULT_TIME
        nextFragment.sharedElementEnterTransition = enterTransitionSet

        // 3. Enter Transition for New Fragment
        val enterFade = Fade()
        enterFade.duration = FADE_DEFAULT_TIME
        nextFragment.enterTransition = enterFade

        fragmentTransaction.add(containerId, nextFragment)
        if (tag != null) fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        when (ActivityBase.activity) {

        }
    }
}