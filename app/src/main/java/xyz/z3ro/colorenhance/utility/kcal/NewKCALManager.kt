package xyz.z3ro.colorenhance.utility.kcal

import xyz.z3ro.colorenhance.utility.Root
import java.io.File

class NewKCALManager : KCALInterface {
    // File paths
    private val KCAL_RED = "/sys/module/msm_drm/parameters/kcal_red"
    private val KCAL_GREEN = "/sys/module/msm_drm/parameters/kcal_green"
    private val KCAL_BLUE = "/sys/module/msm_drm/parameters/kcal_blue"

    private val SAT_INTENSITY = "/sys/module/msm_drm/parameters/kcal_sat"
    private val HUE = "/sys/module/msm_drm/parameters/kcal_hue"
    private val SCREEN_VAL = "/sys/module/msm_drm/parameters/kcal_val"
    private val CONTRAST = "/sys/module/msm_drm/parameters/kcal_cont"

    override val isSupported: Boolean
        get() = (File(KCAL_RED).exists() || Root.doesFileExist(KCAL_RED)) && (File(KCAL_GREEN).exists() || Root.doesFileExist(
            KCAL_GREEN
        )) && (File(KCAL_BLUE).exists() || Root.doesFileExist(KCAL_BLUE))

    override var enabled: Boolean
        get() = true
        set(value) {}

    override var colors: IntArray
        get() {
            return intArrayOf(
                Root.readOneLine(KCAL_RED).toInt(),
                Root.readOneLine(KCAL_GREEN).toInt(),
                Root.readOneLine(KCAL_BLUE).toInt()
            )
        }
        set(value) {
            Root.writeToFiles(
                listOf(value[0].toString(), value[1].toString(), value[2].toString()),
                listOf(KCAL_RED, KCAL_GREEN, KCAL_BLUE)
            )
        }

    override var rgbMultiplier: Int
        get() = 0
        set(value) {}

    override var saturationIntensity: Int
        get() = Root.readOneLine(SAT_INTENSITY).toInt()
        set(value) {
            Root.writeToSingleFile("$value", SAT_INTENSITY)
        }

    override var hue: Int
        get() = Root.readOneLine(HUE).toInt()
        set(value) {
            Root.writeToSingleFile("$value", HUE)
        }

    override var screenValue: Int
        get() = Root.readOneLine(SCREEN_VAL).toInt()
        set(value) {
            Root.writeToSingleFile("$value", SCREEN_VAL)
        }

    override var contrast: Int
        get() = Root.readOneLine(CONTRAST).toInt()
        set(value) {
            Root.writeToSingleFile("$value", CONTRAST)
        }

    override val getImplementationName = "KCAL for v4.4 kernels"

    override val getImplementationSwitchPath = "Not Available"

    override val getImplementationFilePaths = "$KCAL_RED\n$KCAL_GREEN\n$KCAL_BLUE"

    override val getImplementationFormat = "%d"
}