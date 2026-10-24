package ru.otus.java.pro.crm.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.java.pro.crm.service.DBServiceClient;

import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends BaseHtmlServlet {
    private final DBServiceClient serviceClient;

    public LoginServlet(DBServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var session = request.getSession(false);
        if (session != null && "admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("/admin");
            return;
        }

        showLoginPage(response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        var client = serviceClient.findByEmail(email);

        if (client.isEmpty()
                || !client.get().getPassword().equals(password)
                || !"admin".equals(client.get().getRole())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            showLoginPage(response, "Неверный email/пароль или у пользователя нет роли admin");
            return;
        }

        var session = request.getSession(true);
        session.setAttribute("clientId", client.get().getId());
        session.setAttribute("role", client.get().getRole());

        response.sendRedirect("/admin");
    }

    private void showLoginPage(HttpServletResponse response, String error) throws IOException {
        PrintWriter out = beginHtml(response, "Вход");
        out.println("<h3>Вход для администратора</h3>");

        if (error != null) {
            out.printf("<p>%s</p>%n", escape(error));
        }

        out.println("""
                <form method="post" action="/">
                    <p>
                        <label for="email">Email</label><br>
                        <input type="email" id="email" name="email" required>
                    </p>
                    <p>
                        <label for="password">Пароль</label><br>
                        <input type="password" id="password" name="password" required>
                    </p>
                    <button type="submit">Войти</button>
                </form>
                """);
        endHtml(out);
    }
}
