package com.gc.controller;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gc.model.Address;
import com.gc.model.Person;
import com.gc.model.Product;
import com.gc.util.HibernateUtil;

/*
 * author: Antonella Solomon
 *
 */

@Controller
public class HomeController {

	@RequestMapping("/welco")
	public ModelAndView helloWorld() {
		// adding to DB
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();

		Person antonella = new Person("Antonella", "Solomon");
		Person merc = new Person("Merc", "Tedder");

		Address valley = new Address("Antonellas Building", "San Francisco", "11111");
		Address newyork = new Address("Trump Tower", "New York", "22222");
		Address chicago = new Address("Trump Tower", "Chicago", "33333");

		antonella.getAddresses().add(valley);
		merc.getAddresses().add(newyork);
		merc.getAddresses().add(chicago);

		System.out.println("Creating Person: " + antonella.getFirstName());
		session.persist(antonella);
		System.out.println("Creating Person: " + merc.getFirstName());
		session.persist(merc);

		session.getTransaction().commit();
		session.close();
		return new ModelAndView("welcome", "message",
				"Creating Person: " + antonella.getFirstName() + "<br> " + "Creating Person: " + merc.getFirstName());

	}

	@RequestMapping("/showresults")
	public ModelAndView listResults(Model model) {

		/*
		 * The SessionFactory is a factory of session and client of Connection Provider.
		 * It holds second level cache (optional) of data
		 */
		SessionFactory sf = HibernateUtil.getSessionFactory();
		/*
		 * A Session is used to get a physical connection with a database. The Session
		 * object is lightweight and designed to be instantiated each time an
		 * interaction is needed with the database. Persistent objects are saved and
		 * retrieved through a Session object.
		 * 
		 * The session objects should not be kept open for a long time because they are
		 * not usually thread safe and they should be created and destroyed them as
		 * needed. The main function of the Session is to offer, create, read, and
		 * delete operations for instances of mapped entity classes.
		 */
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Criteria crit = session.createCriteria(Address.class); // the strikethrough indicates this is deprecated
		ArrayList<Address> list = (ArrayList<Address>) crit.list();
		tx.commit();
		session.close();

		return new ModelAndView("listAll", "cList", list);
	}
	@RequestMapping("/welcome")
	public ModelAndView helloWorld2() {

		Configuration config = new Configuration().configure("hibernate.cfg.xml");
		/*
		 * The SessionFactory is a factory of session and client of Connection Provider.
		 * It holds second level cache (optional) of data
		 */
		SessionFactory sessionFactory = config.buildSessionFactory();
		//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		/*
		 * A Session is used to get a physical connection with a database. The Session
		 * object is lightweight and designed to be instantiated each time an
		 * interaction is needed with the database. Persistent objects are saved and
		 * retrieved through a Session object.
		 * 
		 * The session objects should not be kept open for a long time because they are
		 * not usually thread safe and they should be created and destroyed them as
		 * needed. The main function of the Session is to offer, create, read, and
		 * delete operations for instances of mapped entity classes.
		 */
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Criteria crit = session.createCriteria(Product.class); // the strikethrough indicates this is deprecated
		ArrayList<Product> list = (ArrayList<Product>) crit.list(); 
		tx.commit();
		session.close();

		return new ModelAndView("welcome", "pList", list);
	}

	@RequestMapping("/searchbyproduct")
	public ModelAndView searchCity(@RequestParam("product") String prod) {

		Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
//
		SessionFactory sessionFact = cfg.buildSessionFactory();
		//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session selectCustomers = sessionFact.openSession();

		selectCustomers.beginTransaction();

		// Criteria is used to create the query
		Criteria c = selectCustomers.createCriteria(Product.class);

		// adding additional search criteria to the query
		// the first parameter is referencing the table column that we want to search
		// against but specifically referencing the variable from our code if wrong case is used it will throw an error
		c.add(Restrictions.like("code", "%" + prod + "%"));

		ArrayList<Product> productList = (ArrayList<Product>) c.list();
		sessionFact.close();

		return new ModelAndView("welcome", "pList", productList);
	}

	// mapping needed to send to a form to add a new product
	@RequestMapping("/getnewprod")
	public String newProduct() {
		return "addProdForm";
	}

	@RequestMapping("/addnewproduct")
	public String addNewCustomer(@RequestParam("code") String code, @RequestParam("description") String desc,
			@RequestParam("listPrice") double price, Model model) {
//
		Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
//
		SessionFactory sessionFact = cfg.buildSessionFactory();
		//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

		Session session = sessionFact.openSession();

		Transaction tx = session.beginTransaction();

		Product newProduct = new Product();

		newProduct.setCode(code);
		newProduct.setDescription(desc);
		newProduct.setListPrice(price);

		session.save(newProduct);
		tx.commit();
		session.close();

		model.addAttribute("newStuff", newProduct);
		
		return "addprodsuccess";
	}

	@RequestMapping("/delete")
	public ModelAndView deleteCustomer(@RequestParam("id") int id) {

		// temp Object will store info for the object we want to delete
		Product temp = new Product();
		temp.setProductID(id);

		Configuration cfg = new Configuration().configure("hibernate.cfg.xml");

		SessionFactory sessionFact = cfg.buildSessionFactory();
		//SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

		Session codes = sessionFact.openSession();

		codes.beginTransaction();

		codes.delete(temp); // delete the object from the list

		codes.getTransaction().commit(); // delete the row from the database table

		ArrayList<Product> prodList = getAllProducts();
		sessionFact.close();

		return new ModelAndView("welcome", "pList", prodList);
	}

	// this mapping is needed to pass the parameter as a hidden field to the update
	// form
	@RequestMapping("/update")
	public ModelAndView updateForm(@RequestParam("id") int id) {

		return new ModelAndView("updateprodform", "productID", id);
	}

	@RequestMapping("/updateproduct")
	public ModelAndView updateProduct(@RequestParam("id") int id, @RequestParam("code") String code,
			@RequestParam("description") String desc, @RequestParam("listPrice") double price) {

		// temp Object will store info for the object we want to update
		Product temp = new Product();
		temp.setProductID(id);
		temp.setCode(code);
		temp.setDescription(desc);
		temp.setListPrice(price);

//		Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
//
//		SessionFactory sessionFact = cfg.buildSessionFactory();
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

		Session codes = sessionFactory.openSession();

		codes.beginTransaction();

		codes.update(temp); // update the object from the list

		codes.getTransaction().commit(); // update the row from the database table

		ArrayList<Product> prodList = getAllProducts();
		sessionFactory.close();

		return new ModelAndView("welcome", "pList", prodList);
	}

	private ArrayList<Product> getAllProducts() {
		//Configuration config = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Criteria crit = session.createCriteria(Product.class);
		ArrayList<Product> list = (ArrayList<Product>) crit.list();
		tx.commit();
		session.close();
		return list;
	}

}


