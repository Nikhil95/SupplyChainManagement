package supplyChainManagement.contracts

import supplyChainManagement.states.ProductState
import net.corda.core.contracts.*
import net.corda.core.transactions.LedgerTransaction
import supplyChainManagement.isValidUnit

class ProductContract : Contract {
    companion object {
        const val ID = "supplyChainManagement.contracts.IntermediateProductContract"
    }

    override fun verify(tx: LedgerTransaction) {
        val command = tx.commands.requireSingleCommand<Commands>()
        val inputs = tx.inputs
        val outputs = tx.outputs
        when (command.value) {
            is Commands.IssueIP -> requireThat {
                "Issue IP must have no inputs"                            using (inputs.isEmpty())
                "Issue IP must have 1 output"                             using (outputs.size == 1)
                val outState = tx.outputStates[0] as ProductState
                "only positive amounts of a product can be manufactured"  using (outState.sentMaterialData.qty > 0)
                "the quantity of the product manufactured must be given in valid units"     using (isValidUnit(outState.sentMaterialData.units))
                "Both participants of output state must sign the transaction"               using (command.signers.toSet() == outState.participants.map { it.owningKey }.toSet())
            }
            is Commands.TransferIP -> requireThat {
                val inState = tx.inputStates[0] as ProductState
                val outState = tx.outputStates[0] as ProductState
                "transfer IP must have 1 inputs"                                            using (inputs.size == 1)
                "transfer IP must have its inputs as a ProductState"                        using (tx.inputStates[0] is ProductState)
                "transfer IP must have 1 or more outputs"                                   using (outputs.isNotEmpty())
                "product cannot be manufactured before inputs are received"                 using (outState.extTimeDate.isAfter(inState.extTimeDate))
                "only positive amounts of a product can be manufactured"                    using (outState.sentMaterialData.qty > 0)
                "the quantity of the product manufactured must be given in valid units"     using (isValidUnit(outState.sentMaterialData.units))
                "Inputs for output state must be from manufacturer of input state"          using (inState.receiver.owningKey == outState.sender.owningKey)

                "Everybody related to the txn (inputsFrom and manufacturer of input state and output state must sign the transaction" using
                    (command.signers.toSet() == (inState.participants.map { it.owningKey }.toSet() `union` outState.participants.map { it.owningKey }.toSet()))
            }
        }
    }

    interface Commands : CommandData {
        class IssueIP  : TypeOnlyCommandData(), Commands
        class TransferIP : TypeOnlyCommandData(), Commands
    }
}
