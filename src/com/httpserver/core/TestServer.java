package com.httpserver.core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;

import java.net.URI;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.json.simple.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.sql.*;

public class TestServer {
	
	final static TestServer view = new TestServer();

	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private static final String DB_URL = "jdbc:mysql://localhost/IranianCharityDB";

	private static final String USER = "root";
	private static final String PASS = "root";

	private static final String HOSTNAME = "0.0.0.0";
	private static final int PORT = 8002;
	private static final int BACKLOG = 1;

	private static final String HEADER_ALLOW = "Allow";
	private static final String HEADER_CONTENT_TYPE = "Content-Type";

	private static final Charset CHARSET = StandardCharsets.UTF_8;

	private static final int STATUS_OK = 200;
	private static final int STATUS_METHOD_NOT_ALLOWED = 405;

	private static final int NO_RESPONSE_LENGTH = -1;

	private static final String METHOD_GET = "GET";
	private static final String METHOD_OPTIONS = "OPTIONS";
	private static final String ALLOWED_METHODS = METHOD_GET + "," + METHOD_OPTIONS;

	static HttpServer server;
	static HttpsServer ser;

	static ArrayList<String> keyArrays = new ArrayList<String>();
	
	public static void main(String[] args) throws Exception {

		// Do Creating Server
		server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);

		HttpContext hc1 = server.createContext("/info", new InfoHandler());
		HttpContext hc2 = server.createContext("/kill", new KillHandler());

		HttpContext hc3 = server.createContext("/invitationsTable", new ReqInvitesHandler());
		HttpContext hc4 = server.createContext("/sendersTable", new ReqSendersHandler());

		hc1.setAuthenticator(new BasicAuthenticator("get") {
			@Override
			public boolean checkCredentials(String user, String pwd) {
				return user.equals("1viS0fTInfo") && pwd.equals("1viS0fTInfo");
			}
		});

		hc2.setAuthenticator(new BasicAuthenticator("get") {
			@Override
			public boolean checkCredentials(String user, String pwd) {
				return user.equals("1viS0fTKill") && pwd.equals("1viS0fTKill");
			}
		});

		hc3.setAuthenticator(new BasicAuthenticator("get") {
			@Override
			public boolean checkCredentials(String user, String pwd) {
				return user.equals("1viS0fTWJINV") && pwd.equals("1viS0fTWJINV");
			}
		});

		hc4.setAuthenticator(new BasicAuthenticator("get") {
			@Override
			public boolean checkCredentials(String user, String pwd) {
				return user.equals("1viS0fTWJSND") && pwd.equals("1viS0fTWJSND");
			}
		});

