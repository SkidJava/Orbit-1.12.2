package net.minecraft.command;

import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;

public interface ICommandManager
{
    /**
     * Attempt to execute a command. This method should return the number of times that the command was executed. If the
     * command does not exist or if the thePlayer does not have permission, 0 will be returned. A number greater than 1 can
     * be returned if a thePlayer selector is used.
     */
    int executeCommand(ICommandSender sender, String rawCommand);

    List<String> getTabCompletionOptions(ICommandSender sender, String input, BlockPos pos);

    List<ICommand> getPossibleCommands(ICommandSender sender);

    Map<String, ICommand> getCommands();
}
