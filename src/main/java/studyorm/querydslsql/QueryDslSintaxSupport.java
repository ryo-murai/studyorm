package studyorm.querydslsql;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.data.jdbc.support.DatabaseType;

import com.mysema.query.sql.HSQLDBTemplates;
import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.OracleTemplates;
import com.mysema.query.sql.PostgresTemplates;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLQueryImpl;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.Expression;

public class QueryDslSintaxSupport {
	private final Connection conn;
	private QueryDslSintaxSupport(Connection conn) {
		this.conn = conn;
	}

	public static QueryDslSintaxSupport queryDsl(Connection conn) {
		return new QueryDslSintaxSupport(conn);
	}

	public SQLQuery query() {
		return new SQLQueryImpl(getConnection(), getDialect());
	}

	public SQLQuery queryFrom(Expression<?>... o) {
		return query().from(o);
	}

	public SQLInsertClause insertInto(RelationalPath<?> o)  {
		return new SQLInsertClause(getConnection(), getDialect(), o);
	}
	
	public SQLUpdateClause update(RelationalPath<?> o) {
		return new SQLUpdateClause(getConnection(), getDialect(), o);
	}
	
	public SQLDeleteClause delete(RelationalPath<?> o) {
		return new SQLDeleteClause(getConnection(), getDialect(), o);
	}

	
	protected Connection getConnection() {
		return conn;
	}
	
	protected SQLTemplates getDialect() {
		return createDialect(getConnection());
	}
	
	private static SQLTemplates createDialect(Connection connection) {
		try {
			DatabaseType dbType = DatabaseType.fromProductName(connection.getMetaData().getDatabaseProductName());
			switch(dbType) {
			case HSQL: 
				return new HSQLDBTemplates();
			case POSTGRES: 
				return new PostgresTemplates();
			case MYSQL: 
				return new MySQLTemplates();
			case ORACLE: 
				return new OracleTemplates();
			// other database management systems are also available, but
			// yet implemented in this class.
			default: 
				throw new IllegalArgumentException(dbType+" is not supported or implemented");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
