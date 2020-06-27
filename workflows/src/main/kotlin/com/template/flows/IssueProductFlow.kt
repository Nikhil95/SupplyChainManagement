package supplyChainManagementFlows

import co.paralleluniverse.fibers.Suspendable
import net.corda.core.contracts.requireThat
import net.corda.core.flows.*
import net.corda.core.identity.Party
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import supplyChainManagement.Location
import supplyChainManagement.ProductData
import supplyChainManagement.contracts.ProductContract
import supplyChainManagement.states.ProductState
import java.time.LocalDateTime
import java.util.Collections.singletonList

@InitiatingFlow
@StartableByRPC
class IssueProductFlow (

    private val receiver                    : Party,
    private val id                          : String,
    private val name                        : String,
    private val qty                         : Double,
    private val units                       : String,
    private val s_address                   : String,
    private val s_posCode                   : Int,
    private val s_city                      : String,
    private val s_state                     : String,
    private val s_country                   : String,
    private val r_address                   : String,
    private val r_posCode                   : Int,
    private val r_city                      : String,
    private val r_state                     : String,
    private val r_country                   : String

) : FlowLogic<SignedTransaction>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call() : SignedTransaction {
        val     sentOnTimeDate   = LocalDateTime.now()
        val     sentMaterialData = ProductData(id, name, qty, units)
        val     notary           = serviceHub.networkMapCache.notaryIdentities[0]
        val     sender           = ourIdentity

        val     sentFromLocation = Location(s_address, s_posCode, s_city, s_state, s_country)
        val     sentToLocation   = Location(r_address, r_posCode, r_city, r_state, r_country)
        val     pstate           = ProductState(sender, receiver, sentMaterialData, sentFromLocation, sentToLocation, sentOnTimeDate)

        val     command          = ProductContract.Commands.IssueIP()
        val     tb               = TransactionBuilder(notary)

        tb.addOutputState   (pstate, ProductContract.ID)
        tb.addCommand       (command, sender.owningKey, receiver.owningKey)

        tb.verify(serviceHub)

        val     sesh            = initiateFlow(receiver)
        val     stx             = serviceHub.signInitialTransaction((tb))
        val     ftx             = subFlow(CollectSignaturesFlow(stx, singletonList(sesh)))

        return subFlow(FinalityFlow(ftx, singletonList(sesh)))
    }
}

@InitiatedBy(IssueProductFlow::class)
class Responder(val otherPartySession: FlowSession): FlowLogic<SignedTransaction>() {
    @Suspendable override fun call(): SignedTransaction {
        val flow = object : SignTransactionFlow(otherPartySession) {
            @Suspendable override fun checkTransaction(stx: SignedTransaction) = requireThat {}
        }
        val expectedTxId = subFlow(flow).id

        return subFlow(ReceiveFinalityFlow(otherPartySession, expectedTxId))
    }
}
