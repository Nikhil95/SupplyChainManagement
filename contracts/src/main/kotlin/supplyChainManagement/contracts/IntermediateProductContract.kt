package supplyChainManagement.contracts

import supplyChainManagement.states.IntermediateProductState
import net.corda.core.contracts.*
import net.corda.core.transactions.LedgerTransaction

class IntermediateProductContract : Contract {
    companion object {
        const val ID = "supplyChainManagement.contracts.IntermediateProductContract"
    }

    override fun verify(tx: LedgerTransaction) {
        val command = tx.commands.requireSingleCommand<Commands>()
        val inputs = tx.inputs
        val outputs = tx.outputs
        when (command.value) {
            is Commands.IssueIP -> requireThat {

            }
            is Commands.TransferIP -> requireThat {

            }
            is Commands.ExhaustIP -> requireThat {

            }
        }
    }

    interface Commands : CommandData {
        class IssueIP  : TypeOnlyCommandData(), Commands
        class TransferIP : TypeOnlyCommandData(), Commands
        class ExhaustIP : TypeOnlyCommandData(), Commands
    }
}