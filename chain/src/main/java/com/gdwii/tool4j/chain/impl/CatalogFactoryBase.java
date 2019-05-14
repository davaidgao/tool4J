package com.gdwii.tool4j.chain.impl;


import com.gdwii.tool4j.chain.Catalog;
import com.gdwii.tool4j.chain.CatalogFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>A simple implementation of {@link CatalogFactory}.</p>
 */

public class CatalogFactoryBase extends CatalogFactory {
    // ----------------------------------------------------- Instance Variables

    /**
     * <p>Map of named {@link Catalog}s, keyed by catalog name.</p>
     */
    private Map<String, Catalog> catalogs = new ConcurrentHashMap<>();


    // --------------------------------------------------------- Public Methods
    /**
     * <p>Retrieves a Catalog instance by name (if any); otherwise
     * return <code>null</code>.</p>
     *
     * @param name the name of the Catalog to retrieve
     * @return the specified Catalog
     */
    @Override
    public Catalog getCatalog(String name) {
        return catalogs.get(name);
    }

    /**
     * <p>Adds a named instance of Catalog to the factory (for subsequent
     * retrieval later).</p>
     *
     * @param name the name of the Catalog to add
     * @param catalog the Catalog to add
     */
    @Override
    public void addCatalog(String name, Catalog catalog) {
        catalogs.put(name, catalog);
    }

    /**
     * <p>Return an <code>Iterator</code> over the set of named
     * {@link Catalog}s known to this {@link CatalogFactory}.
     * If there are no known catalogs, an empty Iterator is returned.</p>
     * @return An Iterator of the names of the Catalogs known by this factory.
     */
    @Override
    public Iterator<String> getNames() {
        return catalogs.keySet().iterator();
    }
}