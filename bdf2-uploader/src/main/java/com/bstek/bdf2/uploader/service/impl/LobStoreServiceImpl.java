package com.bstek.bdf2.uploader.service.impl;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;
import org.springframework.jdbc.support.nativejdbc.CommonsDbcpNativeJdbcExtractor;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

import com.bstek.bdf2.core.context.ContextHolder;
import com.bstek.bdf2.uploader.UploaderJdbcDao;
import com.bstek.bdf2.uploader.service.ILobStoreService;

/**
 * <p>
 * 以{@link LobHandler}和{@link LobCreator}为基础，实现了跨数据库的大对象存储操作。<br/>
 * 以下为兼容的数据库及其版本列表:</br> <strong>*注:BLOB在SQLServer系列中的类型可能称为IMAGE</strong><br/>
 * <table style="border-width: 1px;border-color: #FFC555;border-style: solid;padding: 4;margin: 0;">
 * <tr style = "background-color: #FDFDFD;">
 * <td>数据库</td>
 * <td>版本</td>
 * <td>驱动版本</td>
 * </tr>
 * <tr>
 * <td>MySQL</td>
 * <td>5.5</td>
 * <td>mysql-connector-java-5.1.0-bin.jar</td>
 * </tr>
 * <tr style = "background-color: #FEFCCD;">
 * <td>Oracle</td>
 * <td>11g</td>
 * <td>ojdbc14.jar</td>
 * </tr>
 * <tr>
 * <td>DB2</td>
 * <td>9.7</td>
 * <td>db2jcc.jar</td>
 * </tr>
 * <tr style = "background-color: #FEFCCD;">
 * <td>SQLServer</td>
 * <td>2005</td>
 * <td>sqljdbc4.jar</td>
 * </tr>
 * </table>
 * </p>
 * @author jacky.gao@bstek.com
 * @since 2.0
 */
