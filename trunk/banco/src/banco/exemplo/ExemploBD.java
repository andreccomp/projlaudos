package banco.exemplo;

import java.io.*;
import java.sql.*;

public class ExemploBD {

	/*
	 *
	 * $Id: Basico.java,v 1.1.1.1 2005/02/04 08:08:27 halleypo Exp $
	 *
	 * Este exemplo testa os componentes b�sicos do driver JDBC, e mostra a
	 * implementa��o de comandos simples.
	 *
	 * Para utilizar este exemplo � necess�rio um banco de dados existente, onde
	 * � criada uma tabela chamada tbl_basica.
	 *
	 * Nota: Somente funciona nos drivers p�s-7.0.
	 */

	Connection db; // Conex�o com o servidor de banco de dados
	Statement st; // Declara��o para executar os comandos

	public void go() throws ClassNotFoundException, FileNotFoundException,
			IOException, SQLException {
		// Banco de dados, usu�rio e senha igual a "teste"
		// CREATE USER teste WITH password 'teste';
		// CREATE DATABASE teste WITH OWNER teste ENCODING 'LATIN1';
		String url = "jdbc:mysql://127.0.0.1/banco?user=root&password=root";
		String usr = "root";
		String pwd = "root";

		// Carregar o driver
		Class.forName("com.mysql.jdbc.Driver");

		// Conectar com o servidor de banco de dados
		System.out.println("Conectando ao banco de dados\nURL = " + url);
		db = DriverManager.getConnection(url, usr, pwd);

		System.out.println("Conectado...Criando a declara��o");
		st = db.createStatement();

		// Limpar o banco de dados (no caso de uma falha anterior) e inicializar
		cleanup();

		// Executar os testes utilizando os m�todos do JDBC
		doexample();

		// Limpar o banco de dados
		cleanup();

		// Fechar a conex�o
		System.out.println("Fechando a conex�o");
		st.close();
		db.close();

		// throw postgresql.Driver.notImplemented();
	}

	/*
	 * Remover a tabela (caso exista). Nenhum erro � mostrado.
	 */
	public void cleanup() {
		try {
			st.executeUpdate("drop table tbl_basica");
		} catch (Exception ex) {
			// Ignorar todos os erros
		}
	}

	/*
	 * Criar a tabela e manipular os dados
	 */
	public void doexample() throws SQLException {
		System.out.println("\nExecutando os testes:");

		// Criar a tabela que armazena os dados
		st.executeUpdate("create table tbl_basica (a int2, b int2)");

		// Inserir alguns dados utilizando Statement
		st.executeUpdate("insert into tbl_basica values (1,1)");
		st.executeUpdate("insert into tbl_basica values (2,1)");
		st.executeUpdate("insert into tbl_basica values (3,1)");

		// Mostrar como obter o OID da linha rec�m inserida
		st.executeUpdate("insert into tbl_basica values (4,1)");
		System.out.println("Linha inserida com o OID ");

		// Mudar o valor da coluna b de 1 para 8
		st.executeUpdate("update tbl_basica set b=8");
		System.out.println("Atualizadas " + st.getUpdateCount() + " linhas");

		// Excluir linhas
		st.executeUpdate("delete from tbl_basica where a<3");
		System.out.println("Exclu�das " + st.getUpdateCount() + " linhas");

		// Havendo muitas inser��es o PreparedStatement � mais eficiente, por
		// causa da pr�-compila��o da declara��o SQL e do armazenamento direto
		// do objeto Java na coluna. O PostgreSQL n�o faz pr�-compila��o, mas
		// permite armazenar em uma coluna o valor do objeto Java (como Date,
		// String, etc).
		//
		// Tamb�m, esta � a �nica maneira de escrever a data de uma maneira
		// independente do estilo da data (DateStyle), usado pelo PostgreSQL
		// para interpretar valores de entrada de data amb�guos.
		PreparedStatement ps = db
				.prepareStatement("insert into tbl_basica values (?,?)");
		for (int i = 2; i < 5; i++) {
			ps.setInt(1, 4); // "coluna a" = 4
			ps.setInt(2, i); // "coluna b" = i
			ps.executeUpdate(); // porque o insert n�o retorna dados
		}
		ps.close(); // Sempre fechar ao terminar

		// Consultar a tabela
		System.out.println("Realizando uma consulta");
		ResultSet rs = st.executeQuery("select a, b from tbl_basica");
		if (rs != null) {
			// Percorrer o conjunto de resultados mostrando os valores.
			// � necess�rio chamar .next() antes de ler qualquer resultado.
			while (rs.next()) {
				int a = rs.getInt("a"); // Nome da coluna
				int b = rs.getInt(2); // N�mero da coluna
				System.out.println("  a=" + a + " b=" + b);
			}
			rs.close(); // � necess�rio fechar o resultado ao terminar
		}

		// Consultar a tabela novamente, mostrando uma forma mais
		// eficiente de obter os resultados quando n�o se sabe o
		// n�mero da coluna do resultado.

		System.out.println("Realizando outra consulta");
		rs = st.executeQuery("select * from tbl_basica where b>1");
		if (rs != null) {
			// Descobrir os n�meros das colunas.
			//
			// � melhor ser feito neste ponto, porque os m�todos chamados
			// passando os nomes das colunas realizam internamente esta
			// chamada toda toda vez que s�o chamados. Esta forma realmente
			// melhora o desempenho em consultas grandes.
			//
			int col_a = rs.findColumn("a");
			int col_b = rs.findColumn("b");

			// Percorrer o conjunto de resultados mostrando os valores.
			// � necess�rio chamar .next() antes de ler qualquer resultado.
			while (rs.next()) {
				int a = rs.getInt(col_a); // N�mero da coluna
				int b = rs.getInt(col_b); // N�mero da coluna
				System.out.println("  a=" + a + " b=" + b);
			}
			rs.close(); // � necess�rio fechar o resultado ao terminar.
		}

		// Testar 'maxrows' definindo-o como 3 linhas

		st.setMaxRows(3);
		System.out.println("Realizando uma consulta limitada a "
				+ st.getMaxRows() + " linhas.");
		rs = st.executeQuery("select a, b from tbl_basica");
		while (rs.next()) {
			int a = rs.getInt("a"); // Obter o valor pelo nome da coluna
			int b = rs.getInt(2); // Obter o valor pelo n�mero da coluna
			System.out.println("  a=" + a + " b=" + b);
		}
		rs.close(); // repetindo, � necess�rio fechar o resultado ao terminar.

		// A �ltima tarefa a ser realizada � remover a tabela,
		// o que � feito pelo m�todo cleanup().
	}

	/*
	 * Testar o driver JDBC
	 */
	public static void main(String args[]) {
		System.out.println("MySql - Teste b�sico v6.3 rev 1\n");

		try {
			ExemploBD objBasico = new ExemploBD();
			objBasico.go();
		} catch (Exception ex) {
			System.err.println("Exce��o capturada.\n" + ex);
			ex.printStackTrace();
		}
	}
}
