package com.hibernate.gui.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernate.gui.model.Cliente;
import com.hibernate.gui.model.Compra;
import com.hibernate.gui.model.Producto;
import com.hibernate.gui.util.ProductoConection;

public class CompraDAO {
	public List<Compra> selectAllCompras() {
	    try (Session session = ProductoConection.getSessionFactory().openSession()) {
	        // Usamos JOIN FETCH para traer el cliente y producto en una sola consulta
	        // aunque la relación sea LAZY por defecto
	        return session.createQuery(
	            "SELECT c FROM Compra c JOIN FETCH c.cliente JOIN FETCH c.producto", 
	            Compra.class).list();
	    }
	}
	public void insertCompra(Compra compra) {
	    Transaction transaction = null;
	    try (Session session = ProductoConection.getSessionFactory().openSession()) {
	        // Iniciar transacción
	        transaction = session.beginTransaction();
	        
	        // Guardar la compra
	        session.persist(compra);
	        
	        // Confirmar
	        transaction.commit();
	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        e.printStackTrace();
	    }
	}
	public void deleteCompra(int id) {
	    Transaction transaction = null;
	    try (Session session = ProductoConection.getSessionFactory().openSession()) {
	        transaction = session.beginTransaction();
	        
	        // Buscamos la compra
	        Compra compra = session.get(Compra.class, id);
	        
	        if (compra != null) {
	            // OPCIONAL: Si quieres devolver el stock al producto al borrar la compra
	            Producto p = compra.getProducto();
	            p.setStock(p.getStock() + compra.getUnidades());
	            session.merge(p); // Actualizamos el producto con el stock devuelto
	            
	            // Borramos la relación
	            session.remove(compra);
	        }
	        
	        transaction.commit();
	    } catch (Exception e) {
	        if (transaction != null) transaction.rollback();
	        e.printStackTrace();
	    }
	}
}
