package top.toptimus.formula.formula;

import java.util.Date;

/**
 * Value of Expression
 * <p>
 * <code>ExprValue</code> supports five basic data types:long,string,double,boolean,date.
 *
 * @author gaoyu
 * @version 1.6.4.22 [20170324 gaoyu] <br>
 * - 修正公式解析器中double值的比较问题 <br>
 */
public class ExprValue implements Comparable<ExprValue> {
    /**
     * value
     */
    protected Object value = null;
    /**
     * data type of the value
     */
    protected DataType dataType = DataType.Void;

    /**
     * constructor
     */
    public ExprValue() {
        // nothing to do
    }

    /**
     * constructor
     *
     * @param longValue long value
     */
    public ExprValue(long longValue) {
        setLong(longValue);
    }

    /**
     * constructor
     *
     * @param stringValue string value
     */
    public ExprValue(String stringValue) {
        setString(stringValue);
    }

    /**
     * constructor
     *
     * @param dateValue data value
     */
    public ExprValue(Date dateValue) {
        setDate(dateValue);
    }

    public ExprValue(boolean boolValue) {
        setBoolean(boolValue);
    }

    public ExprValue(double doubleValue) {
        setDouble(doubleValue);
    }

    /**
     * to get data type of the value
     *
     * @return data type of the value
     */
    public DataType getDataType() {
        return dataType;
    }

    /**
     * get long value from <code>ExprValue</code>
     *
     * @return long value of the expr value
     * @throws FormulaException en excption when data type is mismatched
     */
    public long getLong() {
        switch (dataType) {
            case Long:
                return ((Long) value).longValue();
            case Double:
                return ((Double) value).longValue();
            default:
        }
        throw new FormulaException("Can not get a long value from " + dataType.toString());
    }

    public ExprValue setLong(long longValue) {
        value = Long.valueOf(longValue);
        dataType = DataType.Long;
        return this;
    }

    public int getInt() {
        return (int) getLong();
    }

    /**
     * get double value from <code>ExprValue</code>
     *
     * @return double value
     * @throws FormulaException en excption when data type is mismatched
     */
    public double getDouble() {
        switch (dataType) {
            case Long:
                return ((Long) value).doubleValue();
            case Double:
                return ((Double) value).doubleValue();
            default:
        }
        throw new FormulaException("Can not get a double value from "
                + dataType.toString());
    }

    public ExprValue setDouble(double doubleValue) {
        value = Double.valueOf(doubleValue);
        dataType = DataType.Double;
        return this;
    }

    public float getFloat() {
        return (float) getDouble();
    }

    /**
     * get string value from <code>ExprValue</code>
     *
     * @return double value
     * @throws FormulaException
     */
    public String getString() {
        return value.toString();
    }

