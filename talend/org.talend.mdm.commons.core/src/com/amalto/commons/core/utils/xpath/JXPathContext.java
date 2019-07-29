/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amalto.commons.core.utils.xpath;

import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;


public abstract class JXPathContext {
    private static JXPathContextFactory contextFactory;
    private static JXPathContext compilationContext;

    /** parent context */
    protected JXPathContext parentContext;
    /** context bean */
    protected Object contextBean;

    /** AbstractFactory */
    protected AbstractFactory factory;

    /** decimal format map */
    protected HashMap decimalFormats;

    private Locale locale;
    private boolean lenientSet = false;
    private boolean lenient = false;

    /**
     * Creates a new JXPathContext with the specified object as the root node.
     * @param contextBean Object
     * @return JXPathContext
     */
    public static JXPathContext newContext(Object contextBean) {
        return getContextFactory().newContext(null, contextBean);
    }

    /**
     * Creates a new JXPathContext with the specified bean as the root node and
     * the specified parent context. Variables defined in a parent context can
     * be referenced in XPaths passed to the child context.
     * @param parentContext parent context
     * @param contextBean Object
     * @return JXPathContext
     */
    public static JXPathContext newContext(
            JXPathContext parentContext,
            Object contextBean) {
        return getContextFactory().newContext(parentContext, contextBean);
    }

    /**
     * Acquires a context factory and caches it.
     * @return JXPathContextFactory
     */
    private static JXPathContextFactory getContextFactory () {
        if (contextFactory == null) {
            contextFactory = JXPathContextFactory.newInstance();
        }
        return contextFactory;
    }

    /**
     * This constructor should remain protected - it is to be overridden by
     * subclasses, but never explicitly invoked by clients.
     * @param parentContext parent context
     * @param contextBean Object
     */
    protected JXPathContext(JXPathContext parentContext, Object contextBean) {
        this.parentContext = parentContext;
        this.contextBean = contextBean;
    }

    /**
     * Returns the parent context of this context or null.
     * @return JXPathContext
     */
    public JXPathContext getParentContext() {
        return parentContext;
    }

    /**
     * Returns the JavaBean associated with this context.
     * @return Object
     */
    public Object getContextBean() {
        return contextBean;
    }


    /**
     * Install an abstract factory that should be used by the
     * <code>createPath()</code> and <code>createPathAndSetValue()</code>
     * methods.
     * @param factory AbstractFactory
     */
    public void setFactory(AbstractFactory factory) {
        this.factory = factory;
    }

    /**
     * Returns the AbstractFactory installed on this context.
     * If none has been installed and this context has a parent context,
     * returns the parent's factory.  Otherwise returns null.
     * @return AbstractFactory
     */
    public AbstractFactory getFactory() {
        if (factory == null && parentContext != null) {
            return parentContext.getFactory();
        }
        return factory;
    }

    /**
     * Set the locale for this context.  The value of the "lang"
     * attribute as well as the the lang() function will be
     * affected by the locale.  By default, JXPath uses
     * <code>Locale.getDefault()</code>
     * @param locale Locale
     */
    public synchronized void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Returns the locale set with setLocale. If none was set and
     * the context has a parent, returns the parent's locale.
     * Otherwise, returns Locale.getDefault().
     * @return Locale
     */
    public synchronized Locale getLocale() {
        if (locale == null) {
            if (parentContext != null) {
                return parentContext.getLocale();
            }
            locale = Locale.getDefault();
        }
        return locale;
    }

    /**
     * Sets {@link DecimalFormatSymbols} for a given name. The DecimalFormatSymbols
     * can be referenced as the third, optional argument in the invocation of
     * <code>format-number (number,format,decimal-format-name)</code> function.
     * By default, JXPath uses the symbols for the current locale.
     *
     * @param name the format name or null for default format.
     * @param symbols DecimalFormatSymbols
     */
    public synchronized void setDecimalFormatSymbols(String name,
            DecimalFormatSymbols symbols) {
        if (decimalFormats == null) {
            decimalFormats = new HashMap();
        }
        decimalFormats.put(name, symbols);
    }

    /**
     * Get the named DecimalFormatSymbols.
     * @param name key
     * @return DecimalFormatSymbols
     * @see #setDecimalFormatSymbols(String, DecimalFormatSymbols)
     */
    public synchronized DecimalFormatSymbols getDecimalFormatSymbols(String name) {
        if (decimalFormats == null) {
            return parentContext == null ? null : parentContext.getDecimalFormatSymbols(name);
        }
        return (DecimalFormatSymbols) decimalFormats.get(name);
    }

    /**
     * If the context is in the lenient mode, then getValue() returns null
     * for inexistent paths.  Otherwise, a path that does not map to
     * an existing property will throw an exception.  Note that if the
     * property exists, but its value is null, the exception is <i>not</i>
     * thrown.
     * <p>
     * By default, lenient = false
     * @param lenient flag
     */
    public synchronized void setLenient(boolean lenient) {
        this.lenient = lenient;
        lenientSet = true;
    }

    /**
     * Learn whether this JXPathContext is lenient.
     * @return boolean
     * @see #setLenient(boolean)
     */
    public synchronized boolean isLenient() {
        if (!lenientSet && parentContext != null) {
            return parentContext.isLenient();
        }
        return lenient;
    }

    /**
     * Compiles the supplied XPath and returns an internal representation
     * of the path that can then be evaluated.  Use CompiledExpressions
     * when you need to evaluate the same expression multiple times
     * and there is a convenient place to cache CompiledExpression
     * between invocations.
     * @param xpath to compile
     * @return CompiledExpression
     */
    public static CompiledExpression compile(String xpath) {
        if (compilationContext == null) {
            compilationContext = JXPathContext.newContext(null);
        }
        return compilationContext.compilePath(xpath);
    }

    /**
     * Overridden by each concrete implementation of JXPathContext
     * to perform compilation. Is called by <code>compile()</code>.
     * @param xpath to compile
     * @return CompiledExpression
     */
    protected abstract CompiledExpression compilePath(String xpath);



}
