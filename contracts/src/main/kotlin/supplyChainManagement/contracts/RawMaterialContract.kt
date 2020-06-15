package supplyChainManagement.contracts

import supplyChainManagement.states.RawMaterialState
import net.corda.core.contracts.*
import net.corda.core.transactions.LedgerTransaction
import supplyChainManagement.isValidUnit

class RawMaterialContract : Contract {
    companion object {
        const val ID = "supplyChainManagement.contracts.RawMaterialContract"
    }

    override fun verify(tx: LedgerTransaction) {
        val command = tx.commands.requireSingleCommand<Commands>()
        val inputs = tx.inputs
        val outputs = tx.outputs
        val outState = tx.outputStates[0] as RawMaterialState
        when (command.value) {
            is Commands.IssueRM -> requireThat {
                "Creating raw material involves no inputs" using (inputs.isEmpty())
                "Creating raw material involves one output only" using (outputs.size == 1)
                "You can't extract -ve amount of material" using (outState.materialData.qty > 0)
                "Product qty must be in valid units" using isValidUnit(outState.materialData.units)
                "Extractor signature must exist" using (command.signers.containsAll(outState.participants.map{ it.owningKey }))
            }
        }
    }

    interface Commands : CommandData {
        class IssueRM  : TypeOnlyCommandData(), Commands
    }
}