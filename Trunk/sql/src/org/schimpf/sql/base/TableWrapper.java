/**
 * | This program is free software: you can redistribute it and/or modify
 * | it under the terms of the GNU General Public License as published by
 * | the Free Software Foundation, either version 3 of the License.
 * |
 * | This program is distributed in the hope that it will be useful,
 * | but WITHOUT ANY WARRANTY; without even the implied warranty of
 * | MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * | GNU General Public License for more details.
 * |
 * | You should have received a copy of the GNU General Public License
 * | along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Apr 26, 2012 7:18:00 PM
 */
package org.schimpf.sql.base;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Metodos para obtencion de datos de las tablas
 * 
 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
 * @author <B>Schimpf.NET</B>
 * @version Apr 26, 2012 7:18:00 PM
 * @param <SQLConnector> Tipo de conexion SQL
 * @param <MType> Tipo de Sistema de Base de Datos
 * @param <DType> Tipo de Base de Datos
 * @param <SType> Tipo de esquema
 * @param <TType> Tipo de tabla
 * @param <CType> Tipo de columnas
 */
public abstract class TableWrapper<SQLConnector extends SQLProcess, MType extends DBMSWrapper<SQLConnector, MType, DType, SType, TType, CType>, DType extends DataBaseWrapper<SQLConnector, MType, DType, SType, TType, CType>, SType extends SchemaWrapper<SQLConnector, MType, DType, SType, TType, CType>, TType extends TableWrapper<SQLConnector, MType, DType, SType, TType, CType>, CType extends ColumnWrapper<SQLConnector, MType, DType, SType, TType, CType>> extends BaseWrapper<SQLConnector> implements Comparable<TType> {
	/**
	 * Columnas de la tabla
	 * 
	 * @version Apr 26, 2012 7:20:49 PM
	 */
	private final HashMap<String, CType>			columns		= new HashMap<String, CType>();

	/**
	 * Columnas Foraneas de la tabla
	 * 
	 * @version Oct 4, 2012 9:45:43 AM
	 */
	private final ArrayList<ForeignKey<CType>>	fkColumns	= new ArrayList<ForeignKey<CType>>();

	/**
	 * Columnas Clave Primaria de la tabla
	 * 
	 * @version May 2, 2012 11:09:49 AM
	 */
	private final ArrayList<CType>					pkColumns	= new ArrayList<CType>();

	/**
	 * Esquema al que pertenece la tabla
	 * 
	 * @version May 2, 2012 2:02:12 AM
	 */
	private final SType									schema;

	/**
	 * Nombre fisico de la tabla
	 * 
	 * @version Apr 26, 2012 7:32:31 PM
	 */
	private final String									tableName;

	/**
	 * Columnas con indice UNIQUE
	 * 
	 * @version Oct 4, 2012 10:21:06 AM
	 */
	private final ArrayList<CType>					uqColumns	= new ArrayList<CType>();

	/**
	 * Clase para obtener los datos de claves foraneas de tablas
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 4, 2012 9:48:26 AM
	 * @param <FKCType> Tipo de columnas
	 */
	@SuppressWarnings("unchecked")
	public static final class ForeignKey<FKCType extends ColumnWrapper> {
		/**
		 * Columnas relacionadas de la clave foranea
		 * 
		 * @version Oct 4, 2012 9:51:49 AM
		 */
		private final HashMap<FKCType, FKCType>	foreignKeyColumns;

		/**
		 * Nombre de la clave foranea
		 * 
		 * @version Oct 4, 2012 9:51:37 AM
		 */
		private final String								foreignKeyName;

		/**
		 * Politica al eliminar
		 * 
		 * @version Oct 4, 2012 11:47:22 AM
		 */
		private final Short								onDelete;

		/**
		 * Politica al actualizar
		 * 
		 * @version Oct 4, 2012 11:47:15 AM
		 */
		private final Short								onUpdate;

		/**
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
		 * @version Oct 4, 2012 9:48:40 AM
		 * @param fkName Nombre de la FK
		 * @param columns Columnas relacionadas de la FK
		 * @param onUpdate Politica al actualizar
		 * @param onDelete Politica al eliminar
		 */
		protected ForeignKey(final String fkName, final HashMap<FKCType, FKCType> columns, final Short onUpdate, final Short onDelete) {
			// almacenamos el nombre de la FK
			this.foreignKeyName = fkName;
			// almacenamos las columnas
			this.foreignKeyColumns = columns;
			// almacenamos las logicas
			this.onUpdate = onUpdate;
			this.onDelete = onDelete;
		}

