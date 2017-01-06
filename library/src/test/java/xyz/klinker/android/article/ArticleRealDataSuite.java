/*
 * Copyright (C) 2017 Jacob Klinker
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

package xyz.klinker.android.article;

import android.annotation.TargetApi;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.robolectric.RuntimeEnvironment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import xyz.klinker.android.article.data.DataSource;
import xyz.klinker.android.article.data.DatabaseSQLiteHelper;

import static junit.framework.Assert.assertTrue;

public abstract class ArticleRealDataSuite extends ArticleRobolectricSuite {

    public DataSource source;

    @Before
    public void setUp() throws Exception {
        SQLiteDatabase database = SQLiteDatabase.create(null);
        DatabaseSQLiteHelper helper = new DatabaseSQLiteHelper(RuntimeEnvironment.application);
        helper.onCreate(database);

        source = new DataSource(database);
        insertData();
    }

    private void insertData() throws Exception {
        SQLiteDatabase database = source.getDatabase();
        FixtureLoader loader = new FixtureLoader();
        loader.loadFixturesToDatabase(database);
    }

    /**
     * Loads data from assets/fixtures into the given database. This class will read any files that
     * are stored in the fixtures folder. These files should be named by the table that the data
     * should be inserted into. Inside of each file, data should be separated by |'s, much like a
     * cucumber test.
     * <p/>
     * For example, a file could be named apps.fixture (or apps.txt, etc.) and have the following
     * table stored inside of it:
     * | app_uid    | name      | package_name                      |
     * | 1          | Source    | com.klinker.android.source        |
     * | 2          | EvolveSMS | com.klinker.android.evolve_sms    |
     * | 3          | Talon     | com.klinker.android.twitter       |
     * <p/>
     * This would load 3 apps into the database with the given names.
     */
    public static class FixtureLoader {

        private static final String FIXTURE_FOLDER = System.getProperty("user.dir") +
                "/src/test/assets/fixtures";

        private File folder;

        public FixtureLoader() {
            folder = new File(FIXTURE_FOLDER);
            assertTrue(folder.exists());
        }

        @TargetApi(19)
        public void loadFixturesToDatabase(SQLiteDatabase database, String... fixturesToLoad)
                throws Exception {
            for (File fixture : folder.listFiles()) {
                String tableName = fixture.getName().split("\\.")[0];

                if (fixturesToLoad != null && fixturesToLoad.length > 0) {
                    boolean found = false;

                    for (String toLoad : fixturesToLoad) {
                        if (tableName.equals(toLoad)) {
                            found = true;
                        }
                    }

                    if (!found) {
                        continue;
                    }
                }

                String[] columnNames = new String[]{};
                List<String[]> lines = new ArrayList<String[]>();
                try (BufferedReader br = new BufferedReader(new FileReader(fixture))) {
                    String line;
                    boolean isFirst = true;
                    while ((line = br.readLine()) != null) {
                        String[] data = line.substring(1, line.length() - 2).split("\\|");

                        if (isFirst) {
                            columnNames = data;
                            isFirst = false;
                        } else {
                            lines.add(data);
                        }
                    }

                    if (lines.size() == 0) {
                        continue;
                    }

                    StringBuilder names = new StringBuilder();
                    names.append("(");

                    for (int i = 0; i < columnNames.length; i++) {
                        names.append(columnNames[i].trim());

                        if (i < columnNames.length - 1) {
                            names.append(", ");
                        }
                    }

                    names.append(")");

                    String insert = "insert into " + tableName + " " + names.toString();

                    database.beginTransaction();

                    for (String[] l : lines) {
                        StringBuilder values = new StringBuilder();
                        values.append("(\"");

                        for (int i = 0; i < l.length; i++) {
                            values.append(l[i].trim());

                            if (i < l.length - 1) {
                                values.append("\", \"");
                            }
                        }

                        values.append("\");");

                        String statement = insert + " values " + values.toString();
                        database.execSQL(statement);
                    }

                    database.setTransactionSuccessful();
                    database.endTransaction();
                }
            }
        }

    }

}
