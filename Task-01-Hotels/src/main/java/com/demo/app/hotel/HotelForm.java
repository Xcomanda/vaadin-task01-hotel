package com.demo.app.hotel;

import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class HotelForm extends FormLayout {
	private TextField name = new TextField("Name");
	private TextField address = new TextField("Address");
	private TextField rating = new TextField("Rating");
	private DateField operatesFrom = new DateField("Operates from");
	private NativeSelect<HotelCategory> category = new NativeSelect<>("Category");
	private TextField url = new TextField("URL");
	private TextArea description = new TextArea("Description");

	private Button save = new Button("Save");
	private Button delete = new Button("Delete");

	private HotelService hotelService = HotelService.getInstance();
	private Hotel hotel;
	private MyUI myUI;
	private Binder<Hotel> binder = new Binder<>(Hotel.class);

	public HotelForm(MyUI myUI) {
		this.myUI = myUI;
		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(save, delete);
		addComponents(name, address, rating, operatesFrom, category, url, description, buttons);
		category.setItems(HotelCategory.values());
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		binder.bindInstanceFields(this);
		save.addClickListener(e -> save());
		delete.addClickListener(e -> delete());
		delete.setStyleName(ValoTheme.BUTTON_DANGER);
		
		name.setWidth("600px");
		address.setWidth("600px");
		rating.setWidth("50px");
		operatesFrom.setWidth("200px");
		category.setWidth("200px");
		url.setWidth("600px");
		description.setWidth("600px");
		description.setHeight("70px");
	}
	
	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
		binder.setBean(hotel);

		delete.setVisible(hotel.isPersisted());
		setVisible(true);
		name.selectAll();
	}
	
	private void delete() {
		hotelService.delete(hotel);
		myUI.updateList();
		setVisible(false);
	}

	private void save() {
		hotelService.save(hotel);
		myUI.updateList();
		setVisible(false);
	}
}