		/**
		 * Retorna las columnas de la clave foranea
		 * 
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
		 * @version Oct 4, 2012 9:52:14 AM
		 * @return Columnas de la clave
		 */
		public HashMap<FKCType, FKCType> getColumns() {
			// retornamos las columnas
			return this.foreignKeyColumns;
		}

		/**
		 * Retorna el nombre de la clave foranea
		 * 
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
		 * @version Oct 4, 2012 9:53:16 AM
		 * @return Nombre de la clave foranea
		 */
		public String getForeignKeyName() {
			// retornamos el nombre
			return this.foreignKeyName;
		}

		@Override
		public int hashCode() {
			// enviamos el padre
			return super.hashCode();
		}

		/**
		 * Retorna la accion a tomar cuando la clave relacionada es eliminada
		 * 
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
		 * @version Oct 4, 2012 11:49:27 AM
		 * @return Accion a tomar cuando la clave es eliminada
		 * @see java.sql.DatabaseMetaData#importedKeyCascade
		 * @see java.sql.DatabaseMetaData#importedKeyNoAction
		 * @see java.sql.DatabaseMetaData#importedKeySetNull
		 * @see java.sql.DatabaseMetaData#importedKeyRestrict
		 * @see java.sql.DatabaseMetaData#importedKeySetDefault
		 */
		public Short onDelete() {
			// retornamos la politica al eliminar
			return this.onDelete;
		}

		/**
		 * Retorna la accion a tomar cuando la clave relacionada es actualizada
		 * 
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
		 * @version Oct 4, 2012 11:49:48 AM
		 * @return Accion a tomar cuando la clave es modificada
		 * @see java.sql.DatabaseMetaData#importedKeyCascade
		 * @see java.sql.DatabaseMetaData#importedKeyNoAction
		 * @see java.sql.DatabaseMetaData#importedKeySetNull
		 * @see java.sql.DatabaseMetaData#importedKeyRestrict
		 * @see java.sql.DatabaseMetaData#importedKeySetDefault
		 */
		public Short onUpdate() {
			// retornamos la politica al actualizar
			return this.onUpdate;
		}

		/**
		 * Compara fisicamente la clave foranea actual con la clave foranea especificada
		 * 
		 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
		 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
		 * @version Nov 19, 2012 6:50:14 PM
		 * @param foreignKey Clave foranea a comparar
		 * @return True si la clave foranea especificada es igual a la clave foranea actual
		 */
		public boolean physicalEquals(final ForeignKey<FKCType> foreignKey) {
			try {
				// verificamos si es null
				if (foreignKey == null)
					// retornamos false
					return false;
				// verificamos si la FK tiene el mismo nombre
				if (!this.getForeignKeyName().equals(foreignKey.getForeignKeyName()))
					// retornamos false
					return false;
				// verificamos si tienen la misma cantidad de columnas
				if (this.getColumns().size() != foreignKey.getColumns().size())
					// retornamos false
					return false;
				// obtenemos las columnas relacionadas
				Iterator<Entry<FKCType, FKCType>> fkColumns = this.getColumns().entrySet().iterator();
				// recorremos las columnas
				while (fkColumns.hasNext()) {
					// obtenemos la tupla de columnas
					Entry<FKCType, FKCType> fks = fkColumns.next();
					// generamos una bandera
					boolean fkEqual = false;
					// obtenemos las columnas relacionadas de la otra tabla
					Iterator<Entry<FKCType, FKCType>> lastFKColumns = foreignKey.getColumns().entrySet().iterator();
					// recorremoas las columnas de la otra tabla
					while (lastFKColumns.hasNext()) {
						// obtenemos la tupla de columnas
						Entry<FKCType, FKCType> lastKFs = lastFKColumns.next();
						// verificamos si es la tupla correcta
						if (fks.getKey().getColumnName().equals(lastKFs.getKey().getColumnName()) && fks.getValue().getColumnName().equals(lastKFs.getValue().getColumnName())) {
							// modificamos la bandera
							fkEqual = true;
							// finalizamos el bucle
							break;
						}
					}
					// verificamos si se encontro la tupla
					if (!fkEqual)
						// retornamos false
						return false;
				}
			} catch (Exception e) {
				// retornamos false
				return false;
			}
			// retornamos true
			return true;
		}

