package com.example.demoapplication.utils

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T>:MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()){
            Log.e("SingleLiveEvent","multiple observer registered")
        }
        super.observe(owner, Observer{
            if (pending.compareAndSet(true,false)){
                observer.onChanged(it)
            }
        })
    }

    @MainThread
    override fun setValue(@Nullable value: T?) {
        pending.set(true)
        super.setValue(value)
    }
    @MainThread
    fun call(){
        value = null
    }
}
fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>