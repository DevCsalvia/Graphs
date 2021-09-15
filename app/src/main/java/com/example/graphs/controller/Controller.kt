package com.example.graphs.controller

import android.content.Context
import android.os.Bundle
import android.provider.SyncStateContract
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.graphs.R
import com.example.graphs.models.Converter
import com.example.graphs.models.ViewModel
import com.example.graphs.views.Cartesian
import kotlin.math.sin
import kotlin.math.sqrt

class Controller: Fragment() {

    companion object {
        fun newInstance() = Controller()
    }
    private lateinit var viewModel: ViewModel
    private var cartesianSys: Cartesian? = null
    private var editTextStartOx: EditText? = null
    private var editTextEndOx: EditText? = null
    private var editTextStartOy: EditText? = null
    private var editTextEndOy: EditText? = null
    private var strokeWidth: EditText? = null
    private var graphColor: Spinner? = null
    private var errorTextView: TextView? = null
    private lateinit var converter: Converter
    var items: Array<String> = arrayOf<String>("red", "blue", "yellow")
    private var visibilityBtn: Button? = null
    private var func1Button: Button? = null
    private var func2Button: Button? = null

    private fun func1(x: Float) = sin(x) * sin(x) - x / 5
    private fun func2(x: Float) = sqrt((4 * x * x - 100) / 25)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        cartesianSys = activity?.findViewById<Cartesian>(R.id.cartesian)


        graphColor = activity?.findViewById<Spinner>(R.id.graph_color)?.apply {
            var adapter: ArrayAdapter<String> =  ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, items)
            this.adapter = adapter
            var index: Int? = null
            for(i in 0 until items.size)
                if(viewModel.graphColor!!.value != null) {
                    if (viewModel.graphColor!!.value == items[i]) {
                        index = i
                    }
                }
            this.setSelection(index?:0)

