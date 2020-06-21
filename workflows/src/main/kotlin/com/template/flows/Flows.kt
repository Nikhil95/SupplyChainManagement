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

    private val manufacturer            : Party,
    private val id                      : String,
    private val name                    : String,
    private val qty                     : Double,
    private val units                   : String,
    private val address                 : String,
    private val posCode                 : Int,
    private val city                    : String,
    private val state                   : String,
    private val country                 : String

) : FlowLogic<SignedTransaction>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call() : SignedTransaction {
        val     extTimeDate     = LocalDateTime.now()
        val     materialData    = ProductData(id, name, qty, units)
        val     notary          = serviceHub.networkMapCache.notaryIdentities[0]
        val     inputsFrom      = ourIdentity

        val     extLocation     = Location(address, posCode, city, state, country)
        val     pstate          = ProductState(inputsFrom, manufacturer, materialData, extLocation, extTimeDate)

        val     command         = ProductContract.Commands.IssueIP()
        val     tb              = TransactionBuilder(notary)

        tb.addOutputState   (pstate, ProductContract.ID)
        tb.addCommand       (command, inputsFrom.owningKey, manufacturer.owningKey)

        tb.verify(serviceHub)

        val     sesh            = initiateFlow(manufacturer)
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
