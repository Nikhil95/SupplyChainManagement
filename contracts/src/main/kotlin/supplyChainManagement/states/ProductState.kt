package supplyChainManagement.states

import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.Party
import supplyChainManagement.Location
import supplyChainManagement.ProductData
import supplyChainManagement.contracts.ProductContract
import java.time.LocalDateTime

@BelongsToContract(ProductContract::class)
data class ProductState (

    val sender              : Party,                // Who sent the material ?
    val receiver            : Party,                // Who was the material sent to ?
    val sentMaterialData    : ProductData,          // What product was sent ?
    val sentFromLocation    : Location,             // Where was the product sent from ?
    val sentToLocation      : Location,             // Where was the product sent to ?
    val sentOnTimeDate      : LocalDateTime,        // When was the product extracted / manufactured ?
    override val linearId   : UniqueIdentifier = UniqueIdentifier()

) : LinearState {
    override val participants: List<Party> get() = listOf(sender, receiver)
}
