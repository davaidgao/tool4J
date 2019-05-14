package com.gdwii.tool4j.chain;

import java.util.Iterator;


/**
 * <p>A {@link Catalog} is a collection of named {@link Command}s (or
 * {@link Chain}s) that can be used to retrieve the set of commands that
 * should be performed based on a symbolic identifier.  Use of catalogs
 * is optional, but convenient when there are multiple possible chains
 * that can be selected and executed based on environmental conditions.</p>
 */
public interface Catalog {
    /**
     * <p>Add a new name and associated {@link Command} or {@link Chain}
     * to the set of named commands known to this {@link Catalog},
     * replacing any previous command for that name.
     *
     * @param name Name of the new command
     * @param command {@link Command} or {@link Chain} to be returned
     *  for later lookups on this name
     */
    void addCommand(String name, Command command);

    /**
     * <p>Return the {@link Command} or {@link Chain} associated with the
     * specified name, if any; otherwise, return <code>null</code>.</p>
     *
     * @param name Name for which a {@link Command} or {@link Chain}
     *  should be retrieved
     * @return The Command associated with the specified name.
     */
    Command getCommand(String name);

    /**
     * <p>Return an <code>Iterator</code> over the set of named commands
     * known to this {@link Catalog}.  If there are no known commands,
     * an empty Iterator is returned.</p>
     * @return An iterator of the names in this Catalog.
     */
    Iterator<String> getNames();
}