package com.gdwii.tool4j.chain.impl;


import com.gdwii.tool4j.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * <p>Convenience base class for {@link Context} implementations.</p>
 *
 * <p>In addition to the minimal functionality required by the {@link Context}
 * interface, this class implements the recommended support for
 * <em>Attribute-Property Transparency</em>. This is implemented by
 * analyzing the available JavaBeans properties of this class (or its
 * subclass), exposes them as key-value pairs in the <code>Map</code>,
 * with the key being the name of the property itself.</p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - Because <code>empty</code> is a
 * read-only property defined by the <code>Map</code> interface, it may not
 * be utilized as an attribute key or property name.</p>
 */
public class ContextBase implements Context {
    private static final Logger logger = LoggerFactory.getLogger(ContextBase.class);

// ------------------------------------------------------------ Constructors
    /**
     * Default, no argument constructor.
     */
    public ContextBase() {}

    /**
     * <p>Initialize the contents of this {@link Context} by copying the
     * values from the specified <code>Map</code>.  Any keys in <code>map</code>
     * that correspond to local properties will cause the setter method for
     * that property to be called.</p>
     *
     * @param map Map whose key-value pairs are added
     *
     * @exception IllegalArgumentException if an exception is thrown
     *  writing a local property value
     * @exception UnsupportedOperationException if a local property does not
     *  have a write method.
     */
    public ContextBase(Map<String, Object> map) {
        putAll(map);
    }


    // ------------------------------------------------------ Instance Variables
    /**
     * internal container to store info except JavaBeans properties
     */
    private Map<String, Object> internalContainer = EMPTY_MAP;

    /**
     * <p>The <code>PropertyDescriptor</code>s for all JavaBeans properties
     * of this {@link Context} implementation class, keyed by property name.
     * This collection is allocated only if there are any JavaBeans
     * properties.</p>
     */
    private Map<String, PropertyDescriptor> propertyDescriptors = createPropertyDescriptors();

    /**
     * <p>Zero-length array of parameter values for calling property getters.
     * </p>
     */
    private static final Object[] zeroParams = new Object[0];

    /**
     * <p>Zero-length array of parameter values for calling property getters.
     * </p>
     */
    private static final Map<String, Object> EMPTY_MAP = Collections.emptyMap();

    // ------------------------------------------------------------- Map Methods
    /**
     * <p>Override the default <code>Map</code> behavior to clear all keys and
     * values except those corresponding to JavaBeans properties.</p>
     */
    public void clear() {
        internalContainer.clear();
    }


    /**
     * <p>Override the default <code>Map</code> behavior to return
     * <code>true</code> if the specified value is present in either the
     * underlying <code>Map</code> or one of the local property values.</p>
     *
     * @param value the value look for in the context.
     * @return <code>true</code> if found in this context otherwise
     *  <code>false</code>.
     * @exception IllegalArgumentException if a property getter
     *  throws an exception
     */
    public boolean containsValue(Object value) {
        // Case 1 -- value found in the underlying Map
        if (internalContainer.containsValue(value)) {
            return true;
        }

        // Case 2 -- check the values of our readable properties
        return propertyDescriptors.entrySet().stream()
                .anyMatch(entry -> {
                    PropertyDescriptor propertyDescriptor = entry.getValue();
                    if (propertyDescriptor.getReadMethod() != null) {
                        Object prop = readProperty(propertyDescriptor);
                        return Objects.equals(value, prop);
                    }
                    return false;
                });
    }


    /**
     * <p>Override the default <code>Map</code> behavior to return a
     * <code>Set</code> that meets the specified default behavior except
     * for attempts to remove the key for a property of the {@link Context}
     * implementation class, which will throw
     * <code>UnsupportedOperationException</code>.</p>
     *
     * @return Set of entries in the Context.
     */
    public Set<Map.Entry<String, Object>> entrySet() {
        Set<Map.Entry<String, Object>> entrySet = new HashSet<>();
        for(String key : keySet()){
            entrySet.add(new MapEntryImpl(key, get(key)));
        }
        return entrySet;
    }

