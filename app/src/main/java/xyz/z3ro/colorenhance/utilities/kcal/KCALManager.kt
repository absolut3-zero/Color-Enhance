package xyz.z3ro.colorenhance.utilities.kcal

object KCALManager {
    var kcalImplementation: KCALInterface

    init {
        kcalImplementation = GenericKCALManager()
        if (!kcalImplementation.isSupported) kcalImplementation = SD845KCALManager()
        if (!kcalImplementation.isSupported) kcalImplementation = DummyKCALManager()
    }

    val isKCALAvailable: Boolean
        get() = kcalImplementation.isSupported

    private val isKCALEnabled: Boolean
        get() = kcalImplementation.enabled

    private val kcalValues: String
        get() {
            val reading = kcalImplementation.colors

            return "${reading[0]} ${reading[1]} ${reading[2]}"
        }

    fun enableKCAl() {
        kcalImplementation.enabled = true
    }

}