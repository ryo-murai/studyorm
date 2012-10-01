package studyorm.jpa.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TORDER")
public class Order implements Serializable {

	private static final long serialVersionUID = -8777824445811362213L;

	public Order() {
	}

	public Order(String item, Date date, Customer customer) {
		this.item = item;
		this.date = date;
		this.customer = customer;
		this.customerId = customer.getId();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ORDER_ID")
	private Long id;

	private String item;

	@Temporal(TemporalType.DATE)
	private Date date;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "CUST_ID", nullable = false, updatable = false, insertable = false)
	private Customer customer;

	@Column(name = "CUST_ID")
	private Long customerId;

	public Long getId() {
		return id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
