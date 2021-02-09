package com.example.focusstartrxjava

import android.text.Editable
import android.text.TextWatcher
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers
import android.widget.EditText


fun getEditable(editTextString: EditText) = Observable.create { emitter: ObservableEmitter<Editable?> ->
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
}

fun getflowable(text: String) = Flowable
        .fromIterable(text
                .replace("[,.!?:;()-«»]", "")
                .split(" ")
        )

fun getParallelflowable(text: String) = getflowable(text).concatMap {
    Flowable.just(it).observeOn(Schedulers.io())
}

