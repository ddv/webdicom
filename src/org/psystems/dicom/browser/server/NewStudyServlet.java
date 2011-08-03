package org.psystems.dicom.browser.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.Session;

public class NewStudyServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(NewStudyServlet.class
			.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		

		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		System.out.println("isMultipart=" + isMultipart);
		HttpSession session = req.getSession();

		
		String imgTmpDir = getServletContext().getInitParameter("webdicom.dir.newdcm.uploadimages");
		
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory(100000, new File(imgTmpDir));

		// Set factory constraints
		// factory.setSizeThreshold(yourMaxMemorySize);
		// factory.setRepository(yourTempDirectory);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// TODO Вынести в конфиг!!
		long yourMaxRequestSize = 10000000;
		// Set overall request size constraint
		upload.setSizeMax(yourMaxRequestSize);

		// Parse the request
		try {
			List /* FileItem */items = upload.parseRequest(req);
			Properties props = new Properties();
			InputStream stream = null;

			// Process the uploaded items
			Iterator iter = items.iterator();

			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();

				if (item.isFormField()) {
					processFormField(item, props);
				} else {
					stream = processUploadedFile(item);
				}
			}


			org.psystems.dicom.commons.CommonUtil.makeSendDicomFile(
					getServletContext(), props, stream);

			// *********** Работа с сессией ***************
			// Сохраняем настройку  состояния
			// пользовательских контролов

			Session sessionObject = (Session) session
					.getAttribute(org.psystems.dicom.browser.server.Util.sessionAttrName);
			if (sessionObject == null) {
				sessionObject = new Session();
			}

			for (Iterator<Object> iterProps = props.keySet().iterator(); iterProps
					.hasNext();) {
				String key = (String) iterProps.next();
				String value = props.getProperty(key);

				// ManufacturerModelName
				if (key.equals("00081090")) {
					sessionObject
							.setStudyManagePanel_ManufacturerModelName(value);
				}

				// Modality
				if (key.equals("00080060")) {
					sessionObject.setStudyManagePanel_Modality(value);
				}

				session.setAttribute(
						org.psystems.dicom.browser.server.Util.sessionAttrName,
						sessionObject);

			}
			
			resp.setStatus(200);
			resp.getWriter().write("___success___");

			
			
		} catch (FileUploadException e) {
			
			//TODO Сделать через LOG4J
			e.printStackTrace();
			resp.setStatus(400);//200 - ОК
			DefaultGWTRPCException ex = Util.throwPortalException("Ошибка загрузки вложения", e);
			resp.getWriter().write("TRACECODE ["+ex.getLogMarker()+"] \n "+ex.getMessage()+" \n "+ex.getStack());
		}  catch (Exception e) {
			//TODO Сделать через LOG4J
			e.printStackTrace();
			resp.setStatus(400);
			DefaultGWTRPCException ex = Util.throwPortalException("Ошибка сохранения исследования", e);
			resp.getWriter().write("TRACECODE ["+ex.getLogMarker()+"] \n "+ex.getMessage()+" \n "+ex.getStack());
		}
		

	}

	private InputStream processUploadedFile(FileItem item) throws IOException {
		// TODO Auto-generated method stub

		String fieldName = item.getFieldName();
		String fileName = item.getName();
		String contentType = item.getContentType();
		boolean isInMemory = item.isInMemory();
		long sizeInBytes = item.getSize();
		

		System.out.println("!!! UploadFile: " + fieldName + ";" + fileName
				+ ";" + contentType + ";" + isInMemory + ";" + sizeInBytes);

		return item.getInputStream();
	}

	private void processFormField(FileItem item, Properties props)
			throws UnsupportedEncodingException {
		String name = item.getFieldName();
		// TODO Зделать _авто_ определение кодировки !!!
		String value = item.getString("UTF-8");

		System.out.println("!!! FormFiled: " + name + "=" + value);

		//Исключаем не IDCOM поля
		if(name.equalsIgnoreCase("ID")) return;
		
		props.put(name, value);
	}


}
