package javadbf;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.charset.Charset;

public class DBF7Field {

	private String name; /* 0-31 */
	private DBFDataType type; /* 32 */
	private int length; /* 33 */
	private byte decimalCount; /* 34 */
	private short reserv1; /* 35-36 */
	private byte mdxFlag; /* 37 */
	private short reserv2; /* 38-39 */
	private int nextAutoIncrementValue; /* 40-43 */
	private int reserv3; /* 44-47 */

	/**
	 * Default constructor
	 */
	public DBF7Field() {
		super();
	}

	// For cloning
	DBF7Field(DBF7Field origin) {
		super();

		this.name = origin.name;
		this.type = origin.type;
		this.length = origin.length;
		this.decimalCount = origin.decimalCount;
		this.reserv1 = origin.reserv1;
		this.mdxFlag = origin.mdxFlag;
		this.reserv2 = origin.reserv2;
		this.nextAutoIncrementValue = origin.nextAutoIncrementValue;
		this.reserv3 = origin.reserv3;

	}

	/**
	 * Returns field length.
	 *
	 * @return field length as int.
	 */
	public int getLength() {
		return this.length;
	}

	/**
	 * Returns the decimal part. This is applicable only if the field type if of
	 * numeric in nature.
	 * 
	 * If the field is specified to hold integral values the value returned by this
	 * method will be zero.
	 * 
	 * @return decimal field size as int.
	 */
	public int getDecimalCount() {
		return this.decimalCount;
	}

	public DBF7Field(String name, DBFDataType type) {
		super();
		setName(name);
		setType(type);
	}

	public void setName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Field name cannot be null");
		}

		if (name.length() == 0 || name.length() > 32) {
			throw new IllegalArgumentException("DBF7 Field name should be of length 1-32");
		}

		this.name = name;
	}

	public void setType(DBFDataType type) {
		if (!type.isWriteSupported()) {
			throw new IllegalArgumentException("No support for writting " + type);
		}
		this.type = type;
		if (type.getDefaultSize() > 0) {
			this.length = type.getDefaultSize();
		}

	}

	/**
	 * Sets the decimal place size of the field. Before calling this method the size
	 * of the field should be set by calling setFieldLength().
	 * 
	 * @param size of the decimal field.
	 * 
	 */
	public void setDecimalCount(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Decimal length should be a positive number");
		}
		if (size > this.length) {
			throw new IllegalArgumentException("Decimal length should be less than field length");
		}
		if (this.type != DBFDataType.NUMERIC && this.type != DBFDataType.FLOATING_POINT) {
			throw new UnsupportedOperationException("Cannot set decimal count on this field:" + this.type);
		}
		this.decimalCount = (byte) size;
	}

	protected void write(DataOutput out, Charset charset) throws IOException {
		// Field Name
		byte[] fieldBytes = this.name.getBytes(charset);
		if (fieldBytes.length > 32) {
			throw new IOException("NON-ASCII field name:" + name + " exceds allowed length");
		}
		out.write(DBFUtils.textPadding(this.name, charset, 32, DBFAlignment.LEFT, (byte) 0)); /* 0-31 */
		// data type
		out.writeByte(this.type.getCode()); /* 32 */
		out.writeByte(this.length); /* 33 */
		out.writeByte(this.decimalCount); /* 34 */
		out.writeShort((short) 0x00); /* 35-36 */
		out.writeByte((byte) 0x00); /* 37 */
		out.writeShort((short) 0x00); /* 38-39 */
		out.writeInt(this.nextAutoIncrementValue); /* 40-43 */
		out.write(new byte[3]); /* 44-46 */
		out.write((byte) 0x00); /* 47 */
	}

	/**
	 * Set Length of the field. This method should be called before calling
	 * setDecimalCount().
	 * 
	 * @param length of the field as int.
	 */
	public void setLength(int length) {
		if (length > this.type.getMaxSize() || length < this.type.getMinSize()) {
			throw new UnsupportedOperationException("Length for " + this.type + " must be between "
					+ this.type.getMinSize() + " and " + this.type.getMaxSize());
		}
		this.length = length;
	}

	/**
	 * Gets the type for this field
	 * 
	 * @return The type for this field
	 */

	public DBFDataType getType() {
		return this.type;
	}

	/**
	 * Returns the name of the field.
	 *
	 * @return Name of the field as String.
	 */
	public String getName() {
		return this.name;
	}
}
