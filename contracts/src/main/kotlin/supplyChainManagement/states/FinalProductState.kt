package supplyChainManagement.states

import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.Party
import supplyChainManagement.Location
import supplyChainManagement.ProductData
import supplyChainManagement.contracts.FinalProductContract
import java.time.LocalDateTime

@BelongsToContract(FinalProductContract::class)
data class FinalProductState (
    val seller              : Party,            // Who sold the finished product ?
    val consumer            : Party,            // Who bought the finished product ?
    val materialData        : ProductData,      // What product was sold ?
    val ext_location        : Location,         // Where was the product sold/ location of outlet ?
    val extTimeDate         : LocalDateTime,    // When was the product sold ?
    override val linearId   : UniqueIdentifier = UniqueIdentifier()
) : LinearState {
    override val participants: List<Party> get() = listOf(seller, consumer)
}
