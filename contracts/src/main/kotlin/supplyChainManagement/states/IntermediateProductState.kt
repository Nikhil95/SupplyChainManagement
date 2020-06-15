package supplyChainManagement.states

import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.Party
import supplyChainManagement.Location
import supplyChainManagement.ProductData
import supplyChainManagement.contracts.IntermediateProductContract
import java.time.LocalDateTime

@BelongsToContract(IntermediateProductContract::class)
data class IntermediateProductState (
    val inputsFrom          : Party,                // Who supplied the raw materials to the manufacturer ?
    val manufacturer        : Party,                // Who manufactured said product ?
    val materialData        : ProductData,          // What product was manufactured ?
    val ext_location        : Location,             // Where was the product manufactured ?
    val extTimeDate         : LocalDateTime,        // When was the product manufactured ?
    override val linearId   : UniqueIdentifier = UniqueIdentifier()
) : LinearState {
    override val participants: List<Party> get() = listOf(inputsFrom, manufacturer)
}
