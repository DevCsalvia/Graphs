package com.example.graphs.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModel: ViewModel() {
    var points: MutableLiveData<MutableList<Float>>? = MutableLiveData(mutableListOf())
    var startOx: MutableLiveData<Float>? = MutableLiveData<Float>(null)
    var endOx: MutableLiveData<Float>? = MutableLiveData<Float>(null)
    var startOy: MutableLiveData<Float>? = MutableLiveData<Float>(null)
    var endOy: MutableLiveData<Float>? = MutableLiveData<Float>(null)
    var strokeWidth: MutableLiveData<Float>? = MutableLiveData<Float>(null)
    var graphColor: MutableLiveData<String>? = MutableLiveData<String>(null)
    var currentFunction: MutableLiveData<Int>? = MutableLiveData<Int>(null)
    var lastFunction: MutableLiveData<Int>? = MutableLiveData<Int>(null)
}