package xyz.z3ro.colorenhance.utilities.kcal

import xyz.z3ro.colorenhance.utilities.Root
import java.io.File

class GenericKCALManager : KCALInterface {

    // File paths
    private val KCAL_SWITCH = "/sys/devices/platform/kcal_ctrl.0/kcal_enable"
    private val KCAL_COLOR = "/sys/devices/platform/kcal_ctrl.0/kcal"

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

    override val getImplementationName = "Generic KCAL"

    override val getImplementationSwitchPath = KCAL_SWITCH

    override val getImplementationFilePaths = KCAL_COLOR

    override val getImplementationFormat = "%d %d %d"
}