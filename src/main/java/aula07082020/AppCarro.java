	package aula07082020;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class AppCarro {
	
	public static void main(String[] args) throws Exception {
		Connection conexão = null; 
		try {
			conexão = abrirConexão();
			criarTabelaCarros(conexão);
			inserirDoisMilCarros(conexão);			
			alterarPrimeiroCarro(conexão);			
			
			
			long quantidadeDeCarrosAntesDeDeletar = contarCarros(conexão);			
			
			System.out.println(
					"Antes: " + quantidadeDeCarrosAntesDeDeletar);
			
			listarNoConsoleOsCarros(conexão);			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conexão.close();
		}
		System.out.println("Foi.");
	}

	private static void listarNoConsoleOsCarros(Connection conexão) throws Exception {
		Statement recuperarCarros = null;
		try {
			recuperarCarros = conexão.createStatement();
			ResultSet resultado = recuperarCarros.executeQuery("select id, nome from tipos_de_carros");
			System.out.println("Listando Carros...");
			while (resultado.next()) {
				System.out.println("- " + resultado.getString("nome") + ", id=" + resultado.getLong("id"));
			}		
			System.out.println("Listagem concluída.");
		} finally {
			recuperarCarros.close();
		}		
	}
	

	private static long contarCarros(Connection conexão) throws Exception {
		long quantidadeDeCarros = 0;
		Statement contarCarros = null;
		try {
			contarCarros = conexão.createStatement();
			ResultSet resultado = contarCarros.executeQuery("select count(*) as quantidade from tipos_de_carros");
			if (resultado.next()) {
				quantidadeDeCarros = resultado.getLong("quantidade");
			}		
		} finally {
			contarCarros.close();
		}
		return quantidadeDeCarros;
	}

	private static void alterarPrimeiroCarro(Connection conexão) throws Exception {
		alterarNomeDoCarro(conexão, 1L, "Gol");
	}
	
	private static void alterarNomeDoCarro(Connection conexão, Long id, String novoNome) throws Exception {
		PreparedStatement alterarCarros = null;
		try {
			alterarCarros = conexão.prepareStatement("update tipos_de_carros set nome = ? where id = ?");
			alterarCarros.setLong(2, id);
			alterarCarros.setString(1, novoNome);			
			alterarCarros.execute();
		} finally {
			alterarCarros.close();
		}		
	}
	
	private static void inserirDoisMilCarros(Connection conexão) throws Exception {
		PreparedStatement inserirCarros = null;
		try {
			inserirCarros = conexão.prepareStatement("insert into tipos_de_carros (id, nome) values (?,?)");
			for (int contador = 1; contador <= 2000; contador++) {
				inserirCarros.setLong(1, contador);
				inserirCarros.setString(2, "carro " + contador);
				inserirCarros.execute();
			}
		} finally {
			inserirCarros.close();
		}		
	}

	private static void criarTabelaCarros(Connection conexão) throws Exception {
		Statement criarTabela = null;
		try {
			criarTabela = conexão.createStatement();
			criarTabela.execute("create table if not exists tipos_de_carros ("
					+ "id long not null primary key,"
					+ "nome varchar(255) not null unique"
					+ ")");
		} finally {
			criarTabela.close();
		}
	}

	private static Connection abrirConexão() throws Exception {
		Connection c = DriverManager.getConnection("jdbc:h2:~/pets", "sa", "");
		return c;
	}

}
	
