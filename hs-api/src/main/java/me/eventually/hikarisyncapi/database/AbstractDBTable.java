package me.eventually.hikarisyncapi.database;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * A table definition for the database.
 * tableName is the name of the table.
 * Must implement getFields()
 * @author Eventually
 */
@ParametersAreNonnullByDefault
public abstract class AbstractDBTable {
    protected String tableName;
    public AbstractDBTable(String tableName) {
        this.tableName = tableName;
    }
    protected abstract List<DBField> getFields();

    /**
     * A field definition for the database.
     * @author Eventually
     */
    protected static class DBField {
        private String name;
        private String type;
        private Boolean nullable;
        private Boolean primaryKey;
        private Boolean isId = false;

        /**
         * If your field is the id, set this to true, this will automatically set the type to INT and make it the primary key.
         * Please note id field should be the first field in the table.
         * @param isId true if this field is id
         */
        public DBField(Boolean isId){
            this.isId = isId;
        }

        /**
         * Common constructor for all fields.
         * @param name
         * @param type
         */
        public DBField(String name, String type) {
            this.name = name;
            this.type = type;
            this.nullable = false;
            this.primaryKey = false;
            this.isId = false;
        }

        /**
         * Primary key
         * @param primaryKey true if this field is primary key
         * @return field instance, can be chained.
         */
        public DBField setPrimaryKey(Boolean primaryKey) {
            this.primaryKey = primaryKey;
            return this;
        }
        /**
         * Nullable
         * @param nullable true if this field is nullable
         * @return field instance, can be chained.
         */
        public DBField setNullable(Boolean nullable) {
            this.nullable = nullable;
            return this;
        }

        /**
         * Type(String)
         * @param type type of the field
         * @return field instance, can be chained.
         */
        public DBField setType(String type) {
            this.type = type;
            return this;
        }

        public Boolean isId() {
            return isId;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
        public Boolean getNullable() {
            return nullable;
        }

        public Boolean getPrimaryKey() {
            return primaryKey;
        }
    }
    public String generateCreateTableScript() throws IllegalStateException{
        StringBuilder script = new StringBuilder();
        script
                .append("CREATE TABLE IF NOT EXISTS ")
                .append(tableName)
                .append(" (");
        int i = 0;
        for (DBField field : getFields()) {
            i++;
            if (field.isId()) {
                if (i != 1){
                    throw new IllegalArgumentException("The id key should be first.");
                }
                script.append("id INT AUTO_INCREMENT PRIMARY KEY, ");
            } else {
                script.append(field.getName()).append(" ").append(field.getType());
                if (field.getPrimaryKey()) {
                    script.append(" PRIMARY KEY");
                }
                if (!field.getNullable()) {
                    script.append(" NOT NULL");
                }
                script.append(", ");
            }
        }
        script.delete(script.length() - 2, script.length());
        script.append(");");
        return script.toString();
    }
}
