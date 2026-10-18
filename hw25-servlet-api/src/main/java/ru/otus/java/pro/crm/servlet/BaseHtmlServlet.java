package ru.otus.java.pro.crm.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

abstract class BaseHtmlServlet extends HttpServlet {
    protected PrintWriter beginHtml(HttpServletResponse response, String title) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("""
                <!doctype html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <link rel="stylesheet" href="/static/style.css">
                """);
        out.printf("    <title>%s</title>%n", escape(title));
        out.println("""
                </head>
                <body>
                <main class="container">
                """);
        return out;
    }

    protected void endHtml(PrintWriter out) {
        out.println("""
                </main>
                </body>
                </html>
                """);
    }

    protected String escape(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
