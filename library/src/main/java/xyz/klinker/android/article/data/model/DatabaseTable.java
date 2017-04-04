/*
 * Copyright (C) 2017 Jake Klinker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.klinker.android.article.data.model;

/**
 * A table definition to be inserted into a database.
 */
public interface DatabaseTable {

    /**
     * Provides the create statement, defining a columns for the table.
     *
     * @return a SQLite formatted create statement.
     */
    String getCreateStatement();

    /**
     * Gets the table name.
     *
     * @return the SQLite table name.
     */
    String getTableName();

    /**
     * Gets statements that can be run to index the table on a certain column.
     *
     * @return an array of SQLite statement used to index a column for faster querying.
     */
    String[] getIndexStatements();

}
