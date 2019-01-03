package xyz.z3ro.colorenhance.utility.kcal

object KCALManager {
    var kcalImplementation: KCALInterface

    init {
        kcalImplementation = GenericKCALManager()
        if (!kcalImplementation.isSupported) kcalImplementation = DummyKCALManager()
    }

    val isKCALAvailable: Boolean
        get() = kcalImplementation.isSupported

    var kcalEnabled: Boolean
        get() = kcalImplementation.enabled
        set(value) {
            kcalImplementation.enabled = value
        }

    val kcalColorValues: String
        get() {
            val reading = kcalImplementation.colors

            return "${reading[0]} ${reading[1]} ${reading[2]}"
        }

    fun updateKCALValues(rawValue: String?): Boolean {
        if (rawValue == null) return false
        val colors = rawValue.split(" ".toRegex())
        return updateKCALValues(colors[0].toInt(), colors[1].toInt(), colors[2].toInt())
    }

    fun updateKCALValues(red: Int, green: Int, blue: Int): Boolean {
        val rgb: IntArray = intArrayOf(red, green, blue)
        if (rgb.isNotEmpty()) {
            kcalImplementation.colors = rgb
            return true
        }
        return false
    }

    fun updateKCALValues(rgb: IntArray): Boolean {
        if (rgb.isNotEmpty()) {
            kcalImplementation.colors = rgb
            return true
        }
        return false
    }

    fun updateKCALWithDefaultValues(): Boolean {
        return updateKCALValues(256, 256, 256)
    }
}