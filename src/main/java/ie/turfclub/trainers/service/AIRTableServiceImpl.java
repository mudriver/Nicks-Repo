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
}
