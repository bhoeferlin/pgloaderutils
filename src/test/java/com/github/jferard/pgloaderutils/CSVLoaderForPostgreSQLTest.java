package com.github.jferard.pgloaderutils;

import com.google.common.io.Resources;
import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CSVLoaderForPostgreSQLTest {
	public final void test() throws IOException, InterruptedException {
		try {
			Class.forName("org.postgresql.Driver");
			try {
				Connection connection = DriverManager.getConnection(
						"jdbc:postgresql://127.0.0.1:5432/testdb", "postgres",
						"postgres");
				try {
					Statement statement = connection.createStatement();
					statement.executeUpdate(
							"DROP TABLE IF EXISTS testtable");
					statement.executeUpdate(
							"CREATE TABLE testtable ("
									+ "col1 text," + "col2 decimal,"
									+ "col3 text)");

					CSVLoaderForPostgreSQL loader = CSVLoaderForPostgreSQL
							.toTable("testtable");
					final StringReader stringReader = new StringReader("\"a\", 1.0, \"b,c\"\n"
							+ "\"d\", 2.0, \"f,g\"\n");
					final CSVSimpleFileReader csvReader = new CSVSimpleFileReader(
							stringReader);
					loader.populate(connection,
							csvReader);
				} finally {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public final void testResource() throws IOException, InterruptedException {
		try {
			Class.forName("org.postgresql.Driver");
			try {
				Connection connection = DriverManager.getConnection(
						"jdbc:postgresql://127.0.0.1:5432/sirene", "postgres",
						"postgres");
				try {
					Statement statement = connection.createStatement();
					statement.executeUpdate(
							"DROP TABLE IF EXISTS sirc");
					statement.executeUpdate(
							"CREATE TABLE sirc (\n" +
									"	siren text,\n" +
									"	nic text,\n" +
									"	l1_normalisee text,\n" +
									"	l2_normalisee text,\n" +
									"	l3_normalisee text,\n" +
									"	l4_normalisee text,\n" +
									"	l5_normalisee text,\n" +
									"	l6_normalisee text,\n" +
									"	l7_normalisee text,\n" +
									"	l1_declaree text,\n" +
									"	l2_declaree text,\n" +
									"	l3_declaree text,\n" +
									"	l4_declaree text,\n" +
									"	l5_declaree text,\n" +
									"	l6_declaree text,\n" +
									"	l7_declaree text,\n" +
									"	numvoie text,\n" +
									"	indrep text,\n" +
									"	typvoie text,\n" +
									"	libvoie text,\n" +
									"	codpos text,\n" +
									"	cedex text,\n" +
									"	rpet text,\n" +
									"	libreg text,\n" +
									"	depet text,\n" +
									"	arronet text,\n" +
									"	ctonet text,\n" +
									"	comet text,\n" +
									"	libcom text,\n" +
									"	du text,\n" +
									"	tu text,\n" +
									"	uu text,\n" +
									"	epci text,\n" +
									"	tcd text,\n" +
									"	zemet text,\n" +
									"	siege text,\n" +
									"	enseigne text,\n" +
									"	ind_publipo text,\n" +
									"	diffcom text,\n" +
									"	amintret text,\n" +
									"	natetab text,\n" +
									"	libnatetab text,\n" +
									"	apet700 text,\n" +
									"	libapet text,\n" +
									"	dapet text,\n" +
									"	tefet text,\n" +
									"	libtefet text,\n" +
									"	efetcent text,\n" +
									"	defet text,\n" +
									"	origine text,\n" +
									"	dcret text,\n" +
									"	date_deb_etat_adm_et text,\n" +
									"	activnat text,\n" +
									"	lieuact text,\n" +
									"	actisurf text,\n" +
									"	saisonat text,\n" +
									"	modet text,\n" +
									"	prodet text,\n" +
									"	prodpart text,\n" +
									"	auxilt text,\n" +
									"	nomen_long text,\n" +
									"	sigle text,\n" +
									"	nom text,\n" +
									"	prenom text,\n" +
									"	civilite text,\n" +
									"	rna text,\n" +
									"	nicsiege text,\n" +
									"	rpen text,\n" +
									"	depcomen text,\n" +
									"	adr_mail text,\n" +
									"	nj text,\n" +
									"	libnj text,\n" +
									"	apen700 text,\n" +
									"	libapen text,\n" +
									"	dapen text,\n" +
									"	aprm text,\n" +
									"	essen text,\n" +
									"	dateess text,\n" +
									"	tefen text,\n" +
									"	libtefen text,\n" +
									"	efencent text,\n" +
									"	defen text,\n" +
									"	categorie text,\n" +
									"	dcren text,\n" +
									"	amintren text,\n" +
									"	monoact text,\n" +
									"	moden text,\n" +
									"	proden text,\n" +
									"	esaann text,\n" +
									"	tca text,\n" +
									"	esaapen text,\n" +
									"	esasec1n text,\n" +
									"	esasec2n text,\n" +
									"	esasec3n text,\n" +
									"	esasec4n text,\n" +
									"	vmaj text,\n" +
									"	vmaj1 text,\n" +
									"	vmaj2 text,\n" +
									"	vmaj3 text,\n" +
									"	datemaj text\n" +
									")");

					CSVLoaderForPostgreSQL loader = CSVLoaderForPostgreSQL
							.toTable("sirc", ';', '"');
					final Reader reader = new InputStreamReader(Resources.getResource
							("sirc-17804_9075_14209_201612_L_M_20170104_171522721-part.csv").openStream(),
							"ISO-8859-1");
					final CSVSimpleFileReader csvReader = new CSVSimpleFileReader(
							reader);
					loader.populate(connection,
							csvReader);
				} finally {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public final void testHugeFile() throws IOException, InterruptedException {
		try {
			Class.forName("org.postgresql.Driver");
			try {
				Connection connection = DriverManager.getConnection(
						"jdbc:postgresql://127.0.0.1:5432/sirene", "postgres",
						"postgres");
				try {
					Statement statement = connection.createStatement();
					statement.executeUpdate(
							"DROP TABLE IF EXISTS sirc");
					statement.executeUpdate(
							"CREATE TABLE sirc (\n" +
									"	siren text,\n" +
									"	nic text,\n" +
									"	l1_normalisee text,\n" +
									"	l2_normalisee text,\n" +
									"	l3_normalisee text,\n" +
									"	l4_normalisee text,\n" +
									"	l5_normalisee text,\n" +
									"	l6_normalisee text,\n" +
									"	l7_normalisee text,\n" +
									"	l1_declaree text,\n" +
									"	l2_declaree text,\n" +
									"	l3_declaree text,\n" +
									"	l4_declaree text,\n" +
									"	l5_declaree text,\n" +
									"	l6_declaree text,\n" +
									"	l7_declaree text,\n" +
									"	numvoie text,\n" +
									"	indrep text,\n" +
									"	typvoie text,\n" +
									"	libvoie text,\n" +
									"	codpos text,\n" +
									"	cedex text,\n" +
									"	rpet text,\n" +
									"	libreg text,\n" +
									"	depet text,\n" +
									"	arronet text,\n" +
									"	ctonet text,\n" +
									"	comet text,\n" +
									"	libcom text,\n" +
									"	du text,\n" +
									"	tu text,\n" +
									"	uu text,\n" +
									"	epci text,\n" +
									"	tcd text,\n" +
									"	zemet text,\n" +
									"	siege text,\n" +
									"	enseigne text,\n" +
									"	ind_publipo text,\n" +
									"	diffcom text,\n" +
									"	amintret text,\n" +
									"	natetab text,\n" +
									"	libnatetab text,\n" +
									"	apet700 text,\n" +
									"	libapet text,\n" +
									"	dapet text,\n" +
									"	tefet text,\n" +
									"	libtefet text,\n" +
									"	efetcent text,\n" +
									"	defet text,\n" +
									"	origine text,\n" +
									"	dcret text,\n" +
									"	date_deb_etat_adm_et text,\n" +
									"	activnat text,\n" +
									"	lieuact text,\n" +
									"	actisurf text,\n" +
									"	saisonat text,\n" +
									"	modet text,\n" +
									"	prodet text,\n" +
									"	prodpart text,\n" +
									"	auxilt text,\n" +
									"	nomen_long text,\n" +
									"	sigle text,\n" +
									"	nom text,\n" +
									"	prenom text,\n" +
									"	civilite text,\n" +
									"	rna text,\n" +
									"	nicsiege text,\n" +
									"	rpen text,\n" +
									"	depcomen text,\n" +
									"	adr_mail text,\n" +
									"	nj text,\n" +
									"	libnj text,\n" +
									"	apen700 text,\n" +
									"	libapen text,\n" +
									"	dapen text,\n" +
									"	aprm text,\n" +
									"	essen text,\n" +
									"	dateess text,\n" +
									"	tefen text,\n" +
									"	libtefen text,\n" +
									"	efencent text,\n" +
									"	defen text,\n" +
									"	categorie text,\n" +
									"	dcren text,\n" +
									"	amintren text,\n" +
									"	monoact text,\n" +
									"	moden text,\n" +
									"	proden text,\n" +
									"	esaann text,\n" +
									"	tca text,\n" +
									"	esaapen text,\n" +
									"	esasec1n text,\n" +
									"	esasec2n text,\n" +
									"	esasec3n text,\n" +
									"	esasec4n text,\n" +
									"	vmaj text,\n" +
									"	vmaj1 text,\n" +
									"	vmaj2 text,\n" +
									"	vmaj3 text,\n" +
									"	datemaj text\n" +
									")");

					CSVLoaderForPostgreSQL loader = CSVLoaderForPostgreSQL
							.toTable("sirc");
					final Reader reader = new InputStreamReader(new FileInputStream(
							"C:\\Users\\Julien\\Downloads\\sirc-17804_9075_14209_201612_L_M_20170104_171522721.csv"),
							"ISO-8859-1");
					final OpenableReader csvReader = CSVCleanerFileReader.fromReader(reader, CSVFormat
							.RFC4180.withDelimiter(';'),
							new CSVRecordCleanerExample());
					loader.populate(connection,
							csvReader);
				} finally {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}