package supplyChainManagement.contracts

import supplyChainManagement.states.FinalProductState
import net.corda.core.contracts.*
import net.corda.core.transactions.LedgerTransaction

class FinalProductContract : Contract {
    companion object {
        const val ID = "supplyChainManagement.contracts.FinalProductContract"
    }

    override fun verify(tx: LedgerTransaction) {
        val command = tx.commands.requireSingleCommand<Commands>()
        val inputs = tx.inputs
        val outputs = tx.outputs
        when (command.value) {
            is Commands.IssueFP -> requireThat {

            }
            is Commands.ExhaustFP -> requireThat {

            }
        }
    }

    interface Commands : CommandData {
        class IssueFP  : TypeOnlyCommandData(), Commands
        class ExhaustFP : TypeOnlyCommandData(), Commands
    }
}