		@Override
		public String toString() {
			// armamos la definicion de la FK
			StringBuffer fk = new StringBuffer("FOREIGN KEY " + this.getForeignKeyName());
			StringBuffer localColumns = new StringBuffer();
			StringBuffer foreignColumns = new StringBuffer();
			// obtenemos las columnas
			Iterator<Entry<FKCType, FKCType>> couples = this.getColumns().entrySet().iterator();
			// recorremos las columnas
			while (couples.hasNext()) {
				// obtenemos la relacion
				Entry<FKCType, FKCType> couple = couples.next();
				// agregamos la columna origen
				localColumns.append((localColumns.toString().length() == 0 ? "" : ", ") + couple.getKey().getColumnName());
				// agregamos la columna destino
				foreignColumns.append((foreignColumns.toString().length() == 0 ? "" : ", ") + couple.getValue().getColumnName());
			}
			// agregamos las columnas origen
			fk.append(" (" + localColumns.toString() + ") REFERENCES " + this.getColumns().entrySet().iterator().next().getValue().getTable().getTableName() + " (" + foreignColumns.toString() + ")");
			// agregamos la accion al UPDATE
			fk.append(" ON UPDATE");
			fk.append(this.onUpdate() == java.sql.DatabaseMetaData.importedKeyCascade ? " CASCADE" : "");
			fk.append(this.onUpdate() == java.sql.DatabaseMetaData.importedKeyNoAction ? " NO ACTION" : "");
			fk.append(this.onUpdate() == java.sql.DatabaseMetaData.importedKeyRestrict ? " RESTRICT" : "");
			fk.append(this.onUpdate() == java.sql.DatabaseMetaData.importedKeySetDefault ? " SET DEFAULT" : "");
			fk.append(this.onUpdate() == java.sql.DatabaseMetaData.importedKeySetNull ? " SET NULL" : "");
			// agregamos la accion al DELETE
			fk.append(" ON DELETE");
			fk.append(this.onDelete() == java.sql.DatabaseMetaData.importedKeyCascade ? " CASCADE" : "");
			fk.append(this.onDelete() == java.sql.DatabaseMetaData.importedKeyNoAction ? " NO ACTION" : "");
			fk.append(this.onDelete() == java.sql.DatabaseMetaData.importedKeyRestrict ? " RESTRICT" : "");
			fk.append(this.onDelete() == java.sql.DatabaseMetaData.importedKeySetDefault ? " SET DEFAULT" : "");
			fk.append(this.onDelete() == java.sql.DatabaseMetaData.importedKeySetNull ? " SET NULL" : "");
			// retornamos la definicion de la FK
			return fk.toString();
		}
	}

	/**
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:33:20 PM
	 * @param schema Esquema de la tabla
	 * @param tableName Nombre de la tabla
	 */
	protected TableWrapper(final SType schema, final String tableName) {
		// enviamos el conector SQL
		super(schema.getSQLConnector());
		// almacenamos el esquema
		this.schema = schema;
		// almacenamos el nombre de la tabla
		this.tableName = tableName;
	}

	@Override
	public int compareTo(final TType table) {
		// retornamos si es el mismo esquema y la misma tabla
		return this.getSchema().compareTo(table.getSchema()) == 0 && this.getTableName().equals(table.getTableName()) ? 0 : 1;
	}

	/**
	 * Retorna una columna
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 10:34:48 AM
	 * @param columnName Nombre de la columna
	 * @return Columna o Null si no existe
	 * @throws SQLException Si se produce un error al cargar la columna
	 */
	public final CType getColumn(final String columnName) throws SQLException {
		// cargamos las columnas
		this.getColumns();
		// retornamos la columna
		return this.columns.get(columnName);
	}

	/**
	 * Retorna las columnas de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:24:25 PM
	 * @return Lista de columnas de la tabla
	 * @throws SQLException Si no se pueden obtener las columnas de la tabla
	 */
	public final ArrayList<CType> getColumns() throws SQLException {
		// retornamos las columnas de la tabla
		return this.getColumns(false);
	}

