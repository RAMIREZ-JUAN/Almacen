package com.emergentes.controlador;

import com.emergentes.modelo.Producto;
import com.emergentes.utiles.ConexionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String op;
            op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";

            ArrayList<Producto> lista = new ArrayList<Producto>();
            ConexionDB canal = new ConexionDB();
            Connection conn = canal.conectar();
            PreparedStatement ps;
            ResultSet rs;

            if (op.equals("list")) {

                String sql = "select * from productos";
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();

                while (rs.next()) {
                    Producto lib = new Producto();

                    lib.setId(rs.getInt("id"));
                    lib.setProducto(rs.getString("producto"));
                    lib.setPrecio(rs.getFloat("precio"));
                    lib.setCantidad(rs.getInt("cantidad"));

                    lista.add(lib);
                }
                request.setAttribute("lista", lista);
                request.getRequestDispatcher("index.jsp").forward(request, response);

            }
            if (op.equals("nuevo")) {
                Producto li = new Producto();
                System.out.println(li.toString());

                request.setAttribute("lib", li);
                request.getRequestDispatcher("editar.jsp").forward(request, response);

            }
            if (op.equals("editar")) {
                int id = Integer.parseInt(request.getParameter("id"));
                String sql = "delete from productos where id=?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);

                ps.executeUpdate();
                Producto li = new Producto();
                System.out.println(li.toString());

                request.setAttribute("lib", li);
                request.getRequestDispatcher("editar.jsp").forward(request, response); 
                

            }

            if (op.equals("eliminar")) {
                int id = Integer.parseInt(request.getParameter("id"));
                String sql = "delete from productos where id=?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);

                ps.executeUpdate();
                response.sendRedirect("MainController");

            }
        } catch (SQLException ex) {
            System.out.println("Error al conectar" + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            int id = Integer.parseInt(request.getParameter("id"));

            String producto = request.getParameter("producto");
            float precio = Float.parseFloat(request.getParameter("precio"));
            int cantidad = Integer.parseInt(request.getParameter("cantidad"));

            Producto lib = new Producto();
            lib.setProducto(producto);
            lib.setId(id);
            lib.setPrecio(precio);
            lib.setCantidad(cantidad);

            ConexionDB canal = new ConexionDB();
            Connection conn = canal.conectar();

            PreparedStatement ps;
            ResultSet rs;

            if (id == 0) {
                String sql = "insert into productos (producto,precio,cantidad)values(?,?,?)";

                ps = conn.prepareStatement(sql);
                ps.setString(1, lib.getProducto());
                ps.setFloat(2, lib.getPrecio());
                ps.setInt(3, lib.getCantidad());

                ps.executeUpdate();
                response.sendRedirect("MainController");
            }

        } catch (SQLException ex) {
            System.out.println("Error en sql" + ex.getMessage());
        }

    }

}
