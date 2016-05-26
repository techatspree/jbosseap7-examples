package de.akquinet.jbosscc.examples.eap.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@SuppressWarnings("serial")
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private static String PAGE_HEADER = "<html><head><title>Logged out successfully</title>" +
        "</head><body>";
    private static String PAGE_FOOTER = "</body></html>";

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final PrintWriter writer = resp.getWriter();

        writer.println(PAGE_HEADER);

        // BASIC auth: invalidate is not sufficient, because the status
        // HTTP basic authentication credentials are stored until the browser is closed
        req.getSession().invalidate();
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        writer.println("<h1>Successfully logged out</h1>"
            + " (<a href=\"dynamicproxy/index.faces\">Login again </a>)");

        writer.println(PAGE_FOOTER);
        writer.close();
    }
}