	/**
	 * Retorna las columnas de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:26:02 PM
	 * @param reload True para recargar las columnas de la tabla
	 * @return Lista de columnas de la tabla
	 * @throws SQLException Si no se pueden obtener las columnas de la tabla
	 */
	public synchronized final ArrayList<CType> getColumns(final boolean reload) throws SQLException {
		// verificamos si ya cargamos las columnas
		if (this.columns.size() == 0 || reload) {
			// vaciamos la lista
			this.columns.clear();
			// recorremos las columnas
			for (final CType column: this.retrieveColumns(this.getTableName()))
				// agregamos la columna de la tabla
				this.columns.put(column.getColumnName(), column);
		}
		// retornamos las columnas
		return this.<CType> toArrayList(this.columns);
	}

	/**
	 * Retorna las claves foraneas de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 4, 2012 9:46:55 AM
	 * @return Lista de claves foraneas de la tabla
	 * @throws SQLException Si se produce un error al obtener las claves foraneas
	 */
	@SuppressWarnings("unchecked")
	public synchronized final ArrayList<ForeignKey<CType>> getForeignKeys() throws SQLException {
		// verificamos si tenemos la lista
		if (this.fkColumns.size() == 0) {
			// obtenemos las columnas PKs
			ResultSet foreignKeys = this.getSQLConnector().getMetadata().getImportedKeys(this.getSchema().getDataBase().getDataBaseName(), null, this.getTableName());
			// armamos una lista
			HashMap<String, HashMap<String, Object>> fks = new HashMap<String, HashMap<String, Object>>();
			// recorremos las columnas
			while (foreignKeys.next()) {
				// verificamos si la FK ya existe
				if (!fks.containsKey(foreignKeys.getString(12))) {
					// creamos la lista para los valores
					fks.put(foreignKeys.getString(12), new HashMap<String, Object>());
					// agregamos las validaciones de la FK
					fks.get(foreignKeys.getString(12)).put("UPDATE", foreignKeys.getShort(10));
					fks.get(foreignKeys.getString(12)).put("DELETE", foreignKeys.getShort(11));
					// creamos la lista de columnas
					fks.get(foreignKeys.getString(12)).put("COLUMNS", new HashMap<CType, CType>());
				}
				// agregamos la colunma a la FK
				((HashMap<CType, CType>) fks.get(foreignKeys.getString(12)).get("COLUMNS")).put(this.getColumn(foreignKeys.getString(8)), this.getSchema().getTable(foreignKeys.getString(3)).getColumn(foreignKeys.getString(4)));
			}
			// obtenemos las FKs armadas
			Iterator<Entry<String, HashMap<String, Object>>> armedFKs = fks.entrySet().iterator();
			// recorremos las FKs armadas
			while (armedFKs.hasNext()) {
				// obtenemos la FK
				Entry<String, HashMap<String, Object>> fk = armedFKs.next();
				// agregamos la clave foranea
				this.fkColumns.add(new ForeignKey(fk.getKey(), (HashMap<CType, CType>) fk.getValue().get("COLUMNS"), (Short) fk.getValue().get("UPDATE"), (Short) fk.getValue().get("DELETE")));
			}
		}
		// retornamos las columnas FK
		return this.fkColumns;
	}

	/**
	 * Retorna las columnas clave primarias de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 11:11:40 AM
	 * @throws SQLException Si se produce un error al identificar las columnas primarias
	 * @return Lista de columnas Clave Primarias
	 */
	public synchronized final ArrayList<CType> getPrimaryKeys() throws SQLException {
		// verificamos si tenemos la lista
		if (this.pkColumns.size() == 0) {
			// obtenemos las columnas PKs
			ResultSet primaryKeys = this.getSQLConnector().getMetadata().getPrimaryKeys(this.getSchema().getDataBase().getDataBaseName(), null, this.getTableName());
			// creamos una lista temporal
			HashMap<Short, String> unsortedPKs = new HashMap<Short, String>();
			// recorremos las columnas
			while (primaryKeys.next())
				// agregamos la columna PK con su posicion
				unsortedPKs.put(primaryKeys.getShort(5), primaryKeys.getString(4));
			// recorremos las columnas en orden
			for (short i = 1; i <= unsortedPKs.size(); i++)
				// agregamos la columna PK
				this.pkColumns.add(this.getColumn(unsortedPKs.get(i)));
		}
		// retornamos las columnas PK
		return this.pkColumns;
	}

