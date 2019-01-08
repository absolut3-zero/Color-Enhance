package xyz.z3ro.colorenhance.model

import android.os.Parcel
import android.os.Parcelable

data class KCAL(
    val name: String = "",
    val switch: String = "",
    val rgb: String = "",
    val rgbMultiplier: String = "",
    val saturationIntensity: String = "",
    val hue: String = "",
    val screenValue: String = "",
    val contrast: String = ""
) : Parcelable {

    constructor(input: Parcel?) : this(
        input!!.readString()!!,
        input.readString()!!,
        input.readString()!!,
        input.readString()!!,
        input.readString()!!,
        input.readString()!!,
        input.readString()!!,
        input.readString()!!
    )

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<KCAL> = object : Parcelable.Creator<KCAL> {
            override fun createFromParcel(input: Parcel?): KCAL {
                return KCAL(input)
            }

            override fun newArray(size: Int): Array<KCAL> {
                return Array(size) { KCAL() }
            }
        }
    }


    override fun writeToParcel(destination: Parcel?, p1: Int) {
        if (destination != null) {
            destination.writeString(name)
            destination.writeString(switch)
            destination.writeString(rgb)
            destination.writeString(rgbMultiplier)
            destination.writeString(saturationIntensity)
            destination.writeString(hue)
            destination.writeString(screenValue)
            destination.writeString(contrast)
        }
    }

    override fun describeContents(): Int {
        return 0
    }
}