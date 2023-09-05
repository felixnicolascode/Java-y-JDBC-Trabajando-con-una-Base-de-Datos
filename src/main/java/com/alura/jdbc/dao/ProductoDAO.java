package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Producto;

public class ProductoDAO {

	final private Connection con;

	public ProductoDAO(Connection con) {
		this.con = con;
	}

	public void guardar(Producto producto) {

		try (con) {

			con.setAutoCommit(false);

			final PreparedStatement statement = con.prepareStatement(
					"INSERT INTO PRODUCTO (nombre, descripcion, cantidad, categoria_id)" + " VALUES (?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			try (statement) {

				ejecutaRegristro(producto, statement);

			}

			con.commit();

		} catch (SQLException e) {
			try {
				con.rollback(); // También es una buena práctica manejar las excepciones y hacer rollback si
								// algo sale mal
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			throw new RuntimeException();
		}
	}

	private void ejecutaRegristro(Producto producto, PreparedStatement statement) throws SQLException {

		statement.setString(1, producto.getNombre());
		statement.setString(2, producto.getDescripcion());
		statement.setInt(3, producto.getCantidad());
		statement.setInt(4, producto.getCategoriaId());

		statement.execute();

		ResultSet resulSet = statement.getGeneratedKeys();

		try (resulSet) {

			while (resulSet.next()) {
				producto.setId(resulSet.getInt(1));
				System.out.println(String.format("Fue insertado el producto de ID %s", producto));

			}

		}

	}

	public List<Producto> listar() {

		List<Producto> resultado = new ArrayList<>();

		ConnectionFactory factory = new ConnectionFactory();

		final Connection con = new ConnectionFactory().recuperaConexion();

		try (con) {
			final PreparedStatement statement = con
					.prepareStatement("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO");

			try (statement) {
				statement.execute();

				final ResultSet resultSet = statement.getResultSet();

				try (resultSet) {

					while (resultSet.next()) {

						Producto fila = new Producto(resultSet.getInt("ID"), resultSet.getString("NOMBRE"),
								resultSet.getString("DESCRIPCION"), resultSet.getInt("CANTIDAD"));

						resultado.add(fila);

					}

				}

			}

			return resultado;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
