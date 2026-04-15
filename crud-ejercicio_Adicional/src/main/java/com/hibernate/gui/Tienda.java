package com.hibernate.gui;

import java.awt.Color;

import java.awt.EventQueue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatDarkLaf;
import com.hibernate.gui.dao.ClienteDAO;
import com.hibernate.gui.dao.CompraDAO;
import com.hibernate.gui.dao.DistribuidorDAO;
import com.hibernate.gui.dao.ProductoDAO;
import com.hibernate.gui.model.Cliente;
import com.hibernate.gui.model.Compra;
import com.hibernate.gui.model.Distribuidor;
import com.hibernate.gui.model.Producto;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSpinner;

import jakarta.validation.*;
import java.util.Set;
import java.util.stream.Collectors;

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
	Cliente c = new Cliente();
	ClienteDAO cDAO = new ClienteDAO();
	Compra comp = new Compra();
	CompraDAO compDAO = new CompraDAO();

	private JTable tableDis;
	private JTextField txtIdDis;
	private JTextField txtNombreDis;
	private JTextField txtAnyo;
	private JTable tablePxd;
	private JTable tableCliente;
	private JTextField txtIdCliente;
	private JTextField txtNomClie;
	private JTextField txtEdad;
	private JLabel lblFotoFrame; // Donde se verá la imagen
	private byte[] fotoActual; // Buffer temporal para la imagen seleccionada
	private JTextField txtFotoUrl;
	private JLabel lblFotoPreview;
	private JTable tableCompra;

	private DefaultTableModel getModeloNoEditable() {
		return new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
	}

	void mostrarTabla() {
		try {
			DefaultTableModel model = getModeloNoEditable();
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
			DefaultTableModel model = getModeloNoEditable();
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

	public void mostrarTablaPxD() {
		try {
			// 1. Creamos el modelo usando tu método auxiliar (igual que en las otras
			// tablas)
			DefaultTableModel model = getModeloNoEditable();
			model.addColumn("ID Distribuidor");
			model.addColumn("ID Producto");

			// Limpiamos filas (aunque al crear uno nuevo ya nace vacío)
			model.setRowCount(0);

			// 2. Pedimos los datos al DAO
			List<Distribuidor> distribuidores = dDAO.selectAllDistribuidorWithProducts();

			// 3. Rellenar el modelo recorriendo la relación ManyToMany
			if (distribuidores != null) {
				for (Distribuidor d : distribuidores) {
					for (Producto p : d.getProductos()) {
						Object[] fila = { d.getCodigo(), p.getCodigo() };
						model.addRow(fila);
					}
				}
			}

			// 4. Asignar el modelo a la tabla
			tablePxd.setModel(model);

		} catch (Exception e) {
			System.err.println("Error al cargar la tabla de relaciones: " + e.getMessage());
			e.printStackTrace();
		}
	}

	void mostrarTablaCli() {
		try {
			DefaultTableModel model = getModeloNoEditable();
			model.addColumn("id");
			model.addColumn("nombre");
			model.addColumn("edad");
			model.setRowCount(0);
			List<Cliente> cliente = cDAO.selectAllCliente();
			for (Cliente c : cliente) {
				Object[] fila = { c.getCodigo(), c.getNombre(), c.getEdad() };
				model.addRow(fila);
			}
			tableCliente.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void mostrarTablaCompras() {
		try {
			// ... (configuración de columnas igual)
			DefaultTableModel model = getModeloNoEditable();
			model.addColumn("id Compra");
			model.addColumn("id Cliente");
			model.addColumn("id Producto");
			model.addColumn("Unidades");
			model.setRowCount(0);
			// El DAO ahora puede devolver un Set o puedes convertir la lista a Set
			Collection<Compra> lista = compDAO.selectAllCompras();

			for (Compra c : lista) {
				Object[] fila = { c.getId(), c.getCliente().getCodigo(), c.getProducto().getCodigo(), c.getUnidades() };
				model.addRow(fila);
			}
			tableCompra.setModel(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void mostrarTablaSinStock() {
		try {
			DefaultTableModel model = getModeloNoEditable();
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
			DefaultTableModel model = getModeloNoEditable();
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
			DefaultTableModel model = getModeloNoEditable();
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

	private void elegirFoto() {
		javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
		int res = fc.showOpenDialog(frmTienda);
		if (res == javax.swing.JFileChooser.APPROVE_OPTION) {
			java.io.File archivo = fc.getSelectedFile();
			try {
				// Convertir archivo a bytes
				fotoActual = java.nio.file.Files.readAllBytes(archivo.toPath());
				// Mostrar vista previa (escalada a 100x100 por ejemplo)
				javax.swing.ImageIcon icono = new javax.swing.ImageIcon(fotoActual);
				java.awt.Image img = icono.getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
				lblFotoFrame.setIcon(new javax.swing.ImageIcon(img));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void mostrarImagenProducto(String urlTexto) {
		if (urlTexto == null || urlTexto.trim().isEmpty()) {
			lblFotoPreview.setIcon(null);
			lblFotoPreview.setText("Sin imagen");
			return;
		}
		try {
			java.net.URL url = new java.net.URL(urlTexto);
			java.awt.Image img = javax.imageio.ImageIO.read(url);
			if (img != null) {
				// Escalamos la imagen al tamaño del label (105x105)
				java.awt.Image escalada = img.getScaledInstance(105, 105, java.awt.Image.SCALE_SMOOTH);
				lblFotoPreview.setIcon(new javax.swing.ImageIcon(escalada));
				lblFotoPreview.setText("");
			}
		} catch (Exception e) {
			lblFotoPreview.setIcon(null);
			lblFotoPreview.setText("URL inválida");
		}
	}

	private boolean esValido(Object objeto) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Object>> violations = validator.validate(objeto);

		if (!violations.isEmpty()) {
			// Unimos todos los mensajes de error en un solo String
			String errores = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("\n"));

			JOptionPane.showMessageDialog(frmTienda, errores, "Error de validación", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		// 2. Personalización global

		// Redondear todos los campos de texto globalmente
		UIManager.put("Component.arc", 10);
		UIManager.put("Button.arc", 10);
		UIManager.put("Button.arc", 15);
		UIManager.put("TextComponent.arc", 15);
		UIManager.put("Component.focusWidth", 2);

		UIManager.put("Button.hoverBackground", new Color(60, 60, 60));

		// Color del arco (curvatura) global para todos los botones
		UIManager.put("Button.arc", 20);

		// Color del borde cuando el botón tiene el foco (tabulador)
		UIManager.put("Button.focusColor", new Color(0, 150, 200));

		// O usar colores específicos para estados (Hover, Pressed)
		UIManager.put("Button.hoverBackground", new Color(50, 50, 50));
		UIManager.put("Button.focusedBackground", new Color(70, 70, 70));
		UIManager.put("Component.accentColor", new Color(255, 128, 0));
		UIManager.put("Button.foreground", Color.WHITE); // Texto blanco para que contraste

		UIManager.put("Table.background", Color.decode("#1e1e1e"));
		UIManager.put("Table.foreground", Color.decode("#ffffff"));

		// Color de la fila seleccionada
		UIManager.put("Table.selectionBackground", Color.decode("#a00123"));
		UIManager.put("Table.selectionForeground", Color.decode("#ffffff"));

		// Color de la cuadrícula (las líneas divisorias)
		UIManager.put("Table.gridColor", Color.decode("#333333"));
		UIManager.put("Table.showHorizontalLines", true);
		UIManager.put("Table.showVerticalLines", true);

		// IMPORTANTE: El encabezado (Header) se configura aparte
		UIManager.put("TableHeader.background", Color.decode("#2d2d2d"));
		UIManager.put("TableHeader.foreground", Color.decode("#ff5500"));
		UIManager.put("TableHeader.separatorColor", Color.decode("#444444"));

		UIManager.put("Table.alternateRowColor", Color.decode("#252525"));

		FlatDarkLaf.setup();
		JFrame.setDefaultLookAndFeelDecorated(true);

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
		frmTienda.setBounds(100, 100, 873, 709);
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
				mostrarTablaCli();
				mostrarTablaPxD();
			}
		});
		btnMostrarDis.setBounds(150, 324, 89, 23);
		frmTienda.getContentPane().add(btnMostrarDis);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		// O si usas FlatLaf, puedes redondearlo
		scrollPane.putClientProperty("JComponent.outline", Color.decode("#a00123"));
		scrollPane.setBounds(29, 12, 191, 149);
		frmTienda.getContentPane().add(scrollPane);

		table = new JTable();
		// Si solo quieres activarlo en UNA tabla específica:
		table.putClientProperty("Table.alternateRowColor", Color.decode("#252525"));
		table.setFillsViewportHeight(true);
		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int fila = table.getSelectedRow();
				if (fila != -1) {
					int id = Integer.parseInt(table.getValueAt(fila, 0).toString());
					// Buscamos el producto completo usando el DAO para traer la URL
					Producto prodSeleccionado = pDAO.selectProductById(id);

					txtId.setText(String.valueOf(prodSeleccionado.getCodigo()));
					txtNombre.setText(prodSeleccionado.getNombre());
					txtStock.setText(String.valueOf(prodSeleccionado.getStock()));
					txtPrecio.setText(String.valueOf(prodSeleccionado.getPrecio()));

					// Cargar la URL y la imagen
					txtFotoUrl.setText(prodSeleccionado.getFotoUrl());
					mostrarImagenProducto(prodSeleccionado.getFotoUrl());
				}
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
		btnGuardar.setBackground(Color.decode("#00FFFB"));
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String nombre = txtNombre.getText();
					int stock = Integer.parseInt(txtStock.getText());
					int precio = Integer.parseInt(txtPrecio.getText());
					String url = txtFotoUrl.getText(); // <--- Capturar URL
					p = Producto.builder().nombre(nombre).stock(stock).precio(precio).fotoUrl(url).build();
					if (esValido(p)) {
						pDAO.insertProducto(p);
						btnMostrar.doClick();
						JOptionPane.showMessageDialog(null, "Producto creado con exito", "SUCCES_MESSAGE",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null,
							"Debes rellenar con numeros enteros los campos stock ni precios", "ERROR_MESSAGE",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		btnGuardar.setBounds(227, 167, 105, 27);
		frmTienda.getContentPane().add(btnGuardar);

		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.setBackground(Color.decode("#00FFFB"));
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int idS = Integer.parseInt(txtId.getText());
					String nombre = txtNombre.getText();
					int stock = Integer.parseInt(txtStock.getText());
					int precio = Integer.parseInt(txtPrecio.getText());
					if (nombre.isEmpty()) {
						JOptionPane.showMessageDialog(null, "No puedes dejar vacio el campo nombre", "ERROR_MESSAGE",
								JOptionPane.ERROR_MESSAGE);
					} else {
						p = pDAO.selectProductById(idS);
						p.setNombre(nombre);
						p.setStock(stock);
						p.setPrecio(precio);
						p.setFotoUrl(txtFotoUrl.getText()); // <--- Actualizar URL
						pDAO.updateProduct(p);
						btnMostrar.doClick();
						JOptionPane.showMessageDialog(null, "Producto actualizado con exito", "SUCCES_MESSAGE",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "No debes dejar campos vacios", "ERROR_MESSAGE",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnActualizar.setBounds(227, 201, 105, 27);
		frmTienda.getContentPane().add(btnActualizar);

		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.setBackground(Color.decode("#00FFFB"));
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int idS = Integer.parseInt(txtId.getText());
					pDAO.deleteProduct(idS);
					btnMostrar.doClick();
					JOptionPane.showMessageDialog(null, "Producto borrado con exito", "SUCCES_MESSAGE",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Debes seleccionar una id para poder borrarla", "ERROR_MESSAGE",
							JOptionPane.ERROR_MESSAGE);
				}

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
		spinner.setBounds(289, 88, 61, 20);
		frmTienda.getContentPane().add(spinner);

		JButton btnComprar = new JButton("Comprar");
		btnComprar.setBackground(Color.decode("#249920"));
		btnComprar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
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
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Debes elegir un producto que comprar", "ERROR_MESSAGE",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		btnComprar.setBounds(261, 117, 89, 23);
		frmTienda.getContentPane().add(btnComprar);

		JLabel lblUrlProd = new JLabel("URL Foto:");
		lblUrlProd.setBounds(29, 288, 74, 17);
		frmTienda.getContentPane().add(lblUrlProd);

		txtFotoUrl = new JTextField();
		txtFotoUrl.setBounds(106, 286, 114, 21);
		frmTienda.getContentPane().add(txtFotoUrl);

		// El cuadro donde se verá la foto (al lado de los botones de Guardar/Borrar)
		lblFotoPreview = new JLabel("Sin imagen");
		lblFotoPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		lblFotoPreview.setHorizontalAlignment(JLabel.CENTER);
		lblFotoPreview.setBounds(245, 283, 105, 105);
		frmTienda.getContentPane().add(lblFotoPreview);

		JScrollPane scrollDis = new JScrollPane();
		scrollDis.setBorder(BorderFactory.createEmptyBorder());
		// O si usas FlatLaf, puedes redondearlo
		scrollDis.putClientProperty("JComponent.outline", Color.decode("#a00123"));
		scrollDis.setBounds(381, 12, 191, 149);
		frmTienda.getContentPane().add(scrollDis);

		tableDis = new JTable();
		// Si solo quieres activarlo en UNA tabla específica:
		tableDis.putClientProperty("Table.alternateRowColor", Color.decode("#252525"));
		tableDis.setFillsViewportHeight(true);
		tableDis.getSelectionModel().addListSelectionListener(e -> {
			// getValueIsAdjusting evita que el evento se dispare dos veces
			if (!e.getValueIsAdjusting()) {
				int fila = tableDis.getSelectedRow();

				if (fila != -1) { // Verificar que hay una fila seleccionada
					txtIdDis.setText(tableDis.getValueAt(fila, 0).toString());
					txtNombreDis.setText(tableDis.getValueAt(fila, 1).toString());
					txtAnyo.setText(tableDis.getValueAt(fila, 2).toString());
				}
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
		btnGuardarDis.setBackground(Color.decode("#4B17A3"));
		btnGuardarDis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String nombre = txtNombreDis.getText();
					int anyo = Integer.parseInt(txtAnyo.getText());

					d = Distribuidor.builder().nombre(nombre).anyo_inicio(anyo).build();
					if (esValido(d)) {
						dDAO.insertDistribuidor(d);
						btnMostrarDis.doClick();
						JOptionPane.showMessageDialog(null, "Distribuidor añadido con exito", "SUCCES_MESSAGE",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Debes rellenar con numeros enteros el campo año de inicio",
							"ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnGuardarDis.setBounds(561, 167, 105, 27);
		frmTienda.getContentPane().add(btnGuardarDis);

		JButton btnActualizarDis = new JButton("Actualizar");
		btnActualizarDis.setBackground(Color.decode("#4B17A3"));
		btnActualizarDis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (txtIdDis.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Selecciona un Distribuidor de la tabla");
						return;
					}
					int idS = Integer.parseInt(txtIdDis.getText());
					String nombre = txtNombreDis.getText();
					int anyo = Integer.parseInt(txtAnyo.getText());

					d = Distribuidor.builder().nombre(nombre).anyo_inicio(anyo).build();
					if (esValido(d)) {
						dDAO.updateDistribuidor(d);
						btnMostrarDis.doClick();
						JOptionPane.showMessageDialog(null, "Distribuidor actualizado con exito", "SUCCES_MESSAGE",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "El año se debe indicar en numeros enteros", "ERROR_MESSAGE",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnActualizarDis.setBounds(561, 201, 105, 27);
		frmTienda.getContentPane().add(btnActualizarDis);

		JButton btnBorrarDis = new JButton("Borrar");
		btnBorrarDis.setBackground(Color.decode("#4B17A3"));
		btnBorrarDis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int idS = Integer.parseInt(txtIdDis.getText());
					dDAO.deleteDistribuidor(idS);
					btnMostrarDis.doClick();
					JOptionPane.showMessageDialog(null, "Distribuidor borrado con exito", "SUCCES_MESSAGE",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Debes seleccionar una id para poder borrarla", "ERROR_MESSAGE",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		btnBorrarDis.setBounds(561, 237, 105, 27);
		frmTienda.getContentPane().add(btnBorrarDis);

		JScrollPane scrollPxD = new JScrollPane();
		scrollPxD.setBounds(382, 274, 172, 120);
		frmTienda.getContentPane().add(scrollPxD);

		tablePxd = new JTable();
		// Si solo quieres activarlo en UNA tabla específica:
		tablePxd.putClientProperty("Table.alternateRowColor", Color.decode("#252525"));
		tablePxd.setFillsViewportHeight(true);
		tablePxd.getSelectionModel().addListSelectionListener(e -> {
			// getValueIsAdjusting evita que el evento se dispare dos veces
			if (!e.getValueIsAdjusting()) {
				int fila = tablePxd.getSelectedRow();

				if (fila != -1) { // Verificar que hay una fila seleccionada
					txtIdDis.setText(tablePxd.getValueAt(fila, 0).toString());
					txtId.setText(tablePxd.getValueAt(fila, 1).toString());
				}
			}
		});
		tablePxd.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollPxD.setViewportView(tablePxd);
		frmTienda.getContentPane().add(scrollPxD);

		JButton btnAsignar = new JButton("Asignar");
		btnAsignar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int idProd = Integer.parseInt(txtId.getText());
					int idDist = Integer.parseInt(txtIdDis.getText());

					// En lugar de manipular las listas aquí, llamamos a un método del DAO
					dDAO.asignarProductoADistribuidor(idDist, idProd);

					// Refrescamos la tabla
					mostrarTablaPxD();
					JOptionPane.showMessageDialog(null, "Relación asignada correctamente");

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Selecciona un producto y un distribuidor");
				}
			}
		});
		btnAsignar.setBounds(561, 354, 105, 27);
		frmTienda.getContentPane().add(btnAsignar);

		JButton btnBorrarPxD = new JButton("Borrar");
		btnBorrarPxD.setBounds(561, 283, 105, 27);
		btnBorrarPxD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtId.getText().isEmpty() || txtIdDis.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Selecciona una fila de la tabla de relaciones");
					return;
				}

				try {
					int idProd = Integer.parseInt(txtId.getText());
					int idDist = Integer.parseInt(txtIdDis.getText());

					// 1. Llamar al borrado directo
					dDAO.eliminarRelacionFisica(idDist, idProd);

					// 2. RECARGAR DATOS (Muy importante)
					// Asegúrate de que mostrarTablaPxD() haga una nueva consulta a la DB
					mostrarTablaPxD();

					JOptionPane.showMessageDialog(null, "Relación eliminada permanentemente.");

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error crítico: " + e.getMessage());
				}
			}
		});
		frmTienda.getContentPane().add(btnBorrarPxD);

		JScrollPane scrollCliente = new JScrollPane();
		scrollCliente.setBounds(12, 410, 191, 175);
		frmTienda.getContentPane().add(scrollCliente);

		tableCliente = new JTable();
		// Si solo quieres activarlo en UNA tabla específica:
		tableCliente.putClientProperty("Table.alternateRowColor", Color.decode("#252525"));
		tableCliente.setFillsViewportHeight(true);
		tableCliente.getSelectionModel().addListSelectionListener(e -> {
			// getValueIsAdjusting evita que el evento se dispare dos veces
			if (!e.getValueIsAdjusting()) {
				int fila = tableCliente.getSelectedRow();

				if (fila != -1) {
					int id = (int) tableCliente.getValueAt(fila, 0);
					txtIdCliente.setText(tableCliente.getValueAt(fila, 0).toString());
					txtNomClie.setText(tableCliente.getValueAt(fila, 1).toString());
					txtEdad.setText(tableCliente.getValueAt(fila, 2).toString());
					Cliente seleccionado = cDAO.selectClienteById(id); // Debes tener este método en el DAO

					if (seleccionado.getFoto() != null) {
						fotoActual = seleccionado.getFoto();
						javax.swing.ImageIcon icono = new javax.swing.ImageIcon(fotoActual);
						java.awt.Image img = icono.getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
						lblFotoFrame.setIcon(new javax.swing.ImageIcon(img));
					} else {
						lblFotoFrame.setIcon(null);
						fotoActual = null;
					}
				}
			}
		});
		tableCliente.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollCliente.setViewportView(tableCliente);
		frmTienda.getContentPane().add(scrollCliente);

		JLabel lblIdCliente = new JLabel("idCliente:");
		lblIdCliente.setBounds(12, 592, 74, 17);
		frmTienda.getContentPane().add(lblIdCliente);

		txtIdCliente = new JTextField();
		txtIdCliente.setText((String) null);
		txtIdCliente.setEditable(false);
		txtIdCliente.setColumns(10);
		txtIdCliente.setBounds(89, 592, 114, 21);
		frmTienda.getContentPane().add(txtIdCliente);

		JLabel lblNombreCli = new JLabel("Nombre:");
		lblNombreCli.setBounds(12, 621, 60, 17);
		frmTienda.getContentPane().add(lblNombreCli);

		txtNomClie = new JTextField();
		txtNomClie.setColumns(10);
		txtNomClie.setBounds(90, 619, 114, 21);
		frmTienda.getContentPane().add(txtNomClie);

		JLabel lblEdadClie = new JLabel("Edad:");
		lblEdadClie.setBounds(12, 650, 60, 17);
		frmTienda.getContentPane().add(lblEdadClie);

		txtEdad = new JTextField();
		txtEdad.setColumns(10);
		txtEdad.setBounds(89, 648, 114, 21);
		frmTienda.getContentPane().add(txtEdad);

		JButton btnGuardarCli = new JButton("Guardar");
		btnGuardarCli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					// 1. Recolectamos datos
					String nombre = txtNomClie.getText();
					int edad = Integer.parseInt(txtEdad.getText());

					// 2. Construimos el objeto
					Cliente nuevoCliente = Cliente.builder().nombre(nombre).edad(edad).foto(fotoActual).build();

					// 3. VALIDACIÓN CENTRALIZADA
					if (esValido(nuevoCliente)) {
						cDAO.insertCliente(nuevoCliente);
						fotoActual = null;
						btnMostrarDis.doClick();
						JOptionPane.showMessageDialog(null, "Cliente añadido con éxito");
					}

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "La edad debe ser un número válido");
				}
			}
		});
		btnGuardarCli.setBounds(215, 432, 105, 27);
		frmTienda.getContentPane().add(btnGuardarCli);

		JButton btnActualizarCli = new JButton("Actualizar");
		btnActualizarCli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					// 1. Verificar que haya un ID seleccionado
					if (txtIdCliente.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Selecciona un cliente de la tabla");
						return;
					}

					int idS = Integer.parseInt(txtIdCliente.getText());
					String nombre = txtNomClie.getText();
					int edad = Integer.parseInt(txtEdad.getText());

					// 2. Crear un objeto temporal con los nuevos datos para validar
					// Ojo: Usamos el ID recuperado para que sea un objeto completo
					Cliente clienteAValidar = Cliente.builder().codigo(idS).nombre(nombre).edad(edad).foto(fotoActual)
							.build();

					// 3. Ejecutar la validación centralizada
					if (esValido(clienteAValidar)) {
						// Si pasa la validación, procedemos a actualizar en la DB
						cDAO.updateCliente(clienteAValidar);

						btnMostrarDis.doClick(); // Refrescar tabla
						JOptionPane.showMessageDialog(null, "Cliente actualizado con éxito");
					}

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "La edad debe ser un número entero", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnActualizarCli.setBounds(215, 482, 105, 27);
		frmTienda.getContentPane().add(btnActualizarCli);

		JButton btnBorrarCli = new JButton("Borrar");
		btnBorrarCli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int idS = Integer.parseInt(txtIdCliente.getText());
					cDAO.deleteCliente(idS);
					btnMostrarDis.doClick();
					JOptionPane.showMessageDialog(null, "Cliente borrado con exito", "SUCCES_MESSAGE",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Debes seleccionar una id para poder borrarla", "ERROR_MESSAGE",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnBorrarCli.setBounds(215, 536, 105, 27);
		frmTienda.getContentPane().add(btnBorrarCli);

		// Label para la imagen
		lblFotoFrame = new JLabel("Sin foto");
		lblFotoFrame.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		lblFotoFrame.setBounds(215, 580, 100, 100); // Ajusta según tu diseño
		frmTienda.getContentPane().add(lblFotoFrame);

		// Botón para cargar
		JButton btnCargarFoto = new JButton("Subir Foto");
		btnCargarFoto.setBounds(325, 580, 100, 25);
		btnCargarFoto.addActionListener(e -> elegirFoto());
		frmTienda.getContentPane().add(btnCargarFoto);

		JScrollPane scrollCompra = new JScrollPane();
		scrollCompra.setBounds(440, 410, 226, 195);
		frmTienda.getContentPane().add(scrollCompra);

		tableCompra = new JTable();
		scrollCompra.setViewportView(tableCompra);

		JButton btnComprar2 = new JButton("Comprar");
		btnComprar2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// 1. Obtener datos de la GUI
					int idProd = Integer.parseInt(txtId.getText());
					int idClie = Integer.parseInt(txtIdCliente.getText());
					int cant = (int) spinner.getValue();

					// 2. Obtener objetos de la DB
					Producto prod = pDAO.selectProductById(idProd);
					Cliente clie = cDAO.selectClienteById(idClie);

					if (prod.getStock() >= cant) {
						// 3. Actualizar Stock del producto
						prod.setStock(prod.getStock() - cant);
						pDAO.updateProduct(prod);

						// 4. Crear el registro de la compra (Relación N:M con atributo)
						Compra nuevaCompra = Compra.builder().cliente(clie).producto(prod).unidades(cant).build();

						compDAO.insertCompra(nuevaCompra);

						// 5. Refrescar interfaces
						mostrarTabla();
						mostrarTablaCompras();
						JOptionPane.showMessageDialog(null, "Compra registrada con éxito");
					} else {
						JOptionPane.showMessageDialog(null, "No hay stock suficiente");
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Selecciona un producto y un cliente");
				}
			}
		});
		btnComprar2.setBounds(678, 482, 105, 27);
		frmTienda.getContentPane().add(btnComprar2);

		JButton btnBorrarCompra = new JButton("Borrar Compra");
		btnBorrarCompra.setBackground(UIManager.getColor("Button.background")); // Rojo para avisar
		btnBorrarCompra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = tableCompra.getSelectedRow();

				if (fila == -1) {
					JOptionPane.showMessageDialog(null, "Selecciona una compra de la tabla");
					return;
				}

				// 1. Obtener el ID de la compra (columna 0 de la tabla de compras)
				int idCompra = (int) tableCompra.getValueAt(fila, 0);

				int confirmar = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres anular esta compra?",
						"Confirmar", JOptionPane.YES_NO_OPTION);

				if (confirmar == JOptionPane.YES_OPTION) {
					// 2. Llamar al DAO
					compDAO.deleteCompra(idCompra);

					// 3. Refrescar TODO
					mostrarTabla(); // El stock habrá cambiado
					mostrarTablaCompras(); // La compra habrá desaparecido

					JOptionPane.showMessageDialog(null, "Compra eliminada y stock actualizado.");
				}
			}
		});
		btnBorrarCompra.setBounds(678, 536, 134, 27); // Ajusta la posición según tu diseño
		frmTienda.getContentPane().add(btnBorrarCompra);

		btnMostrar.setVisible(false);
		btnMostrarDis.setVisible(false);
		mostrarTabla();
		mostrarTablaDis();
		mostrarTablaPxD();
		mostrarTablaCli();
		mostrarTablaCompras();
	}
}
