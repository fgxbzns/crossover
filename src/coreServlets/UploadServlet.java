package coreServlets;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadServlet extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {
	static final long serialVersionUID = 1L;

	private static final String DATA_DIRECTORY = "";
	private static final int MAX_MEMORY_SIZE = 1024 * 1024 * 20;
	private static final int MAX_REQUEST_SIZE = 1024 * 1024;
	static String filePath = "";

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Check that we have a file upload request
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		PrintWriter out = response.getWriter();
		String fileName= "";
		String userFile= "";
		
		if (!isMultipart) {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet upload</title>");
			out.println("</head>");
			out.println("<body>");			
			out.println("<p>NThis is not a upload request</p>");
			out.println("</body>");
			out.println("</html>");		
			return;
		} 

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Sets the size threshold beyond which files are written directly to
		// disk.
		factory.setSizeThreshold(MAX_MEMORY_SIZE);

		// Sets the directory used to temporarily store files that are larger
		// than the configured size threshold. We use temporary directory for
		// java
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		// constructs the folder where uploaded file will be stored
		String uploadFolder = getServletContext().getRealPath("")
				+ File.separator + DATA_DIRECTORY + "/models/";
		
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Set overall request size constraint
		upload.setSizeMax(MAX_REQUEST_SIZE);

		try {
			// Parse the request
			List items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			if (!iter.hasNext()) {
					out.println(iter.hasNext());
				out.println("<h1>No fields found, please try again</h1>");
				return;
			}
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();

				if (!item.isFormField()) {
					fileName = new File(item.getName()).getName();
					if(fileName.equals("")){
						out.println("<h1>No file found, please try again</h1>");
						return;
					} 
					filePath = uploadFolder + fileName;

					File uploadedFile = new File(filePath);

					// saves the file to upload directory
					item.write(uploadedFile);
				}
			}

	
			
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet upload</title>");
			out.println("</head>");
			out.println("<body>");
		
			out.println("</body>");
			out.println("</html>");
			
			//request.setAttribute(userFile, fileName);
			
			HttpSession session = request.getSession(false);
			
//			if(session!=null) {
//				session.invalidate();
//				}
			// Create a new session for the user.
			session = request.getSession(true); 

			//HttpSession session = request.getSession(false);
			session.setAttribute("userFile", fileName.substring(0, fileName.indexOf('.')));
				
			RequestDispatcher dispatcher = request.getRequestDispatcher("../webSimulator.jsp");
			
//			RequestDispatcher dispatcher = request.getRequestDispatcher("http://www.gsu.edu");
			dispatcher.forward(request, response);
//			getServletContext().getRequestDispatcher("/webSimulator.jsp").forward(
//					request, response);

		} catch (FileUploadException ex) {
			throw new ServletException(ex);
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	
	}
}