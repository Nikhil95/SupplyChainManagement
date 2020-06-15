package supplyChainManagement

import net.corda.core.serialization.CordaSerializable
import java.util.*

@CordaSerializable
data class Location (
    val address     :String,        // Address
    val posCode     :Int,           // Postal Code
    val city        :String,        // City
    val state       :String,        // State
    val country     :String         // Country
)

@CordaSerializable
data class ProductData (
    val id          :String,        // Product ID
    val name        :String,        // Product Name
    val qty         :Double,        // Quantity of Product -> Unit Agnostic
    val units       :String         // Units in which _qty_ is measured
)

val validUnits = arrayOf(
    "lb",       // pounds
    "kg",       // kilograms
    "oz",       // ounces
    "no"        // number -> if you sold '10' boxes, it will have 'no' as the unit
)

fun isValidUnit (u :String) : Boolean {
    for (unit in validUnits) {
        if (u == unit) {
            return true
        }
    }
    return false
}