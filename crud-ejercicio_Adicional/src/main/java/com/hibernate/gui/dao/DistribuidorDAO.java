package com.hibernate.gui.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernate.gui.model.Distribuidor;
import com.hibernate.gui.model.Producto;
import com.hibernate.gui.util.ProductoConection;

public class DistribuidorDAO {
	public void insertDistribuidor(Distribuidor d) {
		Transaction transaction = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(d);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}

	public void updateDistribuidor(Distribuidor dist) {
	    Transaction transaction = null;
	    try (Session session = ProductoConection.getSessionFactory().openSession()) {
	        transaction = session.beginTransaction();
	        
	        // merge es mejor para objetos que vienen de fuera de la sesión (como los de la GUI)
	        session.merge(dist); 
	        
	        transaction.commit();
	    } catch (Exception e) {
	        if (transaction != null) transaction.rollback();
	        e.printStackTrace();
	    }
	}

	public void deleteDistribuidor(int id) {
		Transaction transaction = null;
		Distribuidor d = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			d = session.find(Distribuidor.class, id);
			session.remove(d);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}

	public Distribuidor selectDistribuidorById(int id) {
		Transaction transaction = null;
		Distribuidor d = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			d = session.find(Distribuidor.class, id);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return d;
	}

	public List<Distribuidor> selectAllDistribuidor() {
		Transaction transaction = null;
		List<Distribuidor> distribuidor = null;
		try (Session session = ProductoConection.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			distribuidor = session.createQuery("from Distribuidor", Distribuidor.class).getResultList();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
		return distribuidor;
	}
	
	public List<Distribuidor> selectAllDistribuidorWithProducts() {
	    try (Session session = ProductoConection.getSessionFactory().openSession()) {
	        // "JOIN FETCH" obliga a traer la colección en la misma consulta
	        return session.createQuery("SELECT DISTINCT d FROM Distribuidor d LEFT JOIN FETCH d.productos", Distribuidor.class)
	                      .getResultList();
	    }
	}
	
	public void deleteRelacion(int idDist, int idProd) {
	    Transaction transaction = null;
	    try (Session session = ProductoConection.getSessionFactory().openSession()) {
	        transaction = session.beginTransaction();
	        
	        // 1. Ejecutamos el borrado físico directo en la tabla intermedia
	        // Usamos SQL Nativo para saltarnos las restricciones de las colecciones de Hibernate
	        String sql = "DELETE FROM PxD WHERE idDist = :idDist AND idProd = :idProd";
	        
	        int filasAfectadas = session.createNativeQuery(sql)
	                .setParameter("idDist", idDist)
	                .setParameter("idProd", idProd)
	                .executeUpdate();

	        // 2. CRUCIAL: Romper los vínculos en los objetos si estuvieran cargados en esta sesión
	        Distribuidor dist = session.get(Distribuidor.class, idDist);
	        Producto prod = session.get(Producto.class, idProd);
	        
	        if (dist != null && prod != null) {
	            dist.getProductos().remove(prod);
	            prod.getDistribuidores().remove(dist);
	        }

	        // 3. LIMPIEZA TOTAL de la caché
	        // Esto obliga a que cualquier consulta posterior vaya a la DB y no a la memoria
	        session.flush(); 
	        session.clear(); 
	        ProductoConection.getSessionFactory().getCache().evictAllRegions();

	        transaction.commit();
	        System.out.println("Relación eliminada. Filas afectadas: " + filasAfectadas);
	        
	    } catch (Exception e) {
	        if (transaction != null) transaction.rollback();
	        e.printStackTrace();
	    }
	}
	
	public void eliminarRelacionFisica(int idDist, int idProd) {
	    Transaction transaction = null;
	    try (Session session = ProductoConection.getSessionFactory().openSession()) {
	        transaction = session.beginTransaction();

	        // Usamos comillas si el nombre de la tabla tiene mayúsculas para asegurar compatibilidad
	        String sql = "DELETE FROM PxD WHERE idDist = :idDist AND idProd = :idProd";
	        
	        int filasAfec = session.createNativeQuery(sql)
	               .setParameter("idDist", idDist)
	               .setParameter("idProd", idProd)
	               .executeUpdate();

	        transaction.commit(); // IMPORTANTE: Sin esto, los cambios no se guardan permanentemente
	        
	        // Forzamos a Hibernate a olvidar los datos viejos que tiene en memoria
	        ProductoConection.getSessionFactory().getCache().evictAllRegions();
	        
	        System.out.println("Filas borradas físicamente: " + filasAfec);
	    } catch (Exception e) {
	        if (transaction != null) transaction.rollback();
	        e.printStackTrace();
	    }
	}
}
