package org.bambrikii.tiny.db.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class TinyDbCallableStatement implements CallableStatement {
    @Override
    public void registerOutParameter(int parameterIndex, int sqlType) {

    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, int scale) {

    }

    @Override
    public boolean wasNull() {
        return false;
    }

    @Override
    public String getString(int parameterIndex) {
        return "";
    }

    @Override
    public boolean getBoolean(int parameterIndex) {
        return false;
    }

    @Override
    public byte getByte(int parameterIndex) {
        return 0;
    }

    @Override
    public short getShort(int parameterIndex) {
        return 0;
    }

    @Override
    public int getInt(int parameterIndex) {
        return 0;
    }

    @Override
    public long getLong(int parameterIndex) {
        return 0;
    }

    @Override
    public float getFloat(int parameterIndex) {
        return 0;
    }

    @Override
    public double getDouble(int parameterIndex) {
        return 0;
    }

    @Override
    public BigDecimal getBigDecimal(int parameterIndex, int scale) {
        return null;
    }

    @Override
    public byte[] getBytes(int parameterIndex) {
        return new byte[0];
    }

    @Override
    public Date getDate(int parameterIndex) {
        return null;
    }

    @Override
    public Time getTime(int parameterIndex) {
        return null;
    }

    @Override
    public Timestamp getTimestamp(int parameterIndex) {
        return null;
    }

    @Override
    public Object getObject(int parameterIndex) {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(int parameterIndex) {
        return null;
    }

    @Override
    public Object getObject(int parameterIndex, Map<String, Class<?>> map) {
        return null;
    }

    @Override
    public Ref getRef(int parameterIndex) {
        return null;
    }

    @Override
    public Blob getBlob(int parameterIndex) {
        return null;
    }

    @Override
    public Clob getClob(int parameterIndex) {
        return null;
    }

    @Override
    public Array getArray(int parameterIndex) {
        return null;
    }

    @Override
    public Date getDate(int parameterIndex, Calendar cal) {
        return null;
    }

    @Override
    public Time getTime(int parameterIndex, Calendar cal) {
        return null;
    }

    @Override
    public Timestamp getTimestamp(int parameterIndex, Calendar cal) {
        return null;
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, String typeName) {

    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType) {

    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType, int scale) {

    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType, String typeName) {

    }

    @Override
    public URL getURL(int parameterIndex) {
        return null;
    }

    @Override
    public void setURL(String parameterName, URL val) {

    }

    @Override
    public void setNull(String parameterName, int sqlType) {

    }

    @Override
    public void setBoolean(String parameterName, boolean x) {

    }

    @Override
    public void setByte(String parameterName, byte x) {

    }

    @Override
    public void setShort(String parameterName, short x) {

    }

    @Override
    public void setInt(String parameterName, int x) {

    }

    @Override
    public void setLong(String parameterName, long x) {

    }

    @Override
    public void setFloat(String parameterName, float x) {

    }

    @Override
    public void setDouble(String parameterName, double x) {

    }

    @Override
    public void setBigDecimal(String parameterName, BigDecimal x) {

    }

    @Override
    public void setString(String parameterName, String x) {

    }

    @Override
    public void setBytes(String parameterName, byte[] x) {

    }

    @Override
    public void setDate(String parameterName, Date x) {

    }

    @Override
    public void setTime(String parameterName, Time x) {

    }

    @Override
    public void setTimestamp(String parameterName, Timestamp x) {

    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x, int length) {

    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x, int length) {

    }

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType, int scale) {

    }

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType) {

    }

    @Override
    public void setObject(String parameterName, Object x) {

    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader, int length) {

    }

    @Override
    public void setDate(String parameterName, Date x, Calendar cal) {

    }

    @Override
    public void setTime(String parameterName, Time x, Calendar cal) {

    }

    @Override
    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) {

    }

    @Override
    public void setNull(String parameterName, int sqlType, String typeName) {

    }

    @Override
    public String getString(String parameterName) {
        return "";
    }

    @Override
    public boolean getBoolean(String parameterName) {
        return false;
    }

    @Override
    public byte getByte(String parameterName) {
        return 0;
    }

    @Override
    public short getShort(String parameterName) {
        return 0;
    }

    @Override
    public int getInt(String parameterName) {
        return 0;
    }

    @Override
    public long getLong(String parameterName) {
        return 0;
    }

    @Override
    public float getFloat(String parameterName) {
        return 0;
    }

    @Override
    public double getDouble(String parameterName) {
        return 0;
    }

    @Override
    public byte[] getBytes(String parameterName) {
        return new byte[0];
    }

    @Override
    public Date getDate(String parameterName) {
        return null;
    }

    @Override
    public Time getTime(String parameterName) {
        return null;
    }

    @Override
    public Timestamp getTimestamp(String parameterName) {
        return null;
    }

    @Override
    public Object getObject(String parameterName) {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String parameterName) {
        return null;
    }

    @Override
    public Object getObject(String parameterName, Map<String, Class<?>> map) {
        return null;
    }

    @Override
    public Ref getRef(String parameterName) {
        return null;
    }

    @Override
    public Blob getBlob(String parameterName) {
        return null;
    }

    @Override
    public Clob getClob(String parameterName) {
        return null;
    }

    @Override
    public Array getArray(String parameterName) {
        return null;
    }

    @Override
    public Date getDate(String parameterName, Calendar cal) {
        return null;
    }

    @Override
    public Time getTime(String parameterName, Calendar cal) {
        return null;
    }

    @Override
    public Timestamp getTimestamp(String parameterName, Calendar cal) {
        return null;
    }

    @Override
    public URL getURL(String parameterName) {
        return null;
    }

    @Override
    public RowId getRowId(int parameterIndex) {
        return null;
    }

    @Override
    public RowId getRowId(String parameterName) {
        return null;
    }

    @Override
    public void setRowId(String parameterName, RowId x) {

    }

    @Override
    public void setNString(String parameterName, String value) {

    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value, long length) {

    }

    @Override
    public void setNClob(String parameterName, NClob value) {

    }

    @Override
    public void setClob(String parameterName, Reader reader, long length) {

    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream, long length) {

    }

    @Override
    public void setNClob(String parameterName, Reader reader, long length) {

    }

    @Override
    public NClob getNClob(int parameterIndex) {
        return null;
    }

    @Override
    public NClob getNClob(String parameterName) {
        return null;
    }

    @Override
    public void setSQLXML(String parameterName, SQLXML xmlObject) {

    }

    @Override
    public SQLXML getSQLXML(int parameterIndex) {
        return null;
    }

    @Override
    public SQLXML getSQLXML(String parameterName) {
        return null;
    }

    @Override
    public String getNString(int parameterIndex) {
        return "";
    }

    @Override
    public String getNString(String parameterName) {
        return "";
    }

    @Override
    public Reader getNCharacterStream(int parameterIndex) {
        return null;
    }

    @Override
    public Reader getNCharacterStream(String parameterName) {
        return null;
    }

    @Override
    public Reader getCharacterStream(int parameterIndex) {
        return null;
    }

    @Override
    public Reader getCharacterStream(String parameterName) {
        return null;
    }

    @Override
    public void setBlob(String parameterName, Blob x) {

    }

    @Override
    public void setClob(String parameterName, Clob x) {

    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x, long length) {

    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x, long length) {

    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader, long length) {

    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x) {

    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x) {

    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader) {

    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value) {

    }

    @Override
    public void setClob(String parameterName, Reader reader) {

    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream) {

    }

    @Override
    public void setNClob(String parameterName, Reader reader) {

    }

    @Override
    public <T> T getObject(int parameterIndex, Class<T> type) {
        return null;
    }

    @Override
    public <T> T getObject(String parameterName, Class<T> type) {
        return null;
    }

    @Override
    public ResultSet executeQuery() {
        return null;
    }

    @Override
    public int executeUpdate() {
        return 0;
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) {

    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) {

    }

    @Override
    public void setByte(int parameterIndex, byte x) {

    }

    @Override
    public void setShort(int parameterIndex, short x) {

    }

    @Override
    public void setInt(int parameterIndex, int x) {

    }

    @Override
    public void setLong(int parameterIndex, long x) {

    }

    @Override
    public void setFloat(int parameterIndex, float x) {

    }

    @Override
    public void setDouble(int parameterIndex, double x) {

    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) {

    }

    @Override
    public void setString(int parameterIndex, String x) {

    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) {

    }

    @Override
    public void setDate(int parameterIndex, Date x) {

    }

    @Override
    public void setTime(int parameterIndex, Time x) {

    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) {

    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) {

    }

    @Override
    public void clearParameters() {

    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) {

    }

    @Override
    public void setObject(int parameterIndex, Object x) {

    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public void addBatch() {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) {

    }

    @Override
    public void setRef(int parameterIndex, Ref x) {

    }

    @Override
    public void setBlob(int parameterIndex, Blob x) {

    }

    @Override
    public void setClob(int parameterIndex, Clob x) {

    }

    @Override
    public void setArray(int parameterIndex, Array x) {

    }

    @Override
    public ResultSetMetaData getMetaData() {
        return null;
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) {

    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) {

    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) {

    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) {

    }

    @Override
    public void setURL(int parameterIndex, URL x) {

    }

    @Override
    public ParameterMetaData getParameterMetaData() {
        return null;
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) {

    }

    @Override
    public void setNString(int parameterIndex, String value) {

    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) {

    }

    @Override
    public void setNClob(int parameterIndex, NClob value) {

    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) {

    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) {

    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) {

    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) {

    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) {

    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) {

    }

    @Override
    public void setClob(int parameterIndex, Reader reader) {

    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) {

    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) {

    }

    @Override
    public ResultSet executeQuery(String sql) {
        return null;
    }

    @Override
    public int executeUpdate(String sql) {
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public int getMaxFieldSize() {
        return 0;
    }

    @Override
    public void setMaxFieldSize(int max) {

    }

    @Override
    public int getMaxRows() {
        return 0;
    }

    @Override
    public void setMaxRows(int max) {

    }

    @Override
    public void setEscapeProcessing(boolean enable) {

    }

    @Override
    public int getQueryTimeout() {
        return 0;
    }

    @Override
    public void setQueryTimeout(int seconds) {

    }

    @Override
    public void cancel() {

    }

    @Override
    public SQLWarning getWarnings() {
        return null;
    }

    @Override
    public void clearWarnings() {

    }

    @Override
    public void setCursorName(String name) {

    }

    @Override
    public boolean execute(String sql) {
        return false;
    }

    @Override
    public ResultSet getResultSet() {
        return null;
    }

    @Override
    public int getUpdateCount() {
        return 0;
    }

    @Override
    public boolean getMoreResults() {
        return false;
    }

    @Override
    public void setFetchDirection(int direction) {

    }

    @Override
    public int getFetchDirection() {
        return 0;
    }

    @Override
    public void setFetchSize(int rows) {

    }

    @Override
    public int getFetchSize() {
        return 0;
    }

    @Override
    public int getResultSetConcurrency() {
        return 0;
    }

    @Override
    public int getResultSetType() {
        return 0;
    }

    @Override
    public void addBatch(String sql) {

    }

    @Override
    public void clearBatch() {

    }

    @Override
    public int[] executeBatch() {
        return new int[0];
    }

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public boolean getMoreResults(int current) {
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() {
        return null;
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) {
        return 0;
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) {
        return false;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) {
        return false;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) {
        return false;
    }

    @Override
    public int getResultSetHoldability() {
        return 0;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void setPoolable(boolean poolable) {

    }

    @Override
    public boolean isPoolable() {
        return false;
    }

    @Override
    public void closeOnCompletion() {

    }

    @Override
    public boolean isCloseOnCompletion() {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }
}
