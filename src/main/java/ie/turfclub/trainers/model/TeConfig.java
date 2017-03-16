package ie.turfclub.trainers.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "te_config", catalog = "trainers")
public class TeConfig {
	private int year;


	 @Id
		@GeneratedValue(strategy = IDENTITY)
		@Column(name = "config_year", unique = true, nullable = false)
	    public int getYear() {
			return year;
		}
		public void setYear(int year) {
			this.year = year;
			System.out.println(year);
		}

}

