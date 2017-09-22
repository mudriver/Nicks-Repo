update te_employment_history hi join te_employees emp join te_cards ca
	on hi.eh_employee_id=emp.employees_employee_id and emp.employees_employee_id=ca.cards_employee_id
    set hi.card_type=ca.cards_card_type;