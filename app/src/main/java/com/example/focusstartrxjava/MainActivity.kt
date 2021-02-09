package com.example.focusstartrxjava

import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_scrolling.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private var compositeDisposable = CompositeDisposable()
    private var staticCounter: Int = 0
    private var dynamicCounter: Int = 0
    private var staticSearchString: String = ""
    private var dynamicSearchString: String = ""
    private lateinit var bigText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val TIME_OUT = 700L
        bigText = textViewText.text.toString()

        compositeDisposable.add(
                getEditable(editTextString)
                        .subscribeOn(Schedulers.io())
                        .debounce(TIME_OUT, TimeUnit.MILLISECONDS)
                        .map(::setStatics)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe() {
                            textViewCounterStatic.text = (staticCounter).toString()
                        }
        )

        compositeDisposable.add(
                getEditable(editTextString)
                        .subscribeOn(Schedulers.io())
                        .debounce(TIME_OUT, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map {
                            setDynamicCounterIsZero()
                            textViewCounterDynamic.text = (dynamicCounter).toString()
                            it
                        }
                        .observeOn(Schedulers.io())
                        .map {
                            dynamicSearchString = it.toString()
                            getParallelflowable(bigText)
                                    .subscribeOn(Schedulers.io())
                                    .map(::setDynamicCounter)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe() {
                                        textViewCounterDynamic.text = (dynamicCounter).toString()
                                    }
                        }
                        .subscribe()
        )
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun setStatics(editable: Editable) {
        staticSearchString = editable.toString()
        staticCounter =
                if (staticSearchString.isEmpty())
                    0
                else {
                    maxOf(bigText.split(staticSearchString).count() - 1, 0)
                }
    }

    private fun setDynamicCounterIsZero() {
        dynamicCounter = 0
    }

    private fun setDynamicCounter(word: String = "") {
        if (dynamicSearchString.isEmpty()) {
            setDynamicCounterIsZero()
        } else if (word.contains(dynamicSearchString)) {
            dynamicCounter++
        }
    }
}