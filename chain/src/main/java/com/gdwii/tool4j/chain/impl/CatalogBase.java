package com.gdwii.tool4j.chain.impl;


import com.gdwii.tool4j.chain.Catalog;
import com.gdwii.tool4j.chain.Command;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Simple in-memory implementation of {@link Catalog}.  This class can
 * also be used as the basis for more advanced implementations.</p>
 *
 * <p>This implementation is thread-safe.</p>
 */
public class CatalogBase implements Catalog {
    // ----------------------------------------------------- Instance Variables

    /**
     * <p>The map of named {@link Command}s, keyed by name.
     */
    protected final Map<String, Command> commands;


    // --------------------------------------------------------- Constructors

    /**
     * Create an empty catalog.
     */
    public CatalogBase() {
        this.commands = new ConcurrentHashMap<>();
    }

    /**
     * <p>Create a catalog whose commands are those specified in the given <code>Map</code>.
     * All Map keys should be <code>String</code> and all values should be <code>Command</code>.</p>
     *
     * @param commands Map of Commands.
     *
     * @since Chain 1.1
     */
    public CatalogBase( Map<String, Command> commands ) {
        this.commands = new ConcurrentHashMap<>(commands);
    }

    // --------------------------------------------------------- Public Methods


    /**
     * <p>Add a new name and associated {@link Command}
     * to the set of named commands known to this {@link Catalog},
     * replacing any previous command for that name.
     *
     * @param name Name of the new command
     * @param command {@link Command} to be returned
     *  for later lookups on this name
     */
    public void addCommand(String name, Command command) {
        commands.put(name, command);
    }

    /**
     * <p>Return the {@link Command} associated with the
     * specified name, if any; otherwise, return <code>null</code>.</p>
     *
     * @param name Name for which a {@link Command}
     *  should be retrieved
     * @return The Command associated with the specified name.
     */
    public Command getCommand(String name) {
        return commands.get(name);
    }


    /**
     * <p>Return an <code>Iterator</code> over the set of named commands
     * known to this {@link Catalog}.  If there are no known commands,
     * an empty Iterator is returned.</p>
     * @return An iterator of the names in this Catalog.
     */
    public Iterator<String> getNames() {
        return commands.keySet().iterator();
    }

    /**
     * Converts this Catalog to a String.  Useful for debugging purposes.
     * @return a representation of this catalog as a String
     */
    public String toString() {
        Iterator names = getNames();
        StringBuilder str =
                new StringBuilder("[" + this.getClass().getName() + ": ");

        while (names.hasNext()) {
            str.append(names.next());
            if (names.hasNext()) {
                str.append(", ");
            }
        }
        str.append("]");

        return str.toString();
    }
}