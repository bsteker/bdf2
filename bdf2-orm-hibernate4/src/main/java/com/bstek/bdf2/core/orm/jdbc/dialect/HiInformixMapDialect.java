package com.bstek.bdf2.core.orm.jdbc.dialect;
import java.sql.Types;

import org.hibernate.dialect.InformixDialect;

/**
 * @since 2013-1-17
 * @author Jacky.gao
 */
public class HiInformixMapDialect extends InformixDialect {
    public HiInformixMapDialect() {
        super();
        registerColumnType(Types.BLOB, "BYTE" );
        registerColumnType(Types.CLOB, "TEXT");
  }
}
