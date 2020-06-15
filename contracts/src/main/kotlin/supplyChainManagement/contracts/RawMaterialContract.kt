package supplyChainManagement.contracts

import supplyChainManagement.states.RawMaterialState
import net.corda.core.contracts.*
import net.corda.core.transactions.LedgerTransaction

class RawMaterialContract : Contract {
    companion object {
        const val ID = "supplyChainManagement.contracts.RawMaterialContract"
    }

    override fun verify(tx: LedgerTransaction) {
        val command = tx.commands.requireSingleCommand<Commands>()
        val inputs = tx.inputs
        val outputs = tx.outputs
        when (command.value) {
            is Commands.IssueRM -> requireThat {
                "Creating raw material involves no inputs" using (inputs.isEmpty())
                "Creating raw material involves one output only" using (outputs.size == 1)
                "You can't extract -ve amount of material" using ((tx.outputStates[0] as RawMaterialState).materialData.qty > 0)
            }
            is Commands.ExhaustRM -> requireThat {
                "Exhausting raw material involves one input only" using (inputs.size == 1)
                "Exhausting raw material involves no outputs" using (outputs.isEmpty())
                "You cannot exhaust something you don't have" using ((tx.inputStates[0] as RawMaterialState).materialData.qty > 0)
            }
        }
    }

    interface Commands : CommandData {
        class IssueRM  : TypeOnlyCommandData(), Commands
        class ExhaustRM : TypeOnlyCommandData(), Commands
    }
}