package hu.moni.model.dto;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {

    private Long id;
    private String title;
    private String href;
    private String icon;
    private Boolean external = false;
    private List<MenuItem> child;

    public MenuItem() {
        this.child = new ArrayList<>();
    }

    public MenuItem(String title, String href, String icon) {
        this.title = title;
        this.href = href;
        this.icon = icon;
        this.child = new ArrayList<>();
    }

    public MenuItem(Long id, String title, String href, String icon) {
        this.id = id;
        this.title = title;
        this.href = href;
        this.icon = icon;
        this.child = new ArrayList<>();
    }

    public MenuItem(Long id, String title, String href, String icon, Boolean external) {
        this.id = id;
        this.title = title;
        this.href = href;
        this.icon = icon;
        this.external = external;
        this.child = new ArrayList<>();
    }
}
