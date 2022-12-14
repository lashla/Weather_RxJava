package com.lasha.rxweather.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.lasha.rxweather.R
import kotlinx.android.synthetic.main.forecast_view.view.*

class ForecastView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    init {
        inflate(context, R.layout.forecast_view, this)

        setMeasuredDimension(57,70)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ForecastView)
        ivTempOfTime.setImageDrawable(attributes.getDrawable(R.styleable.ForecastView_image))
        tvTime.text = attributes.getString(R.styleable.ForecastView_timeText)
        tvTimeTemp.text = attributes.getString(R.styleable.ForecastView_temperatureText)
        attributes.recycle()

    }

}