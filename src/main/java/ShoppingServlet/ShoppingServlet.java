package ShoppingServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

public class ShoppingServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8705831569797721077L;

	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		// esto indica que si hay sesion me la usas pero sino no me las crees.
		HttpSession session = req.getSession(false);

		// en caso de que no haya valorpara la sesion pero se haya creado
		// enviame error
		if (session == null) {
			res.sendRedirect("http://localhost:8000/error.html");
		}
		// obtengo el vector del carrito de la compra puede estar vacio o no
		Vector buylist = (Vector) session.getValue("shopping.shoppingcart");
		
		//que haccion has hecho  --> ADD  o DELETE o CHECKOUT
		String action = req.getParameter("action");
		
		if (!action.equals("CHECKOUT")) {
			
			if (action.equals("DELETE")) {
				
				String del = req.getParameter("delindex");
				//forma fashion de pasar a int desde String
				int d = (new Integer(del)).intValue();
				//elimina de lista el elemento que toca
				buylist.removeElementAt(d);
				
			} else if (action.equals("ADD")) {
				
				// any previous buys of same cd?
				boolean match = false;
				//obtien el cd elegido
				CD aCD = getCD(req);
				//la lista esta vacia?
				if (buylist == null) {
					// add first cd to the cart
					buylist = new Vector(); // first order
					buylist.addElement(aCD);
					
					Context envContext = null;
					 try{
					//realizamos una consulta la base de datos.
					envContext = new InitialContext();
					Context initContext = (Context) envContext
							.lookup("java:/comp/env");
					DataSource ds3 = (DataSource) initContext.lookup("jdbc/testDB");
					//DataSource ds = (DataSource)envContext.lookup("java:/comp/env/jdbc/testDB");
					Connection con3 = ds3.getConnection();

					Statement stmt3 = con3.createStatement();							
					String query3 = "Update  shopping set quantity=(quantity-"+aCD.getQuantity()+") where Album='"+aCD.getAlbum()+"';";
					 stmt3.executeUpdate(query3);
					 }
					 catch(Exception e)
					 {
						 e.printStackTrace();
					 }
					 
					
				} else { // not first buy
					for (int i = 0; i < buylist.size(); i++) {
						CD cd = (CD) buylist.elementAt(i);
						if (cd.getAlbum().equals(aCD.getAlbum())) {
							
							 Context envContext = null;
							 try{
							//realizamos una consulta la base de datos.
							envContext = new InitialContext();
							Context initContext = (Context) envContext
									.lookup("java:/comp/env");
							DataSource ds3 = (DataSource) initContext.lookup("jdbc/testDB");
							//DataSource ds = (DataSource)envContext.lookup("java:/comp/env/jdbc/testDB");
							Connection con3 = ds3.getConnection();

							Statement stmt3 = con3.createStatement();							
							String query3 = "Update  shopping set quantity=(quantity-"+aCD.getQuantity()+") where Album='"+aCD.getAlbum()+"';";
							 stmt3.executeUpdate(query3);
							 }
							 catch(Exception e)
							 {
								 e.printStackTrace();
							 }
							
							
							
							cd.setQuantity(cd.getQuantity() + aCD.getQuantity());
							buylist.setElementAt(cd, i);
							match = true;
						} // end of if name matches
					} // end of for
					if (!match)
						buylist.addElement(aCD);
				}
			}
			session.putValue("shopping.shoppingcart", buylist);
			String url = "/EShop.jsp";
			ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher(url);
			rd.forward(req, res);
			
		} else if (action.equals("CHECKOUT")) {
			float total = 0;
			for (int i = 0; i < buylist.size(); i++) {
				CD anOrder = (CD) buylist.elementAt(i);
				float price = anOrder.getPrice();
				int qty = anOrder.getQuantity();
				total += (price * qty);
			}
			total += 0.005;
			String amount = new Float(total).toString();
			int n = amount.indexOf('.');
			amount = amount.substring(0, n + 3);
			req.setAttribute("amount", amount);
			String url = "/Checkout.jsp";
			ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher(url);
			rd.forward(req, res);
		}
	}

	private CD getCD(HttpServletRequest req) {
		// imagine if all this was in a scriptlet...ugly, eh?
		String myCd = req.getParameter("CD");
		String qty = req.getParameter("qty");
		StringTokenizer t = new StringTokenizer(myCd, "|");
		String album = t.nextToken();
		String artist = t.nextToken();
		String country = t.nextToken();
		String price = t.nextToken();
		price = price.replace('$', ' ').trim();
		CD cd = new CD();
		cd.setAlbum(album);
		cd.setArtist(artist);
		cd.setCountry(country);
		cd.setPrice((new Float(price)).floatValue());
		cd.setQuantity((new Integer(qty)).intValue());
		return cd;
	}
}