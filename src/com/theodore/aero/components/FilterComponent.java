package com.theodore.aero.components;

import com.theodore.aero.core.Aero;
import com.theodore.aero.graphics.filters.Filter;

public class FilterComponent extends GameComponent {

    private Filter filter;

    public FilterComponent(Filter filter) {
        this.filter = filter;
    }

    @Override
    public void addToEngine() {
        Aero.graphics.addFilter(filter);
    }

}
