package com.sefatombul.gcase.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.os.postDelayed
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.sefatombul.gcase.R
import com.sefatombul.gcase.ui.MainActivity
import com.sefatombul.gcase.utils.Constants.UNKNOWN_ERROR
import retrofit2.Response
import timber.log.Timber
import java.net.ConnectException


fun <T> LiveData<Resource<T>?>.observeCall(
    activity: Activity,
    lifecycleOwner: LifecycleOwner,
    error: ((message: String?) -> Unit)? = null,
    loading: (() -> Unit)? = null,
    isAutoShowLoading:Boolean = true,
    success: ((data: T?) -> Unit)? = null,
    finally: (() -> Unit)? = null,
) {
    this.observe(lifecycleOwner) { response ->
        when (response) {
            is Resource.Error -> {
                error?.let { f ->
                    f(response.message)
                }
                finally?.let {
                    it()
                }
                if(isAutoShowLoading){
                    (activity as? MainActivity)?.let {
                        it.hideLoading()
                    }
                }
            }
            is Resource.Success -> {
                success?.let { f ->
                    f(response.data)
                }
                finally?.let {
                    it()
                }
                if(isAutoShowLoading){
                    (activity as? MainActivity)?.let {
                        it.hideLoading()
                    }
                }
            }
            is Resource.Loading -> {
                loading?.let { f ->
                    f()
                }
                if(isAutoShowLoading){
                    (activity as? MainActivity)?.let {
                        it.showLoading()
                    }
                }
            }
            else -> {}
        }
    }
}

suspend fun <T> globalSafeCall(
    context: Context,
    f: suspend () -> Response<T>
): Resource<T>? {
    var res: Resource<T>? = null
    if (NetworkHelper.hasInternetConnection(context)) {
        try {
            val response = f()
            res = RequestHelper.handleResponse(response)
        } catch (ce: ConnectException) {
            res = Resource.Error(Constants.NO_INTERNET_CONNECTION)
        } catch (e: Exception) {
            res = Resource.Error(UNKNOWN_ERROR)
            Timber.e(UNKNOWN_ERROR)
            e.printStackTrace()
        }
    } else {
        res = Resource.Error(Constants.NO_INTERNET_CONNECTION)
        Toast.makeText(
            context,
            context.resources.getString(R.string.check_internet_connection),
            Toast.LENGTH_SHORT
        ).show()
    }
    return res
}


@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}


fun ImageView.loadImage(
    image: String?,
    scaleType: GlideImageScaleEnum = GlideImageScaleEnum.NONE,
    isPlaceholder: Boolean = false,
    @DrawableRes placeHolder: Int? = null,
    radius: Int? = null
) {
    val glide = Glide.with(this.context).load(image)
    if (isPlaceholder) {
        placeHolder?.let { holder ->
            glide.placeholder(holder)
                .error(holder)
        } ?: run {
            //glide.placeholder(R.drawable.ic_glider_placeholder)
            // .error(R.drawable.ic_glider_placeholder)

        }
    }
    when (scaleType) {
        GlideImageScaleEnum.NONE -> {

        }
        GlideImageScaleEnum.CENTER_CROP -> {
            glide.centerCrop()
        }
        GlideImageScaleEnum.FIT_CENTER -> {
            glide.fitCenter()
        }
        GlideImageScaleEnum.CIRCLE_CROP -> {
            glide.circleCrop()

        }
    }

    if (scaleType != GlideImageScaleEnum.CIRCLE_CROP) {
        radius?.let { rad ->
            glide.transform(
                RoundedCornersTransformation(
                    rad, 0,
                    RoundedCornersTransformation.CornerType.ALL
                )
            )
        }
    }
    glide.into(this)
}


fun Context.dpConvertToPx(dip: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip,
        this.resources.displayMetrics
    )
}

/**
 * Mevcut fragmenttan önce acılmıs olan herhangi bir fragmenta geri atlanılabilir.
 * @author Sefa Tombul
 * @since 27.03.2023
 **/
