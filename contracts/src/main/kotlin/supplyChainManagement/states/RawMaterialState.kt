package supplyChainManagement.states

import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.Party
import supplyChainManagement.Location
import supplyChainManagement.ProductData
import supplyChainManagement.contracts.RawMaterialContract
import java.time.LocalDateTime

@BelongsToContract(RawMaterialContract::class)
data class RawMaterialState (
    val extractor           : Party,            // Who extracted the raw material from its source ?
    val materialData        : ProductData,      // What material was extracted ?
    val ext_location        : Location,         // What is the location of the extraction site ?
    val extTimeDate         : LocalDateTime,    // When was this batch of raw material extracted ?,
    override val linearId   : UniqueIdentifier = UniqueIdentifier()
) : LinearState {
    override val participants: List<Party> get() = listOf(extractor)
}