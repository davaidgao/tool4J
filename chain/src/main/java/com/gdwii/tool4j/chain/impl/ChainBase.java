package com.gdwii.tool4j.chain.impl;


import com.gdwii.tool4j.chain.Chain;
import com.gdwii.tool4j.chain.Command;
import com.gdwii.tool4j.chain.Context;
import com.gdwii.tool4j.chain.Filter;

import java.util.Collection;


/**
 * <p>Convenience base class for {@link Chain} implementations.</p>
 */
public class ChainBase implements Chain {
    // ----------------------------------------------------------- Constructors
    /**
     * <p>Construct a {@link Chain} configured with the specified
     * {@link Command}s.</p>
     *
     * @param commands The {@link Command}s to be configured
     *
     * @exception IllegalArgumentException if <code>commands</code>,
     *  or one of the individual {@link Command} elements,
     *  is <code>null</code>
     */
    public ChainBase(Command[] commands) {
        this.commands = initCommands(commands);
    }

    private Command[] initCommands(Command[] commands) {
        if (commands == null) {
            throw new IllegalArgumentException();
        }
        for (Command command : commands) {
            if (command == null) {
                throw new IllegalArgumentException();
            }
        }
        Command[] results = new Command[commands.length];
        System.arraycopy(commands, 0, results, 0, commands.length);
        return results;
    }


    /**
     * <p>Construct a {@link Chain} configured with the specified
     * {@link Command}s.</p>
     *
     * @param commands The {@link Command}s to be configured
     *
     * @exception IllegalArgumentException if <code>commands</code>,
     *  or one of the individual {@link Command} elements,
     *  is <code>null</code>
     */
    public ChainBase(Collection<? extends Command> commands) {
        if (commands == null) {
            throw new IllegalArgumentException();
        }
        this.commands = initCommands(commands.toArray(new Command[0]));
    }

    // ----------------------------------------------------- Instance Variables

    /**
     * <p>The list of {@link Command}s configured for this {@link Chain}, in
     * the order in which they may delegate processing to the remainder of
     * the {@link Chain}.</p>
     */
    protected final Command[] commands;

    // ---------------------------------------------------------- Chain Methods
    /**
     * See the {@link Chain} JavaDoc.
     *
     * @param context The {@link Context} to be processed by this
     *  {@link Chain}
     *
     * @throws Exception if thrown by one of the {@link Command}s
     *  in this {@link Chain} but not handled by a <code>postprocess()</code>
     *  method of a {@link Filter}
     * @throws IllegalArgumentException if <code>context</code>
     *  is <code>null</code>
     *
     * @return <code>true</code> if the processing of this {@link Context}
     *  has been completed, or <code>false</code> if the processing
     *  of this {@link Context} should be delegated to a subsequent
     *  {@link Command} in an enclosing {@link Chain}
     */
    @Override
    public boolean execute(Context context) throws Exception {
        // Verify our parameters
        if (context == null) {
            throw new IllegalArgumentException();
        }

        // Execute the commands in this list until one returns true
        // or throws an exception
        boolean saveResult = false;
        Exception saveException = null;
        int i;
        int n = commands.length;
        for (i = 0; i < n; i++) {
            try {
                saveResult = commands[i].execute(context);
                if (saveResult) {
                    break;
                }
            } catch (Exception e) {
                saveException = e;
                break;
            }
        }

        // Call postprocess methods on Filters in reverse order
        if (i >= n) { // Fell off the end of the chain
            i--;
        }
        boolean handled = false;
        for (int j = i; j >= 0; j--) {
            if (commands[j] instanceof Filter) {
                try {
                    boolean result =
                            ((Filter) commands[j]).postprocess(context,
                                    saveException);
                    if (result) {
                        handled = true;
                    }
                } catch (Exception e) {
                    // Silently ignore
                }
            }
        }

        // Return the exception or result state from the last execute()
        if ((saveException != null) && !handled) {
            throw saveException;
        } else {
            return (saveResult);
        }
    }
}
