package supplyChainManagement.states

import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.ContractState
import net.corda.core.identity.Party
import supplyChainManagement.Location
import supplyChainManagement.ProductData
import supplyChainManagement.TimeDate
import supplyChainManagement.contracts.IntermediateProductContract

@BelongsToContract(IntermediateProductContract::class)
data class IntermediateProductState (
    val inputsFrom      : Party,        // Who supplied the raw materials to the manufacturer ?
    val manufacturer    : Party,        // Who manufactured said product ?
    val materialData    : ProductData,  // What product was manufactured ?
    val ext_location    : Location,     // Where was the product manufactured ?
    val extTimeDate     : TimeDate      // When was the product manufactured ?
) : ContractState {
    override val participants: List<Party> get() = listOf(inputsFrom, manufacturer)
}
