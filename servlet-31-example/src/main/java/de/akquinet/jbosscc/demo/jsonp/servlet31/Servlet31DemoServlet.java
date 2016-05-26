package de.akquinet.jbosscc.demo.jsonp.servlet31;

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@WebServlet(
    urlPatterns = {"/spec"},
    asyncSupported = true
)
//http://localhost:8080/servlet31/spec
public class Servlet31DemoServlet extends HttpServlet {

    private final static String FILE_PATH = "/servlet-3_1-final.pdf";

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            //let's serve a big file - the servlet 3.1 spec pdf
            final AsyncContext asyncContext = request.startAsync();
            final InputStream in = getClass().getResourceAsStream(FILE_PATH);
            final ServletOutputStream out = response.getOutputStream();

            response.setContentType("application/pdf");

            out.setWriteListener(new WriteListener() {
                public void onWritePossible() throws IOException {
                    int b = 0;
                    // always check if out.isReady() and only write in that case
                    // also watch out not to read() when out is not ready!
                    while (out.isReady() && (b = in.read()) != -1) {
                        out.write(b);
                    }

                    if (b == -1) {
                        in.close();
                        asyncContext.complete();
                    }
                }

                public void onError(Throwable t) {
                    t.printStackTrace();
                    asyncContext.complete();
                }
            });
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