public class LobStoreServiceImpl extends UploaderJdbcDao implements
		ILobStoreService {

	public String storeBytes(final byte[] content) throws SQLException {
		final String sql = "INSERT INTO BDF2_BLOB_STORE(ID_,CONTENT_) VALUES (?,?)";
		final String id = this.generateId();
		int updatedRowCount = super.getJdbcTemplate().execute(
				sql,
				new AbstractLobCreatingPreparedStatementCallback(this
						.getLobHandler()) {
					@Override
					protected void setValues(
							PreparedStatement preparedstatement,
							LobCreator lobcreator) throws SQLException,
							DataAccessException {
						preparedstatement.setString(1, id);
						lobcreator.setBlobAsBytes(preparedstatement, 2, content);
					}
				});
		if (0 == updatedRowCount) {
			throw new SQLException("未能成功存储Byte");
		}
		return id;
	}
	public void storeBytes(final byte[] content,final String id) throws SQLException {
		final String sql = "INSERT INTO BDF2_BLOB_STORE(ID_,CONTENT_) VALUES (?,?)";
		int updatedRowCount = super.getJdbcTemplate().execute(
				sql,
				new AbstractLobCreatingPreparedStatementCallback(this
						.getLobHandler()) {
					@Override
					protected void setValues(
							PreparedStatement preparedstatement,
							LobCreator lobcreator) throws SQLException,
							DataAccessException {
						preparedstatement.setString(1, id);
						lobcreator.setBlobAsBytes(preparedstatement, 2, content);
					}
				});
		if (0 == updatedRowCount) {
			throw new SQLException("未能成功存储Byte");
		}
	}
	
	public void deleteBytes(final String id) throws SQLException {
		deleteFromBlob(id);
	}

	/**
	 * 从删除一条代表二进制数据的记录
	 * 
	 * @param id
	 *            主键
	 * @throws SQLException
	 */
	protected void deleteFromBlob(final String id) throws SQLException {
		final String sql = "DELETE FROM BDF2_BLOB_STORE WHERE ID_=?";
		int updatedRowCount = super.getJdbcTemplate().execute(sql,
				new PreparedStatementCallback<Integer>() {
					public Integer doInPreparedStatement(
							PreparedStatement preparedstatement)
							throws SQLException, DataAccessException {
						preparedstatement.setString(1, id);
						return preparedstatement.executeUpdate();
					}
				});
		if (0 == updatedRowCount) {
			throw new SQLException(String.format("未能成功删除大二进制对象[id=%s],请检查其是否存在", id));
		}
	}

	/**
	 * 删除一条代表文本的记录。
	 * 
	 * @param id
	 *            主键
	 * @throws SQLException
	 */
	protected void deleteFromClob(final String id) throws SQLException {
		final String sql = "DELETE FROM BDF2_CLOB_STORE WHERE ID_=?";
		int updatedRowCount = super.getJdbcTemplate().execute(sql,
				new PreparedStatementCallback<Integer>() {
					public Integer doInPreparedStatement(
							PreparedStatement preparedstatement)
							throws SQLException, DataAccessException {
						preparedstatement.setString(1, id);
						return preparedstatement.executeUpdate();
					}

				});
		if (0 == updatedRowCount) {
			throw new SQLException(String.format("未能成功删除大字符对象[id=%s],请检查其是否存在",
					id));
		}
	}
	public void updateBytes(final String id, final byte[] content)
			throws SQLException {
		final String sql = "UPDATE BDF2_BLOB_STORE SET CONTENT_=? WHERE ID_=?";
		int updatedRowCount = super.getJdbcTemplate().execute(
				sql,
				new AbstractLobCreatingPreparedStatementCallback(this
						.getLobHandler()) {

					@Override
					protected void setValues(
							PreparedStatement preparedstatement,
							LobCreator lobcreator) throws SQLException,
							DataAccessException {
						lobcreator
								.setBlobAsBytes(preparedstatement, 1, content);
						preparedstatement.setString(2, id);

					}
				});
		if (0 == updatedRowCount) {
			throw new SQLException(String.format(
					"未能成功更新Byte[id=%s],请检查此记录是否存在", id));
		}
	}

	public byte[] getBytes(String id) throws SQLException {
		final String sql = "SELECT CONTENT_ FROM BDF2_BLOB_STORE WHERE ID_=?";
		List<byte[]> list = super.getJdbcTemplate().query(sql,
				new Object[] { id }, new RowMapper<byte[]>() {
					public byte[] mapRow(ResultSet resultset, int i)
							throws SQLException {
						byte[] content = LobStoreServiceImpl.this
								.getLobHandler().getBlobAsBytes(resultset, 1);
						return content;
					}

				});
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public String storeBinaryStream(final InputStream inputStream,final int contentLength) throws SQLException {
		final String sql = "INSERT INTO BDF2_BLOB_STORE (ID_,CONTENT_) VALUES (?,?)";
		final String id = this.generateId();
		int updatedRowCount = super.getJdbcTemplate().execute(
				sql,
				new AbstractLobCreatingPreparedStatementCallback(this
						.getLobHandler()) {

					@Override
					protected void setValues(
							PreparedStatement preparedstatement,
							LobCreator lobcreator) throws SQLException,
							DataAccessException {
						preparedstatement.setString(1, id);
						lobcreator.setBlobAsBinaryStream(preparedstatement, 2,
								inputStream, contentLength);
					}
				});
		if (0 == updatedRowCount) {
			throw new SQLException("未能成功存储二进制流");
		}
		return id;
	}
	public void storeBinaryStream(final InputStream inputStream,final int contentLength,final String id) throws SQLException {
		final String sql = "INSERT INTO BDF2_BLOB_STORE (ID_,CONTENT_) VALUES (?,?)";
		int updatedRowCount = super.getJdbcTemplate().execute(
				sql,
				new AbstractLobCreatingPreparedStatementCallback(this
						.getLobHandler()) {
					
					@Override
					protected void setValues(
							PreparedStatement preparedstatement,
							LobCreator lobcreator) throws SQLException,
							DataAccessException {
						preparedstatement.setString(1, id);
						lobcreator.setBlobAsBinaryStream(preparedstatement, 2,
								inputStream, contentLength);
					}
				});
		if (0 == updatedRowCount) {
			throw new SQLException("未能成功存储二进制流");
		}
	}
	
	public void deleteBinaryStream(String id) throws SQLException {
		this.deleteFromBlob(id);
	}

	public void updateBinaryStream(final String id,
			final InputStream inputStream, final int contentLength)
			throws SQLException {
		final String sql = "UPDATE BDF2_BLOB_STORE SET CONTENT_=? WHERE ID_=?";
		int updatedRowCount = super.getJdbcTemplate().execute(
				sql,
				new AbstractLobCreatingPreparedStatementCallback(this
						.getLobHandler()) {

					@Override
					protected void setValues(
							PreparedStatement preparedstatement,
							LobCreator lobcreator) throws SQLException,
							DataAccessException {
						lobcreator.setBlobAsBinaryStream(preparedstatement, 1,
								inputStream, contentLength);
						preparedstatement.setString(2, id);

					}
				});
		if (0 == updatedRowCount) {
			throw new SQLException(String.format(
					"未能成功更新Byte[id=%s],请检查此记录是否存在", id));
		}
	}
	
	public InputStream getBinaryStream(String id) throws SQLException {
		final String sql = "SELECT CONTENT_ FROM BDF2_BLOB_STORE WHERE ID_=?";
		List<InputStream> list = super.getJdbcTemplate().query(sql,
				new Object[] { id }, new RowMapper<InputStream>() {

					public InputStream mapRow(ResultSet resultset, int i)
							throws SQLException {
						InputStream content = LobStoreServiceImpl.this
								.getLobHandler().getBlobAsBinaryStream(
										resultset, 1);
						return content;
					}
				});
		if(list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public String storeString(final String content) throws SQLException {
		final String sql = "INSERT INTO BDF2_CLOB_STORE (ID_,CONTENT_) VALUES(?,?)";
		final String id = this.generateId();
		int updatedRowCount = super.getJdbcTemplate().execute(
				sql,
				new AbstractLobCreatingPreparedStatementCallback(this
						.getLobHandler()) {

					@Override
					protected void setValues(
							PreparedStatement preparedstatement,
							LobCreator lobcreator) throws SQLException,
							DataAccessException {
						preparedstatement.setString(1, id);
						lobcreator.setClobAsString(preparedstatement, 2,
								content);
					}
				});
		if (0 == updatedRowCount) {
			throw new SQLException("未能成功储存大字符串");
		}
		return id;
	}
	public void storeString(final String content,final String id) throws SQLException {
		final String sql = "INSERT INTO BDF2_CLOB_STORE (ID_,CONTENT_) VALUES(?,?)";
		int updatedRowCount = super.getJdbcTemplate().execute(
				sql,
				new AbstractLobCreatingPreparedStatementCallback(this
						.getLobHandler()) {
					
					@Override
					protected void setValues(
							PreparedStatement preparedstatement,
							LobCreator lobcreator) throws SQLException,
							DataAccessException {
						preparedstatement.setString(1, id);
						lobcreator.setClobAsString(preparedstatement, 2,
								content);
					}
				});
		if (0 == updatedRowCount) {
			throw new SQLException("未能成功储存大字符串");
		}
	}

	public void deleteString(String id) throws SQLException {
		this.deleteFromClob(id);
	}

	public void updateString(final String id, final String content)
			throws SQLException {
		final String sql = "UPDATE BDF2_CLOB_STORE SET CONTENT_=? WHERE ID_=?";
		int updatedRowCount = super.getJdbcTemplate().execute(
				sql,
				new AbstractLobCreatingPreparedStatementCallback(this
						.getLobHandler()) {

					@Override
					protected void setValues(
							PreparedStatement preparedstatement,
							LobCreator lobcreator) throws SQLException,
							DataAccessException {
						preparedstatement.setString(2, id);
						lobcreator.setClobAsString(preparedstatement, 1,
								content);

					}

				});
		if (0 == updatedRowCount) {
			throw new SQLException(String.format("未能成功更新大字符串[%s],请检查其是否存在", id));
		}
	}

	public String getString(String id) throws SQLException {
		final String sql = "SELECT CONTENT_ FROM BDF2_CLOB_STORE WHERE ID_=?";
		List<String> list = super.getJdbcTemplate().query(sql, new Object[]{id}, new RowMapper<String>() {
			public String mapRow(ResultSet resultset, int i)
					throws SQLException {
				String content = LobStoreServiceImpl.this
						.getLobHandler().getClobAsString(resultset, 1);
				return content;
			}
		});
		if(list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public String storeAsciiStream(final InputStream asciiStream,
			final int contentLength) throws SQLException {
		final String sql = "INSERT INTO BDF2_CLOB_STORE (ID_,CONTENT_) VALUES(?,?)";
		final String id = this.generateId();
		int updatedRowCount = super.getJdbcTemplate().execute(
				sql,
				new AbstractLobCreatingPreparedStatementCallback(this
						.getLobHandler()) {

					@Override
					protected void setValues(
							PreparedStatement preparedstatement,
							LobCreator lobcreator) throws SQLException,
							DataAccessException {
						preparedstatement.setString(1, id);
						lobcreator.setClobAsAsciiStream(preparedstatement, 2,
								asciiStream, contentLength);
					}
				});
		if (0 == updatedRowCount) {
			throw new SQLException("未能成功储存大字符串");
		}
		return id;
	}
	public void storeAsciiStream(final InputStream asciiStream,
			final int contentLength,final String id) throws SQLException {
		final String sql = "INSERT INTO BDF2_CLOB_STORE (ID_,CONTENT_) VALUES(?,?)";
		int updatedRowCount = super.getJdbcTemplate().execute(
				sql,
				new AbstractLobCreatingPreparedStatementCallback(this
						.getLobHandler()) {
					
					@Override
					protected void setValues(
							PreparedStatement preparedstatement,
							LobCreator lobcreator) throws SQLException,
							DataAccessException {
						preparedstatement.setString(1, id);
						lobcreator.setClobAsAsciiStream(preparedstatement, 2,
								asciiStream, contentLength);
					}
				});
		if (0 == updatedRowCount) {
			throw new SQLException("未能成功储存大字符串");
		}
	}

	public void deleteAsciiStream(String id) throws SQLException {
		this.deleteFromClob(id);
	}

	public void updateAsciiStream(final String id,
			final InputStream asciiStream, final int contentLength)
			throws SQLException {
		final String sql = "UPDATE BDF2_CLOB_STORE SET CONTENT_=? WHERE ID_=?";
		int updatedRowCount = super.getJdbcTemplate().execute(
				sql,
				new AbstractLobCreatingPreparedStatementCallback(this
						.getLobHandler()) {

					@Override
					protected void setValues(
							PreparedStatement preparedstatement,
							LobCreator lobcreator) throws SQLException,
							DataAccessException {
						preparedstatement.setString(2, id);
						lobcreator.setClobAsAsciiStream(preparedstatement, 1,
								asciiStream, contentLength);

					}

				});
		if (0 == updatedRowCount) {
			throw new SQLException(String.format("未能成功更新大字符串[%s],请检查其是否存在", id));
		}

	}

	public InputStream getAsciiStream(String id) throws SQLException {
		final String sql = "SELECT CONTENT_ FROM BDF2_CLOB_STORE WHERE ID_=?";
		List<InputStream> list = super.getJdbcTemplate().query(sql,
				new Object[] { id }, new RowMapper<InputStream>() {

					public InputStream mapRow(ResultSet resultset, int i)
							throws SQLException {
						InputStream content = LobStoreServiceImpl.this
								.getLobHandler().getClobAsAsciiStream(
										resultset, 1);
						return content;
					}

				});
		if(list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public String storeCharacterStream(final Reader reader,
			final int contentLength) throws SQLException {
		final String sql = "INSERT INTO BDF2_CLOB_STORE (ID_,CONTENT_) VALUES(?,?)";
		final String id = this.generateId();
		int updatedRowCount = super.getJdbcTemplate().execute(
				sql,
				new AbstractLobCreatingPreparedStatementCallback(this
						.getLobHandler()) {

					@Override
					protected void setValues(
							PreparedStatement preparedstatement,
							LobCreator lobcreator) throws SQLException,
							DataAccessException {
						preparedstatement.setString(1, id);
						lobcreator.setClobAsCharacterStream(preparedstatement,
								2, reader, contentLength);
					}
				});
		if (0 == updatedRowCount) {
			throw new SQLException("未能成功储存大字符串");
		}
		return id;
	}
	public void storeCharacterStream(final Reader reader,
			final int contentLength,final String id) throws SQLException {
		final String sql = "INSERT INTO BDF2_CLOB_STORE (ID_,CONTENT_) VALUES(?,?)";
		int updatedRowCount = super.getJdbcTemplate().execute(
				sql,
				new AbstractLobCreatingPreparedStatementCallback(this
						.getLobHandler()) {
					
					@Override
					protected void setValues(
							PreparedStatement preparedstatement,
							LobCreator lobcreator) throws SQLException,
							DataAccessException {
						preparedstatement.setString(1, id);
						lobcreator.setClobAsCharacterStream(preparedstatement,
								2, reader, contentLength);
					}
				});
		if (0 == updatedRowCount) {
			throw new SQLException("未能成功储存大字符串");
		}
	}

	public void deleteCharacterStream(String id) throws SQLException {
		this.deleteFromClob(id);
	}

	public void updateCharacterStream(final String id, final Reader reader,
			final int contentLength) throws SQLException {
		final String sql = "UPDATE BDF2_CLOB_STORE SET CONTENT_=? WHERE ID_=?";
		int updatedRowCount = super.getJdbcTemplate().execute(
				sql,
				new AbstractLobCreatingPreparedStatementCallback(this
						.getLobHandler()) {

					@Override
					protected void setValues(
							PreparedStatement preparedstatement,
							LobCreator lobcreator) throws SQLException,
							DataAccessException {
						preparedstatement.setString(2, id);
						lobcreator.setClobAsCharacterStream(preparedstatement,
								1, reader, contentLength);

					}

				});
		if (0 == updatedRowCount) {
			throw new SQLException(String.format("未能成功更新大字符串[%s],请检查其是否存在", id));
		}

	}


	public Reader getCharacterStream(String id) throws SQLException {
		final String sql = "SELECT CONTENT_ FROM BDF2_CLOB_STORE WHERE ID_=?";
		List<Reader> list = super.getJdbcTemplate().query(sql,
				new Object[] { id }, new RowMapper<Reader>() {

					public Reader mapRow(ResultSet resultset, int i)
							throws SQLException {
						Reader content = LobStoreServiceImpl.this
								.getLobHandler().getClobAsCharacterStream(
										resultset, 1);
						return content;
					}

				});
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	protected String generateId() {
		return UUID.randomUUID().toString();
	}


	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

	/**
	 * 根据数据库类型取得LobHander接口的具体实现。
	 * <p>
	 * <li>MySQL、MSSQL、DB2等都遵循JDBC标准的BLOB和CLOB操作API，因此使用DefaultLobHander即可</li>
	 * <li>Oracle9i及以前的版本需要使用OracleLobHandler</li>
	 * </p>
	 * 
	 * @return LobHander 特定于数据库的实例
	 */
	protected LobHandler getLobHandler() {
		if (null == this.lobHandler) {
			synchronized (this) {
				if (null == this.lobHandler) {
					LobHandlerResolver lobHandlerResolver = new LobHandlerResolver(
							this);
					super.getJdbcTemplate().execute(lobHandlerResolver);
				}
			}
		}
		return this.lobHandler;
	}

	private LobHandler lobHandler = null;
}

class LobHandlerResolver implements ConnectionCallback<Void> {
	public LobHandlerResolver(LobStoreServiceImpl lobStoreServiceImpl) {
		this.lobStoreServiceImpl = lobStoreServiceImpl;
	}

	public Void doInConnection(Connection connection) throws SQLException,
			DataAccessException {
		String productName = connection.getMetaData().getDatabaseProductName();
		LobHandler lobHandler = null;
		if (StringUtils.containsIgnoreCase(productName, ORACLE)) {
			lobHandler = createOracleLobHandler();
		} else {
			lobHandler = createDefaultLobHandler(productName);
		}
		this.lobStoreServiceImpl.setLobHandler(lobHandler);
		return null;
	}

	/**
	 * 创建一个通用的的LobHandler,但对SqlServer数据库做了特殊处理.
	 * 
	 * @param productName
	 *            数据库驱动的名称
	 * @return 返回DefaultLobHandler对象
	 */
	private DefaultLobHandler createDefaultLobHandler(String productName) {
		DefaultLobHandler defaultLobHandler = new DefaultLobHandler();
		// 为支持sqljdbc4的处理方式,需要将其大对象流包装到BLOB/CLOB里,否则流将被自动关闭
		if (StringUtils.containsIgnoreCase(productName, SQLSERVER)) {
			defaultLobHandler.setWrapAsLob(true);
		}
		return defaultLobHandler;
	}

	/**
	 * 创建一个特定于Oracle的LobHandler,用于适配Oracle的大对象字段.
	 * 
	 * @return 返回OracleLobHandler对象
	 */
	private OracleLobHandler createOracleLobHandler() {
		OracleLobHandler oracleLobHandler = new OracleLobHandler();
		Map<String, NativeJdbcExtractor> jdbcExtractors = ContextHolder.getApplicationContext().getBeansOfType(
						NativeJdbcExtractor.class);
		NativeJdbcExtractor jdbcExtractor;
		if (null == jdbcExtractors || 0 == jdbcExtractors.size()) {
			jdbcExtractor = new CommonsDbcpNativeJdbcExtractor();
		} else {
			jdbcExtractor = jdbcExtractors.values().iterator().next();
		}
		oracleLobHandler.setNativeJdbcExtractor(jdbcExtractor);
		return oracleLobHandler;
	}

	private LobStoreServiceImpl lobStoreServiceImpl = null;
	private static final String ORACLE = "oracle";
	private static final String SQLSERVER = "sql server";
}