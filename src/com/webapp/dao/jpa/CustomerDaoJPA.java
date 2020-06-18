package com.webapp.dao.jpa;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.webapp.bean.Customer;
import com.webapp.dao.DaoException;
import com.webapp.dao.ICustomerDAO;

public class CustomerDaoJPA implements ICustomerDAO {

	private static ICustomerDAO instance;
	private EntityManagerFactory emf;
	
	public static ICustomerDAO getInstance() {
		if(instance == null)
			instance = new CustomerDaoJPA();
		return instance;
	}
	
	private CustomerDaoJPA() {
		emf = Persistence.createEntityManagerFactory("WebAppPU");
	}
	
	@Override
	public Collection<Customer> selectAllCustomers() throws DaoException {
		EntityManager em = emf.createEntityManager();	
		try {
			//Creo la query di select che mi ritorna oggetti di tipo Region
			String customerSelect = "SELECT c FROM Customer c";
			
			//Creo la TypedQuery usando JPQL
			TypedQuery<Customer> query = em.createQuery(customerSelect, Customer.class);

			//Eseguo la query di select con ritorno degli oggetti
			Collection<Customer> customers = query.getResultList();
			
			//ritorno i customers
			return customers;
		} catch (Exception e) {
			//Se si verifica una qualsiasi eccezione rilancio una DaoException
			throw new DaoException(e.getMessage());
		} finally {
			//infine, a prescindere dall'esito della transazione, chiudo l'entity manager
			em.close();
		}
	}

	@Override
	public void insertCustomer(Customer c) throws DaoException {
		//Creo un entity manager per gestire la query
		EntityManager em = emf.createEntityManager();
		
		//Transazione che permette di eseguire una query di ADD
		EntityTransaction transaction = null;
		try{
			transaction = em.getTransaction();
			transaction.begin();
			
			//Eseguo l'inserimento e committo la transazione
			em.persist(c);
			transaction.commit();
		} catch (Exception e) {
			//Se si verifica una qualsiasi eccezione faccio il rollback e rilancio DaoException
			if(transaction != null)
				transaction.rollback();
			throw new DaoException(e.getMessage());
		} finally {
			em.close();
		}	
	}

	@Override
	public void deleteCustomer(Integer id) throws DaoException{
		//Creo un entity manager e transaction per gestire la query
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = null;
		
		try{
			transaction = em.getTransaction();
			transaction.begin();
			
			//Troppo il customer con l'id ricercato e lo rimuovo
			Customer c = em.find(Customer.class, id);
			em.remove(c);
			transaction.commit();
		} catch (Exception e) {
			//Se si verifica una qualsiasi eccezione faccio il rollback e rilancio DaoException
			if(transaction != null)
				transaction.rollback();
			throw new DaoException(e.getMessage());
		} finally {
			em.close();
		}	
	}

}
