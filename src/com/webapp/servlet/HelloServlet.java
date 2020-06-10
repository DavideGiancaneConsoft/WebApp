package com.webapp.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet che risponde alla richiesta http get "HelloServlet"
 * interrogando un db con la tabella "customer" e stampando a video,
 * tramite JSP, l'unica riga presente
 */
@WebServlet("/HelloServlet")
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String DB_URI = "jdbc:mysql://localhost/test";
	private static final String DB_USER = "root";
	private static final String DB_PSW = "consoft";
	   
   
    public HelloServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//Prelevo il customer dal DB
			Customer customer = readCustomerFromDB();
			
			//Inizializzo la view JSP
			String jspPage = "/ShowCustomer.jsp";
			
			//Passo il customer alla JSP
			request.setAttribute("customer", customer);
			getServletContext().getRequestDispatcher(jspPage).forward(request, response);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	/**
	 * Legge l'unico customer nella tabella "customer" del database "test
	 * @return il customer presente nel ResultSet
	 * @throws SQLException se ci sono errori di connessione o nella query
	 * @throws ClassNotFoundException se non trova la classe del Driver jdbc
	 */
	private Customer readCustomerFromDB() throws SQLException, ClassNotFoundException{
		//Carico il Driver JDBC
		Class.forName("com.mysql.cj.jdbc.Driver"); 
		
		//Apro la connessione al db
		Connection conn = DriverManager.getConnection(DB_URI, DB_USER, DB_PSW);
		
		//Interrogo il DB
		String query = "SELECT * FROM customer WHERE cust_id=1";
		PreparedStatement statement = 
				conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		statement.execute();
		ResultSet resultSet = statement.getResultSet();
		
		//mi posizionono sulla tupla che mi aspetto di ottenere
		resultSet.first();
		
		//Prelevo i dati dalle colonne e avvaloro il bean
		String first_name = resultSet.getString(ColumnNames.firstName.toString());
		String last_name = resultSet.getString(ColumnNames.lastName.toString());
		String phone_number = resultSet.getString(ColumnNames.phoneNumber.toString());
		Customer cust = new Customer(first_name, last_name, phone_number);
		
		//Chiudo la connessione
		conn.close();
		return cust;
			
	}
	
	/**
	 * Enum utility per considerare i nomi delle colonne del db come costanti
	 * @author davide.giancane
	 *
	 */
	private enum ColumnNames{
		firstName("first_name"), lastName("last_name"), phoneNumber("phone");
		private String columnName;
		
		private ColumnNames(String name) {
			this.columnName = name;
		}
		
		@Override
		public String toString() {
			return columnName;
		}	
	}
}