    /**
     * <p>Override the default <code>Map</code> behavior to return the value
     * of a local property if the specified key matches a local property name.
     * </p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - If the specified
     * <code>key</code> identifies a write-only property, <code>null</code>
     * will arbitrarily be returned, in order to avoid difficulties implementing
     * the contracts of the <code>Map</code> interface.</p>
     *
     * @param key Key of the value to be returned
     * @return The value for the specified key.
     *
     * @exception IllegalArgumentException if an exception is thrown
     *  reading this local property value
     * @exception UnsupportedOperationException if this local property does not
     *  have a read method.
     */
    public Object get(Object key) {
        if(key == null){
            return null;
        }

        // Case 1 -- this is a local property
        PropertyDescriptor descriptor = propertyDescriptors.get(key);
        if (descriptor != null) {
            if (descriptor.getReadMethod() != null) {
                return readProperty(descriptor);
            } else {
                return null;
            }
        }

        // Case 2 -- retrieve value from our underlying Map
        return internalContainer.get(key);
    }


    @Override
    public int size() {
        return propertyDescriptors.size() + internalContainer.size();
    }

    /**
     * <p>Override the default <code>Map</code> behavior to return
     * <code>true</code> if the underlying <code>Map</code> only contains
     * key-value pairs for local properties (if any).</p>
     *
     * @return <code>true</code> if this Context is empty, otherwise
     *  <code>false</code>.
     */
    public boolean isEmpty() {
        return propertyDescriptors.isEmpty() && internalContainer.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return propertyDescriptors.containsKey(key) || internalContainer.containsKey(key);
    }


    /**
     * <p>Override the default <code>Map</code> behavior to return a
     * <code>Set</code> that meets the specified default behavior except
     * for attempts to remove the key for a property of the {@link Context}
     * implementation class, which will throw
     * <code>UnsupportedOperationException</code>.</p>
     *
     * @return The set of keys for objects in this Context.
     */
    public Set<String> keySet() {
        Set<String> keySet = new HashSet<>();
        keySet.addAll(propertyDescriptors.keySet());
        keySet.addAll(internalContainer.keySet());
        return keySet;
    }


    /**
     * <p>Override the default <code>Map</code> behavior to set the value
     * of a local property if the specified key matches a local property name.
     * </p>
     *
     * @param key Key of the value to be stored or replaced
     * @param value New value to be stored
     * @return The value added to the Context.
     *
     * @exception IllegalArgumentException if an exception is thrown
     *  reading or wrting this local property value
     * @exception UnsupportedOperationException if this local property does not
     *  have both a read method and a write method
     */
    public Object put(String key, Object value) {
        // Case 1 -- no local properties
        if (key == null) {
            throw new IllegalArgumentException("key is not null");
        }

        // Case 2 -- this is a local property
        PropertyDescriptor descriptor = propertyDescriptors.get(key);
        if (descriptor != null) {
            Object previous = null;
            if (descriptor.getReadMethod() != null) {
                previous = readProperty(descriptor);
            }
            writeProperty(descriptor, value);
            return previous;
        }

        // Case 3 -- store or replace value in our underlying map
        Map<String, Object> internalContainer = ensureInternalContainer();
        return internalContainer.put(key, value);
    }

    /**
     * <p>Override the default <code>Map</code> behavior to call the
     * <code>put()</code> method individually for each key-value pair
     * in the specified <code>Map</code>.</p>
     *
     * @param map <code>Map</code> containing key-value pairs to store
     *  (or replace)
     *
     * @exception IllegalArgumentException if an exception is thrown
     *  reading or wrting a local property value
     * @exception UnsupportedOperationException if a local property does not
     *  have both a read method and a write method
     */
    public void putAll(Map<? extends String, ?> map) {
        map.forEach((key, value) -> put(key, value));
    }


    /**
     * <p>Override the default <code>Map</code> behavior to throw
     * <code>UnsupportedOperationException</code> on any attempt to
     * remove a key that is the name of a local property.</p>
     *
     * @param key Key to be removed
     * @return The value removed from the Context.
     *
     * @exception UnsupportedOperationException if the specified
     *  <code>key</code> matches the name of a local property
     */
    public Object remove(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("key is not null");
        }

        // Case 1 -- this is a local property
        if (propertyDescriptors.containsKey(key)) {
                throw new UnsupportedOperationException
                        ("Local property '" + key + "' cannot be removed");
        }

