
package com.example.planets.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class DbHandler {

	public static void performExecute(@NotNull SQLiteDatabase database, @NotNull String sql, Object[] parameters) throws SQLException {
		SQLiteStatement compiledStatement = getCompiledStatement(database, sql, parameters);
		compiledStatement.execute();
	}

	public static long performExecuteInsert(@NotNull SQLiteDatabase database, @NotNull String sql, Object[] parameters) throws SQLException {
		SQLiteStatement compiledStatement = getCompiledStatement(database, sql, parameters);
		return compiledStatement.executeInsert();
	}

	public static boolean performExecuteUpdateDelete(@NotNull SQLiteDatabase database, @NotNull String sql, Object[] parameters) throws SQLException {
		SQLiteStatement compiledStatement = getCompiledStatement(database, sql, parameters);
		return compiledStatement.executeUpdateDelete() > 0;
	}

	public static void performExecute(@NotNull SQLiteStatement sqLiteStatement, Object[] parameters) throws SQLException {
		SQLiteStatement compiledStatement = bindParameters(sqLiteStatement, parameters);
		compiledStatement.execute();
	}

	public static long performExecuteInsert(@NotNull SQLiteStatement sqLiteStatement, Object[] parameters) throws SQLException {
		SQLiteStatement compiledStatement = bindParameters(sqLiteStatement, parameters);
		return compiledStatement.executeInsert();
	}

	public static boolean performExecuteUpdateDelete(@NotNull SQLiteStatement sqLiteStatement, Object[] parameters) throws SQLException {
		SQLiteStatement compiledStatement = bindParameters(sqLiteStatement, parameters);
		return compiledStatement.executeUpdateDelete() > 0;
	}

	public static Cursor performRawQuery(@NotNull SQLiteDatabase database, @NotNull String sql, Object[] parameters) {
		return database.rawQuery(sql, convertToStringArray(parameters));
	}

	private static SQLiteStatement getCompiledStatement(@NotNull SQLiteDatabase database, @NotNull String sql, Object[] parameters) throws SQLException {
		SQLiteStatement sqLiteStatement = database.compileStatement(sql);
		String[] stringParameters = convertToStringArray(parameters);
		if (stringParameters != null) {
			sqLiteStatement.bindAllArgsAsStrings(stringParameters);
		}
		return sqLiteStatement;
	}

	private static SQLiteStatement bindParameters(@NotNull SQLiteStatement sqLiteStatement, Object[] parameters) {
		String[] stringParameters = convertToStringArray(parameters);
		if (stringParameters != null) {
			sqLiteStatement.bindAllArgsAsStrings(stringParameters);
		}
		return sqLiteStatement;
	}

	@Nullable
	private static String[] convertToStringArray(Object[] parameters) {
		if (parameters == null) {
			return null;
		}
		final int PARAMETERS_LENGTH = parameters.length;
		if (PARAMETERS_LENGTH == 0) {
			return null;
		}
		String[] stringParameters = new String[PARAMETERS_LENGTH];
		for (int i = 0; i < PARAMETERS_LENGTH; i++) {
			stringParameters[i] = String.valueOf(parameters[i]);
		}
		return stringParameters;
	}
}
