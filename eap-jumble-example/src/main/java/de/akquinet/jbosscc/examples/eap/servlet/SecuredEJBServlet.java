package de.akquinet.jbosscc.examples.eap.servlet;

import de.akquinet.jbosscc.examples.eap.dynamic.SecurityService;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@SuppressWarnings("serial")
@WebServlet("/SecuredEJBServlet")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"user", "admin"}))
public class SecuredEJBServlet extends HttpServlet {
    private static String PAGE_HEADER = "<html><head><title>Back</title>" +
        " (<a href=\"index.faces\">Back</a>)" +
        "</head><body>";
    private static String PAGE_FOOTER = "</body></html>";

    @EJB
    private SecurityService securedEJB;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final boolean logout = req.getParameter("logout") != null;
        final PrintWriter writer = resp.getWriter();
        final String principal = securedEJB.getPrincipal();
        final String authType = req.getAuthType();
        final String remoteUser = req.getRemoteUser();

        writer.println(PAGE_HEADER);

        if (logout) {
            // BASIC auth: invalidate is not sufficient, because the status
            // HTTP basic authentication credentials are stored until the browser is closed
            req.getSession().invalidate();
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            writer.println("<h1>Successfully logged out</h1>"
                + " (<a href=\"index.faces\">Login again </a>)");
        } else {
            writer.println("<h1>Successfully called Secured EJB</h1>");
            writer.println("<p>" + "Principal  : " + principal + "</p>");
            writer.println("<p>" + "Remote User : " + remoteUser + "</p>");
            writer.println("<p>" + "Authentication Type : " + authType + "</p>");
        }

        writer.println(PAGE_FOOTER);
        writer.close();
    }
}
