package com.hibernate.gui.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernate.gui.model.Producto;
import com.hibernate.gui.util.ProductoConection;

public class ProductoDAO {
	public void insertProducto(Producto p) {
		Transaction transaction = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(p);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}

	public void updateProduct(Producto p) {
		Transaction transaction = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(p);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}

	public void deleteProduct(int id) {
		Transaction transaction = null;
		Producto p = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			p = session.find(Producto.class, id);
			session.remove(p);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}

	public Producto selectProductById(int id) {
		Transaction transaction = null;
		Producto p = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			p = session.find(Producto.class, id);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return p;
	}

	public List<Producto> selectAllProduct() {
		Transaction transaction = null;
		List<Producto> producto = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			producto = session.createQuery("from Producto", Producto.class).getResultList();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return producto;
	}

	public List<Producto> selectProductSinStock() {
		Transaction transaction = null;
		List<Producto> producto = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			producto = session.createQuery("from Producto WHERE stock <= 0", Producto.class).getResultList();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return producto;
	}

	public List<Producto> selectProductosCaros() {
		Transaction transaction = null;
		List<Producto> producto = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			producto = session.createQuery("from Producto WHERE precio > 1000", Producto.class).getResultList();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return producto;
	}

	public List<Producto> selectProductosBaratos() {
		Transaction transaction = null;
		List<Producto> producto = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			producto = session.createQuery("from Producto WHERE precio < 10", Producto.class).getResultList();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return producto;
	}
	
}
