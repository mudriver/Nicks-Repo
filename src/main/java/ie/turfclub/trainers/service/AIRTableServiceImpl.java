package ie.turfclub.trainers.service;

import ie.turfclub.trainers.model.AIRTable;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AIRTableServiceImpl implements AIRTableService {

	@Autowired
	private SessionFactory sessionFactory;
	@Resource
	private Environment env;
	
	private String[] header = {"Account", "Surname", "Firstname", "Category Code", "Address1", "Address2", "Address3",
			"Current Trainer Number", "Active", "PostDirect", "HRI Account Holder", "HRI Account Number"};
	
	@Override
	public List<AIRTable> findAll() {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AIRTable.class);
		return criteria.list();
	}
	
	@Override
	public Object loadDataByPagination(int start, int length, int draw) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AIRTable.class);
		criteria.setFirstResult(start*length);
		criteria.setMaxResults(length-1);
		List<AIRTable> data = criteria.list();
		
		criteria = sessionFactory.getCurrentSession().createCriteria(AIRTable.class);
		criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		
		map.put("data", data);
		map.put("draw", draw);
		map.put("recordsTotal", count);
		map.put("recordsFiltered", count);
		
		return map;
	}
	
	@Override
	public String getCSVString() {
		
		StringBuffer buffer = new StringBuffer();
		
		for(int i=0; i<header.length; i++) {
			if(i < (header.length - 1))
				buffer.append(header[i]+",");
			else
				buffer.append(header[i]+"\n");
		}
		
		List<AIRTable> records = sessionFactory.getCurrentSession().createCriteria(AIRTable.class).list();
		if(records != null && records.size() > 0) {
			for (AIRTable rec : records) {
				buffer.append("\""+rec.getAccount()+"\","+
							"\""+rec.getSurname()+"\","+
							"\""+rec.getFirstname()+"\","+
							"\""+rec.getCategoryCode()+"\","+
							"\""+(rec.getAddress1() != null ? rec.getAddress1() : "")+"\","+
							"\""+(rec.getAddress2() != null ? rec.getAddress2() : "")+"\","+
							"\""+(rec.getAddress3() != null ? rec.getAddress3() : "")+"\","+
							"\""+rec.getCurrentTrainerNum()+"\","+
							"\""+rec.isActive()+"\","+
							"\""+rec.isPostDirect()+"\","+
							"\""+rec.isHriAccountHolder()+"\","+
							"\""+rec.getHriAccNum()+"\"\n");
			}
		}
		return buffer.toString();
	}
}
