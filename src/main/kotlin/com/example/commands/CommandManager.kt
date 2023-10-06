package com.example.commands

import com.example.ExampleMod
import com.example.commands.SimpleCommand.ProcessCommandRunnable
import com.example.utils.ChatUtils
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraftforge.client.ClientCommandHandler

class CommandManager {

    init {
        registerCommand("testcommand") {
            ChatUtils.messageToChat("Test successful.")
        }
        registerCommand("openconfig") {
            ExampleMod.configManager.openConfigGui()
        }
    }

    private fun registerCommand(name: String, function: (Array<String>) -> Unit) {
        ClientCommandHandler.instance.registerCommand(SimpleCommand(name, createCommand(function)))
    }

    private fun registerCommand0(
        name: String,
        function: (Array<String>) -> Unit,
        autoComplete: ((Array<String>) -> List<String>) = { listOf() }
    ) {
        val command = SimpleCommand(
            name,
            createCommand(function),
            object : SimpleCommand.TabCompleteRunnable {
                override fun tabComplete(sender: ICommandSender?, args: Array<String>?, pos: BlockPos?): List<String> {
                    return autoComplete(args ?: emptyArray())
                }
            }
        )
        ClientCommandHandler.instance.registerCommand(command)
    }

    private fun createCommand(function: (Array<String>) -> Unit) = object : ProcessCommandRunnable() {
        override fun processCommand(sender: ICommandSender?, args: Array<String>?) {
            if (args != null) function(args.asList().toTypedArray())
        }
    }
}