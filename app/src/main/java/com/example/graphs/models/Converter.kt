package com.example.graphs.models

class Converter(StartX: Float, EndX: Float, widthOfX: Float, StartY: Float, EndY: Float, heightOfY: Float) {
    private var _StartX: Float
    var StartX: Float
        get() = _StartX
        set(value) { _StartX = value }

    private var _EndX: Float
    var EndX: Float
        get() = _EndX
        set(value) { _EndX = value }

    private var _widthOfX: Float
    var widthOfX: Float
        get() = _widthOfX
        set(value) { _widthOfX = value }

    private var _StartY: Float
    var StartY: Float
        get() = _StartY
        set(value) { _StartY = value }

    private var _EndY: Float
    var EndY: Float
        get() = _EndY
        set(value) { _EndY = value }

    private var _heightOfY: Float
    var heightOfY: Float
        get() = _heightOfY
        set(value) { _heightOfY = value }

    init{
        this._StartX = StartX
        this._EndX = EndX
        this._widthOfX = widthOfX
        this._StartY = StartY
        this._EndY = EndY
        this._heightOfY = heightOfY
    }
    fun toCartesianX(value: Float): Float{
        return (this.StartX + (value * (this.EndX - this.StartX) / this.widthOfX ))
    }
    fun toPointsX(value: Float): Float{
        return (((this.widthOfX / (this.EndX - this.StartX)) * value))
    }
    fun toPointsY(value: Float): Float{
        return (((this.heightOfY) - ((value - this.StartY) * this.heightOfY / (this.EndY - this.StartY))))
    }
}