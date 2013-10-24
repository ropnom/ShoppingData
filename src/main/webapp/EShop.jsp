<%@ page session="true"
	import="java.util.*, java.io.IOException, 
java.io.PrintWriter, java.sql.Connection, java.sql.ResultSet, java.sql.SQLException,
java.sql.Statement, javax.naming.Context, javax.naming.InitialContext, javax.naming.NamingException,
javax.servlet.ServletException, javax.servlet.http.HttpServlet, javax.servlet.http.HttpServletRequest,
javax.servlet.http.HttpServletResponse, javax.sql.DataSource "%>
<html>
<head>
<title>Music Without Borders</title>
</head>
<body bgcolor="#33CCFF">
	<font face="Times New Roman,Times" size="+3"> Music Without
		Borders </font>
	<hr>
	<p>
	<center>
		<form name="shoppingForm" action="/ShoppingData/ShoppingServlet"
			method="POST">
			<b>CD:</b> <select name=CD>

				<%
					Context envContext = null;

					try {
						envContext = new InitialContext();
						Context initContext = (Context) envContext
								.lookup("java:/comp/env");
						DataSource ds2 = (DataSource) initContext.lookup("jdbc/testDB");
						//DataSource ds = (DataSource)envContext.lookup("java:/comp/env/jdbc/testDB");
						Connection con2 = ds2.getConnection();

						Statement stmt2 = con2.createStatement();
						String query2 = "select * from shopping";
						ResultSet rs2 = stmt2.executeQuery(query2);

						PrintWriter out2 = response.getWriter();	
						
						%>
						
						<option>Yuan | The Guo Brothers | China | 4.95</option>
						
						<%

						String linea = "";
						while (rs2.next()) {
							linea = rs2.getString("Album") + " | "
									+ rs2.getString("Artista") + " | "
									+ rs2.getString("Country") + " | "
									+ rs2.getInt("Price") + " | "
									+ rs2.getFloat("quantity");
							
							%>							
							<option><%= linea %></option>							
							<%
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				%>
				


			</select> <b>Quantity: </b><input type="text" name="qty" SIZE="3" value=1>
			<input type="hidden" name="action" value="ADD"> <input
				type="submit" name="Submit" value="Add to Cart">
		</form>
	</center>
	<p>
		<jsp:include page="Cart.jsp" flush="true" />
</body>
</html>