package com.hibernate.gui;

import java.awt.EventQueue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.hibernate.gui.dao.DistribuidorDAO;
import com.hibernate.gui.dao.ProductoDAO;
import com.hibernate.gui.model.Distribuidor;
import com.hibernate.gui.model.Producto;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSpinner;

public class Tienda {

	private JFrame frmTienda;
	private JTable table;
	private JTextField txtId;
	private JTextField txtNombre;
	private JTextField txtStock;
	private JTextField txtPrecio;
	private JComboBox<Object> comboBox;

	ProductoDAO pDAO = new ProductoDAO();
	Producto p = new Producto();
	DistribuidorDAO dDAO = new DistribuidorDAO();
	Distribuidor d = new Distribuidor();

	private JTable tableDis;
	private JTextField txtIdDis;
	private JTextField txtNombreDis;
	private JTextField txtAnyo;

	void mostrarTabla() {
		try {
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("id");
			model.addColumn("nombre");
			model.addColumn("stock");
			model.addColumn("precio");
			model.setRowCount(0);
			List<Producto> producto = pDAO.selectAllProduct();
			for (Producto p : producto) {
				Object[] fila = { p.getCodigo(), p.getNombre(), p.getStock(), p.getPrecio() };
				model.addRow(fila);
			}
			table.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void mostrarTablaDis() {
		try {
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("id");
			model.addColumn("nombre");
			model.addColumn("Año de inicio");
			model.setRowCount(0);
			List<Distribuidor> distribuidor = dDAO.selectAllDistribuidor();
			for (Distribuidor d : distribuidor) {
				Object[] fila = { d.getCodigo(), d.getNombre(), d.getAnyo_inicio() };
				model.addRow(fila);
			}
			tableDis.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void mostrarTablaSinStock() {
		try {
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("id");
			model.addColumn("nombre");
			model.addColumn("stock");
			model.addColumn("precio");
			model.setRowCount(0);
			List<Producto> producto = pDAO.selectProductSinStock();
			for (Producto p : producto) {
				Object[] fila = { p.getCodigo(), p.getNombre(), p.getStock(), p.getPrecio() };
				model.addRow(fila);
			}
			table.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void mostrarTablaCaros() {
		try {
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("id");
			model.addColumn("nombre");
			model.addColumn("stock");
			model.addColumn("precio");
			model.setRowCount(0);
			List<Producto> producto = pDAO.selectProductosCaros();
			for (Producto p : producto) {
				Object[] fila = { p.getCodigo(), p.getNombre(), p.getStock(), p.getPrecio() };
				model.addRow(fila);
			}
			table.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void mostrarTablaBaratos() {
		try {
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("id");
			model.addColumn("nombre");
			model.addColumn("stock");
			model.addColumn("precio");
			model.setRowCount(0);
			List<Producto> producto = pDAO.selectProductosBaratos();
			for (Producto p : producto) {
				Object[] fila = { p.getCodigo(), p.getNombre(), p.getStock(), p.getPrecio() };
				model.addRow(fila);
			}
			table.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tienda window = new Tienda();
					window.frmTienda.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Tienda() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTienda = new JFrame();
		frmTienda.setTitle("Tienda");
		frmTienda.setBounds(100, 100, 692, 436);
		frmTienda.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTienda.getContentPane().setLayout(null);

		JButton btnMostrar = new JButton("Mostrar");
		btnMostrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object seleccion = comboBox.getSelectedItem();
				if (seleccion.equals("Todos")) {
					mostrarTabla();
				} else if (seleccion.equals("Sin Stock")) {
					mostrarTablaSinStock();
				} else if (seleccion.equals("Caros")) {
					mostrarTablaCaros();
				} else {
					mostrarTablaBaratos();
				}
			}
		});
		btnMostrar.setBounds(12, 322, 105, 27);
		frmTienda.getContentPane().add(btnMostrar);

		JButton btnMostrarDis = new JButton("MostrarDis");
		btnMostrarDis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarTablaDis();
			}
		});
		btnMostrarDis.setBounds(150, 324, 89, 23);
		frmTienda.getContentPane().add(btnMostrarDis);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(29, 12, 191, 149);
		frmTienda.getContentPane().add(scrollPane);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = table.getSelectedRow();

				String id = table.getValueAt(fila, 0).toString();
				String nombre = table.getValueAt(fila, 1).toString();
				String stock = table.getValueAt(fila, 2).toString();
				String precio = table.getValueAt(fila, 3).toString();

				txtId.setText(id);
				txtNombre.setText(nombre);
				txtStock.setText(stock);
				txtPrecio.setText(precio);

			}
		});
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollPane.setViewportView(table);
		frmTienda.getContentPane().add(scrollPane);

		JLabel lblIdproduct = new JLabel("idProducto:");
		lblIdproduct.setBounds(29, 172, 74, 17);
		frmTienda.getContentPane().add(lblIdproduct);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(29, 201, 60, 17);
		frmTienda.getContentPane().add(lblNombre);

		JLabel lblStock = new JLabel("Stock:");
		lblStock.setBounds(29, 230, 60, 17);
		frmTienda.getContentPane().add(lblStock);

		JLabel lblPrecio = new JLabel("Precio:");
		lblPrecio.setBounds(29, 259, 60, 17);
		frmTienda.getContentPane().add(lblPrecio);

		txtId = new JTextField();
		txtId.setEditable(false);
		txtId.setBounds(106, 172, 114, 21);
		frmTienda.getContentPane().add(txtId);
		txtId.setColumns(10);

		txtNombre = new JTextField();
		txtNombre.setBounds(107, 199, 114, 21);
		frmTienda.getContentPane().add(txtNombre);
		txtNombre.setColumns(10);

		txtStock = new JTextField();
		txtStock.setBounds(106, 228, 114, 21);
		frmTienda.getContentPane().add(txtStock);
		txtStock.setColumns(10);

		txtPrecio = new JTextField();
		txtPrecio.setBounds(107, 257, 114, 21);
		frmTienda.getContentPane().add(txtPrecio);
		txtPrecio.setColumns(10);

		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String nombre = txtNombre.getText();
				int stock = Integer.parseInt(txtStock.getText());
				int precio = Integer.parseInt(txtPrecio.getText());
				p = Producto.builder().nombre(nombre).stock(stock).precio(precio).build();
				pDAO.insertProducto(p);
				btnMostrar.doClick();
			}
		});
		btnGuardar.setBounds(227, 167, 105, 27);
		frmTienda.getContentPane().add(btnGuardar);

		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int idS = Integer.parseInt(txtId.getText());
				String nombre = txtNombre.getText();
				int stock = Integer.parseInt(txtStock.getText());
				int precio = Integer.parseInt(txtPrecio.getText());
				p = pDAO.selectProductById(idS);
				p.setNombre(nombre);
				p.setStock(stock);
				p.setPrecio(precio);
				pDAO.updateProduct(p);
				btnMostrar.doClick();
			}
		});
		btnActualizar.setBounds(227, 201, 105, 27);
		frmTienda.getContentPane().add(btnActualizar);

		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int idS = Integer.parseInt(txtId.getText());
				pDAO.deleteProduct(idS);
				btnMostrar.doClick();
			}
		});
		btnBorrar.setBounds(227, 237, 105, 27);
		frmTienda.getContentPane().add(btnBorrar);

		comboBox = new JComboBox<Object>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnMostrar.doClick();
			}
		});
		comboBox.setModel(new DefaultComboBoxModel<Object>(new String[] { "Todos", "Sin Stock", "Caros", "Baratos" }));
		comboBox.setBounds(232, 12, 100, 26);
		frmTienda.getContentPane().add(comboBox);

		JLabel lblComprar = new JLabel("Comprar Productos:");
		lblComprar.setBounds(230, 49, 102, 14);
		frmTienda.getContentPane().add(lblComprar);

		JLabel lblCantidad = new JLabel("Cantidad:");
		lblCantidad.setBounds(230, 91, 53, 14);
		frmTienda.getContentPane().add(lblCantidad);

		JSpinner spinner = new JSpinner();
		spinner.setBounds(289, 88, 43, 20);
		frmTienda.getContentPane().add(spinner);

		JButton btnComprar = new JButton("Comprar");
		btnComprar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int idS = Integer.parseInt(txtId.getText());
				p = pDAO.selectProductById(idS);
				int nuevo_stock = p.getStock() - (int) spinner.getValue();
				if (nuevo_stock < 0) {
					JOptionPane.showMessageDialog(null, "Se ha completado la venta de: " + p.getStock()
							+ " unidades por un precio de: " + p.getPrecio() * p.getStock());
					p.setStock(0);
				} else {
					JOptionPane.showMessageDialog(null, "Se ha completado la venta de: " + spinner.getValue()
							+ " unidades por un precio de: " + p.getPrecio() * (int) spinner.getValue());
					p.setStock(p.getStock() - (int) spinner.getValue());
				}
				pDAO.updateProduct(p);
				btnMostrar.doClick();
			}
		});
		btnComprar.setBounds(261, 117, 89, 23);
		frmTienda.getContentPane().add(btnComprar);

		JScrollPane scrollDis = new JScrollPane();
		scrollDis.setBounds(381, 12, 191, 149);
		frmTienda.getContentPane().add(scrollDis);

		tableDis = new JTable();
		tableDis.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = tableDis.getSelectedRow();

				String id = tableDis.getValueAt(fila, 0).toString();
				String nombre = tableDis.getValueAt(fila, 1).toString();
				String anyo = tableDis.getValueAt(fila, 2).toString();

				txtIdDis.setText(id);
				txtNombreDis.setText(nombre);
				txtAnyo.setText(anyo);

			}
		});
		tableDis.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollDis.setViewportView(tableDis);
		frmTienda.getContentPane().add(scrollDis);

		JLabel lblDis = new JLabel("idDistribuidor:");
		lblDis.setBounds(363, 172, 74, 17);
		frmTienda.getContentPane().add(lblDis);

		txtIdDis = new JTextField();
		txtIdDis.setEditable(false);
		txtIdDis.setColumns(10);
		txtIdDis.setBounds(440, 172, 114, 21);
		frmTienda.getContentPane().add(txtIdDis);

		JLabel lblNombreDis = new JLabel("Nombre:");
		lblNombreDis.setBounds(363, 201, 60, 17);
		frmTienda.getContentPane().add(lblNombreDis);

		txtNombreDis = new JTextField();
		txtNombreDis.setColumns(10);
		txtNombreDis.setBounds(441, 199, 114, 21);
		frmTienda.getContentPane().add(txtNombreDis);

		JLabel lblAnyo = new JLabel("Año de inicio:");
		lblAnyo.setBounds(356, 230, 81, 17);
		frmTienda.getContentPane().add(lblAnyo);

		txtAnyo = new JTextField();
		txtAnyo.setColumns(10);
		txtAnyo.setBounds(440, 228, 114, 21);
		frmTienda.getContentPane().add(txtAnyo);

		JButton btnGuardarDis = new JButton("Guardar");
		btnGuardarDis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = txtNombreDis.getText();
				int anyo = Integer.parseInt(txtAnyo.getText());
				d = Distribuidor.builder().nombre(nombre).anyo_inicio(anyo).build();
				dDAO.insertDistribuidor(d);
				btnMostrarDis.doClick();
			}
		});
		btnGuardarDis.setBounds(561, 167, 105, 27);
		frmTienda.getContentPane().add(btnGuardarDis);

		JButton btnActualizarDis = new JButton("Actualizar");
		btnActualizarDis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int idS = Integer.parseInt(txtIdDis.getText());
				String nombre = txtNombreDis.getText();
				int anyo = Integer.parseInt(txtAnyo.getText());
				d = dDAO.selectDistribuidorById(idS);
				d.setNombre(nombre);
				d.setAnyo_inicio(anyo);
				dDAO.updateDistribuidor(d);
				btnMostrarDis.doClick();
			}
		});
		btnActualizarDis.setBounds(561, 201, 105, 27);
		frmTienda.getContentPane().add(btnActualizarDis);

		JButton btnBorrarDis = new JButton("Borrar");
		btnBorrarDis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int idS = Integer.parseInt(txtIdDis.getText());
				dDAO.deleteDistribuidor(idS);
				btnMostrarDis.doClick();
			}
		});
		btnBorrarDis.setBounds(561, 237, 105, 27);
		frmTienda.getContentPane().add(btnBorrarDis);

		btnMostrar.setVisible(false);
		btnMostrarDis.setVisible(false);
		mostrarTabla();
		mostrarTablaDis();
	}
}
