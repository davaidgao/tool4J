package com.gdwii.tool4j.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * <p>A {@link CatalogFactory} is a class used to store and retrieve
 * {@link Catalog}s.  The factory allows for a default {@link Catalog}
 * as well as {@link Catalog}s stored with a name key.  Follows the
 * Factory pattern (see GoF).</p>
 *
 * <p>The base <code>CatalogFactory</code> implementation also implements
 * a resolution mechanism which allows lookup of a command based on a single
 * String which encodes both the catalog and command names.</p>
 *
 * @author Sean Schofield
 * @version $Revision: 480477 $ $Date: 2006-11-29 08:34:52 +0000 (Wed, 29 Nov 2006) $
 */

public abstract class CatalogFactory {

    private static final Logger logger = LoggerFactory.getLogger(CatalogFactory.class);

    /**
     * <p>Values passed to the <code>getCommand(String)</code> method should
     * use this as the delimiter between the "catalog" name and the "command"
     * name.</p>
     */
    public static final String DELIMITER = ":";


    // --------------------------------------------------------- Public Methods
    /**
     * <p>Retrieves a Catalog instance by name (if any); otherwise
     * return <code>null</code>.</p>
     *
     * @param name the name of the Catalog to retrieve
     * @return the specified Catalog
     */
    public abstract Catalog getCatalog(String name);


    /**
     * <p>Adds a named instance of Catalog to the factory (for subsequent
     * retrieval later).</p>
     *
     * @param name the name of the Catalog to add
     * @param catalog the Catalog to add
     */
    public abstract void addCatalog(String name, Catalog catalog);


    /**
     * <p>Return an <code>Iterator</code> over the set of named
     * {@link Catalog}s known to this {@link CatalogFactory}.
     * If there are no known catalogs, an empty Iterator is returned.</p>
     * @return An Iterator of the names of the Catalogs known by this factory.
     */
    public abstract Iterator<String> getNames();


    /**
     * <p>Return a <code>Command</code> based on the given commandID.</p>
     *
     * <p>At this time, the structure of commandID is relatively simple:  if the
     * commandID contains a DELIMITER, treat the segment of the commandID
     * up to (but not including) the DELIMITER as the name of a catalog, and the
     * segment following the DELIMITER as a command name within that catalog.
     * If the commandID contains no DELIMITER, treat the commandID as the name
     * of a command in the default catalog.</p>
     *
     * <p>To preserve the possibility of future extensions to this lookup
     * mechanism, the DELIMITER string should be considered reserved, and
     * should not be used in command names.  commandID values which contain
     * more than one DELIMITER will cause an
     * <code>IllegalArgumentException</code> to be thrown.</p>
     *
     * @param commandID the identifier of the command to return
     * @return the command located with commandID, or <code>null</code>
     *  if either the command name or the catalog name cannot be resolved
     * @throws IllegalArgumentException if the commandID contains more than
     *  one DELIMITER
     *
     * @since Chain 1.1
     */
    public Command getCommand(String commandID) {

        String commandName = commandID;
        String catalogName = null;

        if (commandID != null) {
            int splitPos = commandID.indexOf(DELIMITER);
            if (splitPos != -1) {
                catalogName = commandID.substring(0, splitPos);
                commandName = commandID.substring(splitPos + DELIMITER.length());
                if (commandName.contains(DELIMITER)) {
                    throw new IllegalArgumentException("commandID [" +
                            commandID +
                            "] has too many delimiters (reserved for future use)");
                }
            }
        }

        if(isEmpty(catalogName)){
            throw new IllegalArgumentException("commandID [" +
                    commandID +
                    "] catalogName is empty");
        }
        if(isEmpty(commandName)){
            throw new IllegalArgumentException("commandID [" +
                    commandID +
                    "] commandName is empty");
        }

        Catalog catalog = getCatalog(catalogName);
        if (catalog == null) {
            logger.warn("No catalog found for name: " + catalogName + ".");
            return null;
        }
        return catalog.getCommand(commandName);
    }

    private static boolean isEmpty(String commandName) {
        return commandName == null || commandName.trim().length() == 0;
    }
}
