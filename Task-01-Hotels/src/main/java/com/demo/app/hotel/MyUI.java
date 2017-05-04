package com.demo.app.hotel;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

@Theme("mytheme")
public class MyUI extends UI {

	private HotelService hotelService = HotelService.getInstance();
	
	private TextField filterTextByName;
	private TextField filterTextByAddress;
	private Button clearFilterByNameBtn;
	private Button clearFilterByAdressBtn;
	private Grid<Hotel> gridHotel;
	private Button addHotelBtn;

	private VerticalLayout main;
	private HorizontalLayout toolbar;
	private HorizontalLayout filters;
	private CssLayout filterByName;
	private CssLayout filterByAddress;
	
	private HotelForm form;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		initComponents();
		initLayouts();
		
		filterByName.addComponents(filterTextByName, clearFilterByNameBtn);
		filterByAddress.addComponents(filterTextByAddress, clearFilterByAdressBtn);
		filters.addComponents(filterByName, filterByAddress);
		toolbar.addComponents(filters, addHotelBtn);
		main.addComponents(toolbar, gridHotel, form);
		
		updateList();
		setContent(main);
	}

	private void initLayouts() {
		main = new VerticalLayout();

		filterByName = new CssLayout();
		filterByName.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		
		filterByAddress = new CssLayout();
		filterByAddress.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		
		toolbar = new HorizontalLayout();
		
		filters = new HorizontalLayout();
		
	}

	private void initComponents() {
		filterTextByName = new TextField();
		filterTextByName.setPlaceholder("Filter by name");
		filterTextByName.addValueChangeListener(e -> updateList());
		filterTextByName.setValueChangeMode(ValueChangeMode.LAZY);

		filterTextByAddress = new TextField();
		filterTextByAddress.setPlaceholder("Filter by address");
		filterTextByAddress.addValueChangeListener(e -> updateList());
		filterTextByAddress.setValueChangeMode(ValueChangeMode.LAZY);

		clearFilterByNameBtn = new Button();
		clearFilterByNameBtn.setDescription("Clear filter by name");
		clearFilterByNameBtn.setIcon(VaadinIcons.CLOSE);
		clearFilterByNameBtn.addClickListener(e -> filterTextByName.clear());

		clearFilterByAdressBtn = new Button();
		clearFilterByAdressBtn.setDescription("Clear filter by address");
		clearFilterByAdressBtn.setIcon(VaadinIcons.CLOSE);
		clearFilterByAdressBtn.addClickListener(e -> filterTextByAddress.clear());

		gridHotel = new Grid<>(Hotel.class);
		gridHotel.setColumns("name", "address", "rating", "operatesFrom", "category");
		gridHotel.addColumn(hotel -> "<a href='" + hotel.getUrl() + "' target='_blank'>link</a>", new HtmlRenderer()).setCaption("url");
		gridHotel.setSizeFull();
		gridHotel.asSingleSelect().addValueChangeListener(e -> {
			if (e.getValue() == null) {
				form.setVisible(false);
			} else {
				form.setHotel(e.getValue());
			}
		});
		
		addHotelBtn = new Button("Add new hotel");
		addHotelBtn.addClickListener(e -> {
			gridHotel.asSingleSelect().clear();
			form.setHotel(new Hotel());
		});
		
		form = new HotelForm(this);
		form.setVisible(false);
		
	}

	public void updateList() {
		List<Hotel> hotels = hotelService.findAll(filterTextByName.getValue(), filterTextByAddress.getValue());
		gridHotel.setItems(hotels);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
