package me.piggypiglet.docdex.db.server.commands;

import me.piggypiglet.docdex.db.dbo.DatabaseObjects;
import me.piggypiglet.docdex.db.server.Server;
import me.piggypiglet.docdex.documentation.index.algorithm.Algorithm;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

// ------------------------------
// Copyright (c) PiggyPiglet 2020
// https://www.piggypiglet.me
// ------------------------------
public final class ModifyAlgorithmCommand extends ServerCommand {
    public ModifyAlgorithmCommand(@NotNull final String usage, @NotNull final DatabaseObjects adapters) {
        super(usage, 1, adapters);
    }

    @Override
    protected boolean execute(final @NotNull Server server, final @NotNull List<String> args,
                              final @NotNull Consumer<String> messageFunction) {
        final Algorithm algorithm = Algorithm.NAMES.get(args.get(1).toUpperCase());

        if (algorithm == null) {
            messageFunction.accept("Unknown algorithm, use one of the following: " + String.join(", ", Algorithm.NAMES.keySet()));
            return false;
        }

        server.setAlgorithm(algorithm);
        messageFunction.accept("Successfully set the algorithm to " + algorithm + '.');
        return true;
    }
}
