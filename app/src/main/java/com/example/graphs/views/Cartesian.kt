package com.example.graphs.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.graphs.models.Converter

class Cartesian(context: Context, attrs: AttributeSet? = null): View(context, attrs) {
    constructor(context: Context) : this(context, null)

    // Variables To Paint Axes and Bg
    val paintBg: Paint = Paint()
    val paintAxis: Paint = Paint()
    val paintAxisSerif: Paint = Paint()
    val paintGraph: Paint = Paint()
    var paintAxisNumberSmall: Paint = Paint()
    var paintAxisNumberLarge: Paint = Paint()

    private var _converter: Converter ? = null
    var converter: Converter ?
        get() = _converter
        set(value) { this._converter = value }

    private var listOfPoints: MutableList<Float> = mutableListOf<Float>()
    fun updateGraph(listOfPoints: MutableList<Float>, strokeWidth: Float, color: String){
        this.listOfPoints = listOfPoints
        this.paintGraph.strokeWidth = strokeWidth
        if(color == "red") paintGraph.color = 0xffff0000.toInt();
        if(color == "blue") paintGraph.color = 0xff0000ff.toInt()
        if(color == "yellow") paintGraph.color = 0xffffff00.toInt()
    invalidate()
    }

    // Fields
    private var _StartX: Float? = null
    private var _EndX: Float? = null
    private var _StartY: Float? = null
    private var _EndY: Float? = null

    var StartX: Float?
        get() = _StartX
        set(value) { _StartX = value }
    var EndX: Float?
        get() = _EndX
        set(value) { _EndX = value }

    var StartY: Float?
        get() = _StartY
        set(value) { _StartY = value }
    var EndY: Float?
        get() = _EndY
        set(value) { _EndY = value }

    init {
        paintBg.color = 0xffedd3d9.toInt()
        paintAxis.color = 0x4cff053d9.toInt()
        paintAxis.strokeWidth = 5F
        paintAxisSerif.strokeWidth = 5F
        paintGraph.strokeWidth = 3F
        paintGraph.color = 0xff66ac4a.toInt()
        paintAxisNumberSmall.textSize = 40F
        paintAxisNumberLarge.textSize = 60F
    }

    fun changeColor(color: String): Unit{

        if(color == "red") paintGraph.color = 0xff000020.toInt()
        if(color == "blue") paintGraph.color = 0x0000ff20.toInt()
        if(color == "yellow") paintGraph.color = 0xffff0020.toInt()
        invalidate()
    }

    fun drawAxis(canvas: Canvas?) {
        canvas?.apply{
            var StartXInt: Int = StartX!!.toInt()
            var EndXInt: Int = EndX!!.toInt()
            var StartYInt: Int = StartY!!.toInt()
            var EndYInt: Int = EndY!!.toInt()

            if(StartX!! <= 0 && EndX!! >= 0) {
                drawLine(converter!!.toPointsX(- StartX!!), 0F, converter!!.toPointsX(- StartX!!), this@Cartesian.height.toFloat(), paintAxis )
                if(EndYInt - StartYInt <= 50)  {
                    for(i in StartYInt..EndYInt) {
                        var valOX: Float = converter?.toPointsX(0F - StartX!!)!!
                        var valOY: Float = converter?.toPointsY(i.toFloat())!!
                        drawLine(valOX + 20F, valOY, valOX - 20F, valOY, paintAxisSerif)
                        drawText(i.toString(), valOX + 30F, valOY, paintAxisNumberSmall)
                    }
                }
                else if(EndYInt - StartYInt > 50 && EndYInt - StartYInt <= 100) {
                    for(i in StartYInt..EndYInt step 10) {
                        var valOY: Float = converter?.toPointsY(i.toFloat())!!
                        var valOX: Float = converter?.toPointsX(0F - StartX!!)!!
                        drawLine(valOX + 20F, valOX, valOX - 20F, valOX , paintAxisSerif);
                        drawText(i.toString(), valOX + 30F, valOX, paintAxisNumberLarge)
                    }
                }
            }

            if(StartY!! <= 0 && EndY!! >= 0) {
                drawLine(0F, converter?.toPointsY(0F)!!, this@Cartesian.width.toFloat(), converter?.toPointsY(0F)!!, paintAxis)
                if(EndXInt - StartXInt <= 50){
                    for(i in StartXInt..EndXInt) {
                        drawLine(converter!!.toPointsX(i.toFloat() - StartX!!),converter!!.toPointsY(0F) + 20F, converter!!.toPointsX(i.toFloat() - StartX!!), converter!!.toPointsY(0F) - 20F, paintAxisSerif)
                        drawText(i.toString(), converter!!.toPointsX(i.toFloat() - StartX!!) - 10F, converter!!.toPointsY(0F) + 40F, paintAxisNumberSmall)
                    }
                }
                else if(EndXInt - StartXInt > 50 && EndXInt - StartXInt <= 100) {
                    for(i in StartXInt..EndXInt step 10) {
                        drawLine(converter!!.toPointsX(i.toFloat() - StartX!!),converter!!.toPointsY(0F) + 20F, converter!!.toPointsX(i.toFloat() - StartX!!), converter!!.toPointsY(0F) - 20F, paintAxisSerif)
                        drawText(i.toString(), converter!!.toPointsX(i.toFloat() - StartX!!) - 10F, converter!!.toPointsY(0F) + 40F, paintAxisNumberLarge)
                    }
                }
            }
        }
    }

    fun drawGraph(canvas: Canvas?) {
        canvas?.apply {
            val size: Int = this@Cartesian.listOfPoints.size
            if(size != 0) {
                for(i in 0..(size - 2)){
                    drawLine(i.toFloat(), this@Cartesian.listOfPoints[i], (i+1).toFloat(), this@Cartesian.listOfPoints[i+1], paintGraph)
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply{
            drawPaint(paintBg)
            drawAxis(this)
            drawGraph(this)
        }
    }
}
