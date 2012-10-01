package studyorm.querydslsql.springdataext;

import java.util.Date;

import com.mysema.query.Tuple;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;

public class QueryDslTemplateUtil {
    @SuppressWarnings("serial")
    public static <T> MappingProjection<T> mapping(Class<T> type,
            final Expression<T> exp) {
        return new MappingProjection<T>(type, exp) {
            @Override
            protected T map(Tuple row) {
                return row.get(exp);
            }
        };
    }

    public static java.sql.Date toSql(Date date) {
        return new java.sql.Date(date.getTime());
    }

}
