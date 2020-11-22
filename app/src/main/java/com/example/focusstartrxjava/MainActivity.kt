package com.example.focusstartrxjava

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_scrolling.*
import java.util.concurrent.TimeUnit



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val TEXT = textViewText.text
        var staticCount: Int
        var dynamicCount: Int
        var searchString: String
        var disposable: Disposable? = null
        var compositeDisposable = CompositeDisposable()

        fun getEditable() = Observable.create { emitter: ObservableEmitter<Editable> ->
            val watcher: TextWatcher = object : TextWatcher {
                override fun beforeTextChanged(chSeq: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(chSeq: CharSequence, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(editable: Editable) {
                    if (!emitter.isDisposed) {
                        emitter.onNext(editable)
                    }
                }
            }
            emitter.setCancellable {
                editTextString.removeTextChangedListener(watcher)
            }
            editTextString.addTextChangedListener(watcher)
        }.observeOn(Schedulers.io())


        fun getflowable() = Flowable
                .fromIterable(TEXT
                        .toString()
                        .replace("[,.!?:;()-«»]", "")
                        .split(" ")
                )


        fun getParallel() = getflowable().concatMap {
            Flowable.just(it).observeOn(Schedulers.io())
        }


        /*        val observable = Observable.create { emitter: ObservableEmitter<Editable> ->
            val watcher: TextWatcher = object : TextWatcher {
                override fun beforeTextChanged(chSeq: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(chSeq: CharSequence, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(editable: Editable) {
                    if (!emitter.isDisposed) { //если еще не отписались
                        emitter.onNext(editable) //отправляем текущее состояние
                    }
                }
            }
            emitter.setCancellable {
                editTextString.removeTextChangedListener(watcher)
            } //удаляем листенер при отписке от observable

            editTextString.addTextChangedListener(watcher)
        }.observeOn(Schedulers.io())

 */
            val observable = getEditable()
            val parallel =  getParallel()

 /*           getEditable()
                    .debounce(700L, TimeUnit.MILLISECONDS)
                    .subscribe() {
                        dynamicCount = 0}.
                    .observeOn(AndroidSchedulers.mainThread())
*/


            observable.debounce(700L, TimeUnit.MILLISECONDS).subscribe() {
            dynamicCount = 0
            Handler(Looper.getMainLooper()).post {
                textViewCounterDynamic.text = (dynamicCount).toString()
            }

            searchString = it.toString()
            if (!searchString.equals("")) {
                staticCount = maxOf(TEXT.split(searchString).count() - 1, 0)
                Handler(Looper.getMainLooper()).post {
                    textViewCounterStatic.text = (staticCount).toString()
                }

                parallel.subscribe() {
                    if (it.contains(searchString)) {
                        Handler(Looper.getMainLooper()).post {
                            textViewCounterDynamic.text = (++dynamicCount).toString()
                        }
                    }
                }
            } else {
                staticCount = 0
                Handler(Looper.getMainLooper()).post {
                    textViewCounterStatic.text = (staticCount).toString()
                }
            }
        }

    }


}