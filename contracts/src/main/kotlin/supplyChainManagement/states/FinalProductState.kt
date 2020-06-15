package supplyChainManagement.states

import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.ContractState
import net.corda.core.identity.Party
import supplyChainManagement.Location
import supplyChainManagement.ProductData
import supplyChainManagement.TimeDate
import supplyChainManagement.contracts.FinalProductContract

@BelongsToContract(FinalProductContract::class)
data class FinalProductState (
    val seller          : Party,        // Who sold the finished product ?
    val consumer        : Party,        // Who bought the finished product ?
    val materialData    : ProductData,  // What product was sold ?
    val ext_location    : Location,     // Where was the product sold/ location of outlet ?
    val extTimeDate     : TimeDate      // When was the product sold ?
) : ContractState {
    override val participants: List<Party> get() = listOf(seller, consumer)
}