    public ExprValue setString(String stringValue) {
        value = stringValue;
        dataType = DataType.String;
        return this;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * get boolean value from <code>ExprValue</code>
     *
     * @return boolean value
     * @throws FormulaException en excption when data type is mismatched
     */
    public boolean getBoolean() {
        switch (dataType) {
            case Long:
                return ((Long) value).longValue() != 0;
            case Boolean:
                return ((Boolean) value).booleanValue();
            default:
        }
        throw new FormulaException("Can not get a boolean value from "
                + dataType.toString());
    }

    public ExprValue setBoolean(boolean boolValue) {
        value = Boolean.valueOf(boolValue);
        dataType = DataType.Boolean;
        return this;
    }

    /**
     * get date value from <code>ExprValue</code>
     *
     * @return date value
     * @throws FormulaException en excption when data type is mismatched
     */
    public Date getDate() {
        if (dataType == DataType.Date) {
            return (Date) value;
        }
        throw new FormulaException("Can not get a date value from "
                + dataType.toString());
    }

    public ExprValue setDate(Date dateValue) {
        value = dateValue;
        dataType = DataType.Date;
        return this;
    }

    /**
     * add
     *
     * @param other anonther <code>ExprValue</code>
     * @return ExprValue
     * @throws FormulaException en excption when data type is mismatched
     */
    public ExprValue add(ExprValue other) { // NOSONAR
        switch (dataType) {
            case Long:// NOSONAR
                if (other.dataType == DataType.Long) {
                    setLong(getLong() + other.getLong());
                    return this;
                } else {
                    if (other.dataType == DataType.Double) {
                        setDouble(getLong() + other.getDouble());
                        return this;
                    }
                }
                break;
            case Double:
                if (other.dataType == DataType.Long || other.dataType == DataType.Double) {
                    setDouble(getDouble() + other.getDouble());
                    return this;
                }
                break;
            case String:
                setString(getString() + other.getString());
                return this;
            default:
        }
        throw new FormulaException("Can not add value between "
                + dataType.toString() + " and " + other.dataType.toString());                 // NOSONAR
    }

    /**
     * sub
     *
     * @param other another <code>ExprValue</code>
     * @return ExprValue
     * @throws FormulaException
     */
    public ExprValue sub(ExprValue other) { // NOSONAR
        switch (dataType) {
            case Long:// NOSONAR
                if (other.dataType == DataType.Long) {
                    setLong(getLong() - other.getLong());
                    return this;
                } else {
                    if (other.dataType == DataType.Double) {
                        setDouble(getLong() - other.getDouble());
                        return this;
                    }
                }
                break;
            case Double:
                if (other.dataType == DataType.Long || other.dataType == DataType.Double) {
                    setDouble(getDouble() - other.getDouble());
                    return this;
                }
                break;
            default:
        }
        throw new FormulaException("Can not sub value between "
                + dataType.toString() + " and " + other.dataType.toString());
    }

    /**
     * mul
     *
     * @param other anther <code>ExprValue</code>
     * @return ExprValue
     * @throws FormulaException
     */
    public ExprValue mul(ExprValue other) {    // NOSONAR
        switch (dataType) {
            case Long: // NOSONAR
                if (other.dataType == DataType.Long) {
                    setLong(getLong() * other.getLong());
                    return this;
                } else {
                    if (other.dataType == DataType.Double) {
                        setDouble(getLong() * other.getDouble());
                        return this;
                    }
                }
                break;
            case Double:
                if (other.dataType == DataType.Long || other.dataType == DataType.Double) {
                    setDouble(getDouble() * other.getDouble());
                    return this;
                }
                break;
            default:
        }
        throw new FormulaException("Can not mul value between "
                + dataType.toString() + " and " + other.dataType.toString());
    }

    /**
     * div
     *
     * @param other anther <code>ExprValue</code>
     * @return ExprValue
     * @throws FormulaException
     */
    public ExprValue div(ExprValue other) { // NOSONAR
        switch (dataType) {
            case Long: // NOSONAR
                if (other.dataType == DataType.Long) {
                    if (other.getLong() == 0)
                        throw new FormulaException("divided by zero"); // NOSONAR
                    setLong(getLong() / other.getLong());
                    return this;
                } else {
                    if (other.dataType == DataType.Double) {
                        if (other.getDouble() == 0) // NOSONAR
                            throw new FormulaException("divided by zero");
                        setDouble(getLong() / other.getDouble());
                        return this;
                    }
                }
                break;
            case Double: // NOSONAR
                if (other.dataType == DataType.Long || other.dataType == DataType.Double) {
                    if (other.getDouble() == 0) // NOSONAR
                        throw new FormulaException("divided by zero");
                    setDouble(getDouble() / other.getDouble());
                    return this;
                }
                break;
            default:
        }
        throw new FormulaException("Can not div value between "
                + dataType.toString() + " and " + other.dataType.toString());
    }

    /**
     * mod
     *
     * @param other anther <code>ExprValue</code>
     * @return ExprValue
     * @throws FormulaException
     */
    public ExprValue mod(ExprValue other) {
        switch (dataType) { // NOSONAR
            case Long:
                if (other.dataType == DataType.Long) {
                    setLong(getLong() % other.getLong());
                    return this;
                }
            default:
        }
        throw new FormulaException("Can not mod value between "
                + dataType.toString() + " and " + other.dataType.toString());
    }

    /**
     * get value
     *
     * @return value of the <code>ExprValue</code>
     */
    public Object getValue() {
        return value;
    }

    @Override
    public int compareTo(ExprValue other) { // NOSONAR
        switch (dataType) {
            case String:
                if (other.dataType == DataType.String) {
                    return value.toString().compareTo(other.value.toString());
                }
                break;
            case Boolean:
                if (other.dataType == DataType.Boolean) {
                    return ((Boolean) value).compareTo((Boolean) other.value);
                }
                break;
            case Date:
                if (other.dataType == DataType.Date) {
                    return ((Date) value).compareTo((Date) other.value);
                }
                break;
            case Long: // NOSONAR
                switch (other.dataType) {
                    case Long:
                        return ((Long) value).compareTo((Long) other.value);
                    case Double: // NOSONAR
                        if (getLong() > other.getDouble()) {
                            return 1;
                        } else {
                            return other.getDouble() > getLong() ? -1 : 0;
                        }
                    default:
                }
                break;
            case Double: // NOSONAR
                switch (other.dataType) {
                    case Double:
                        return ((Double) value).compareTo((Double) other.value);
                    case Long: // NOSONAR
                        boolean compare = getDouble() > other.getLong();
                        if (compare) {
                            return 1;
                        } else {
                            return getDouble() < other.getLong() ? -1 : 0;
                        }
                    default:
                }
            default:
                break;
        }
        throw new FormulaException("Can not compare between "
                + dataType.toString() + " and " + other.dataType.toString());
    }

    /**
     * Data type
     *
     * @author gaoyu
     * @version 1.0.0
     */
    public enum DataType {
        Long, String, Date, Boolean, Double, Void
    } // NOSONAR
}