@SuppressLint("RestrictedApi")
fun <T> NavController.backStackCustomInclusive(
    @IdRes destinationId: Int,
    inclusive: Boolean,
    key: String,
    value: T
): Boolean {
    var control = false
    this.backQueue?.reversed()?.forEach { navBackStackEntry ->
        navBackStackEntry?.let {
            Timber.e("destination.id : ${it.destination.id} destination.displayName : ${it.destination.displayName}")
            if (control) {
                if (inclusive) {
                    it.savedStateHandle.set(key, value)
                }
                this.popBackStack(destinationId, inclusive)
                return true
            }
            if (it.destination.id == destinationId) {
                if (!inclusive) {
                    it.savedStateHandle.set(key, value)
                }
                control = true
            }
        }
    }

    return false
}

/**
 * Mevcut fragmenttan bir önceki farklı fragmenta dönülür.
 * @author Sefa Tombul
 * @since 27.03.2023
 * @param key Gönderilecek datanın keyi,
 * @param data Gönderilecek data
 **/
@SuppressLint("RestrictedApi")
fun NavController.backStackCustom(key: String? = null, data: Any? = null) {
    var selfDestination: NavBackStackEntry? = null
    var navBackStackEntry: NavBackStackEntry? = null
    this.currentBackStackEntry?.let {
        selfDestination = it
    }

    this.backQueue?.reversed()?.let {
        it.forEach {
            if (navBackStackEntry == null && selfDestination?.destination?.id != it.destination.id) {
                navBackStackEntry = it
            }
        }
    }

    navBackStackEntry?.let { navBackStack ->
        if (!key.isNullOrEmpty() && data != null) {
            navBackStack.savedStateHandle?.set(key, data)
        }
        this.popBackStack(navBackStack.destination.id, false)
    } ?: run {
        selfDestination?.let { selfNav ->
            this.popBackStack(selfNav.destination.id, true)
        } ?: run {
            Timber.e("selfDestination is null")
        }
    }
}


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.remove() {
    this.visibility = View.GONE
}

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}


/**
 * Android 12'de notification alınamamaası sorununu çözmek için flag değerleri güncellendi.
 * */
fun getPendingIntentFlags(): Int {
    return if (Build.VERSION.SDK_INT >= 23) 1140850688 else 1073741824
}

/**
 * Function that checks if the app is running in the foreground
 * **/
fun Context.isApplicationRun(): Boolean {
    val manager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val runningApplicationTaskInformation = manager.getRunningTasks(1)
    val componentInformation = runningApplicationTaskInformation[0].topActivity
    componentInformation?.let { info ->
        if (info.packageName == this.packageName) {
            return true
        }
    }
    return false
}

fun getDeviceName(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.lowercase().startsWith(manufacturer.lowercase())) {
        capitalize(model)
    } else {
        capitalize(manufacturer) + " " + model
    }
}


private fun capitalize(s: String?): String {
    if (s == null || s.isEmpty()) {
        return ""
    }
    val first = s[0]
    return if (Character.isUpperCase(first)) {
        s
    } else {
        Character.toUpperCase(first).toString() + s.substring(1)
    }
}

fun androidVersion(): String {
    return " Android SDK: ${Build.VERSION.SDK_INT} (Android ${Build.VERSION.RELEASE}) "
}

fun applicationVersion(context: Context): String {
    try {
        val info = context.packageManager.getPackageInfo(context.packageName, 0)
        return "\nversionName : ${info.versionName} \nversionCode : ${
            PackageInfoCompat.getLongVersionCode(
                info
            ).toInt()
        }"
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return ""
}

fun handlerPost(c: Context, f: () -> Unit) {
    android.os.Handler(Looper.getMainLooper()).post {
        f()
    }
}

fun handlerPostDelay(c: Context, delay: Long, f: () -> Unit) {
    android.os.Handler(Looper.getMainLooper()).postDelayed(delay) {
        f()
    }
}

fun NavController.safeNavigate(@IdRes direction: Int, bundle: Bundle? = null) {
    currentDestination?.getAction(direction)?.let {
        navigate(direction, bundle)
    } ?: run {
        try {
            navigate(direction, bundle)
        } catch (e: Exception) {
            Timber.e("SafeNavigate - not found direction (action or id)")
            e.printStackTrace()
        }
    }
}