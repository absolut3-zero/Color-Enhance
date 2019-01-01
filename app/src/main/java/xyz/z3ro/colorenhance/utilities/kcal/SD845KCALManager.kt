package xyz.z3ro.colorenhance.utilities.kcal

import xyz.z3ro.colorenhance.utilities.Root
import java.io.File

class SD845KCALManager : KCALInterface {
    // File paths
    private val KCAL_RED = "/sys/module/msm_drm/parameters/kcal_red"
    private val KCAL_GREEN = "/sys/module/msm_drm/parameters/kcal_green"
    private val KCAL_BLUE = "/sys/module/msm_drm/parameters/kcal_blue"

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
            Root.writeToMultipleFiles(
                listOf(value[0].toString(), value[1].toString(), value[2].toString()),
                listOf(KCAL_RED, KCAL_GREEN, KCAL_BLUE)
            )
        }

    override val getImplementationName = "KCAL for v4.4 kernels"

    override val getImplementationSwitchPath = "Not Available"

    override val getImplementationFilePaths = "$KCAL_RED\n$KCAL_GREEN\n$KCAL_BLUE"

    override val getImplementationFormat = "%d"
}