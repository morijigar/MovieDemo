package com.example.apifetch.base

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.apifetch.R
import com.example.apifetch.util.NetworkUtils


abstract class BaseActivity<T : ViewDataBinding?, V : ViewModel?> :
    AppCompatActivity() {


    //var isGranted = true

    var viewDataBinding: T? = null

    private var mViewModel: V? = null

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
    }

    open fun onFragmentAttached() {}

    open fun onFragmentDetached(tag: String?) {}

    /* override fun attachBaseContext(newBase: Context?) {
         super.attachBaseContext(newBase)
     }*/

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String?): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission!!) == PackageManager.PERMISSION_GRANTED
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    val isNetworkConnected: Boolean
        get() = NetworkUtils.isNetworkConnected(applicationContext)

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(
        permissions: Array<String?>?,
        requestCode: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions!!, requestCode)
        }
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView<T>(this, layoutId)
        mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.executePendingBindings()
    }

    fun setFragmentReplaceBackPress(isForNotBack: Boolean = false, myFragment: Fragment) {
        if (isForNotBack) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, myFragment)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, myFragment)
                .addToBackStack(myFragment::class.java.simpleName)
                .commit()
        }

    }

    fun backStackUpTo(name: String) {
        val fm = supportFragmentManager
        fm.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun backStackUpToLast() {
        val fm = supportFragmentManager
        val count = fm.backStackEntryCount

        for (i in count downTo 0) {
            fm.popBackStack()
        }

        /* for (i in count downTo 1) {
             fm.popBackStack()
         }*/

    }

    /*fun isPermissionsAllowed(permissions: Array<String>, shouldRequestIfNotAllowed: Boolean = false, requestCode: Int = -1): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                isGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
                if (!isGranted)
                    break
            }
        }
        if (!isGranted && shouldRequestIfNotAllowed) {
            if (requestCode.equals(-1))
                throw RuntimeException("Send request code in third parameter")
            requestRequiredPermissions(permissions, requestCode)
        }

        return isGranted
    }

    fun requestRequiredPermissions(permissions: Array<String>, requestCode: Int) {
        val pendingPermissions: ArrayList<String> = ArrayList()
        permissions.forEachIndexed { index, permission ->
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED)
                pendingPermissions.add(permission)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val array = arrayOfNulls<String>(pendingPermissions.size)
            pendingPermissions.toArray(array)
            requestPermissions(array, requestCode)
        }
    }

    fun isAllPermissionsGranted(grantResults: IntArray): Boolean {
        var isGranted = true
        for (grantResult in grantResults) {
            isGranted = grantResult.equals(PackageManager.PERMISSION_GRANTED)
            if (!isGranted)
                break
        }
        return isGranted
    }*/

    fun isOnline(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }
}