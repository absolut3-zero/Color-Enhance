package xyz.z3ro.colorenhance.utility.kcal

interface KCALInterface {

    val isSupported: Boolean

    var enabled: Boolean

    var colors: IntArray

    var rgbMultiplier: Int

    var saturationIntensity: Int

    var hue: Int

    var screenValue: Int

    var contrast: Int

    val getImplementationName: String
    val getImplementationSwitchPath: String
    val getImplementationFilePaths: String
    val getImplementationFormat: String
}