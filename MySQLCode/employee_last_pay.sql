update te_employees as te   
	join (select c.cards_employee_id as id, e.last_pay as last_pay from
         te_cards as c join Employee as e on e.card_number = c.cards_card_number and
         e.card_type=c.cards_card_type) as records on te.employees_employee_id = records.id
		set te.employees_last_year_paid = records.last_pay