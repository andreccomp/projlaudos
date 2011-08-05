package br.teste;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

public class PhoneProvider extends ContentProvider {

	public static final Uri CONTENT_URI = Uri.parse("content://br.teste.phoneprovider");

	public static final String AUTHORITY = "br.teste.phoneprovider";

	// Nome do arquivo que ir� conter o banco de dados.
	private static final String DATABASE_NAME = "phone.db";

	// Versao do banco de dados.
	// Este valor � importante pois � usado em futuros updates do DB.
	private static final int DATABASE_VERSION = 1;

	// Nome da tabela que ir� conter as anota��es.
	private static final String TESTE_TABLE = "teste";

	// 'Id' da Uri referente �s notas do usu�rio.
	private static final int TESTE = 1;

	private DBHelper mHelper;

	// Uri matcher - usado para extrair informa��es das Uris
	private static final UriMatcher mMatcher;

	private static HashMap<String, String> mProjection;

	static {
		mProjection = new HashMap<String, String>();
		mProjection.put(Teste.TESTE_ID, Teste.TESTE_ID);
		mProjection.put(Teste.TEXT, Teste.TEXT);
	}

	static {
		mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mMatcher.addURI(AUTHORITY, TESTE_TABLE, TESTE);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		int count;
		switch (mMatcher.match(uri)) {
		case TESTE:
			count = db.delete(TESTE_TABLE, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("URI desconhecida " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (mMatcher.match(uri)) {
		case TESTE:
			return Teste.CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("URI desconhecida " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (mMatcher.match(uri)) {
		case TESTE:
			SQLiteDatabase db = mHelper.getWritableDatabase();
			long rowId = db.insert(TESTE_TABLE, Teste.TEXT, values);
			if (rowId > 0) {
				Uri noteUri = ContentUris.withAppendedId(Teste.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(noteUri, null);
				return noteUri;
			}
		default:
			throw new IllegalArgumentException("URI desconhecida " + uri);
		}
	}

	@Override
	public boolean onCreate() {
		mHelper = new DBHelper(getContext());
		;
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// Aqui usaremos o SQLiteQueryBuilder para construir
		// a query que ser� feito ao DB, retornando um cursor
		// que enviaremos � aplica��o.
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		SQLiteDatabase database = mHelper.getReadableDatabase();
		Cursor cursor;
		switch (mMatcher.match(uri)) {
		case TESTE:
			// O Builer receber� dois parametros: a tabela
			// onde ser� feita a busca, e uma projection -
			// que nada mais � que uma HashMap com os campos
			// que queremos recuperar do banco de dados.
			builder.setTables(TESTE_TABLE);
			builder.setProjectionMap(mProjection);
			break;

		default:
			throw new IllegalArgumentException("URI desconhecida " + uri);
		}

		cursor = builder.query(database, projection, selection, selectionArgs, null, null, sortOrder);

		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		int count;
		switch (mMatcher.match(uri)) {
		case TESTE:
			count = db.update(TESTE_TABLE, values, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("URI desconhecida " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	public static final class Teste implements BaseColumns {
		public static final Uri CONTENT_URI = Uri.parse("content://" + PhoneProvider.AUTHORITY + "/teste");

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + PhoneProvider.AUTHORITY;

		public static final String TESTE_ID = "_id";

		public static final String TEXT = "text";
	}

	private static class DBHelper extends SQLiteOpenHelper {

		DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/*
		 * O m�todo onCreate � chamado quando o provider � executado pela
		 * primeira vez, e usado para criar as tabelas no database
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TESTE_TABLE + " (" + Teste.TESTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + Teste.TEXT
					+ " LONGTEXT" + ");");
		}

		/*
		 * O m�todo onUpdate � invocado quando a vers�o do banco de dados muda.
		 * Assim, � usado para fazer adequa��es para a aplica��o funcionar
		 * corretamente.
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Como ainda estamos na primeira vers�o do DB,
			// n�o precisamos nos preocupar com o update agora.
		}
	}

}