		server.setExecutor(null);
		server.start();
		System.out.println("Server is listening on port "+PORT);
	}

	static class InfoHandler implements HttpHandler {
		public void handle(HttpExchange httpExchange) throws IOException {
			String response = "Call me WooJ Hurricanc@gmail.com | 0912 151 3962";
			TestServer.writeResponse(httpExchange, response.toString());
		}
	}

	static class KillHandler implements HttpHandler {

		public void handle(HttpExchange exchange) throws IOException {
			String response = "Server Process Killing in 1 Seconds!";
			exchange.sendResponseHeaders(200, response.length());
			exchange.getResponseBody().write(response.getBytes());
			exchange.close();
			server.stop(1);
		}
	}

	static class ReqInvitesHandler implements HttpHandler {
		@SuppressWarnings("unchecked")
		@Override
		public void handle(HttpExchange he) throws IOException {

			try {
				final Headers headers = he.getResponseHeaders();
				final String requestMethod = he.getRequestMethod().toUpperCase();
				switch (requestMethod) {
					case METHOD_GET:
					final Map<String, List<String>> requestParameters = getRequestParameters(he.getRequestURI());
					// do something with the request parameters
					String whereStr = TestServer.produceWhereStatement(requestParameters);

					String up = "<!DOCTYPE html><html><head><style type=\"text/css\">table, th, td {border: 1px solid black;border-collapse: collapse;}th, td {padding: 5px;}</style></head><body>";
					String midHead = "<table style=\"width:100%\"><tr><th>UserIdentifier</th><th>Firstname</th><th>Lastname</th><th>City</th><th>PhoneNumber</th><th>CourseDegree</th><th>Tablighat</th><th>Jam'avari</th><th>BasteBandi</th><th>Tozi'e</th><th>RavabetOmumi</th><th>Ejrayi:Omrani</th><th>Ejrayi:Darmani</th><th>Ejrayi:Ravanshenasi</th><th>Ejrayi:Poshtibani</th><th>Ejrayi:Hamahangi</th><th>Ejrayi:Amoozeshi</th><th>Ejrayi:Sayer</th><th>ModiriyatMali</th><th>Resane</th><th>IT</th><th>Modiriyat</th><th>ModatZaman</th></tr>";
					String midBody = TestServer.LoadDBDriver(true, whereStr);
					String dateTime = "</table><p id=\"demo\"></p><script>document.getElementById(\"demo\").innerHTML = Date();</script>";
					String down = "</body></html>";

					if (midBody != null && !midBody.isEmpty())
					{
						final String responseBody = up+midHead+midBody+dateTime+down;
						headers.set(HEADER_CONTENT_TYPE, String.format("text/html; charset=%s", CHARSET));
						final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
						he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
						he.getResponseBody().write(rawResponseBody);
					}
					else
					{
						String response = "An Error Occured, Checking ...!";
						TestServer.writeResponse(he, response.toString());
					}

					break;
					case METHOD_OPTIONS:
					headers.set(HEADER_ALLOW, ALLOWED_METHODS);
					he.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
					break;
					default:
					headers.set(HEADER_ALLOW, ALLOWED_METHODS);
					he.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
					break;
				}
			} finally {
				he.close();
			}
		}
	}

	static class ReqSendersHandler implements HttpHandler {
		@SuppressWarnings("unchecked")
		@Override
		public void handle(HttpExchange he) throws IOException {

			try {
				final Headers headers = he.getResponseHeaders();
				final String requestMethod = he.getRequestMethod().toUpperCase();
				switch (requestMethod) {
					case METHOD_GET:
					final Map<String, List<String>> requestParameters = getRequestParameters(he.getRequestURI());
					// do something with the request parameters
					System.out.println("REQ PARAMETERS: "+requestParameters.toString());
					String whereStr = TestServer.produceWhereStatement(requestParameters);
					
					String up = "<!DOCTYPE html><html><head><style>table, th, td {border: 1px solid black;border-collapse: collapse;}th, td {padding: 5px;}</style></head><body>";
					String midHead = "<table style=\"width:100%\"><tr><th>UserIdentifier</th><th>Firstname</th><th>Lastname</th><th>City</th><th>PhoneNumber</th><th>FullAddress</th><th>Tozihat</th></tr>";
					String midBody = TestServer.LoadDBDriver(false, whereStr);
					String dateTime = "</table><p id=\"demo\"></p><script>document.getElementById(\"demo\").innerHTML = Date();</script>";
					String down = "</body></html>";

					if (midBody != null && !midBody.isEmpty())
					{
						final String responseBody = up+midHead+midBody+dateTime+down;
						headers.set(HEADER_CONTENT_TYPE, String.format("text/html; charset=%s", CHARSET));
						final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
						he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
						he.getResponseBody().write(rawResponseBody);
					}
					else
					{
						String response = "An Error Occured, Checking ...!";
						TestServer.writeResponse(he, response.toString());
					}

					break;
					case METHOD_OPTIONS:
					headers.set(HEADER_ALLOW, ALLOWED_METHODS);
					he.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
					break;
					default:
					headers.set(HEADER_ALLOW, ALLOWED_METHODS);
					he.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
					break;
				}
			} finally {
				he.close();
			}
		}
	}

	public static void writeResponse(HttpExchange httpExchange, String response) throws IOException {
		System.out.println(response);
		httpExchange.sendResponseHeaders(200, response.length());
		OutputStream os = httpExchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	private static String decodeUrlComponent(final String urlComponent) {
		try {
			return URLDecoder.decode(urlComponent, CHARSET.name());
		} catch (final UnsupportedEncodingException ex) {
			throw new InternalError();
		}
	}

	private static Map<String, List<String>> getRequestParameters(final URI requestUri) {
		final Map<String, List<String>> requestParameters = new LinkedHashMap<>();
		final String requestQuery = requestUri.getRawQuery();
		if (requestQuery != null) {
			final String[] rawRequestParameters = requestQuery.split("[&;]", -1);
			for (final String rawRequestParameter : rawRequestParameters) {
				final String[] requestParameter = rawRequestParameter.split("=", 2);
				final String requestParameterName = decodeUrlComponent(requestParameter[0]);
				requestParameters.put(requestParameterName, new ArrayList<String>());
				final String requestParameterValue = requestParameter.length > 1 ? decodeUrlComponent(requestParameter[1]) : null;
				requestParameters.get(requestParameterName).add(requestParameterValue);
			}
		}
		return requestParameters;
	}

	private static String produceWhereStatement(Map<String, List<String>> requestParameters) {
		Iterator<String> itr = requestParameters.keySet().iterator();
		Map<String, String> queryParams = new HashMap<String, String>();
		String statment = "";
		while(itr.hasNext()) {
			String Key = itr.next();
			String Value = requestParameters.get(Key).get(0);
			statment = statment + Key + "=" + "\"" + Value + "\"" +" AND ";
		}
		if (statment.length() != 0)
			statment = statment.substring(0, statment.length()-5);
		return statment;
	}

	private static String LoadDBDriver(boolean isInvitations, String whereStr) {
		Connection conn = null;
		Statement stmt = null;
		String midBody = "";
		try {
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			if (isInvitations)
			{
				if (whereStr != null && !whereStr.isEmpty()) 
					sql = "SELECT UserIdentifier, Firstname, Lastname, City, PhoneNumber, CourseDegree, IAG, CG, PSG, DG, PR, EXCV, EXMD, EXPS, EXSP, EXJC, EXTC, EXOT, FM, MD, IT, MG, TimeP FROM InvUserTableV1 WHERE "+whereStr;
				else
					sql = "SELECT UserIdentifier, Firstname, Lastname, City, PhoneNumber, CourseDegree, IAG, CG, PSG, DG, PR, EXCV, EXMD, EXPS, EXSP, EXJC, EXTC, EXOT, FM, MD, IT, MG, TimeP FROM InvUserTableV1";
			}
			else
			{
				if (whereStr != null && !whereStr.isEmpty()) 
					sql = "SELECT UserIdentifier, Firstname, Lastname, City, PhoneNumber, FullAddress, AdditionalDescription FROM SndUserTableV1 WHERE "+whereStr;
				else
					sql = "SELECT UserIdentifier, Firstname, Lastname, City, PhoneNumber, FullAddress, AdditionalDescription FROM SndUserTableV1";
			}

			System.out.println("SQL QUERY: "+sql);
			ResultSet rs = stmt.executeQuery(sql);

			//STEP 5: Extract data from result set
			if(isInvitations)
			{
				while(rs.next()){
					String userIdentifier = rs.getString("UserIdentifier");
					String firstname = rs.getString("Firstname");
					String lastname = rs.getString("Lastname");
					String city = rs.getString("City");
					String phoneNumber = rs.getString("PhoneNumber");
					String courseDegree = rs.getString("CourseDegree");
					String iag = rs.getString("IAG");
					String cg = rs.getString("CG");
					String psg = rs.getString("PSG");
					String dg = rs.getString("DG");

					String pr = rs.getString("PR");
					String excv = rs.getString("EXCV");
					String exmd = rs.getString("EXMD");
					String exps = rs.getString("EXPS");
					String exsp = rs.getString("EXSP");
					String exjc = rs.getString("EXJC");
					String extc = rs.getString("EXTC");
					String exot = rs.getString("EXOT");
					String fm = rs.getString("FM");
					String md = rs.getString("MD");
					String it = rs.getString("IT");
					String mg = rs.getString("MG");
					String timep = rs.getString("TimeP");

					//String tableRow = "<tr>"+"<td>"+userIdentifier+"</td>"+"<td>"+firstname+"</td>"+"<td>"+lastname+"</td>"+"<td>"+city+"</td>"+"<td>"+phoneNumber+"</td>"+"<td>"+courseDegree+"</td>"+"<td>"+iag+"</td>"+"<td>"+cg+"</td>"+"<td>"+psg+"</td>"+"<td>"+dg+"</td>"+"<td>"+pr+"</td>"+"<td>"+excv+"</td>"+"<td>"+exmd+"</td>"+"<td>"+exps+"</td>"+"<td>"+exsp+"</td>"+"<td>"+exjc+"</td>"+"<td>"+extc+"</td>"+"<td>"+exot+"</td>"+"<td>"+fm+"</td>"+"<td>"+md+"</td>"+"<td>"+md+"</td>"+"<td>"+it+"</td>"+"<td>"+mg+"</td>"+"<td>"+timep+"</td>"+"</tr>";
					String tableRow = "<tr>"+"<td>"+userIdentifier+"</td>"+"<td>"+firstname+"</td>"+"<td>"+lastname+"</td>"+"<td>"+city+"</td>"+"<td>"+phoneNumber+"</td>"+"<td>"+courseDegree+"</td>"+"<td>"+iag+"</td>"+"<td>"+cg+"</td>"+"<td>"+psg+"</td>"+"<td>"+dg+"</td>"+"<td>"+pr+"</td>"+"<td>"+excv+"</td>"+"<td>"+exmd+"</td>"+"<td>"+exps+"</td>"+"<td>"+exsp+"</td>"+"<td>"+exjc+"</td>"+"<td>"+extc+"</td>"+"<td>"+exot+"</td>"+"<td>"+fm+"</td>"+"<td>"+md+"</td>"+"<td>"+it+"</td>"+"<td>"+mg+"</td>"+"<td>"+timep+"</td>"+"</tr>";
					midBody = midBody + tableRow;
				}
			}
			else
			{
				while(rs.next()){
					String userIdentifier = rs.getString("UserIdentifier");
					String firstname = rs.getString("Firstname");
					String lastname = rs.getString("Lastname");
					String city = rs.getString("City");
					String phoneNumber = rs.getString("PhoneNumber");
					String address = rs.getString("FullAddress");
					String additionalDescription = rs.getString("AdditionalDescription");

					String tableRow = "<tr>"+"<td>"+userIdentifier+"</td>"+"<td>"+firstname+"</td>"+"<td>"+lastname+"</td>"+"<td>"+city+"</td>"+"<td>"+phoneNumber+"</td>"+"<td>"+address+"</td>"+"<td>"+additionalDescription+"</td>"+"</tr>";
					midBody = midBody + tableRow;
				}
			}

			//STEP 6: Clean-up environment
			rs.close();
			stmt.close();
			conn.close();

		} catch(SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			//finally block used to close resources
			try {
				if(stmt!=null)
					stmt.close();
			} catch(SQLException se2) {
				se2.printStackTrace();
			}// nothing we can do

			try{
				if(conn!=null)
					conn.close();
			} catch(SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try
		return midBody;
	}

}
