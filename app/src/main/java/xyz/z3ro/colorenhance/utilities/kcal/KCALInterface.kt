package xyz.z3ro.colorenhance.utilities.kcal

interface KCALInterface {

    val isSupported: Boolean

    var enabled: Boolean

    var colors: IntArray

    val getImplementationName: String
    val getImplementationSwitchPath: String
    val getImplementationFilePaths: String
    val getImplementationFormat: String
}