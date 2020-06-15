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
data class TimeDate (               // 24 HOUR FORMAT :
    val hrs         :Int,           // Hours
    val min         :Int,           // Minutes
    val day         :Int,           // Day -> by date -> 1, 2, 3, 4, .....
    val mon         :String,        // Month -> by name -> Jan, Feb, .....
    val year        :Int            // Year -> 4 digit number -> 2020, ...
)

@CordaSerializable
data class ProductData (
    val id          :String,        // Product ID
    val name        :String,        // Product Name
    val qty         :Double,        // Quantity of Product -> Unit Agnostic
    val units       :String         // Units in which _qty_ is measured
)