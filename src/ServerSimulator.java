import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.Document;

public class ServerSimulator extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {
	static final long serialVersionUID = 1L;

	private static final String DATA_DIRECTORY = "";
	private static final int MAX_MEMORY_SIZE = 1024 * 1024 * 2;
	private static final int MAX_REQUEST_SIZE = 1024 * 1024;
	static String filePath = "";

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Check that we have a file upload request
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		PrintWriter out = response.getWriter();
		out.println("Hello World");

		if (!isMultipart) {
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet upload</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<p>No file uploaded</p>");
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
				+ File.separator + DATA_DIRECTORY;
		
//		String path = request.getContextPath();
//		String basePath = request.getScheme() + "://"
//				+ request.getServerName() + ":" + request.getServerPort()
//				+ path + "/";
//		String servletPath = basePath + "servlet/coreservlets.";
		

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Set overall request size constraint
		upload.setSizeMax(MAX_REQUEST_SIZE);

		try {
			// Parse the request
			List items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();

				if (!item.isFormField()) {
					String fileName = new File(item.getName()).getName();
//					String filePath = uploadFolder + File.separator + fileName;
//					filePath = uploadFolder + File.separator + fileName;
					filePath = uploadFolder + fileName;

					File uploadedFile = new File(filePath);

					// saves the file to upload directory
					item.write(uploadedFile);
				}
			}

			// displays done.jsp page after upload finished
//			getServletContext().getRequestDispatcher("/done.jsp").forward(
//					request, response);
			
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet upload</title>");
			out.println("</head>");
			out.println("<body>");
			
//			out.println("<p> uploadFolder  is  "+uploadFolder+"</p>");
			out.println(" file path is  "+filePath+"");
//			out.println("<p> path is  "+path+"</p>");
//			out.println("<p>basePath is  "+basePath+"</p>");
//			out.println("<p>servletPath is  "+servletPath+"</p>");
			
			
			Document doc = null;
			doc = Function.getDocument(filePath);
			
			ArrayList<Partition> partitionList = new ArrayList<Partition>();
			ArrayList<Reactant> reactantList = new ArrayList<Reactant>();
			ArrayList<Reaction> reactionList = new ArrayList<Reaction>();

			Function.getGlobalSystem(doc);
			
			// get partition with its reactants and reactions
			Function.getPartitionList(doc);

			GlobalSystem system = GlobalSystem.getInstance();
			
			partitionList = system.getPartitions();
			
			reactantList = partitionList.get(0).getReactantList();

			try {
				reactionList = partitionList.get(0).getReactionList();
				out.println("<p>there are " + reactionList.size()
						+ " reactions </p>");
			} catch (Exception e) {
				e.printStackTrace();
			}
			out.println(Function.printReactantListWeb(partitionList));
			out.println("</body>");
			out.println("</html>");

		} catch (FileUploadException ex) {
			throw new ServletException(ex);
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
}