import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "simple-servlet", urlPatterns = "/simpleservlet")
public class SimpleServlet extends HttpServlet {
    private static final long serialVersionUID = 5202643316550976083L;
    private final Map<String, String> usersAgentMap = new HashMap<>();
    private final List<String> usersAgentList = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter respWriter = resp.getWriter();

        String userIp = req.getRemoteHost();
        String userAgent = req.getHeader("User-Agent");
        usersAgentMap.put(userIp, userAgent);
        String user = userIp + "::" + userAgent;
        usersAgentList.add(user);

        for (String key : usersAgentMap.keySet()) {
            if (key.equals(userIp)) {
                respWriter.println("<b>" + key + " :: " + usersAgentMap.get(key) + "</b>");
            } else {
                respWriter.println(key + " :: " + usersAgentMap.get(key));
            }
        }

        respWriter.println("<p>All requests:</p>\n" + "<ul>\n");
        for (String str : usersAgentList) {
            respWriter.println("<li>" + str + "</li>\n");
        }
        respWriter.println("</ul>\n");
    }
}