        // Case 2 -- remove from underlying Map
        return internalContainer.remove(key);
    }


    /**
     * <p>Override the default <code>Map</code> behavior to return a
     * <code>Collection</code> that meets the specified default behavior except
     * for attempts to remove the key for a property of the {@link Context}
     * implementation class, which will throw
     * <code>UnsupportedOperationException</code>.</p>
     *
     * @return The collection of values in this Context.
     */
    public Collection<Object> values() {
        throw new UnsupportedOperationException("values unsupported");
    }


    // --------------------------------------------------------- Private Methods

    private Map<String, Object> ensureInternalContainer() {
        Map<String, Object> internalContainer = this.internalContainer;
        if(internalContainer == EMPTY_MAP){
            internalContainer = new HashMap<>();
            this.internalContainer = internalContainer;
        }
        return internalContainer;
    }

    private Map<String, PropertyDescriptor> createPropertyDescriptors() {
        if(ContextBase.class.equals(getClass())){
            return Collections.emptyMap();
        }

        // Retrieve the set of property descriptors for this Context class
        try {
            PropertyDescriptor[] pds = Introspector.getBeanInfo
                    (getClass()).getPropertyDescriptors(); // 由于内部有缓存,性能有一定的保证
            return Arrays.stream(pds)
                    .filter(pd -> {
                        String name = pd.getName();
                        return !("class".equals(name) || "empty".equals(name));
                    }) // Add descriptor (ignoring getClass() and isEmpty())
                    .collect(Collectors.toMap(PropertyDescriptor::getName, Function.identity()));
        } catch (IntrospectionException e) {
            logger.warn("class:{} could not introspect", getClass(), e);
            return Collections.emptyMap();
        }
    }


    /**
     * <p>Get and return the value for the specified property.</p>
     *
     * @param descriptor <code>PropertyDescriptor</code> for the
     *  specified property
     *
     * @exception IllegalArgumentException if an exception is thrown
     *  reading this local property value
     * @exception UnsupportedOperationException if this local property does not
     *  have a read method.
     */
    private Object readProperty(PropertyDescriptor descriptor) {

        try {
            Method method = descriptor.getReadMethod();
            if (method == null) {
                throw new UnsupportedOperationException
                        ("Property '" + descriptor.getName()
                                + "' is not readable");
            }
            return (method.invoke(this, zeroParams));
        } catch (Exception e) {
            throw new UnsupportedOperationException
                    ("Exception reading property '" + descriptor.getName()
                            + "': " + e.getMessage());
        }

    }

    /**
     * <p>Set the value for the specified property.</p>
     *
     * @param descriptor <code>PropertyDescriptor</code> for the
     *  specified property
     * @param value The new value for this property (must be of the
     *  correct type)
     *
     * @exception IllegalArgumentException if an exception is thrown
     *  writing this local property value
     * @exception UnsupportedOperationException if this local property does not
     *  have a write method.
     */
    private void writeProperty(PropertyDescriptor descriptor, Object value) {

        try {
            Method method = descriptor.getWriteMethod();
            if (method == null) {
                throw new UnsupportedOperationException
                        ("Property '" + descriptor.getName()
                                + "' is not writeable");
            }
            method.invoke(this, value);
        } catch (Exception e) {
            throw new UnsupportedOperationException
                    ("Exception writing property '" + descriptor.getName()
                            + "': " + e.getMessage());
        }
    }

    // --------------------------------------------------------- Private Classes
    /**
     * <p>Private implementation of <code>Map.Entry</code> for each item in
     * <code>EntrySetImpl</code>.</p>
     */
    private class MapEntryImpl implements Map.Entry<String, Object> {
        MapEntryImpl(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        private String key;
        private Object value;

        public boolean equals(Object obj) {
            if (obj == null) {
                return (false);
            } else if (!(obj instanceof Map.Entry)) {
                return (false);
            }
            Map.Entry entry = (Map.Entry) obj;
            if (key == null) {
                return (entry.getKey() == null);
            }
            if (key.equals(entry.getKey())) {
                if (value == null) {
                    return (entry.getValue() == null);
                } else {
                    return (value.equals(entry.getValue()));
                }
            } else {
                return (false);
            }
        }

        public String getKey() {
            return this.key;
        }

        public Object getValue() {
            return this.value;
        }

        public int hashCode() {
            return (((key == null) ? 0 : key.hashCode())
                    ^ ((value == null) ? 0 : value.hashCode()));
        }

        public Object setValue(Object value) {
            Object previous = this.value;
            ContextBase.this.put(this.key, value);
            this.value = value;
            return previous;
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }
    }
}
