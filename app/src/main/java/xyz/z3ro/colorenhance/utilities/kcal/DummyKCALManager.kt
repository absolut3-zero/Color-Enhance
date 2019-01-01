package xyz.z3ro.colorenhance.utilities.kcal

class DummyKCALManager : KCALInterface {

    override val isSupported: Boolean = false

    override var enabled: Boolean
        get() = false
        set(value) {}

    override var colors: IntArray
        get() = intArrayOf(0, 0, 0)
        set(value) {}

    override val getImplementationName = "Dummy KCAL Manager"

    override val getImplementationSwitchPath = "Not available"

    override val getImplementationFilePaths = "Not available"

    override val getImplementationFormat = "Not applicable"
}