package ie.turfclub.formatters;

import ie.turfclub.inspections.model.InspectionsOfficials;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

public class InspectionsOfficialsFormatter implements Formatter<InspectionsOfficials> {

		@Override
		public String print(InspectionsOfficials official, Locale locale) {
			System.out.println("print official: " + official.getOfficialsId().toString());
			return official.getOfficialsId().toString();
		}

		@Override
		public InspectionsOfficials parse(String id, Locale locale) throws ParseException {
			System.out.println("parse official:" + id);
			InspectionsOfficials official = new InspectionsOfficials();
			official.setOfficialsId(Integer.parseInt(id));
			return official;
		}
	}
