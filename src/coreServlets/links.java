//CSC8711 
//Assignment 2: Multi-Dimensional Database Search Engine
//Author: Guoxing Fu 
//Feb. 17 2011

package coreservlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class links extends HttpServlet {
	String address;

	String propertyName = "";
	String propertyQuestion = "";
	int displayorder = 0;
	String propertyDisplayType = "checkbox";

	String allowedValue = "";
	String allowedValueCode = "";
	String bookMarkMessage = "";
	String domainName = "";
	String userID = "";

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String ID = request.getParameter("id");

		// get existing session
		HttpSession session = request.getSession(false);

		int counter = 0;
		// default user is 'raj'. this will be changed if the user login with a
		// different name.
		userID = "raj";

		address = "/login.jsp";
		
		synchronized (session) {
			if (session.getAttribute("domainName") != null) {
				domainName = session.getAttribute("domainName").toString();
			}
		}

		ArrayList<propertyBean> list = dataManage.getPropertyList(domainName);
		int propertyNum = list.size();

		ArrayList<String> selection = new ArrayList<String>();
		// dataManage.selectionInitialize(selection, propertyNum);

		// synchronize session
		synchronized (session) {
			if (session.getAttribute("counter") != null) {
				counter = Integer.parseInt(session.getAttribute("counter")
						.toString());
			}
			if (session.getAttribute("selection") != null) {
				selection = (ArrayList<String>) session
						.getAttribute("selection");
			}
			if (session.getAttribute("userID") != null) {
				userID = session.getAttribute("userID").toString();
			}
		}	
		
		if(selection.size()!=0) {
		// get the values of selected properties
		for (int i = counter; i < counter + 3; i++) {
			propertyName = list.get(i).getPropertyName();
			propertyDisplayType = list.get(i).getPropertyDisplayType();

			if (propertyDisplayType.equals("radio")) {
				String value = request.getParameter(propertyName);
				if (value == null) {
					value = "0";
				}
				selection.set(i, value);
			}

			if (propertyDisplayType.equals("checkbox")) {
				String arrayValue = "";
				String[] value = request.getParameterValues(propertyName);
				if (value == null) {
					arrayValue = "0";
				} else {
					for (int j = 0; j < value.length; j++) {
						arrayValue = arrayValue + value[j] + ", ";
					}
					arrayValue = arrayValue.substring(0,
							arrayValue.length() - 2);
				}
				selection.set(i, arrayValue);
			}

			if (propertyDisplayType.equals("singleselect")) {
				String value = request.getParameter(propertyName);
				selection.set(i, value);
			}

			if (propertyDisplayType.equals("multiselect")) {
				String arrayValue = "";
				String[] value = request.getParameterValues(propertyName);
				if (value == null) {
					arrayValue = "0";
				} else {
					for (int j = 0; j < value.length; j++) {
						arrayValue = arrayValue + value[j] + ", ";
					}
					arrayValue = arrayValue.substring(0,
							arrayValue.length() - 2);
				}
				selection.set(i, arrayValue);
			}
		}
		}
		

		if (ID.equals("previous")) {
			counter = counter - 3;
			if (counter < 0) {
				counter = 0;
			}
			address = "/links.jsp";
		} else if (ID.equals("next")) {
			counter = counter + 3;
			if ((counter + 3) >= propertyNum) {
				counter = propertyNum - 3;
			}
			address = "/links.jsp";
		}

		session.setAttribute("counter", counter);
		session.setAttribute("selection", selection);

		// get the number of selected properties
		int selectedProperty = 0;
		if(selection.size()!=0) {
		for (int i = 0; i < propertyNum; i++) {
			if (!selection.get(i).equals("0"))
				selectedProperty++;
			}
		}

		if (ID.equals("bookmark")) {
			if (selectedProperty == 0) {
				bookMarkMessage = "No selection made";
			} else {
				String factTableName = dataManage.getFactTableName(domainName);

				String bookmark = "select * from " + factTableName + " where ";
				for (int i = 0; i < propertyNum; i++) {
					propertyName = list.get(i).getPropertyName();

					if (!selection.get(i).equals("0")) {
						bookmark = bookmark + propertyName + " in ("
								+ selection.get(i) + ")";
						bookmark = bookmark + " and ";
					}
				}
				// System.out.println(bookmark);
				bookmark = bookmark.substring(0, bookmark.length() - 4).trim();

				String sql = "insert into userBookmarks (userid, domainName, bookmark) values ('"
						+ userID
						+ "', '"
						+ domainName
						+ "', '"
						+ bookmark
						+ "')";
				// System.out.println(sql);
				Connection conn = dataManage.makeConnection();
				Statement statement;
				try {
					statement = conn.prepareStatement(sql);
					statement.executeUpdate(sql);
				} catch (SQLException ex) {
					ex.printStackTrace();
				} finally {
					dataManage.closeConnection(conn);
				}
				bookMarkMessage = "Bookmark saved";

			}
			session.setAttribute("bookMarkMessage", bookMarkMessage);
			address = "/links.jsp";
		}

		if (ID.equals("result")) {
			if (selectedProperty != 0) {
				address = "/result.jsp";
			} else {
				bookMarkMessage = "Please make selection first";
				session.setAttribute("bookMarkMessage", bookMarkMessage);
				address = "/links.jsp";
			}
		}

		if (ID.equals("gotobookmark")) {
			String searchsql = request.getParameter("searchsql");
			request.setAttribute("searchsql", searchsql);
			// System.out.println(searchsql);
			address = "/result.jsp";
		}

		if (ID.equals("remove")) {
			String searchsql = request.getParameter("searchsql");

			String sql = "delete from userBookmarks where domainName = '"
					+ domainName + "' and userid = '" + userID
					+ "' and bookmark='" + searchsql + "'";
			// System.out.println(sql);
			System.out.println(searchsql.length());
			Connection conn = dataManage.makeConnection();
			Statement statement;
			try {
				statement = conn.prepareStatement(sql);
				statement.executeUpdate(sql);
			} catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
				dataManage.closeConnection(conn);
			}

			bookMarkMessage = "Bookmark removed";
			session.setAttribute("bookMarkMessage", bookMarkMessage);
			address = "/links.jsp";
		}

		if (ID.equals("login")) {
			address = "/login.jsp";
		}

		if (ID.equals("logout")) {
			session.invalidate();
			address = "/login.jsp";
		}

		if (ID.equals("clear")) {
			address = "/addData.jsp";
		}

		if (ID.equals("save")) {
			address = "/login.jsp";
		}

		if (ID.equals("StartOver")) {
			counter = 0;
			selection.clear();
			session.removeAttribute("counter");
			session.removeAttribute("selection");
			address = "/links.jsp";
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher(address);
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
