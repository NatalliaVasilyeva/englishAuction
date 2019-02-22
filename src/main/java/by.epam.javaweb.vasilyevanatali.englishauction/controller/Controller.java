package main.java.by.epam.javaweb.vasilyevanatali.englishauction.controller;

import main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.impl.UserDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/controller")
public class Controller extends HttpServlet {
    private static final long serialVersionUID = 124578466L;
    private static String INSERT_OR_EDIT = "/user.jsp";
    private static String LIST_USER = "/listuser.jsp";
    private UserDao dao;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String forward = "";
        String action = request.getParameter("action");

        if(action.equalsIgnoreCase("delete")){
            String userID = request.getParameter("id");
        }
    }
}
