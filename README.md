# javadbf
DBF7 write function added version of https://github.com/albfernandez/javadbf

Some of header parameters are skipped and can't write to it. Only DBF7 file writing functionality.


Usage example:

```java
// let us create field definitions first
// we will go for 3 fields

DBF7Field[] fields = new DBF7Field[4];

fields[0] = new DBF7Field();
fields[0].setName("emp_code");
fields[0].setType(DBFDataType.CHARACTER);
fields[0].setLength(10);

fields[1] = new DBF7Field();
fields[1].setName("emp_name_testing_test");
fields[1].setType(DBFDataType.CHARACTER);
fields[1].setLength(20);

fields[2] = new DBF7Field();
fields[2].setName("salary");
fields[2].setType(DBFDataType.NUMERIC);
fields[2].setLength(12);
fields[2].setDecimalCount(2);

fields[3] = new DBF7Field();
fields[3].setName("date_test");
fields[3].setType(DBFDataType.DATE);

DBFWriter writer = new DBFWriter(new FileOutputStream("D:/test1.dbf"));
writer.setFields(fields);

// now populate DBFWriter

Object rowData[] = new Object[4];
rowData[0] = "1000";
rowData[1] = "John";
rowData[2] = new Double(5000.01);
rowData[3] = new Date();

writer.addRecord(rowData);

rowData = new Object[4];
rowData[0] = "1001";
rowData[1] = "Lalit";
rowData[2] = new Double(3400.55);
rowData[3] = new Date();

writer.addRecord(rowData);

rowData = new Object[4];
rowData[0] = "1002";
rowData[1] = "Rohit";
rowData[2] = new Double(7350.45);
rowData[3] = new Date();

writer.addRecord(rowData);

// write to file
writer.close();
```
