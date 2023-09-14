package com.alura.jdbc.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alura.jdbc.dao.ProductoDAO;
import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;

public class ProductoController {

	private ProductoDAO productoDAO;

	public ProductoController() {
		// TODO Auto-generated constructor stub

		this.productoDAO = new ProductoDAO(new ConnectionFactory().recuperaConexion());
	}

	public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) throws SQLException {

		ConnectionFactory factory = new ConnectionFactory();
		final Connection con = factory.recuperaConexion();

		try (con) {
			final PreparedStatement statement = con.prepareStatement(
					"UPDATE PRODUCTO SET " + " NOMBRE = ?, " + " DESCRIPCION = ?," + " CANTIDAD = ?" + " WHERE ID = ?");

			try (statement) {

				statement.setString(1, nombre);
				statement.setString(2, descripcion);
				statement.setInt(3, cantidad);
				statement.setInt(4, id);

				statement.execute();

				int updateCount = statement.getUpdateCount();

				return updateCount;

			}
		}
	}

	public int eliminar(Integer id) throws SQLException {

		final Connection con = new ConnectionFactory().recuperaConexion();

		try (con) {

			final PreparedStatement statement = con.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?");

			try (statement) {

				statement.setInt(1, id);

				statement.execute();

				return statement.getUpdateCount();

			}

		}

	}

	public List<Producto> listar() {

		return productoDAO.listar();

	}
	
	public List<Producto> listar(Categoria categoria){
		return productoDAO.listar(categoria.getId());		
	}

	public void guardar(Producto producto, Integer categoriaId) {
		producto.setCategoriaId(categoriaId);
		productoDAO.guardar(producto);
	}

}
