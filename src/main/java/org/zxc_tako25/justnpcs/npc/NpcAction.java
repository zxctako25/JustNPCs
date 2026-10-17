package org.zxc_tako25.justnpcs.npc;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public final class NpcAction {
    private String action;
    private final List<String> commands;

    public NpcAction(String action, List<String> commands) {
        this.action = action;
        this.commands = new ArrayList<>(commands == null ? Collections.emptyList() : commands);
    }

    public static NpcAction empty() {
        return new NpcAction(null, Collections.emptyList());
    }

    public boolean isEmpty() {
        boolean noAction = action == null || action.isEmpty();
        return noAction && commands.isEmpty();
    }

    public void addCommand(String command) {
        if (command == null || command.isEmpty())
            return;
        commands.add(command);
    }

    public void clear() {
        this.action = null;
        this.commands.clear();
    }

    public List<String> commandsSnapshot() {
        return Collections.unmodifiableList(commands);
    }
}
