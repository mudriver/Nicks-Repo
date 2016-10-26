package ie.turfclub.formatters;

import ie.turfclub.inspections.model.InspectionsCategories;
import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

public class InspectionsCategoriesFormatter implements Formatter<InspectionsCategories> {

		@Override
		public String print(InspectionsCategories cat, Locale locale) {
			
			return cat.getCatId().toString();
		}

		@Override
		public InspectionsCategories parse(String id, Locale locale) throws ParseException {
			System.out.println("parse official:" + id);
			InspectionsCategories cat = new InspectionsCategories();
			cat.setCatId(Integer.parseInt(id));
			return cat;
		}
	}
