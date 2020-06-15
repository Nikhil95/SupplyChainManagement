package supplyChainManagement.contracts

import supplyChainManagement.states.IntermediateProductState
import net.corda.core.contracts.*
import net.corda.core.transactions.LedgerTransaction
import org.checkerframework.checker.nullness.qual.Raw
import supplyChainManagement.isValidUnit
import supplyChainManagement.states.RawMaterialState

class IntermediateProductContract : Contract {
    companion object {
        const val ID = "supplyChainManagement.contracts.IntermediateProductContract"
    }

    override fun verify(tx: LedgerTransaction) {
        val command = tx.commands.requireSingleCommand<Commands>()
        val inputs = tx.inputs
        val outputs = tx.outputs
        when (command.value) {
            // Intermediate product will always be issued from a RawMaterialState.
            is Commands.IssueIP -> requireThat {
                "Issue IP must have 1 input" using (inputs.size == 1)
                "Issue IP must have its input as a RawMaterialState" using (tx.inputStates[0] is RawMaterialState)
                "Issue IP must have 1 output" using (outputs.size == 1)
                val inState = tx.inputStates[0] as RawMaterialState
                val outState = tx.outputStates[0] as IntermediateProductState
                "inputs to make IP must be from extractor or the raw materials" using (inState.extractor.owningKey == outState.inputsFrom.owningKey)
                "product cannot be manufactured before raw material extraction" using (inState.extTimeDate.isBefore(outState.extTimeDate))
                "only positive amounts of a product can be manufactured" using (outState.materialData.qty > 0)
                "the quantity of the product manufactured must be given in valid units" using (isValidUnit(outState.materialData.units))
                "Both participants of output state must sign the transaction" using (command.signers.toSet() == outState.participants.map { it.owningKey }.toSet())
            }
            is Commands.TransferIP -> requireThat {
                "transfer IP must have 1 input" using (inputs.size == 1)
                "transfer IP must have its input as a IntermediateProductState" using (tx.inputStates[0] is IntermediateProductState)
                "transfer IP must have 1 output" using (outputs.size == 1)
                val inState = tx.inputStates[0] as IntermediateProductState
                val outState = tx.outputStates[0] as IntermediateProductState
                "product cannot be manufactured before inputs are received" using (inState.extTimeDate.isBefore(outState.extTimeDate))
                "only positive amounts of a product can be manufactured" using (outState.materialData.qty > 0)
                "the quantity of the product manufactured must be given in valid units" using (isValidUnit(outState.materialData.units))
                "Inputs for output state must be from manufacturer of input state" using (inState.manufacturer.owningKey == outState.inputsFrom.owningKey)
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