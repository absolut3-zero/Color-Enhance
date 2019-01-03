package xyz.z3ro.colorenhance.utility.kcal

import xyz.z3ro.colorenhance.utility.Root
import java.io.File

class GenericKCALManager : KCALInterface {

    // File paths
    private val KCAL_SWITCH = "/sys/devices/platform/kcal_ctrl.0/kcal_enable"
    private val KCAL_COLOR = "/sys/devices/platform/kcal_ctrl.0/kcal"
    private val MIN_RGB_MULIPLIER = "/sys/devices/platform/kcal_ctrl.0/kcal_min"
    private val SAT_INTENSITY = "/sys/devices/platform/kcal_ctrl.0/kcal_sat"
    private val HUE = "/sys/devices/platform/kcal_ctrl.0/kcal_hue"
    private val SCREEN_VAL = "/sys/devices/platform/kcal_ctrl.0/kcal_val"
    private val CONTRAST = "/sys/devices/platform/kcal_ctrl.0/kcal_cont"

    override val isSupported: Boolean = File(KCAL_SWITCH).exists() || Root.doesFileExist(KCAL_SWITCH)

    override var enabled: Boolean
        get() = Root.readOneLine(KCAL_SWITCH) == "1"
        set(value) {
            if (value) Root.writeToFile("1", KCAL_SWITCH)
            else Root.writeToFile("0", KCAL_SWITCH)
        }

    override var colors: IntArray
        get() {
            val reading = Root.readOneLine(KCAL_COLOR)
            val colorReadings = reading.split(" ".toRegex())

            return intArrayOf(
                colorReadings[0].toInt(),
                colorReadings[1].toInt(),
                colorReadings[2].toInt()
            )
        }
        set(value) {
            Root.writeToFile("${value[0]} ${value[1]} ${value[2]}", KCAL_COLOR)
        }

    override var rgbMultiplier: Int
        get() = Root.readOneLine(MIN_RGB_MULIPLIER).toInt()
        set(value) {
            Root.writeToFile("$value", MIN_RGB_MULIPLIER)
        }

    override var saturationIntensity: Int
        get() = Root.readOneLine(SAT_INTENSITY).toInt()
        set(value) {
            Root.writeToFile("$value", SAT_INTENSITY)
        }

    override var hue: Int
        get() = Root.readOneLine(HUE).toInt()
        set(value) {
            Root.writeToFile("$value", HUE)
        }

    override var screenValue: Int
        get() = Root.readOneLine(SCREEN_VAL).toInt()
        set(value) {
            Root.writeToFile("$value", SCREEN_VAL)
        }

    override var contrast: Int
        get() = Root.readOneLine(CONTRAST).toInt()
        set(value) {
            Root.writeToFile("$value", CONTRAST)
        }

    
    override val getImplementationName = "Generic KCAL"

    override val getImplementationSwitchPath = KCAL_SWITCH

    override val getImplementationFilePaths = KCAL_COLOR

    override val getImplementationFormat = "%d %d %d"
}