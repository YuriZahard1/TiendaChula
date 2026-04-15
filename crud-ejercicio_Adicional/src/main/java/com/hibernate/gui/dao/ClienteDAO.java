package com.hibernate.gui.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernate.gui.model.Cliente;
import com.hibernate.gui.model.Producto;
import com.hibernate.gui.util.ProductoConection;

public class ClienteDAO {
	public void insertCliente(Cliente c) {
		Transaction transaction = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(c);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}

	public void updateCliente(Cliente c) {
		Transaction transaction = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(c);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}

	public void deleteCliente(int id) {
		Transaction transaction = null;
		Cliente c = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			c = session.find(Cliente.class, id);
			session.remove(c);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}

	public Cliente selectClienteById(int id) {
		Transaction transaction = null;
		Cliente c = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			c = session.find(Cliente.class, id);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return c;
	}

	public List<Cliente> selectAllCliente() {
		Transaction transaction = null;
		List<Cliente> cliente = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			cliente = session.createQuery("from Cliente", Cliente.class).getResultList();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return cliente;
	}
}
