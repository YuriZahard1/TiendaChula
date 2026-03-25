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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.hibernate.gui.dao.ProductoDAO;
import com.hibernate.gui.model.Producto;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Tienda {

	private JFrame frmTienda;
	private JTable table;
	private JTextField txtId;
	private JTextField txtNombre;
	private JTextField txtStock;
	private JTextField txtPrecio;
	
	ProductoDAO pDAO = new ProductoDAO();
	Producto p=new Producto();
	
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
				Object[] fila = {p.getCodigo(), p.getNombre(), p.getStock(), p.getPrecio()};
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
		frmTienda.setBounds(100, 100, 569, 399);
		frmTienda.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTienda.getContentPane().setLayout(null);
		
		JButton btnMostrar = new JButton("Mostrar");
		btnMostrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mostrarTabla();
			}
		});
		btnMostrar.setBounds(423, 182, 105, 27);
		frmTienda.getContentPane().add(btnMostrar);
		
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
				p = new Producto(nombre, stock, precio);
				pDAO.insertProducto(p);
				btnMostrar.doClick();
			}
		});
		btnGuardar.setBounds(12, 288, 105, 27);
		frmTienda.getContentPane().add(btnGuardar);
		
		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int idS=Integer.parseInt(txtId.getText());
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
		btnActualizar.setBounds(169, 288, 105, 27);
		frmTienda.getContentPane().add(btnActualizar);
		
		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int idS=Integer.parseInt(txtId.getText());
				pDAO.deleteProduct(idS);
				btnMostrar.doClick();
			}
		});
		btnBorrar.setBounds(339, 288, 105, 27);
		frmTienda.getContentPane().add(btnBorrar);
		
		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int seleccion = comboBox.getSelectedIndex();
				if(seleccion==0) {
					mostrarTabla();
				}else if (seleccion==1) {
					
				}
				pDAO.selectAllProduct();
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Todos", "Sin Stock", "Caros", "Baratos"}));
		comboBox.setBounds(232, 12, 100, 26);
		frmTienda.getContentPane().add(comboBox);
		
		mostrarTabla();
	}
}
