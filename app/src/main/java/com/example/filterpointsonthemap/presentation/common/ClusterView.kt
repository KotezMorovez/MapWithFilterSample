package com.example.filterpointsonthemap.presentation.common

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.filterpointsonthemap.R

class ClusterView(context: Context) : LinearLayout(context) {
    private val redText by lazy { findViewById<TextView>(R.id.text_red_pins) }
    private val greenText by lazy { findViewById<TextView>(R.id.text_green_pins) }
    private val blueText by lazy { findViewById<TextView>(R.id.text_blue_pins) }
    private val purpleText by lazy { findViewById<TextView>(R.id.text_purple_pins) }


    private val redLayout by lazy { findViewById<View>(R.id.layout_red_group) }
    private val greenLayout by lazy { findViewById<View>(R.id.layout_green_group) }
    private val blueLayout by lazy { findViewById<View>(R.id.layout_blue_group) }
    private val purpleLayout by lazy { findViewById<View>(R.id.layout_purple_group) }

    init {
        inflate(context, R.layout.cluster_view, this)
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        orientation = HORIZONTAL
        setBackgroundResource(R.drawable.cluster_view_background)
    }

    fun setData(placemarkTypes: List<PlacemarkType>) {
        PlacemarkType.entries.forEach {
            updateViews(placemarkTypes, it)
        }
    }

    private fun updateViews(
        placemarkTypes: List<PlacemarkType>,
        type: PlacemarkType
    ) {
        val (textView, layoutView) = when (type) {
            PlacemarkType.RED -> redText to redLayout
            PlacemarkType.GREEN -> greenText to greenLayout
            PlacemarkType.BLUE -> blueText to blueLayout
            PlacemarkType.PURPLE -> purpleText to purpleLayout
        }
        val value = if(placemarkTypes.isNotEmpty()) placemarkTypes.countTypes(type) else 0

        textView.text = value.toString()
        layoutView.isVisible = value != 0
    }

    private fun List<PlacemarkType>.countTypes(type: PlacemarkType) = count { it == type }
}

enum class PlacemarkType {
    RED,
    GREEN,
    BLUE,
    PURPLE
}

data class PlacemarkUserData(
    val name: String,
    val type: PlacemarkType,
)