            this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                    cartesianSys?.changeColor(parent.selectedItem.toString())
                    viewModel.graphColor?.postValue(parent.selectedItem.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

        editTextStartOx = activity?.findViewById<EditText>(R.id.ox_start)?.apply {
            this.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if(editTextStartOx?.text.toString() == "" || editTextStartOx?.text.toString() == "-") viewModel.startOx?.postValue(0.0F)
                    else {
                        viewModel.startOx?.postValue(editTextStartOx?.text.toString().toFloat())
                        viewModel.points?.postValue(null)
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, before: Int) {
                }
            })
        }

        editTextEndOx = activity?.findViewById<EditText>(R.id.ox_end)?.apply {
            this.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if(editTextEndOx?.text.toString() == "" || editTextEndOx?.text.toString() == "-") viewModel.endOx?.postValue(0.0F)
                    else {
                        viewModel.endOx?.postValue(editTextEndOx?.text.toString().toFloat())
                        viewModel.points?.postValue(null)
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, before: Int) {
                }
            })
        }

        editTextStartOy = activity?.findViewById<EditText>(R.id.oy_start)?.apply {
            this.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if(editTextStartOy?.text.toString() == "" || editTextStartOy?.text.toString() == "-") viewModel.startOy?.postValue(0.0F)
                    else {
                        viewModel.startOy?.postValue(editTextStartOy?.text.toString().toFloat())
                        viewModel.points?.postValue(null)
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, before: Int) {
                }
            })
        }

        editTextEndOy = activity?.findViewById<EditText>(R.id.oy_end)?.apply {
            this.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if(editTextEndOy?.text.toString() == "" || editTextEndOy?.text.toString() == "-") viewModel.endOy?.postValue(0.0F)
                    else {
                        viewModel.endOy?.postValue(editTextEndOy?.text.toString().toFloat())
                        viewModel.points?.postValue(null)
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, before: Int) {
                }
            })
        }

        strokeWidth = activity?.findViewById<EditText>(R.id.stroke_width)?.apply {
            this.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if(strokeWidth?.text.toString() == "") viewModel.strokeWidth?.postValue(2F)
                    else viewModel.strokeWidth?.postValue(strokeWidth?.text.toString().toFloat())
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, before: Int) {
                }
            })
        }

        errorTextView = activity?.findViewById<TextView>(R.id.error_textView)?.apply {
            this.visibility = android.view.View.INVISIBLE
        }

        // btns
        visibilityBtn = activity?.findViewById<Button>(R.id.visibilityBtn)
        func1Button = activity?.findViewById<Button>(R.id.func1Btn)
        func2Button = activity?.findViewById<Button>(R.id.func2Btn)


        func1Button?.setOnClickListener {
            if(viewModel.startOx!!.value != null && viewModel.endOx!!.value != null && viewModel.strokeWidth!!.value != null){
                if(viewModel.startOx!!.value!! < viewModel.endOx!!.value!!) {
                    if(viewModel.startOy!!.value!! < viewModel.endOy!!.value!!) {
                        errorTextView?.visibility = android.view.View.INVISIBLE
                        visibilityBtn?.text = "Hide"
                        visibilityBtn?.visibility = android.view.View.VISIBLE
                        cartesianSys?.visibility = android.view.View.VISIBLE

                        // hiding fields
                        activity?.findViewById<TextView>(R.id.textView)?.visibility = android.view.View.INVISIBLE
                        activity?.findViewById<TextView>(R.id.textView2)?.visibility = android.view.View.INVISIBLE
                        activity?.findViewById<TextView>(R.id.textView3)?.visibility = android.view.View.INVISIBLE
                        activity?.findViewById<TextView>(R.id.textView4)?.visibility = android.view.View.INVISIBLE
                        activity?.findViewById<TextView>(R.id.textView5)?.visibility = android.view.View.INVISIBLE
                        activity?.findViewById<TextView>(R.id.textView6)?.visibility = android.view.View.INVISIBLE
                        editTextEndOx?.visibility = android.view.View.INVISIBLE
                        editTextStartOx?.visibility = android.view.View.INVISIBLE
                        editTextEndOy?.visibility = android.view.View.INVISIBLE
                        editTextStartOy?.visibility = android.view.View.INVISIBLE
                        strokeWidth?.visibility = android.view.View.INVISIBLE
                        graphColor?.visibility = android.view.View.INVISIBLE
                        func1Button?.visibility = android.view.View.INVISIBLE
                        func2Button?.visibility = android.view.View.INVISIBLE

                        // converter init
                        converter = Converter(viewModel.startOx!!.value!!, viewModel.endOx!!.value!!, cartesianSys!!.width.toFloat(), viewModel.startOy!!.value!!, viewModel.endOy!!.value!!, cartesianSys!!.height.toFloat())

                        cartesianSys?.converter = converter;
                        cartesianSys?.StartX = viewModel.startOx!!.value!!
                        cartesianSys?.EndX = viewModel.endOx!!.value!!
                        cartesianSys?.StartY = viewModel.startOy!!.value!!
                        cartesianSys?.EndY = viewModel.endOy!!.value!!

                        // points of the list calculation
                        if(viewModel.points?.value == null || viewModel.points?.value.isNullOrEmpty() || viewModel.lastFunction!!.value!! != 1) {
                            var points: MutableList<Float> = mutableListOf<Float>()
                            var size: Int = cartesianSys?.width?:1
                            for(i in 0 until size) points.add(converter.toPointsY(this.func1(converter.toCartesianX(i.toFloat()))))
                            viewModel.points?.postValue(points)
                            cartesianSys?.updateGraph(points, viewModel.strokeWidth!!.value!!, viewModel.graphColor!!.value!!)
                        }
                        else if(viewModel.points?.value != null) cartesianSys?.updateGraph(viewModel.points!!.value!!, viewModel.strokeWidth!!.value!!, viewModel.graphColor!!.value!!)
                        viewModel.currentFunction?.postValue(1)
                        viewModel.lastFunction?.postValue(1)
                    }
                }
            }
        }

        func2Button?.setOnClickListener {
            if (viewModel.startOx!!.value != null && viewModel.endOx!!.value != null && viewModel.strokeWidth!!.value != null) {
                if (viewModel.startOx!!.value!! < viewModel.endOx!!.value!!) {
                    if (viewModel.startOy!!.value!! < viewModel.endOy!!.value!!) {
                        errorTextView?.visibility = android.view.View.INVISIBLE
                        visibilityBtn?.text = "Hide"
                        visibilityBtn?.visibility = android.view.View.VISIBLE
                        cartesianSys?.visibility = android.view.View.VISIBLE

                        // hiding fields
                        activity?.findViewById<TextView>(R.id.textView)?.visibility = android.view.View.INVISIBLE
                        activity?.findViewById<TextView>(R.id.textView2)?.visibility = android.view.View.INVISIBLE
                        activity?.findViewById<TextView>(R.id.textView3)?.visibility = android.view.View.INVISIBLE
                        activity?.findViewById<TextView>(R.id.textView4)?.visibility = android.view.View.INVISIBLE
                        activity?.findViewById<TextView>(R.id.textView5)?.visibility = android.view.View.INVISIBLE
                        activity?.findViewById<TextView>(R.id.textView6)?.visibility = android.view.View.INVISIBLE
                        editTextEndOx?.visibility = android.view.View.INVISIBLE
                        editTextStartOx?.visibility = android.view.View.INVISIBLE
                        editTextEndOy?.visibility = android.view.View.INVISIBLE
                        editTextStartOy?.visibility = android.view.View.INVISIBLE
                        strokeWidth?.visibility = android.view.View.INVISIBLE
                        graphColor?.visibility = android.view.View.INVISIBLE
                        func1Button?.visibility = android.view.View.INVISIBLE
                        func2Button?.visibility = android.view.View.INVISIBLE

                        // converter init
                        converter = Converter(
                            viewModel.startOx!!.value!!,
                            viewModel.endOx!!.value!!,
                            cartesianSys!!.width.toFloat(),
                            viewModel.startOy!!.value!!,
                            viewModel.endOy!!.value!!,
                            cartesianSys!!.height.toFloat()
                        )

                        cartesianSys?.converter = converter;
                        cartesianSys?.StartX = viewModel.startOx!!.value!!
                        cartesianSys?.EndX = viewModel.endOx!!.value!!
                        cartesianSys?.StartY = viewModel.startOy!!.value!!
                        cartesianSys?.EndY = viewModel.endOy!!.value!!

                        // points of the list calculation
                        if (viewModel.points?.value == null || viewModel.points?.value.isNullOrEmpty() || viewModel.lastFunction!!.value!! != 2) {
                            var points: MutableList<Float> = mutableListOf<Float>()
                            var size: Int = cartesianSys?.width ?: 1
                            for (i in 0 until size) points.add(
                                converter.toPointsY(
                                    this.func2(
                                        converter.toCartesianX(i.toFloat())
                                    )
                                )
                            )
                            viewModel.points?.postValue(points)
                            cartesianSys?.updateGraph(
                                points,
                                viewModel.strokeWidth!!.value!!,
                                viewModel.graphColor!!.value!!
                            )
                        } else if (viewModel.points?.value != null) cartesianSys?.updateGraph(
                            viewModel.points!!.value!!,
                            viewModel.strokeWidth!!.value!!,
                            viewModel.graphColor!!.value!!
                        )
                        viewModel.currentFunction?.postValue(2)
                        viewModel.lastFunction?.postValue(2)
                    }
                }
            }
        }
            visibilityBtn?.setOnClickListener {
                viewModel.currentFunction?.postValue(null)
                visibilityBtn?.visibility = android.view.View.INVISIBLE
                cartesianSys?.visibility = android.view.View.INVISIBLE

                // showing fields
                activity?.findViewById<TextView>(R.id.textView)?.visibility = android.view.View.VISIBLE
                activity?.findViewById<TextView>(R.id.textView2)?.visibility = android.view.View.VISIBLE
                activity?.findViewById<TextView>(R.id.textView3)?.visibility = android.view.View.VISIBLE
                activity?.findViewById<TextView>(R.id.textView4)?.visibility = android.view.View.VISIBLE
                activity?.findViewById<TextView>(R.id.textView5)?.visibility = android.view.View.VISIBLE
                activity?.findViewById<TextView>(R.id.textView6)?.visibility = android.view.View.VISIBLE
                editTextEndOx?.visibility = android.view.View.VISIBLE
                editTextStartOx?.visibility = android.view.View.VISIBLE
                editTextEndOy?.visibility = android.view.View.VISIBLE
                editTextStartOy?.visibility = android.view.View.VISIBLE
                strokeWidth?.visibility = android.view.View.VISIBLE
                graphColor?.visibility = android.view.View.VISIBLE
                func1Button?.visibility = android.view.View.VISIBLE
                func2Button?.visibility = android.view.View.VISIBLE
            }
    }

    override fun onStart() {
        super.onStart()
        val prefs = activity?.getSharedPreferences("com.example.graphs", Context.MODE_PRIVATE)
        prefs?.run {
            val strokeWidthVal = getFloat("com.example.graphs.stoke_width", 5F)
            viewModel.strokeWidth?.postValue(strokeWidthVal)
                strokeWidth?.setText(strokeWidthVal.toString() , TextView.BufferType.EDITABLE)

            val graphColorVal = getString("com.example.graphs.graph_color", "")
            viewModel.graphColor?.postValue(graphColorVal)
            var index: Int? = null
            for(i in 0 until items.size)
                if(graphColorVal != null) {
                    if (graphColorVal == items[i]) {
                        index = i
                    }
                }
            graphColor?.setSelection(index?:0)

            val startOxVal = getFloat("com.example.graphs.start_ox", 0F)
            viewModel.startOx?.postValue(startOxVal)
            val endOxVal = getFloat("com.example.graphs.end_ox", 0F)
            viewModel.endOx?.postValue(endOxVal)
                editTextStartOx?.setText(startOxVal.toString() , TextView.BufferType.EDITABLE)
                editTextEndOx?.setText(endOxVal.toString() , TextView.BufferType.EDITABLE)

            // limitations
            val startOyVal = getFloat("com.example.graphs.start_oy", 0F)
            viewModel.startOy?.postValue(startOyVal)
            val endOyVal = getFloat("com.example.graphs.end_oy", 0F)
            viewModel.endOy?.postValue(endOyVal)
                editTextStartOy?.setText(startOyVal.toString() , TextView.BufferType.EDITABLE)
                editTextEndOy?.setText(endOyVal.toString() , TextView.BufferType.EDITABLE)

            // points
            val pointsList = MutableList(cartesianSys?.width?:1){
                getFloat("com.example.graphs.points_"+it.toString(), 0F)
            }
            viewModel.points?.postValue(pointsList.toMutableList())
        }
    }

    override fun onStop() {
        super.onStop()
        val prefs = activity?.getSharedPreferences("com.example.graphs", Context.MODE_PRIVATE)
        prefs?.run {
            edit().run {
                putFloat("com.example.graphs.stoke_width", viewModel.strokeWidth?.value?:5F)

                putFloat("com.example.graphs.start_ox", viewModel.startOx?.value?:0F)
                putFloat("com.example.graphs.end_ox", viewModel.endOx?.value?:0F)
                putFloat("com.example.graphs.start_oy", viewModel.startOy?.value?:0F)
                putFloat("com.example.graphs.end_oy", viewModel.endOy?.value?:0F)

                putString("com.example.graphs.graph_color", viewModel.graphColor!!.value?:"green")

                viewModel.points?.value?.forEachIndexed() { id, value ->
                    putFloat("com.example.graphs.points_" + id.toString(), value)
                }

                apply()
            }
        }
    }
}