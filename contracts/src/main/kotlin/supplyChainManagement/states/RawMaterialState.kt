package supplyChainManagement.states

import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.ContractState
import net.corda.core.identity.Party
import supplyChainManagement.Location
import supplyChainManagement.ProductData
import supplyChainManagement.TimeDate
import supplyChainManagement.contracts.RawMaterialContract

@BelongsToContract(RawMaterialContract::class)
data class RawMaterialState (
    val extractor       : Party,        // Who extracted the raw material from its source ?
    val materialData    : ProductData,  // What material was extracted ?
    val ext_location    : Location,     // What is the location of the extraction site ?
    val extTimeDate     : TimeDate      // When was this batch of raw material extracted ?
) : ContractState {
    override val participants: List<Party> get() = listOf(extractor)
}
