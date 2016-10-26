package ie.turfclub.vetReports.service;



import ie.turfclub.vetReports.model.VetreportAlert;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VetReportsAlertServiceImpl implements VetReportsAlertService {


	   @Autowired
	    private SessionFactory sessionFactory;
	 
	    private Session getCurrentSession() {
	        return sessionFactory.getCurrentSession();
	    }
	
	

	@Override
	public void putOnAlert(VetreportAlert alert) {
		getCurrentSession().saveOrUpdate(alert);

	}



	@Override
	public List<VetreportAlert> getAlert() {
		Query query = getCurrentSession().createQuery("from VetreportAlert");
		List<VetreportAlert> list = (List<VetreportAlert>)query.list();
		return list;
	}



	@Override
	public void offAlert(VetreportAlert offAlert) {
		getCurrentSession().delete(offAlert);
		
	}
}