	/**
	 * Retorna el esquema al que pertenece la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version May 2, 2012 2:02:30 AM
	 * @return Esquema de la tabla
	 */
	public final SType getSchema() {
		// retornamos el esquema
		return this.schema;
	}

	/**
	 * Retorna el nombre de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:33:44 PM
	 * @return Nombre de la tabla
	 */
	public final String getTableName() {
		// retornamos el nombre de la tabla
		return this.tableName;
	}

	@Override
	public int hashCode() {
		// enviamos el padre
		return super.hashCode();
	}

	/**
	 * Compara fisicamente la tabla actual con la tabla especificada
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Nov 19, 2012 6:50:14 PM
	 * @param table Tabla a comparar
	 * @return True si la tabla especificada es igual a la tabla actual
	 */
	public boolean physicalEquals(final TType table) {
		try {
			// verificamos si la tabla es null
			if (table == null)
				// retornamos false
				return false;
			// comparamos la cantidad de columnas
			if (this.getColumns().size() != table.getColumns().size())
				// retornamos false
				return false;
			// comparamos la cantidad de columnas PK
			if (this.getPrimaryKeys().size() != table.getPrimaryKeys().size())
				// retornamos false
				return false;
			// comparamos la cantidad de columnas FK
			if (this.getForeignKeys().size() != table.getForeignKeys().size())
				// retornamos false
				return false;
			// recorremos las columnas
			for (CType column: this.getColumns())
				// verificamos si existe una columna con el mismo nombre en la otra tabla y comparamos las columnas
				if (table.getColumn(column.getColumnName()) == null || !column.physicalEquals(table.getColumn(column.getColumnName())))
					// retornamos false
					return false;
			// recorremos las columnas FK
			for (ForeignKey<CType> column: this.getForeignKeys())
				// verificamos si son iguales
				if (!column.physicalEquals(table.getForeignKeys().get(this.getForeignKeys().indexOf(column))))
					// retornamos false
					return false;
		} catch (Exception e) {
			// retornamos false
			return false;
		}
		// retornamos true
		return true;
	}

	@Override
	public String toString() {
		// retornamos el nombre de la tabla
		return this.getTableName();
	}

	/**
	 * Carga las columnas de la tabla
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>SCHIMPF</B> - <FONT style="font-style:italic;">Sistemas de Informaci&oacute;n y Gesti&oacute;n</FONT>
	 * @author <B>Schimpf.NET</B>
	 * @version Apr 26, 2012 7:26:44 PM
	 * @param tableName Nombre de la tabla actual
	 * @return Lista de columnas de la tabla
	 * @throws SQLException Si se produce un error al obtener la lista de las columnas
	 */
	protected abstract ArrayList<CType> retrieveColumns(String tableName) throws SQLException;

	/**
	 * Retorna las columnas con Indice UNIQUE
	 * 
	 * @author <FONT style='color:#55A; font-size:12px; font-weight:bold;'>Hermann D. Schimpf</FONT>
	 * @author <B>HDS Solutions</B> - <FONT style="font-style:italic;">Soluci&oacute;nes Inform&aacute;ticas</FONT>
	 * @version Oct 4, 2012 10:22:33 AM
	 * @return Columnas con indice UQ
	 * @throws SQLException Si se produce un error al obtener las columnas
	 */
	synchronized final ArrayList<CType> getUniqueColumns() throws SQLException {
		// verificamos si tenemos la lista
		if (this.uqColumns.size() == 0) {
			// obtenemos las claves
			ResultSet uqs = this.getSQLConnector().getMetadata().getIndexInfo(this.getSchema().getDataBase().getDataBaseName(), null, this.getTableName(), true, false);
			// recorremos las columnas
			while (uqs.next())
				// verificamos si es una PK
				if (this.getPrimaryKeys().contains(this.getColumn(uqs.getString(9))))
					// agregamos la columna UQ
					this.uqColumns.add(this.getColumn(uqs.getString(9)));
		}
		// retornamos las columnas UQ
		return this.uqColumns;
	}
}