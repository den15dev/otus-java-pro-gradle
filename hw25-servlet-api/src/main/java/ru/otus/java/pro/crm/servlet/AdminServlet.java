package ru.otus.java.pro.crm.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.java.pro.crm.model.Client;
import ru.otus.java.pro.crm.service.DBServiceClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AdminServlet extends BaseHtmlServlet {
    private final DBServiceClient serviceClient;

    public AdminServlet(DBServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("/");
            return;
        }

        List<Client> clients = serviceClient.findAll();

        PrintWriter out = beginHtml(response, "Клиенты");
        out.println("""
            <h2>Клиенты</h2>

            <h3>Создать клиента</h3>
            <form method="post" action="/admin">
                <p>
                    <label for="name">Имя</label><br>
                    <input type="text" id="name" name="name" required>
                </p>
                <p>
                    <label for="email">Email</label><br>
                    <input type="email" id="email" name="email" required>
                </p>
                <p>
                    <label for="password">Пароль</label><br>
                    <input type="password" id="password" name="password" required>
                </p>
                <button type="submit">Создать</button>
            </form>

            <h3>Список клиентов</h3>
            <table border="1" cellpadding="6" cellspacing="0">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Имя</th>
                        <th>Email</th>
                        <th>Роль</th>
                    </tr>
                </thead>
                <tbody>
            """);

        for (Client client : clients) {
            out.printf("""
                <tr>
                    <td>%d</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                </tr>
                """,
                client.getId(),
                escape(client.getName()),
                escape(client.getEmail()),
                escape(client.getRole())
            );
        }

        out.println("""
                    </tbody>
                </table>
                """);
        endHtml(out);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("/");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        serviceClient.saveClient(new Client(
            email,
            password,
            "user",
            name,
            null,
            List.of()
        ));

        response.sendRedirect("/admin");
    }

    private boolean isAdmin(HttpServletRequest request) {
        var session = request.getSession(false);
        return session != null && "admin".equals(session.getAttribute("role"));
    }
}
